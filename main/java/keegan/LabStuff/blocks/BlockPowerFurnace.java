package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityPowerFurnace;
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

public class BlockPowerFurnace extends Block implements ITileEntityProvider
{
	
	public BlockPowerFurnace(Material mat)
	{
		super(mat);
		this.setDefaultState(getDefaultState().withProperty(POWERED, false));
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(!world.isRemote)
    	{
    		player.openGui(LabStuffMain.instance, 7, world, pos.getX(), pos.getY(), pos.getZ());
    		return true;
    	}
    	return false;
    }
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
	

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(FACING).getIndex() + 1)* ((state.getValue(POWERED) ? 2 : 1));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		if(meta < 6)
			return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta-1)).withProperty(POWERED, false);
		else
			return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta/2-1)).withProperty(POWERED, true);
	}
    
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{FACING, POWERED});
	}
	
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack)
    {
    	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	world.setBlockState(pos, getDefaultState().withProperty(FACING, EnumFacing.fromAngle(90*l)).withProperty(POWERED, false));    
    }
	
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) 
	{
		// TODO Auto-generated method stub
		return new TileEntityPowerFurnace();
	}
	
	public boolean isOn(IBlockAccess access, BlockPos pos) 
	{
		return access.getBlockState(pos).getValue(POWERED);
	}

	public void setOn(World world, BlockPos pos, boolean on) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, on)); 
	}
	
	
}
