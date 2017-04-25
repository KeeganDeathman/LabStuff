package keegan.labstuff.world;

import net.minecraft.world.biome.Biome;

public class BiomeOrbit extends Biome
{
    public static final Biome space = new BiomeOrbit(new BiomeProperties("Space").setRainfall(0.0F));

    @SuppressWarnings("unchecked")
    private BiomeOrbit(BiomeProperties properties)
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}