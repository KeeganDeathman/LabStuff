package keegan.labstuff.blocks;

import java.util.*;

import com.google.common.base.Predicate;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.TickHandlerServer;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.*;

public class BlockLuna extends Block implements ITerraformableBlock, ISortableBlock
{
    public static final PropertyEnum BASIC_TYPE_MOON = PropertyEnum.create("basictypemoon", EnumBlocLuna.class);

    public enum EnumBlocLuna implements IStringSerializable
    {
        MOON_DIRT(3, "moon_dirt_moon"),
        MOON_STONE(4, "moon_stone"),
        MOON_TURF(5, "moon_turf");

        private final int meta;
        private final String name;

        EnumBlocLuna(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumBlocLuna byMetadata(int meta)
        {
            for (EnumBlocLuna value : values())
            {
                if (value.getMeta() == meta)
                {
                    return value;
                }
            }

            return null;
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    public BlockLuna(String assetName)
    {
        super(Material.ROCK);
        this.blockHardness = 1.5F;
        this.blockResistance = 2.5F;
        this.setDefaultState(this.blockState.getBaseState().withProperty(BASIC_TYPE_MOON, EnumBlocLuna.MOON_STONE));
        this.setRegistryName("labstuff",assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion)
    {
        EnumBlocLuna type = ((EnumBlocLuna) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type == EnumBlocLuna.MOON_STONE)
        {
            return 6.0F;
        }
        return this.blockResistance / 5.0F;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        EnumBlocLuna type = ((EnumBlocLuna) worldIn.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type == EnumBlocLuna.MOON_DIRT || type == EnumBlocLuna.MOON_TURF)
        {
            return 0.5F;
        }

        return this.blockHardness;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        EnumBlocLuna type = ((EnumBlocLuna) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));
        if (type == EnumBlocLuna.MOON_DIRT || type == EnumBlocLuna.MOON_TURF)
        {
            return true;
        }

        return super.canHarvestBlock(world, pos, player);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        EnumBlocLuna type = ((EnumBlocLuna) state.getValue(BASIC_TYPE_MOON));
        switch (type)
        {
        default:
            return Item.getItemFromBlock(this);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        EnumBlocLuna type = ((EnumBlocLuna) state.getValue(BASIC_TYPE_MOON));
        return getMetaFromState(state);
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random)
    {
    	return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List)
    {
        for (EnumBlocLuna type : EnumBlocLuna.values())
        {
            par3List.add(new ItemStack(par1, 1, type.getMeta()));
        }
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        EnumBlocLuna type = ((EnumBlocLuna) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type != EnumBlocLuna.MOON_TURF)
        {
            return false;
        }

        plantable.getPlant(world, pos.offset(EnumFacing.UP));

        return plantable instanceof BlockFlower;
    }

    @Override
    public boolean isTerraformable(World world, BlockPos pos)
    {
        EnumBlocLuna type = ((EnumBlocLuna) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));

        if (type == EnumBlocLuna.MOON_TURF)
        {
            return world.getBlockState(pos.offset(EnumFacing.UP)).getBlock().isAir(world.getBlockState(pos), world, pos);
        }

        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
//        int metadata = getMetaFromState(world.getBlockState(pos));
//        if (metadata == 2)
//        {
//            return new ItemStack(Item.getItemFromBlock(this), 1, metadata);
//        }

        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target)
    {
        EnumBlocLuna type = ((EnumBlocLuna) world.getBlockState(pos).getValue(BASIC_TYPE_MOON));
        return type == EnumBlocLuna.MOON_STONE || type == EnumBlocLuna.MOON_DIRT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BASIC_TYPE_MOON, EnumBlocLuna.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumBlocLuna) state.getValue(BASIC_TYPE_MOON)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BASIC_TYPE_MOON);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}