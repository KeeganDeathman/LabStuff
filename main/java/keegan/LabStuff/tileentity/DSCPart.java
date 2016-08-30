package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;

public class DSCPart extends TileEntity
{
	private String id;
	private TileEntityRibbonCable network;
	private int tickCount;
	
	
	@Override
	public void updateEntity()
	{
		tickCount++;
		if(tickCount>=100)
		{
			tickCount=0;
			if(id == null && !worldObj.isRemote)
				registerWithNetwork();
		}
	}
	
	public void registerWithNetwork()
	{
		if(!worldObj.isRemote) {
			System.out.println("remote");
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
		else
		{
			System.out.println("So remote");
		}
	}
	
	public void register(int x, int y, int z)
	{
		int idPossible = 0;
		if(worldObj.getTileEntity(x, y, z) instanceof TileEntityRibbonCable)
		while(id == null)
		{
			network = (TileEntityRibbonCable)worldObj.getTileEntity(x, y, z);
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
	
	public void performAction(String command, DSCPart sender) {}
	
	public TileEntityRibbonCable getNetwork()
	{
		return network;
	}
}
