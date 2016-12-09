package keegan.labstuff.world;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockRubberSapling;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenTree extends WorldGenerator
{
/** The minimum height of a generated tree. */
private final int minTreeHeight;
/** True if this tree should grow Vines. */
private final boolean vinesGrow;
/** The metadata value of the wood to use in tree generation. */
private final IBlockState metaWood;
/** The metadata value of the leaves to use in tree generation. */
private final IBlockState metaLeaves;
public WorldGenTree(boolean par1)
{
         this(par1, 4, LabStuffMain.blockRubberLog.getDefaultState(), LabStuffMain.blockRubberLeaves.getDefaultState(), false);
}
public WorldGenTree(boolean par1, int par2, IBlockState par3, IBlockState par4, boolean par5)
{
         super(par1);
         this.minTreeHeight = par2;
         this.metaWood = par3;
         this.metaLeaves = par4;
         this.vinesGrow = par5;
}
public boolean generate(World p_76484_1_, Random p_76484_2_, BlockPos pos)
{
    int l = p_76484_2_.nextInt(3) + this.minTreeHeight;
    boolean flag = true;

    if (pos.getY() >= 1 && pos.getY() + l + 1 <= 256)
    {
        byte b0;
        int k1;
        Block block;

        for (int i1 = pos.getY(); i1 <= pos.getY() + 1 + l; ++i1)
        {
            b0 = 1;

            if (i1 == pos.getY())
            {
                b0 = 0;
            }

            if (i1 >= pos.getY() + 1 + l - 2)
            {
                b0 = 2;
            }

            for (int j1 = pos.getX() - b0; j1 <= pos.getX() + b0 && flag; ++j1)
            {
                for (k1 = pos.getZ() - b0; k1 <= pos.getZ() + b0 && flag; ++k1)
                {
                    if (i1 >= 0 && i1 < 256)
                    {
                        block = p_76484_1_.getBlockState(new BlockPos(j1, i1, k1)).getBlock();

                        if (!this.isReplaceable(p_76484_1_, j1, i1, k1))
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = false;
                    }
                }
            }
        }

        if (!flag)
        {
            return false;
        }
        else
        {
            Block block2 = p_76484_1_.getBlockState(pos.down()).getBlock();

            boolean isSoil = block2.canSustainPlant(p_76484_1_.getBlockState(pos.down()), p_76484_1_, pos.down(), EnumFacing.UP, (BlockRubberSapling)LabStuffMain.blockRubberSapling);
            if (isSoil && pos.getY() < 256 - l - 1)
            {
                block2.onPlantGrow(p_76484_1_.getBlockState(pos.down()), p_76484_1_, pos.down(), pos);          
                b0 = 3;
                byte b1 = 0;
                int l1;
                int i2;
                int j2;
                int i3;

                for (k1 = pos.getY() - b0 + l; k1 <= pos.getY() + l; ++k1)
                {
                    i3 = k1 - (pos.getY() + l);
                    l1 = b1 + 1 - i3 / 2;

                    for (i2 = pos.getX() - l1; i2 <= pos.getX() + l1; ++i2)
                    {
                        j2 = i2 - pos.getX();

                        for (int k2 = pos.getZ() - l1; k2 <= pos.getZ() + l1; ++k2)
                        {
                            int l2 = k2 - pos.getZ();

                            if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || p_76484_2_.nextInt(2) != 0 && i3 != 0)
                            {
                                Block block1 = p_76484_1_.getBlockState(new BlockPos(i2, k1, k2)).getBlock();

                                if (block1.isAir(p_76484_1_.getBlockState(new BlockPos(i2,k1,k2)),p_76484_1_, new BlockPos(i2, k1, k2)) || block1.isLeaves(p_76484_1_.getBlockState(new BlockPos(i2,k1,k2)),p_76484_1_, new BlockPos(i2, k1, k2)))
                                {
                                    this.setBlockAndNotifyAdequately(p_76484_1_, new BlockPos(i2, k1, k2), this.metaLeaves);
                                }
                            }
                        }
                    }
                }
                for (k1 = 0; k1 < l; ++k1)
                {
                    block = p_76484_1_.getBlockState(pos).getBlock();

                    if (block.isAir(p_76484_1_.getBlockState(pos.add(0, k1, 0)), p_76484_1_, pos.add(0, k1, 0)) || block.isLeaves(p_76484_1_.getBlockState(pos.add(0, k1, 0)), p_76484_1_, pos.add(0, k1, 0)))
                    {
                        this.setBlockAndNotifyAdequately(p_76484_1_, pos.add(0, k1, 0), this.metaWood);
                    }
                }

                return true;
            }
            else
            {
            	return false;
            }
        }
    }
    else
    {
    	return false;
    }
}

	private boolean isReplaceable(World p_76484_1_, int j1, int i1, int k1) 
	{
		Block block = p_76484_1_.getBlockState(new BlockPos(j1, i1, k1)).getBlock();
		if(block.isAir(p_76484_1_.getBlockState(new BlockPos(j1,i1,k1)), p_76484_1_, new BlockPos(j1, i1, k1)) || block.equals(Blocks.GRASS) || block.equals(Blocks.DIRT) || block.isWood(p_76484_1_, new BlockPos(j1, i1, k1)) || block.isLeaves(p_76484_1_.getBlockState(new BlockPos(j1,i1,k1)), p_76484_1_, new BlockPos(j1, i1, k1)))
			return true;
		return false;
	}
}