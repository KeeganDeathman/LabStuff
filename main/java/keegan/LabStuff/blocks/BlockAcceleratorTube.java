package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityAcceleratorTube;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;

public class BlockAcceleratorTube extends Block implements ITileEntityProvider
{

	public BlockAcceleratorTube()
	{
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityAcceleratorTube();
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
    public boolean renderAsNormalBlock()
    {
    	return false;
    }

}
