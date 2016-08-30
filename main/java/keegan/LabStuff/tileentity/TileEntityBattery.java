package keegan.labstuff.tileentity;

import keegan.labstuff.tileentity.TileEntityPowerConnection;
import net.minecraft.tileentity.TileEntity;

public class TileEntityBattery extends TileEntityPowerConnection
{
	private int power = 0;
	
	@Override
	public void updateEntity()
	{
		if(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) && getPowerSource() != null)
		{
			if(getPowerSource().subtractPower(500, this))
				power += 500;
		}
		else if(power >= 500 && getPowerSource() != null)
		{
			power -= 500;
			getPowerSource().addPower(500, this);
		}
	}

	public int getPower() {
		return power;
	}
}
