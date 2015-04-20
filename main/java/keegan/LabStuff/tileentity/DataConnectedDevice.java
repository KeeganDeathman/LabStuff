package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;

public class DataConnectedDevice extends TileEntity
{
	private String id;
	
	
	@Override
	public void updateEntity()
	{
		if(id == null)
			registerWithNetwork();
	}
	
	public void registerWithNetwork()
	{
		if(!worldObj.isRemote) {
			if (worldObj.getBlock(xCoord + 1, yCoord, zCoord) != null && id == null) {
				register(xCoord + 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord - 1, yCoord, zCoord) != null && id == null) {
				register(xCoord - 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) != null && id == null) {
				register(xCoord, yCoord + 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) != null && id == null) {
				register(xCoord, yCoord - 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord + 1) != null && id == null) {
				register(xCoord, yCoord, zCoord + 1);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord - 1) != null && id == null) {
				register(xCoord, yCoord, zCoord - 1);
			}
		}
	}
	
	public void register(int x, int y, int z)
	{
		int idPossible = 0;
		if(worldObj.getTileEntity(x, y, z) instanceof TileEntityDataCable)
		while(id == null)
		{
			TileEntityDataCable network = (TileEntityDataCable)worldObj.getTileEntity(x, y, z);
			if(network.getDeviceById("_" + idPossible) != null)
			{
				idPossible += 1;
			}
			else
			{
				this.id = "_" + idPossible;
				network.addDevice(this);
			}
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void performAction(String command) {}
}
