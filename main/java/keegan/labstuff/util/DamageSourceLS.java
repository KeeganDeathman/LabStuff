package keegan.labstuff.util;

import net.minecraft.util.DamageSource;

public class DamageSourceLS extends DamageSource
{
    public static final DamageSourceLS spaceshipCrash = (DamageSourceLS) new DamageSourceLS("spaceship_crash").setDamageBypassesArmor();
    public static final DamageSourceLS oxygenSuffocation = (DamageSourceLS) new DamageSourceLS("oxygen_suffocation").setDamageBypassesArmor();
    public static final DamageSourceLS thermal = (DamageSourceLS) new DamageSourceLS("thermal").setDamageBypassesArmor();
    public static final DamageSourceLS acid = (DamageSourceLS) new DamageSourceLS("sulphuric_acid");

    public DamageSourceLS(String damageType)
    {
        super(damageType);
    }
}