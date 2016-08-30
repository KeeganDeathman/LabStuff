package keegan.labstuff.recipes;

import keegan.labstuff.tileentity.TileEntityDLLaptop;
import net.minecraft.tileentity.TileEntity;

public class WebDownload extends WebAction 
{
	@Override
	public void performAction(String param, TileEntity tile)
	{
		if(param.contains("dPadOS"));
			dPad(tile);
	}
	
	private void dPad(TileEntity tile)
	{
		if(tile instanceof TileEntityDLLaptop)
		{
			((TileEntityDLLaptop) tile).setUpTablet();
		}
	}
	
	public String getID()
	{
		return "download";
	}
}
