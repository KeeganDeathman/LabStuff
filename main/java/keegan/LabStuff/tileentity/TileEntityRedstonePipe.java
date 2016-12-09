package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityRedstonePipe extends TileEntity implements IFluidHandler, ITickable
{
	public FluidTank tank;
	
	
	
	private IFluidHandler source;

	public TileEntityRedstonePipe()
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
	
	private EnumFacing calcDirection(TileEntity tile)
	{
		if(pos.getY() > tile.getPos().getY())
			return EnumFacing.UP;
		if(pos.getX() > tile.getPos().getX())
			return EnumFacing.EAST;
		if(pos.getX() < tile.getPos().getX())
			return EnumFacing.WEST;
		if(pos.getZ() > tile.getPos().getZ())
			return EnumFacing.SOUTH;
		if(pos.getZ() < tile.getPos().getZ())
			return EnumFacing.NORTH;
		return EnumFacing.DOWN;
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
					if(((IFluidHandler)remote).getTankProperties() != null)
						if(((IFluidHandler)remote).getTankProperties()[0].getContents() != null)
						{
							if(((IFluidHandler)remote).getTankProperties()[0].getContents().amount < ((IFluidHandler)remote).getTankProperties()[0].getCapacity())
								tanks.add((IFluidHandler) remote);
						}
						else
						{
							tanks.add((IFluidHandler) remote);
						}
		
			}
		}
		
		IFluidHandler[] tankArray = new IFluidHandler[tanks.size()];
		return (IFluidHandler[]) tanks.toArray(tankArray);
	}
	
	@Override
	public void update()
	{
		if(tank.getFluid() != null && worldObj.isBlockPowered(pos))
		{
			for(IFluidHandler remote : getConnectedTanks(new FluidStack(tank.getFluid().getFluid(), 0), null))
			{
				if(remote.getTankProperties() != null && remote.getTankProperties()[0].getContents() == null)
					remote.fill(drain(tank.getFluidAmount()/2, true), true);
			}
		}
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		IFluidHandler[] tanks = getConnectedTanks(resource, null);
		if(resource == null)
			return 0;
		FluidStack resourceN = new FluidStack(resource.getFluid(), 0);
		
		if(tanks.length > 0 && worldObj.isBlockPowered(pos))
		{
			for(IFluidHandler remote : tanks)
			{
				FluidStack resourceM = new FluidStack(resource.getFluid(), resource.amount/(tanks.length+1));
				resourceN = resourceM.copy();
				if(remote.fill(resourceM, false) < resourceM.amount)
					resourceN.amount += remote.fill(resourceM, true);
				else
					remote.fill(resourceM, true);
			}
		}
		else
		{
			resourceN = resource.copy();
		}
		
		return tank.fill(resourceN, doFill);
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		// TODO Auto-generated method stub
		if(worldObj.isBlockPowered(pos))
			return tank.drain(resource.amount, doDrain);
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if(worldObj.isBlockPowered(pos))
			return tank.drain(maxDrain, doDrain);
		return null;
	}
	
	@Override
	public FluidTankProperties[] getTankProperties()
	{
		// TODO Auto-generated method stub
		return (FluidTankProperties[]) tank.getTankProperties();
	}

}
