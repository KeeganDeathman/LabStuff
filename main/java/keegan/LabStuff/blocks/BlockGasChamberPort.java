package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityGasChamberPort;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGasChamberPort extends Block implements ITileEntityProvider
{

	
	
	public BlockGasChamberPort(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(!world.isRemote)
    	{
    		player.openGui(LabStuffMain.instance, 5, world, pos.getX(), pos.getY(), pos.getZ());
    		return true;
    	}
    	return false;
    }

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		//return new TileEntityGasChamberPort();
		return new TileEntityGasChamberPort();
	}

}
