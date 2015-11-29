package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityRedstonePipe extends TileEntity implements IFluidHandler
{

	public FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
	public ForgeDirection from;
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord))
		{
			if(this.from == null)
			{
				this.from=from;
			}
			if(!this.from.equals(from))
				return 0;
			ArrayList<FluidHandler> pipes = new ArrayList<FluidHandler>();
			if(from == ForgeDirection.UP || from == ForgeDirection.DOWN)
			{
				TileEntity te1 = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				if(te1 instanceof FluidHandler && te1 != null)
				{
					for(FluidHandler pipe : ((FluidHandler)te1).network.pipes)
					{
						pipes.add(pipe);
					}
				}
				TileEntity te2 = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				if(te2 instanceof FluidHandler && te2 != null)
				{
					for(FluidHandler pipe : ((FluidHandler)te2).network.pipes)
					{
						pipes.add(pipe);
					}
				}
			}
			if(from == ForgeDirection.SOUTH || from == ForgeDirection.NORTH)
			{
				TileEntity te1 = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				if(te1 instanceof FluidHandler && te1 != null)
				{
					for(FluidHandler pipe : ((FluidHandler)te1).network.pipes)
					{
						pipes.add(pipe);
					}
				}
				TileEntity te2 = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				if(te2 instanceof FluidHandler && te2 != null)
				{
					for(FluidHandler pipe : ((FluidHandler)te2).network.pipes)
					{
						pipes.add(pipe);
					}
				}
			}
			if(from == ForgeDirection.EAST || from == ForgeDirection.WEST)
			{
				TileEntity te1 = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				if(te1 instanceof FluidHandler && te1 != null)
				{
					for(FluidHandler pipe : ((FluidHandler)te1).network.pipes)
					{
						pipes.add(pipe);
					}
				}
				TileEntity te2 = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				if(te2 instanceof FluidHandler && te2 != null)
				{
					for(FluidHandler pipe : ((FluidHandler)te2).network.pipes)
					{
						pipes.add(pipe);
					}
				}
			}
			int volume = tank.getFluidAmount();
			int maxVolume = tank.getCapacity();
			FluidStack resourceN = resource;
			for(FluidHandler pipe: pipes)
			{
				volume += pipe.tank.getFluidAmount();
				maxVolume += pipe.tank.getCapacity();
			}
			if(volume + resourceN.amount > maxVolume)
			{
				resourceN.amount -= (maxVolume-(volume+resource.amount));
			}
			int perTank = resourceN.amount/pipes.size() + 1;
			for(FluidHandler pipe:pipes)
			{
				FluidStack resourceM = new FluidStack(resourceN.getFluid(), perTank);
				pipe.tank.fill(resourceM, doFill);
			}
			FluidStack resourceM = new FluidStack(resourceN.getFluid(), perTank);
			tank.fill(resourceM, doFill);
			return resourceN.amount;
		}
		return 0;
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

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return null;
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
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}

}
