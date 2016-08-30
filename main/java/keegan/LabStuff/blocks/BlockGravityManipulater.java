package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityGravityManipulater;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockGravityManipulater extends Block implements ITileEntityProvider
{

	public BlockGravityManipulater(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityGravityManipulater();
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			// System.out.println("Server");
			TileEntityGravityManipulater te = (TileEntityGravityManipulater) world.getTileEntity(x, y, z);
			if (!player.isSneaking() && te != null)
			{
				// System.out.println("All systems are go!");
				player.openGui(LabStuffMain.instance, 16, world, x, y, z);
				// System.out.println("Gui opened");
				return true;
			}
		}
		return false;
	}

}
