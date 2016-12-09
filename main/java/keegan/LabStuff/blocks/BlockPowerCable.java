package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityPowerCable;
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

public class BlockPowerCable extends Block implements ITileEntityProvider
{
	public BlockPowerCable(Material mat)
	{
		super(mat);
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(pos);
    		if(tile instanceof TileEntityPowerCable)
    		{
    			player.addChatMessage(new TextComponentString("Network is holding " + ((TileEntityPowerCable)tile).getPower()));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityPowerCable();
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) 
    {
            return false;
    }
    
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return false;
	}
}
