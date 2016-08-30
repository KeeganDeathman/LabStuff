package keegan.labstuff.tileentity;

import keegan.labstuff.tileentity.*;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAcceleratorPowerInput extends TileEntityPowerConnection
{
	@Override
	public void updateEntity()
	{
		TileEntityPowerCable cable = (TileEntityPowerCable) this.getPowerSource();
		if(cable != null)
		{
			TileEntity core = worldObj.getTileEntity(xCoord + 6, yCoord, zCoord);
			if(core instanceof TileEntityAcceleratorDetectorCore)
			{
				if(cable.getPower() > 500)
				{
					cable.subtractPower(500, this);
					((TileEntityAcceleratorDetectorCore) core).setPowered(true);
				}
				else
				{
					((TileEntityAcceleratorDetectorCore) core).setPowered(false);
				}
			}
		}
	}
}
