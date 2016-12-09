package keegan.labstuff.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockSolenoid extends Block {

	public BlockSolenoid(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
	}

	public static final PropertyBool COMPLETE = PropertyBool.create("complete");
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{COMPLETE});
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
		
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
	
	

}
