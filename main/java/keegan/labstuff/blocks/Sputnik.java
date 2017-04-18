package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TESputnik;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Sputnik extends BlockRocketPart implements ITileEntityProvider{

	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new TESputnik();
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		((TESputnik)world.getTileEntity(pos)).resetRocket();
	}

}
