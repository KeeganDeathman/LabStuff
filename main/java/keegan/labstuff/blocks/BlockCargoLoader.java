package keegan.labstuff.blocks;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.IShiftDescription;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class BlockCargoLoader extends BlockAdvancedTile implements IShiftDescription, ISortableBlock
{
    private enum EnumLoaderType implements IStringSerializable
    {
        CARGO_LOADER(METADATA_CARGO_LOADER, "cargo_loader"),
        CARGO_UNLOADER(METADATA_CARGO_UNLOADER, "cargo_unloader");

        private final int meta;
        private final String name;

        EnumLoaderType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumLoaderType.class);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public static final int METADATA_CARGO_LOADER = 0;
    public static final int METADATA_CARGO_UNLOADER = 4;

    public BlockCargoLoader(String assetName)
    {
        super(Material.ROCK);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.setRegistryName("labstuff",assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        par3List.add(new ItemStack(par1, 1, BlockCargoLoader.METADATA_CARGO_LOADER));
        par3List.add(new ItemStack(par1, 1, BlockCargoLoader.METADATA_CARGO_UNLOADER));
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockAdded(worldIn, pos, state);

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity != null)
        {
            if (tileEntity instanceof TileEntityCargoLoader)
            {
                ((TileEntityCargoLoader) tileEntity).checkForCargoEntity();
            }
            else if (tileEntity instanceof TileEntityCargoUnloader)
            {
                ((TileEntityCargoUnloader) tileEntity).checkForCargoEntity();
            }
        }
    }

    @Override
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(LabStuffMain.instance, -1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        if (getMetaFromState(state) < BlockCargoLoader.METADATA_CARGO_UNLOADER)
        {
            return new TileEntityCargoLoader();
        }
        else
        {
            return new TileEntityCargoUnloader();
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int metadata = getMetaFromState(world.getBlockState(pos));
        int change = world.getBlockState(pos).getValue(FACING).rotateY().getHorizontalIndex();

        world.setBlockState(pos, this.getStateFromMeta(metadata - (metadata % 4) + change), 3);

        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

        if (stack.getItemDamage() >= METADATA_CARGO_UNLOADER)
        {
            change += METADATA_CARGO_UNLOADER;
        }
        else if (stack.getItemDamage() >= METADATA_CARGO_LOADER)
        {
            change += METADATA_CARGO_LOADER;
        }

        worldIn.setBlockState(pos, getStateFromMeta(change), 3);

        for (int dX = -2; dX < 3; dX++)
        {
            for (int dZ = -2; dZ < 3; dZ++)
            {
                final Block block = worldIn.getBlockState(pos.add(dX, 0, dZ)).getBlock();

                if (block == LabStuffMain.landingPadFull)
                {
                    BlockPos updatePos = pos.add(dX, 0, dZ);
                    IBlockState updateState = worldIn.getBlockState(updatePos);
                    worldIn.notifyBlockUpdate(updatePos, updateState, updateState, 3);
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        super.onBlockDestroyedByPlayer(worldIn, pos, state);

        for (int dX = -2; dX < 3; dX++)
        {
            for (int dZ = -2; dZ < 3; dZ++)
            {
                BlockPos newPos = new BlockPos(pos.getX() + dX, pos.getY(), pos.getZ() + dZ);
                final Block block = worldIn.getBlockState(newPos).getBlock();

                if (block == LabStuffMain.landingPadFull)
                {
                    IBlockState newState = worldIn.getBlockState(newPos);
                    worldIn.notifyBlockUpdate(newPos, newState, newState, 3);
                }
            }
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return ((EnumLoaderType) state.getValue(TYPE)).getMeta();
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case METADATA_CARGO_LOADER:
            return LabStuffUtils.translate("tile.cargo_loader.description");
        case METADATA_CARGO_UNLOADER:
            return LabStuffUtils.translate("tile.cargo_unloader.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumLoaderType type = meta >= METADATA_CARGO_UNLOADER ? EnumLoaderType.CARGO_UNLOADER : EnumLoaderType.CARGO_LOADER;

        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() + ((EnumLoaderType) state.getValue(TYPE)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }
}