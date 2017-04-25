package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.IBM650Punch;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockIBM650Punch extends Block implements ITileEntityProvider
{

	public static PropertyInteger RENDER = PropertyInteger.create("render", 0, 4);
	
	public BlockIBM650Punch(Material materialIn) {
		super(materialIn);
		this.setDefaultState(getDefaultState().withProperty(RENDER, 0));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {

		if(!world.isRemote)
		{
			if (!player.isSneaking()) {
				player.openGui(LabStuffMain.instance, 26, world, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return false;
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
		return new IBM650Punch();
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing facing)
	{
		return access.getBlockState(pos).getValue(RENDER) == 0;
	}

}
