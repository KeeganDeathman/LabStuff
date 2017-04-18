package keegan.labstuff.common.capabilities;

import keegan.labstuff.common.capabilities.DefaultStorageHelper.NullStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * Created by ben on 19/05/16.
 */
public class DefaultWrenchable implements IWrenchable
{
    @Override
    public EnumActionResult onSneakRightClick(EntityPlayer player, EnumFacing side)
    {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onRightClick(EntityPlayer player, EnumFacing side)
    {
        return EnumActionResult.PASS;
    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IWrenchable.class, new NullStorage<IWrenchable>(), DefaultWrenchable.class);
    }
}