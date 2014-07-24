package keegan.labstuff.container;

import keegan.labstuff.slot.SlotCircuitMakerCircuitBoardOutput;
import keegan.labstuff.slot.SlotCircuitMakerCircuitDesignInput;
import keegan.labstuff.slot.SlotCircuitMakerCircuitPlateInput;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCircuitMaker extends Container {

protected TileEntityCircuitMaker tileEntity;
	
	public ContainerCircuitMaker(InventoryPlayer invPlayer, TileEntityCircuitMaker te)
	{
		tileEntity = te;
		
		this.bindPlayerInventory(invPlayer);
		
		this.addSlotToContainer(new SlotCircuitMakerCircuitDesignInput(this, te, 0, 189, 155));
		//Drill
		this.addSlotToContainer(new SlotCircuitMakerCircuitPlateInput(this, te, 1, 29, 128));
		this.addSlotToContainer(new SlotCircuitMakerCircuitBoardOutput(this, te, 2, 77, 128));
		//Etcher
		this.addSlotToContainer(new SlotCircuitMakerCircuitPlateInput(this, te, 3, 66, 173));
		this.addSlotToContainer(new SlotCircuitMakerCircuitBoardOutput(this, te, 4, 107, 173));
		
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
