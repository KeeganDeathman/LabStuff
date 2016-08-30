package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityAcceleratorInterface;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAcceleratorInterface extends Block implements ITileEntityProvider
{

	public BlockAcceleratorInterface()
	{
		super(Material.iron);
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityAcceleratorInterface();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			// System.out.println("Server");
			if (!player.isSneaking())
			{
				player.openGui(LabStuffMain.instance, 12, world, x, y, z);
				return true;
			}
		}
		return false;
	}

}
