package keegan.labstuff.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPartialCircuitBoard extends Item 
{
	
	private IIcon drilledBoard;
	private IIcon etchedBoard;
	
	@SideOnly(Side.CLIENT)
	public void registerIcon(IIconRegister iconRegister)
	{
		this.drilledBoard = this.itemIcon = iconRegister.registerIcon("labstuff:itemDrilledCircuitBoard");
		this.etchedBoard = iconRegister.registerIcon("labstuff:itemEtchedCircuitBoard");
	}
	
	@Override
	public IIcon getIcon(ItemStack stack, int pass)
	{
		if(this.getUnlocalizedName().equals("itemBasicDrilledCircuitBoard"))
		{
			return this.drilledBoard;
		}
		else if(this.getUnlocalizedName().equals("itemBasicEtchedCircuitBoard"))
		{
			return this.etchedBoard;
		}
		return this.drilledBoard;
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		if(this.getUnlocalizedName().contains("Basic"))
		{
			par3List.add("Circuit Type: BASIC");
		}
	}
}
