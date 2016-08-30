package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityFusionToroidalMagnet;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;

public class BlockFusionToroidalMagnet extends Block implements ITileEntityProvider{

	public BlockFusionToroidalMagnet(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityFusionToroidalMagnet();
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		if(player.getHeldItem() != null)
    		{
    			if(world.getBlockMetadata(x, y, z) == 7 && player.getHeldItem().isItemEqual(new ItemStack(LabStuffMain.itemWrench)))
        			world.setBlockMetadataWithNotify(x, y, z, 0, 2);
        		else if (player.getHeldItem().isItemEqual(new ItemStack(LabStuffMain.itemWrench)))
        			world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+1, 2);
        		return true;

    		}
    	}
    	return false;
    }
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess ba, int x, int y, int z, int side)
	{
		return false;
	}

}
