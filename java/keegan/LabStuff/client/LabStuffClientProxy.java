package keegan.labstuff.client;

import keegan.labstuff.common.*;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.render.*;
import cpw.mods.fml.client.registry.ClientRegistry;

public class LabStuffClientProxy extends LabStuffCommonProxy
{
	@Override
	public void registerRenders()
	{
		//TileEntityrenderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCircuitDesignTable.class, new TileEntityRenderCircuitDesignTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityComputer.class, new TileEntityRenderComputer());
	}
	
	@Override
	public void initMod(){
		
	}
}
