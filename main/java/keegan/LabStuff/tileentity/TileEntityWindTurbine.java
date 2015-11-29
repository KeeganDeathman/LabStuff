package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityWindTurbine extends TileEntityPowerConnection
{
	
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		tagCompound.setInteger("blockMeta", blockMetadata);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, tag.getInteger("blockMeta"), 2);
	}
	
	public void updateEntity()
	{
		TileEntityPower powerTap = getPowerSource();
		if(powerTap != null)
		{
			if(isProperPosition())
			{
				int powerOutput;
				if(yCoord > 150)
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
		if (yCoord > 64 && worldObj.canBlockSeeTheSky(xCoord, yCoord, zCoord))
			return true;
		return false;
	}
	
}
