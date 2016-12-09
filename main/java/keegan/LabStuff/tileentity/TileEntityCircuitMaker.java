package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.ItemCircuitBoard;
import keegan.labstuff.items.ItemPartialCircuitBoard;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCircuitMaker extends TileEntity implements IInventory
{
	private ItemStack[] chestContents  = new ItemStack[5];
	
	
	public TileEntityCircuitMaker()
	{
		
	}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < chestContents.length; i++) 
		{
			ItemStack stack = chestContents[i];
			if (stack != null) 
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		
		return tagCompound;
	}
	

	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); i++) 
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < chestContents.length) 
			{
				chestContents[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public int getSizeInventory() 
	{
		// TODO Auto-generated method stub
		return chestContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		// TODO Auto-generated method stub
		return chestContents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) 
	{
		// TODO Auto-generated method stub
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
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) 
	{
		chestContents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		
	}

	@Override
	public int getInventoryStackLimit() 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
	{
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) 
	{
		if(stack.getItem() instanceof ItemPartialCircuitBoard || stack.getItem() == LabStuffMain.itemCircuitBoardPlate || stack.getItem() instanceof ItemCircuitBoard)
			return true;
		else
			return false;
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
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return ItemStackHelper.getAndRemove(chestContents, index);
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
		for(ItemStack stack : chestContents)
			stack = null;
	}
	
}
