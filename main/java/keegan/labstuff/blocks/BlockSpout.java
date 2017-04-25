package keegan.labstuff.blocks;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntitySpout;
import keegan.labstuff.util.EnumSortCategoryBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class BlockSpout extends Block implements ISortableBlock, ITileEntityProvider
{
    public BlockSpout(String assetName)
    {
        super(Material.ROCK);
        this.blockHardness = 4.5F;
        this.blockResistance = 2.5F;
        this.setRegistryName("labstuff",assetName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTabToDisplayOn()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public EnumSortCategoryBlock getCategory(int meta)
    {
        return EnumSortCategoryBlock.GENERAL;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntitySpout();
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(LabStuffMain.venusBlock);
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return BlockBasicVenus.EnumBlockBasicVenus.ROCK_SOFT.getMeta();
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        return super.getDrops(world, pos, state, fortune);
    }
}