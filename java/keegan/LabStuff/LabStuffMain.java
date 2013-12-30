package keegan.LabStuff;

import keegan.LabStuff.blocks.BlockCircuitDesignTable;
import keegan.LabStuff.blocks.BlockCopperOre;
import keegan.LabStuff.common.LabStuffCommonProxy;
import keegan.LabStuff.common.TabLabStuff;
import keegan.LabStuff.items.ItemCircuitBoardPlate;
import keegan.LabStuff.items.ItemCircuitDesign;
import keegan.LabStuff.items.ItemCopperIngot;
import keegan.LabStuff.items.ItemFiberGlass;
import keegan.LabStuff.tileentity.TileEntityCircuitDesignTable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid= "labstuff", name="LabStuff", version="0.2")
public class LabStuffMain 
{
	@SidedProxy(clientSide = "LabStuff.client.LabStuffClientProxy", serverSide = "LabStuff.common.LabStuffCommonProxy")
	public static LabStuffCommonProxy proxy;
	
	
	@Instance("labstuff")
	public static LabStuffMain instance;
	
	//Blocks
	public static Blocks blockCopperOre;
	public static Block blockCircuitDesignTable;
	public static Block blockCircuitMaker;
			
	//Items
	public static Item itemFiberGlass;
	public static Item itemCopperIngot;
	public static Item itemCircuitBoardPlate;
	public static Item itemBasicCircuitDesign;
	public static Item itemDrilledCircuitBoard;
	public static Item itemEtchedCircuitBoard;
	public static Item itemCircuitBoard;
	
	
	//Other
	public static CreativeTabs tabLabStuff = new TabLabStuff("tabLabStuff");
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		//Blocks
		blockCopperOre = new BlockCopperOre(600, Material.field_151566_D).setHardness(3.0F).setResistance(5.0F);
		blockCircuitDesignTable = new BlockCircuitDesignTable(601, Material.field_151566_D).setUnlocalizedName("blockCircuitDesignTable").setCreativeTab(tabLabStuff);
		//Items
		itemFiberGlass = new ItemFiberGlass(600).setUnlocalizedName("itemFiberGlass").setCreativeTab(tabLabStuff);
		itemCopperIngot = new ItemCopperIngot(601).setUnlocalizedName("itemCopperIngot").setCreativeTab(tabLabStuff);
		itemCircuitBoardPlate = new ItemCircuitBoardPlate(602).setUnlocalizedName("itemCircuitBoardPlate").setCreativeTab(tabLabStuff);
		itemBasicCircuitDesign = new ItemCircuitDesign(603).setUnlocalizedName("itemBasicCircuitDesign").setCreativeTab(tabLabStuff);
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		//Proxy junk
		proxy.registerRenders();
		
		//Blocks
		GameRegistry.registerBlock(blockCopperOre, "blockCopperOre");
		LanguageRegistry.addName(blockCopperOre, "Copper Ore");
		GameRegistry.registerBlock(blockCircuitDesignTable, "blockCircuitDesignTable");
		LanguageRegistry.addName(blockCircuitDesignTable, "Circuit Design Table");
		
		//Items
		GameRegistry.registerItem(itemFiberGlass, "itemFiberGlass");
		GameRegistry.registerItem(itemCopperIngot, "itemCopperIngot");
		GameRegistry.registerItem(itemBasicCircuitDesign, "itemBasicCircuitDesign");
		LanguageRegistry.addName(itemCopperIngot, "Copper Ingot");
		LanguageRegistry.addName(itemFiberGlass, "Fiberglass");
		LanguageRegistry.addName(itemBasicCircuitDesign, "Basic Circuit Design");
		
		//Crafting Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemFiberGlass), new ItemStack(Items.bread), new ItemStack(Block.thinGlass));
		
		//Tile Entities
		GameRegistry.registerTileEntity(TileEntityCircuitDesignTable.class, "TileEntityCircuitDesignTable");
		
		//Other stuff
		//LanguageRegistry.instance().addStringLocalization("itemGroup.TabLabStuff", "en_US", "LabStuff");
	}
	
}
