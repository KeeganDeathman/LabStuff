package keegan.labstuff.items;

import net.minecraft.item.Item;

public class ItemDiscoveryDrive extends Item
{
	public ItemDiscoveryDrive()
	{
		this.setTextureName("labstuff:discovdrive");
	}
	
	public String getDiscoveryName()
	{
		return this.getUnlocalizedName().replace("itemDiscovery", "");
	}
	

}
