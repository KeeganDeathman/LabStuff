package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;

public class TileEntityDataCable extends TileEntity 
{
	private ArrayList<DataConnectedDevice> devices;
	
	public TileEntityDataCable()
	{
		devices = new ArrayList<DataConnectedDevice>();
	}
	
	public void addDevice(DataConnectedDevice device)
	{
		devices.add(device);
	}
	
	public DataConnectedDevice getDeviceById(int id)
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
	
	public DataConnectedDevice getDeviceByIndex(int index)
	{
		if(devices.get(index) != null)
			return devices.get(index);
		return null;
	}
	
	public void removeDeviceById(int id)
	{
		for(DataConnectedDevice device:devices)
		{
			if(device.getId() == id)
			{
				devices.remove(device);
			}
		}
	}
	
	public void removeDeviceByIndex(int index)
	{
		if(devices.get(index) != null)
			devices.remove(index);
	}
	
	public void sendMessage(Package msg)
	{
		if(getDeviceById(msg.getTarget().getId()) != null)
		{
			msg.getTarget().performAction(msg.getMessage());
		}
	}
	
	public abstract class Package
	{
		private DataConnectedDevice target;
		private String message;
		
		public Package(DataConnectedDevice target, String message) {
			super();
			this.target = target;
			this.message = message;
		}

		public DataConnectedDevice getTarget() {
			return target;
		}

		public String getMessage() {
			return message;
		}
	}
}
