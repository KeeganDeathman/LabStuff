package keegan.labstuff.world;

import keegan.labstuff.config.ConfigManagerCore;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenFlatMoon extends BiomeLuna
{
    public BiomeGenFlatMoon(BiomeProperties par1)
    {
        super(par1);
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.registerBiomeType(this, BiomeDictionary.Type.COLD, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD);
        }
    }
}