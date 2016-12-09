package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.FluidTankProperties;

@SuppressWarnings(value = {"deprecation" })
public class TileEntityLiquidPipe extends TileEntity implements IFluidHandler, ITickable
{
	public FluidTank tank;
	
	
	
	private IFluidHandler source;

	public TileEntityLiquidPipe()
	{
		tank = new FluidTank(4000);
		source = null;
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
	/**Returns nearby tanks that we can  dump into*/
	private IFluidHandler[] getConnectedTanks(FluidStack stack, EnumFacing from)
	{
		if(stack == null)
			return null;
		ArrayList<IFluidHandler> tanks = new ArrayList<IFluidHandler>();
		
		for(EnumFacing dir : EnumFacing.VALUES)
		{
			if(!dir.equals(from))
			{
				TileEntity remote = worldObj.getTileEntity(pos.offset(dir));
				if(remote != null && remote instanceof IFluidHandler)
				{
					if(((IFluidHandler)remote).getTankInfo(getDir(remote)) != null && ((IFluidHandler)remote).getTankInfo(getDir(remote)).length >= 1)
					{
						if(((IFluidHandler) remote).getTankInfo(getDir(remote))[0].fluid != null)
						{
							if(((IFluidHandler)remote).getTankInfo(getDir(remote))[0].fluid.amount < ((IFluidHandler)remote).getTankInfo(getDir(remote))[0].capacity)
								tanks.add((IFluidHandler) remote);
						}
						else
						{
							tanks.add((IFluidHandler) remote);
						}
					}
				}
			}
		}
		
		IFluidHandler[] tankArray = new IFluidHandler[tanks.size()];
		return (IFluidHandler[]) tanks.toArray(tankArray);
	}
	
	@Override
	public void update()
	{
		if(tank.getFluid() != null)
		{
			for(IFluidHandler remote : getConnectedTanks(new FluidStack(tank.getFluid().getFluid(), 0), null))
			{
				if(remote.getTankInfo(getDir((TileEntity)remote)) != null && remote.getTankInfo(getDir((TileEntity)remote))[0] == null)
					remote.fill(getDir((TileEntity)remote), drain(getDir((TileEntity) remote), tank.getFluidAmount()/2, true), true);
			}
		}
	}

	private EnumFacing getDir(TileEntity remote) {
		// TODO Auto-generated method stub
		return EnumFacing.getFacingFromVector(pos.getX() - remote.getPos().getX(), pos.getY() - remote.getPos().getY(), pos.getZ() - remote.getPos().getZ());
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill)
	{
		int filled = tank.fill(resource, doFill);
		if(doFill)
		{
			IFluidHandler[] tanks = getConnectedTanks(resource, null);
			if(tanks != null)
			{
				for(IFluidHandler remote : tanks)
				{
					if(remote.getTankInfo(getDir((TileEntity)remote)) != null && remote.getTankInfo(getDir((TileEntity)remote))[0].fluid != null && !((remote.getTankInfo(getDir((TileEntity)remote))[0].fluid.amount + resource.amount) > tank.getFluidAmount()))
						remote.fill(getDir((TileEntity)remote), resource, doFill);
				}
			}
		}
		return filled;
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
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		// TODO Auto-generated method stub
		return null;
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
