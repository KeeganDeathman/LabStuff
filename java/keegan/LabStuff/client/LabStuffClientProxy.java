package keegan.LabStuff.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import keegan.LabStuff.common.LabStuffCommonProxy;
import keegan.LabStuff.render.TileEntityRenderCircuitDesignTable;

public class LabStuffClientProxy extends LabStuffCommonProxy
{
	@Override
	public void registerRenders()
	{
		//TileEntityrenderers
		ClientRegistry.bindTileEntitySpecialRenderer(keegan.LabStuff.tileentity.TileEntityCircuitDesignTable.class, new TileEntityRenderCircuitDesignTable());
	}
	
	@Override
	public void initMod(){
		
	}
}
