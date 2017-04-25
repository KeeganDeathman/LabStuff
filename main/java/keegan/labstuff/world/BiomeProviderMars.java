package keegan.labstuff.world;

import net.minecraft.world.biome.Biome;

public class BiomeProviderMars extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeMars.marsFlat;
    }
}