package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;

public class TileEntitySolarPanel extends TileEntity
{
	public TileEntitySolarPanel()
	{
		super();
	}
	
	@Override
	public void updateEntity()
	{
		if(worldObj != null)
		{
			TileEntity tileBelo = worldObj.getTileEntity(xCoord,yCoord-1,zCoord);
			if(tileBelo instanceof TileEntityPower)
			{
				TileEntityPower tileBelow = (TileEntityPower)tileBelo;
				if(worldObj.isDaytime() && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord))
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
