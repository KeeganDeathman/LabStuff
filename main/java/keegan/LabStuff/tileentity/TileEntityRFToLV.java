package keegan.labstuff.tileentity;

import cofh.api.energy.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

public class TileEntityRFToLV extends TileEntity implements IEnergyReceiver, ITickable
{
	
	public EnergyStorage storage;
	
	public TileEntityRFToLV()
	{
		storage = new EnergyStorage(getMaxEnergyStored());
	}
	
	protected int getMaxEnergyStored()
	{
		return 1000;
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		storage.writeToNBT(tag);
		
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		storage.readFromNBT(tag);
	}
	
	@Override
	public void update()
	{
		TileEntity[] connectedTEs = new TileEntity[6];
		connectedTEs[0] = worldObj.getTileEntity(pos.east());
		connectedTEs[1] = worldObj.getTileEntity(pos.west());
		connectedTEs[2] = worldObj.getTileEntity(pos.up());
		connectedTEs[3] = worldObj.getTileEntity(pos.down());
		connectedTEs[4] = worldObj.getTileEntity(pos.north());
		connectedTEs[5] = worldObj.getTileEntity(pos.south());
		for(int i = 0; i < connectedTEs.length; i++)
		{
			if(connectedTEs[i] != null && connectedTEs[i] instanceof TileEntityPower)
			{
				((TileEntityPower)connectedTEs[i]).addPower(storage.extractEnergy(500, false)*2, this);
				
			}
		}
	}
	
	@Override
	public boolean canConnectEnergy(EnumFacing from) 
	{
		return true;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive,boolean simulate) 
	{
		return storage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing from) 
	{
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) 
	{
		return storage.getMaxEnergyStored();
	}

}
