package keegan.labstuff.world;

import net.minecraft.world.biome.Biome;

public class BiomeProviderLuna extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeLuna.moonFlat;
    }
}