package keegan.labstuff.items;

import keegan.labstuff.common.capabilities.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemWrench extends Item 
{

	@Override
	public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if(!world.isRemote)
		{
			TileEntity tile = world.getTileEntity(pos);
			if(CapabilityUtils.hasCapability(tile, Capabilities.WRENCHABLE_CAPABILITY, side))
			{
				IWrenchable config = CapabilityUtils.getCapability(tile, Capabilities.WRENCHABLE_CAPABILITY, side);

				if(player.isSneaking())
				{
					return config.onSneakRightClick(player, side);
				}
				else {
					return config.onRightClick(player, side);
				}
		
			}
		}
		return EnumActionResult.PASS;
	}
}
