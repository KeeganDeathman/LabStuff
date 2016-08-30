package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityDLLaptop;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.*;

public class BlockDLLaptop extends Block implements ITileEntityProvider
{
	

	public BlockDLLaptop(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		// TODO Auto-generated method stub
		return new TileEntityDLLaptop();
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
    	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	world.setBlockMetadataWithNotify(x, y, z, l, 2);
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
    public boolean renderAsNormalBlock()
    {
    	return false;
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	if(!world.isRemote)
    	{
    		//System.out.println("Server");
    		TileEntityDLLaptop te = (TileEntityDLLaptop)world.getTileEntity(x, y, z);
    		if(!player.isSneaking() && te != null)
        	{
    			//System.out.println("All systems are go!");
      			player.openGui(LabStuffMain.instance, 15, world, x, y, z);
      			//System.out.println("Gui opened");
      			return true;
    		}
    	}
    	return false;
    }

}
