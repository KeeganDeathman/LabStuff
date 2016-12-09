package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.recipes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class TileEntityCharger extends TileEntityPowerConnection implements IInventory, ITickable
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
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void update()
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

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Charger";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return true;
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
		// TODO Auto-generated method stub
		for(ItemStack stack : chestContents)
			stack = null;
	}

}
