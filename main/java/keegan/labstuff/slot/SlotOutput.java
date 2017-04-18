package keegan.labstuff.slot;

import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class SlotOutput extends Slot {

	public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
		// TODO Auto-generated constructor stub
		
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return false;
	}

}
