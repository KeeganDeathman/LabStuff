package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.dimension.WorldProviderSpaceStation;
import keegan.labstuff.items.IShiftDescription;
import keegan.labstuff.tileentity.TileEntityThruster;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockSpinThruster extends BlockAdvanced implements IShiftDescription, ITileEntityProvider, ISortableBlock
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.4F, 0.8F, 0.8F, 1.0F);
    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.2F, 0.2F, 0.0F, 0.8F, 0.8F, 0.6F);
    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.4F, 0.2F, 0.2F, 1.0F, 0.8F, 0.8F);
    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0F, 0.2F, 0.2F, 0.6F, 0.8F, 0.8F);

    public BlockSpinThruster(String assetName)
    {
        super(Material.CIRCUITS);
        this.setHardness(0.1F);
        this.setSoundType(SoundType.WOOD);
        this.setRegistryName("labstuff",assetName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
        default:
        case NORTH:
            return NORTH_AABB;
        }
    }

    private static boolean isBlockSolidOnSide(World world, BlockPos pos, EnumFacing direction)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isSideSolid(state, world, pos, direction);
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
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.WEST), EnumFacing.EAST)
                || BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.EAST), EnumFacing.WEST)
                || BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.NORTH), EnumFacing.SOUTH)
                || BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.SOUTH), EnumFacing.NORTH);
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getStateFromMeta(facing.getHorizontalIndex());
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (getMetaFromState(state) == 0)
        {
            this.onBlockAdded(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        int metadata = getMetaFromState(state);

        BlockPos baseBlock;
        switch (metadata)
        {
        case 1:
            baseBlock = pos.offset(EnumFacing.WEST);
            break;
        case 2:
            baseBlock = pos.offset(EnumFacing.EAST);
            break;
        case 3:
            baseBlock = pos.offset(EnumFacing.NORTH);
            break;
        case 4:
            baseBlock = pos.offset(EnumFacing.SOUTH);
            break;
        default:
            this.dropTorchIfCantStay(worldIn, pos);
            return;
        }

        if (!worldIn.isRemote)
        {
            if (worldIn.provider instanceof WorldProviderSpaceStation)
            {
                ((WorldProviderSpaceStation) worldIn.provider).getSpinManager().refresh(baseBlock, true);
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        if (this.dropTorchIfCantStay(worldIn, pos))
        {
            final int var6 = getMetaFromState(state) & 7;
            boolean var7 = false;

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.WEST), EnumFacing.EAST) && var6 == 1)
            {
                var7 = true;
            }

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.EAST), EnumFacing.WEST) && var6 == 2)
            {
                var7 = true;
            }

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.NORTH), EnumFacing.SOUTH) && var6 == 3)
            {
                var7 = true;
            }

            if (!BlockSpinThruster.isBlockSolidOnSide(worldIn, pos.offset(EnumFacing.SOUTH), EnumFacing.NORTH) && var6 == 4)
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }

        if (!worldIn.isRemote)
        {
            if (worldIn.provider instanceof WorldProviderSpaceStation)
            {
                ((WorldProviderSpaceStation) worldIn.provider).getSpinManager().refresh(pos, true);
            }
        }
    }

    private boolean dropTorchIfCantStay(World worldIn, BlockPos pos)
    {
        if (!this.canPlaceBlockAt(worldIn, pos))
        {
            if (worldIn.getBlockState(pos).getBlock() == this)
            {
                this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
        else
        {
            return true;
        }
    }

//    @Override
//    public RayTraceResult collisionRayTrace(World worldIn, BlockPos pos, Vec3d start, Vec3d end)
//    {
//        float var8 = 0.3F;
//
//        EnumFacing facing = worldIn.getBlockState(pos).getValue(BlockMachine.FACING);
//
//        switch (facing)
//        {
//        case NORTH:
//            this.setBlockBounds(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
//            break;
//        case EAST:
//            this.setBlockBounds(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
//            break;
//        case SOUTH:
//            this.setBlockBounds(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
//            break;
//        case WEST:
//            this.setBlockBounds(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
//            break;
//        }
//
//        return super.collisionRayTrace(worldIn, pos, start, end);
//    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        //TODO this is torch code as a placeholder, still need to adjust positioning and particle type
        //Also make small thrust sounds
        if (worldIn.provider instanceof WorldProviderSpaceStation)
        {
            if (((WorldProviderSpaceStation) worldIn.provider).getSpinManager().thrustersFiring || rand.nextInt(80) == 0)
            {
                final int var6 = getMetaFromState(stateIn) & 7;
                final double var7 = pos.getX() + 0.5F;
                final double var9 = pos.getY() + 0.7F;
                final double var11 = pos.getZ() + 0.5F;
                final double var13 = 0.2199999988079071D;
                final double var15 = 0.27000001072883606D;

                if (var6 == 1)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 - var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 2)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7 + var15, var9 + var13, var11, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 3)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 - var15, 0.0D, 0.0D, 0.0D);
                }
                else if (var6 == 4)
                {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var7, var9 + var13, var11 + var15, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        EnumFacing currentFacing = world.getBlockState(pos).getValue(FACING);
        for (EnumFacing nextFacing = currentFacing.rotateY(); ; nextFacing = nextFacing.rotateY())
        {
            if (BlockSpinThruster.isBlockSolidOnSide(world, pos.offset(nextFacing.getOpposite()), nextFacing))
            {
                world.setBlockState(pos, getStateFromMeta(nextFacing.getHorizontalIndex()), 2);
                break;
            }

            if (nextFacing == currentFacing)
            {
                break;
            }
        }

        if (world.provider instanceof WorldProviderSpaceStation && !world.isRemote)
        {
            WorldProviderSpaceStation worldOrbital = (WorldProviderSpaceStation) world.provider;
            worldOrbital.getSpinManager().refresh(pos, true);
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityThruster();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            final int facing = getMetaFromState(state) & 8;
            if (worldIn.provider instanceof WorldProviderSpaceStation)
            {
                WorldProviderSpaceStation worldOrbital = (WorldProviderSpaceStation) worldIn.provider;
                worldOrbital.getSpinManager().removeThruster(pos, facing == 0);
                worldOrbital.getSpinManager().updateSpinSpeed();
            }
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return LabStuffMain.tabLabStuffSpace;
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
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}