package keegan.labstuff.items;

import net.minecraft.entity.player.EntityPlayer;

public interface IArmorGravity
{
	/**
	 * Effective on worlds where the LabStuff gravity is less than Overworld normal.
	 * 
	 * @return value between 0 and 100  (other values have undefined results)
	 *	0 = standard LabStuff gravity for the world
	 *  50 = mid=way
	 *  100 = gravity as if the player was on the LabStuff (the armor makes the player heavy)
	 *  
	 *  The total effect will be cumulative for all pieces of IArmorGravity armor worn. 
	 */
	public int gravityOverrideIfLow(EntityPlayer p);
	
	/**
	 * Effective on worlds where the LabStuff gravity is higher than Overworld normal.
	 * 
	 * @return value between 0 and 100  (other values have undefined results)
	 *	0 = standard LabStuff gravity for the world
	 *  50 = mid=way
	 *  100 = gravity as if the player was on the LabStuff (the armor makes the player lighter / stronger)
	 *  
	 *  The total effect will be cumulative for all pieces of IArmorGravity armor worn. 
	 */
	public int gravityOverrideIfHigh(EntityPlayer p);
}