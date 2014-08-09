package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityPlasmaPipe;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPlasmaPipe extends Block implements ITileEntityProvider {

	public BlockPlasmaPipe(Material p_i45394_1_) {
		super(p_i45394_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return new TileEntityPlasmaPipe();
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
