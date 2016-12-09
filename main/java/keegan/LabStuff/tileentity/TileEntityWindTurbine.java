package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWindTurbine extends TileEntityPowerConnection
{
	
	
	public void update()
	{
		TileEntityPower powerTap = getPowerSource();
		if(powerTap != null)
		{
			if(isProperPosition())
			{
				int powerOutput;
				if(pos.getY() > 150)
				{
					powerOutput = 200;
				}
				else
				{
					powerOutput = 150;
				}
				if(worldObj.getWorldInfo().isRaining())
				{
					powerOutput += 50;
				}
				else if(worldObj.getWorldInfo().isThundering())
				{
					powerOutput += 100;
				}
				powerTap.addPower(powerOutput, this);
				
			}
		}
	}
	
	public boolean isProperPosition()
	{
		if (pos.getY() > 64 && worldObj.canBlockSeeSky(pos))
			return true;
		return false;
	}
	
}
