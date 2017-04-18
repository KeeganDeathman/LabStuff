package keegan.labstuff.network;

import java.util.HashMap;

import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.multipart.PartDataCable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DataUtils 
{
	public static DataNetwork getNetwork(BlockPos pos, World worldObj)
	{
		for(EnumFacing side : EnumFacing.VALUES)
		{
			if(CapabilityUtils.hasCapability(worldObj.getTileEntity(pos.offset(side)), Capabilities.GRID_TRANSMITTER_CAPABILITY, side) && CapabilityUtils.hasCapability(worldObj.getTileEntity(pos.offset(side)), Capabilities.DATA_DEVICE_CAPABILITY, side))
			{
				return (DataNetwork) CapabilityUtils.getCapability(worldObj.getTileEntity(pos.offset(side)), Capabilities.GRID_TRANSMITTER_CAPABILITY, side).getTransmitterNetwork();
			}
		}
		return null;
	}
	
	public static HashMap<Coord4D, DataHandlerWrapper> getDevices(BlockPos pos, World worldObj)
	{
		return getNetwork(pos,worldObj).possibleAcceptors;
	}

	public static void sendMessage(DataPackage dataPackage) 
	{
		dataPackage.getTarget().performAction(dataPackage.getMessage());
	}
}
