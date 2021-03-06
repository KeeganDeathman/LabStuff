package keegan.labstuff.world;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockBasicVenus;
import keegan.labstuff.config.ConfigManagerCore;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.BiomeProperties;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.common.BiomeDictionary;

public class BiomeGenVenusValley extends BiomeVenus
{
    public BiomeGenVenusValley(BiomeProperties properties)
    {
        super(properties);
        if (!ConfigManagerCore.disableBiomeTypeRegistrations)
        {
            BiomeDictionary.registerBiomeType(this, BiomeDictionary.Type.HOT, BiomeDictionary.Type.DRY, BiomeDictionary.Type.DEAD, BiomeDictionary.Type.SANDY);
        }
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
    {
        this.topBlock = LabStuffMain.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.ROCK_SOFT);
        this.fillerBlock = LabStuffMain.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.ROCK_SOFT);
        IBlockState stoneBlock = LabStuffMain.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.ROCK_HARD);
        IBlockState gravelBlock = LabStuffMain.venusBlock.getDefaultState().withProperty(BlockBasicVenus.BASIC_TYPE_VENUS, BlockBasicVenus.EnumBlockBasicVenus.ROCK_VOLCANIC_DEPOSIT);

        int i = worldIn.getSeaLevel();
        IBlockState topBlock = this.topBlock;
        IBlockState fillerBlock = this.fillerBlock;
        int j = -1;
        int k = (int)(p_180622_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = p_180622_4_ & 15;
        int i1 = p_180622_5_ & 15;

        for (int j1 = 255; j1 >= 0; --j1)
        {
            if (j1 <= rand.nextInt(5))
            {
                chunkPrimerIn.setBlockState(i1, j1, l, Blocks.BEDROCK.getDefaultState());
            }
            else
            {
                IBlockState iblockstate2 = chunkPrimerIn.getBlockState(i1, j1, l);

                if (iblockstate2.getBlock().getMaterial(iblockstate2) == Material.AIR)
                {
                    j = -1;
                }
                else if (iblockstate2.getBlock() == LabStuffMain.venusBlock)
                {
                    if (j == -1)
                    {
                        if (k <= 0)
                        {
                            topBlock = null;
                            fillerBlock = stoneBlock;
                        }
                        else if (j1 >= i - 4 && j1 <= i + 1)
                        {
                            topBlock = this.topBlock;
                            fillerBlock = this.fillerBlock;
                        }

                        j = k;

                        if (j1 >= i - 1)
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, topBlock);
                        }
                        else if (j1 < i - 7 - k)
                        {
                            topBlock = null;
                            fillerBlock = stoneBlock;
                            chunkPrimerIn.setBlockState(i1, j1, l, gravelBlock);
                        }
                        else
                        {
                            chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
                        }
                    }
                    else if (j > 0)
                    {
                        --j;
                        chunkPrimerIn.setBlockState(i1, j1, l, fillerBlock);
                    }
                }
            }
        }
    }
}