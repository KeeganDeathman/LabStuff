package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityPlasmaTank;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class BlockPlasmaTank extends Block implements ITileEntityProvider
{
	

	public BlockPlasmaTank(Material arg0) 
	{
		super(arg0);
	}
	
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(x,y,z);
    		if(world.getTileEntity(x, y, z) == tile)
    		{
    			player.addChatMessage(new ChatComponentText("Tank is holding " + ((TileEntityPlasmaTank) tile).getPlasma()));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return  new TileEntityPlasmaTank();
	}

}
