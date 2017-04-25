package keegan.labstuff.dimension;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.entities.*;
import keegan.labstuff.util.*;
import keegan.labstuff.world.ITeleportType;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.FMLLog;

public class TeleportTypeAsteroids implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
    {
        if (player != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);
            int x = MathHelper.floor_double(stats.getCoordsTeleportedFromX());
            int z = MathHelper.floor_double(stats.getCoordsTeleportedFromZ());
            int limit = ConfigManagerCore.otherPlanetWorldBorders - 2;
            if (limit > 20)
            {
                if (x > limit)
                {
                    z *= limit / x;
                    x = limit;
                }
                else if (x < -limit)
                {   
                    z *= -limit / x;
                    x = -limit;
                }
                if (z > limit)
                {
                    x *= limit / z;
                    z = limit;
                }
                else if (z < -limit)
                {
                    x *= - limit / z;
                    z = -limit;
                }
            }

            int attemptCount = 0;

            //Small pre-generate with a chunk loading radius of 3, to make sure some asteroids get generated
            //(if the world is already generated here, this will be very quick)
            this.preGenChunks(world, x >> 4, z >> 4);

            do
            {
                BlockVec3 bv3 = null;
                if (world.provider instanceof WorldProviderAsteroids)
                {
                    bv3 = ((WorldProviderAsteroids) world.provider).getClosestAsteroidXZ(x, 0, z);
                }

                if (bv3 != null)
                {
                    //Check whether the returned asteroid is too far from the desired entry location in which case, give up
                    if (bv3.distanceSquared(new BlockVec3(x, 128, z)) > 25600)
                    {
                        break;
                    }

                    this.loadChunksAround(bv3.x, bv3.z, 2, world.getChunkProvider());
                    this.loadChunksAround(bv3.x, bv3.z, -3, world.getChunkProvider());

                    if (goodAsteroidEntry(world, bv3.x, bv3.y, bv3.z))
                    {
                        return new Vector3(bv3.x, 310, bv3.z);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3(bv3.x + 2, 310, bv3.z + 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x + 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3(bv3.x + 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z - 2))
                    {
                        return new Vector3(bv3.x - 2, 310, bv3.z - 2);
                    }
                    if (goodAsteroidEntry(world, bv3.x - 2, bv3.y, bv3.z + 2))
                    {
                        return new Vector3(bv3.x - 2, 310, bv3.z + 2);
                    }

                    //Failed to find an asteroid even though there should be one there
                    ((WorldProviderAsteroids) world.provider).removeAsteroid(bv3.x, bv3.y, bv3.z);
                }

                attemptCount++;
            }
            while (attemptCount < 5);

            FMLLog.info("Failed to find good large asteroid landing spot! Falling back to making a small one.");
            this.makeSmallLandingSpot(world, x, z);
            return new Vector3(x, 310, z);
        }

        FMLLog.severe("Null player when teleporting to Asteroids!");
        return new Vector3(0, 310, 0);
    }

    private boolean goodAsteroidEntry(World world, int x, int yorig, int z)
    {
        for (int k = 208; k > 48; k--)
        {
            if (!world.isAirBlock(new BlockPos(x, k, z)))
            {
                if (Math.abs(k - yorig) > 20)
                {
                    continue;
                }
                //Clear the downward path of small asteroids and any other asteroid rock
                for (int y = k + 2; y < 256; y++)
                {
                    if (world.getBlockState(new BlockPos(x, y, z)).getBlock() == LabStuffMain.asteroid)
                    {
                        world.setBlockToAir(new BlockPos(x, y, z));
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z)).getBlock() == LabStuffMain.asteroid)
                    {
                        world.setBlockToAir(new BlockPos(x - 1, y, z));
                    }
                    if (world.getBlockState(new BlockPos(x, y, z - 1)).getBlock() == LabStuffMain.asteroid)
                    {
                        world.setBlockToAir(new BlockPos(x, y, z - 1));
                    }
                    if (world.getBlockState(new BlockPos(x - 1, y, z - 1)).getBlock() == LabStuffMain.asteroid)
                    {
                        world.setBlockToAir(new BlockPos(x - 1, y, z - 1));
                    }
                }
                return true;
            }
        }
        return false;
    }

    private void makeSmallLandingSpot(World world, int x, int z)
    {
        this.loadChunksAround(x, z, -1, (ChunkProviderServer) world.getChunkProvider());

        for (int k = 255; k > 48; k--)
        {
            if (!world.isAirBlock(new BlockPos(x, k, z)))
            {
                this.makePlatform(world, x, k - 1, z);
                return;
            }
            if (!world.isAirBlock(new BlockPos(x - 1, k, z)))
            {
                this.makePlatform(world, x - 1, k - 1, z);
                return;
            }
            if (!world.isAirBlock(new BlockPos(x - 1, k, z - 1)))
            {
                this.makePlatform(world, x - 1, k - 1, z - 1);
                return;
            }
            if (!world.isAirBlock(new BlockPos(x, k, z - 1)))
            {
                this.makePlatform(world, x, k - 1, z - 1);
                return;
            }
        }

        this.makePlatform(world, x, 48 + world.rand.nextInt(128), z);
        return;
    }

    private void loadChunksAround(int x, int z, int i, ChunkProviderServer cp)
    {
        cp.loadChunk(x >> 4, z >> 4);
        if ((x + i) >> 4 != x >> 4)
        {
            cp.loadChunk((x + i) >> 4, z >> 4);
            if ((z + i) >> 4 != z >> 4)
            {
                cp.loadChunk(x >> 4, (z + i) >> 4);
                cp.loadChunk((x + i) >> 4, (z + i) >> 4);
            }
        }
        else if ((z + i) >> 4 != z >> 4)
        {
            cp.loadChunk(x >> 4, (z + i) >> 4);
        }
    }

    private void makePlatform(World world, int x, int y, int z)
    {
        for (int xx = -3; xx < 3; xx++)
        {
            for (int zz = -3; zz < 3; zz++)
            {
                if (xx == -3 && (zz == -3 || zz == 2))
                {
                    continue;
                }
                if (xx == 2 && (zz == -3 || zz == 2))
                {
                    continue;
                }
                doBlock(world, x + xx, y, z + zz);
            }
        }
        for (int xx = -2; xx < 2; xx++)
        {
            for (int zz = -2; zz < 2; zz++)
            {
                doBlock(world, x + xx, y - 1, z + zz);
            }
        }
        doBlock(world, x - 1, y - 2, z - 1);
        doBlock(world, x - 1, y - 2, z);
        doBlock(world, x, y - 2, z);
        doBlock(world, x, y - 2, z - 1);
    }

    private void doBlock(World world, int x, int y, int z)
    {
        int meta = (int) (world.rand.nextFloat() * 1.5F);
        if (world.isAirBlock(new BlockPos(x, y, z)))
        {
            world.setBlockState(new BlockPos(x, y, z), LabStuffMain.asteroid.getStateFromMeta(meta), 2);
        }
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
    {
        return new Vector3(entity.posX, 900.0, entity.posZ);
    }


    private void preGenChunks(World w, int cx, int cz)
    {
        this.preGenChunk(w, cx, cz);
        for (int r = 1; r < 3; r++)
        {
            int xmin = cx - r;
            int xmax = cx + r;
            int zmin = cz - r;
            int zmax = cz + r;
            for (int i = -r; i < r; i++)
            {
                this.preGenChunk(w, xmin, cz + i);
                this.preGenChunk(w, xmax, cz - i);
                this.preGenChunk(w, cx - i, zmin);
                this.preGenChunk(w, cx + i, zmax);
            }
        }
    }

    private void preGenChunk(World w, int chunkX, int chunkZ)
    {
        w.getChunkFromChunkCoords(chunkX, chunkZ);
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);
            CapabilityUtils.getCapability(player, Capabilities.RESEARCHER_CAPABILITY, null).research(LabStuffMain.ASTEROIDS);;

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.capabilities.isFlying)
                {
                    player.capabilities.isFlying = false;
                }

                if (!newWorld.isRemote)
                {
                    EntityEntryPod entryPod = new EntityEntryPod(player);

                    newWorld.spawnEntityInWorld(entryPod);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        LSPlayerStats stats = LSPlayerStats.get(player);        
        ItemStack[] rocketStacks = new ItemStack[20];
        stats.setFuelLevel(1000);
        int i = 0;
        rocketStacks[i++] = new ItemStack(LabStuffMain.oxMask);
        rocketStacks[i++] = new ItemStack(LabStuffMain.oxygenGear);
        rocketStacks[i++] = new ItemStack(LabStuffMain.oxTankMedium);
        rocketStacks[i++] = new ItemStack(LabStuffMain.oxTankHeavy);
        rocketStacks[i++] = new ItemStack(LabStuffMain.oxTankHeavy);
        rocketStacks[i++] = new ItemStack(LabStuffMain.canisterLOX);
        rocketStacks[i++] = new ItemStack(LabStuffMain.canisterLOX);
        rocketStacks[i++] = new ItemStack(LabStuffMain.canisterLOX);
        rocketStacks[i++] = new ItemStack(LabStuffMain.thermalCloth, 32, 7);
        rocketStacks[i++] = new ItemStack(Blocks.GLASS_PANE, 16);
        rocketStacks[i++] = new ItemStack(Blocks.PLANKS, 32, 2);
        rocketStacks[i++] = new ItemStack(LabStuffMain.blockSolarPanel, 2); //Solar Panels
        rocketStacks[i++] = new ItemStack(Items.EGG, 12);

        rocketStacks[i++] = new ItemStack(Items.POTIONITEM, 4, 8262); //Night Vision Potion
        rocketStacks[i++] = new ItemStack(LabStuffMain.cryoChamber, 1, 4); //Cryogenic Chamber
        rocketStacks[i++] = new ItemStack(LabStuffMain.rocketMars, 1, IRocketType.EnumRocketType.INVENTORY36.ordinal());
        stats.setRocketStacks(rocketStacks);
    }
}