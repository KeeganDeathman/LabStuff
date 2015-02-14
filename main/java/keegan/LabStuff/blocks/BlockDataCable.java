package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityDataCable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDataCable extends Block implements ITileEntityProvider
{

	public BlockDataCable(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityDataCable();
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

}
