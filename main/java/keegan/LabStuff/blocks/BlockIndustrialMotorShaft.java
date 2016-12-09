package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityIndustrialMotorShaft;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockIndustrialMotorShaft extends Block implements ITileEntityProvider
{

	public static final PropertyBool COMPLETE = PropertyBool.create("complete");
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{COMPLETE});
	}
	
	
	public BlockIndustrialMotorShaft(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		setDefaultState(getDefaultState().withProperty(COMPLETE, true));
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return !state.getValue(COMPLETE);
		
	}
	
	@Override
	public int getLightOpacity(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		return 5;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(COMPLETE) ? 1 : 0);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COMPLETE, meta > 0);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return !state.getValue(COMPLETE);
	}


	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new TileEntityIndustrialMotorShaft();
	}

}
