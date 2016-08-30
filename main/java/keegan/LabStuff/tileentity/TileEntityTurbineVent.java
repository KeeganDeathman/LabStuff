package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityTurbineVent extends TileEntity implements IFluidHandler 
{
	
	private FluidTank tank = new FluidTank(500000000);
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return tank.drain(maxDrain, doDrain);
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
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	@Override
	public void updateEntity()
	{
		if(getWaterMain() != null && getWaterMain().canFill(this.getDirectionOfConnection((TileEntity)getWaterMain(), this), FluidRegistry.WATER))
			getWaterMain().fill(this.getDirectionOfConnection((TileEntity)getWaterMain(), this), tank.drain(1000, true),true);
	}

	protected IFluidHandler getWaterMain()
	{
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof IFluidHandler && !(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityTurbineVent))
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof IFluidHandler && !(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityTurbineVent))
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof IFluidHandler && !(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityTurbineVent))
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof IFluidHandler && !(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityTurbineVent))
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof IFluidHandler && !(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityTurbineVent))
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof IFluidHandler && !(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityTurbineVent))
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
			return powerSource;
		}
		return null;
	}
	
	protected ForgeDirection getDirectionOfConnection(TileEntity connection, TileEntity current)
	{
			if(current.xCoord != connection.xCoord)
			{
				if(current.xCoord > connection.xCoord)
				{
					return ForgeDirection.EAST;
				}
				else
				{
					return ForgeDirection.WEST;
				}
			}
			else if(current.yCoord != connection.yCoord)
			{
				if(current.yCoord > connection.yCoord)
				{
					return ForgeDirection.UP;
				}
				else
				{
					return ForgeDirection.DOWN;
				}
			}
			else if(current.zCoord != connection.zCoord)
			{
				if(current.zCoord > connection.zCoord)
				{
					return ForgeDirection.NORTH;
				}
				else
				{
					return ForgeDirection.SOUTH;
				}
			}
			return null;
	}
	
}
