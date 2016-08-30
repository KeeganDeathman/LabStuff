package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

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
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		if(from.equals(ForgeDirection.DOWN) && resource.isFluidEqual(new ItemStack(Items.water_bucket)))
		{
			return waterTank.fill(resource, doFill);
		}
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if(!(from.equals(ForgeDirection.DOWN)) && resource.isFluidEqual(new FluidStack(LabStuffMain.steam, 1)))
		{
			return steamTank.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if(!(from.equals(ForgeDirection.DOWN)))
		{
			return steamTank.drain(maxDrain, doDrain);
		}
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{steamTank.getInfo(),waterTank.getInfo()};
	}

	public void addPower() 
	{
		waterTank.drain(4000, true);
		steamTank.fill(new FluidStack(LabStuffMain.steam, 4000), true);
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(!(dir.equals(ForgeDirection.DOWN)))
			{
				TileEntity remote = worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
				if(remote != null && remote instanceof IFluidHandler && ((IFluidHandler)remote).canFill(dir.getOpposite(), steamTank.getFluid().getFluid()))
					steamTank.fill(new FluidStack(LabStuffMain.steam, ((IFluidHandler)remote).fill(dir.getOpposite(), steamTank.drain(steamTank.getFluidAmount(), true), true)), true);
			}
		}
	}
	
}
