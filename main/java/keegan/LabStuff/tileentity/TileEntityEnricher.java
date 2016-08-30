package keegan.labstuff.tileentity;

import java.util.*;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.*;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityEnricher extends TileEntityPowerConnection implements IInventory
{
	
	public ItemStack[] chestContents = new ItemStack[2];
	private boolean enriching = false;
	
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
		return 64;
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
		return true;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "Enricher";
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
        dust.writeToNBT(tagCompound);
        ore.writeToNBT(tagCompound);
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
        dust = ItemStack.loadItemStackFromNBT(tagCompound);
        ore = ItemStack.loadItemStackFromNBT(tagCompound);
	}
	private ItemStack ore;
	private ItemStack dust;
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
		ItemStack input = getStackInSlot(0);
		int[] inputID = OreDictionary.getOreIDs(input);
		if(input != null && inputID.length > 0)
		{
			for(int i = 0; i < inputID.length; i++)
			{
				String inputName = OreDictionary.getOreName(inputID[i]);
				if(inputName.startsWith("ore"))
				{
					ore = getStackInSlot(0);
					if(OreDictionary.doesOreNameExist("dust" + inputName.substring(3)))
					{
						ArrayList<ItemStack> dusts = OreDictionary.getOres("dust" + inputName.substring(3));
						if(dusts.size() > 0)
						{
							dust = dusts.get(1);
							if(getStackInSlot(1) == null || getStackInSlot(1).isItemEqual(dust))
							{
								worldObj.scheduleBlockUpdate(xCoord, yCoord, zCoord, blockType, 1200);
								enriching = true;
								getPowerSource().subtractPower(50, this);
							}
						}
					}
				}
			}
		}
	}

	public void completeEnrichment() 
	{
		enriching = false;	
		if(getStackInSlot(1) != null && getStackInSlot(1).isItemEqual(dust) && getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(ore))
		{
			decrStackSize(0, 1);
			setInventorySlotContents(1, new ItemStack(dust.getItem(), getStackInSlot(1).stackSize+2));
		}
		else if(getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(ore))
		{
			decrStackSize(0, 1);
			setInventorySlotContents(1, new ItemStack(dust.getItem(), 2));
		}
	}

}
