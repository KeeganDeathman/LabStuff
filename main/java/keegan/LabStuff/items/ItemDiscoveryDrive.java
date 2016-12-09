package keegan.labstuff.items;

import net.minecraft.item.Item;

public class ItemDiscoveryDrive extends Item
{
	public ItemDiscoveryDrive()
	{
	}
	
	public String getDiscoveryName()
	{
		return this.getUnlocalizedName().replace("itemDiscovery", "");
	}
	

}
