package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRotary extends TileEntity
{
	protected int energy;
	private ForgeDirection dirIn;
	private ForgeDirection dirOut;
	
	public ForgeDirection getDirIn()
	{
		return dirIn;
	}
	protected void setDirIn(ForgeDirection dirIn)
	{
		this.dirIn = dirIn;
	}
	public ForgeDirection getDirOut()
	{
		return dirOut;
	}
	protected void setDirOut(ForgeDirection dirOut)
	{
		this.dirOut = dirOut;
	}
	
	public void addEnergy(int amt)
	{
		energy += amt;
		if(getNext() != null)
			getNext().addEnergy(amt);
	}
	public void subEnergy(int amt)
	{
		energy += amt;
		if(getNext() != null)
			getNext().addEnergy(-amt);
		if(getLast() != null)
			getLast().subEnergy(amt);
	}
	/**This is ussed for when you do not want a subtraction signal to travel back up the track**/
	public void subenergy(int amt)
	{
		energy += amt;
		if(getLast() != null)
			getLast().subEnergy(amt);
	}
	public int getEnergy()
	{
		return energy;
	}
	protected TileEntityRotary getNext()
	{
		if(dirOut != null)
		{
			if(dirOut.equals(ForgeDirection.DOWN))
			{
				if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				}
			}
			if(dirOut.equals(ForgeDirection.UP))
			{
				if(worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				}
			}
			if(dirOut.equals(ForgeDirection.NORTH))
			{
				if(worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				}
			}
			if(dirOut.equals(ForgeDirection.EAST))
			{
				if(worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				}
			}
			if(dirOut.equals(ForgeDirection.SOUTH))
			{
				if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				}
			}
			if(dirOut.equals(ForgeDirection.WEST))
			{
				if(worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
				}
			}
		}
		return null;
	}
	protected TileEntityRotary getLast()
	{
		if(dirIn != null)
		{
			if(dirIn.equals(ForgeDirection.DOWN))
			{
				if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
				}
			}
			if(dirIn.equals(ForgeDirection.UP))
			{
				if(worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord + 1, zCoord);
				}
			}
			if(dirIn.equals(ForgeDirection.NORTH))
			{
				if(worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
				}
			}
			if(dirIn.equals(ForgeDirection.EAST))
			{
				if(worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
				}
			}
			if(dirIn.equals(ForgeDirection.SOUTH))
			{
				if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
				}
			}
			if(dirIn.equals(ForgeDirection.WEST))
			{
				if(worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) instanceof TileEntityRotary)
				{
					return (TileEntityRotary)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
				}
			}
		}
		return null;
	}
	
	
	
}
