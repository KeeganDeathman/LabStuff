package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityDLLaptop;
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

public class BlockDLLaptop extends Block implements ITileEntityProvider
{
	

	public BlockDLLaptop(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
		this.setDefaultState(getDefaultState().withProperty(TABLET, false));
	}


	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getIndex() + (state.getValue(TABLET)? 10 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		switch(meta / 10 %10)
		{
			case 0:
				return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta)).withProperty(TABLET, false);
			case 1:
				return getDefaultState().withProperty(TABLET, true).withProperty(FACING, EnumFacing.getFront(meta % 10));
			default:
				return getStateFromMeta(2);
		}
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		// TODO Auto-generated method stub
		return new TileEntityDLLaptop();
	}
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	public static final PropertyBool TABLET = PropertyBool.create("tablet");
    
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[]{FACING, TABLET});
	}
	
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack)
    {
    	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	world.setBlockState(pos, getDefaultState().withProperty(FACING, EnumFacing.fromAngle(90*l)));    
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
	 @Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) 
	 {
		return false;
	}
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if (!world.isRemote)
		{
			// System.out.println("Server");
			if (!player.isSneaking())
			{
				// System.out.println("All systems are go!");
				player.openGui(LabStuffMain.instance, 15, world, pos.getX(), pos.getY(), pos.getZ());
				// System.out.println("Gui opened");
				return true;
			}
		}
    	return false;
    }

}
