package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntitySolarPanel extends TileEntity implements ITickable
{
	public TileEntitySolarPanel()
	{
		super();
	}
	
	@Override
	public void update()
	{
		if(worldObj != null)
		{
			TileEntity tileBelo = worldObj.getTileEntity(pos.down());
			if(tileBelo instanceof TileEntityPower)
			{
				TileEntityPower tileBelow = (TileEntityPower)tileBelo;
				if(worldObj.isDaytime() && worldObj.canBlockSeeSky(pos))
				{
					if(worldObj.getWorldInfo().isThundering())
					{
						return;
					}
					else if(worldObj.getWorldInfo().isRaining())
					{
						tileBelow.addPower(50, this);
					}
					else
					{
						tileBelow.addPower(100, this);
					}
				}
				else if(!worldObj.isDaytime())
				{
					if(worldObj.getWorldInfo().isThundering())
					{
						return;
					}
					else if(worldObj.getWorldInfo().isRaining())
					{
						tileBelow.addPower(10, this);
					}
					else
					{
						tileBelow.addPower(20, this);
					}
				}
			}
		}
	}
}
