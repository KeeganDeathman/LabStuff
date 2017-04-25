package keegan.labstuff.world;

import net.minecraft.world.biome.Biome;

public class BiomeProviderAsteroids extends BiomeProviderSpace
{
    @Override
    public Biome getBiome()
    {
        return BiomeAsteroids.asteroid;
    }
}