package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSolarGag extends Block {

	public BlockSolarGag(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public boolean isOpaqueCube() 
    {
            return false;
    }
    
    public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
    	return false;
	}
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata)
    {
    	super.onBlockDestroyedByPlayer(world, x, y, z, metadata);
    	int position = this.getMultiBlockPosition(world, x, y, z);
    	switch(position)
    	{
    		case 0:
    			world.setBlockToAir(x-1, y, z);
    			world.setBlockToAir(x-2, y, z);
    			world.setBlockToAir(x, y, z-1);
    			world.setBlockToAir(x, y, z-2);
    			world.setBlockToAir(x-1, y, z-1);
    			world.setBlockToAir(x-2, y, z-1);
    			world.setBlockToAir(x-1, y, z-2);
    			world.setBlockToAir(x-2, y, z-2);
    		case 1:
    			world.setBlockToAir(x-1, y, z);
    			world.setBlockToAir(x-2, y, z);
    			world.setBlockToAir(x, y, z-1);
    			world.setBlockToAir(x, y, z+1);
    			world.setBlockToAir(x-1, y, z-1);
    			world.setBlockToAir(x-2, y, z-1);
    			world.setBlockToAir(x-1, y, z+1);
    			world.setBlockToAir(x-2, y, z+1);
    		case 2:
    			world.setBlockToAir(x-1, y, z);
    			world.setBlockToAir(x-2, y, z);
    			world.setBlockToAir(x, y, z-1);
    			world.setBlockToAir(x, y, z-2);
    			world.setBlockToAir(x-1, y, z+1);
    			world.setBlockToAir(x-2, y, z+1);
    			world.setBlockToAir(x-1, y, z+2);
    			world.setBlockToAir(x-2, y, z+2);
    		case 3:
    			world.setBlockToAir(x-1, y, z);
    			world.setBlockToAir(x+1, y, z);
    			world.setBlockToAir(x, y, z-1);
    			world.setBlockToAir(x, y, z-2);
    			world.setBlockToAir(x-1, y, z-1);
    			world.setBlockToAir(x+1, y, z-1);
    			world.setBlockToAir(x-1, y, z-2);
    			world.setBlockToAir(x+1, y, z-2);
    		case 5:
    			world.setBlockToAir(x-1, y, z);
    			world.setBlockToAir(x+1, y, z);
    			world.setBlockToAir(x, y, z-1);
    			world.setBlockToAir(x, y, z-2);
    			world.setBlockToAir(x-1, y, z+1);
    			world.setBlockToAir(x+1, y, z+1);
    			world.setBlockToAir(x-1, y, z+2);
    			world.setBlockToAir(x+1, y, z+2);
    		case 6:
    			world.setBlockToAir(x+1, y, z);
    			world.setBlockToAir(x+2, y, z);
    			world.setBlockToAir(x, y, z-1);
    			world.setBlockToAir(x, y, z-2);
    			world.setBlockToAir(x+1, y, z-1);
    			world.setBlockToAir(x+2, y, z-1);
    			world.setBlockToAir(x+1, y, z-2);
    			world.setBlockToAir(x+2, y, z-2);
    		case 7:
    			world.setBlockToAir(x+1, y, z);
    			world.setBlockToAir(x-1, y, z);
    			world.setBlockToAir(x, y, z-1);
    			world.setBlockToAir(x, y, z+1);
    			world.setBlockToAir(x+1, y, z-1);
    			world.setBlockToAir(x-1, y, z-1);
    			world.setBlockToAir(x+1, y, z+1);
    			world.setBlockToAir(x-1, y, z+1);
    		case 8:
    			world.setBlockToAir(x+1, y, z);
    			world.setBlockToAir(x+2, y, z);
    			world.setBlockToAir(x, y, z+1);
    			world.setBlockToAir(x, y, z+2);
    			world.setBlockToAir(x+1, y, z+1);
    			world.setBlockToAir(x+2, y, z+1);
    			world.setBlockToAir(x+1, y, z+2);
    			world.setBlockToAir(x+2, y, z+2);
    	}
    }
    
    private int getMultiBlockPosition(World world, int x, int y, int z)
    {
    	//In the following diagram, towards the top of the file is positive x, and towards the left is positive z. Y is always the same.
    	//0 1 2
    	//3 X 5
    	//6 7 8
    	//Will never be X as X is the tile entity, not a gag
    	
    	if(world.getBlock(x+1, y, z) != null && world.getBlock(x+1,y,z) == LabStuffMain.blockSolarGag)
    	{
    		//Is between 3-8
    		if(world.getBlock(x-1, y, z) != null && world.getBlock(x-1,y,z) == LabStuffMain.blockSolarGag)
    		{
    			//Is between 3-5
    			if(world.getBlock(x, y, z+1) != null && world.getBlock(x,y,z+1) == LabStuffMain.blockSolarGag)
    			{
    				//Is between 4-5
    				if(world.getBlock(x,y,z-1) != null && world.getBlock(x,y,z-1) == LabStuffMain.blockSolarGag)
    				{
    					//To throw off the swutch method, we put in an undefined number
    					return 10000000;
    				}
    				return 5;
    			}
    			return 3;
    		}
    		else
    		{
    			//Is between 6-8
    			if(world.getBlock(x, y, z+1) != null && world.getBlock(x,y,z+1) == LabStuffMain.blockSolarGag)
    			{
    				//Is between 7-8
    				if(world.getBlock(x,y,z-1) != null && world.getBlock(x,y,z-1) == LabStuffMain.blockSolarGag)
    				{
    					return 7;
    				}
    				return 8;
    			}
    			return 6;
    		}
    	}
    	else
    	{
    		//Is between 0-2
    		if(world.getBlock(x, y, z+1) != null && world.getBlock(x,y,z+1) == LabStuffMain.blockSolarGag)
			{
				//Is between 1-2
				if(world.getBlock(x,y,z-1) != null && world.getBlock(x,y,z-1) == LabStuffMain.blockSolarGag)
				{
					return 1;
				}
				return 2;
			}
			return 0;
    	}
    }

}
