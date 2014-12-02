package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.ItemSiliconBoule;
import keegan.labstuff.items.ItemSiliconBoulePart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class TileEntityCzo extends TileEntityPowerConnection implements IInventory
{
	
	public ItemStack[] chestContents = new ItemStack[3];
	private String spinning = "Still";
	
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
	public boolean isItemValidForSlot(int slot, ItemStack par1ItemStack)
	{
		if(par1ItemStack.getItem() instanceof ItemSiliconBoulePart || par1ItemStack.isItemEqual(new ItemStack(LabStuffMain.itemSiliconIngot)) || par1ItemStack.getItem() instanceof ItemSiliconBoule)
		{
			return true;
		}
		return false;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "Czo";
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
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < this.chestContents.length; i++)
        {
            ItemStack stack = this.chestContents[i];
            if (stack != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
        tagCompound.setString("Spinning", spinning);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.chestContents.length)
                this.chestContents[slot] = ItemStack.loadItemStackFromNBT(tag);
        }
        spinning = tagCompound.getString("Spinning");
	}
	
	private void grow(TileEntityPower powerSource)
	{
		if (spinning.equals("Still")) {
			if (getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(new ItemStack(LabStuffMain.itemRodMountedSiliconSeed)) && getStackInSlot(1) != null && getStackInSlot(1).isItemEqual(new ItemStack(LabStuffMain.itemSiliconIngot))) 
			{
				spinning = "spinning";
				System.out.println("Beginning spin " + spinning);
				worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, getBlockType(), 6000);
			}
		}
		else
		{
			System.out.println(spinning);
		}
	}
	
	public void completeGrowth()
	{
		if(spinning.equals("spinning"))
		{
			if (getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(new ItemStack(LabStuffMain.itemRodMountedSiliconSeed)) && getStackInSlot(1) != null && getStackInSlot(1).isItemEqual(new ItemStack(LabStuffMain.itemSiliconIngot))) 
			{
				decrStackSize(0, 1);
				setInventorySlotContents(2, new ItemStack(LabStuffMain.itemSiliconBoule));
				spinning = "Still";
			}
		}
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		TileEntityPower powerSource = getPowerSource();
		if(getStackInSlot(0) != null && !spinning.equals("spinning") && powerSource.getPower() > 600)
		{
				System.out.println("Attempting Spin");
				grow(powerSource);
		}
	}

}
