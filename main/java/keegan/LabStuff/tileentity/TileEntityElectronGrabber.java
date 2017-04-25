package keegan.labstuff.tileentity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityElectronGrabber extends TileEntity {

	public Block[][][] multiblocks = new Block[3][3][3];
	private boolean multiblock = false;
	private BlockPos coords;

	public TileEntityElectronGrabber() {
	}
	

}
