package keegan.labstuff.world;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockRubberSapling;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

public class WorldGenTree extends WorldGenerator
{
/** The minimum height of a generated tree. */
private final int minTreeHeight;
/** True if this tree should grow Vines. */
private final boolean vinesGrow;
/** The metadata value of the wood to use in tree generation. */
private final int metaWood;
/** The metadata value of the leaves to use in tree generation. */
private final int metaLeaves;
public WorldGenTree(boolean par1)
{
         this(par1, 4, 0, 0, false);
}
public WorldGenTree(boolean par1, int par2, int par3, int par4, boolean par5)
{
         super(par1);
         this.minTreeHeight = par2;
         this.metaWood = par3;
         this.metaLeaves = par4;
         this.vinesGrow = par5;
}
public boolean generate(World p_76484_1_, Random p_76484_2_, int p_76484_3_, int p_76484_4_, int p_76484_5_)
{
    int l = p_76484_2_.nextInt(3) + this.minTreeHeight;
    boolean flag = true;

    if (p_76484_4_ >= 1 && p_76484_4_ + l + 1 <= 256)
    {
        byte b0;
        int k1;
        Block block;

        for (int i1 = p_76484_4_; i1 <= p_76484_4_ + 1 + l; ++i1)
        {
            b0 = 1;

            if (i1 == p_76484_4_)
            {
                b0 = 0;
            }

            if (i1 >= p_76484_4_ + 1 + l - 2)
            {
                b0 = 2;
            }

            for (int j1 = p_76484_3_ - b0; j1 <= p_76484_3_ + b0 && flag; ++j1)
            {
                for (k1 = p_76484_5_ - b0; k1 <= p_76484_5_ + b0 && flag; ++k1)
                {
                    if (i1 >= 0 && i1 < 256)
                    {
                        block = p_76484_1_.getBlock(j1, i1, k1);

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
            Block block2 = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ - 1, p_76484_5_);

            boolean isSoil = block2.canSustainPlant(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, ForgeDirection.UP, (BlockRubberSapling)LabStuffMain.blockRubberSapling);
            if (isSoil && p_76484_4_ < 256 - l - 1)
            {
                block2.onPlantGrow(p_76484_1_, p_76484_3_, p_76484_4_ - 1, p_76484_5_, p_76484_3_, p_76484_4_, p_76484_5_);
                b0 = 3;
                byte b1 = 0;
                int l1;
                int i2;
                int j2;
                int i3;

                for (k1 = p_76484_4_ - b0 + l; k1 <= p_76484_4_ + l; ++k1)
                {
                    i3 = k1 - (p_76484_4_ + l);
                    l1 = b1 + 1 - i3 / 2;

                    for (i2 = p_76484_3_ - l1; i2 <= p_76484_3_ + l1; ++i2)
                    {
                        j2 = i2 - p_76484_3_;

                        for (int k2 = p_76484_5_ - l1; k2 <= p_76484_5_ + l1; ++k2)
                        {
                            int l2 = k2 - p_76484_5_;

                            if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || p_76484_2_.nextInt(2) != 0 && i3 != 0)
                            {
                                Block block1 = p_76484_1_.getBlock(i2, k1, k2);

                                if (block1.isAir(p_76484_1_, i2, k1, k2) || block1.isLeaves(p_76484_1_, i2, k1, k2))
                                {
                                    this.setBlockAndNotifyAdequately(p_76484_1_, i2, k1, k2, LabStuffMain.blockRubberLeaves, this.metaLeaves);
                                }
                            }
                        }
                    }
                }
                for (k1 = 0; k1 < l; ++k1)
                {
                    block = p_76484_1_.getBlock(p_76484_3_, p_76484_4_ + k1, p_76484_5_);

                    if (block.isAir(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_) || block.isLeaves(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_))
                    {
                        this.setBlockAndNotifyAdequately(p_76484_1_, p_76484_3_, p_76484_4_ + k1, p_76484_5_, LabStuffMain.blockRubberLog, this.metaWood);
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
		Block block = p_76484_1_.getBlock(j1, i1, k1);
		if(block.isAir(p_76484_1_, j1, i1, k1) || block.equals(Blocks.grass) || block.equals(Blocks.dirt) || block.isWood(p_76484_1_, j1, i1, k1) || block.isLeaves(p_76484_1_, j1, i1, k1))
			return true;
		return false;
	}
}