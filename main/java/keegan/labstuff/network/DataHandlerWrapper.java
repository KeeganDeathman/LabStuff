package keegan.labstuff.network;

import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class DataHandlerWrapper implements IDataDevice
{

	Coord4D coord;
	
	@Override
	public void performAction(String command) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getDeviceType()
	{
		return "";
	}
	
	public static DataHandlerWrapper get(TileEntity tileEntity, EnumFacing side)
	{
		if(tileEntity == null || tileEntity.getWorld() == null)
		{
			return null;
		}
		
		DataHandlerWrapper wrapper = null;
		
		wrapper = new DataWrapper(CapabilityUtils.getCapability(tileEntity, Capabilities.DATA_DEVICE_CAPABILITY, side));
		
		if(wrapper != null)
		{
			wrapper.coord = Coord4D.get(tileEntity);
		}
		
		return wrapper;
	}
	
	public static class DataWrapper extends DataHandlerWrapper
	{

		private IDataDevice acceptor;

		public DataWrapper(IDataDevice dataDevice)
		{
			acceptor = dataDevice;
		}
		
		public void performAction(String command)
		{
			acceptor.performAction(command);
		}

		@Override
		public String getDeviceType() {
			// TODO Auto-generated method stub
			return acceptor.getDeviceType();
		}

	
	}

}
