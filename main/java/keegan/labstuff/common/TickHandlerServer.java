package keegan.labstuff.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;

import com.google.common.collect.Lists;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.*;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.blocks.BlockUnlitTorch;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.network.*;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import keegan.labstuff.world.IOrbitDimension;
import keegan.labstuff.wrappers.*;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;

public class TickHandlerServer
{
    private static Map<Integer, CopyOnWriteArrayList<ScheduledBlockChange>> scheduledBlockChanges = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<ScheduledBlockChange>>();
    private static Map<Integer, CopyOnWriteArrayList<BlockVec3>> scheduledTorchUpdates = new ConcurrentHashMap<Integer, CopyOnWriteArrayList<BlockVec3>>();
    private static Map<Integer, Set<BlockPos>> edgeChecks = new TreeMap<Integer, Set<BlockPos>>();
    private static LinkedList<EnergyNetwork> networkTicks = new LinkedList<EnergyNetwork>();
    public static ArrayList<EntityPlayerMP> playersRequestingMapData = Lists.newArrayList();
    private static long tickCount;
    private static CopyOnWriteArrayList<ScheduledDimensionChange> scheduledDimensionChanges = new CopyOnWriteArrayList<ScheduledDimensionChange>();
    private final int MAX_BLOCKS_PER_TICK = 50000;
    private static List<LabstuffPacketHandler> packetHandlers = Lists.newCopyOnWriteArrayList();

    public static void addPacketHandler(LabstuffPacketHandler handler)
    {
        TickHandlerServer.packetHandlers.add(handler);
    }

    @SubscribeEvent
    public void worldUnloadEvent(WorldEvent.Unload event)
    {
        for (LabstuffPacketHandler packetHandler : packetHandlers)
        {
            packetHandler.unload(event.getWorld());
        }
    }

    public static void restart()
    {
        TickHandlerServer.scheduledBlockChanges.clear();
        TickHandlerServer.scheduledTorchUpdates.clear();
        TickHandlerServer.edgeChecks.clear();
        TickHandlerServer.networkTicks.clear();
        TickHandlerServer.playersRequestingMapData.clear();
        TickHandlerServer.networkTicks.clear();

        TickHandlerServer.tickCount = 0L;
        MapUtil.reset();
    }


    public static void scheduleNewBlockChange(int dimID, ScheduledBlockChange change)
    {
        CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(dimID);

        if (changeList == null)
        {
            changeList = new CopyOnWriteArrayList<ScheduledBlockChange>();
        }

        changeList.add(change);
        TickHandlerServer.scheduledBlockChanges.put(dimID, changeList);
    }

    /**
     * Only use this for AIR blocks (any type of BlockAir)
     *
     * @param dimID
     * @param changeAdd List of <ScheduledBlockChange>
     */
    public static void scheduleNewBlockChange(int dimID, List<ScheduledBlockChange> changeAdd)
    {
        CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(dimID);

        if (changeList == null)
        {
            changeList = new CopyOnWriteArrayList<ScheduledBlockChange>();
        }

        changeList.addAll(changeAdd);
        TickHandlerServer.scheduledBlockChanges.put(dimID, changeList);
    }

    public static void scheduleNewDimensionChange(ScheduledDimensionChange change)
    {
        scheduledDimensionChanges.add(change);
    }

    public static void scheduleNewTorchUpdate(int dimID, List<BlockVec3> torches)
    {
        CopyOnWriteArrayList<BlockVec3> updateList = TickHandlerServer.scheduledTorchUpdates.get(dimID);

        if (updateList == null)
        {
            updateList = new CopyOnWriteArrayList<BlockVec3>();
        }

        updateList.addAll(torches);
        TickHandlerServer.scheduledTorchUpdates.put(dimID, updateList);
    }

    public static void scheduleNewEdgeCheck(int dimID, BlockPos edgeBlock)
    {
        Set<BlockPos> updateList = TickHandlerServer.edgeChecks.get(dimID);

        if (updateList == null)
        {
            updateList = new HashSet<BlockPos>();
        }

        updateList.add(edgeBlock);
        TickHandlerServer.edgeChecks.put(dimID, updateList);
    }

    public static boolean scheduledForChange(int dimID, BlockPos test)
    {
        CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(dimID);

        if (changeList != null)
        {
            for (ScheduledBlockChange change : changeList)
            {
                if (test.equals(change.getChangePosition()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static void scheduleNetworkTick(EnergyNetwork grid)
    {
        TickHandlerServer.networkTicks.add(grid);
    }

    public static void removeNetworkTick(EnergyNetwork grid)
    {
        TickHandlerServer.networkTicks.remove(grid);
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event)
    {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        //Prevent issues when clients switch to LAN servers
        if (server == null)
        {
            return;
        }

        if (event.phase == Phase.START)
        {
            for (ScheduledDimensionChange change : TickHandlerServer.scheduledDimensionChanges)
            {
                try
                {
                    LSPlayerStats stats = LSPlayerStats.get(change.getPlayer());
                    final WorldProvider provider = WorldUtil.getProviderForNameServer(change.getDimensionName());
                    final Integer dim = LabStuffUtils.getDimensionID(provider);
                    LSLog.info("Found matching world (" + dim.toString() + ") for name: " + change.getDimensionName());

                    if (change.getPlayer().worldObj instanceof WorldServer)
                    {
                        final WorldServer world = (WorldServer) change.getPlayer().worldObj;

                        WorldUtil.transferEntityToDimension(change.getPlayer(), dim, world);
                    }

                    stats.setTeleportCooldown(10);
                    LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_CLOSE_GUI, LabStuffUtils.getDimensionID(change.getPlayer().worldObj), new Object[] {}), change.getPlayer());
                }
                catch (Exception e)
                {
                    LSLog.severe("Error occurred when attempting to transfer entity to dimension: " + change.getDimensionName());
                    e.printStackTrace();
                }
            }

            TickHandlerServer.scheduledDimensionChanges.clear();

            if (MapUtil.calculatingMap.get())
            {
                MapUtil.BiomeMapNextTick_MultiThreaded();
            }
            else if (!MapUtil.doneOverworldTexture)
            {
                MapUtil.makeOverworldTexture();
            }

            TileEntityOxygenSealer.onServerTick();
            if (tickCount % 20 == 0)
            {
                if (!playersRequestingMapData.isEmpty())
                {
                    File baseFolder = new File(server.worldServerForDimension(0).getChunkSaveLocation(), "galacticraft/overworldMap");
                    if (!baseFolder.exists() && !baseFolder.mkdirs())
                    {

                        LSLog.severe("Base folder(s) could not be created: " + baseFolder.getAbsolutePath());
                    }
                    else
                    {
                        ArrayList<EntityPlayerMP> copy = new ArrayList<EntityPlayerMP>(playersRequestingMapData);
                        BufferedImage reusable = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
                        for (EntityPlayerMP playerMP : copy)
                        {
                            LSPlayerStats stats = LSPlayerStats.get(playerMP);
                            MapUtil.makeVanillaMap(playerMP.dimension, (int) Math.floor(stats.getCoordsTeleportedFromZ()) >> 4, (int) Math.floor(stats.getCoordsTeleportedFromZ()) >> 4, baseFolder, reusable);
                        }
                        playersRequestingMapData.removeAll(copy);
                    }
                }
            }

            TickHandlerServer.tickCount++;
        }
    }

    private static Set<Integer> worldsNeedingUpdate = new HashSet<Integer>();

    public static void markWorldNeedsUpdate(int dimension)
    {
        worldsNeedingUpdate.add(dimension);
    }

    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
        if (event.phase == Phase.START)
        {
            final WorldServer world = (WorldServer) event.world;

            CopyOnWriteArrayList<ScheduledBlockChange> changeList = TickHandlerServer.scheduledBlockChanges.get(LabStuffUtils.getDimensionID(world));

            if (changeList != null && !changeList.isEmpty())
            {
                int blockCount = 0;
                int blockCountMax = Math.max(this.MAX_BLOCKS_PER_TICK, changeList.size() / 4);
                List<ScheduledBlockChange> newList = new ArrayList<ScheduledBlockChange>(Math.max(0, changeList.size() - blockCountMax));

                for (ScheduledBlockChange change : changeList)
                {
                    if (++blockCount > blockCountMax)
                    {
                        newList.add(change);
                    }
                    else
                    {
                        if (change != null)
                        {
                            BlockPos changePosition = change.getChangePosition();
                            Block block = world.getBlockState(changePosition).getBlock();
                            //Only replace blocks of type BlockAir or fire - this is to prevent accidents where other mods have moved blocks
                            if (changePosition != null && (block instanceof BlockAir || block == Blocks.FIRE))
                            {
                                world.setBlockState(changePosition, change.getChangeState(), change.getChangeUpdateFlag());
                            }
                        }
                    }
                }

                changeList.clear();
                TickHandlerServer.scheduledBlockChanges.remove(LabStuffUtils.getDimensionID(world));
                if (newList.size() > 0)
                {
                    TickHandlerServer.scheduledBlockChanges.put(LabStuffUtils.getDimensionID(world), new CopyOnWriteArrayList<ScheduledBlockChange>(newList));
                }
            }

            CopyOnWriteArrayList<BlockVec3> torchList = TickHandlerServer.scheduledTorchUpdates.get(LabStuffUtils.getDimensionID(world));

            if (torchList != null && !torchList.isEmpty())
            {
                for (BlockVec3 torch : torchList)
                {
                    if (torch != null)
                    {
                        BlockPos pos = new BlockPos(torch.x, torch.y, torch.z);
                        Block b = world.getBlockState(pos).getBlock();
                        if (b instanceof BlockUnlitTorch)
                        {
                            world.scheduleUpdate(pos, b, 2 + world.rand.nextInt(30));
                        }
                    }
                }

                torchList.clear();
                TickHandlerServer.scheduledTorchUpdates.remove(LabStuffUtils.getDimensionID(world));
            }

            if (world.provider instanceof IOrbitDimension)
            {
                final Object[] entityList = world.loadedEntityList.toArray();

                for (final Object o : entityList)
                {
                    if (o instanceof Entity)
                    {
                        final Entity e = (Entity) o;

                        if (e.worldObj.provider instanceof IOrbitDimension)
                        {
                            final IOrbitDimension dimension = (IOrbitDimension) e.worldObj.provider;

                            if (e.posY <= dimension.getYCoordToTeleportToPlanet())
                            {
                                int dim = 0;
                                try
                                {
                                    dim = LabStuffUtils.getDimensionID(WorldUtil.getProviderForNameServer(dimension.getPlanetToOrbit()));
                                }
                                catch (Exception ex)
                                {
                                }

                                WorldUtil.transferEntityToDimension(e, dim, world, false, null);
                            }
                        }
                    }
                }
            }

            int dimensionID = LabStuffUtils.getDimensionID(world);
        }
        else if (event.phase == Phase.END)
        {
            final WorldServer world = (WorldServer) event.world;

            for (LabstuffPacketHandler handler : packetHandlers)
            {
                handler.tick(world);
            }

            int dimID = LabStuffUtils.getDimensionID(world);
            Set<BlockPos> edgesList = TickHandlerServer.edgeChecks.get(dimID);
            final HashSet<BlockPos> checkedThisTick = new HashSet();

            if (edgesList != null && !edgesList.isEmpty())
            {
                List<BlockPos> edgesListCopy = new ArrayList();
                edgesListCopy.addAll(edgesList);
                for (BlockPos edgeBlock : edgesListCopy)
                {
                    if (edgeBlock != null && !checkedThisTick.contains(edgeBlock))
                    {
                        if (TickHandlerServer.scheduledForChange(dimID, edgeBlock))
                        {
                            continue;
                        }

                        ThreadFindSeal done = new ThreadFindSeal(world, edgeBlock, 0, new ArrayList<TileEntityOxygenSealer>());
                        checkedThisTick.addAll(done.checkedAll());
                    }
                }

                TickHandlerServer.edgeChecks.remove(LabStuffUtils.getDimensionID(world));
            }
        }
    }
}