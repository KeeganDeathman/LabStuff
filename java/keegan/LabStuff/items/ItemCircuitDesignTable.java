package keegan.labstuff.items;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockCircuitDesignTable;

public class ItemCircuitDesignTable extends Item 
{
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int side, float par8, float par9, float par10)
	{
		System.out.println(side);
		if(side==1)
		{
			world.setBlock(i, j+1, k,LabStuffMain.blockCircuitDesignTable);
			Block table = world.getBlock(i, j+1, k);
			int l = MathHelper.floor_double((double)(entityplayer.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
			((BlockCircuitDesignTable)table).onBlockPlacedBy(world, i, j, k, entityplayer, itemstack, l);
			return true;
		}
		return false;
	}
	
}
