package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityRedstonePipe;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;

public class BlockRedstonePipe extends Block implements ITileEntityProvider
{

	public BlockRedstonePipe(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing dir)
	{
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityRedstonePipe();
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(pos);
    		if(tile instanceof TileEntityRedstonePipe)
    		{
    			player.addChatMessage(new TextComponentString("Network is holding " + ((TileEntityRedstonePipe)tile).getTankProperties()[0].getContents().amount + " / " + ((TileEntityRedstonePipe)tile).getTankProperties()[0].getCapacity() + "mb of " + ((TileEntityRedstonePipe)tile).getTankProperties()[0].getContents().getLocalizedName()));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }

}
