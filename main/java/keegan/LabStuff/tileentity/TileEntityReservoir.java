package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityReservoir extends TileEntity implements IFluidHandler
{
	
	public FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME*10);
	
	@Override
	public void updateEntity()
	{
		if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof IFluidHandler)
		{
			IFluidHandler pipe = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
			if(tank.getFluidAmount() > 0)
			{
				if((tank.getFluid().equals(pipe.getTankInfo(ForgeDirection.UP)[0].fluid) || pipe.getTankInfo(ForgeDirection.UP)[0].fluid == null))
				{
					pipe.fill(ForgeDirection.UP, drain(null, new FluidStack(tank.getFluid(), FluidContainerRegistry.BUCKET_VOLUME), true), true);
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
	}
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		// TODO Auto-generated method stub
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}

}
