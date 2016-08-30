package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockLiquidPipe extends Block implements ITileEntityProvider
{

	public BlockLiquidPipe(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityLiquidPipe();
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(x, y, z);
    		if(tile instanceof TileEntityLiquidPipe && ((TileEntityLiquidPipe)tile).tank.getFluid() != null)
    		{
    			player.addChatMessage(new ChatComponentText("Network is holding " + ((TileEntityLiquidPipe)tile).getTankInfo(ForgeDirection.UNKNOWN)[0].fluid.amount + "/" + ((TileEntityLiquidPipe)tile).getTankInfo(ForgeDirection.UNKNOWN)[0].capacity + "mb of " + ((TileEntityLiquidPipe)tile).getTankInfo(ForgeDirection.UNKNOWN)[0].fluid.getLocalizedName()));
    			return true;
    		}
    		player.addChatMessage(new ChatComponentText("Error! Maybe it's empty?"));
    		return false;
    	}
    	return false;
    }

}
