package keegan.labstuff.common.capabilities;

import keegan.labstuff.common.capabilities.DefaultStorageHelper.NullStorage;
import keegan.labstuff.network.IBlockableConnection;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultBlockableConnection implements IBlockableConnection
{
	@Override
	public boolean canConnectMutual(EnumFacing side) 
	{
		return false;
	}

	@Override
	public boolean canConnect(EnumFacing side) 
	{
		return false;
	}
	
	public static void register()
	{
        CapabilityManager.INSTANCE.register(IBlockableConnection.class, new NullStorage<>(), DefaultBlockableConnection.class);
	}
}