package keegan.labstuff.util;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.*;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import keegan.labstuff.*;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.dimension.*;
import keegan.labstuff.entities.*;
import keegan.labstuff.galaxies.*;
import keegan.labstuff.items.ItemParaChute;
import keegan.labstuff.recipes.SpaceStationRecipe;
import keegan.labstuff.tileentity.TileEntityTelemetry;
import keegan.labstuff.world.*;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.*;


public class WorldUtil
{
    public static HashMap<Integer, Integer> registeredSpaceStations;  //Dimension IDs and providers (providers are -26 or -27 by default)
    public static Map<Integer, String> dimNames = new TreeMap();  //Dimension IDs and provider names
    public static Map<EntityPlayerMP, HashMap<String, Integer>> celestialMapCache = new MapMaker().weakKeys().makeMap();
    public static List<Integer> registeredPlanets;

    public static float getGravityFactor(Entity entity)
    {
        if (entity.worldObj.provider instanceof ILabstuffWorldProvider)
        {
            final ILabstuffWorldProvider customProvider = (ILabstuffWorldProvider) entity.worldObj.provider;
            float returnValue = MathHelper.sqrt_float(0.08F / (0.08F - customProvider.getGravity()));
            if (returnValue > 2.5F)
            {
                returnValue = 2.5F;
            }
            if (returnValue < 0.75F)
            {
                returnValue = 0.75F;
            }
            return returnValue;
        }
        else if (entity instanceof IAntiGrav)
        {
            return 1F;
        }
        else
        {
            return 1F;
        }
    }

    public static Vector3 getWorldColor(World world)
    {
        if (world.provider instanceof WorldProviderVenus)
        {
            return new Vector3(1, 0.8F, 0.6F);
        }

        return new Vector3(1, 1, 1);
    }

    public static WorldProvider getProviderForNameServer(String par1String)
    {
        String nameToFind = par1String;
        if (par1String.contains("$"))
        {
            final String[] twoDimensions = par1String.split("\\$");
            nameToFind = twoDimensions[0];
        }
        if (nameToFind == null)
        {
            return null;
        }

        for (Map.Entry<Integer, String> element : WorldUtil.dimNames.entrySet())
        {
            if (nameToFind.equals(element.getValue()))
            {
                return WorldUtil.getProviderForDimensionServer(element.getKey());
            }
        }

        LSLog.info("Failed to find matching world for '" + par1String + "'");
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static WorldProvider getProviderForNameClient(String par1String)
    {
        String nameToFind = par1String;
        if (par1String.contains("$"))
        {
            final String[] twoDimensions = par1String.split("\\$");
            nameToFind = twoDimensions[0];
        }
        if (nameToFind == null)
        {
            return null;
        }

        for (Map.Entry<Integer, String> element : WorldUtil.dimNames.entrySet())
        {
            if (nameToFind.equals(element.getValue()))
            {
                return WorldUtil.getProviderForDimensionClient(element.getKey());
            }
        }

        LSLog.info("Failed to find matching world for '" + par1String + "'");
        return null;
    }

    public static void initialiseDimensionNames()
    {
    	WorldProvider provider = WorldUtil.getProviderForDimensionServer(ConfigManagerCore.idDimensionOverworld);
    	WorldUtil.dimNames.put(ConfigManagerCore.idDimensionOverworld, provider.getDimensionType().getName());
    }

    /**
     * This will *load* all the GC dimensions which the player has access to (taking account of space station permissions).
     * Loading the dimensions through Forge activates any chunk loaders or forced chunks in that dimension,
     * if the dimension was not previously loaded.  This may place load on the server.
     *
     * @param tier       - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for space station permissions)
     * @return a List of integers which are the dimension IDs
     */
    public static List<Integer> getPossibleDimensionsForSpaceshipTier(int tier, EntityPlayerMP playerBase)
    {
        List<Integer> temp = new ArrayList<Integer>();

        for (Integer element : WorldUtil.registeredPlanets)
        {
            if (element == ConfigManagerCore.idDimensionOverworld)
            {
                continue;
            }
            WorldProvider provider = WorldUtil.getProviderForDimensionServer(element);

            if (provider != null)
            {
                if (provider instanceof ILabstuffWorldProvider)
                {
                    if (((ILabstuffWorldProvider) provider).canSpaceshipTierPass(tier))
                    {
                        temp.add(element);
                    }
                }
                else
                {
                    temp.add(element);
                }
            }
        }

        for (Integer element : WorldUtil.registeredSpaceStations.keySet())
        {
            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, element, null);

            if (data.getAllowedAll() || data.getAllowedPlayers().contains(playerBase.getGameProfile().getName()) || ArrayUtils.contains(playerBase.mcServer.getPlayerList().getOppedPlayerNames(), playerBase.getName()))
            {
                //Satellites always reachable from their own homeworld or from its other satellites
                if (playerBase != null)
                {
                    int currentWorld = playerBase.dimension;
                    //Player is on homeworld
                    if (currentWorld == data.getHomePlanet())
                    {
                        temp.add(element);
                        continue;
                    }
                    if (playerBase.worldObj.provider instanceof IOrbitDimension)
                    {
                        //Player is currently on another space station around the same planet
                        final SpaceStationWorldData dataCurrent = SpaceStationWorldData.getStationData(playerBase.worldObj, playerBase.dimension, null);
                        if (dataCurrent.getHomePlanet() == data.getHomePlanet())
                        {
                            temp.add(element);
                            continue;
                        }
                    }
                }

                //Testing dimension is a satellite, but with a different homeworld - test its tier
                WorldProvider homeWorld = WorldUtil.getProviderForDimensionServer(data.getHomePlanet());

                if (homeWorld != null)
                {
                    if (homeWorld instanceof ILabstuffWorldProvider)
                    {
                        if (((ILabstuffWorldProvider) homeWorld).canSpaceshipTierPass(tier))
                        {
                            temp.add(element);
                        }
                    }
                    else
                    {
                        temp.add(element);
                    }
                }
            }
        }

        return temp;
    }

    public static CelestialBody getReachableCelestialBodiesForDimensionID(int id)
    {
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredSatellites().values());

        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.getReachable())
            {
                if (cBody.getDimensionID() == id)
                {
                    return cBody;
                }
            }
        }

        return null;
    }

    public static CelestialBody getReachableCelestialBodiesForName(String name)
    {
        List<CelestialBody> celestialBodyList = Lists.newArrayList();
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        celestialBodyList.addAll(GalaxyRegistry.getRegisteredSatellites().values());

        for (CelestialBody cBody : celestialBodyList)
        {
            if (cBody.getReachable())
            {
                if (cBody.getName().equals(name))
                {
                    return cBody;
                }
            }
        }

        return null;
    }

    /**
     * CAUTION: this loads the dimension if it is not already loaded.  This can cause
     * server load if used too frequently or with a list of multiple dimensions.
     *
     * @param id
     * @return
     */
    public static WorldProvider getProviderForDimensionServer(int id)
    {
        MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (theServer == null)
        {
            LSLog.debug("Called WorldUtil server side method but FML returned no server - is this a bug?");
            return null;
        }
        World ws = theServer.worldServerForDimension(id);
        if (ws != null)
        {
            return ws.provider;
        }
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static WorldProvider getProviderForDimensionClient(int id)
    {
        World ws = LabStuffClientProxy.mc.theWorld;
        if (ws != null && LabStuffUtils.getDimensionID(ws) == id)
        {
            return ws.provider;
        }
        return DimensionManager.createProviderFor(id);
    }

    /**
     * This will *load* all the GC dimensions which the player has access to (taking account of space station permissions).
     * Loading the dimensions through Forge activates any chunk loaders or forced chunks in that dimension,
     * if the dimension was not previously loaded.  This may place load on the server.
     *
     * @param tier       - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for checking space station permissions)
     * @return a Map of the names of the dimension vs. the dimension IDs
     */
    public static HashMap<String, Integer> getArrayOfPossibleDimensions(int tier, EntityPlayerMP playerBase)
    {
        List<Integer> ids = WorldUtil.getPossibleDimensionsForSpaceshipTier(tier, playerBase);
        final HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (Integer id : ids)
        {
            CelestialBody celestialBody = getReachableCelestialBodiesForDimensionID(id);

            //It's a space station
            if (id > 0 && celestialBody == null)
            {
                celestialBody = LabStuffMain.satelliteSpaceStation;
                //This no longer checks whether a WorldProvider can be created, for performance reasons (that causes the dimension to load unnecessarily at map building stage)
                if (playerBase != null)
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, id, null);
                    map.put(celestialBody.getName() + "$" + data.getOwner() + "$" + data.getSpaceStationName() + "$" + id + "$" + data.getHomePlanet(), id);
                }
            }
            else
            //It's a planet or moon
            {
                if (celestialBody == LabStuffMain.planetOverworld)
                {
                    map.put(celestialBody.getName(), id);
                }
                else
                {
                    WorldProvider provider = WorldUtil.getProviderForDimensionServer(id);
                    if (celestialBody != null && provider != null)
                    {
                        if (provider instanceof ILabstuffWorldProvider && !(provider instanceof IOrbitDimension) || LabStuffUtils.getDimensionID(provider) == 0)
                        {
                            map.put(celestialBody.getName(), LabStuffUtils.getDimensionID(provider));
                        }
                    }
                }
            }
        }

        ArrayList<CelestialBody> cBodyList = new ArrayList<CelestialBody>();
        cBodyList.addAll(GalaxyRegistry.getRegisteredPlanets().values());
        cBodyList.addAll(GalaxyRegistry.getRegisteredMoons().values());

        for (CelestialBody body : cBodyList)
        {
            if (!body.getReachable())
            {
                map.put(body.getLocalizedName() + "*", body.getDimensionID());
            }
        }

        WorldUtil.celestialMapCache.put(playerBase, map);
        return map;
    }

    /**
     * Get the cached version of getArrayOfPossibleDimensions() to reduce server load + unwanted dimension loading
     * The cache will be updated every time the 'proper' version of getArrayOfPossibleDimensions is called.
     *
     * @param tier       - the rocket tier to test
     * @param playerBase - the player who will be riding the rocket (needed for checking space station permissions)
     * @return a Map of the names of the dimension vs. the dimension IDs
     */
    public static HashMap<String, Integer> getArrayOfPossibleDimensionsAgain(int tier, EntityPlayerMP playerBase)
    {
        HashMap<String, Integer> map = WorldUtil.celestialMapCache.get(playerBase);
        if (map != null)
        {
            return map;
        }
        return getArrayOfPossibleDimensions(tier, playerBase);
    }

    public static void unregisterSpaceStations()
    {
        if (WorldUtil.registeredSpaceStations != null)
        {
            for (Integer registeredID : WorldUtil.registeredSpaceStations.keySet())
            {
                DimensionManager.unregisterDimension(registeredID);
            }

            WorldUtil.registeredSpaceStations = null;
        }
    }

    public static void registerSpaceStations(File spaceStationList)
    {
//        WorldUtil.registeredSpaceStations = WorldUtil.getExistingSpaceStationList(spaceStationList);
        WorldUtil.registeredSpaceStations = Maps.newHashMap();
        MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (theServer == null)
        {
            return;
        }

        final File[] var2 = spaceStationList.listFiles();

        if (var2 != null)
        {
            for (File var5 : var2)
            {
                if (var5.getName().contains("spacestation_"))
                {
                    try
                    {
                        // Note: this is kind of a hacky way of doing this, loading the NBT from each space station file
                        // during dimension registration, to find out what each space station's provider IDs are.

                        String name = var5.getName();
                        SpaceStationWorldData worldDataTemp = new SpaceStationWorldData(name);
                        name = name.substring(13, name.length() - 4);
                        int registeredID = Integer.parseInt(name);

                        FileInputStream fileinputstream = new FileInputStream(var5);
                        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
                        fileinputstream.close();
                        worldDataTemp.readFromNBT(nbttagcompound.getCompoundTag("data"));

                        // Search for id in server-defined statically loaded dimensions
                        int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID);

                        int providerID = id >= 0 ? worldDataTemp.getDimensionIdStatic() : worldDataTemp.getDimensionIdDynamic();
                        boolean registrationOK = false;
                        if (!DimensionManager.isDimensionRegistered(registeredID))
                        {
                            DimensionManager.registerDimension(registeredID, WorldUtil.getDimensionTypeById(providerID));
                            registrationOK = true;
                        }
                        else if (LabStuffRegistry.isDimensionTypeIDRegistered(providerID))
                        {
                            registrationOK = DimensionManager.getProviderType(id).getId() == providerID;
                            if (!registrationOK)
                            {
                                try {
                                    Class sponge = Class.forName("org.spongepowered.common.world.WorldManager");
                                    Field dtDI = sponge.getDeclaredField("dimensionTypeByDimensionId");
                                    dtDI.setAccessible(true);
                                    Int2ObjectMap<DimensionType> result = (Int2ObjectMap<DimensionType>) dtDI.get(null);
                                    if (result != null)
                                    {
                                        result.put(id, WorldUtil.getDimensionTypeById(providerID));
                                        LSLog.info("Re-registered dimension type " + providerID);
                                    }
                                    registrationOK = true;
                                } catch (ClassNotFoundException ignore) { }
                                catch (Exception e) { e.printStackTrace(); }
                            }
                        }
                        if (registrationOK)
                        {
                            WorldUtil.registeredSpaceStations.put(registeredID, providerID);
                            if (id >= 0)
                            {
                                theServer.worldServerForDimension(registeredID);
                            }
                            WorldUtil.dimNames.put(registeredID, "Space Station " + registeredID);
                        }
                        else
                        {
                            LSLog.severe("Dimension already registered to another mod: unable to register space station dimension " + registeredID);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }

//        for (Integer registeredID : WorldUtil.registeredSpaceStations)
//        {
//            int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, registeredID);
//
//            if (!DimensionManager.isDimensionRegistered(registeredID))
//            {
//	            if (id >= 0)
//	            {
//	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbitStatic);
//	                theServer.worldServerForDimension(registeredID);
//                }
//	            else
//	            {
//	                DimensionManager.registerDimension(registeredID, ConfigManagerCore.idDimensionOverworldOrbit);
//	            }
//            }
//            else
//            {
//                LSLog.severe("Dimension already registered to another mod: unable to register space station dimension " + registeredID);
//            }
//        }
    }

    /**
     * Call this on FMLServerStartingEvent to register a planet which has a dimension ID.
     * Now returns a boolean to indicate whether registration was successful.
     * <p>
     * NOTE: Planets and Moons dimensions should normally be initialised at server init
     * If you do not do this, you must find your own way to register the dimension in DimensionManager
     * and you must find your own way to include the cached provider name in WorldUtil.dimNames
     * <p>
     * IMPORTANT: LabStuffRegistry.registerDimension() must always be called in parallel with this
     * meaning the CelestialBodies are iterated in the same order when registered there and here.
     * 
     * The defaultID should be 0, and the id should be both a dimension ID and a DimensionType id.
     */
    public static boolean registerPlanet(int id, boolean initialiseDimensionAtServerInit, int defaultID)
    {
        if (WorldUtil.registeredPlanets == null)
        {
            WorldUtil.registeredPlanets = new ArrayList<Integer>();
        }

        if (initialiseDimensionAtServerInit)
        {
            if (!DimensionManager.isDimensionRegistered(id))
            {
                DimensionManager.registerDimension(id, WorldUtil.getDimensionTypeById(id));
                LSLog.info("Registered Dimension: " + id);
                WorldUtil.registeredPlanets.add(id);
            }
            else
            {
                if (DimensionManager.getProviderType(id).getId() == id && LabStuffRegistry.isDimensionTypeIDRegistered(id))
                {
                    LSLog.info("Re-registered dimension: " + id);
                    WorldUtil.registeredPlanets.add(id);
                }
                else
                {
                    LSLog.severe("Dimension already registered: unable to register planet dimension " + id);
                    //Add 0 to the list to preserve the correct order of the other planets (e.g. if server/client initialise with different dimension IDs in configs, the order becomes important for figuring out what is going on)
                    WorldUtil.registeredPlanets.add(defaultID);
                    return false;
                }
            }
            World w = WorldUtil.getWorldForDimensionServer(id);
            WorldUtil.dimNames.put(id, getDimensionName(w.provider));
            return true;
        }

        //Not to be initialised - still add to the registered planets list (for hotloading later?)
        WorldUtil.registeredPlanets.add(id);
        return true;
    }

    public static void unregisterPlanets()
    {
        if (WorldUtil.registeredPlanets != null)
        {
            for (Integer var1 : WorldUtil.registeredPlanets)
            {
                DimensionManager.unregisterDimension(var1);
                LSLog.info("Unregistered Dimension: " + var1);
            }

            WorldUtil.registeredPlanets = null;
        }
        WorldUtil.dimNames.clear();
    }

    public static void registerPlanetClient(Integer dimID, int providerIndex)
    {
        int typeID = LabStuffRegistry.getDimensionTypeID(providerIndex);

        if (typeID == 0)
        {
            LSLog.severe("Server dimension " + dimID + " has no match on client due to earlier registration problem.");
        }
        else if (dimID == 0)
        {
            LSLog.severe("Client dimension " + providerIndex + " has no match on server - probably a server dimension ID conflict problem.");
        }
        else

        {
            if (!WorldUtil.registeredPlanets.contains(dimID))
            {
                WorldUtil.registeredPlanets.add(dimID);
                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(typeID));
            }
            else
            {
                LSLog.severe("Dimension already registered to another mod: unable to register planet dimension " + dimID);
            }
        }
    }

    public static SpaceStationWorldData bindSpaceStationToNewDimension(World world, EntityPlayerMP player, int homePlanetID)
    {
        int dynamicProviderID = -1;
        int staticProviderID = -1;
        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getParentPlanet().getDimensionID() == homePlanetID)
            {
                dynamicProviderID = satellite.getDimensionID();
                staticProviderID = satellite.getDimensionIdStatic();
            }
        }
        if (dynamicProviderID == -1 || staticProviderID == -1)
        {
            throw new RuntimeException("Space station being bound on bad provider IDs!");
        }
        int newID = DimensionManager.getNextFreeDimId();
        SpaceStationWorldData data = WorldUtil.createSpaceStation(world, newID, homePlanetID, dynamicProviderID, staticProviderID, player);
        dimNames.put(newID, "Space Station " + newID);
        LSPlayerStats stats = LSPlayerStats.get(player);
        stats.getSpaceStationDimensionData().put(homePlanetID, newID);
        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), player);
        return data;
    }

    public static SpaceStationWorldData createSpaceStation(World world, int dimID, int homePlanetID, int dynamicProviderID, int staticProviderID, EntityPlayerMP player)
    {
        int id = Arrays.binarySearch(ConfigManagerCore.staticLoadDimensions, dimID);

        if (!DimensionManager.isDimensionRegistered(dimID))
        {
            if (id >= 0)
            {
                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(staticProviderID));
                WorldUtil.registeredSpaceStations.put(dimID, staticProviderID);
            }
            else
            {
                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(dynamicProviderID));
                WorldUtil.registeredSpaceStations.put(dimID, dynamicProviderID);
            }
        }
        else
        {
            LSLog.severe("Dimension already registered to another mod: unable to register space station dimension " + dimID);
        }

        for (WorldServer server : world.getMinecraftServer().worldServers)
        {
            LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_LIST, LabStuffUtils.getDimensionID(server), WorldUtil.getSpaceStationList()), LabStuffUtils.getDimensionID(server));
        }
        return SpaceStationWorldData.getStationData(world, dimID, homePlanetID, dynamicProviderID, staticProviderID, player);
    }

    public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world)
    {
        return WorldUtil.transferEntityToDimension(entity, dimensionID, world, true, null);
    }

    /**
     * It is not necessary to use entity.setDead() following calling this method.
     * If the entity left the old world it was in, it will now automatically be removed from that old world before the next update tick.
     * (See WorldUtil.removeEntityFromWorld())
     */
    public static Entity transferEntityToDimension(Entity entity, int dimensionID, WorldServer world, boolean transferInv, EntityAutoRocket ridingRocket)
    {
        if (!world.isRemote)
        {
            //LabStuffMain.packetPipeline.sendToAll(new PacketSimple(EnumSimplePacket.C_UPDATE_PLANETS_LIST, WorldUtil.getPlanetList()));

            MinecraftServer mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();

            if (mcServer != null)
            {
                final WorldServer var6 = mcServer.worldServerForDimension(dimensionID);

                if (var6 == null)
                {
                    System.err.println("Cannot Transfer Entity to Dimension: Could not get World for Dimension " + dimensionID);
                    return null;
                }

                final ITeleportType type = LabStuffRegistry.getTeleportTypeForDimension(var6.provider.getClass());

                if (type != null)
                {
                    return WorldUtil.teleportEntity(var6, entity, dimensionID, type, transferInv, ridingRocket);
                }
            }
        }

        return null;
    }

    private static Entity teleportEntity(World worldNew, Entity entity, int dimID, ITeleportType type, boolean transferInv, EntityAutoRocket ridingRocket)
    {
        Entity otherRiddenEntity = null;
        if (entity.getRidingEntity() != null)
        {
            if (entity.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                entity.startRiding(entity.getRidingEntity());
            }
            else if (entity.getRidingEntity() instanceof EntityCelestialFake)
            {
                Entity e = entity.getRidingEntity();
                e.removePassengers();
                e.setDead();
            }
        	else
        	{
                otherRiddenEntity = entity.getRidingEntity();
        	    entity.dismountRidingEntity();
        	}
        }

        boolean dimChange = entity.worldObj != worldNew;
        entity.worldObj.updateEntityWithOptionalForce(entity, false);
        EntityPlayerMP player = null;
        Vector3 spawnPos = null;
        int oldDimID = LabStuffUtils.getDimensionID(entity.worldObj);

        if (ridingRocket != null)
        {
            ArrayList<TileEntityTelemetry> tList = ridingRocket.getTelemetry();
            NBTTagCompound nbt = new NBTTagCompound();
            ridingRocket.isDead = false;
            ridingRocket.removePassengers();
            ridingRocket.writeToNBTOptional(nbt);

            ((WorldServer) ridingRocket.worldObj).getEntityTracker().untrackEntity(ridingRocket);
            removeEntityFromWorld(ridingRocket.worldObj, ridingRocket, true);

            ridingRocket = (EntityAutoRocket) EntityList.createEntityFromNBT(nbt, worldNew);

            if (ridingRocket != null)
            {
                ridingRocket.setWaitForPlayer(true);

                if (ridingRocket instanceof IWorldTransferCallback)
                {
                    ((IWorldTransferCallback) ridingRocket).onWorldTransferred(worldNew);
                }
            }
        }

        if (dimChange)
        {
            if (entity instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) entity;
                World worldOld = player.worldObj;
                {
                    try
                    {
                        ((WorldServer) worldOld).getPlayerChunkMap().removePlayer(player);
                    }
                    catch (Exception e)
                    {
                    }
                }

                LSPlayerStats stats = LSPlayerStats.get(player);
                stats.setUsingPlanetSelectionGui(false);

                player.dimension = dimID;
                player.connection.sendPacket(new SPacketRespawn(dimID, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), player.interactionManager.getGameType()));

                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    if (WorldUtil.registeredSpaceStations.containsKey(dimID))
                    //TODO This has never been effective before due to the earlier bug - what does it actually do?
                    {
                        NBTTagCompound var2 = new NBTTagCompound();
                        SpaceStationWorldData.getStationData(worldNew, dimID, player).writeToNBT(var2);
                        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_DATA, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { dimID, var2 }), player);
                    }
                }

                removeEntityFromWorld(worldOld, player, true);
                spawnPos = type.getPlayerSpawnLocation((WorldServer) worldNew, player);
                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, LabStuffUtils.getDimensionID(player.worldObj), new Object[] {}), player);
                }
                worldNew.spawnEntityInWorld(entity);
                entity.setWorld(worldNew);

                ChunkPos pair = worldNew.getChunkFromChunkCoords(spawnPos.intX(), spawnPos.intZ()).getChunkCoordIntPair();
                ((WorldServer) worldNew).getChunkProvider().loadChunk(pair.chunkXPos, pair.chunkZPos);
                //entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
                worldNew.updateEntityWithOptionalForce(entity, false);
                entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);

                player.mcServer.getPlayerList().preparePlayer(player, (WorldServer) worldNew);
                player.connection.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
                //worldNew.updateEntityWithOptionalForce(entity, false);

                LSLog.info("Server attempting to transfer player " + player.getGameProfile().getName() + " to dimension " + LabStuffUtils.getDimensionID(worldNew));

                player.interactionManager.setWorld((WorldServer) worldNew);
                player.mcServer.getPlayerList().updateTimeAndWeatherForPlayer(player, (WorldServer) worldNew);
                player.mcServer.getPlayerList().syncPlayerInventory(player);

                for (Object o : player.getActivePotionEffects())
                {
                    PotionEffect var10 = (PotionEffect) o;
                    player.connection.sendPacket(new SPacketEntityEffect(player.getEntityId(), var10));
                }

                player.connection.sendPacket(new SPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
            }
            else
            //Non-player entity transfer i.e. it's an EntityCargoRocket or an empty rocket
            {
                ArrayList<TileEntityTelemetry> tList = null;
                if (entity instanceof EntitySpaceshipBase)
                {
                    tList = ((EntitySpaceshipBase) entity).getTelemetry();
                }
                WorldUtil.removeEntityFromWorld(entity.worldObj, entity, true);

                NBTTagCompound nbt = new NBTTagCompound();
                entity.isDead = false;
                entity.writeToNBTOptional(nbt);
                entity = EntityList.createEntityFromNBT(nbt, worldNew);

                if (entity == null)
                {
                    return null;
                }

                if (entity instanceof IWorldTransferCallback)
                {
                    ((IWorldTransferCallback) entity).onWorldTransferred(worldNew);
                }

                worldNew.spawnEntityInWorld(entity);
                entity.setWorld(worldNew);
                worldNew.updateEntityWithOptionalForce(entity, false);

                if (tList != null && tList.size() > 0)
                {
                    for (TileEntityTelemetry t : tList)
                    {
                        t.addTrackedEntity(entity);
                    }
                }
            }
        }
        else
        {
            //Same dimension player transfer
            if (entity instanceof EntityPlayerMP)
            {
                player = (EntityPlayerMP) entity;
                player.closeScreen();
                LSPlayerStats stats = LSPlayerStats.get(player);
                stats.setUsingPlanetSelectionGui(false);

                if (worldNew.provider instanceof WorldProviderSpaceStation)
                {
                    LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, LabStuffUtils.getDimensionID(player.worldObj), new Object[] {}), player);
                }
                worldNew.updateEntityWithOptionalForce(entity, false);

                spawnPos = type.getPlayerSpawnLocation((WorldServer) entity.worldObj, (EntityPlayerMP) entity);
                player.connection.setPlayerLocation(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
                entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
                worldNew.updateEntityWithOptionalForce(entity, false);

                LSLog.info("Server attempting to transfer player " + player.getGameProfile().getName() + " within same dimension " + LabStuffUtils.getDimensionID(worldNew));
            }

            //Cargo rocket does not needs its location setting here, it will do that itself
        }

        //Update PlayerStatsGC
        if (player != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);
            if (ridingRocket == null && type.useParachute() && stats.getExtendedInventory().getStackInSlot(4) != null && stats.getExtendedInventory().getStackInSlot(4).getItem() instanceof ItemParaChute)
            {
                LSPlayerHandler.setUsingParachute(player, stats, true);
            }
            else
            {
                LSPlayerHandler.setUsingParachute(player, stats, false);
            }

            if (stats.getRocketStacks() != null && stats.getRocketStacks().length > 0)
            {
                for (int stack = 0; stack < stats.getRocketStacks().length; stack++)
                {
                    if (transferInv)
                    {
                        if (stats.getRocketStacks()[stack] == null)
                        {
                            if (stack == stats.getRocketStacks().length - 1)
                            {
                                if (stats.getRocketItem() != null)
                                {
                                    stats.getRocketStacks()[stack] = new ItemStack(stats.getRocketItem(), 1, stats.getRocketType());
                                }
                            }
                            else if (stack == stats.getRocketStacks().length - 2)
                            {
                                stats.getRocketStacks()[stack] = stats.getLaunchpadStack();
                                stats.setLaunchpadStack(null);
                            }
                        }
                    }
                    else
                    {
                        stats.getRocketStacks()[stack] = null;
                    }
                }
            }

        }

        //If in a rocket (e.g. with launch controller) set the player to the rocket's position instead of the player's spawn position
        if (ridingRocket != null)
        {
            entity.setPositionAndRotation(ridingRocket.posX, ridingRocket.posY, ridingRocket.posZ, 0, 0);
            worldNew.updateEntityWithOptionalForce(entity, true);

            worldNew.spawnEntityInWorld(ridingRocket);
            ridingRocket.setWorld(worldNew);

            worldNew.updateEntityWithOptionalForce(ridingRocket, true);
            entity.startRiding(ridingRocket);
        }
        else if (otherRiddenEntity != null)
        {
            if (dimChange)
            {
                World worldOld = otherRiddenEntity.worldObj;
                NBTTagCompound nbt = new NBTTagCompound();
                otherRiddenEntity.writeToNBTOptional(nbt);
                removeEntityFromWorld(worldOld, otherRiddenEntity, true);
                otherRiddenEntity = EntityList.createEntityFromNBT(nbt, worldNew);
                worldNew.spawnEntityInWorld(otherRiddenEntity);
                otherRiddenEntity.setWorld(worldNew);
            }
            if (spawnPos != null)
            {
                otherRiddenEntity.setPositionAndRotation(spawnPos.x, spawnPos.y - 10, spawnPos.z, otherRiddenEntity.rotationYaw, otherRiddenEntity.rotationPitch);
            }
            else
            {
                otherRiddenEntity.setPositionAndRotation(entity.posX, entity.posY - 10, entity.posZ, 0, 0);
            }
            worldNew.updateEntityWithOptionalForce(otherRiddenEntity, true);
        }
        else if (spawnPos != null)
        {
            entity.setLocationAndAngles(spawnPos.x, spawnPos.y, spawnPos.z, entity.rotationYaw, entity.rotationPitch);
        }

        //Spawn in a lander if appropriate
        if (entity instanceof EntityPlayerMP)
        {
            FMLCommonHandler.instance().firePlayerChangedDimensionEvent((EntityPlayerMP) entity, oldDimID, dimID);
            type.onSpaceDimensionChanged(worldNew, (EntityPlayerMP) entity, ridingRocket != null);
        }

        return entity;
    }

    public static WorldServer getStartWorld(WorldServer worldOld)
    {
        return worldOld;
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer forceRespawnClient(int dimID, int par2, String par3, int par4)
    {
        SPacketRespawn fakePacket = new SPacketRespawn(dimID, EnumDifficulty.getDifficultyEnum(par2), WorldType.parseWorldType(par3), WorldSettings.getGameTypeById(par4));
        FMLClientHandler.instance().getClient().thePlayer.connection.handleRespawn(fakePacket);
        return FMLClientHandler.instance().getClientPlayerEntity();
    }

    private static void removeEntityFromWorld(World var0, Entity var1, boolean directlyRemove)
    {
        if (var1 instanceof EntityPlayer)
        {
            final EntityPlayer var2 = (EntityPlayer) var1;
            var2.closeScreen();
            var0.playerEntities.remove(var2);
            var0.updateAllPlayersSleepingFlag();
        }

        if (directlyRemove)
        {
            List l = new ArrayList<Entity>();
            l.add(var1);
            var0.unloadEntities(l);
            //This will automatically remove the entity from the world and the chunk prior to the world's next update entities tick
            //It is important NOT to directly modify World.loadedEntityList here, as the World will be currently iterating through that list when updating each entity (see the line "this.loadedEntityList.remove(i--);" in World.updateEntities()
        }

        var1.isDead = false;
    }

    public static SpaceStationRecipe getSpaceStationRecipe(int planetID)
    {
        for (SpaceStationType type : LabStuffRegistry.getSpaceStationData())
        {
            if (type.getWorldToOrbitID() == planetID)
            {
                return type.getRecipeForSpaceStation();
            }
        }

        return null;
    }

    /**
     * This must return planets in the same order their provider IDs
     * were registered in LabStuffRegistry by LabStuffMain.
     */
    public static List<Object> getPlanetList()
    {
        List<Object> objList = new ArrayList<Object>();
        objList.add(getPlanetListInts());
        return objList;
    }

    public static Integer[] getPlanetListInts()
    {
        Integer[] iArray = new Integer[WorldUtil.registeredPlanets.size()];

        for (int i = 0; i < iArray.length; i++)
        {
            iArray[i] = WorldUtil.registeredPlanets.get(i);
        }

        return iArray;
    }

    /**
     * What's important here is that Labstuff and the server both register
     * the same reachable Labstuff planets (and their provider types) in the same order.
     * See WorldUtil.registerPlanet().
     * 
     * Even if there are dimension conflicts or other problems, the planets must be
     * registered in the same order on both client and server.  This should happen
     * automatically if Labstuff versions match, and if planets modules
     * match  (including Labstuff-Planets and any other sub-mods).
     * 
     * It is NOT a good idea for sub-mods to make the registration order of planets variable
     * or dependent on configs.
     */
    public static void decodePlanetsListClient(List<Object> data)
    {
        try
        {
            if (WorldUtil.registeredPlanets != null)
            {
                for (Integer registeredID : WorldUtil.registeredPlanets)
                {
                    if (DimensionManager.isDimensionRegistered(registeredID))
                    {
                        DimensionManager.unregisterDimension(registeredID);
                    }
                }
            }
            WorldUtil.registeredPlanets = new ArrayList<Integer>();

            String ids = "";
            if (data.size() > 0)
            {
                //Start the provider index at offset 2 to skip the two Overworld Orbit dimensions
                //(this will be iterating through LabStuffRegistry.worldProviderIDs)
                int providerIndex = GalaxyRegistry.getRegisteredSatellites().size() * 2;
                if (data.get(0) instanceof Integer)
                {
                    for (Object o : data)
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                }
                else if (data.get(0) instanceof Integer[])
                {
                    for (Object o : (Integer[]) data.get(0))
                    {
                        WorldUtil.registerPlanetClient((Integer) o, providerIndex);
                        providerIndex++;
                        ids += ((Integer) o).toString() + " ";
                    }
                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public static List<Object> getSpaceStationList()
    {
        List<Object> objList = new ArrayList<Object>();
        objList.add(getSpaceStationListInts());
        return objList;
    }

    public static Integer[] getSpaceStationListInts()
    {
        Integer[] iArray = new Integer[WorldUtil.registeredSpaceStations.size() * 2];

        int i = 0;
        for (Map.Entry<Integer, Integer> e : WorldUtil.registeredSpaceStations.entrySet())
        {
            iArray[i] = e.getKey();
            iArray[i + 1] = e.getValue();
            i += 2;
        }

//        for (int i = 0; i < iArray.length; i++)
//        {
//            iArray[i] = WorldUtil.registeredSpaceStations.get(i);
//        }

        return iArray;
    }

    public static void decodeSpaceStationListClient(List<Object> data)
    {
        try
        {
            if (WorldUtil.registeredSpaceStations != null)
            {
                for (Integer registeredID : WorldUtil.registeredSpaceStations.keySet())
                {
                    DimensionManager.unregisterDimension(registeredID);
                }
            }
            WorldUtil.registeredSpaceStations = Maps.newHashMap();

            if (data.size() > 0)
            {
                if (data.get(0) instanceof Integer)
                {
                    for (int i = 0; i < data.size(); i += 2)
                    {
                        registerSSdim((Integer) data.get(i), (Integer) data.get(i + 1));
                    }
//                    for (Object dimID : data)
//                    {
//                        registerSSdim((Integer) dimID);
//                    }
                }
                else if (data.get(0) instanceof Integer[])
                {
                    Integer[] array = ((Integer[]) data.get(0));
                    for (int i = 0; i < array.length; i += 2)
                    {
                        registerSSdim(array[i], array[i + 1]);
                    }
//                    for (Object dimID : (Integer[]) data.get(0))
//                    {
//                        registerSSdim((Integer) dimID);
//                    }
                }
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void registerSSdim(Integer dimID, Integer providerKey)
    {
        if (!WorldUtil.registeredSpaceStations.containsKey(dimID))
        {
            if (!DimensionManager.isDimensionRegistered(dimID))
            {
                WorldUtil.registeredSpaceStations.put(dimID, providerKey);
                DimensionManager.registerDimension(dimID, WorldUtil.getDimensionTypeById(providerKey));
            }
            else
            {
                LSLog.severe("Dimension already registered on client: unable to register space station dimension " + dimID);
            }
        }
    }

    public static void toCelestialSelection(EntityPlayerMP player, LSPlayerStats stats, int tier)
    {
        player.dismountRidingEntity();
        stats.setSpaceshipTier(tier);

        HashMap<String, Integer> map = WorldUtil.getArrayOfPossibleDimensions(tier, player);
        String dimensionList = "";
        int count = 0;
        for (Entry<String, Integer> entry : map.entrySet())
        {
            dimensionList = dimensionList.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { player.getGameProfile().getName(), dimensionList }), player);
        stats.setUsingPlanetSelectionGui(true);
        stats.setSavedPlanetList(dimensionList);
        Entity fakeEntity = new EntityCelestialFake(player.worldObj, player.posX, player.posY, player.posZ);
        player.worldObj.spawnEntityInWorld(fakeEntity);
        player.startRiding(fakeEntity);
    }

    public static Vector3 getFootprintPosition(World world, float rotation, Vector3 startPosition, BlockVec3 playerCenter)
    {
        Vector3 position = startPosition.clone();
        float footprintScale = 0.375F;

        int mainPosX = position.intX();
        int mainPosY = position.intY();
        int mainPosZ = position.intZ();
        BlockPos posMain = new BlockPos(mainPosX, mainPosY, mainPosZ);

        // If the footprint is hovering over air...
        if (world.getBlockState(posMain).getBlock().isAir(world.getBlockState(posMain), world, posMain))
        {
            position.x += (playerCenter.x - mainPosX);
            position.z += (playerCenter.z - mainPosZ);

            BlockPos pos1 = new BlockPos(position.intX(), position.intY(), position.intZ());
            // If the footprint is still over air....
            Block b2 = world.getBlockState(pos1).getBlock();
            if (b2 != null && b2.isAir(world.getBlockState(pos1), world, pos1))
            {
                for (EnumFacing direction : EnumFacing.values())
                {
                    BlockPos offsetPos = posMain.offset(direction);
                    if (direction != EnumFacing.DOWN && direction != EnumFacing.UP)
                    {
                        if (!world.getBlockState(offsetPos).getBlock().isAir(world.getBlockState(offsetPos), world, offsetPos))
                        {
                            position.x += direction.getFrontOffsetX();
                            position.z += direction.getFrontOffsetZ();
                            break;
                        }
                    }
                }
            }
        }

        mainPosX = position.intX();
        mainPosZ = position.intZ();

        double x0 = (Math.sin((45 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double x1 = (Math.sin((135 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double x2 = (Math.sin((225 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double x3 = (Math.sin((315 - rotation) * Math.PI / 180.0D) * footprintScale) + position.x;
        double z0 = (Math.cos((45 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;
        double z1 = (Math.cos((135 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;
        double z2 = (Math.cos((225 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;
        double z3 = (Math.cos((315 - rotation) * Math.PI / 180.0D) * footprintScale) + position.z;

        double xMin = Math.min(Math.min(x0, x1), Math.min(x2, x3));
        double xMax = Math.max(Math.max(x0, x1), Math.max(x2, x3));
        double zMin = Math.min(Math.min(z0, z1), Math.min(z2, z3));
        double zMax = Math.max(Math.max(z0, z1), Math.max(z2, z3));

        if (xMin < mainPosX)
        {
            position.x += mainPosX - xMin;
        }

        if (xMax > mainPosX + 1)
        {
            position.x -= xMax - (mainPosX + 1);
        }

        if (zMin < mainPosZ)
        {
            position.z += mainPosZ - zMin;
        }

        if (zMax > mainPosZ + 1)
        {
            position.z -= zMax - (mainPosZ + 1);
        }

        return position;
    }

    public static String spaceStationDataToString(HashMap<Integer, Integer> data)
    {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<Integer, Integer>> it = data.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<Integer, Integer> e = it.next();
            builder.append(e.getKey());
            builder.append("$");
            builder.append(e.getValue());
            if (it.hasNext())
            {
                builder.append("?");
            }
        }
        return builder.toString();
    }

    public static HashMap<Integer, Integer> stringToSpaceStationData(String input)
    {
        HashMap<Integer, Integer> data = Maps.newHashMap();
        if (!input.isEmpty())
        {
            String[] str0 = input.split("\\?");
            for (int i = 0; i < str0.length; ++i)
            {
                String[] str1 = str0[i].split("\\$");
                data.put(Integer.parseInt(str1[0]), Integer.parseInt(str1[1]));
            }
        }
        return data;
    }

    public static String getDimensionName(WorldProvider wp)
    {
        if (wp instanceof ILabstuffWorldProvider)
        {
            CelestialBody cb = ((ILabstuffWorldProvider) wp).getCelestialBody();
            if (cb != null && !(cb instanceof Satellite))
            {
                return cb.getUnlocalizedName();
            }
        }

        if (LabStuffUtils.getDimensionID(wp) == ConfigManagerCore.idDimensionOverworld)
        {
            return "Overworld";
        }

        return wp.getDimensionType().getName();
    }

    public static Map<String, List<String>> getAllChecklistKeys()
    {
        Map<String, List<String>> checklistMap = Maps.newHashMap();

        for (Planet planet : GalaxyRegistry.getRegisteredPlanets().values())
        {
            if (planet.getReachable())
            {
                checklistMap.put(planet.getUnlocalizedName(), planet.getChecklistKeys());
            }
        }

        for (Moon moon : GalaxyRegistry.getRegisteredMoons().values())
        {
            if (moon.getReachable())
            {
                checklistMap.put(moon.getUnlocalizedName(), moon.getChecklistKeys());
            }
        }

        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
        {
            if (satellite.getReachable())
            {
                checklistMap.put(satellite.getUnlocalizedName(), satellite.getChecklistKeys());
            }
        }

        return checklistMap;
    }
    
    public static DimensionType getDimensionTypeById(int id)
    {
        for (DimensionType dimensiontype : DimensionType.values())
        {
            if (dimensiontype.getId() == id)
            {
                return dimensiontype;
            }
        }

        LSLog.severe("There was a problem getting WorldProvider type " + id);
        LSLog.severe("(possibly this is a conflict, check Labstuff config.)");
        return null;
    }

	public static World getWorldForDimensionServer(int idDim) {
        MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (theServer == null)
        {
            LSLog.debug("Called WorldUtil server side method but FML returned no server - is this a bug?");
            return null;
        }
        World ws = theServer.worldServerForDimension(idDim);
        return ws;

	}
}