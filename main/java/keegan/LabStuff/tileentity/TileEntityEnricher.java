package keegan.labstuff.tileentity;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.recipes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
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
	public void openInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Enricher";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
        return ItemStackHelper.getAndRemove(this.chestContents, index);
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

	        for (int i = 0; i < this.chestContents.length; ++i)
	        {
	            this.chestContents[i] = null;
	        }		
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
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
        if(dust != null)
        	dust.writeToNBT(tagCompound);
        if(ore != null)
        	ore.writeToNBT(tagCompound);
        
        return tagCompound;
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
	public void update()
	{
		super.update();
		if(getStackInSlot(0) != null && getPowerSource().powerInt >= 50)
		{
			ItemStack input = getStackInSlot(0);
			if(input != null)
			{
				for(Enrichment enrich : Recipes.enrichments)
				{
					if(enrich.getInput().equals(input.getItem()))
					{
						ore = getStackInSlot(0);
						dust = new ItemStack(enrich.getOutput());
						worldObj.scheduleUpdate(pos, LabStuffMain.blockEnricher, 1200);
						enriching = true;
						getPowerSource().subtractPower(50, this);
					}
				}
			}

		}
	}
	
	public boolean isEnriching()
	{
		return enriching;
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
