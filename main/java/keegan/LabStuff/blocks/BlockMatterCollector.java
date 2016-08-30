package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityMatterCollector;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;

public class BlockMatterCollector extends Block implements ITileEntityProvider {

	public BlockMatterCollector(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityMatterCollector();
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess a, int x, int y, int z, int side)
	{
		return false;
	}

	
	@Override
    public void onBlockAdded(World world, int x, int y, int z)
    {
		if(!world.getBlock(x, y-1, z).equals(LabStuffMain.blockMatterCollector))
			world.setBlock(x, y+1, z, LabStuffMain.blockMatterCollector);
    }
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			// System.out.println("Server");
			if (!player.isSneaking())
			{
				if(world.getBlock(x, y+1, z).equals(LabStuffMain.blockMatterCollector))
					player.openGui(LabStuffMain.instance, 18, world, x, y, z);
				else
					player.openGui(LabStuffMain.instance, 18, world, x, y-1, z);
				return true;
			}
		}
		return false;
	}
	
	@Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int metadata)
    {
    	super.onBlockDestroyedByPlayer(world, x, y, z, metadata);
    	if(world.getBlock(x, y-1, z).equals(LabStuffMain.blockMatterCollector))
    		world.setBlockToAir(x, y-1, z);
    	if(world.getBlock(x, y+1, z).equals(LabStuffMain.blockMatterCollector))
    		world.setBlockToAir(x, y+1, z);
    }


}
