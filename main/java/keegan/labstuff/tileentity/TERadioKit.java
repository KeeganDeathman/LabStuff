package keegan.labstuff.tileentity;

import keegan.labstuff.common.capabilities.Capabilities;
import keegan.labstuff.network.IDataDevice;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TERadioKit extends TileEntity implements IDataDevice 
{
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.DATA_DEVICE_CAPABILITY
				|| super.hasCapability(capability, facing);
	}


	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == Capabilities.DATA_DEVICE_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}


	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return "radio_kit";
	}


	@Override
	public void performAction(String command) {
		// TODO Auto-generated method stub
		
	}
}
