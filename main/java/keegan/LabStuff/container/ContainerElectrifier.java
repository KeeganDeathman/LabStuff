package keegan.labstuff.container;

import keegan.labstuff.items.ItemCircuitDesign;
import keegan.labstuff.slot.*;
import keegan.labstuff.tileentity.TileEntityCircuitDesignTable;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerElectrifier extends Container
{

	protected TileEntityElectrifier tileEntity;
	
	public ContainerElectrifier(InventoryPlayer invPlayer, TileEntityElectrifier te)
	{
		tileEntity = te;
		
		bindPlayerInventory(invPlayer);
		
		this.addSlotToContainer(new SlotElectrifier(tileEntity, 0, 120, 125));
		this.addSlotToContainer(new SlotElectrifier(tileEntity, 1, 120, 201));
		this.addSlotToContainer(new SlotElectrifier(tileEntity, 2, 58, 201));
		this.addSlotToContainer(new SlotElectrifier(tileEntity, 3, 184, 201));
		
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
