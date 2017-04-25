package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntitySolarPanel;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockSolarPanel extends Block implements ITileEntityProvider
{

	public BlockSolarPanel(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
		this.setDefaultState(getDefaultState());
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntitySolarPanel();
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
	public boolean canPlaceBlockAt(IBlockAccess world, BlockPos pos)
	{
		return world.getBlockState(pos.add(1,0,1)).getBlock().equals(Blocks.AIR) && world.getBlockState(pos.add(1,0,0)).getBlock().equals(Blocks.AIR) && world.getBlockState(pos.add(1,0,-1)).getBlock().equals(Blocks.AIR)
			&& world.getBlockState(pos.add(0,0,1)).getBlock().equals(Blocks.AIR) && world.getBlockState(pos.add(0,0,0)).getBlock().equals(Blocks.AIR) && world.getBlockState(pos.add(0,0,-1)).getBlock().equals(Blocks.AIR)
			&& world.getBlockState(pos.add(-1,0,1)).getBlock().equals(Blocks.AIR) && world.getBlockState(pos.add(-1,0,0)).getBlock().equals(Blocks.AIR) && world.getBlockState(pos.add(-1,0,-1)).getBlock().equals(Blocks.AIR);
	}
	
    public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
    	return false;
	}
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack)
    {
    	world.setBlockState(pos.north().east(), LabStuffMain.blockSolarGag.getDefaultState());
    	world.setBlockState(pos.north().west(), LabStuffMain.blockSolarGag.getDefaultState());
    	world.setBlockState(pos.south().east(), LabStuffMain.blockSolarGag.getDefaultState());
    	world.setBlockState(pos.south().west(), LabStuffMain.blockSolarGag.getDefaultState());
    	world.setBlockState(pos.north(), LabStuffMain.blockSolarGag.getDefaultState());
    	world.setBlockState(pos.east(), LabStuffMain.blockSolarGag.getDefaultState());
    	world.setBlockState(pos.south(), LabStuffMain.blockSolarGag.getDefaultState());
    	world.setBlockState(pos.west(), LabStuffMain.blockSolarGag.getDefaultState());

    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
    	super.onBlockDestroyedByPlayer(world, pos, state);
    	world.setBlockToAir(pos.add(1, 0, 1));
		world.setBlockToAir(pos.add(1, 0, 0));
		world.setBlockToAir(pos.subtract(new Vec3i(0,0,1)).add(1,0,0));
		world.setBlockToAir(pos.add(0, 0, 1));
		world.setBlockToAir(pos);
		world.setBlockToAir(pos.subtract(new Vec3i(0,0,1)));
		world.setBlockToAir(pos.subtract(new Vec3i(1,0,0)).add(0,0,1));
		world.setBlockToAir(pos.subtract(new Vec3i(1,0,0)));
		world.setBlockToAir(pos.subtract(new Vec3i(1,0,1)));
    }
	
}
