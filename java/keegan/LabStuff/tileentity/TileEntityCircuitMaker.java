package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityCircuitMaker extends TileEntity implements IInventory
{
	private ItemStack[] inv  = new ItemStack[5];
	private World world;
	
	
	public TileEntityCircuitMaker(World world)
	{
		this.world = world;
	}
	
	
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) 
		{
			ItemStack stack = inv[i];
			if (stack != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setByte("Slot", (byte) i);
			stack.writeToNBT(tag);
			itemList.appendTag(tag);
			}
			}
			tagCompound.setTag("Inventory", itemList);
	}
	

	

	public void readFromNBT(NBTTagCompound tagCompound)
	{
		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); i++) 
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) 
			{
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public int getSizeInventory() 
	{
		// TODO Auto-generated method stub
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		// TODO Auto-generated method stub
		return inv[slot];
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
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) 
	{
		inv[slot] = itemstack;
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
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if(slot == 0)
		{
			if(stack.getItem() == LabStuffMain.itemBasicCircuitDesign)
			{
				return true;
			}
			return false;
		}
		else if(slot == 1 || slot == 3)
		{
			if(stack.getItem() == LabStuffMain.itemCircuitBoardPlate || stack.getItem() == LabStuffMain.itemBasicDrilledCircuitBoard || stack.getItem() == LabStuffMain.itemBasicEtchedCircuitBoard)
			{
				return true;
			}
			return false;
		}
		else if(slot == 2 || slot == 4)
		{
			if(stack.getItem() == LabStuffMain.itemBasicDrilledCircuitBoard || stack.getItem() == LabStuffMain.itemBasicEtchedCircuitBoard || stack.getItem() == LabStuffMain.itemBasicCircuitBoard)
			{
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "Circuit Maker";
	}



	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}
}
