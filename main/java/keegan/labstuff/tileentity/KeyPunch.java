package keegan.labstuff.tileentity;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class KeyPunch extends TileEntity implements IInventory 
{
	private ItemStack[] chestContents = new ItemStack[1];

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}


	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return chestContents[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
	if (stack != null)
	{
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
	return stack;}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return ItemStackHelper.getAndRemove(chestContents, index);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		chestContents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 1;
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
	
	public void write(String code)
	{
		int[] data = LabStuffUtils.convertToBinary(code);
		if(data.length <= 960)
		{
			ItemStack punchDeck = new ItemStack(LabStuffMain.punchCard, 1);
			if(punchDeck.getTagCompound() == null)
				punchDeck.setTagCompound(new NBTTagCompound());
			punchDeck.getTagCompound().setString("card", code);
			setInventorySlotContents(0, punchDeck);
		}
		else
		{
			int numberOfCards = (int) Math.ceil(data.length/960);
			NBTTagCompound deck = new NBTTagCompound();
			for(int i = 0; i < numberOfCards; i++)
			{
				deck.setString("card"+(deck.getSize()+1), code.substring(i*960, i*960+960));
			}
			ItemStack punchDeck = new ItemStack(LabStuffMain.punchDeck, 1);
			punchDeck.getTagCompound().setTag("cards", deck);
			setInventorySlotContents(0, punchDeck);
		}
	
	}
	
	public String readCard()
	{

		if(this.getStackInSlot(0) != null && ItemStack.areItemsEqual(getStackInSlot(0), new ItemStack(LabStuffMain.punchCard)))
		{
			return getStackInSlot(0).getTagCompound().getString("card").replace(";", "; \n");
		}
		if(this.getStackInSlot(0) != null && ItemStack.areItemsEqual(getStackInSlot(0), new ItemStack(LabStuffMain.punchDeck)))
		{
			String data = "";
			NBTTagCompound cards = (NBTTagCompound)getStackInSlot(0).getTagCompound().getTag("cards");
			for(int i = 0; i < cards.getKeySet().size(); i++)
			{
				data = data + cards.getString("card"+i);
			}
			return data.replace(";", "; \n");
		}
		return "";
	
	}

}
