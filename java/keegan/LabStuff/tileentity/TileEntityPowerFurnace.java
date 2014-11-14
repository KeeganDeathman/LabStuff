package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.BlockPowerFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;

public class TileEntityPowerFurnace extends TileEntityPower implements IInventory
{
	
	private ItemStack[] chestContents = new ItemStack[1];
	private int burnTime;
	
	public TileEntityPowerFurnace()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
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
		tagCompound.setInteger("burnTime", getBurnTime());
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
		setBurnTime(tagCompound.getInteger("burnTime"));
	}
	
	@Override
	public void updateEntity()
	{
		if(worldObj.isRemote == false)
		{
			switch(getBurnTime())
			{
				default:
					System.out.println("Burn time " + getBurnTime());
					setBurnTime(getBurnTime() - 1);
					TileEntity tileAbove = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
					if(tileAbove instanceof TileEntityPower)
						((TileEntityPower)tileAbove).addPower(1, this);
					((BlockPowerFurnace)worldObj.getBlock(xCoord, yCoord, zCoord)).setOn(worldObj, xCoord, yCoord, zCoord, true);
					System.out.println("Burn time " + getBurnTime());
				case 0:
					System.out.println("Burn time " + getBurnTime());
					if(getStackInSlot(0) != null)
					{
						setBurnTime(getBurnTime() + TileEntityFurnace.getItemBurnTime(getStackInSlot(0)));
						decrStackSize(0, 1);
						((BlockPowerFurnace)worldObj.getBlock(xCoord, yCoord, zCoord)).setOn(worldObj, xCoord, yCoord, zCoord, true);
					}
					else
						((BlockPowerFurnace)worldObj.getBlock(xCoord, yCoord, zCoord)).setOn(worldObj, xCoord, yCoord, zCoord, false);
					System.out.println("Burn time " + getBurnTime());				
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
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "Power Furnace";
	}



	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack item) 
	{
		return TileEntityFurnace.isItemFuel(item);
	}

	public int getBurnTime() {
		return burnTime;
	}

	public void setBurnTime(int burnTime) {
		this.burnTime = burnTime;
	}

}
