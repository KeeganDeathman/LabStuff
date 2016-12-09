package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

public class TileEntityRotary extends TileEntity implements ITickable
{
	protected int energy;
	private EnumFacing dirIn;
	private EnumFacing dirOut;
	
	public EnumFacing getDirIn()
	{
		return dirIn;
	}
	protected void setDirIn(EnumFacing dirIn)
	{
		this.dirIn = dirIn;
	}
	public EnumFacing getDirOut()
	{
		return dirOut;
	}
	protected void setDirOut(EnumFacing dirOut)
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
		energy -= amt;
		if(getNext() != null)
			getNext().addEnergy(-amt);
		if(getLast() != null)
			getLast().subEnergy(amt);
	}
	/**This is ussed for when you do not want a subtraction signal to travel back up the track**/
	public void subenergy(int amt)
	{
		energy -= amt;
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
			if(worldObj.getTileEntity(pos.offset(dirOut)) != null && worldObj.getTileEntity(pos.offset(dirOut)) instanceof TileEntityRotary)
				return (TileEntityRotary)worldObj.getTileEntity(pos.offset(dirOut));
		}
		return null;
	}
	protected TileEntityRotary getLast()
	{
		if(dirIn != null)
		{if(worldObj.getTileEntity(pos.offset(dirIn)) != null && worldObj.getTileEntity(pos.offset(dirIn)) instanceof TileEntityRotary)
			return (TileEntityRotary)worldObj.getTileEntity(pos.offset(dirIn));}
		return null;
	}
	
	public void update()
	{
		
	}
	
	
	
}
