package keegan.labstuff.world;

import net.minecraft.world.biome.Biome;

public class BiomeMars extends Biome
{
    public static final Biome marsFlat = new BiomeGenFlatMars(new BiomeProperties("Mars Flat").setBaseHeight(2.5F).setHeightVariation(0.4F).setRainfall(0.0F).setRainDisabled());

    @SuppressWarnings("unchecked")
    BiomeMars(BiomeProperties properties)
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