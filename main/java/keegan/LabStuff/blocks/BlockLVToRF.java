package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityLVToRF;
import keegan.labstuff.tileentity.TileEntityPowerCable;
import keegan.labstuff.tileentity.TileEntityRFToLV;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class BlockLVToRF extends Block implements ITileEntityProvider 
{

	public BlockLVToRF(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(x, y, z);
    		if(tile instanceof TileEntityLVToRF)
    		{
    			player.addChatMessage(new ChatComponentText("Internal Buffer is holding " + ((TileEntityLVToRF)tile).storage.getEnergyStored()));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		if(Loader.isModLoaded("ThermalFoundation"))
			return new TileEntityLVToRF();
		return null;
	}

}
