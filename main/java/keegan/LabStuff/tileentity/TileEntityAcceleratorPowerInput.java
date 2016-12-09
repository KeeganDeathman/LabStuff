package keegan.labstuff.tileentity;

import keegan.labstuff.tileentity.*;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAcceleratorPowerInput extends TileEntityPowerConnection
{
	@Override
	public void update()
	{
		TileEntityPowerCable cable = (TileEntityPowerCable) this.getPowerSource();
		if(cable != null)
		{
			TileEntity core = worldObj.getTileEntity(pos.add(6, 0, 0));
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
