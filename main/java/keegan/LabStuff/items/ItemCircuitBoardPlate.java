package keegan.labstuff.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCircuitBoardPlate extends Item 
{

	public ItemCircuitBoardPlate(int par1) {
		super();
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		this.itemIcon = register.registerIcon("LabStuff:itemCircuitBoardPlate");
	}

}
