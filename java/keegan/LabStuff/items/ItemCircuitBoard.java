package keegan.labstuff.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCircuitBoard extends Item 
{
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4)
	{
		if(this.getUnlocalizedName().contains("Basic"))
		{
			list.add("Circuit Type: BASIC");
		}
		else if(this.getUnlocalizedName().contains("Computer"))
		{
			list.add("Circuit Type: COMPUTER");
		}
	}
}
