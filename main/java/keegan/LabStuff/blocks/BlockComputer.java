package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityComputer;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockComputer extends Block implements ITileEntityProvider
{

	
	public BlockComputer(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}
	
	public boolean providingPower = false;


	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) 
	{
		return new TileEntityComputer();
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess par1iBlockAccess, BlockPos pos, EnumFacing facing)
	{
		return true;
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return false;
	}
    
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
    
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{FACING});
	}
	
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack)
    {
    	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	world.setBlockState(pos, getDefaultState().withProperty(FACING, EnumFacing.fromAngle(90*l)));    
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(world.isRemote == true)
    	{
    		if(!player.isSneaking())
    		{
    			player.openGui(LabStuffMain.instance, 2, world, pos.getX(), pos.getY(), pos.getZ());
    		}
    	}
    	return false;
    }



}
