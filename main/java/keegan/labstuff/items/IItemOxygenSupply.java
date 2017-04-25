package keegan.labstuff.items;

import net.minecraft.item.ItemStack;

public interface IItemOxygenSupply
{
    /*
     * Returns the amount of gas that this oxygen item is able to supply
     */
	public int discharge(ItemStack itemStack, int amount);

    public int getOxygenStored(ItemStack theItem);
}