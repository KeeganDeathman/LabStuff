package keegan.labstuff.tileentity;

import keegan.labstuff.common.capabilities.CapabilityWrapperManager;
import keegan.labstuff.network.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityReservoir extends TileEntity implements IFluidHandlerWrapper, ITickable
{
	
	public FluidTank tank = new FluidTank(4000);
	
	@Override
	public void update()
	{
		if(worldObj.getTileEntity(pos.down()) != null && worldObj.getTileEntity(pos.down()).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
		{
			IFluidHandler pipe = worldObj.getTileEntity(pos.down()).getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
			if(tank.getFluidAmount() > 0 && pipe.getTankProperties() != null)
			{
				if((pipe.getTankProperties().length > 0 && (tank.getFluid().equals(pipe.getTankProperties()[0].getContents()) || pipe.getTankProperties()[0].getContents() == null)) && worldObj.isBlockIndirectlyGettingPowered(pos) == 0)
				{
					pipe.fill(drain(EnumFacing.UP, new FluidStack(tank.getFluid(), 1000), true), true);
				}
			}
		}
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing side)
	{
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, side);
	}
	
	public CapabilityWrapperManager manager = new CapabilityWrapperManager(IFluidHandlerWrapper.class, FluidHandlerWrapper.class);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return (T)manager.getWrapper(this, side);
		}
		
		return super.getCapability(capability, side);
	}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		
		tank.writeToNBT(tag);
		
		return tag;
	}
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		
		tank.readFromNBT(tag);
	}


	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return tank.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		if(from != null && from.equals(EnumFacing.DOWN))
			return false;
		return true;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}

}
