package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.labstuff.common.capabilities.Capabilities;
import keegan.labstuff.network.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityWindTurbine extends TileEntity implements ITickable, IEnergyWrapper
{
	
	private double buffer = 0;
	private int powerOutput = 0;
	
	public void update()
	{

		if(isProperPosition())
		{
			if(pos.getY() > 150)
			{
				powerOutput = 200;
			}
			else
			{
				powerOutput = 150;
			}
			if(worldObj.getWorldInfo().isRaining())
			{
				powerOutput += 50;
			}
			else if(worldObj.getWorldInfo().isThundering())
			{
				powerOutput += 100;
			}
			buffer += powerOutput;
			CableUtils.emit(this);
		}
	
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

	
	public boolean isProperPosition()
	{
		if (pos.getY() > 64 && worldObj.canBlockSeeSky(pos))
			return true;
		return false;
	}

	@Override
	public boolean canOutputTo(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return buffer;
	}


	@Override
	public void setEnergy(double energy) 
	{
		// TODO Auto-generated method stub
		buffer = energy;
	}


	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return EnumSet.of(EnumFacing.DOWN);
	}


	@Override
	public EnumSet<EnumFacing> getConsumingSides() {
		// TODO Auto-generated method stub
		return EnumSet.noneOf(EnumFacing.class);
	}


	@Override
	public double getMaxOutput() {
		// TODO Auto-generated method stub
		return powerOutput;
	}


	@Override
	public double removeEnergyFromProvider(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return buffer - amount;
	}
	
}
