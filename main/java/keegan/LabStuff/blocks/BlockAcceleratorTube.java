package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityAcceleratorTube;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

public class BlockAcceleratorTube extends Block implements ITileEntityProvider
{

	public BlockAcceleratorTube()
	{
		super(Material.IRON);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityAcceleratorTube();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
    {
            return false;
    }
    
	@Override
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side)
	{
    	return false;
	}
    
    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess access, BlockPos pos) 
    {
    	return false;
    }

}
