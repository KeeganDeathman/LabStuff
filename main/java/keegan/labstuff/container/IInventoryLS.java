package keegan.labstuff.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

/**
 * An interface for other mods to access the Labstuff
 * extended inventory slots.
 * 
 * (All normal IInventory methods will work)
 */
public interface IInventoryLS extends IInventory
{
    /**
     * Drop only the LabStuff items from the player's inventory.
     * @param player
     */
	public void dropExtendedItems(EntityPlayer player);

    /**
     * Make the implementing inventory a copy of the specified extended inventory.
     * @param par1InventoryPlayer  The inventory to copy
     */
    public void copyInventory(IInventoryLS par1InventoryPlayer);
}