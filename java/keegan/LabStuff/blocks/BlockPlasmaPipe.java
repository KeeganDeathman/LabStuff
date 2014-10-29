package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityPlasmaPipe;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPlasmaPipe extends Block implements ITileEntityProvider {

	public BlockPlasmaPipe(Material p_i45394_1_) {
		super(p_i45394_1_);
	}
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		// TODO Auto-generated method stub
		return new TileEntityPlasmaPipe();
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(x, y, z);
    		if(tile instanceof TileEntityPlasmaPipe)
    		{
    			player.addChatMessage(new ChatComponentText("Network is holding " + ((TileEntityPlasmaPipe)tile).getPlasma()));
    			return true;
    		}
    		return false;
    	}
    	return false;
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
    	super.onBlockPlacedBy(world, x, y, z, player, stack);
    	((TileEntityPlasmaPipe)world.getTileEntity(z, y, z)).equalize();
    }

}
