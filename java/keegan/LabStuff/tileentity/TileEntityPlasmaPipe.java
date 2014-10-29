package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;


public class TileEntityPlasmaPipe extends TileEntityPlasma	
{
	private int plasma;
	
	public TileEntityPlasmaPipe()
	{
		plasmaInt = this.getPlasma();
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("plasma", this.plasmaInt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		plasmaInt = tag.getInteger("plasma");
	}
	
	@Override
	public void equalize()
	{
		super.equalize();
	}
}
