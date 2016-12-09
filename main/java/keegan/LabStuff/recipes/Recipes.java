package keegan.labstuff.recipes;

import static keegan.labstuff.LabStuffMain.*;

import java.util.ArrayList;

import keegan.labstuff.LabStuffMain;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.*;

public class Recipes
{
	//Circuit Design Table
	public static ArrayList<CircuitDesign> cicruitDesigns = new ArrayList<CircuitDesign>();
	//Circuit Maker
	public static ArrayList<CircuitCreation> cicruitCreations = new ArrayList<CircuitCreation>();
	//Charger
	public static ArrayList<Charge> charges = new ArrayList<Charge>();
	public static ArrayList<AcceleratorDiscovery> accelDiscoveries = new ArrayList<AcceleratorDiscovery>();
	public static ArrayList<DiscoveryItem> discoveryItems = new ArrayList<DiscoveryItem>();
	public static ArrayList<WebAction> webActions = new ArrayList<WebAction>();
	public static ArrayList<Enrichment> enrichments = new ArrayList<Enrichment>();

	
	public static void registerShaplessCrafting()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(LabStuffMain.itemFiberGlass), "paneGlass", "paneGlass"));
		//GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemBasicCircuitBoard), new ItemStack(LabStuffMain.itemBasicCircuitDesign), "ingotCopper", "ingotCopper", "ingotCopper", "ingotCopper", new ItemStack(LabStuffMain.itemCircuitBoardPlate));
		//GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemComputerCircuitBoard), new ItemStack(LabStuffMain.itemComputerCircuitDesign), "ingotCopper", "ingotCopper", "ingotCopper", "ingotCopper", new ItemStack(LabStuffMain.itemCircuitBoardPlate));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemSteel), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.COAL));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(LabStuffMain.itemBattery), "ingotZinc", "ingotZinc", "ingotManganese", "ingotManganese", "ingotCopper", "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(LabStuffMain.blockRFToLVConverter), new ItemStack(LabStuffMain.blockLVToRFConverter)));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemSiliconCrystalSeed), new ItemStack(LabStuffMain.itemSiliconIngot));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemRodMountedSiliconSeed), new ItemStack(LabStuffMain.itemSteelRod), new ItemStack(LabStuffMain.itemSiliconCrystalSeed));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemSiliconWafer, 64), new ItemStack(LabStuffMain.itemSiliconBoule), new ItemStack(LabStuffMain.itemSaw));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.blockFusionPlasmaTap), new ItemStack(LabStuffMain.blockPlasmaPipe));
		GameRegistry.addShapelessRecipe(new ItemStack(itemTouchScreen), new ItemStack(Blocks.GLASS_PANE), new ItemStack(itemTouchMesh));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(blockTurbineGlass), "blockGlass", new ItemStack(blockTurbineCasing), new ItemStack(blockTurbineCasing)));
		GameRegistry.addShapelessRecipe(new ItemStack(blockTurbineValve), new ItemStack(blockPowerCable), new ItemStack(blockTurbineCasing), new ItemStack(blockLiquidPipe));
		GameRegistry.addShapelessRecipe(new ItemStack(blockTurbineVent), new ItemStack(Blocks.IRON_BARS), new ItemStack(blockTurbineCasing));
	}
	
	public static void registerShapedCrafting()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemCircuitBoardPlate), "x", "y", "x", 'x', new ItemStack(LabStuffMain.itemFiberGlass), 'y', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockComputer), " x "," yi", 'x', new ItemStack(LabStuffMain.itemMonitor), 'y', new ItemStack(LabStuffMain.itemComputerTower), 'i', new ItemStack(LabStuffMain.itemKeyboard)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemMonitor), "iii","ibg","ici",'i',new ItemStack(Items.IRON_INGOT), 'b', "circuitBasic", 'g', new ItemStack(Blocks.GLASS_PANE), 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemComputerTower), "iii","ibi","iii",'i',new ItemStack(Items.IRON_INGOT), 'b', "circuitComputer"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemKeyboard), "ppp","cbp","ppp", 'p', new ItemStack(LabStuffMain.itemPlastic), 'b', "circuitBasic", 'c', new ItemStack(LabStuffMain.itemCopperIngot)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemBattery), "pzp", "pmp", "pzp", 'p', new ItemStack(Items.PAPER), 'z', "ingotZinc", 'm', "ingotMaganese"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockCircuitDesignTable), "sss","w w", "w w", 's', "ingotSteel", 'w', new ItemStack(Items.STICK)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockCircuitMaker), "sss", "isi", "sss", 's', "ingotSteel", 'i', "ingotIron"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockElectrifier), "gcg", "gcg", "ccc", 'c', "ingotCopper", 'g', "blockGlass"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockPlasmaPipe), "rgr", "rgr", "rgr", 'r', "dustRedstone", 'g', "paneGlass"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockGasChamberPort), "s s", "sps", "sps", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPlasmaPipe)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockGasChamberWall), "sp ", "sp ", "sp ", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPlasmaPipe)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockElectronGrabber), "spr", "spr", "spr", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPlasmaPipe), 'r', "dustRedstone"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemTestTube), "gwg", "g g", "ggg", 'g', "paneGlass", 'w', "plankWood"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockPowerFurnace), "scs",  "sfs", "scs", 's', "ingotSteel", 'c', "ingotCopper", 'f', new ItemStack(Blocks.FURNACE)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockPowerCable), "www", "ccc", "www", 'w', new ItemStack(Blocks.WOOL), 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockLVToRFConverter), "sss", "psr", "sss", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPowerCable), 'r', new ItemStack(Items.REDSTONE)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemSteelRod), " s ", " s ", "   ", 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockCzochralskistor), "qbq", "qcq", "qtq", 'q', new ItemStack(Items.QUARTZ), 'b', "circuitIntermediate", 't', new ItemStack(LabStuffMain.itemTestTube), 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemSaw), " ss", "sii", "   ", 's', new ItemStack(Items.STICK), 'i', new ItemStack(Items.IRON_INGOT)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemSolarCell, 3), "ggg", "ccc", "sss", 'g', "paneGlass", 'c', new ItemStack(LabStuffMain.itemSiliconWafer), 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockSolarPanel), "ccc", " w ", "sss", 'c', new ItemStack(LabStuffMain.itemSolarCell), 'w', "ingotCopper", 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockWindTurbine), "sps", "sis", "scs", 's', "ingotSteel", 'p', new ItemStack(Blocks.PISTON), 'i', new ItemStack(Items.IRON_INGOT), 'c', "circuitBasic"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemElectromagnet), "ccc", "cic", "ccc", 'c', "ingotCopper", 'i', new ItemStack(Items.IRON_INGOT)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockDataCable), "dpd","pdp","dpd", 'd', new ItemStack(LabStuffMain.blockPowerCable), 'p', new ItemStack(LabStuffMain.itemPlastic)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockIndustrialMotor), "eee", "ecc", "eee", 'e', new ItemStack(LabStuffMain.itemElectromagnet), 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockIndustrialMotorShaft), "iri", "iri", "iri", 'i', "ingotIron", 'r', new ItemStack(LabStuffMain.itemSteelRod)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockIndustrialMotorContact), "msm", "mcm", "mpm", 's', new ItemStack(LabStuffMain.blockIndustrialMotorShaft), 'm', new ItemStack(LabStuffMain.blockIndustrialMotor), 'p', new ItemStack(LabStuffMain.blockPowerCable), 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockFusionSolenoid), "ces", "ces", "ces", 'e', new ItemStack(LabStuffMain.itemElectromagnet), 'c', "ingotCopper", 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockFusionSolenoidArm), "sss", 's', new ItemStack(LabStuffMain.blockFusionSolenoid)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockFusionSolenoidAxel), "mam", "ara", "mam", 'm', new ItemStack(LabStuffMain.blockFusionSolenoid), 'a', new ItemStack(LabStuffMain.blockFusionSolenoidArm), 'r', new ItemStack(LabStuffMain.itemSteelRod)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockFusionHeatExchange), "sss", "lcl", "sss", 'l', new ItemStack(LabStuffMain.blockLiquidPipe), 'c', "ingotCopper", 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemWrench), "c c", " c ", " c ", 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockCharger), "cpc", "p p", "pcp", 'c', "ingotCopper", 'p', new ItemStack(LabStuffMain.blockPowerCable)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockLiquidPipe), "sgs", "sbs", "sgs", 's', "ingotSteel", 'g', "blockGlass", 'b', new ItemStack(Items.BUCKET)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockFusionToroidalMagnet), "ses", "e e", "ses", 's', "ingotSteel", 'e', new ItemStack(LabStuffMain.itemElectromagnet)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockBattery), "ses", "srs", "sss", 's', "ingotSteel", 'e', new ItemStack(LabStuffMain.blockPowerCable), 'r', new ItemStack(LabStuffMain.itemBattery)));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorHadronCalorimeter), "iei", "ihi", "iei", 'i', new ItemStack(Items.IRON_INGOT), 'e', new ItemStack(LabStuffMain.itemElectromagnet), 'h', new ItemStack(itemGluonDetector));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorElectromagneticCalorimeter), "iei", "i i", "iei", 'i', new ItemStack(Items.IRON_INGOT), 'e', new ItemStack(LabStuffMain.itemElectromagnet));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorSolenoid), "iei", "iei", "iei", 'i', new ItemStack(Items.IRON_INGOT), 'e', new ItemStack(LabStuffMain.itemElectromagnet));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorMuonDetector), "iie", "iie", "iie", 'i', new ItemStack(Items.IRON_INGOT), 'e', new ItemStack(LabStuffMain.itemElectromagnet));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorTrackingDetector), "ies", "ies", "ies", 'i', new ItemStack(Items.IRON_INGOT), 'e', new ItemStack(LabStuffMain.itemElectromagnet), 's', new ItemStack(LabStuffMain.itemSiliconWafer));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorDetectorCore), "ici", "cpc", "ici", 'i', new ItemStack(Items.IRON_INGOT), 'c', new ItemStack(LabStuffMain.itemInterCircuitBoard), 'p', new ItemStack(blockAcceleratorTube));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorTube), "iii", "eee", "iii", 'i', new ItemStack(Items.IRON_INGOT), 'e', new ItemStack(LabStuffMain.itemElectromagnet));
		GameRegistry.addRecipe(new ItemStack(itemGluonDetector), " e ", "e e", " e ", 'e', new ItemStack(LabStuffMain.itemElectromagnet));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorInterface), "iii", "tht", "iii", 'i', new ItemStack(Items.IRON_INGOT), 't', new ItemStack(blockAcceleratorTube), 'h', new ItemStack(Blocks.HOPPER));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorPowerInput), "iii", "tpt", "iii", 'i', new ItemStack(Items.IRON_INGOT), 't', new ItemStack(blockAcceleratorTube), 'p', new ItemStack(LabStuffMain.blockPowerCable));
		GameRegistry.addRecipe(new ItemStack(blockAcceleratorControlPanel), "lbb", "ici", "iii", 'i', new ItemStack(Items.IRON_INGOT), 'l', new ItemStack(Blocks.REDSTONE_LAMP), 'b', new ItemStack(Blocks.STONE_BUTTON), 'c', new ItemStack(LabStuffMain.itemComputerCircuitBoard));
		GameRegistry.addRecipe(new ItemStack(blockDSCRibbonCable), "ccc", "ccc","ccc", 'c', new ItemStack(LabStuffMain.blockDataCable));
		GameRegistry.addRecipe(new ItemStack(itemTouchMesh), "rsr","srs","rsr",'r',new ItemStack(Items.REDSTONE),'s',new ItemStack(Items.STRING));
		GameRegistry.addRecipe(new ItemStack(itemUnProgrammedDPad),"pp ","pct","pp ",'t',new ItemStack(itemTouchScreen),'p',new ItemStack(LabStuffMain.itemPlastic),'c', new ItemStack(LabStuffMain.itemComputerCircuitBoard));
		GameRegistry.addRecipe(new ItemStack(blockDLLaptop), "imt", "ick", "iii", 'i', new ItemStack(Items.IRON_INGOT), 't', new ItemStack(itemTouchScreen), 'm', new ItemStack(LabStuffMain.itemMonitor), 'c', new ItemStack(LabStuffMain.itemComputerTower), 'k', new ItemStack(LabStuffMain.itemKeyboard));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(get(LabStuffMain.blockEnricher)), "rcr", "isi", "rpr", 'r', new ItemStack(Items.REDSTONE), 'c', "circuitBasic", 'i', "ingotIron", 's', "blockSteel", 'p', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(get(LabStuffMain.blockSteel)), "sss", "sss", "sss", 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(get(LabStuffMain.blockTurbineCasing)), "   ", "sss", "   ", 's', "blockSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(get(LabStuffMain.blockTurbineRotor)), "sss", "sss", "   ", 's', new ItemStack(LabStuffMain.itemSteelRod)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemTurbineBlades), " s ", "srs", " s ", 'r', new ItemStack(LabStuffMain.itemSteelRod), 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(get(LabStuffMain.blockElectromagneticCoil)), "scs", "cec", "scs", 'e', new ItemStack(LabStuffMain.itemElectromagnet) , 's', "ingotSteel", 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(get(blockVent)), "iii", "php","   ", 'i', new ItemStack(get(Blocks.IRON_BARS)), 'p', new ItemStack(get(blockLiquidPipe)), 'h', new ItemStack(get(Blocks.HOPPER))));
	}
	
	public static void performAction(String action, TileEntity tile) {
		for(WebAction webAction : webActions)
		{
			if(action.startsWith(webAction.getID()))
				webAction.performAction(action.substring(action.indexOf(webAction.getID())), tile);
		}
	}
	
	public static void addAction(WebAction action)
	{
		webActions.add(action);
	}
	
	public static void registerSmelting()
	{
		GameRegistry.addSmelting(LabStuffMain.blockCopperOre, new ItemStack(LabStuffMain.itemCopperIngot, 1), 3);
		GameRegistry.addSmelting(LabStuffMain.itemRubber, new ItemStack(LabStuffMain.itemPlastic, 1), 3);
		GameRegistry.addSmelting(get(LabStuffMain.blockZincOre), new ItemStack(LabStuffMain.itemZinc, 1), 3);
		GameRegistry.addSmelting(get(LabStuffMain.blockMangOre), new ItemStack(LabStuffMain.itemManganese, 1), 3);
		GameRegistry.addSmelting(get(LabStuffMain.blockSiliconOre), new ItemStack(LabStuffMain.itemSiliconIngot), 6);
		GameRegistry.addSmelting(LabStuffMain.itemZincDust, new ItemStack(LabStuffMain.itemZinc), 3);
		GameRegistry.addSmelting(LabStuffMain.itemMangDust, new ItemStack(LabStuffMain.itemManganese), 3);
		GameRegistry.addSmelting(LabStuffMain.itemCopperDust, new ItemStack(LabStuffMain.itemCopperIngot), 3);
		GameRegistry.addSmelting(LabStuffMain.itemIronDust, new ItemStack(Items.IRON_INGOT), 3);
		GameRegistry.addSmelting(LabStuffMain.itemGoldDust, new ItemStack(Items.GOLD_INGOT), 3);
	}
	
	public static void addCircuitDesign(String name, Item output)
	{
		CircuitDesign design = new CircuitDesign(name, output);
		cicruitDesigns.add(design);
	}
	
	public static ArrayList<CircuitDesign> getCircuitDeisgns()
	{
		return cicruitDesigns;
	}
	
	public static void addCircuitCreation(String name, Item etched, Item drilled, Item output)
	{
		CircuitCreation creation = new CircuitCreation(name, drilled, etched, output);
		cicruitCreations.add(creation);
	}
	
	public static ArrayList<CircuitCreation> getCircuitCreations()
	{
		return cicruitCreations;
	}
	
	public static void addCharge(Item dead, Item charged, int power)
	{
		Charge charge = new Charge(dead, charged, power);
		charges.add(charge);
	}
	
	public static ArrayList<Charge> getCharges()
	{
		return charges;
	}
	
	public static void addEnrichment(Item in, Item out)
	{
		Enrichment enrichment = new Enrichment(in, out);
		enrichments.add(enrichment);
	}
	
	public static ArrayList<Enrichment> getEnrichments()
	{
		return enrichments;
	}

	
	public static void addAccelDiscovery(AcceleratorDiscovery dependency, ItemStack flash)
	{
		AcceleratorDiscovery discovery = new AcceleratorDiscovery(dependency, flash, accelDiscoveries.size());
		accelDiscoveries.add(discovery);
	}
	
	public static void addDiscovItem(ItemStack r, ItemStack i1, ItemStack i2, ItemStack i3, AcceleratorDiscovery d, String name)
	{
		DiscoveryItem item = new DiscoveryItem(r,i1,i2,i3,d, name, discoveryItems.size());
		discoveryItems.add(item);
	}

	public static AcceleratorDiscovery getDiscovFromDrive(Item flash)
	{
		for(AcceleratorDiscovery discov : accelDiscoveries)
		{
			if(discov.getDiscoveryFlashDrive().isItemEqual(new ItemStack(flash)))
				return discov;
		}
		return null;
	}

	public static Item getDesignFromName(String designName) {
		for(CircuitDesign design : cicruitDesigns)
		{
			if(design.getName().equals(designName))
				return design.getDesignSheet();
		}
		return null;
	}
	
}
