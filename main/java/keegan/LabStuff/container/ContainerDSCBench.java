package keegan.labstuff.container;

import keegan.labstuff.tileentity.DSCBench;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerDSCBench extends Container
{

	private DSCBench tile;
	private EntityPlayer player;

	public ContainerDSCBench(InventoryPlayer inv, DSCBench tile)
	{
		this.tile = tile;
		this.player = inv.player;
		
		this.bindPlayerInventory(inv);
		
		this.addSlotToContainer(new Slot(tile, 0, 114, 188));
		this.addSlotToContainer(new Slot(tile, 1, 130, 188));
		this.addSlotToContainer(new Slot(tile, 2, 146, 188));
		this.addSlotToContainer(new Slot(tile, 3, 130, 194));
		
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 10 + i * 18));
				}
			}

			for (int i = 0; i < 9; i++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 70));
			}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotID)
	{
		ItemStack stack = null;
		Slot currentSlot = (Slot)inventorySlots.get(slotID);

		if(currentSlot != null && currentSlot.getHasStack())
		{
			ItemStack slotStack = currentSlot.getStack();
			stack = slotStack.copy();

			if(slotID != 0 && slotID != 1 && slotID != 2 && slotID != 3)
			{
					if(slotID >= 4 && slotID <= 30)
					{
						if(!mergeItemStack(slotStack, 31, inventorySlots.size(), false))
						{
							return null;
						}
					}
					else if(slotID > 30)
					{
						if(!mergeItemStack(slotStack, 4, 30, false))
						{
							return null;
						}
					}
					else {
						if(!mergeItemStack(slotStack, 4, inventorySlots.size(), true))
						{
							return null;
						}
					}
			}
			else {
				if(!mergeItemStack(slotStack, 4, inventorySlots.size(), true))
				{
					return null;
				}
			}

			if(slotStack.stackSize == 0)
			{
				currentSlot.putStack((ItemStack)null);
			}
			else {
				currentSlot.onSlotChanged();
			}

			if(slotStack.stackSize == stack.stackSize)
			{
				return null;
			}

			currentSlot.onPickupFromSlot(player, slotStack);
		}

		return stack;
	}


}
