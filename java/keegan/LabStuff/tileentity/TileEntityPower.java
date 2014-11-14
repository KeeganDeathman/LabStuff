package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPower extends TileEntity
{
protected int powerInt = 0;
	
	public TileEntityPower()
	{
		super();
	}
	
	
	public void addPower(int addition, TileEntity issuer)
	{
		powerInt+=addition;
		if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null) 
		{
			((TileEntityPower) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
				((TileEntityPower) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null) 
		{
			((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
		{
			((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
		{
			((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)).addPowerNetworked(powerInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null) {
				((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1)).addPowerNetworked(powerInt, this);
		}
	}
	
	public void addPowerNetworked(int addition, TileEntityPower issuer)
	{
		if(powerInt + addition == issuer.getPower())
		{
			powerInt+=addition;
			if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null) 
			{
				((TileEntityPower) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
			{
				((TileEntityPower) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null) 
			{
				((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
			{
				((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
			{
				((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)).addPowerNetworked(powerInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null) 
			{
				((TileEntityPower) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1)).addPowerNetworked(powerInt, this);
			}
		}
		else
		{
			int diff = issuer.getPower() - powerInt;
			if(diff > 0)
			{
				this.addPowerNetworked(diff, issuer);
			}
		}
	}
	
	public void subtractPower(int subtraction, TileEntity issuer)
	{
		powerInt-=subtraction;
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null)
		{
			((TileEntityPower)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
		}
		if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null)
		{
			((TileEntityPower)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null)
		{
			((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).subtractPowerNetworked(powerInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null)
		{
			((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).subtractPowerNetworked(powerInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null)
		{
			((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).subtractPowerNetworked(powerInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null)
		{
			((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).subtractPowerNetworked(powerInt, this);
		}
	}
	
	public void subtractPowerNetworked(int subtraction, TileEntityPower issuer)
	{
		if(powerInt - subtraction == issuer.getPower())
		{
			powerInt-=subtraction;
			if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null)
			{
				((TileEntityPower)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null)
			{
				((TileEntityPower)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null)
			{
				((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null)
			{
				((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null)
			{
				((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).subtractPowerNetworked(powerInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPower && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null)
			{
				((TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).subtractPowerNetworked(powerInt, this);
			}
		}
		else
		{
			int diff = powerInt - issuer.getPower();
			if(diff > 0)
			{
				this.subtractPowerNetworked(diff, issuer);
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("power", this.powerInt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		powerInt = tag.getInteger("power");
	}
	
	public int getPower()
	{
		return powerInt;
	}
}
