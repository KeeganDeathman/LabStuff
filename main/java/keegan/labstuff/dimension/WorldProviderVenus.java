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

public class WorldProviderVenus extends WorldProviderSpace implements ILabstuffWorldProvider, ISolarLevel
{
    private double solarMultiplier = -1D;

    @Override
    public Vector3 getFogColor()
    {
        float night = this.getStarBrightness(1.0F);
        float day = 1.0F - this.getStarBrightness(1.0F);
        float dayColR = 203.0F / 255.0F;
        float dayColG = 147.0F / 255.0F;
        float dayColB = 0.0F / 255.0F;
        float nightColR = 131.0F / 255.0F;
        float nightColG = 108.0F / 255.0F;
        float nightColB = 46.0F / 255.0F;
        return new Vector3(dayColR * day + nightColR * night,
                dayColG * day + nightColG * night,
                dayColB * day + nightColB * night);
    }

    @Override
    public Vector3 getSkyColor()
    {
        float night = this.getStarBrightness(1.0F);
        float day = 1.0F - this.getStarBrightness(1.0F);
        float dayColR = 255.0F / 255.0F;
        float dayColG = 207.0F / 255.0F;
        float dayColB = 81.0F / 255.0F;
        float nightColR = 118.0F / 255.0F;
        float nightColG = 89.0F / 255.0F;
        float nightColB = 21.0F / 255.0F;
        return new Vector3(dayColR * day + nightColR * night,
                dayColG * day + nightColG * night,
                dayColB * day + nightColB * night);
    }

    @Override
    public boolean hasSunset()
    {
        return false;
    }

    @Override
    public long getDayLength()
    {
        return 720000L; // 30 times longer than earth
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public Class<? extends IChunkGenerator> getChunkProviderClass()
    {
        return ChunkProviderVenus.class;
    }

    @Override
    public Class<? extends BiomeProvider> getBiomeProviderClass()
    {
        return BiomeProviderVenus.class;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        float f1 = this.worldObj.getCelestialAngle(par1);
        float f2 = 1.0F - (MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.25F);

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f2 > 1.0F)
        {
            f2 = 1.0F;
        }

        return f2 * f2 * 0.75F;
    }

    @Override
    public double getHorizon()
    {
        return 44.0D;
    }

    @Override
    public int getAverageGroundLevel()
    {
        return 76;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        return true;
    }

    //Overriding only in case the Galacticraft API is not up-to-date
    //(with up-to-date API this makes zero difference)
    @Override
    public boolean isSurfaceWorld()
    {
        return (this.worldObj == null) ? false : this.worldObj.isRemote;
    }

    //Overriding so that beds do not explode on Mars
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
        return 0.0375F;
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
        return 0.38F;
    }

    @Override
    public float getSoundVolReductionAmount()
    {
        return 10.0F;
    }

    @Override
    public CelestialBody getCelestialBody()
    {
        return LabStuffMain.planetVenus;
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return false;
    }

    @Override
    public float getThermalLevelModifier()
    {
        return 5;
    }

    @Override
    public float getWindLevel()
    {
        return 0.3F;
    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        if (this.solarMultiplier < 0D)
        {
            double s = this.getSolarSize();
            this.solarMultiplier = s * s * s;
        }
        return this.solarMultiplier;
    }

    @Override
    public boolean shouldDisablePrecipitation()
    {
        return true;
    }

    @Override
    public boolean shouldCorrodeArmor()
    {
        return true;
    }

    @Override
    public DimensionType getDimensionType()
    {
        return LabStuffDimensions.VENUS;
    }
}