package keegan.labstuff.recipes;

import java.util.ArrayList;

import keegan.labstuff.LabStuffMain;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class Recipes
{
	//Circuit Design Table
	public static ArrayList<CircuitDesign> cicruitDesigns = new ArrayList<CircuitDesign>();
	//Circuit Maker
	public static ArrayList<CircuitCreation> cicruitCreations = new ArrayList<CircuitCreation>();

	
	public static void registerShaplessCrafting()
	{
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemFiberGlass), new ItemStack(Items.bread), new ItemStack(Blocks.glass_pane));
		//GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemBasicCircuitBoard), new ItemStack(LabStuffMain.itemBasicCircuitDesign), "ingotCopper", "ingotCopper", "ingotCopper", "ingotCopper", new ItemStack(LabStuffMain.itemCircuitBoardPlate));
		//GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemComputerCircuitBoard), new ItemStack(LabStuffMain.itemComputerCircuitDesign), "ingotCopper", "ingotCopper", "ingotCopper", "ingotCopper", new ItemStack(LabStuffMain.itemCircuitBoardPlate));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemSteel), new ItemStack(Items.iron_ingot), new ItemStack(Items.coal));
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(LabStuffMain.itemBattery), "ingotZinc", "ingotZinc", "ingotManganese", "ingotManganese", "ingotCopper", "ingotCopper"));
		if(Loader.isModLoaded("ThermalFoundation"))
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(LabStuffMain.blockRFToLVConverter), new ItemStack(LabStuffMain.blockLVToRFConverter)));
		}
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemSiliconCrystalSeed), new ItemStack(LabStuffMain.itemSiliconIngot));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemRodMountedSiliconSeed), new ItemStack(LabStuffMain.itemSteelRod), new ItemStack(LabStuffMain.itemSiliconCrystalSeed));
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemSiliconWafer, 64), new ItemStack(LabStuffMain.itemSiliconBoule), new ItemStack(LabStuffMain.itemSaw));
	}
	
	public static void registerShapedCrafting()
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemCircuitBoardPlate), "x", "y", "x", 'x', new ItemStack(LabStuffMain.itemFiberGlass), 'y', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockComputer), " x "," yi", 'x', new ItemStack(LabStuffMain.itemMonitor), 'y', new ItemStack(LabStuffMain.itemComputerTower), 'i', new ItemStack(LabStuffMain.itemKeyboard)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemMonitor), "iii","ibg","ici",'i',new ItemStack(Items.iron_ingot), 'b', "circuitBasic", 'g', new ItemStack(Blocks.glass_pane), 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemComputerTower), "iii","ibi","iii",'i',new ItemStack(Items.iron_ingot), 'b', "circuitComputer"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemKeyboard), "ppp","cbp","ppp", 'p', new ItemStack(LabStuffMain.itemPlastic), 'b', "circuitBasic", 'c', new ItemStack(LabStuffMain.itemCopperIngot)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemBattery), "pzp", "pmp", "pzp", 'p', new ItemStack(Items.paper), 'z', "ingotZinc", 'm', "ingotMaganese"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockCircuitDesignTable), "sss","w w", "w w", 's', "ingotSteel", 'w', new ItemStack(Items.stick)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockCircuitMaker), "sss", "isi", "sss", 's', "ingotSteel", 'i', "ingotIron"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockElectrifier), "gcg", "gcg", "ccc", 'c', "ingotCopper", 'g', "blockGlass"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockPlasmaPipe), "rgr", "rgr", "rgr", 'r', "dustRedstone", 'g', "paneGlass"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockGasChamberPort), "s s", "sps", "sps", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPlasmaPipe)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockGasChamberWall), "sp ", "sp ", "sp ", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPlasmaPipe)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockElectronGrabber), "spr", "spr", "spr", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPlasmaPipe), 'r', "dustRedstone"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemTestTube), "gwg", "g g", "ggg", 'g', "paneGlass", 'w', "plankWood"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockPowerFurnace), "scs",  "sfs", "scs", 's', "ingotSteel", 'c', "ingotCopper", 'f', new ItemStack(Blocks.furnace)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockPowerCable), "www", "ccc", "www", 'w', new ItemStack(Blocks.wool), 'c', "ingotCopper"));
		if(Loader.isModLoaded("ThermalFoundation"))
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockLVToRFConverter), "sss", "psr", "sss", 's', "ingotSteel", 'p', new ItemStack(LabStuffMain.blockPowerCable), 'r', new ItemStack(Items.redstone)));
		}
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemSteelRod), " s ", " s ", "   ", 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockCzochralskistor), "qbq", "qcq", "qtq", 'q', new ItemStack(Items.quartz), 'b', "circuitIntermediate", 't', new ItemStack(LabStuffMain.itemTestTube), 'c', "ingotCopper"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemSaw), " ss", "sii", "   ", 's', new ItemStack(Items.stick), 'i', new ItemStack(Items.iron_ingot)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemSolarCell, 3), "ggg", "ccc", "sss", 'g', "paneGlass", 'c', new ItemStack(LabStuffMain.itemSiliconWafer), 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockSolarPanel), "ccc", " w ", "sss", 'c', new ItemStack(LabStuffMain.itemSolarCell), 'w', "ingotCopper", 's', "ingotSteel"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockWindTurbine), "sps", "sis", "scs", 's', "ingotSteel", 'p', new ItemStack(Blocks.piston), 'i', new ItemStack(Items.iron_ingot), 'c', "circuitBasic"));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.itemElectromagnet), "ccc", "cic", "ccc", 'c', "ingotCopper", 'i', new ItemStack(Items.iron_ingot)));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(LabStuffMain.blockDataCable), "dpd","pdp","dpd", 'd', new ItemStack(LabStuffMain.blockDataCable), 'p', new ItemStack(LabStuffMain.itemPlastic)));
	}
	
	public static void registerSmelting()
	{
		GameRegistry.addSmelting(LabStuffMain.blockCopperOre, new ItemStack(LabStuffMain.itemCopperIngot, 2), 3);
		GameRegistry.addSmelting(LabStuffMain.blockPlasticOre, new ItemStack(LabStuffMain.itemPlastic, 2), 3);
		GameRegistry.addSmelting(LabStuffMain.blockZincOre, new ItemStack(LabStuffMain.itemZinc, 2), 3);
		GameRegistry.addSmelting(LabStuffMain.blockMangOre, new ItemStack(LabStuffMain.itemManganese, 2), 3);
		GameRegistry.addSmelting(LabStuffMain.blockSiliconOre, new ItemStack(LabStuffMain.itemSiliconIngot), 6);
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
	
}
