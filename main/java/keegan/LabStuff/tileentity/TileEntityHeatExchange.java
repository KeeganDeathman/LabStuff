package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

@SuppressWarnings("deprecation")
public class TileEntityHeatExchange extends TileEntity implements IFluidHandler
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
				if(remote != null && remote instanceof IFluidHandler)
					steamTank.fill(new FluidStack(LabStuffMain.steam, ((IFluidHandler)remote).fill(dir.getOpposite(), steamTank.drain(steamTank.getFluidAmount(), true), true)), true);
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
