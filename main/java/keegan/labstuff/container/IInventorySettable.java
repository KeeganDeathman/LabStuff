package keegan.labstuff.container;

import net.minecraft.inventory.IInventory;

public interface IInventorySettable extends IInventory
{
    void setSizeInventory(int size);
}