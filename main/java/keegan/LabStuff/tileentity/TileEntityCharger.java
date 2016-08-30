package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.recipes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCharger extends TileEntityPowerConnection implements IInventory
{

	public ItemStack[] chestContents = new ItemStack[1];
	
	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return chestContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		// TODO Auto-generated method stub
		return chestContents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
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
	public ItemStack getStackInSlotOnClosing(int slot) {
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
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
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "Charger";
	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void updateEntity()
	{
		ArrayList<Charge> charges = Recipes.getCharges();
		if(!worldObj.isRemote)
		{
			for(int i=0; i < charges.size(); i++)
			{
				if(getStackInSlot(0) != null && getStackInSlot(0).getItem().equals(charges.get(i).getDeadItem()))
				{
					getPowerSource().subtractPower(50, this);
					setInventorySlotContents(0, new ItemStack(charges.get(i).getChargedItem(), getStackInSlot(0).stackSize));
				}
			}
		}
	}

}
