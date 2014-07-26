package keegan.labstuff;


import java.io.File;

import keegan.labstuff.PacketHandling.PacketCircuitDesignTable;
import keegan.labstuff.PacketHandling.PacketComputer;
import keegan.labstuff.PacketHandling.PacketElectrifier;
import keegan.labstuff.PacketHandling.PacketPipeline;
import keegan.labstuff.blocks.BlockCircuitDesignTable;
import keegan.labstuff.blocks.BlockCircuitMaker;
import keegan.labstuff.blocks.BlockComputer;
import keegan.labstuff.blocks.BlockCopperOre;
import keegan.labstuff.blocks.BlockElectrifier;
import keegan.labstuff.blocks.BlockLabOre;
import keegan.labstuff.blocks.BlockPlasticOre;
import keegan.labstuff.blocks.BlockWorkbench;
import keegan.labstuff.client.GuiHandler;
import keegan.labstuff.common.LabStuffCommonProxy;
import keegan.labstuff.common.TabLabStuff;
import keegan.labstuff.items.ItemBattery;
import keegan.labstuff.items.ItemCircuitBoard;
import keegan.labstuff.items.ItemCircuitBoardPlate;
import keegan.labstuff.items.ItemCircuitDesign;
import keegan.labstuff.items.ItemComputerPart;
import keegan.labstuff.items.ItemCopperIngot;
import keegan.labstuff.items.ItemFiberGlass;
import keegan.labstuff.items.ItemLabIngot;
import keegan.labstuff.items.ItemPartialCircuitBoard;
import keegan.labstuff.items.ItemPlastic;
import keegan.labstuff.items.ItemTestTube;
import keegan.labstuff.tileentity.TileEntityCircuitDesignTable;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;
import keegan.labstuff.tileentity.TileEntityComputer;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import keegan.labstuff.world.LabStuffOreGen;
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
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;

@Mod(modid= "labstuff", name="LabStuff", version="1.0")
public class LabStuffMain 
{
	@SidedProxy(clientSide = "keegan.labstuff.client.LabStuffClientProxy", serverSide = "keegan.labstuff.common.LabStuffCommonProxy")
	public static LabStuffCommonProxy proxy;
	
	@Instance("labstuff")
	public static LabStuffMain instance;
	
	//Blocks
	public static Block blockCopperOre;
	public static Block blockPlasticOre;
	public static Block blockCircuitDesignTable;
	public static Block blockCircuitMaker;
	public static Block blockComputer;
	public static Block blockWorkbench;
	public static Block blockElectrifier;
	public static Block blockZincOre;
	public static Block blockMangOre;
			
	//Items
	public static Item itemFiberGlass;
	public static Item itemCopperIngot;
	public static Item itemCircuitBoardPlate;
	public static Item itemBasicCircuitDesign;
	public static Item itemComputerCircuitDesign;
	public static Item itemBasicDrilledCircuitBoard;
	public static Item itemBasicEtchedCircuitBoard;
	public static Item itemBasicCircuitBoard;
	public static Item itemComputerCircuitBoard;
	public static Item itemMonitor;
	public static Item itemComputerTower;
	public static Item itemKeyboard;
	public static Item itemPlastic;
	public static Item itemTestTube;
	public static Item itemOxygenTestTube;
	public static Item itemHydrogenTestTube;
	public static Item itemManganese;
	public static Item itemZinc;
	public static Item itemBattery;
	public static Item itemDeadBattery;
	
	//Other
	public static CreativeTabs tabLabStuff = new TabLabStuff("tabLabStuff");

	public static final File minecraftDir = (File) FMLInjectionData.data()[6];
    public static final File filesDir = new File(minecraftDir, "labstuff");
    static {filesDir.mkdirs();}
    
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		System.out.println("Were doing stuff");
		//Blocks
		blockCopperOre = new BlockCopperOre(Material.rock).setHardness(10F).setResistance(20F).setStepSound(Block.soundTypeStone).setBlockName("blockCopperOre").setBlockTextureName("labstuff:blockCopperOre").setCreativeTab(tabLabStuff);
		blockPlasticOre = new BlockPlasticOre(Material.rock).setHardness(10F).setResistance(20F).setStepSound(Block.soundTypeStone).setBlockName("blockPlasticOre").setBlockTextureName("labstuff:blockPlasticOre").setCreativeTab(tabLabStuff);
		blockCircuitDesignTable = new BlockCircuitDesignTable(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockCircuitDesignTable");
		blockCircuitMaker = new BlockCircuitMaker(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockCircuitMaker").setBlockTextureName("labstuff:blockCircuitMaker");
		blockWorkbench = new BlockWorkbench(Material.iron).setBlockName("blockWorkbench").setBlockTextureName("labstuff:blockWorkbenchSide").setCreativeTab(tabLabStuff);
		blockComputer = new BlockComputer(Material.iron).setBlockName("blockComputer").setCreativeTab(tabLabStuff);
		blockZincOre = new BlockLabOre().setBlockName("blockZincOre").setBlockTextureName("labstuff:blockZincOre").setCreativeTab(tabLabStuff).setHardness(10F).setResistance(20F);
		blockMangOre = new BlockLabOre().setBlockName("blockMangOre").setBlockTextureName("labstuff:blockMangOre").setCreativeTab(tabLabStuff).setHardness(10F).setResistance(20F);
		blockElectrifier = new BlockElectrifier(Material.iron).setBlockName("blockElectrifier").setCreativeTab(tabLabStuff);
		//Items
		itemFiberGlass = new ItemFiberGlass(600).setUnlocalizedName("itemFiberGlass").setCreativeTab(tabLabStuff);
		itemCopperIngot = new ItemCopperIngot(601).setUnlocalizedName("itemCopperIngot").setCreativeTab(tabLabStuff);
		itemCircuitBoardPlate = new ItemCircuitBoardPlate(602).setUnlocalizedName("itemCircuitBoardPlate").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemCircuitBoardPlate");
		itemBasicDrilledCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemBasicDrilledCircuitBoard").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemBasicEtchedCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemEtchedCircuitBoard").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemBasicCircuitBoard = new ItemCircuitBoard().setUnlocalizedName("itemBasicCircuitboard").setTextureName("labstuff:itemCircuitBoard").setCreativeTab(tabLabStuff);
		itemComputerCircuitBoard = new ItemCircuitBoard().setUnlocalizedName("itemComputerCircuitboard").setTextureName("labstuff:itemCircuitBoard").setCreativeTab(tabLabStuff);
		itemBasicCircuitDesign = new ItemCircuitDesign(603).setUnlocalizedName("itemBasicCircuitDesign").setCreativeTab(tabLabStuff);
		itemComputerCircuitDesign = new ItemCircuitDesign(603).setUnlocalizedName("itemComputerCircuitDesign").setCreativeTab(tabLabStuff);
		itemMonitor = new ItemComputerPart().setUnlocalizedName("itemMonitor").setTextureName("labstuff:itemMonitor").setCreativeTab(tabLabStuff);
		itemComputerTower = new ItemComputerPart().setUnlocalizedName("itemComputerTower").setTextureName("labstuff:itemComputerTower").setCreativeTab(tabLabStuff);
		itemKeyboard = new ItemComputerPart().setUnlocalizedName("itemKeyboard").setTextureName("labstuff:itemKeyboard").setCreativeTab(tabLabStuff);
		itemPlastic = new ItemPlastic().setCreativeTab(tabLabStuff).setUnlocalizedName("itemPlastic").setTextureName("labstuff:itemPlastic");
		itemZinc = new ItemLabIngot().setCreativeTab(tabLabStuff).setUnlocalizedName("itemZinc").setTextureName("labstuff:itemZinc");
		itemManganese = new ItemLabIngot().setCreativeTab(tabLabStuff).setUnlocalizedName("itemMang").setTextureName("labstuff:itemMang");
		itemBattery = new ItemBattery().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemBattery").setUnlocalizedName("itemBattery");
		itemDeadBattery = new ItemBattery().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemBattery").setUnlocalizedName("itemDeadBattery");
		itemTestTube = new ItemTestTube().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemTestTube").setUnlocalizedName("itemTestTube");
		itemHydrogenTestTube = new ItemTestTube().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemHydrogenTestTube").setUnlocalizedName("itemHydrogenTestTube");
		itemOxygenTestTube = new ItemTestTube().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemOxygenTestTube").setUnlocalizedName("itemOxygenTestTube");
		//Registries
		//Blocks
		GameRegistry.registerBlock(blockCopperOre, blockCopperOre.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(blockCircuitDesignTable, "blockCircuitDesignTable");
		GameRegistry.registerBlock(blockCircuitMaker, blockCircuitMaker.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(blockPlasticOre, "PlasticOre");
		GameRegistry.registerBlock(blockComputer, "Computer");
		GameRegistry.registerBlock(blockZincOre, "blockZincOre");
		GameRegistry.registerBlock(blockMangOre, "blockMangOre");
		GameRegistry.registerBlock(blockElectrifier, "blockElectrifier");
				
		//Items
		GameRegistry.registerItem(itemFiberGlass, "FiberGlass");
		GameRegistry.registerItem(itemCopperIngot, "CopperIngot");
		GameRegistry.registerItem(itemBasicCircuitDesign, "BasicCircuitDesign");
		GameRegistry.registerItem(itemComputerCircuitDesign, "ComputerCircuitDesign");
		GameRegistry.registerItem(itemCircuitBoardPlate, "CircuitBoardPlate");
		GameRegistry.registerItem(itemBasicDrilledCircuitBoard, "BasicDrilledCircuitBoard");
		GameRegistry.registerItem(itemBasicEtchedCircuitBoard, "BasicEtchedCircuitBoard");
		GameRegistry.registerItem(itemBasicCircuitBoard, "BasicCircuitBoard");
		GameRegistry.registerItem(itemComputerCircuitBoard, "CompuuterCircuitBoard");
		GameRegistry.registerItem(itemMonitor, "Monitor");
		GameRegistry.registerItem(itemKeyboard, "Keyboard");
		GameRegistry.registerItem(itemComputerTower, "ComputerTower");
		GameRegistry.registerItem(itemPlastic, "Plastic");
		GameRegistry.registerItem(itemZinc, "itemZinc");
		GameRegistry.registerItem(itemManganese, "itemMang");
		GameRegistry.registerItem(itemBattery, "itemBattery");
		GameRegistry.registerItem(itemDeadBattery, "itemDeadBattery");
		GameRegistry.registerItem(itemTestTube, "itemTestTube");
		GameRegistry.registerItem(itemHydrogenTestTube, "itemHydrogenTestTube");
		GameRegistry.registerItem(itemOxygenTestTube, "itemOxygenTestTube");

	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		System.out.println("Hi yah we get here.");			
		
		//Proxy junk
		proxy.registerRenders();
		
		//Crafting Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemFiberGlass), new ItemStack(Items.bread), new ItemStack(Blocks.glass_pane));
		GameRegistry.addShapelessRecipe(new ItemStack(this.itemBasicCircuitBoard), new ItemStack(this.itemBasicCircuitDesign), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCircuitBoardPlate));
		GameRegistry.addShapelessRecipe(new ItemStack(this.itemComputerCircuitBoard), new ItemStack(this.itemComputerCircuitDesign), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCopperIngot), new ItemStack(this.itemCircuitBoardPlate));
		GameRegistry.addRecipe(new ItemStack(this.itemCircuitBoardPlate), "x", "y", "x", 'x', new ItemStack(this.itemFiberGlass), 'y', new ItemStack(this.itemCopperIngot));
		GameRegistry.addRecipe(new ItemStack(blockComputer), " x "," yi", 'x', new ItemStack(itemMonitor), 'y', new ItemStack(itemComputerTower), 'i', new ItemStack(itemKeyboard));
		GameRegistry.addRecipe(new ItemStack(itemMonitor), "iii","ibg","ici",'i',new ItemStack(Items.iron_ingot), 'b', new ItemStack(itemBasicCircuitBoard), 'g', new ItemStack(Blocks.glass_pane), 'c', new ItemStack(itemCopperIngot));
		GameRegistry.addRecipe(new ItemStack(itemComputerTower), "iii","ibi","iii",'i',new ItemStack(Items.iron_ingot), 'b', new ItemStack(itemComputerCircuitBoard));
		GameRegistry.addRecipe(new ItemStack(itemKeyboard), "ppp","cbp","ppp", 'p', new ItemStack(itemPlastic), 'b', new ItemStack(itemBasicCircuitBoard), 'c', new ItemStack(itemCopperIngot));
		GameRegistry.addRecipe(new ItemStack(itemBattery), "pzp", "pmp", "pzp", 'p', new ItemStack(Items.paper), 'z', new ItemStack(itemZinc), 'm', new ItemStack(itemManganese));
		//Smelting recipes
		GameRegistry.addSmelting(blockCopperOre, new ItemStack(this.itemCopperIngot, 2), 3);
		GameRegistry.addSmelting(blockPlasticOre, new ItemStack(this.itemPlastic, 2), 3);
		GameRegistry.addSmelting(blockZincOre, new ItemStack(itemZinc, 2), 3);
		GameRegistry.addSmelting(blockMangOre, new ItemStack(itemManganese, 2), 3);
		//Tile Entities
		GameRegistry.registerTileEntity(TileEntityCircuitDesignTable.class, "TileEntityCircuitDesignTable");
		GameRegistry.registerTileEntity(TileEntityCircuitMaker.class, "TileEntityCircuitMaker");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "TileEntityComputer");
		GameRegistry.registerTileEntity(TileEntityElectrifier.class, "TileEntityElectrifier");
		
		//Packets
		packetPipeline.initalise();
		packetPipeline.registerPacket(PacketCircuitDesignTable.class);
		packetPipeline.registerPacket(PacketComputer.class);
		packetPipeline.registerPacket(PacketElectrifier.class);
	    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	    
	    GameRegistry.registerWorldGenerator(new LabStuffOreGen(), 0);
	}
	
	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
	}
	
}
