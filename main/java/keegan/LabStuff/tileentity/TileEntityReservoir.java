package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fluids.*;

@SuppressWarnings("deprecation")
public class TileEntityReservoir extends TileEntity implements IFluidHandler, ITickable
{
	
	public FluidTank tank = new FluidTank(4000);
	
	@Override
	public void update()
	{
		if(worldObj.getTileEntity(pos.down()) instanceof IFluidHandler)
		{
			IFluidHandler pipe = (IFluidHandler)worldObj.getTileEntity(pos.down());
			if(tank.getFluidAmount() > 0 && pipe.getTankInfo(getDir((TileEntity) pipe)) != null)
			{
				if((pipe.getTankInfo(getDir((TileEntity)pipe)) != null && pipe.getTankInfo(getDir((TileEntity)pipe)).length > 0 && tank.getFluid().equals(pipe.getTankInfo(getDir((TileEntity)pipe))[0].fluid) || pipe.getTankInfo(getDir((TileEntity)pipe))[0].fluid == null) && worldObj.isBlockIndirectlyGettingPowered(pos) == 0)
				{
					pipe.fill(getDir((TileEntity)pipe), drain(getDir((TileEntity) pipe), new FluidStack(tank.getFluid(), 1000), true), true);
				}
			}
		}
	}
	
	private EnumFacing getDir(TileEntity remote) {
		// TODO Auto-generated method stub
		return EnumFacing.getFacingFromVector(pos.getX() - remote.getPos().getX(), pos.getY() - remote.getPos().getY(), pos.getZ() - remote.getPos().getZ());
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
	public int fill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		// TODO Auto-generated method stub
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return tank.drain(maxDrain, doDrain);
	}


	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from)
	{
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}

}
