package keegan.labstuff.world;

import net.minecraft.world.biome.Biome;

public class BiomeProviderOrbit extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeOrbit.space;
    }
}