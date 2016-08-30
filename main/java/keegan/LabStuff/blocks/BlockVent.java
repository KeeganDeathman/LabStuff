package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityVent;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockVent extends Block implements ITileEntityProvider {

	public BlockVent(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
	{
		if(!world.isRemote)
		{
			player.openGui(LabStuffMain.instance, 10, world, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() { return false; }

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityVent();
	}

}
