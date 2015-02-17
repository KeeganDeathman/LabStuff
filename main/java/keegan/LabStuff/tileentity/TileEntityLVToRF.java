package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;

public class TileEntityLVToRF extends TileEntityPowerConnection implements IEnergyProvider
{
	public EnergyStorage storage;
	
	public TileEntityLVToRF()
	{
		storage = new EnergyStorage(1000);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		storage.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		storage.readFromNBT(tag);
	}
	
	
	
	
	@Override
	public void updateEntity()
	{
		TileEntity[] connectedTEs = new TileEntity[6];
		connectedTEs[0] = worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
		connectedTEs[1] = worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
		connectedTEs[2] = worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
		connectedTEs[3] = worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
		connectedTEs[4] = worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
		connectedTEs[5] = worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
		for(int i = 0; i < connectedTEs.length; i++)
		{
			if(connectedTEs[i] != null && connectedTEs[i] instanceof IEnergyHandler && getPowerSource() != null)
			{
				if(((TileEntityPower)getPowerSource()).subtractPower(storage.receiveEnergy(storage.getMaxReceive(), true)*2, this))
				{
					((IEnergyHandler)connectedTEs[i]).receiveEnergy(getDirectionOfConnection(connectedTEs[i], this), ((IEnergyHandler)connectedTEs[i]).getMaxEnergyStored(null), false);
				}
			}
		}
	}
	
	@Override
	public boolean canConnectEnergy(ForgeDirection from) 
	{
		return true;
	}
	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) 
	{
		return storage.extractEnergy(maxExtract, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) 
	{
		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) 
	{
		return storage.getMaxEnergyStored();
	}

}
