package keegan.labstuff.dimension;

import keegan.labstuff.world.*;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraftforge.fml.relauncher.*;

/***
 * Properties of a WorldProviderSpaceStation
 *     1.  Spinnable with Spin Thrusters (if you don't want spin, create your own SpinManager subclass which does nothing)
 *         (note: your SkyProvider needs to rotate according to setSpinDeltaPerTick()
 *     2.  Oregen from other mods is inhibited in this dimension
 *     3.  AstroMiner placement is inhibited in this dimension
 *     4.  The player on arrival into this dimension (after rocket flight) will be in 1st person view
 *
 */
public abstract class WorldProviderSpaceStation extends WorldProviderSpace
{
    private SpinManager spinManager = new SpinManager(this, this.worldObj);

    public SpinManager getSpinManager()
    {
        return spinManager;
    }
    
    @Override
    public void setDimension(int var1)
    {
        super.setDimension(var1);
    }

    /**
     * Called only once from WorldProvider.registerWorld()
     * so this provides a handy initialisation method
     */
    @Override
    protected void createBiomeProvider()
    {
        super.createBiomeProvider();
        this.getSpinManager().registerServerSide();
    }

    @Override
    public Class<? extends IChunkGenerator> getChunkProviderClass()
    {
        return ChunkProviderOrbit.class;
    }

    @Override
    public Class<? extends BiomeProvider> getBiomeProviderClass()
    {
        return BiomeProviderOrbit.class;
    }
    
    @Override
    public void updateWeather()
    {
        super.updateWeather();
        spinManager.updateSpin();
    }
	
	@SideOnly(Side.CLIENT)
	public abstract void setSpinDeltaPerTick(float angle);
	
    @SideOnly(Side.CLIENT)
    public abstract float getSkyRotation();

    @SideOnly(Side.CLIENT)
	public abstract void createSkyProvider();
}