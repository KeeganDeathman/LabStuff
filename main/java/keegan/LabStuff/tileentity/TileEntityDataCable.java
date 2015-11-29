package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;

public class TileEntityDataCable extends TileEntity 
{
	private ArrayList<DataConnectedDevice> devices;
	private boolean networked;
	private int tickCount;
	
	public TileEntityDataCable()
	{
		devices = new ArrayList<DataConnectedDevice>();
		networked = false;
		tickCount = 0;
	}
	
	public void addDevice(DataConnectedDevice device)
	{
		devices.add(device);
		TileEntityDataCable next = getNetwork(this);
		if(next != null)
		{
			next.addDevice(device, this);
		}
	}
	public void addDevice(DataConnectedDevice device, TileEntityDataCable src)
	{
		devices.add(device);
		TileEntityDataCable next = getNetwork(src);
		if(next != null)
		{
			next.addDevice(device, this);
		}
	}
	
	public TileEntityDataCable getNetwork()
	{
		if(!worldObj.isRemote) {
			if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null  && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			}if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null  && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			}if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null  && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			}if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null  && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
			}if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null  && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			}if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null  && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			}
		}
		return null;
	}
	
	public TileEntityDataCable getNetwork(TileEntityDataCable src)
	{
		if(!worldObj.isRemote) {
			TileEntity posX = worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			TileEntity negX = worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			TileEntity posY = worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
			TileEntity negY = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
			TileEntity posZ = worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			TileEntity negZ = worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			if (posX != null  && posX instanceof TileEntityDataCable && !posX.equals(src) && !posX.equals(src)) {
				return (TileEntityDataCable)posX;
			}if (negX != null  && negX instanceof TileEntityDataCable && !negX.equals(src)) {
				return (TileEntityDataCable)negX;
			}if (posY != null  && posY instanceof TileEntityDataCable && !posY.equals(src)) {
				return (TileEntityDataCable)posY;
			}if (negY != null  && negY instanceof TileEntityDataCable && !negY.equals(src)) {
				return (TileEntityDataCable)negY;
			}if (posZ != null  && posZ instanceof TileEntityDataCable && !posZ.equals(src)) {
				return (TileEntityDataCable)posZ;
			}if (negZ != null  && negZ instanceof TileEntityDataCable && !negZ.equals(src)) {
				return (TileEntityDataCable)negZ;
			}
		}
		return null;
	}
	
	public TileEntityDataCable getNetworkWithDevices()
	{
		if(!worldObj.isRemote) {
			if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null  && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null  && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null  && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null  && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null  && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null  && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =  (TileEntityDataCable)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}
		}
		return null;
	}
	
	public void validateDevices()
	{
		for(DataConnectedDevice device : devices)
		{
			if(worldObj.getTileEntity(device.xCoord, device.yCoord, device.zCoord).equals(device)){}
			else
			{ 
				removeDeviceById(device.getId());
			}
		}
	}
	
	public DataConnectedDevice getDeviceById(String id)
	{
		for(DataConnectedDevice device:devices)
		{
			if(device.getId() == id)
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
	
	public DataConnectedDevice getDeviceByIndex(int index)
	{
		if(devices.get(index) != null)
			return devices.get(index);
		return null;
	}
	
	public void removeDeviceById(String id)
	{
		for(DataConnectedDevice device:devices)
		{
			if(device.getId().equals(id))
			{
				devices.remove(device);
				TileEntityDataCable next = getNetwork();
				if(next != null)
				{
					next.removeDeviceById(id, this);
				}
			}
		}
	}
	
	public void removeDeviceById(String id, TileEntityDataCable src)
	{
		for(DataConnectedDevice device:devices)
		{
			if(device.getId().equals(id))
			{
				devices.remove(device);
				TileEntityDataCable next = getNetwork(src);
				if(next != null)
				{
					next.removeDeviceById(id, this);
				}
			}
		}
	}
	
	public void removeDeviceByIndex(int index)
	{
		if(devices.get(index) != null)
		{
			devices.remove(index);
			TileEntityDataCable next = getNetwork();
			if(next != null)
			{
				next.removeDeviceByIndex(index, this);
			}
		}
	}
	
	public void removeDeviceByIndex(int index, TileEntityDataCable src)
	{
		if(devices.get(index) != null)
			devices.remove(index);
		TileEntityDataCable next = getNetwork(src);
		if(next != null)
		{
			next.removeDeviceByIndex(index, this);
		}
	}
	
	public void sendMessage(DataPackage msg)
	{
		if(getDeviceById(msg.getTarget().getId()) != null)
		{
			msg.getTarget().performAction(msg.getMessage());
		}
	}
	
	@Override
	public void updateEntity()
	{
		tickCount++;
		if(tickCount>=120)
		{
			if(!networked)
			{
				if(getNetworkWithDevices() != null)
				{
					ArrayList<DataConnectedDevice> devices2 = getNetworkWithDevices().devices;
					if(devices2 != null)
					{
						devices = devices2;
						if(devices.size() > 0)
							networked = true;
					}
				}
			}
		}
	}
}
