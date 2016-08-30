package keegan.labstuff.slot;

import keegan.labstuff.container.ContainerCharger;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotCharger extends Slot 
{

	public SlotCharger(ContainerCharger par1ContainerCircuitMaker, IInventory par2iInventory, int par3, int par4, int par5) 
	{
		super(par2iInventory, par3, par4, par5);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return true;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}
}