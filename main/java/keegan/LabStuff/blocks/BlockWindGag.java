package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWindGag extends Block {

	public BlockWindGag(Material p_i45394_1_) 
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
    		case 1:
    			world.setBlockToAir(x, y-1, z);
    			world.setBlockToAir(x, y+1, z);
    		case 2:
    			world.setBlockToAir(x, y-1, z);
    			world.setBlockToAir(x, y-2, z);
    	}
    }
    
    private int getMultiBlockPosition(World world, int x, int y, int z)
    {
    	//the returned value is the height above the tile entity, so 1/2.
    	
    	if(world.getBlock(x, y-1, z) != null && world.getBlock(x+1,y,z) == LabStuffMain.blockWindGag)
    	{
    		return 2;
    	}
    	return 1;
    }

}
