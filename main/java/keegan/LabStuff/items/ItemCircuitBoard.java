package keegan.labstuff.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCircuitBoard extends Item 
{
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Circuit Type: " + this.getUnlocalizedName().replace(".", "").replace("item", "").replace("Circuitboard", "").toUpperCase());
	}
}
