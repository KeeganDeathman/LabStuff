package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityAcceleratorControlPanel;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;

public class BlockAcceleratorControlPanel extends Block implements ITileEntityProvider
{

	public BlockAcceleratorControlPanel() 
	{
		super(Material.iron);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityAcceleratorControlPanel();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			// System.out.println("Server");
			if (!player.isSneaking())
			{
				player.openGui(LabStuffMain.instance, 11, world, x, y, z);
				return true;
			}
		}
		return false;
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
    	super.onBlockPlacedBy(world, x, y, z, player, stack);
    	world.setBlock(x-3, y, z, LabStuffMain.blockACPGag);
    	world.setBlock(x-2, y, z, LabStuffMain.blockACPGag);
    	world.setBlock(x-1, y, z, LabStuffMain.blockACPGag);
    	world.setBlock(x-3, y+1, z, LabStuffMain.blockACPGag);
    	world.setBlock(x-2, y+1, z, LabStuffMain.blockACPGag);
    	world.setBlock(x-1, y+1, z, LabStuffMain.blockACPGag);
    	world.setBlock(x, y, z+2, LabStuffMain.blockACPGag);
    	world.setBlock(x, y, z+1, LabStuffMain.blockACPGag);
    	world.setBlock(x, y+1, z+2, LabStuffMain.blockACPGag);
    	world.setBlock(x, y+1, z+1, LabStuffMain.blockACPGag);
    	world.setBlock(x, y+1, z, LabStuffMain.blockACPGag);
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
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		((TileEntityAcceleratorControlPanel)world.getTileEntity(x, y, z)).collision();
	}

	
	

}
