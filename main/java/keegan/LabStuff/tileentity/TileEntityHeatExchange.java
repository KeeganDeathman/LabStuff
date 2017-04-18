package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.CapabilityWrapperManager;
import keegan.labstuff.network.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityHeatExchange extends TileEntity implements IFluidHandlerWrapper
{
	
	private FluidTank waterTank;
	private FluidTank steamTank;

	public TileEntityHeatExchange()
	{
		waterTank = new FluidTank(100000);
		steamTank = new FluidTank(100000);
	}
	
	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		if(resource != null && resource.isFluidEqual(new ItemStack(Items.WATER_BUCKET)))
		{
			return waterTank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		if(steamTank.getFluid() != null && resource.isFluidEqual(steamTank.getFluid()))
		{
			return steamTank.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
			return steamTank.drain(maxDrain, doDrain);
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
	public FluidTankInfo[] getTankInfo(EnumFacing from)
	{
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{waterTank.getInfo(),steamTank.getInfo()};
	}

	public void addPower() 
	{
//		System.out.println("Burn");
		waterTank.drain(4000, true);
		steamTank.fill(new FluidStack(LabStuffMain.steam, 4000), true);
		for(EnumFacing dir : EnumFacing.VALUES)
		{
			if(!(dir.equals(EnumFacing.DOWN)))
			{
				TileEntity remote = worldObj.getTileEntity(pos.offset(dir));
				if(remote != null && remote.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP))
					steamTank.fill(new FluidStack(LabStuffMain.steam, remote.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, dir.getOpposite()).fill(steamTank.drain(steamTank.getFluidAmount(), true), true)), true);
			}
		}
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
	
}
