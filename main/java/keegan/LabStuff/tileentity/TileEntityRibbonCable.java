package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;

public class TileEntityRibbonCable extends TileEntity
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
			if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			}
			if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			}
			if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			}
			if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityRibbonCable)
			{
				return (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			}
		}
		return null;
	}

	public TileEntityRibbonCable getNetwork(TileEntityRibbonCable src)
	{
		if (!worldObj.isRemote)
		{
			TileEntity posX = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			TileEntity negX = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			TileEntity posY = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			TileEntity negY = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
			TileEntity posZ = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			TileEntity negZ = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
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
			if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityRibbonCable)
			{
				TileEntityRibbonCable cable = (TileEntityRibbonCable) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				if (cable.devices != null && cable.getDeviceCount() > 0) return cable;
			}
		}
		return null;
	}

	public void validateDevices()
	{
		for (DSCPart device : devices)
		{
			if (worldObj.getTileEntity(device.xCoord, device.yCoord, device.zCoord).equals(device))
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
	public void updateEntity()
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
