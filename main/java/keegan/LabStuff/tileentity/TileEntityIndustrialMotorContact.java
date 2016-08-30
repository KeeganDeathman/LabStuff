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
			if(getPowerSource().subtractPower(9090, this))
				isPowered = true;
			else
				isPowered = false;
		}
		else
			isPowered=false;
	}
	
}
