package keegan.labstuff.blocks;

import java.util.*;

import com.google.common.base.Predicate;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.util.EnumSortCategoryBlock;
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
    public static final PropertyEnum BASIC_TYPE_LUNA = PropertyEnum.create("basictypemoon", EnumBlockLuna.class);

    public enum EnumBlockLuna implements IStringSerializable
    {
        MOON_DIRT(0, "moon_dirt_moon"),
        MOON_STONE(1, "moon_stone"),
        MOON_TURF(2, "moon_turf");

        private final int meta;
        private final String name;

        EnumBlockLuna(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public static EnumBlockLuna byMetadata(int meta)
        {
            for (EnumBlockLuna value : values())
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
        this.setDefaultState(this.blockState.getBaseState().withProperty(BASIC_TYPE_LUNA, EnumBlockLuna.MOON_STONE));
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
        EnumBlockLuna type = ((EnumBlockLuna) world.getBlockState(pos).getValue(BASIC_TYPE_LUNA));

        if (type == EnumBlockLuna.MOON_STONE)
        {
            return 6.0F;
        }
        return this.blockResistance / 5.0F;
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        EnumBlockLuna type = ((EnumBlockLuna) worldIn.getBlockState(pos).getValue(BASIC_TYPE_LUNA));

        if (type == EnumBlockLuna.MOON_DIRT || type == EnumBlockLuna.MOON_TURF)
        {
            return 0.5F;
        }

        return this.blockHardness;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player)
    {
        EnumBlockLuna type = ((EnumBlockLuna) world.getBlockState(pos).getValue(BASIC_TYPE_LUNA));
        if (type == EnumBlockLuna.MOON_DIRT || type == EnumBlockLuna.MOON_TURF)
        {
            return true;
        }

        return super.canHarvestBlock(world, pos, player);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        EnumBlockLuna type = ((EnumBlockLuna) state.getValue(BASIC_TYPE_LUNA));
        switch (type)
        {
        default:
            return Item.getItemFromBlock(this);
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        EnumBlockLuna type = ((EnumBlockLuna) state.getValue(BASIC_TYPE_LUNA));
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
        for (EnumBlockLuna type : EnumBlockLuna.values())
        {
            par3List.add(new ItemStack(par1, 1, type.getMeta()));
        }
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        EnumBlockLuna type = ((EnumBlockLuna) world.getBlockState(pos).getValue(BASIC_TYPE_LUNA));

        if (type != EnumBlockLuna.MOON_TURF)
        {
            return false;
        }

        plantable.getPlant(world, pos.offset(EnumFacing.UP));

        return plantable instanceof BlockFlower;
    }

    @Override
    public boolean isTerraformable(World world, BlockPos pos)
    {
        EnumBlockLuna type = ((EnumBlockLuna) world.getBlockState(pos).getValue(BASIC_TYPE_LUNA));

        if (type == EnumBlockLuna.MOON_TURF)
        {
            return world.getBlockState(pos.offset(EnumFacing.UP)).getBlock().isAir(world.getBlockState(pos), world, pos);
        }

        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public boolean isReplaceableOreGen(IBlockState state, IBlockAccess world, BlockPos pos, Predicate<IBlockState> target)
    {
        EnumBlockLuna type = ((EnumBlockLuna) world.getBlockState(pos).getValue(BASIC_TYPE_LUNA));
        return type == EnumBlockLuna.MOON_STONE || type == EnumBlockLuna.MOON_DIRT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BASIC_TYPE_LUNA, EnumBlockLuna.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumBlockLuna) state.getValue(BASIC_TYPE_LUNA)).getMeta();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BASIC_TYPE_LUNA);
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }
}