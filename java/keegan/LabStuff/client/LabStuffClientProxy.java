package keegan.labstuff.client;

import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.*;
import keegan.labstuff.render.*;
import keegan.labstuff.tileentity.*;
import cpw.mods.fml.client.registry.ClientRegistry;

public class LabStuffClientProxy extends LabStuffCommonProxy
{
	@Override
	public void registerRenders()
	{
		//TileEntityrenderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCircuitDesignTable.class, new TileEntityRenderCircuitDesignTable());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockCircuitDesignTable), new ItemRenderCircuitDesignTable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityComputer.class, new TileEntityRenderComputer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockComputer), new ItemRenderComputer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectrifier.class, new TileEntityRenderElectrifier());
	}
	
	@Override
	public void initMod(){
		
	}
}
