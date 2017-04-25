package keegan.labstuff.dimension;

	import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.*;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.galaxies.CelestialBody;
import keegan.labstuff.util.Vector3;
import keegan.labstuff.world.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.relauncher.*;

public class WorldProviderOverworldOrbit extends WorldProviderSpaceStation implements IOrbitDimension, IZeroGDimension, ISolarLevel, IExitHeight
{
    Set<Entity> freefallingEntities = new HashSet<Entity>();

    @Override
    public DimensionType getDimensionType()
    {
        return LabStuffDimensions.ORBIT;
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
        return 24000L;
    }

    @Override
    public boolean shouldForceRespawn()
    {
        return !ConfigManagerCore.forceOverworldRespawn;
    }

    @Override
    public boolean isDaytime()
    {
        final float a = this.worldObj.getCelestialAngle(0F);
        //TODO: adjust this according to size of planet below
        return a < 0.42F || a > 0.58F;
    }

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
        return 64;
    }

    @Override
    public boolean canCoordinateBeSpawn(int var1, int var2)
    {
        return true;
    }

//	@Override
//	public String getWelcomeMessage()
//	{
//		return "Entering Earth Orbit";
//	}
//
//	@Override
//	public String getDepartMessage()
//	{
//		return "Leaving Earth Orbit";
//	}

    @Override
    public CelestialBody getCelestialBody()
    {
        return LabStuffMain.satelliteSpaceStation;
    }

    @Override
    public float getGravity()
    {
        return 0.075F;
    }

    @Override
    public boolean hasBreathableAtmosphere()
    {
        return false;
    }

    @Override
    public double getMeteorFrequency()
    {
        return 0;
    }

    @Override
    public double getFuelUsageMultiplier()
    {
        return 0.5D;
    }

    @Override
    public String getPlanetToOrbit()
    {
        return "Overworld";
    }

    @Override
    public int getYCoordToTeleportToPlanet()
    {
        return 30;
    }

    @Override
    public String getSaveFolder()
    {
        return "DIM_SPACESTATION" + this.getDimension();
    }

    @Override
    public double getSolarEnergyMultiplier()
    {
        return ConfigManagerCore.spaceStationEnergyScalar;
    }

    @Override
    public double getYCoordinateToTeleport()
    {
        return 1200;
    }

    @Override
    public boolean canSpaceshipTierPass(int tier)
    {
        return tier > 0;
    }

    @Override
    public float getFallDamageModifier()
    {
        return 0.4F;
    }

    @Override
    public float getSoundVolReductionAmount()
    {
        return 50.0F;
    }

    @Override
    public float getThermalLevelModifier()
    {
        return 0;
    }

    @Override
    public float getWindLevel()
    {
        return 0.1F;
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
    public boolean inFreefall(Entity entity)
    {
        return freefallingEntities.contains(entity);
    }

    @Override
    public void setInFreefall(Entity entity)
    {
        freefallingEntities.add(entity);
    }
    
    @Override
    public void updateWeather()
    {
        freefallingEntities.clear();
        super.updateWeather();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setSpinDeltaPerTick(float angle)
    {
        SkyProviderOrbit skyProvider = ((SkyProviderOrbit)this.getSkyRenderer());
        if (skyProvider != null)
            skyProvider.spinDeltaPerTick = angle;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public float getSkyRotation()
    {
        SkyProviderOrbit skyProvider = ((SkyProviderOrbit)this.getSkyRenderer());
        return skyProvider.spinAngle;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void createSkyProvider()
    {
        this.setSkyRenderer(new SkyProviderOrbit(new ResourceLocation("labstuff:textures/celestialbodies/earth.png"), true, true));
        this.setSpinDeltaPerTick(this.getSpinManager().getSpinRate());
        
        if (this.getCloudRenderer() == null)
            this.setCloudRenderer(new CloudRenderer());
    }
}