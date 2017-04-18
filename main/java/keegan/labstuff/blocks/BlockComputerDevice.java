package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockComputerDevice extends Block {

	public BlockComputerDevice() {
		super(Material.IRON);
		this.setCreativeTab(LabStuffMain.tabLabStuffComputers);
		this.setHardness(10F);
		this.setResistance(5F);
		// TODO Auto-generated constructor stub
	}
	
public static final PropertyBool VANISH = PropertyBool.create("render");
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{VANISH});
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
		
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(VANISH) ? 1 : 0);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(VANISH, meta > 0);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return !state.getValue(VANISH);
	}

}
