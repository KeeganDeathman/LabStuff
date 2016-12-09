package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityToroid;
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

public class BlockFusionToroidalMagnet extends Block implements ITileEntityProvider
{
	
	public static PropertyInteger ANGLE = PropertyInteger.create("angle", 0, 7);

	public BlockFusionToroidalMagnet(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		this.setDefaultState(getDefaultState().withProperty(ANGLE, 0));
		// TODO Auto-generated constructor stub
	}
	


	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{ANGLE});
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(ANGLE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(ANGLE, meta);
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return true;
		
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) 
	{
    	if(!world.isRemote)
    	{
    		if(held != null)
    		{
    			if(world.getBlockState(pos).getValue(ANGLE) == 7 && held.isItemEqual(new ItemStack(LabStuffMain.itemWrench)))
        			world.setBlockState(pos, getDefaultState());
        		else if (held.isItemEqual(new ItemStack(LabStuffMain.itemWrench)))
        			world.setBlockState(pos, getDefaultState().withProperty(ANGLE, world.getBlockState(pos).getValue(ANGLE)+1), 2);
        		return true;

    		}
    	}
    	return false;
    }
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return false;
	}



	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new TileEntityToroid();
	}
	
	

}
