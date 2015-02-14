package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntitySolarPanel;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSolarPanel extends Block implements ITileEntityProvider
{

	public BlockSolarPanel(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntitySolarPanel();
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
    public void onBlockAdded(World world, int x, int y, int z)
    {
    	world.setBlock(x+1, y, z+1, LabStuffMain.blockSolarGag);
    	world.setBlock(x+1, y, z, LabStuffMain.blockSolarGag);
    	world.setBlock(x+1, y, z-1, LabStuffMain.blockSolarGag);
    	world.setBlock(x, y, z+1, LabStuffMain.blockSolarGag);
    	world.setBlock(x, y, z-1, LabStuffMain.blockSolarGag);
    	world.setBlock(x-1, y, z+1, LabStuffMain.blockSolarGag);
    	world.setBlock(x-1, y, z, LabStuffMain.blockSolarGag);
    	world.setBlock(x-1, y, z-1, LabStuffMain.blockSolarGag);

    }
    
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata)
    {
    	super.onBlockDestroyedByPlayer(world, x, y, z, metadata);
    	world.setBlockToAir(x+1, y+1, z+1);
    	world.setBlockToAir(x+1, y+1, z);
    	world.setBlockToAir(x+1, y+1, z-1);
    	world.setBlockToAir(x, y+1, z+1);
    	world.setBlockToAir(x, y+1, z);
    	world.setBlockToAir(x, y+1, z-1);
    	world.setBlockToAir(x-1, y+1, z+1);
    	world.setBlockToAir(x-1, y+1, z);
    	world.setBlockToAir(x-1, y+1, z-1);
    }
	
}
