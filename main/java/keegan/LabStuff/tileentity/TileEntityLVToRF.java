package keegan.labstuff.tileentity;

import cofh.api.energy.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityLVToRF extends TileEntityPowerConnection implements IEnergyProvider
{
	public EnergyStorage storage;
	
	public TileEntityLVToRF()
	{
		storage = new EnergyStorage(1000);
		storage.setMaxReceive(500);
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
		connectedTEs[0] = worldObj.getTileEntity(pos.west());
		connectedTEs[1] = worldObj.getTileEntity(pos.east());
		connectedTEs[2] = worldObj.getTileEntity(pos.down());
		connectedTEs[3] = worldObj.getTileEntity(pos.up());
		connectedTEs[4] = worldObj.getTileEntity(pos.north());
		connectedTEs[5] = worldObj.getTileEntity(pos.south());
		for(int i = 0; i < connectedTEs.length; i++)
		{
			if(connectedTEs[i] != null && connectedTEs[i] instanceof IEnergyReceiver && getPowerSource() != null)
			{
				if(((TileEntityPower)getPowerSource()).subtractPower(storage.receiveEnergy(storage.getMaxReceive(), true)*2, this))
				{
					((IEnergyReceiver)connectedTEs[i]).receiveEnergy(calcDirection(connectedTEs[i]).getOpposite(), storage.getMaxReceive(), false);
				}
			}
		}
	}
	
	private EnumFacing calcDirection(TileEntity tile)
	{
		for(EnumFacing dir : EnumFacing.VALUES)
		{
			if(pos.offset(dir).equals(tile.getPos()))
				return dir;
		}
		
		return null;
	}

	
	@Override
	public boolean canConnectEnergy(EnumFacing from) 
	{
		return true;
	}
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) 
	{
		return storage.extractEnergy(maxExtract, simulate);
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
