package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;


public class TileEntityPowerCable extends TileEntityPower
{
	private boolean networked;
	
	public TileEntityPowerCable()
	{
		super();
		networked = false;
	}
	
	@Override
	public void update()
	{
		if(!networked)
			equalize();
	}
	
	//ONLY call when the block is added!
	public void equalize()
	{
		if(!worldObj.isRemote) {
			
			int xCoord = pos.getX();
			int yCoord = pos.getY();
			int zCoord = pos.getZ();
			
			if (getBlock(xCoord + 1, yCoord, zCoord) != null && getPower() == 0) {
				eqaulizeWith(xCoord + 1, yCoord, zCoord);
			}if (this.getBlock(xCoord - 1, yCoord, zCoord) != null && getPower() == 0) {
				eqaulizeWith(xCoord - 1, yCoord, zCoord);
			}if (this.getBlock(xCoord, yCoord + 1, zCoord) != null && getPower() == 0) {
					eqaulizeWith(xCoord, yCoord + 1, zCoord);
			}if (this.getBlock(xCoord, yCoord - 1, zCoord) != null && getPower() == 0) {
				 eqaulizeWith(xCoord, yCoord - 1, zCoord);
			}if (this.getBlock(xCoord, yCoord, zCoord + 1) != null && getPower() == 0) {
				 eqaulizeWith(xCoord, yCoord, zCoord + 1);
			}if (this.getBlock(xCoord, yCoord, zCoord - 1) != null && getPower() == 0) {
				 eqaulizeWith(xCoord, yCoord, zCoord - 1);
			}
		}
	}
	
	private Block getBlock(int i, int yCoord, int zCoord) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(new BlockPos(i, yCoord, zCoord)).getBlock();
	}

	private void eqaulizeWith(int x, int y, int z)
	{
		if(worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == LabStuffMain.blockPowerCable)
		{
			TileEntityPowerCable tile = (TileEntityPowerCable)worldObj.getTileEntity(new BlockPos(x,y,z));
			powerInt = tile.getPower();
			if(getPower() > 0)
				networked = true;
		}
	}
}
