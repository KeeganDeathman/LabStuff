package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
/**For use on tiles that will connect to power cables, but 
 * do not hold power. Machines for instance, which connect and drain power, but do not carry it.
 * **/
public class TileEntityPowerConnection extends TileEntity implements ITickable
{
	
	//North = +z
	//South = -z
	//East = +x
	//West = -x
	protected EnumFacing getDirectionOfConnection(TileEntity connection, TileEntity current)
	{
			if(current.getPos().getX() != connection.getPos().getX())
			{
				if(current.getPos().getX() > connection.getPos().getX())
				{
					return EnumFacing.EAST;
				}
				else
				{
					return EnumFacing.WEST;
				}
			}
			else if(current.getPos().getY() != connection.getPos().getY())
			{
				if(current.getPos().getY() > connection.getPos().getY())
				{
					return EnumFacing.UP;
				}
				else
				{
					return EnumFacing.DOWN;
				}
			}
			else if(current.getPos().getZ() != connection.getPos().getZ())
			{
				if(current.getPos().getZ() > connection.getPos().getZ())
				{
					return EnumFacing.NORTH;
				}
				else
				{
					return EnumFacing.SOUTH;
				}
			}
			return null;
	}

	protected TileEntityPower getPowerSource()
	{
		if(worldObj.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())) != null && worldObj.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())) != null && worldObj.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ())) != null && worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ())) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())) != null && worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ())) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY()-1, pos.getZ()));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)) != null && worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)) != null && worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1));
			return powerSource;
		}
		return null;
	}
	
	protected TileEntityPower getPowerSourceHorizontal()
	{
		if(worldObj.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())) != null && worldObj.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())) != null && worldObj.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)) != null && worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1));
			return powerSource;
		}
		else if(worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)) != null && worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)) instanceof TileEntityPower)
		{
			TileEntityPower powerSource = (TileEntityPower)worldObj.getTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1));
			return powerSource;
		}
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
	}
	
	
}
