package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityWindTurbine;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.*;
import net.minecraft.block.state.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockWindTurbine extends Block implements ITileEntityProvider
{

	public BlockWindTurbine(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityWindTurbine();
	}

	
	//It's not an opaque cube, so you need this.
    @Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
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
    public void onBlockAdded(World world, BlockPos pos, IBlockState state)
    {
    	world.setBlockState(pos.up(), LabStuffMain.blockWindGag.getDefaultState());
    	world.setBlockState(pos.up(2), LabStuffMain.blockWindGag.getDefaultState());

    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
    	super.onBlockDestroyedByPlayer(world, pos, state);
    	world.setBlockToAir(pos.up());
    	world.setBlockToAir(pos.up(2));
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
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return false;
	}
	
}
