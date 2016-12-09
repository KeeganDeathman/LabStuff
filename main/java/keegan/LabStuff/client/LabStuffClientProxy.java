package keegan.labstuff.client;

import java.util.ArrayList;

import keegan.labstuff.common.LabStuffCommonProxy;
import keegan.labstuff.render.*;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.world.ColorHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LabStuffClientProxy extends LabStuffCommonProxy
{
	@Override
	public void registerRenders()
	{
		//TileEntityrenderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPlasmaPipe.class, new TileEntityRenderPlasmaPipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPowerCable.class, new TileEntityRenderPowerCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindTurbine.class, new TileEntityRenderWindTurbine());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDataCable.class, new TileEntityRenderDataCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLiquidPipe.class, new TileEntityRenderLiquidPipe());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolenoidAxel.class, new RenderSolenoid());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToroid.class, new RenderToroid());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAcceleratorTube.class, new RenderAcceleratorTube());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAcceleratorDetectorCore.class, new RenderAcceleratorDetector());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRibbonCable.class, new RenderRibbonCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbine.class, new RenderTurbineRotor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMatterCollector.class, new RenderMatterCollector());
	}
	
	@Override
	public void preInit()
	{
		OBJLoader.INSTANCE.addDomain("labstuff");	
	}
	
	@Override
	public void registerItemModel(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@Override
	public void initMod()
	{
		ColorHandler.init();
	}
}
