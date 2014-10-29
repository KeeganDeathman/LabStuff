package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlasmaTank extends TileEntityPlasma 
{
	
	private int plasmaMaxInt = 500;
	
	@Override
	public void addPlasma(int addition, TileEntity issuer)
	{
		if(issuer instanceof TileEntityPlasma)
		{
			if(plasmaInt + addition == plasmaMaxInt)
				addition -= ((plasmaInt + addition) - plasmaMaxInt);
			plasmaInt += addition;
			((TileEntityPlasma) issuer).subtractPlasma(addition, this);
		}
	}
	
	@Override
	public void subtractPlasma(int subtraction, TileEntity issuer)
	{
		return;
	}
	
	public int getPlasma()
	{
		return plasmaInt;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("plasma", plasmaInt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		plasmaInt = tag.getInteger("plasma");
	}
	
	
}
