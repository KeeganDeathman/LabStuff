package keegan.labstuff.world;

import net.minecraft.world.biome.*;

public class BiomeLuna extends Biome
{
    public static final Biome moonFlat = new BiomeGenFlatMoon(new BiomeProperties("lunaFlat").setBaseBiome("luna").setBaseHeight(1.5F).setHeightVariation(.04F).setRainDisabled().setRainfall(0.0F));

    BiomeLuna(BiomeProperties var1)
    {
        super(var1);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
    }
    

    public BiomeDecorator createBiomeDecorator()
    {
    	return null;
    }

    @Override
    public float getSpawningChance()
    {
        return 0.1F;
    }
}