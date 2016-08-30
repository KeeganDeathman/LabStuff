package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.DSCRam;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockDSCRam extends Block implements ITileEntityProvider
{

	public BlockDSCRam(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new DSCRam();
	}

}
