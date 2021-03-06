package keegan.labstuff.blocks;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * If block requires updates when oxygen is added and removed, implement this
 * into your block class.
 * 
 * It is recommended that blocks implementing this should be set to tick
 * randomly, and should override @updateTick() also to carry out oxygen checks. 
 */
public interface IOxygenReliantBlock
{
    public void onOxygenRemoved(World world, BlockPos pos);

    public void onOxygenAdded(World world, BlockPos pos);
}