package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
/**For use on tiles that will connect to power cables, but 
 * do not hold power. Machines for instance, which connect and drain power, but do not carry it.
 * **/
public class TileEntityPowerConnection extends TileEntity 
{
	
	//North = +z
	//South = -z
	//East = +x
	//West = -x
	protected ForgeDirection getDirectionOfConnection(TileEntity connection, TileEntity current)
	{
			if(current.xCoord != connection.xCoord)
			{
				if(current.xCoord > connection.xCoord)
				{
					return ForgeDirection.EAST;
				}
				else
				{
					return ForgeDirection.WEST;
				}
			}
			else if(current.yCoord != connection.yCoord)
			{
				if(current.yCoord > connection.yCoord)
				{
					return ForgeDirection.UP;
				}
				else
				{
					return ForgeDirection.DOWN;
				}
			}
			else if(current.zCoord != connection.zCoord)
			{
				if(current.zCoord > connection.zCoord)
				{
					return ForgeDirection.NORTH;
				}
				else
				{
					return ForgeDirection.SOUTH;
				}
			}
			return null;
	}

	protected TileEntityPower getPowerSource()
	{
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord, yCoord+1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
			return powerSource;
		}
		return null;
	}
	
	protected TileEntityPower getPowerSourceHorizontal()
	{
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord+1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord-1, yCoord, zCoord);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord+1);
			return powerSource;
		}
		else if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(xCoord, yCoord, zCoord-1);
			return powerSource;
		}
		return null;
	}
	
	
}
