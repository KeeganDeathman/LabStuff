package keegan.LabStuff;


import keegan.LabStuff.PacketHandling.PacketCircuitDesignTable;
import keegan.LabStuff.PacketHandling.PacketPipeline;
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
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraftforge.event.entity.living.LivingEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid= "labstuff", name="LabStuff", version="0.2")
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
		//Blocks
		blockCopperOre = new BlockCopperOre(600, Material.field_151566_D).func_149647_a(tabLabStuff).func_149658_d("blockCopperOre").func_149711_c(3.0F).func_149752_b(5.0F);
		blockCircuitDesignTable = new BlockCircuitDesignTable(601, Material.field_151566_D).func_149647_a(tabLabStuff).func_149658_d("blockCircuitDesignTable");
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
		GameRegistry.registerBlock(blockCircuitDesignTable, "blockCircuitDesignTable");
		
		//Items
		GameRegistry.registerItem(itemFiberGlass, "itemFiberGlass");
		GameRegistry.registerItem(itemCopperIngot, "itemCopperIngot");
		GameRegistry.registerItem(itemBasicCircuitDesign, "itemBasicCircuitDesign");
		
		//Crafting Recipes
		GameRegistry.addShapelessRecipe(new ItemStack(LabStuffMain.itemFiberGlass), new ItemStack(Items.bread), new ItemStack(Blocks.glass_pane));
		
		//Tile Entities
		GameRegistry.registerTileEntity(TileEntityCircuitDesignTable.class, "TileEntityCircuitDesignTable");
		
		//Packets
		packetPipeline.initalise();
		packetPipeline.registerPacket(PacketCircuitDesignTable.class);
		//LanguageRegistry.instance().addStringLocalization("itemGroup.TabLabStuff", "en_US", "LabStuff");
	}
	
	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
	}
	
}
