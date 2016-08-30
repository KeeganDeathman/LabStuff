package keegan.labstuff.tileentity;

import cofh.api.energy.*;
import keegan.labstuff.LabStuffMain;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityTurbineValve extends TileEntityPowerConnection implements IEnergyProvider, IFluidHandler 
{
		
	FluidTank tank = new FluidTank(500000);
	EnergyStorage buffer = new EnergyStorage(10000000);
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		// TODO Auto-generated method stub
		return true;
	}

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
	public void updateEntity()
	{
		if(buffer.getEnergyStored() > 2000 && getPowerSource() != null)
			getPowerSource().addPower(buffer.extractEnergy(2000, false)*2, this);
		if(buffer.getEnergyStored() > 2000 && getPowerSourceRF() != null)
			getPowerSourceRF().receiveEnergy(this.getDirectionOfConnection((TileEntity)getPowerSourceRF(), this), 2000, false);
		if(getSteamSource() != null && getSteamSource().canDrain(this.getDirectionOfConnection((TileEntity)getSteamSource(), this), LabStuffMain.steam))
			tank.fill(getSteamSource().drain(this.getDirectionOfConnection((TileEntity)getSteamSource(), this), new FluidStack(LabStuffMain.steam,  1000), true),true);
	}
	
	protected IEnergyHandler getPowerSourceRF()
	{
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof IEnergyHandler)
		{
			IEnergyHandler powerSource = (IEnergyHandler)worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof IEnergyHandler)
		{
			IEnergyHandler powerSource = (IEnergyHandler)worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof IEnergyHandler)
		{
			IEnergyHandler powerSource = (IEnergyHandler)worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof IEnergyHandler)
		{
			IEnergyHandler powerSource = (IEnergyHandler)worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof IEnergyHandler)
		{
			IEnergyHandler powerSource = (IEnergyHandler)worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof IEnergyHandler)
		{
			IEnergyHandler powerSource = (IEnergyHandler)worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
			return powerSource;
		}
		return null;
	}
	
	protected IFluidHandler getSteamSource()
	{
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
			return powerSource;
		}
		return null;
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
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		// TODO Auto-generated method stub
		return buffer.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		return buffer.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		// TODO Auto-generated method stub
		return buffer.getMaxEnergyStored();
	}

}
