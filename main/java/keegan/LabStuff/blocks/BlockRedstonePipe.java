package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockRedstonePipe extends Block implements ITileEntityProvider
{

	public BlockRedstonePipe(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(x, y, z);
    		if(tile instanceof TileEntityRedstonePipe)
    		{
    			player.addChatMessage(new ChatComponentText("Network is holding " + ((TileEntityRedstonePipe)tile).getTankInfo(ForgeDirection.UNKNOWN)[0].fluid.amount));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }

}
