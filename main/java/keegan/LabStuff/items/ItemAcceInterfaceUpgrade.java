package keegan.labstuff.items;

import keegan.labstuff.tileentity.TileEntityAcceleratorInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemAcceInterfaceUpgrade extends Item
{
	
	@Override
	public EnumActionResult onItemUse(ItemStack held, EntityPlayer player, World world, BlockPos pos,  EnumHand hand, EnumFacing facing, float fx, float par8, float par9) {
		if(world.getTileEntity(pos) instanceof TileEntityAcceleratorInterface)
		{
			((TileEntityAcceleratorInterface)world.getTileEntity(pos)).activateAntiMatter();
			player.inventory.decrStackSize(player.inventory.currentItem, 1);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}