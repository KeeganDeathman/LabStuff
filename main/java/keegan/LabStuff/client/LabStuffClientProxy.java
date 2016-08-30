package keegan.labstuff.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.LabStuffCommonProxy;
import keegan.labstuff.render.*;
import keegan.labstuff.tileentity.*;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataCable.class, new TileEntityRenderDataCable());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockDataCable), new ItemRenderDataCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidPipe.class, new TileEntityRenderLiquidPipe());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockLiquidPipe), new ItemRenderLiquidPipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIndustrialMotorShaft.class, new TileEntityRenderIndustrialMotor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolenoidAxel.class, new TileEntityRenderSolenoid());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFusionToroidalMagnet.class, new TileEntityRenderToroid());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockFusionToroidalMagnet), new ItemRenderToroid());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAcceleratorControlPanel.class, new RenderAcceleratorControlPanel());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockAcceleratorControlPanel), new ItemRenderAcceleratorControlPanel());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAcceleratorTube.class, new RenderAcceleratorTube());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockAcceleratorTube), new ItemRenderAcceleratorPipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAcceleratorDetectorCore.class, new RenderAcceleratorDetector());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockDSCRibbonCable), new ItemRendererRibbonCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRibbonCable.class, new RenderRibbonCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDLLaptop.class, new RenderDLLaptop());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockDLLaptop), new ItemRenderDLLaptop());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMatterCollector.class, new RenderMatterCollector());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockMatterCollector), new ItemRenderMatterCollector());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbine.class, new RenderTurbineRotor());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(LabStuffMain.blockTurbineRotor), new ItemRenderTurbineRotor());
	}
	
	@Override
	public void initMod(){
		
	}
}
