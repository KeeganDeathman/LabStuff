package keegan.labstuff.blocks;


import keegan.labstuff.tileentity.TileEntitySolenoidAxel;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockSolenoidAxel extends Block implements ITileEntityProvider
{

	public BlockSolenoidAxel(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		this.setDefaultState(getDefaultState().withProperty(RENDER, EnumRender.BLOCK));
		// TODO Auto-generated constructor stub
	}
	
	public static final PropertyEnum<EnumRender> RENDER = PropertyEnum.create("render", EnumRender.class);

	public static enum EnumRender implements IStringSerializable {
	BLOCK("block"), AIR("air"), SOLENOID("solenoid");

	private final String name;

	private EnumRender(String name) {
		this.name = name;
	}

	public String toString() {
		return this.name;
	}

	public static EnumRender fromString(String string) {
		switch (string) {
		case "block":
			return BLOCK;
		case "air":
			return AIR;
		case "solenoid":
			return SOLENOID;
		default:
			return BLOCK;
		}
	}

	public String getName() {
		return this.name;
	}
}


	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{RENDER});
	}

	
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		switch(state.getValue(RENDER))
		{
			default:
				return 0;
			case BLOCK:
				return 0;
			case AIR:
				return 1;
			case SOLENOID:
				return 2;
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		switch(meta)
		{
			default:
				return getDefaultState().withProperty(RENDER, EnumRender.BLOCK);
			case 0:
				return getDefaultState().withProperty(RENDER, EnumRender.BLOCK);
			case 1:
				return getDefaultState().withProperty(RENDER, EnumRender.AIR);
			case 2:
				return getDefaultState().withProperty(RENDER, EnumRender.SOLENOID);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return true;
		
	}
	

	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return !(state.getValue(RENDER).equals(EnumRender.AIR) || state.getValue(RENDER).equals(EnumRender.SOLENOID));
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntitySolenoidAxel();
	}
	

}
