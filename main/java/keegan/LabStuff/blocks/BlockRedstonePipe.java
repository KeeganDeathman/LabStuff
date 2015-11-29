package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityRedstonePipe;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;

public class BlockRedstonePipe extends Block implements ITileEntityProvider
{

	public BlockRedstonePipe(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityRedstonePipe();
	}

}
