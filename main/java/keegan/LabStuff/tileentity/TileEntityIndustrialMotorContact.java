package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntityIndustrialMotorContact extends TileEntityPowerConnection
{

	public boolean isPowered;
	
	@Override
	public void updateEntity()
	{
		if(getPowerSource()!=null)
		{
			if(getPowerSource().subtractPower(250, this))
				isPowered = true;
			else
				isPowered = false;
		}
	}
	
}
