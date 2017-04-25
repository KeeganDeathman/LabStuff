package keegan.labstuff.tileentity;

import keegan.labstuff.common.capabilities.CapabilityWrapperManager;
import keegan.labstuff.network.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityTurbineVent extends TileEntity implements IFluidHandlerWrapper, ITickable
{
	
	private FluidTank tank = new FluidTank(500000000);
	
	
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
	public void update()
	{
		if(getWaterMain() != null)
			getWaterMain().fill(tank.drain(1000, true),true);
	}

	protected IFluidHandler getWaterMain()
	{
		for(EnumFacing dir : EnumFacing.VALUES)
		{
			if(worldObj.getTileEntity(pos.offset(dir)) != null && worldObj.getTileEntity(pos.offset(dir)) instanceof IFluidHandler && !(worldObj.getTileEntity(pos.offset(dir)) instanceof TileEntityTurbineVent))
			{
				IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(pos.offset(dir));
				return powerSource;
			}
		}
		return null;
	}
	
	private EnumFacing getDir(TileEntity remote) {
		// TODO Auto-generated method stub
		return EnumFacing.getFacingFromVector(pos.getX() - remote.getPos().getX(), pos.getY() - remote.getPos().getY(), pos.getZ() - remote.getPos().getZ());
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
		// TODO Auto-generated method stub
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
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
