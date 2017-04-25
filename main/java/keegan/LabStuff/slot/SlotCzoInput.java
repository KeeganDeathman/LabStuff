package keegan.labstuff.slot;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.container.ContainerCzo;
import keegan.labstuff.items.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotCzoInput extends Slot 
{

	public SlotCzoInput(ContainerCzo par1ContainerCircuitMaker, IInventory par2iInventory, int par3, int par4, int par5) 
	{
		super(par2iInventory, par3, par4, par5);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if(par1ItemStack.getItem() instanceof ItemSiliconBoulePart || par1ItemStack.isItemEqual(new ItemStack(LabStuffMain.itemSiliconIngot)) || par1ItemStack.getItem() instanceof ItemSiliconBoule)
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