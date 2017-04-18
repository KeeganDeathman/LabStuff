package keegan.labstuff.common.capabilities;

import keegan.labstuff.common.capabilities.DefaultStorageHelper.NullStorage;
import keegan.labstuff.network.ICableOutputter;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultCableOutputter implements ICableOutputter
{
    @Override
    public boolean canOutputTo(EnumFacing side)
    {
        return true;
    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(ICableOutputter.class, new NullStorage<>(), DefaultCableOutputter.class);
    }
}
