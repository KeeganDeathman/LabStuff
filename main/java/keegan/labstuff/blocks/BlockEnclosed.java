package keegan.labstuff.blocks;

import java.lang.reflect.*;
import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.IShiftDescription;
import keegan.labstuff.tileentity.TileEntityNull;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class BlockEnclosed extends Block implements IPartialSealableBlock, ITileEntityProvider, IShiftDescription, ISortableBlock
{
    public static Item[] pipeItemsBC = new Item[6];
    public static Block blockPipeBC = null;
    public static Method onBlockNeighbourChangeIC2 = null;

    public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumEnclosedBlockType.class);

    public enum EnumEnclosedBlockType implements IStringSerializable
    {
//        copper(1, 1, 0.25F, 0.2D, 128),
//        glass(0, 0, 0.25F, 0.025D, 8192),
//        gold(2, 1, 0.1875F, 0.4D, 512),
//        iron(3, 1, 0.375F, 0.8D, 2048),
//        tin(1, 1, 0.25F, 0.2D, 32),
//        detector(0, 2147483647, 0.5F, 0.5D, 8192),
//        splitter(0, 2147483647, 0.5F, 0.5D, 8192);
        IC2_HV_CABLE(0, "iron", 1, "enclosed_hv_cable"),
        IC2_COPPER_CABLE(1, "copper", 1, "enclosed_copper_cable"),
        IC2_GOLD_CABLE(2, "gold", 1, "enclosed_gold_cable"),
        TE_CONDUIT(3, "enclosed_te_conduit"), //CURRENTLY UNUSED
        IC2_GLASS_FIBRE_CABLE(4, "glass", 0, "enclosed_glass_fibre_cable"),
        IC2_LV_CABLE(5, "tin", 1, "enclosed_lv_cable"),
        BC_ITEM_STONEPIPE(6, "PipeItemsStone", "enclosed_pipe_items_stone"),
        BC_ITEM_COBBLESTONEPIPE(7, "PipeItemsCobblestone", "enclosed_pipe_items_cobblestone"),
        BC_FLUIDS_STONEPIPE(8, "PipeFluidsStone", "enclosed_pipe_fluids_stone"),
        BC_FLUIDS_COBBLESTONEPIPE(9, "PipeFluidsCobblestone", "enclosed_pipe_fluids_cobblestone"),
        BC_POWER_STONEPIPE(10, "PipePowerStone", "enclosed_pipe_power_stone"),
        BC_POWER_GOLDPIPE(11, "PipePowerGold", "enclosed_pipe_power_gold"),
        ME_CABLE(12, "enclosed_me_cable");

        private final int meta;
        private final String name;
        private final String ic2Enum;
        private final int ic2Insulation;
        private final String bcPipeType;

        EnumEnclosedBlockType(int meta, String bcPipeType, String name)
        {
            this(meta, null, -1, bcPipeType, name);
        }

        EnumEnclosedBlockType(int meta, String name)
        {
            this(meta, null, -1, null, name);
        }

        EnumEnclosedBlockType(int meta, String ic2Enum, int ic2Insulation, String name)
        {
            this(meta, ic2Enum, ic2Insulation, null, name);
        }

        EnumEnclosedBlockType(int meta, String ic2Enum, int ic2Insulation, String bcPipeType, String name)
        {
            this.meta = meta;
            this.ic2Enum = ic2Enum;
            this.ic2Insulation = ic2Insulation;
            this.bcPipeType = bcPipeType;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public String getIc2Enum()
        {
            return ic2Enum;
        }

        public int getIc2Insulation()
        {
            return ic2Insulation;
        }

        public String getBCPipeType()
        {
            return bcPipeType;
        }

        public static EnumEnclosedBlockType byMetadata(int meta)
        {
            return values()[meta];
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockEnclosed(String assetName)
    {
        super(Material.CLAY);
        this.setResistance(0.2F);
        this.setHardness(0.4f);
        this.setSoundType(SoundType.STONE);
        this.setRegistryName("labstuff",assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {

        if (CompatibilityManager.isIc2Loaded())
        {
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.IC2_COPPER_CABLE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.IC2_GOLD_CABLE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.IC2_HV_CABLE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.IC2_GLASS_FIBRE_CABLE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.IC2_LV_CABLE.getMeta()));
        }

        if (CompatibilityManager.isBCraftTransportLoaded())
        {
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.BC_ITEM_COBBLESTONEPIPE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.BC_ITEM_STONEPIPE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.BC_FLUIDS_COBBLESTONEPIPE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.BC_FLUIDS_STONEPIPE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.BC_POWER_STONEPIPE.getMeta()));
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.BC_POWER_GOLDPIPE.getMeta()));
        }

        if (CompatibilityManager.isAppEngLoaded())
        {
            par3List.add(new ItemStack(par1, 1, EnumEnclosedBlockType.ME_CABLE.getMeta()));
        }
    }

    public static void initialiseBC()
    {
        for (int i = 0; i < 6; i++)
        {
            try
            {
                Class<?> clazzBC = Class.forName("buildcraft.BuildCraftTransport");
                String pipeName = EnumEnclosedBlockType.values()[i + 6].getBCPipeType();
                pipeName = pipeName.substring(0, 1).toLowerCase() + pipeName.substring(1);
                pipeItemsBC[i] = (Item) clazzBC.getField(pipeName).get(null);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
        return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn)
    {
        int metadata = state.getBlock().getMetaFromState(state);
        final TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (metadata == EnumEnclosedBlockType.TE_CONDUIT.getMeta())
        {
            super.neighborChanged(state, worldIn, pos, blockIn);
        }
        else if (metadata <= 5)
        {
            super.neighborChanged(state, worldIn, pos, blockIn);
            if (CompatibilityManager.isIc2Loaded() && tileEntity != null)
            {
                try
                {
                    onBlockNeighbourChangeIC2.invoke(tileEntity, blockIn);
                    return;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= 11)
        {
            if (CompatibilityManager.isBCraftTransportLoaded())
            {
                if (blockPipeBC != null)
                {
                    try
                    {
                        blockPipeBC.neighborChanged(state, worldIn, pos, blockIn);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    return;
                }
            }

            super.neighborChanged(state, worldIn, pos, blockIn);
        }
        else if (metadata <= EnumEnclosedBlockType.ME_CABLE.getMeta())
        {
            super.neighborChanged(state, worldIn, pos, blockIn);
            if (CompatibilityManager.isAppEngLoaded())
            {
//                worldIn.notifyBlockUpdate(pos); TODO
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        if (metadata == EnumEnclosedBlockType.TE_CONDUIT.getMeta())
        {
            //TODO
        }
        else if (metadata <= 5)
        {
            if (CompatibilityManager.isIc2Loaded())
            {
                try
                {
                    Class<?> clazz = Class.forName("ic2.core.block.wiring.TileEntityCable");
                    Class<?> cableTypeClazz = Class.forName("ic2.core.block.wiring.CableType");
                    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
                    Constructor<?> constructor = null;

                    for (Constructor<?> constructor2 : constructors)
                    {
                        constructor = constructor2;

                        if (constructor.getGenericParameterTypes().length == 2)
                        {
                            break;
                        }
                    }

                    constructor.setAccessible(true);

                    Enum[] enums = (Enum[]) cableTypeClazz.getEnumConstants();
                    Enum foundEnum = null;
                    EnumEnclosedBlockType enclosedType = EnumEnclosedBlockType.byMetadata(metadata);

                    for (Enum e : enums)
                    {
                        if (e.name().equals(enclosedType.getIc2Enum()))
                        {
                            foundEnum = e;
                            break;
                        }
                    }

                    return (TileEntity) constructor.newInstance(foundEnum, enclosedType.getIc2Insulation());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= 11)
        {
            if (CompatibilityManager.isBCraftTransportLoaded())
            {
                try
                {
                    return blockPipeBC.createTileEntity(world, blockPipeBC.getDefaultState());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        else if (metadata <= EnumEnclosedBlockType.ME_CABLE.getMeta())
        {
            if (CompatibilityManager.isAppEngLoaded())
            {
//            	//Emulate Api.INSTANCE.partHelper().getCombinedInstance( TileCableBus.class.getName() )
//                try
//                {
//                    IPartHelper apiPart = AEApi.instance().partHelper();
//                    Class<?> clazzApiPart = Class.forName("appeng.core.api.ApiPart");
//                    Class clazz = (Class) clazzApiPart.getDeclaredMethod("getCombinedInstance", String.class).invoke(apiPart, "appeng.tile.networking.TileCableBus");
//                    //Needs to be: appeng.parts.layers.LayerITileStorageMonitorable_TileCableBus
//                    return (TileEntity) clazz.newInstance();
//                }
//                catch (Exception e) { e.printStackTrace(); }
            }
        }
        return new TileEntityNull();
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, EnumFacing direction)
    {
        return true;
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
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        int metadata = stack.getItemDamage();
        if (metadata >= EnumEnclosedBlockType.BC_ITEM_STONEPIPE.getMeta() && metadata <= EnumEnclosedBlockType.BC_POWER_GOLDPIPE.getMeta())
        {
            EnumEnclosedBlockType type = EnumEnclosedBlockType.byMetadata(metadata);
            if (CompatibilityManager.isBCraftTransportLoaded() && type != null && type.getBCPipeType() != null)
            {
                BlockEnclosed.initialiseBCPipe(worldIn, pos, metadata);
            }
        }       
    }

    public static void initialiseBCPipe(World world, BlockPos pos, int metadata)
    {
        try
        {
            //------
            //This section makes these three calls to initialise the TileEntity:
            //	Pipe pipe = BlockGenericPipe.createPipe(Item);
            //  tilePipe.initialize(pipe);
            //	and optionally: tilePipe.sendUpdateToClient();

            Item pipeItem = pipeItemsBC[metadata - 6];
            Class<?> clazzBlockPipe = CompatibilityManager.classBCBlockGenericPipe;
            TileEntity tilePipe = world.getTileEntity(pos);
            Class<?> clazzTilePipe = tilePipe.getClass();

            if (CompatibilityManager.methodBCBlockPipe_createPipe != null)
            {
                Object pipe = CompatibilityManager.methodBCBlockPipe_createPipe.invoke(null, pipeItem);
                Method initializePipe = null;
                for (Method m : clazzTilePipe.getMethods())
                {
                    if (m.getName().equals("initialize") && m.getParameterTypes().length == 1)
                    {
                        initializePipe = m;
                        break;
                    }
                }
                if (initializePipe != null)
                {
                    initializePipe.invoke(tilePipe, pipe);

                    //Legacy compatibility: TileGenericPipe.sendUpdateToClient() is not in recent BC versions
                    Method m = null;
                    try
                    {
                        m = clazzTilePipe.getMethod("sendUpdateToClient");
                    }
                    catch (Exception e)
                    {
                    }
                    if (m != null)
                    {
                        m.invoke(tilePipe);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumEnclosedBlockType type = EnumEnclosedBlockType.byMetadata(meta);
        return this.getDefaultState().withProperty(TYPE, type);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumEnclosedBlockType) state.getValue(TYPE)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.TRANSMITTER;
    }
}