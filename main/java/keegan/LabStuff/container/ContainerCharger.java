package keegan.labstuff.container;

import keegan.labstuff.slot.SlotCharger;
import keegan.labstuff.tileentity.TileEntityCharger;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerCharger extends Container
{
	TileEntityCharger tileEntity;
	
	public ContainerCharger(InventoryPlayer inv, TileEntityCharger te)
	{		
		tileEntity = te;
	
		this.bindPlayerInventory(inv);
		
		this.addSlotToContainer(new SlotCharger(this, te, 0, 106, 205));
		
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return this.tileEntity.isUseableByPlayer(entityplayer);
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
	public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack stack = null;
        Slot slotObject = (Slot) inventorySlots.get(slot);

        //null checks and checks if the item can be stacked (maxStackSize > 1)
        if (slotObject != null && slotObject.getHasStack()) 
        {
                ItemStack stackInSlot = slotObject.getStack();
                stack = stackInSlot.copy();

                //merges the item into player inventory since its in the tileEntity
                if (slot < 9) {
                        if (!this.mergeItemStack(stackInSlot, 0, 35, true)) {
                                return null;
                        }
                }
                //places it into the tileEntity is possible since its in the player inventory
                else if (!this.mergeItemStack(stackInSlot, 0, 9, false)) {
                        return null;
                }

                if (stackInSlot.stackSize == 0) {
                        slotObject.putStack(null);
                } else {
                        slotObject.onSlotChanged();
                }

                if (stackInSlot.stackSize == stack.stackSize) {
                        return null;
                }
                slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return stack;
	}
}
