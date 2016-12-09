package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockSolarGag extends Block {

	public BlockSolarGag(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
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
	 
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
    
    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state)
    {
    	super.onBlockDestroyedByPlayer(world, pos, state);
    	BlockPos solar = getSolarPanel(world, pos);
    	
    	world.setBlockToAir(pos);
    	world.setBlockToAir(pos.add(1,0,1));
    	world.setBlockToAir(pos.add(1,0,0));
    	world.setBlockToAir(pos.add(1,0,-1));
    	world.setBlockToAir(pos.add(0,0,1));
    	world.setBlockToAir(pos.add(0,0,0));
    	world.setBlockToAir(pos.add(0,0,-1));
    	world.setBlockToAir(pos.add(-1,0,1));
    	world.setBlockToAir(pos.add(-1,0,0));
    	world.setBlockToAir(pos.add(-1,0,-1));
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	return LabStuffMain.get(LabStuffMain.blockSolarPanel);
    }
    
   public BlockPos getSolarPanel(World world, BlockPos pos)
   {
	   for(int i = -1; i < 2; i++)
	   {
		   for(int j = -1; j < 2; j++)
		   {
			   if(world.getBlockState(pos.add(i,0,j)).getBlock().equals(LabStuffMain.blockSolarPanel))
				   return pos.add(i,0,j);
		   }
	   }
	   return pos;
   }

}
