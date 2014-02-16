package keegan.LabStuff.blocks;

import java.util.Random;

import keegan.LabStuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCopperOre extends Block
{

	public BlockCopperOre(Material par2Material) 
	{
		super(par2Material);
	}
	
	public Item func_149650_a(int par1, Random par2Random, int par3)
    {
        return LabStuffMain.itemCopperIngot;
    }
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister register)
	{
		this.blockIcon = register.registerIcon("labstuff:blockCopperOre");
	}

}
