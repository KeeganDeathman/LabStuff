package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityRFToLV;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockRFtoLV extends Block implements ITileEntityProvider 
{

	public BlockRFtoLV(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
//		if(Loader.isModLoaded("ThermalFoundation"))
//			return new TileEntityRFToLV();
		return new TileEntityRFToLV();
	}

}
