package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityRibbonCable extends TileEntity implements ITickable
{

	protected ArrayList<DSCPart> devices;
	protected boolean networked;
	protected int tickCount;

	public TileEntityRibbonCable()
	{
		devices = new ArrayList<DSCPart>();
		networked = false;
		tickCount = 0;
	}

	public void addDevice(DSCPart device)
	{
		devices.add(device);
		TileEntityRibbonCable next = getNetwork(this);
		if (next != null)
		{
			next.addDevice(device, this);
		}
	}

	public void addDevice(DSCPart device, TileEntityRibbonCable src)
	{
		devices.add(device);
		TileEntityRibbonCable next = getNetwork(src);
		if (next != null)
		{
			next.addDevice(device, this);
		}
	}

	public TileEntityRibbonCable getNetwork()
	{
		if (!worldObj.isRemote)
		{
			
			int xCoord = pos.getX();
			int yCoord = pos.getY();
			int zCoord = pos.getZ();
			
			if (getTileEntity(xCoord + 1, yCoord, zCoord) != null && getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) getTileEntity(xCoord + 1, yCoord, zCoord);
			}
			if (getTileEntity(xCoord - 1, yCoord, zCoord) != null && getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) getTileEntity(xCoord - 1, yCoord, zCoord);
			}
			if (getTileEntity(xCoord, yCoord + 1, zCoord) != null && getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) getTileEntity(xCoord, yCoord + 1, zCoord);
			}
			if (getTileEntity(xCoord, yCoord - 1, zCoord) != null && getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) getTileEntity(xCoord, yCoord - 1, zCoord);
			}
			if (getTileEntity(xCoord, yCoord, zCoord + 1) != null && getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) getTileEntity(xCoord, yCoord, zCoord + 1);
			}
			if (getTileEntity(xCoord, yCoord, zCoord - 1) != null && getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) getTileEntity(xCoord, yCoord, zCoord - 1);
			}
		}
		return null;
	}

	private TileEntity getTileEntity(int i, int yCoord, int zCoord) {
		// TODO Auto-generated method stub
		return worldObj.getTileEntity(new BlockPos(i, yCoord, zCoord));
	}

	public TileEntityRibbonCable getNetwork(TileEntityRibbonCable src)
	{
		if (!worldObj.isRemote)
		{
			int xCoord = pos.getX();
			int yCoord = pos.getY();
			int zCoord = pos.getZ();
			TileEntity posX = getTileEntity(xCoord + 1, yCoord, zCoord);
			TileEntity negX = getTileEntity(xCoord - 1, yCoord, zCoord);
			TileEntity posY = getTileEntity(xCoord, yCoord + 1, zCoord);
			TileEntity negY = getTileEntity(xCoord, yCoord - 1, zCoord);
			TileEntity posZ = getTileEntity(xCoord, yCoord, zCoord + 1);
			TileEntity negZ = getTileEntity(xCoord, yCoord, zCoord - 1);
			if (posX != null && posX instanceof TileEntityRibbonCable && !posX.equals(src) && !posX.equals(src))
			{
				return (TileEntityRibbonCable) posX;
			}
			if (negX != null && negX instanceof TileEntityRibbonCable && !negX.equals(src))
			{
				return (TileEntityRibbonCable) negX;
			}
			if (posY != null && posY instanceof TileEntityRibbonCable && !posY.equals(src))
			{
				return (TileEntityRibbonCable) posY;
			}
			if (negY != null && negY instanceof TileEntityRibbonCable && !negY.equals(src))
			{
				return (TileEntityRibbonCable) negY;
			}
			if (posZ != null && posZ instanceof TileEntityRibbonCable && !posZ.equals(src))
			{
				return (TileEntityRibbonCable) posZ;
			}
			if (negZ != null && negZ instanceof TileEntityRibbonCable && !negZ.equals(src))
			{
				return (TileEntityRibbonCable) negZ;
			}
		}
		return null;
	}

	public TileEntityRibbonCable getNetworkWithDevices()
	{
		if (!worldObj.isRemote)
		{
			int xCoord = pos.getX();
			int yCoord = pos.getY();
			int zCoord = pos.getZ();
			if (getTileEntity(xCoord + 1, yCoord, zCoord) != null && getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) getTileEntity(xCoord + 1, yCoord, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (getTileEntity(xCoord - 1, yCoord, zCoord) != null && getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) getTileEntity(xCoord - 1, yCoord, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (getTileEntity(xCoord, yCoord + 1, zCoord) != null && getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) getTileEntity(xCoord, yCoord + 1, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (getTileEntity(xCoord, yCoord - 1, zCoord) != null && getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) getTileEntity(xCoord, yCoord - 1, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (getTileEntity(xCoord, yCoord, zCoord + 1) != null && getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) getTileEntity(xCoord, yCoord, zCoord + 1);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (getTileEntity(xCoord, yCoord, zCoord - 1) != null && getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) getTileEntity(xCoord, yCoord, zCoord - 1);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
		}
		return null;
	}

	public void validateDevices()
	{
		for (DSCPart device : devices)
		{
			if (worldObj.getTileEntity(device.getPos()).equals(device))
			{}
			else
			{
				removeDeviceById(device.getId());
			}
		}
	}

	public DSCPart getDeviceById(String id)
	{
		for (DSCPart device : devices)
		{
			if (device.getId() == id)
			{
				return device;
			}
		}
		return null;
	}

	public int getDeviceCount()
	{
		return devices.size();
	}

	public DSCPart getDeviceByIndex(int index)
	{
		if (devices.get(index) != null) return devices.get(index);
		return null;
	}

	public void removeDeviceById(String id)
	{
		for (DSCPart device : devices)
		{
			if (device.getId().equals(id))
			{
				devices.remove(device);
				TileEntityRibbonCable next = getNetwork();
				if (next != null)
				{
					next.removeDeviceById(id, this);
				}
			}
		}
	}

	public void removeDeviceById(String id, TileEntityRibbonCable src)
	{
		for (DSCPart device : devices)
		{
			if (device.getId().equals(id))
			{
				devices.remove(device);
				TileEntityRibbonCable next = getNetwork(src);
				if (next != null)
				{
					next.removeDeviceById(id, this);
				}
			}
		}
	}

	public void removeDeviceByIndex(int index)
	{
		if (devices.get(index) != null)
		{
			devices.remove(index);
			TileEntityRibbonCable next = getNetwork();
			if (next != null)
			{
				next.removeDeviceByIndex(index, this);
			}
		}
	}

	public void removeDeviceByIndex(int index, TileEntityRibbonCable src)
	{
		if (devices.get(index) != null) devices.remove(index);
		TileEntityRibbonCable next = getNetwork(src);
		if (next != null)
		{
			next.removeDeviceByIndex(index, this);
		}
	}

	public void sendMessage(DSCPackage msg)
	{
		if (getDeviceById(msg.getTarget().getId()) != null)
		{
			msg.getTarget().performAction(msg.getMessage(), msg.getSender());
		}
	}

	@Override
	public void update()
	{
		tickCount++;
		if (tickCount >= 120)
		{
			if (!networked)
			{
				if (getNetworkWithDevices() != null)
				{
					ArrayList<DSCPart> devices2 = getNetworkWithDevices().devices;
					if (devices2 != null)
					{
						devices = devices2;
						if (devices.size() > 0) networked = true;
					}
				}
			}
		}
	}

}
