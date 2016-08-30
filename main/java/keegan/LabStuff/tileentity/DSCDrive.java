package keegan.labstuff.tileentity;

import keegan.labstuff.items.ItemDiscoveryDrive;
import keegan.labstuff.recipes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class DSCDrive extends DSCPart implements IInventory
{
	
	private ItemStack[] chestContents;
	private TileEntityDSCCore core;
	
	public DSCDrive()
	{
		chestContents = new ItemStack[1];
	}
	
	@Override
	public void updateEntity()
	{
		super.updateEntity();
	}
	
	public void install(AcceleratorDiscovery discov)
	{
			if(core != null)
			this.getNetwork().sendMessage(new DSCPackage(core, this, "install-"+Recipes.accelDiscoveries.indexOf(discov)));
	}
	
	@Override
	public void performAction(String msg, DSCPart sender)
	{
		if(msg.equals("installed"))
		{
			decrStackSize(0, 1);
		}
		if(msg.equals("registered"))
			this.core = (TileEntityDSCCore)sender;
	}

	@Override
	public int getSizeInventory()
	{
		// TODO Auto-generated method stub
		return 1;
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
		return "DSCDrive";
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
		return 1;
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
	{
		// TODO Auto-generated method stub
		if(stack.getItem() instanceof ItemDiscoveryDrive)
			return true;
		return false;
	}
	
}
