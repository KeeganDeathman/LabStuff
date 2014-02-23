package keegan.labstuff.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityComputer extends TileEntity 
{

	public String ConsoleLogLine1;
	public String ConsoleLogLine2;
	public String ConsoleLogLine3;
	public String ConsoleLogLine4;
	public String ConsoleLogLine5;
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setString(ConsoleLogLine1, "ConsoleLogLine1");
		tag.setString(ConsoleLogLine2, "ConsoleLogLine2");
		tag.setString(ConsoleLogLine3, "ConsoleLogLine3");
		tag.setString(ConsoleLogLine4, "ConsoleLogLine4");
		tag.setString(ConsoleLogLine5, "ConsoleLogLine5");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		tag.getString("ConsoleLogLine1");
		tag.getString("ConsoleLogLine2");
		tag.getString("ConsoleLogLine3");
		tag.getString("ConsoleLogLine4");
		tag.getString("ConsoleLogLine5");
	}

}
