package keegan.labstuff.recipes;

import net.minecraft.tileentity.TileEntity;

public abstract class WebAction 
{
	
	public void performAction(String parameters, TileEntity tile) {}

	public String getID() 
	{
		return "default";
	}
}
