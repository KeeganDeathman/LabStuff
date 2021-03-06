package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.IShiftDescription;
import keegan.labstuff.tileentity.TileEntityScreen;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockScreen extends BlockAdvanced implements IShiftDescription, IPartialSealableBlock, ITileEntityProvider, ISortableBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool LEFT = PropertyBool.create("left");
    public static final PropertyBool RIGHT = PropertyBool.create("right");
    public static final PropertyBool UP = PropertyBool.create("up");
    public static final PropertyBool DOWN = PropertyBool.create("down");

    protected static final float boundsFront = 0.094F;
    protected static final float boundsBack = 1.0F - boundsFront;
    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0F, 0F, 0F, 1.0F, boundsBack, 1.0F);
    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0F, boundsFront, 0F, 1.0F, 1.0F, 1.0F);
    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0F, 0F, boundsFront, 1.0F, 1.0F, 1.0F);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0F, 0F, 0F, 1.0F, 1.0F, boundsBack);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(boundsFront, 0F, 0F, 1.0F, 1.0F, 1.0F);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0F, 0F, 0F, boundsBack, 1.0F, 1.0F);
    
    //Metadata: 0-5 = direction of screen back;  bit 3 = reserved for future use
    public BlockScreen(String assetName)
    {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(LEFT, false).withProperty(RIGHT, false).withProperty(UP, false).withProperty(DOWN, false));
        this.setHardness(0.1F);
        this.setSoundType(SoundType.GLASS);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing direction)
    {
        return direction.ordinal() != getMetaFromState(world.getBlockState(pos));
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        ((TileEntityScreen) worldIn.getTileEntity(pos)).breakScreen(state);
        super.breakBlock(worldIn, pos, state);
    }

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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getIndex();
        worldIn.setBlockState(pos, getStateFromMeta(change), 3);
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int change = world.getBlockState(pos).getValue(FACING).rotateY().getIndex();
        world.setBlockState(pos, this.getStateFromMeta(change), 3);
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityScreen();
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityScreen)
        {
            ((TileEntityScreen) tile).changeChannel();
            return true;
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityScreen)
        {
            ((TileEntityScreen) tile).refreshConnections(true);
        }
    }

    @Override
    public String getShiftDescription(int meta)
    {
        return LabStuffUtils.translate(this.getUnlocalizedName() + ".description");
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public boolean isSealed(World worldIn, BlockPos pos, EnumFacing direction)
    {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(FACING))
        {
        case EAST:
            return EAST_AABB;
        case WEST:
            return WEST_AABB;
        case SOUTH:
            return SOUTH_AABB;
        case NORTH:
            return NORTH_AABB;
        case DOWN:
            return DOWN_AABB;
        case UP:
        default:
            return UP_AABB;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getFront(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, LEFT, RIGHT, UP, DOWN);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        TileEntityScreen screen = (TileEntityScreen) worldIn.getTileEntity(pos);
        return state.withProperty(LEFT, screen.connectedLeft)
                .withProperty(RIGHT, screen.connectedRight)
                .withProperty(UP, screen.connectedUp)
                .withProperty(DOWN, screen.connectedDown);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}