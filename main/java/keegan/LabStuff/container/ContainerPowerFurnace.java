package keegan.labstuff.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import keegan.labstuff.slot.SlotPowerFurnace;
import keegan.labstuff.tileentity.TileEntityPowerFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPowerFurnace extends Container 
{
	private TileEntityPowerFurnace tile;
	private int lastBurnTime;
	
	public ContainerPowerFurnace(InventoryPlayer inv, TileEntityPowerFurnace tileEntity)
	{
		tile = tileEntity;
		
		this.bindPlayerInventory(inv);
		this.addSlotToContainer(new SlotPowerFurnace(tile, 0, 106, 205));
		
	}
	
	 public void addCraftingToCrafters(ICrafting p_75132_1_)
	 {
		 super.addCraftingToCrafters(p_75132_1_);
		 p_75132_1_.sendProgressBarUpdate(this, 0, this.tile.getBurnTime());
	 }
	 
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return this.tile.isUseableByPlayer(entityplayer);
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
	
	
	  /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastBurnTime != this.tile.getBurnTime())
            {
                icrafting.sendProgressBarUpdate(this, 0, this.tile.getBurnTime());
            }
        }
        this.lastBurnTime = tile.getBurnTime();
    }
    
	
	@SideOnly(Side.CLIENT)
    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
    {
        if (p_75137_1_ == 0)
        {
            this.tile.setBurnTime(p_75137_2_);
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
