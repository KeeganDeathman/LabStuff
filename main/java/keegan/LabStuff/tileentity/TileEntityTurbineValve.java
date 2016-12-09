package keegan.labstuff.tileentity;

import cofh.api.energy.*;
import keegan.labstuff.LabStuffMain;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

@SuppressWarnings("deprecation")
public class TileEntityTurbineValve extends TileEntityPowerConnection implements IFluidHandler, IEnergyProvider
{
		
	FluidTank tank = new FluidTank(500000);
	
	int power = 0;
	
	EnergyStorage buffer = new EnergyStorage(10000000);
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return tank.drain(resource.amount, doDrain);
	}
	
	@Override
	public void update()
	{
		if(buffer.getEnergyStored() > 4000 && getPowerSource() != null)
			getPowerSource().addPower(buffer.extractEnergy(2000, false)*2, this);
		if(buffer.getEnergyStored() > 2000 && getPowerSourceRF() != null)
			getPowerSourceRF().receiveEnergy(this.getDirectionOfConnection((TileEntity)getPowerSourceRF(), this), buffer.extractEnergy(2000, false), false);
		if(getSteamSource() != null)
			tank.fill(getSteamSource().drain(getDir((TileEntity) getSteamSource()),new FluidStack(LabStuffMain.steam,  1000), true),true);
	}
	
	private EnumFacing getDir(TileEntity remote) {
		// TODO Auto-generated method stub
		return EnumFacing.getFacingFromVector(pos.getX() - remote.getPos().getX(), pos.getY() - remote.getPos().getY(), pos.getZ() - remote.getPos().getZ());
	}
	
	protected IEnergyReceiver getPowerSourceRF()
	{
		for(EnumFacing dir : EnumFacing.VALUES)
			if(worldObj.getTileEntity(pos.offset(dir)) != null && worldObj.getTileEntity(pos.offset(dir)) instanceof IEnergyReceiver)
			{
				IEnergyReceiver powerSource = (IEnergyReceiver)worldObj.getTileEntity(pos.offset(dir));
				return powerSource;
			}
		return null;
	}
	
	protected IFluidHandler getSteamSource()
	{
		if(worldObj.getTileEntity(pos.east()) != null && worldObj.getTileEntity(pos.east()) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(pos.east());
			return powerSource;
		}
		else if(worldObj.getTileEntity(pos.west()) != null && worldObj.getTileEntity(pos.west()) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(pos.west());
			return powerSource;
		}
		else if(worldObj.getTileEntity(pos.up()) != null && worldObj.getTileEntity(pos.up()) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(pos.up());
			return powerSource;
		}
		else if(worldObj.getTileEntity(pos.down()) != null && worldObj.getTileEntity(pos.down()) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(pos.down());
			return powerSource;
		}
		else if(worldObj.getTileEntity(pos.south()) != null && worldObj.getTileEntity(pos.south()) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(pos.south());
			return powerSource;
		}
		else if(worldObj.getTileEntity(pos.north()) != null && worldObj.getTileEntity(pos.north()) instanceof IFluidHandler)
		{
			IFluidHandler powerSource = (IFluidHandler)worldObj.getTileEntity(pos.north());
			return powerSource;
		}
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}

	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		// TODO Auto-generated method stub
		return buffer.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		// TODO Auto-generated method stub
		return buffer.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		// TODO Auto-generated method stub
		return buffer.getMaxEnergyStored();
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

	public void add(int i) {
		power += i;
	}

}
