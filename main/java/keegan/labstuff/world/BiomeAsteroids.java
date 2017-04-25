package keegan.labstuff.world;

import keegan.labstuff.config.ConfigManagerCore;
import net.minecraft.world.biome.Biome;

public class BiomeAsteroids extends Biome
{
    public static final Biome asteroid = new BiomeAsteroids(new BiomeProperties("Asteroids").setRainfall(0.0F));

    @SuppressWarnings("unchecked")
    private BiomeAsteroids(BiomeProperties properties)
    {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableWaterCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableCaveCreatureList.clear();
    }

    public void resetMonsterListByMode(boolean challengeMode)
    {
        this.spawnableMonsterList.clear();
    }

    @Override
    public float getSpawningChance()
    {
        return 0.01F;
    }
}