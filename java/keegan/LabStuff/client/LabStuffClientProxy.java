package LabStuff.client;

import codelyoko.Client.TileEntityRenderTowerFloor;
import cpw.mods.fml.client.registry.ClientRegistry;
import LabStuff.common.LabStuffCommonProxy;
import LabStuff.render.TileEntityRenderCircuitDesignTable;

public class LabStuffClientProxy extends LabStuffCommonProxy
{
	@Override
	public void registerRenders()
	{
		//TileEntityrenderers
		ClientRegistry.bindTileEntitySpecialRenderer(LabStuff.tileentity.TileEntityCircuitDesignTable.class, new TileEntityRenderCircuitDesignTable());
	}
	
	@Override
	public void initMod(){
		
	}
}
