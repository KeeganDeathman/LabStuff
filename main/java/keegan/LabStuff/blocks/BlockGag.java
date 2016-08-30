package keegan.labstuff.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.*;

public class BlockGag extends Block
{

	public BlockGag(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
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
