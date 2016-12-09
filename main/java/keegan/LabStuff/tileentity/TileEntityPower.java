package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityPower extends TileEntity implements ITickable
{
protected int powerInt = 0;
	
	public TileEntityPower()
	{
		super();
	}
	
	
	public void addPower(int addition, TileEntity issuer)
	{
		powerInt+=addition;
		
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();
		
		if (getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord + 1, yCoord, zCoord) != issuer && getTileEntity(xCoord + 1, yCoord, zCoord) != null) 
		{
			((TileEntityPower) getTileEntity(xCoord + 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord - 1, yCoord, zCoord) != issuer && getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
				((TileEntityPower) getTileEntity(xCoord - 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord, yCoord + 1, zCoord) != issuer && getTileEntity(xCoord, yCoord + 1, zCoord) != null) 
		{
			((TileEntityPower) getTileEntity(xCoord, yCoord + 1, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord, yCoord - 1, zCoord) != issuer && getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
		{
			((TileEntityPower) getTileEntity(xCoord, yCoord - 1, zCoord)).addPowerNetworked(powerInt, this);
		}
		if (getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord + 1) != issuer && getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
		{
			((TileEntityPower) getTileEntity(xCoord, yCoord, zCoord + 1)).addPowerNetworked(powerInt, this);
		}
		if (getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord - 1) != issuer && getTileEntity(xCoord, yCoord, zCoord - 1) != null) {
				((TileEntityPower) getTileEntity(xCoord, yCoord, zCoord - 1)).addPowerNetworked(powerInt, this);
		}
	}
	
	public void addPowerNetworked(int addition, TileEntityPower issuer)
	{
		if(powerInt + addition == issuer.getPower())
		{
			powerInt+=addition;
			
			int xCoord = pos.getX();
			int yCoord = pos.getY();
			int zCoord = pos.getZ();
			
			if (getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord + 1, yCoord, zCoord) != issuer && getTileEntity(xCoord + 1, yCoord, zCoord) != null) 
			{
				((TileEntityPower) getTileEntity(xCoord + 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord - 1, yCoord, zCoord) != issuer && getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
			{
				((TileEntityPower) getTileEntity(xCoord - 1, yCoord, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord, yCoord + 1, zCoord) != issuer && getTileEntity(xCoord, yCoord + 1, zCoord) != null) 
			{
				((TileEntityPower) getTileEntity(xCoord, yCoord + 1, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityPower && getTileEntity(xCoord, yCoord - 1, zCoord) != issuer && getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
			{
				((TileEntityPower) getTileEntity(xCoord, yCoord - 1, zCoord)).addPowerNetworked(powerInt, this);
			}
			if (getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord + 1) != issuer && getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
			{
				((TileEntityPower) getTileEntity(xCoord, yCoord, zCoord + 1)).addPowerNetworked(powerInt, this);
			}
			if (getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord - 1) != issuer && getTileEntity(xCoord, yCoord, zCoord - 1) != null) 
			{
				((TileEntityPower) getTileEntity(xCoord, yCoord, zCoord - 1)).addPowerNetworked(powerInt, this);
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
	
	public boolean subtractPower(int subtraction, TileEntity issuer)
	{
		if(powerInt >= subtraction)
		{
			powerInt-=subtraction;
			
			int xCoord = pos.getX();
			int yCoord = pos.getY();
			int zCoord = pos.getZ();
			
			if(getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPower && getTileEntity(xCoord+1, yCoord, zCoord) != issuer && getTileEntity(xCoord+1, yCoord, zCoord) != null)
			{
				((TileEntityPower)getTileEntity(xCoord+1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPower && getTileEntity(xCoord-1, yCoord, zCoord) != issuer && getTileEntity(xCoord-1, yCoord, zCoord) != null)
			{
				((TileEntityPower)getTileEntity(xCoord-1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPower && getTileEntity(xCoord, yCoord+1, zCoord) != issuer && getTileEntity(xCoord, yCoord+1, zCoord) != null)
			{
				((TileEntityPower)getTileEntity(xCoord, yCoord+1, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPower && getTileEntity(xCoord, yCoord-1, zCoord) != issuer && getTileEntity(xCoord, yCoord-1, zCoord) != null)
			{
				((TileEntityPower)getTileEntity(xCoord, yCoord-1, zCoord)).subtractPowerNetworked(powerInt, this);
			}
			if(getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord+1) != issuer && getTileEntity(xCoord, yCoord, zCoord+1) != null)
			{
				((TileEntityPower)getTileEntity(xCoord, yCoord, zCoord+1)).subtractPowerNetworked(powerInt, this);
			}
			if(getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord-1) != issuer && getTileEntity(xCoord, yCoord, zCoord-1) != null)
			{
				((TileEntityPower)getTileEntity(xCoord, yCoord, zCoord-1)).subtractPowerNetworked(powerInt, this);
			}
			return true;
		}
		return false;
	}
	
	public void subtractPowerNetworked(int subtraction, TileEntityPower issuer)
	{
		if(powerInt >= subtraction)
		{

			if(powerInt - subtraction == issuer.getPower())
			{
				powerInt-=subtraction;
				
				int xCoord = pos.getX();
				int yCoord = pos.getY();
				int zCoord = pos.getZ();
				
				if(getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPower && getTileEntity(xCoord+1, yCoord, zCoord) != issuer && getTileEntity(xCoord+1, yCoord, zCoord) != null)
				{
					((TileEntityPower)getTileEntity(xCoord+1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
				}
				if(getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPower && getTileEntity(xCoord-1, yCoord, zCoord) != issuer && getTileEntity(xCoord-1, yCoord, zCoord) != null)
				{
					((TileEntityPower)getTileEntity(xCoord-1, yCoord, zCoord)).subtractPowerNetworked(powerInt, this);
				}
				if(getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPower && getTileEntity(xCoord, yCoord+1, zCoord) != issuer && getTileEntity(xCoord, yCoord+1, zCoord) != null)
				{
					((TileEntityPower)getTileEntity(xCoord, yCoord+1, zCoord)).subtractPowerNetworked(powerInt, this);
				}
				if(getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPower && getTileEntity(xCoord, yCoord-1, zCoord) != issuer && getTileEntity(xCoord, yCoord-1, zCoord) != null)
				{
					((TileEntityPower)getTileEntity(xCoord, yCoord-1, zCoord)).subtractPowerNetworked(powerInt, this);
				}
				if(getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord+1) != issuer && getTileEntity(xCoord, yCoord, zCoord+1) != null)
				{
					((TileEntityPower)getTileEntity(xCoord, yCoord, zCoord+1)).subtractPowerNetworked(powerInt, this);
				}
				if(getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPower && getTileEntity(xCoord, yCoord, zCoord-1) != issuer && getTileEntity(xCoord, yCoord, zCoord-1) != null)
				{
					((TileEntityPower)getTileEntity(xCoord, yCoord, zCoord-1)).subtractPowerNetworked(powerInt, this);
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
	}
	
	private TileEntity getTileEntity(int i, int yCoord, int zCoord) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("power", this.powerInt);
		
		return tag;
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


	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
