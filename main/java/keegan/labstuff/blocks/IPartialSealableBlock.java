package keegan.labstuff.blocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPartialSealableBlock
{
    public boolean isSealed(World world, BlockPos pos, EnumFacing direction);
}