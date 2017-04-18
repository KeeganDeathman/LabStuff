package keegan.labstuff.container;

import keegan.labstuff.slot.*;
import keegan.labstuff.tileentity.TileEntityWorkbench;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

public class ContainerWorkbench extends Container {

	
	private TileEntityWorkbench tileEntity;
	
	public ContainerWorkbench(InventoryPlayer invPlayer, TileEntityWorkbench te)
	{
		tileEntity = te;
		
		this.addSlotToContainer(new SlotInput(te, 0, 33, 127));
		this.addSlotToContainer(new SlotInput(te, 1, 51, 127));
		this.addSlotToContainer(new SlotInput(te, 2, 69, 127));
		this.addSlotToContainer(new SlotInput(te, 3, 87, 127));
		this.addSlotToContainer(new SlotInput(te, 4, 105, 127));
		this.addSlotToContainer(new SlotInput(te, 5, 33, 145));
		this.addSlotToContainer(new SlotInput(te, 6, 51, 145));
		this.addSlotToContainer(new SlotInput(te, 7, 69, 145));
		this.addSlotToContainer(new SlotInput(te, 8, 87, 145));
		this.addSlotToContainer(new SlotInput(te, 9, 105, 145));
		this.addSlotToContainer(new SlotInput(te, 10, 33, 163));
		this.addSlotToContainer(new SlotInput(te, 11, 51, 163));
		this.addSlotToContainer(new SlotInput(te, 12, 69, 163));
		this.addSlotToContainer(new SlotInput(te, 13, 87, 163));
		this.addSlotToContainer(new SlotInput(te, 14, 105, 163));
		this.addSlotToContainer(new SlotInput(te, 15, 33, 181));
		this.addSlotToContainer(new SlotInput(te, 16, 51, 181));
		this.addSlotToContainer(new SlotInput(te, 17, 69, 181));
		this.addSlotToContainer(new SlotInput(te, 18, 87, 181));
		this.addSlotToContainer(new SlotInput(te, 19, 105, 181));
		this.addSlotToContainer(new SlotInput(te, 20, 33, 199));
		this.addSlotToContainer(new SlotInput(te, 21, 51, 199));
		this.addSlotToContainer(new SlotInput(te, 22, 69, 199));
		this.addSlotToContainer(new SlotInput(te, 23, 87, 199));
		this.addSlotToContainer(new SlotInput(te, 24, 105, 199));
		this.addSlotToContainer(new SlotOutput(te, 25, 190, 163));
		




		
		bindPlayerInventory(invPlayer);
	}
		
	
	@Override
	public boolean canInteractWith(EntityPlayer arg0) {
		// TODO Auto-generated method stub
		return true;
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
