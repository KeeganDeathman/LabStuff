package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySolenoidAxel extends TileEntityRotary
{
	
	public TileEntitySolenoidAxel()
	{
		setDirIn(ForgeDirection.DOWN);
		setDirOut(ForgeDirection.UP);
	}
	
}
