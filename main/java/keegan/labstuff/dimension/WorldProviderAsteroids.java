package keegan.labstuff.dimension;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.entities.EntityAstroMiner;
import keegan.labstuff.event.EventHandlerLS;
import keegan.labstuff.galaxies.CelestialBody;
import keegan.labstuff.util.*;
import keegan.labstuff.world.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.*;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.*;

public class WorldProviderAsteroids extends WorldProviderSpace implements ISolarLevel
{
    //Used to list asteroid centres to external code that needs to know them
    private HashSet<AsteroidData> asteroids = new HashSet();
    private boolean dataNotLoaded = true;
    private AsteroidSaveData datafile;
    private double solarMultiplier = -1D;

    //	@Override
//	public void registerWorldChunkManager()
//	{
//		this.worldChunkMgr = new WorldChunkManagerAsteroids(this.worldObj, 0F);
//	}

    @Override
    public CelestialBody getCelestialBody()
    {
        return LabStuffMain.planetAsteroids;
    }

    @Override
    public Vector3 getFogColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 getSkyColor()
    {
        return new Vector3(0, 0, 0);
    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 0;
    }

    @Override
    public boolean isDaytime()
    {
        return true;
    }

    @Override
    public Class<? extends IChunkGenerator> getChunkProviderClass()
    {
        return ChunkProviderAsteroids.class;
    }

    @Override
    public Class<? extends BiomeProvider> getBiomeProviderClass()
    {
        return BiomeProviderAsteroids.class;
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public float calculateCelestialAngle(long par1, float par3)
    {
        return 0.25F;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return 1.0F;
    }

//	@Override
//	public IChunkProvider createChunkGenerator()
//	{
//		return new ChunkProviderAsteroids(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
//	}

    @Override
    public double getHorizon()
    {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 96;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        return true;
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return (this.worldObj == null) ? false : this.worldObj.isRemote;
    }

    //Overriding so that beds do not explode on Asteroids
    @Override
    public boolean canRespawnHere()
    {
        if (EventHandlerLS.bedActivated)
        {
            EventHandlerLS.bedActivated = false;
            return true;
        }
        return false;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return this.shouldForceRespawn() ? this.getDimension() : 0;
    }

    @Override
    public float getGravity()
    {
        return 0.072F;
    }

    @Override
    public double getMeteorFrequency()
    {
        return 10.0D;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.9D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier >= 3;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.1F;
    }

    @Override
    public float getSoundVolReductionAmount()
    {
        return 10.0F;
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return false;
    }

    @Override
    public float getThermalLevelModifier()
    {
        return -1.5F;
    }

    public void addAsteroid(int x, int y, int z, int size, int core)
    {
        AsteroidData coords = new AsteroidData(x, y, z, size, core);
        if (!this.asteroids.contains(coords))
        {
            if (this.dataNotLoaded)
            {
                this.loadAsteroidSavedData();
            }
            if (!this.asteroids.contains(coords))
            {
                this.addToNBT(this.datafile.datacompound, coords);
                this.asteroids.add(coords);
            }
        }
    }

    public void removeAsteroid(int x, int y, int z)
    {
        AsteroidData coords = new AsteroidData(x, y, z);
        if (this.asteroids.contains(coords))
        {
            this.asteroids.remove(coords);

            if (this.dataNotLoaded)
            {
                this.loadAsteroidSavedData();
            }
            this.writeToNBT(this.datafile.datacompound);
        }
    }

    private void loadAsteroidSavedData()
    {
        this.datafile = (AsteroidSaveData) this.worldObj.loadItemData(AsteroidSaveData.class, AsteroidSaveData.saveDataID);

        if (this.datafile == null)
        {
            this.datafile = new AsteroidSaveData("");
            this.worldObj.setItemData(AsteroidSaveData.saveDataID, this.datafile);
            this.writeToNBT(this.datafile.datacompound);
        }
        else
        {
            this.readFromNBT(this.datafile.datacompound);
        }

        this.dataNotLoaded = false;
    }

    private void readFromNBT(NBTTagCompound nbt)
    {
        NBTTagList coordList = nbt.getTagList("coords", 10);
        if (coordList.tagCount() > 0)
        {
            for (int j = 0; j < coordList.tagCount(); j++)
            {
                NBTTagCompound tag1 = coordList.getCompoundTagAt(j);

                if (tag1 != null)
                {
                    this.asteroids.add(AsteroidData.readFromNBT(tag1));
                }
            }
        }
    }

    private void writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList coordList = new NBTTagList();
        for (AsteroidData coords : this.asteroids)
        {
            NBTTagCompound tag = new NBTTagCompound();
            coords.writeToNBT(tag);
            coordList.appendTag(tag);
        }
        nbt.setTag("coords", coordList);
        this.datafile.markDirty();
    }

    private void addToNBT(NBTTagCompound nbt, AsteroidData coords)
    {
        NBTTagList coordList = nbt.getTagList("coords", 10);
        NBTTagCompound tag = new NBTTagCompound();
        coords.writeToNBT(tag);
        coordList.appendTag(tag);
        nbt.setTag("coords", coordList);
        this.datafile.markDirty();
    }

    public BlockVec3 getClosestAsteroidXZ(int x, int y, int z)
    {
        if (this.dataNotLoaded)
        {
            this.loadAsteroidSavedData();
        }

        if (this.asteroids.size() == 0)
        {
            return null;
        }

        BlockVec3 result = null;
        AsteroidData resultRoid = null;
        int lowestDistance = Integer.MAX_VALUE;

        for (AsteroidData test : this.asteroids)
        {
            if ((test.sizeAndLandedFlag & 128) > 0)
            {
                continue;
            }

            int dx = x - test.centre.x;
            int dz = z - test.centre.z;
            int a = dx * dx + dz * dz;
            if (a < lowestDistance)
            {
                lowestDistance = a;
                result = test.centre;
                resultRoid = test;
            }
        }

        if (result == null)
        {
            return null;
        }

        resultRoid.sizeAndLandedFlag |= 128;
        this.writeToNBT(this.datafile.datacompound);
        return result.clone();
    }

    public ArrayList<BlockVec3> getClosestAsteroidsXZ(int x, int y, int z, int facing, int count)
    {
        if (this.dataNotLoaded)
        {
            this.loadAsteroidSavedData();
        }

        if (this.asteroids.size() == 0)
        {
            return null;
        }

        TreeMap<Integer, BlockVec3> targets = new TreeMap();

        for (AsteroidData roid : this.asteroids)
        {
            BlockVec3 test = roid.centre;
            switch (facing)
            {
            case 2:
                if (z - 16 < test.z)
                {
                    continue;
                }
                break;
            case 3:
                if (z + 16 > test.z)
                {
                    continue;
                }
                break;
            case 4:
                if (x - 16 < test.x)
                {
                    continue;
                }
                break;
            case 5:
                if (x + 16 > test.x)
                {
                    continue;
                }
                break;
            }
            int dx = x - test.x;
            int dz = z - test.z;
            int a = dx * dx + dz * dz;
            if (a < 262144)
            {
                targets.put(a, test);
            }
        }

        int max = Math.max(count, targets.size());
        if (max <= 0)
        {
            return null;
        }

        ArrayList<BlockVec3> returnValues = new ArrayList();
        int i = 0;
        int offset = EntityAstroMiner.MINE_LENGTH_AST / 2;
        for (BlockVec3 target : targets.values())
        {
            BlockVec3 coords = target.clone();
            LSLog.debug("Found nearby asteroid at " + target.toString());
            switch (facing)
            {
            case 2:
                coords.z += offset;
                break;
            case 3:
                coords.z -= offset;
                break;
            case 4:
                coords.x += offset;
                break;
            case 5:
                coords.x -= offset;
                break;
            }
            returnValues.add(coords);
            if (++i >= count)
            {
                break;
            }
        }

        return returnValues;
    }

    @Override
    public float getWindLevel()
    {
        return 0.05F;
    }

    @Override
    public int getActualHeight()
    {
        return 256;
    }

    @Override
    protected void createBiomeProvider()
    {
        super.createBiomeProvider();
        this.hasNoSky = true;
    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        if (this.solarMultiplier < 0D)
        {
            double s = this.getSolarSize();
            this.solarMultiplier = s * s * s * ConfigManagerCore.spaceStationEnergyScalar;
        }
        return this.solarMultiplier;
    }

    private static class AsteroidData
    {
        protected BlockVec3 centre;
        protected int sizeAndLandedFlag = 15;
        protected int coreAndSpawnedFlag = -2;

        public AsteroidData(int x, int y, int z)
        {
            this.centre = new BlockVec3(x, y, z);
        }

        public AsteroidData(int x, int y, int z, int size, int core)
        {
            this.centre = new BlockVec3(x, y, z);
            this.sizeAndLandedFlag = size;
            this.coreAndSpawnedFlag = core;
        }

        public AsteroidData(BlockVec3 bv)
        {
            this.centre = bv;
        }

        @Override
        public int hashCode()
        {
            if (this.centre != null)
            {
                return this.centre.hashCode();
            }
            else
            {
                return 0;
            }
        }

        @Override
        public boolean equals(Object o)
        {
            if (o instanceof AsteroidData)
            {
                BlockVec3 vector = ((AsteroidData) o).centre;
                return this.centre.x == vector.x && this.centre.y == vector.y && this.centre.z == vector.z;
            }

            if (o instanceof BlockVec3)
            {
                BlockVec3 vector = (BlockVec3) o;
                return this.centre.x == vector.x && this.centre.y == vector.y && this.centre.z == vector.z;
            }

            return false;
        }

        public NBTTagCompound writeToNBT(NBTTagCompound tag)
        {
            tag.setInteger("x", this.centre.x);
            tag.setInteger("y", this.centre.y);
            tag.setInteger("z", this.centre.z);
            tag.setInteger("coreAndFlag", this.coreAndSpawnedFlag);
            tag.setInteger("sizeAndFlag", this.sizeAndLandedFlag);
            return tag;
        }

        public static AsteroidData readFromNBT(NBTTagCompound tag)
        {
            BlockVec3 tempVector = new BlockVec3();
            tempVector.x = tag.getInteger("x");
            tempVector.y = tag.getInteger("y");
            tempVector.z = tag.getInteger("z");

            AsteroidData roid = new AsteroidData(tempVector);
            if (tag.hasKey("coreAndFlag"))
            {
                roid.coreAndSpawnedFlag = tag.getInteger("coreAndFlag");
            }
            if (tag.hasKey("sizeAndFlag"))
            {
                roid.sizeAndLandedFlag = tag.getInteger("sizeAndFlag");
            }

            return roid;
        }
    }

    @Override
    public boolean shouldDisablePrecipitation()
    {
        return true;
    }

    @Override
    public boolean shouldCorrodeArmor()
    {
        return false;
    }

    @Override
    public DimensionType getDimensionType()
    {
        return LabStuffDimensions.ASTEROIDS;
    }
}