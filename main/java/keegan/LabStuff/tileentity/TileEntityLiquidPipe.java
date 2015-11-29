package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityLiquidPipe extends FluidHandler
{
	
	
	

	public TileEntityLiquidPipe()
	{
		tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME);
		network = new FluidNetwork(this);
	}
	
	@Override
	public void updateEntity()
	{
		for(int x = -1; x < 2; x++)
		{
			TileEntity tile = worldObj.getTileEntity(xCoord+x, yCoord, zCoord);
			if(tile instanceof FluidHandler)
			{
				if(!(((FluidHandler)tile).network.equals(network)))
				{
					((FluidHandler)tile).network.merge(this);
				}
			}
		}
		for(int y = -1; y < 2; y++)
		{
			TileEntity tile = worldObj.getTileEntity(xCoord, yCoord+y, zCoord);
			if(tile instanceof FluidHandler)
			{
				if(!(((FluidHandler)tile).network.equals(network)))
				{
					((FluidHandler)tile).network.merge(this);
				}
			}
		}
		for(int z = -1; z < 2; z++)
		{
			TileEntity tile = worldObj.getTileEntity(xCoord, yCoord, zCoord+z);
			if(tile instanceof FluidHandler)
			{
				if(!(((FluidHandler)tile).network.equals(network)))
				{
					((FluidHandler)tile).network.merge(this);
				}
			}
			else if(tile instanceof TileEntityRedstonePipe)
			{
				//if(calcDirection(tile) != ((TileEntityRedstonePipe)tile).from)
				//{
					if(((TileEntityRedstonePipe)tile).getTankInfo(calcDirection(tile))[0].fluid != null)
					{
						if(tank.getFluidAmount() > 0 && tank.getFluid().isFluidEqual(((TileEntityRedstonePipe)tile).getTankInfo(calcDirection(tile))[0].fluid) && tank.getCapacity() != tank.getFluidAmount())
							((TileEntityRedstonePipe)tile).fill(calcDirection(tile), this.drain(null, tank.getFluid(), true), true);
					}
					else if(tank.getFluid() != null)
					{
						((TileEntityRedstonePipe)tile).fill(calcDirection(tile), this.drain(null, tank.getFluid(), true), true);
					}
				//}
			}
			else if(tile instanceof TileEntityDataPipe)
			{
				if(calcDirection(tile) != ((TileEntityDataPipe)tile).from)
				{
					if(((TileEntityDataPipe)tile).getTankInfo(calcDirection(tile))[0].fluid != null)	
					{
						if(tank.getFluidAmount() > 0 && tank.getFluid().isFluidEqual(((TileEntityDataPipe)tile).getTankInfo(calcDirection(tile))[0].fluid) && tank.getCapacity() != tank.getFluidAmount())
							((TileEntityDataPipe)tile).fill(calcDirection(tile), this.drain(null, tank.getFluid(), true), true);
					}
					else
					{
						((TileEntityDataPipe)tile).fill(calcDirection(tile), this.drain(null, tank.getFluid(), true), true);
					}
				}
			}
			else if(tile instanceof IFluidHandler && ((IFluidHandler)tile).getTankInfo(calcDirection(tile)).length >= 1)	
			{
				if(((IFluidHandler)tile).getTankInfo(calcDirection(tile))[0].fluid != null)	
				{
					if(tank.getFluidAmount() > 0 && tank.getFluid().isFluidEqual(((IFluidHandler)tile).getTankInfo(calcDirection(tile))[0].fluid) && tank.getCapacity() != tank.getFluidAmount())
						tank.fill(((IFluidHandler)tile).drain(calcDirection(tile), tank.getFluid(), true), true);
					else if(tank.getFluidAmount() == 0)
						tank.fill(((IFluidHandler)tile).drain(calcDirection(tile), ((IFluidHandler)tile).getTankInfo(calcDirection(tile))[0].fluid, true), true);
					
				}
			}
		}
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
		return network.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return network.drain(resource, doDrain);
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
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}

}
