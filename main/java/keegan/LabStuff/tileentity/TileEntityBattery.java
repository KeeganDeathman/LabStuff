package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityBattery extends TileEntity implements ITickable, IEnergyWrapper
{
	private int power = 0;
	
	@Override
	public void update()
	{
		if(worldObj.isBlockIndirectlyGettingPowered(pos) > 0 && getPowerSource() != null)
		{
//			if(getPowerSource().getEnergy() > 500)
//			{
//				power += 500;
//				getPowerSource().setEnergy(getPowerSource().getEnergy()-500);
//			}
		}
		else
		{
			CableUtils.emit(this);
		}
	}


	private IStrictEnergyStorage getPowerSource() {
		for(int i = -1; i < 2; i++)
		{
			TileEntity tile = worldObj.getTileEntity(pos.add(i,0,0));
			if(tile != null)
			{
				if(CapabilityUtils.hasCapability(tile, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, EnumFacing.getFacingFromVector(i, 0, 0)))
				{
					return CapabilityUtils.getCapability(tile, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, EnumFacing.getFacingFromVector(i, 0, 0));
				}
			}
			
			tile = worldObj.getTileEntity(pos.add(0,i,0));
			if(tile != null)
			{
				if(CapabilityUtils.hasCapability(tile, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, EnumFacing.getFacingFromVector(0, i, 0)))
				{
					return CapabilityUtils.getCapability(tile, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, EnumFacing.getFacingFromVector(0, i, 0));
				}
			}
			
			tile = worldObj.getTileEntity(pos.add(0,0,i));
			if(tile != null)
			{
				if(CapabilityUtils.hasCapability(tile, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, EnumFacing.getFacingFromVector(0, 0, i)))
				{
					return CapabilityUtils.getCapability(tile, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, EnumFacing.getFacingFromVector(0, 0, i));
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.ENERGY_STORAGE_CAPABILITY
				|| capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == Capabilities.CABLE_OUTPUTTER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == Capabilities.CABLE_OUTPUTTER_CAPABILITY)
		{
			return (T)this;
		}
		
		return super.getCapability(capability, facing);
	}
	


	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return power;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub
		power = (int) energy;
	}

	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 10000000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return power += amount;
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		// TODO Auto-generated method stub
		return (worldObj.isBlockIndirectlyGettingPowered(pos) > 0);
	}


	@Override
	public boolean canOutputTo(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		if(worldObj.isBlockIndirectlyGettingPowered(pos) == 1)
			return EnumSet.noneOf(EnumFacing.class);
		return EnumSet.allOf(EnumFacing.class);
	}


	@Override
	public EnumSet<EnumFacing> getConsumingSides() {
		if(worldObj.isBlockIndirectlyGettingPowered(pos) == 0)
			return EnumSet.noneOf(EnumFacing.class);
		return EnumSet.allOf(EnumFacing.class);
	}


	@Override
	public double getMaxOutput() {
		// TODO Auto-generated method stub
		return 5000;
	}


	@Override
	public double removeEnergyFromProvider(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return power -= amount;
	}
}
