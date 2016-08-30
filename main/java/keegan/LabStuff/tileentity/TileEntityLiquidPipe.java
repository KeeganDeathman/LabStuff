package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityLiquidPipe extends TileEntity implements IFluidHandler
{
	public FluidTank tank;
	
	
	
	private IFluidHandler source;

	public TileEntityLiquidPipe()
	{
		tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 4);
		source = null;
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tank.writeToNBT(tag);
	}
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		tank.readFromNBT(tag);
		
	}
	
	private ForgeDirection calcDirection(TileEntity tile)
	{
		if(yCoord > tile.yCoord)
			return ForgeDirection.UP;
		if(xCoord > tile.xCoord)
			return ForgeDirection.EAST;
		if(xCoord < tile.xCoord)
			return ForgeDirection.WEST;
		if(zCoord > tile.zCoord)
			return ForgeDirection.SOUTH;
		if(zCoord < tile.zCoord)
			return ForgeDirection.NORTH;
		return ForgeDirection.DOWN;
	}
	
	/**Returns nearby tanks that we can  dump into*/
	private IFluidHandler[] getConnectedTanks(FluidStack stack, ForgeDirection from)
	{
		if(stack == null)
			return null;
		ArrayList<IFluidHandler> tanks = new ArrayList<IFluidHandler>();
		
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
		{
			if(!dir.equals(from))
			{
				TileEntity remote = worldObj.getTileEntity(xCoord+dir.offsetX, yCoord+dir.offsetY, zCoord+dir.offsetZ);
				if(remote != null && remote instanceof IFluidHandler)
					if(((IFluidHandler)remote).getTankInfo(dir.getOpposite()) != null 
					&& ((IFluidHandler)remote).canFill(dir, stack.getFluid()))
						if(((IFluidHandler)remote).getTankInfo(dir)[0].fluid != null)
						{
							if(((IFluidHandler)remote).getTankInfo(dir.getOpposite())[0].fluid.amount < ((IFluidHandler)remote).getTankInfo(dir.getOpposite())[0].capacity)
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
	public void updateEntity()
	{
		if(tank.getFluid() != null)
		{
			for(IFluidHandler remote : getConnectedTanks(new FluidStack(tank.getFluid().getFluid(), 0), null))
			{
				if(remote.getTankInfo(calcDirection((TileEntity) remote)) != null && remote.getTankInfo(calcDirection((TileEntity) remote))[0].fluid == null)
					remote.fill(calcDirection((TileEntity)remote), drain(null, tank.getFluidAmount()/2, true), true);
			}
		}
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		IFluidHandler[] tanks = getConnectedTanks(resource, from);
		if(resource == null)
			return 0;
		FluidStack resourceN = new FluidStack(resource.getFluid(), 0);
		
		if(tanks.length > 0)
		{
			for(IFluidHandler remote : tanks)
			{
				FluidStack resourceM = new FluidStack(resource.getFluid(), resource.amount/(tanks.length+1));
				resourceN = resourceM.copy();
				if(remote.fill(calcDirection((TileEntity) remote), resourceM, false) < resourceM.amount)
					resourceN.amount += remote.fill(calcDirection((TileEntity) remote), resourceM, true);
				else
					remote.fill(calcDirection((TileEntity) remote), resourceM, true);
			}
		}
		else
		{
			resourceN = resource.copy();
		}
		
		return tank.fill(resourceN, doFill);
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
