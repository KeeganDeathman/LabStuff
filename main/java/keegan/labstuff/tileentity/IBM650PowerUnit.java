package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.*;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class IBM650PowerUnit extends TileEntity implements IStrictEnergyStorage, IStrictEnergyAcceptor, IDataDevice, ITickable
{

	@Override
	public void update() {
		isValid();
	}
	
	private double buffer = 0;
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.ENERGY_STORAGE_CAPABILITY
				|| capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == Capabilities.DATA_DEVICE_CAPABILITY
				|| super.hasCapability(capability, facing);
	}


	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY || capability == Capabilities.DATA_DEVICE_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}
	
	public boolean isValid()
	{
		if(worldObj.getBlockState(pos.add(1,0,0)) != null && worldObj.getBlockState(pos.add(1,0,0)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(1,1,0)) != null && worldObj.getBlockState(pos.add(1,1,0)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,1,0)) != null && worldObj.getBlockState(pos.add(0,1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650PowerUnit.RENDER, 1));
				return true;
			}
		}
		if(worldObj.getBlockState(pos.add(0,0,1)) != null && worldObj.getBlockState(pos.add(0,0,1)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(0,1,1)) != null && worldObj.getBlockState(pos.add(0,1,1)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,1,0)) != null && worldObj.getBlockState(pos.add(0,1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650PowerUnit.RENDER, 2));
				return true;
			}
		}
		if(worldObj.getBlockState(pos.add(-1,0,0)) != null && worldObj.getBlockState(pos.add(-1,0,0)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(-1,1,0)) != null && worldObj.getBlockState(pos.add(-1,1,0)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,1,0)) != null && worldObj.getBlockState(pos.add(0,1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650PowerUnit.RENDER, 3));
				return true;
			}
		}
		if(worldObj.getBlockState(pos.add(0,0,-1)) != null && worldObj.getBlockState(pos.add(0,0,-1)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(0,1,-1)) != null && worldObj.getBlockState(pos.add(0,1,-1)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,1,0)) != null && worldObj.getBlockState(pos.add(0,1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650PowerUnit.RENDER, 4));
				return true;
			}		
		}
		else
			worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650PowerUnit.RENDER, 0));
		return false;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return buffer += amount;
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub
		buffer = energy;
	}

	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 5000;
	}

	public void tick() 
	{
		buffer -= 70;
	}

	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return "ibm_650_power_unit";
	}

	@Override
	public void performAction(String command) {
		// TODO Auto-generated method stub
		
	}

}
