package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityRotary extends TileEntity
{
	private int energy;
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
	protected TileEntityRotary getNext()
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
			if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRotary)
			{
				return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1);
			}
		}
		if(dirOut.equals(ForgeDirection.EAST))
		{
			if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRotary)
			{
				return (TileEntityRotary)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord);
			}
		}
		if(dirOut.equals(ForgeDirection.SOUTH))
		{
			if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRotary)
			{
				return (TileEntityRotary)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1);
			}
		}
		if(dirOut.equals(ForgeDirection.WEST))
		{
			if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityRotary)
			{
				return (TileEntityRotary)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord);
			}
		}
		return null;
	}
	
}
