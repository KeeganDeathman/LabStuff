package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;


public class TileEntityPowerCable extends TileEntityPower
{
	private boolean networked;
	
	public TileEntityPowerCable()
	{
		super();
		networked = false;
	}
	
	@Override
	public void updateEntity()
	{
		if(!networked)
			equalize();
	}
	
	//ONLY call when the block is added!
	public void equalize()
	{
		if(!worldObj.isRemote) {
			if (worldObj.getBlock(xCoord + 1, yCoord, zCoord) != null && getPower() == 0) {
				eqaulizeWith(xCoord + 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord - 1, yCoord, zCoord) != null && getPower() == 0) {
				eqaulizeWith(xCoord - 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) != null && getPower() == 0) {
					eqaulizeWith(xCoord, yCoord + 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) != null && getPower() == 0) {
				 eqaulizeWith(xCoord, yCoord - 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord + 1) != null && getPower() == 0) {
				 eqaulizeWith(xCoord, yCoord, zCoord + 1);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord - 1) != null && getPower() == 0) {
				 eqaulizeWith(xCoord, yCoord, zCoord - 1);
			}
		}
	}
	
	private void eqaulizeWith(int x, int y, int z)
	{
		if(worldObj.getBlock(x,y,z) == LabStuffMain.blockPowerCable)
		{
			TileEntityPowerCable tile = (TileEntityPowerCable)worldObj.getTileEntity(x,y,z);
			powerInt = tile.getPower();
			if(getPower() > 0)
				networked = true;
		}
	}
}
