package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockWindGag extends Block {

	public BlockWindGag(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
    public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
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
    	int position = this.getMultiBlockPosition(world, pos);
    	switch(position)
    	{
    		case 1:
    			world.setBlockToAir(pos.down());
    			world.setBlockToAir(pos.up());
    		case 2:
    			world.setBlockToAir(pos.down());
    			world.setBlockToAir(pos.down(2));
    	}
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
    	return LabStuffMain.get(LabStuffMain.blockWindTurbine);
    }
    
    private int getMultiBlockPosition(World world, BlockPos pos)
    {
    	//the returned value is the height above the tile entity, so 1/2.
    	
    	if(world.getBlockState(pos.down()) != null && world.getBlockState(pos.down()).getBlock().equals(LabStuffMain.blockWindGag))
    	{
    		return 2;
    	}
    	return 1;
    }

}
