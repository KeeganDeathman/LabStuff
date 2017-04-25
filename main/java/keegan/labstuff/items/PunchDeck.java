package keegan.labstuff.items;

import keegan.labstuff.LabStuffMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class PunchDeck extends Item {
	
	public PunchDeck()
	{
		this.setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemstack, World world, EntityPlayer player, EnumHand hand)
	{
		if (!player.isSneaking() && ((NBTTagCompound)itemstack.getTagCompound().getTag("cards")).getSize() > 0) 
			player.openGui(LabStuffMain.instance, 28, world, 0,0,0);
		return new ActionResult(EnumActionResult.SUCCESS, itemstack);
	}
	
}
