package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TERadioKit;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class RadioKit extends BlockComputerDevice implements ITileEntityProvider {

	public RadioKit(Material materialIn) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		// TODO Auto-generated method stub
		return new TERadioKit();
	}

}
