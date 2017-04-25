package keegan.labstuff.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

public interface IWrenchable
{
	/**
	 * Called when a player shift-right clicks this block with a Wrench.
	 * @param player - the player who clicked the block
	 * @param side - the side the block was clicked on
	 * @return action that was performed
	 */
	public EnumActionResult onSneakRightClick(EntityPlayer player, EnumFacing side);

	/**
	 * Called when a player right clicks this block with a Wrench.
	 * @param player - the player who clicked the block
	 * @param side - the side the block was clicked on
	 * @return action that was performed
	 */
	public EnumActionResult onRightClick(EntityPlayer player, EnumFacing side);
}