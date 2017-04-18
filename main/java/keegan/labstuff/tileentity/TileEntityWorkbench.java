package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.IEnergyWrapper;
import keegan.labstuff.recipes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityWorkbench extends TileEntity  implements IEnergyWrapper
{
	private double energy = 0;
	private ItemStack[] contents = new ItemStack[26];
	
	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return energy;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub
		this.energy = energy;
	}

	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		energy = Math.min(getMaxEnergy(), energy += amount);
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		return energy;
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canOutputTo(EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 26;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return contents[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
			if(slot == 25)
			{
				for(ResearchItem item : Recipes.getResearchItems())
				{
					if(ItemStack.areItemStacksEqual(item.getOut(),this.getStackInSlot(slot)))
					{
						energy -= item.getPower();
						for(int i = 0; i < 25; i++)
						{
							if(item.getIn()[i] != null)
							{
								this.decrStackSize(i, item.getIn()[i].stackSize);
							}
						}
					}
				}
			}
			if (stack.stackSize <= amt)
			{
				setInventorySlotContents(slot, null);
			}
			else
			{
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0)
				{
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}
	
	//Sync
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound syncData = super.getUpdateTag();
		syncData.setDouble("energy", energy);
		return syncData;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}
				
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		System.out.println("Received");
		energy = pkt.getNbtCompound().getDouble("energy");
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = contents[index];
		
		if(index == 25)
		{
			for(ResearchItem item : Recipes.getResearchItems())
			{
				if(ItemStack.areItemStacksEqual(item.getOut(),stack))
				{
					energy -= item.getPower();
					for(int i = 0; i < 25; i++)
					{
						this.decrStackSize(i, item.getIn()[i].stackSize);
					}
				}
			}
		}


        return ItemStackHelper.getAndRemove(this.contents, index);
		}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) 
	{
		contents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return true;
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
		return true;
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
		return "Workbench";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return true;
	}

	public void recipeEntered(EntityPlayer player) {
		// TODO Auto-generated method stub
		ItemStack[] input = new ItemStack[25];
		for(int i = 0; i < 25; i++)
		{
			input[i] = contents[i];
		}
		this.setInventorySlotContents(25, null);
		if(Recipes.getResearchItemFromInputs(input) != null)
		{
			ResearchItem item = Recipes.getResearchItemFromInputs(input);
			if(player != null)
			{
				if(player.getCapability(Capabilities.RESEARCHER_CAPABILITY, null).getResearched().contains(item.getDependency()) || item.getDependency() == null)
				{
					if(item.getPower() <= getEnergy())
					{
						this.setInventorySlotContents(25, item.getOut());
					}
				}
			}
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
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == Capabilities.CABLE_OUTPUTTER_CAPABILITY)
		{
			return (T)this;
		}
		
		return super.getCapability(capability, side);
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EnumSet<EnumFacing> getConsumingSides() {
		// TODO Auto-generated method stub
		return EnumSet.allOf(EnumFacing.class);
	}

	@Override
	public double getMaxOutput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double removeEnergyFromProvider(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return 0;
	}


}
