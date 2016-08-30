package keegan.labstuff.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCircuitDesign extends Item {

	public ItemCircuitDesign() {
		super();
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		this.itemIcon = register.registerIcon("labstuff:itemCircuitDesign");
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("Circuit Type: " + this.getUnlocalizedName().toUpperCase().replace(".", "").replace("ITEM", "").replace("CIRCUITDESIGN", ""));
	}

}
