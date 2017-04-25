package keegan.labstuff.blocks;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.IShiftDescription;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockMachineMars extends BlockTileLS implements IShiftDescription, ISortableBlock, IPartialSealableBlock
{
    public static final int TERRAFORMER_METADATA = 0;
    public static final int CRYOGENIC_CHAMBER_METADATA = 4;
    public static final int LAUNCH_CONTROLLER_METADATA = 8;

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumMachineType.class);

    public enum EnumMachineType implements IStringSerializable
    {
        TERRAFORMER(0, "terraformer"),
        CRYOGENIC_CHAMBER(1, "cryogenic_chamber"),
        LAUNCH_CONTROLLER(2, "launch_controller");

        private final int meta;
        private final String name;

        private EnumMachineType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumMachineType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockMachineMars(String assetName)
    {
        super(Material.IRON);
        this.setSoundType(SoundType.METAL);
        this.setUnlocalizedName(assetName);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        final TileEntity var9 = worldIn.getTileEntity(pos);

        if (var9 instanceof IMultiBlock)
        {
            ((IMultiBlock) var9).onDestroy(var9);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = getMetaFromState(state);

        final int angle = MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        int change = EnumFacing.getHorizontal(angle).getOpposite().getHorizontalIndex();

        worldIn.setBlockState(pos, getStateFromMeta((metadata & 12) + change), 3);

        TileEntity var8 = worldIn.getTileEntity(pos);

        if (var8 instanceof IMultiBlock)
        {
            ((IMultiBlock) var8).onCreate(worldIn, pos);
        }

        if (metadata >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            for (int dX = -2; dX < 3; dX++)
            {
                for (int dZ = -2; dZ < 3; dZ++)
                {
                    BlockPos pos1 = pos.add(dX, 0, dZ);
                    final Block id = worldIn.getBlockState(pos1).getBlock();

                    if (id == LabStuffMain.landingPadFull)
                    {
                        IBlockState state1 = worldIn.getBlockState(pos1);
                        worldIn.notifyBlockUpdate(pos1, state1, state1, 3);
                    }
                }
            }
        }

        else if (var8 instanceof TileEntityLaunchController && placer instanceof EntityPlayer)
        {
            ((TileEntityLaunchController) var8).setOwnerName(((EntityPlayer) placer).getGameProfile().getName());
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
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        EnumMachineType type = (EnumMachineType) state.getValue(TYPE);
        if (type == EnumMachineType.LAUNCH_CONTROLLER)
        {
            playerIn.openGui(LabStuffMain.instance, 31, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        else if (type == EnumMachineType.CRYOGENIC_CHAMBER)
        {
            ((IMultiBlock) worldIn.getTileEntity(pos)).onActivated(playerIn);
            return true;
        }
        else
        {
            playerIn.openGui(LabStuffMain.instance, 31, worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
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
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        EnumMachineType type = (EnumMachineType) state.getValue(TYPE);
        if (type == EnumMachineType.LAUNCH_CONTROLLER)
        {
            return new TileEntityLaunchController();
        }
        if (type == EnumMachineType.CRYOGENIC_CHAMBER)
        {
            return new TileEntityCryogenicChamber();
        }
        else
        {
            return new TileEntityTerraformer();
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (getMetaFromState(world.getBlockState(pos)) >= BlockMachineMars.LAUNCH_CONTROLLER_METADATA)
        {
            for (int dX = -2; dX < 3; dX++)
            {
                for (int dZ = -2; dZ < 3; dZ++)
                {
                    BlockPos pos1 = pos.add(dX, 0, dZ);
                    final Block id = world.getBlockState(pos1).getBlock();

                    if (id == LabStuffMain.landingPadFull)
                    {
                        IBlockState state1 = world.getBlockState(pos1);
                        world.notifyBlockUpdate(pos1, state1, state1, 3);
                    }
                }
            }
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    public ItemStack getTerraformer()
    {
        return new ItemStack(this, 1, BlockMachineMars.TERRAFORMER_METADATA);
    }

    public ItemStack getChamber()
    {
        return new ItemStack(this, 1, BlockMachineMars.CRYOGENIC_CHAMBER_METADATA);
    }

    public ItemStack getLaunchController()
    {
        return new ItemStack(this, 1, BlockMachineMars.LAUNCH_CONTROLLER_METADATA);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(this.getTerraformer());
        par3List.add(this.getChamber());
        par3List.add(this.getLaunchController());
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        EnumMachineType type = (EnumMachineType) state.getValue(TYPE);
        if (type == EnumMachineType.LAUNCH_CONTROLLER)
        {
            return BlockMachineMars.LAUNCH_CONTROLLER_METADATA;
        }
        else if (type == EnumMachineType.CRYOGENIC_CHAMBER)
        {
            return BlockMachineMars.CRYOGENIC_CHAMBER_METADATA;
        }
        else
        {
            return BlockMachineMars.TERRAFORMER_METADATA;
        }
    }

    @Override
    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, Entity player)
    {
        return world.getBlockState(pos).getValue(TYPE) == EnumMachineType.CRYOGENIC_CHAMBER;
    }

    @Override
    public BlockPos getBedSpawnPosition(IBlockState state, IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        return pos.up();
    }

    @Override
    public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityCryogenicChamber)
        {
            ((TileEntityCryogenicChamber) tile).isOccupied = true;
        }
    }

    @Override
    public EnumFacing getBedDirection(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return EnumFacing.DOWN;
    }

    @Override
    public String getShiftDescription(int meta)
    {
        switch (meta)
        {
        case CRYOGENIC_CHAMBER_METADATA:
            return LabStuffUtils.translate("tile.cryo_chamber.description");
        case LAUNCH_CONTROLLER_METADATA:
            return LabStuffUtils.translate("tile.launch_controller.description");
        case TERRAFORMER_METADATA:
            return LabStuffUtils.translate("tile.terraformer.description");
        }
        return "";
    }

    @Override
    public boolean showDescription(int meta)
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta % 4);
        EnumMachineType type = EnumMachineType.byMetadata((int) Math.floor(meta / 4.0));
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(FACING)).getHorizontalIndex() + ((EnumMachineType) state.getValue(TYPE)).getMeta() * 4;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, TYPE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World worldIn, BlockPos pos, Random rand)
    {
        if (state.getValue(TYPE) == EnumMachineType.CRYOGENIC_CHAMBER)
        {
            LabStuffMain.spawnParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY(), pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, 0.05 + rand.nextDouble() * 0.01, 0.0));
            LabStuffMain.spawnParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY(), pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, 0.05 + rand.nextDouble() * 0.01, 0.0));
            LabStuffMain.spawnParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY(), pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, 0.05 + rand.nextDouble() * 0.01, 0.0));

            LabStuffMain.spawnParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY() + 2.9F, pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, -0.05 - rand.nextDouble() * 0.01, 0.0));
            LabStuffMain.spawnParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY() + 2.9F, pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, -0.05 - rand.nextDouble() * 0.01, 0.0));
            LabStuffMain.spawnParticle("cryoFreeze", new Vector3(pos.getX() + 0.3 + rand.nextDouble() * 0.4, pos.getY() + 2.9F, pos.getZ() + 0.3 + rand.nextDouble() * 0.4), new Vector3(0.0, -0.05 - rand.nextDouble() * 0.01, 0.0));
        }
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.MACHINE;
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, EnumFacing direction)
    {
	    return world.getBlockState(pos).getValue(TYPE) != EnumMachineType.CRYOGENIC_CHAMBER;
    }
}