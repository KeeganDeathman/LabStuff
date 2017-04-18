package keegan.labstuff.multipart;

import java.util.Collection;

import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PartDataCable extends PartTransmitter<DataHandlerWrapper, DataNetwork> implements IDataDevice
{

	@Override
	public ResourceLocation getType() {
		return new ResourceLocation("labstuff:datacable");
	}

	@Override
	public TransmissionType getTransmissionType() {
		// TODO Auto-generated method stub
		return TransmissionType.DATA;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.DATA_DEVICE_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == Capabilities.DATA_DEVICE_CAPABILITY)
		{
			return (T)this;
		}
		
		return super.getCapability(capability, facing);
	}


	@Override
	public DataNetwork createNewNetwork() {
		return new DataNetwork();
	}

	@Override
	public DataNetwork createNetworkByMerging(Collection<DataNetwork> networks) {
		return new DataNetwork(networks);
	}

	@Override
	public DataHandlerWrapper getCachedAcceptor(EnumFacing side) 
	{
		return DataHandlerWrapper.get(getCachedTile(side), side.getOpposite());
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getBuffer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void takeShare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateShare() 
	{}

	@Override
	public TransmitterType getTransmitterType() {
		// TODO Auto-generated method stub
		return TransmitterType.DATACABLE;
	}
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack stack, PartMOP hit)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			if(this.getTransmitter().getTransmitterNetwork() != null)
			{
				
				player.addChatMessage(new TextComponentString("Network is holding " + this.getTransmitter().getTransmitterNetwork().possibleAcceptors.size() + " Devices"));
				for(Coord4D key : this.getTransmitter().getTransmitterNetwork().possibleAcceptors.keySet())
					player.addChatMessage(new TextComponentString(this.getTransmitter().getTransmitterNetwork().possibleAcceptors.get(key).getDeviceType()));
			}
			else
			{
				player.addChatMessage(new TextComponentString("Cable is holding " + this.cachedAcceptors.length + " Devices"));
			}
		}
		return false;
	}

	@Override
	public boolean isValidAcceptor(TileEntity tile, EnumFacing side) {
		return CapabilityUtils.hasCapability(tile, Capabilities.DATA_DEVICE_CAPABILITY, side.getOpposite()) && !CapabilityUtils.hasCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side);
	}

	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return "data_cable";
	}

	@Override
	public void performAction(String command) {
		// TODO Auto-generated method stub
		
	}

}
