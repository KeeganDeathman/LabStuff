package keegan.labstuff.client;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.LabStuffCommonProxy;
import keegan.labstuff.render.*;
import keegan.labstuff.tileentity.*;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
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
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockElectrifier), new ItemRenderElectrifier());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlasmaPipe.class, new TileEntityRenderPlasmaPipe());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockPlasmaPipe), new ItemRenderPlasmaPipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityElectronGrabber.class, new TileEntityRenderElectronGrabber());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockElectronGrabber), new ItemRenderElectronGrabber());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerCable.class, new TileEntityRenderPowerCable());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockPowerCable), new ItemRenderPowerCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolarPanel.class, new TileEntityRenderSolarPanel());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockSolarPanel), new ItemRenderSolarPanel());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCzo.class, new TileEntityRenderCzo());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockCzochralskistor), new ItemRenderCzo());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindTurbine.class, new TileEntityRenderWindTurbine());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockWindTurbine), new ItemRenderWindTurbine());
	}
	
	@Override
	public void initMod(){
		
	}
}
