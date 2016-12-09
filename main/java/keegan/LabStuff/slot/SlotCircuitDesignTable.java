package keegan.labstuff.slot;

import keegan.labstuff.container.ContainerCircuitDesignTable;
import keegan.labstuff.items.ItemCircuitDesign;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCircuitDesignTable extends Slot {

	public SlotCircuitDesignTable(ContainerCircuitDesignTable par1ContainerCircuitDesignTable, IInventory par2iInventory, int par3, int par4, int par5) 
	{
		super(par2iInventory, par3, par4, par5);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if(par1ItemStack.getItem() == Items.PAPER || par1ItemStack.getItem() instanceof ItemCircuitDesign)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}
