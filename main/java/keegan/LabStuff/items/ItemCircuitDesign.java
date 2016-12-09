package keegan.labstuff.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;

public class ItemCircuitDesign extends Item {

	public ItemCircuitDesign() {
		super();
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Circuit Type: " + this.getUnlocalizedName().toUpperCase().replace(".", "").replace("ITEM", "").replace("CIRCUITDESIGN", ""));
	}

}
