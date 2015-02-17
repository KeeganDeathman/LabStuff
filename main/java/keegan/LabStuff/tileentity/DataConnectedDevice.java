package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;

public class DataConnectedDevice extends TileEntity
{
	private int id;
	
	public DataConnectedDevice(int id, int x, int y, int z)
	{
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void performAction(String command) {}
}
