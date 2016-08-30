package keegan.labstuff.items;

import keegan.labstuff.tileentity.TileEntityAcceleratorInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class ItemAcceInterfaceUpgrade extends Item
{
	
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int par8, float par9, float par10, float par11)
	{
		if(world.getTileEntity(x, y, z) instanceof TileEntityAcceleratorInterface)
		{
			((TileEntityAcceleratorInterface)world.getTileEntity(x, y, z)).activateAntiMatter();
			player.inventory.decrStackSize(player.inventory.currentItem, 1);
			return true;
		}
		return false;
	}
}