package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.IBM650PowerUnit;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockIBM650PowerUnit extends Block implements ITileEntityProvider
{

	public static PropertyInteger RENDER = PropertyInteger.create("render", 0, 4);
	
	public BlockIBM650PowerUnit(Material materialIn) {
		super(materialIn);
		this.setDefaultState(getDefaultState().withProperty(RENDER, 0));
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{RENDER});
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(RENDER) == 0;
		
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(RENDER));
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(RENDER, meta);
	}

	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new IBM650PowerUnit();
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing facing)
	{
		return access.getBlockState(pos).getValue(RENDER) == 0;
	}

}
