package keegan.labstuff.dimension;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.event.EventHandlerLS;
import keegan.labstuff.galaxies.CelestialBody;
import keegan.labstuff.util.Vector3;
import keegan.labstuff.world.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.*;

public class WorldProviderLuna extends WorldProviderSpace implements ILabstuffWorldProvider, ISolarLevel
{
    @Override
    public DimensionType getDimensionType()
    {
        return LabStuffDimensions.LUNA;
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
        return 192000L;
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public Class<? extends IChunkGenerator> getChunkProviderClass()
    {
        return ChunkProviderLuna.class;
    }

    @Override
    public Class<? extends BiomeProvider> getBiomeProviderClass()
    {
        return BiomeProviderLuna.class;
    }

//    @Override
//	public void setDimension(int var1)
//	{
//		this.getDimensionId() = var1;
//		super.setDimension(var1);
//	}

//	@Override
//	protected void generateLightBrightnessTable()
//	{
//		final float var1 = 0.0F;
//
//		for (int var2 = 0; var2 <= 15; ++var2)
//		{
//			final float var3 = 1.0F - var2 / 15.0F;
//			this.lightBrightnessTable[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
//		}
//	}

//	@Override
//	public float[] calcSunriseSunsetColors(float var1, float var2)
//	{
//		return null;
//	}

//	@Override
//	public void registerWorldChunkManager()
//	{
//		this.worldChunkMgr = new WorldChunkManagerMoon();
//	}

//	@SideOnly(Side.CLIENT)
//	@Override
//	public Vec3d getFogColor(float var1, float var2)
//	{
//		return Vec3d.createVectorHelper((double) 0F / 255F, (double) 0F / 255F, (double) 0F / 255F);
//	}

//	@Override
//	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks)
//	{
//		return Vec3d.createVectorHelper(0, 0, 0);
//	}

//	@Override
//	public float calculateCelestialAngle(long par1, float par3)
//	{
//		final int var4 = (int) (par1 % 192000L);
//		float var5 = (var4 + par3) / 192000.0F - 0.25F;
//
//		if (var5 < 0.0F)
//		{
//			++var5;
//		}
//
//		if (var5 > 1.0F)
//		{
//			--var5;
//		}
//
//		final float var6 = var5;
//		var5 = 1.0F - (float) ((Math.cos(var5 * Math.PI) + 1.0D) / 2.0D);
//		var5 = var6 + (var5 - var6) / 3.0F;
//		return var5;
//	}

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        final float var2 = this.worldObj.getCelestialAngle(par1);
        float var3 = 1.0F - (MathHelper.cos(var2 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

        if (var3 < 0.0F)
        {
            var3 = 0.0F;
        }

        if (var3 > 1.0F)
        {
            var3 = 1.0F;
        }

        return var3 * var3 * 0.5F + 0.3F;
    }

//	@Override
//	public IChunkProvider createChunkGenerator()
//	{
//		return new ChunkProviderMoon(this.worldObj, this.worldObj.getSeed(), this.worldObj.getWorldInfo().isMapFeaturesEnabled());
//	}

//	@Override
//	public void updateWeather()
//	{
//		this.worldObj.getWorldInfo().setRainTime(0);
//		this.worldObj.getWorldInfo().setRaining(false);
//		this.worldObj.getWorldInfo().setThunderTime(0);
//		this.worldObj.getWorldInfo().setThundering(false);
//		this.worldObj.rainingStrength = 0.0F;
//		this.worldObj.thunderingStrength = 0.0F;
//	}

    @Override
    public boolean isSkyColored()
    {
        return false;
    }

    @Override
    public double getHorizon()
    {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 68;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        return true;
    }

    //Overriding only in case the LabStuff API is not up-to-date
    //(with up-to-date API this makes zero difference)
    @Override
    public boolean isSurfaceWorld()
    {
        return (this.worldObj == null) ? false : this.worldObj.isRemote;
    }

    //Overriding  so that beds do not explode on Luna
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

    //Overriding only in case the LabStuff API is not up-to-date
    //(with up-to-date API this makes zero difference)
    @Override
    public int getRespawnDimension(EntityPlayerMP player)
    {
        return this.shouldForceRespawn() ? this.getDimension() : 0;
    }

//	@Override
//	public String getSaveFolder()
//	{
//		return "DIM" + ConfigManagerCore.idDimensionMoon;
//	}
//
//	@Override
//	public String getWelcomeMessage()
//	{
//		return "Entering The Moon";
//	}
//
//	@Override
//	public String getDepartMessage()
//	{
//		return "Leaving The Moon";
//	}

//	@Override
//	public String getDimensionName()
//	{
//		return "Moon";
//	}

    //	@Override
    //	public boolean canSnowAt(int x, int y, int z)
    //	{
    //		return false;
    //	}

//	@Override
//	public boolean canBlockFreeze(int x, int y, int z, boolean byWater)
//	{
//		return false;
//	}
//
//	@Override
//	public boolean canDoLightning(Chunk chunk)
//	{
//		return false;
//	}
//
//	@Override
//	public boolean canDoRainSnowIce(Chunk chunk)
//	{
//		return false;
//	}

    @Override
    public float getGravity()
    {
        return 0.062F;
    }

    @Override
    public double getMeteorFrequency()
    {
        return 7.0D;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.7D;
    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        return 1.4D;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier > 0;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.18F;
    }

    @Override
    public float getSoundVolReductionAmount()
    {
        return 20.0F;
    }

    @Override
    public CelestialBody getCelestialBody()
    {
        return LabStuffMain.moonLuna;
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return false;
    }

    @Override
    public float getThermalLevelModifier()
    {
        return 0;
    }

    @Override
    public float getWindLevel()
    {
        return 0;
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
}