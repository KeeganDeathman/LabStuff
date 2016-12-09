package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityLVToRF;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockLVToRF extends Block implements ITileEntityProvider 
{

	public BlockLVToRF(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(pos);
    		if(tile instanceof TileEntityLVToRF)
    		{
    			player.addChatMessage(new TextComponentString("Internal Buffer is holding " + ((TileEntityLVToRF)tile).storage.getEnergyStored()));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
//		if(Loader.isModLoaded("ThermalFoundation"))
//			return new TileEntityLVToRF();
		return new TileEntityLVToRF();
	}

}
