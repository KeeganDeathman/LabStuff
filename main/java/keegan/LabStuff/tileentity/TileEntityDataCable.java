package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityDataCable extends TileEntity implements ITickable
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
			if (worldObj.getTileEntity(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ())) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ()));
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ())) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ()));
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ())) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ()));
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ())) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ()));
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() +1)) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() +1)) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() + 1));
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() -1)) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() -1)) instanceof TileEntityDataCable) {
				return (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() - 1));
			}
		}
		return null;
	}
	
	public TileEntityDataCable getNetwork(TileEntityDataCable src)
	{
		if(!worldObj.isRemote) {
			TileEntity posX = worldObj.getTileEntity(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ()));
			TileEntity negX = worldObj.getTileEntity(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ()));
			TileEntity posY = worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ()));
			TileEntity negY = worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ()));
			TileEntity posZ = worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() + 1));
			TileEntity negZ = worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() - 1));
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
			if (worldObj.getTileEntity(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ())) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX() + 1, getPos().getY(), getPos().getZ()));
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ())) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX() - 1, getPos().getY(), getPos().getZ()));
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ())) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() + 1, getPos().getZ()));
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ())) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ())) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY() - 1, getPos().getZ()));
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() +1)) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() +1)) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =   (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() + 1));
				if(cable.devices != null && cable.getDeviceCount() > 0)
					return cable;
			}if (worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() - 1)) != null  && worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() - 1)) instanceof TileEntityDataCable) {
				TileEntityDataCable cable =  (TileEntityDataCable)worldObj.getTileEntity(new BlockPos(getPos().getX(), getPos().getY(), getPos().getZ() - 1));
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
			if(worldObj.getTileEntity(new BlockPos(device.getPos().getX(), device.getPos().getY(), device.getPos().getZ())).equals(device)){}
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
	public void update()
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
