package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.recipes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class DSCBench extends DSCPart implements IInventory
{

	private ItemStack[] chestContents = new ItemStack[4];
	private TileEntityDSCCore core;
	
	@Override
	public int getSizeInventory()
	{
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public ItemStack getStackInSlot(int p_70301_1_)
	{
		// TODO Auto-generated method stub
		return chestContents[p_70301_1_];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt)
	{
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
		return chestContents[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		chestContents[slot] = stack;
	}

	@Override
	public String getInventoryName()
	{
		return "DSCBench";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getInventoryStackLimit()
	{
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer p_70300_1_)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int p_94041_1_, ItemStack stack)
	{// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public void performAction(String msg, DSCPart sender)
	{
		if(msg.equals("registered"))
			this.core = (TileEntityDSCCore)sender;
	}

	public ArrayList<AcceleratorDiscovery> getInstalled()
	{
		return core.getDiscovered();
	}

	public void make(DiscoveryItem discov)
	{
		if(getInstalled().size() > 0 && getInstalled().contains(discov.getDependency()))
			{
				if(getStackInSlot(0) != null && discov.getIngredients1().isItemEqual(getStackInSlot(0)) && discov.getIngredients1().stackSize <= getStackInSlot(0).stackSize)
				{
					if(getStackInSlot(1) != null && discov.getIngredients2().isItemEqual(getStackInSlot(1)) && discov.getIngredients2().stackSize <= getStackInSlot(1).stackSize)
					{
						if(getStackInSlot(2) != null && discov.getIngredients3().isItemEqual(getStackInSlot(2)) && discov.getIngredients3().stackSize <= getStackInSlot(2).stackSize)
						{
							decrStackSize(0, discov.getIngredients1().stackSize);
							decrStackSize(1, discov.getIngredients2().stackSize);
							decrStackSize(2, discov.getIngredients3().stackSize);
							setInventorySlotContents(3, discov.getResult());
						}
					}
				}
			}
	}

}
