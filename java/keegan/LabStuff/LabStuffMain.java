package keegan.LabStuff;


import keegan.LabStuff.PacketHandling.PacketCircuitDesignTable;
import keegan.LabStuff.PacketHandling.PacketPipeline;
import keegan.LabStuff.blocks.BlockCircuitDesignTable;
import keegan.LabStuff.blocks.BlockCircuitMaker;
import keegan.LabStuff.blocks.BlockCopperOre;
import keegan.LabStuff.client.GuiHandler;
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
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid= "labstuff", name="LabStuff", version="0.5")
public class LabStuffMain 
{
	@SidedProxy(clientSide = "keegan.LabStuff.client.LabStuffClientProxy", serverSide = "keegan.LabStuff.common.LabStuffCommonProxy")
	public static LabStuffCommonProxy proxy;
	
	@Instance("labstuff")
	public static LabStuffMain instance;
	
	//Blocks
	public static Block blockCopperOre;
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
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		System.out.println("Were doing stuff");
		//Blocks
		blockCopperOre = new BlockCopperOre(Material.rock).setHardness(10F).setResistance(20F).setStepSound(Block.soundTypeStone).setBlockName("blockCopperOre").setBlockTextureName("labstuff:blockCopperOre").setCreativeTab(tabLabStuff);
		blockCircuitDesignTable = new BlockCircuitDesignTable(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockCircuitDesignTable");
		blockCircuitMaker = new BlockCircuitMaker(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockCircuitMaker");
		//Items
		itemFiberGlass = new ItemFiberGlass(600).setUnlocalizedName("itemFiberGlass").setCreativeTab(tabLabStuff);
		itemCopperIngot = new ItemCopperIngot(601).setUnlocalizedName("itemCopperIngot").setCreativeTab(tabLabStuff);
		itemCircuitBoardPlate = new ItemCircuitBoardPlate(602).setUnlocalizedName("itemCircuitBoardPlate").setCreativeTab(tabLabStuff);
		itemBasicCircuitDesign = new ItemCircuitDesign(603).setUnlocalizedName("itemBasicCircuitDesign").setCreativeTab(tabLabStuff);

	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		System.out.println("Hi yah we get here.");			
		
		//Proxy junk
		proxy.registerRenders();
		
		//Blocks
		System.out.println("Lets register some blocks");
		GameRegistry.registerBlock(blockCopperOre, blockCopperOre.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(blockCircuitDesignTable, "blockCircuitDesignTable");
		GameRegistry.registerBlock(blockCircuitMaker, blockCircuitMaker.getUnlocalizedName().substring(5));
		System.out.println("Now were here");
		
		//Items
		GameRegistry.registerItem(itemFiberGlass, "FiberGlass");
		GameRegistry.registerItem(itemCopperIngot, "CopperIngot");
		GameRegistry.registerItem(itemBasicCircuitDesign, "BasicCircuitDesign");
		
		//Crafting Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemFiberGlass), new ItemStack(Items.bread), new ItemStack(Blocks.glass_pane));
		
		//Tile Entities
		GameRegistry.registerTileEntity(TileEntityCircuitDesignTable.class, "TileEntityCircuitDesignTable");
		
		//Packets
		packetPipeline.initalise();
		packetPipeline.registerPacket(PacketCircuitDesignTable.class);
		//LanguageRegistry.instance().addStringLocalization("itemGroup.TabLabStuff", "en_US", "LabStuff");
		 NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	}
	
	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
	}
	
}
