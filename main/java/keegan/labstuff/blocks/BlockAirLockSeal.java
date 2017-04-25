package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.util.EnumSortCategoryBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockAirLockSeal extends BlockBreakable implements IPartialSealableBlock, ISortableBlock
{
    public static final PropertyEnum CONNECTION_TYPE = PropertyEnum.create("connection", EnumAirLockSealConnection.class);

    public enum EnumAirLockSealConnection implements IStringSerializable
    {
        X("x"),
        Z("z"),
        FLAT("flat");

        private final String name;

        EnumAirLockSealConnection(String name)
        {
            this.name = name;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockAirLockSeal(String assetName)
    {
        super(Material.IRON, false);
        this.setTickRandomly(true);
        this.setHardness(1000.0F);
        this.setSoundType(SoundType.METAL);
        this.setRegistryName("labstuff",assetName);
    }

//    @Override
//    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getCollisionBoundingBox(worldIn, pos, state);
//    }
//
//    @Override
//    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos)
//    {
//        this.setBlockBoundsBasedOnState(worldIn, pos);
//        return super.getSelectedBoundingBox(worldIn, pos);
//    }
//
//    @Override
//    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
//    {
//        switch (getConnection(worldIn, pos))
//        {
//        case FLAT:
//            this.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 0.75F, 1.0F);
//            break;
//        case X:
//            this.setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
//            break;
//        case Z:
//            this.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 0.75F);
//            break;
//        }
//    }


    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return true;
    }

//    @Override
//    public Item getItem(World world, BlockPos pos)
//    {
//        return null;
//    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, CONNECTION_TYPE);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(CONNECTION_TYPE, getConnection(worldIn, pos));
    }

    private EnumAirLockSealConnection getConnection(IBlockAccess worldIn, BlockPos pos)
    {
        EnumAirLockSealConnection connection;

        Block frameID = LabStuffMain.airLockFrame;
        Block sealID = LabStuffMain.airLockSeal;

        Block idXMin = worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();
        Block idXMax = worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock();

        if (idXMin != frameID && idXMax != frameID && idXMin != sealID && idXMax != sealID)
        {
            connection = EnumAirLockSealConnection.X;
        }
        else
        {
            int adjacentCount = 0;

            for (EnumFacing dir : EnumFacing.values())
            {
                if (dir != EnumFacing.UP && dir != EnumFacing.DOWN)
                {
                    Block blockID = worldIn.getBlockState(pos.offset(dir)).getBlock();

                    if (blockID == LabStuffMain.airLockFrame || blockID == LabStuffMain.airLockSeal)
                    {
                        adjacentCount++;
                    }
                }
            }

            if (adjacentCount == 4)
            {
                connection = EnumAirLockSealConnection.FLAT;
            }
            else
            {
                connection = EnumAirLockSealConnection.Z;
            }
        }

        return connection;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState();
    }
}