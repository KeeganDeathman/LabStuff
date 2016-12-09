package keegan.labstuff;


import java.io.File;
import java.util.*;

import keegan.labstuff.PacketHandling.*;
import keegan.labstuff.blocks.*;
import keegan.labstuff.client.GuiHandler;
import keegan.labstuff.common.*;
import keegan.labstuff.items.*;
import keegan.labstuff.recipes.Recipes;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.world.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid= "labstuff", name="LabStuff", version="2.5")
public class LabStuffMain 
{
	@SidedProxy(clientSide = "keegan.labstuff.client.LabStuffClientProxy", serverSide = "keegan.labstuff.common.LabStuffCommonProxy")
	public static LabStuffCommonProxy proxy;
	
	@Instance("labstuff")
	public static LabStuffMain instance;
	
	public static ArrayList<Block> labstuffBlocks = new ArrayList<Block>();
	public static ArrayList<Item> labstuffItems = new ArrayList<Item>();

	
	//Blocks
	public static Block blockCopperOre;
	public static Block blockCircuitDesignTable;
	public static Block blockCircuitMaker;
	public static Block blockComputer;
	public static Block blockElectrifier;
	public static Block blockZincOre;
	public static Block blockMangOre;
	public static Block blockSteamBlock;
	public static Block blockPlasmaPipe;
	public static Block blockElectronGrabber;
	public static Block blockGasChamberWall;
	public static Block blockGasChamberPort;
	public static Block blockPowerFurnace;
	public static Block blockPowerCable;
	public static Block blockRFToLVConverter;
	public static Block blockLVToRFConverter;
	public static Block blockSolarPanel;
	public static Block blockSolarGag;
	public static Block blockCzochralskistor;
	public static Block blockSiliconOre;
	public static Block blockWindTurbine;
	public static Block blockWindGag;
	public static Block blockDataCable;
	
	//Transport
	public static Block blockLiquidPipe;
	public static Block blockItemPipe;
	public static Block blockReservoir;
	
	public static Block blockFusionToroidalMagnet;
	public static Block blockFusionHeatExchange;
	public static Block blockFusionPlasmaTap;
	public static Block blockFusionSolenoid;
	public static Block blockFusionSolenoidArm;
	public static Block blockFusionSolenoidAxel;
	
	public static Block blockIndustrialMotor;
	public static Block blockIndustrialMotorShaft;
	public static Block blockIndustrialMotorContact;
	
	public static Block blockRedstonePipe;
	public static Block blockRedstoneShaft;
	public static Block blockDataShaft;
	
	public static Block blockCharger;
	public static Block blockBattery;
	public static Block blockVent;
	
	public static Block blockDLLaptop;
	public static Block blockGravityManipulater;
	
	public static Block blockEnricher;
	public static Block blockElectricFurnace;

	//Turbine
	public static Block blockTurbineCasing;
	public static Block blockTurbineGlass;
	public static Block blockTurbineRotor;
	public static Block blockTurbineValve;
	public static Block blockElectromagneticCoil;
	public static Block blockTurbineVent;
	
	
	//Particle accelerator
	public static Block blockAcceleratorTube;
	public static Block blockAcceleratorDetectorCore;
	public static Block blockAcceleratorTrackingDetector;
	public static Block blockAcceleratorSolenoid;
	public static Block blockAcceleratorElectromagneticCalorimeter;
	public static Block blockAcceleratorHadronCalorimeter;
	public static Block blockAcceleratorMuonDetector;
	public static Block blockAcceleratorInterface;
	public static Block blockAcceleratorPowerInput;
	public static Block blockAcceleratorControlPanel;
	public static Block blockACPGag;
	public static Block blockGag;
	
	//DiscoverySupercomputer
	public static Block blockDSCCore;
	public static Block blockDSCRam;
	public static Block blockDSCWorkbench;
	public static Block blockDSCDrive;
	public static Block blockDSCOS;
	public static Block blockDSCRibbonCable;
	
	public static Block blockRubberSapling;
	public static Block blockRubberLog;
	public static Block blockRubberLeaves;
	
	public static Block blockMatterCollector;
	public static Block blockSteel;

	
	//Items
	public static Item itemFiberGlass;
	public static Item itemRubber;
	public static Item itemCopperIngot;
	public static Item itemCircuitBoardPlate;
	public static Item itemBasicCircuitDesign;
	public static Item itemComputerCircuitDesign;
	public static Item itemBasicDrilledCircuitBoard;
	public static Item itemBasicEtchedCircuitBoard;
	public static Item itemComputerDrilledCircuitBoard;
	public static Item itemComputerEtchedCircuitBoard;
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
	public static Item itemSteel;
	public static Item itemSiliconIngot;
	public static Item itemSiliconCrystalSeed;
	public static Item itemSteelRod;
	public static Item itemRodMountedSiliconSeed;
	public static Item itemSiliconBoule;
	public static Item itemInterCircuitDesign;
	public static Item itemInterDrilledCircuitBoard;
	public static Item itemInterEtchedCircuitBoard;
	public static Item itemInterCircuitBoard;
	public static Item itemSiliconWafer;
	public static Item itemSolarCell;
	public static Item itemSaw;
	public static Item itemElectromagnet;
	public static Item itemWrench;
	
	public static Item itemTouchMesh;
	public static Item itemTouchScreen;
	public static Item itemUnProgrammedDPad;
	public static Item itemDPad;
	
	// Discoveries
	public static Item itemDiscoveryDrive;
	public static Item itemDiscoveryDesign;
	public static Item itemEtchedDiscovery;
	public static Item itemDrilledDiscovery;
	public static Item itemDSCCoreDesign;
	public static Item itemEtchedDSCCore;
	public static Item itemDrilledDSCCore;
	public static Item itemDSCRamDesign;
	public static Item itemEtchedDSCRam;
	public static Item itemDrilledDSCRam;
	public static Item itemDSCOSDesign;
	public static Item itemEtchedDSCOS;
	public static Item itemDrilledDSCOS;
	public static Item itemDSCBenchDesign;
	public static Item itemEtchedDSCBench;
	public static Item itemDrilledDSCBench;
	public static Item itemDSCDriveDesign;
	public static Item itemEtchedDSCDrive;
	public static Item itemDrilledDSCDrive;
	public static Item itemAdvancedAccelInterface;
	public static Item itemDiscoveryAntiMatter;
	public static Item itemDiscoveryNegativeEnergy;
	public static Item itemDiscoveryWarp;
	public static Item itemDiscoveryQuantum;
	public static Item itemDiscoveryTemporal;
	
	public static Item itemWarpDriveBattery;
	public static Item itemEmptyWarpDriveBattery;
	
	public static Item itemGluonDetector;

	public static Item itemTurbineBlades;
	
	//Dust
	public static Item itemZincDust;
	public static Item itemMangDust;
	public static Item itemCopperDust;
	public static Item itemIronDust;
	public static Item itemGoldDust;
	
	public static Item itemMatterCollectorCore;
	
	//Other
	public static CreativeTabs tabLabStuff = new TabLabStuff("tabLabStuff");

	public static final File minecraftDir = (File) FMLInjectionData.data()[6];
    public static final File filesDir = new File(minecraftDir, "labstuff");
    static {filesDir.mkdirs();}
    
	public static final PacketPipeline packetPipeline = new PacketPipeline();
	
	public static Fluid steam = new Fluid("steam", new ResourceLocation("labstuff:textures/blocks/steam_still.png"), new ResourceLocation("labstuff:textures/blocks/steam_flow.png"));
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		System.out.println("Were doing stuff");
		//Blocks
		labstuffBlocks.add(blockCopperOre = new BlockCopperOre(Material.ROCK).setHardness(10F).setResistance(20F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockcopperore"));
		labstuffBlocks.add(blockCircuitDesignTable = new BlockCircuitDesignTable(Material.IRON).setHardness(10F).setResistance(20F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockcircuitdesigntable"));
		labstuffBlocks.add(blockCircuitMaker = new BlockCircuitMaker(Material.IRON).setHardness(10F).setResistance(20F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockcircuitmaker"));
		labstuffBlocks.add(blockComputer = new BlockComputer(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockcomputer").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockZincOre = new BlockLabOre().setRegistryName("labstuff:blockzincore").setCreativeTab(tabLabStuff).setHardness(10F).setResistance(20F));
		labstuffBlocks.add(blockMangOre = new BlockLabOre().setRegistryName("labstuff:blockmangore").setCreativeTab(tabLabStuff).setHardness(10F).setResistance(20F));
		labstuffBlocks.add(blockElectrifier = new BlockElectrifier(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockelectrifier").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockPlasmaPipe = new BlockPlasmaPipe(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:plasmapipe").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockGasChamberWall = new BlockGasChamberWall(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockgaschamberwall").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockElectronGrabber = new BlockElectronGrabber(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockelectrongrabber").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockGasChamberPort = new BlockGasChamberPort(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockgaschamberport").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockPowerFurnace = new BlockPowerFurnace(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockpowerfurnace").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockPowerCable = new BlockPowerCable(Material.CLOTH).setRegistryName("labstuff:blockPowerCable").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockRFToLVConverter = new BlockRFtoLV(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockrftolv").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockLVToRFConverter = new BlockLVToRF(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blocklvtorf").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockSolarPanel = new BlockSolarPanel(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blocksolarpanel").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockSolarGag = new BlockSolarGag(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockSolarGag"));
		labstuffBlocks.add(blockCzochralskistor = new BlockCzo(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockCzo").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockSiliconOre = new BlockLabOre().setRegistryName("labstuff:blockSiliconOre").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockWindTurbine = new BlockWindTurbine(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockwindturbine").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockWindGag = new BlockWindGag(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockWindGag"));
		labstuffBlocks.add(blockDataCable = new BlockDataCable(Material.CLOTH).setRegistryName("labstuff:blockDataCable").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockIndustrialMotor = new BlockIndustrialMotor(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockIndustrialMotor").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockIndustrialMotorShaft = new BlockIndustrialMotorShaft(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockindustrialMotorShaft").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockIndustrialMotorContact = new BlockIndustrialMotorContact(Material.IRON).setHardness(10F).setResistance(20F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockIndustrialMotorContact"));
		labstuffBlocks.add(blockFusionSolenoidAxel = new BlockSolenoidAxel(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockFusionSolenoidAxel").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionSolenoidArm = new BlockSolenoid(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockFusionSolenoidArm").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionSolenoid = new BlockSolenoid(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockFusionSolenoid").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionPlasmaTap = new BlockFusionPlasmaTap(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockFusionPlasmaTap").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionToroidalMagnet = new BlockFusionToroidalMagnet(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockfusiontoroid").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionHeatExchange = new BlockFusionHeatExchange(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockFusionHeatExchange").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockLiquidPipe = new BlockLiquidPipe(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockLiquidPipe").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockReservoir=new BlockReservoir(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockReservoir").setCreativeTab(tabLabStuff));
//		labstuffBlocks.add(blockRedstonePipe = new BlockRedstonePipe(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockRedstonePipe").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockCharger = new BlockCharger(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockCharger").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineCasing = new BlockTurbine(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockturbinecasing").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineGlass = new BlockTurbine(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockturbineglass").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineRotor = new BlockTurbine(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockTurbineRotor").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineValve = new BlockTurbine(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockTurbineValve").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockElectromagneticCoil = new BlockTurbine(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockEMCoil").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineVent = new BlockTurbine(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockTurbineVent").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockBattery = new BlockBattery(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockBatteryBlock").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockVent = new BlockVent(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockVent").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockEnricher = new BlockEnricher(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockEnricher").setCreativeTab(tabLabStuff));
		
		labstuffBlocks.add(blockGag = new BlockGag(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockGag"));
		labstuffBlocks.add(blockAcceleratorControlPanel = new BlockAcceleratorControlPanel().setRegistryName("labstuff:blockAcceleratorControlPanel").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockACPGag = new BlockACPGag(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockACPGag"));
		labstuffBlocks.add(blockAcceleratorInterface = new BlockAcceleratorInterface().setRegistryName("labstuff:blockAcceleratorInterface").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorTube = new BlockAcceleratorTube().setRegistryName("labstuff:blockAcceleratorTube").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorDetectorCore = new BlockAcceleratorDetectorCore(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockAcceleratorDetectorCore").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorTrackingDetector = new BlockAcceleratorDetector().setRegistryName("labstuff:blockAcceleratorTrackingDetector").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorSolenoid = new BlockAcceleratorDetector().setRegistryName("labstuff:blockAcceleratorSolenoid").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorElectromagneticCalorimeter = new BlockAcceleratorDetector().setRegistryName("labstuff:blockAcceleratorElectromagneticCalorimeter").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorHadronCalorimeter = new BlockAcceleratorDetector().setRegistryName("labstuff:blockAcceleratorHadronCalorimeter").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorMuonDetector = new BlockAcceleratorDetector().setRegistryName("labstuff:blockAcceleratorMuonDetector").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorPowerInput = new BlockAcceleratorPowerInput().setRegistryName("labstuff:blockAcceleratorPowerInput").setCreativeTab(tabLabStuff));
		
		labstuffBlocks.add(blockDSCRibbonCable = new BlockDSCRibbonCable(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockDSCRibbonCable").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCCore = new BlockDSCCore(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockDSCCore").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCOS = new BlockDSCOS(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockDSCOS").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCRam = new BlockDSCRam(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockDSCRam").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCDrive = new BlockDSCDrive(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockDSCDrive").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCWorkbench = new BlockDSCBench(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockdscbench").setCreativeTab(tabLabStuff));
		
		labstuffBlocks.add(blockDLLaptop = new BlockDLLaptop(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockDLLaptop").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockGravityManipulater = new BlockGravityManipulater(Material.IRON).setHardness(10F).setResistance(20F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockGravityManipulater"));	
		
		labstuffBlocks.add(blockRubberLeaves = new BlockRubberLeaves().setRegistryName("labstuff:blockRubberLeaves"));
		labstuffBlocks.add(blockRubberLog = new BlockRubberLog().setRegistryName("labstuff:blockRubberLog"));
		labstuffBlocks.add(blockRubberSapling = new BlockRubberSapling().setRegistryName("labstuff:blockRubberSapling").setCreativeTab(tabLabStuff).setHardness(0f));
		
		labstuffBlocks.add(blockMatterCollector = new BlockMatterCollector(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockMatterCollector").setCreativeTab(tabLabStuff));
		
		labstuffBlocks.add(blockSteel = new BlockLabBlock(Material.IRON).setHardness(10F).setResistance(20F).setRegistryName("labstuff:blockSteelBlock").setCreativeTab(tabLabStuff));
		
		//Items
		labstuffItems.add(itemFiberGlass = new ItemFiberGlass().setRegistryName("labstuff:itemFiberGlass").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemRubber = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemRubber"));
		labstuffItems.add(itemCopperIngot = new ItemCopperIngot().setRegistryName("labstuff:itemCopperIngot").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemCircuitBoardPlate = new ItemCircuitBoardPlate().setRegistryName("labstuff:itemCircuitBoardPlate").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicDrilledCircuitBoard = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemBasicDrilledCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicEtchedCircuitBoard = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemBasicEtchedCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemComputerDrilledCircuitBoard = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemComputerDrilledCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemComputerEtchedCircuitBoard = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemComputerEtchedCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicCircuitBoard = new ItemCircuitBoard().setRegistryName("labstuff:itemBasicCircuitboard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemComputerCircuitBoard = new ItemCircuitBoard().setRegistryName("labstuff:itemComputerCircuitboard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicCircuitDesign = new ItemCircuitDesign().setRegistryName("labstuff:itemBasicCircuitDesign").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemComputerCircuitDesign = new ItemCircuitDesign().setRegistryName("labstuff:itemComputerCircuitDesign").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemMonitor = new ItemComputerPart().setRegistryName("labstuff:itemMonitor").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemComputerTower = new ItemComputerPart().setRegistryName("labstuff:itemComputerTower").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemKeyboard = new ItemComputerPart().setRegistryName("labstuff:itemKeyboard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemPlastic = new ItemPlastic().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemPlastic"));
		labstuffItems.add(itemZinc = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemZinc"));
		labstuffItems.add(itemManganese = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemMang"));
		labstuffItems.add(itemBattery = new ItemBattery().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemBattery"));
		labstuffItems.add(itemDeadBattery = new ItemBattery().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDeadBattery"));
		labstuffItems.add(itemTestTube = new ItemTestTube().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemTestTube"));
		labstuffItems.add(itemHydrogenTestTube = new ItemTestTube().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemHydrogenTestTube"));
		labstuffItems.add(itemOxygenTestTube = new ItemTestTube().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemOxygenTestTube"));
		labstuffItems.add(itemSteel = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemSteel"));
		labstuffItems.add(itemInterCircuitDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemIntermediateCircuitDesign"));
		labstuffItems.add(itemInterDrilledCircuitBoard = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemIntermediateDrilledCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemInterEtchedCircuitBoard = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemIntermediateEtchedCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemInterCircuitBoard = new ItemCircuitBoard().setRegistryName("labstuff:itemIntermediateCircuitboard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconIngot = new ItemLabIngot().setRegistryName("labstuff:itemSiliconIngot").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconCrystalSeed = new ItemSiliconBoulePart().setRegistryName("labstuff:siliconSeed").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSteelRod = new ItemSiliconBoulePart().setRegistryName("labstuff:itemSteelRod").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemRodMountedSiliconSeed = new ItemSiliconBoulePart().setRegistryName("labstuff:itemRodMountedSiliconSeed").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconBoule = new ItemSiliconBoule().setRegistryName("labstuff:itemSiliconBoule").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSaw = new ItemSaw().setRegistryName("labstuff:saw").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconWafer = new ItemSolarPanelPart().setRegistryName("labstuff:itemSiliconWafer").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSolarCell = new ItemSolarPanelPart().setRegistryName("labstuff:itemSolarCell").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemElectromagnet = new ItemElectromagnet().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemElectromagnet"));
		labstuffItems.add(itemWrench = new ItemWrench().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemWrench"));
		labstuffItems.add(itemDiscoveryDrive = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDiscovery"));
		labstuffItems.add(itemDiscoveryDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDiscoveryDriveCircuitDesign"));
		labstuffItems.add(itemDrilledDiscovery = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemDrilledDiscovery").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDiscovery = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDiscovery").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCCoreDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDSCCoreDesign"));
		labstuffItems.add(itemDrilledDSCCore = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemDrilledDSCCore").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCCore = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDSCCore").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCRamDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDSCRamDesign"));
		labstuffItems.add(itemDrilledDSCRam = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemDrilledDSCRam").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCRam = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDSCRam").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCOSDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDSCOSDesign"));
		labstuffItems.add(itemDrilledDSCOS = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemDrilledDSCOS").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCOS = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDSCOS").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCBenchDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDSCBenchDesign"));
		labstuffItems.add(itemDrilledDSCBench = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemDrilledDSCBench").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCBench = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDSCBench").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCDriveDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDSCDriveCircuitDesign"));
		labstuffItems.add(itemDrilledDSCDrive = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemDrilledDSCDrive").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCDrive = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDSCDrive").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemGluonDetector = new ItemGluonDetector().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemGluonDetector"));
		labstuffItems.add(itemDPad = new ItemDPad().setRegistryName("labstuff:itemDPad").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemTouchMesh = new ItemDPadPart().setRegistryName("labstuff:itemTouchMesh").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemTouchScreen = new ItemDPadPart().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemTouchScreen"));
		labstuffItems.add(itemUnProgrammedDPad = new ItemDPadPart().setRegistryName("labstuff:itemBiosFailDPad").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemWarpDriveBattery = new ItemWarpDriveBattery().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemWarpDriveBattery"));
		labstuffItems.add(itemEmptyWarpDriveBattery = new ItemEmptyWarpDriveBattery().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemEmptyWarpDriveBattery"));
		labstuffItems.add(itemDiscoveryAntiMatter = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDiscoveryAntiMatter"));
		labstuffItems.add(itemDiscoveryNegativeEnergy = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDiscoveryNegativeEnergy"));
		labstuffItems.add(itemDiscoveryWarp = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDiscoveryWarp"));
		labstuffItems.add(itemAdvancedAccelInterface = new ItemAcceInterfaceUpgrade().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemAccelInterfaceUpgrade"));
		labstuffItems.add(itemDiscoveryQuantum = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemDiscoveryQuantum"));
		
		labstuffItems.add(itemZincDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemZincDust"));
		labstuffItems.add(itemMangDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemMangDust"));
		labstuffItems.add(itemCopperDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemCopperDust"));
		labstuffItems.add(itemIronDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemIronDust"));
		labstuffItems.add(itemGoldDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemGoldDust"));
		
		labstuffItems.add(itemMatterCollectorCore = new ItemMatterCollectorCore().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemMatterCollectorCore"));
		labstuffItems.add(itemTurbineBlades = new ItemTurbineBlades().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemTurbineBlades"));
		
		proxy.preInit();
		
		
		//steam liquid
		steam.setLuminosity(5);
		steam.setTemperature(20);
		FluidRegistry.registerFluid(steam);
		blockSteamBlock = new BlockSteam(steam, Material.WATER).setRegistryName("labstuff:blockSteam").setUnlocalizedName("labstuff:blockSteam");
		
		for(Block block : labstuffBlocks)
		{
			if(block.getRegistryName() != null)
			{
				ItemBlock item = new ItemBlock(block);
				item.setRegistryName(block.getRegistryName());
				block.setUnlocalizedName(block.getRegistryName().toString());
				GameRegistry.register(block);
				GameRegistry.register(item);
				proxy.registerItemModel(item);
			}
		}
		
		for(Item item : labstuffItems)
		{
			item.setUnlocalizedName(item.getRegistryName().toString());
			GameRegistry.register(item);
			proxy.registerItemModel(item);
		}
		
		
		

	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		System.out.println("Hi yah we get here.");		
		
		oreDict();
		
		//Proxy junk
		proxy.registerRenders();
		proxy.initMod();
		
		//Crafting Recipes
		Recipes.registerShaplessCrafting();
		Recipes.registerShapedCrafting();
		//Smelting recipes
		Recipes.registerSmelting();
		//Circuit Deisgns
		Recipes.addCircuitDesign("Basic", itemBasicCircuitDesign);
		Recipes.addCircuitDesign("Intermediate", itemInterCircuitDesign);
		Recipes.addCircuitDesign("Computer", itemComputerCircuitDesign);
		//CircuitMaker
		Recipes.addCircuitCreation("Basic", itemBasicEtchedCircuitBoard, itemBasicDrilledCircuitBoard, itemBasicCircuitBoard);
		Recipes.addCircuitCreation("Computer", itemComputerEtchedCircuitBoard, itemComputerDrilledCircuitBoard, itemComputerCircuitBoard);
		Recipes.addCircuitCreation("Intermediate", itemInterEtchedCircuitBoard, itemInterDrilledCircuitBoard, itemInterCircuitBoard);
		
		Recipes.addCircuitDesign("DiscoveryDrive", itemDiscoveryDesign);
		Recipes.addCircuitCreation("DiscoveryDrive", itemEtchedDiscovery, itemDrilledDiscovery, itemDiscoveryDrive);
		Recipes.addCircuitDesign("DSCCore", itemDSCCoreDesign);
		Recipes.addCircuitCreation("DSCCore", itemEtchedDSCCore, itemDrilledDSCCore, get(blockDSCCore));
		Recipes.addCircuitDesign("DSCRam", itemDSCRamDesign);
		Recipes.addCircuitCreation("DSCRam", itemEtchedDSCRam, itemDrilledDSCRam, get(blockDSCRam));
		Recipes.addCircuitDesign("DSCOS", itemDSCOSDesign);
		Recipes.addCircuitCreation("DSCOS", itemEtchedDSCOS, itemDrilledDSCOS, get(blockDSCOS));
		Recipes.addCircuitDesign("DSCBench", itemDSCBenchDesign);
		Recipes.addCircuitCreation("DSCBench", itemEtchedDSCBench, itemDrilledDSCBench, get(blockDSCWorkbench));
		Recipes.addCircuitDesign("DSCDrive", itemDSCDriveDesign);
		Recipes.addCircuitCreation("DSCDrive", itemEtchedDSCDrive, itemDrilledDSCDrive, get(blockDSCDrive));
		
		//Charges
		Recipes.addCharge(itemDeadBattery, itemBattery, 50);
		
		Recipes.addAccelDiscovery(null, new ItemStack(itemDiscoveryAntiMatter));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter), new ItemStack(itemDiscoveryNegativeEnergy));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryNegativeEnergy), new ItemStack(itemDiscoveryWarp));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter), new ItemStack(itemDiscoveryQuantum));
		Recipes.addDiscovItem(new ItemStack(itemAdvancedAccelInterface, 1), new ItemStack(LabStuffMain.itemElectromagnet, 1), new ItemStack(LabStuffMain.itemCopperIngot,2), new ItemStack(Items.IRON_INGOT, 5), Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter), "Advanced Accelerator Interface Upgrade");
		Recipes.addDiscovItem(new ItemStack(itemEmptyWarpDriveBattery, 1), new ItemStack(LabStuffMain.itemElectromagnet, 1), new ItemStack(LabStuffMain.itemCopperIngot,2), new ItemStack(Items.IRON_INGOT, 5), Recipes.getDiscovFromDrive(itemDiscoveryWarp), "Warp Drive Battery (Empty)");
		Recipes.addDiscovItem(new ItemStack(blockGravityManipulater), new ItemStack(Items.IRON_INGOT, 8), new ItemStack(LabStuffMain.itemElectromagnet), new ItemStack(itemTouchScreen), Recipes.getDiscovFromDrive(itemDiscoveryWarp), "Gravity Manipulater");
		Recipes.addDiscovItem(new ItemStack(itemMatterCollectorCore, 1), new ItemStack(blockPowerCable, 2), new ItemStack(itemComputerCircuitBoard, 1), new ItemStack(itemElectromagnet, 5), Recipes.getDiscovFromDrive(itemDiscoveryQuantum), "Matter Collector Core");
		Recipes.addDiscovItem(new ItemStack(blockMatterCollector, 1), new ItemStack(itemSteel, 20), new ItemStack(itemMatterCollectorCore, 1), new ItemStack(itemMonitor, 3), Recipes.getDiscovFromDrive(itemDiscoveryQuantum), "Matter Collector");		
		
		
		//Tile Entities
		GameRegistry.registerTileEntity(TileEntityCircuitDesignTable.class, "TileEntityCircuitDesignTable");
		GameRegistry.registerTileEntity(TileEntityCircuitMaker.class, "TileEntityCircuitMaker");
		GameRegistry.registerTileEntity(TileEntityComputer.class, "TileEntityComputer");
		GameRegistry.registerTileEntity(TileEntityElectrifier.class, "TileEntityElectrifier");
		GameRegistry.registerTileEntity(TileEntityPlasmaPipe.class, "TileEntityPlasmaPipe");
		GameRegistry.registerTileEntity(TileEntityPlasma.class, "TileEntityPlasma");
		GameRegistry.registerTileEntity(TileEntityElectronGrabber.class, "TileEntityElectronGrabber");
		GameRegistry.registerTileEntity(TileEntityGasChamberPort.class, "TileEntityGasChamberPort");
		GameRegistry.registerTileEntity(TileEntityPower.class, "TileEntityPower");
		GameRegistry.registerTileEntity(TileEntityWindTurbine.class, "TileEntityWindTurbine");
		GameRegistry.registerTileEntity(TileEntityPowerFurnace.class, "TileEntityPowerFurnace");
		GameRegistry.registerTileEntity(TileEntityPowerCable.class, "TileEntityPowerCable");
		GameRegistry.registerTileEntity(TileEntitySolarPanel.class, "TileEntitySolarPanel");
		GameRegistry.registerTileEntity(TileEntityRFToLV.class, "TileEntityRFToLV");
		GameRegistry.registerTileEntity(TileEntityLVToRF.class, "TileEntityLVToRF");
		GameRegistry.registerTileEntity(TileEntityCzo.class, "TileEntityCzo");
		GameRegistry.registerTileEntity(DataConnectedDevice.class, "DataConnectedDevice");
		GameRegistry.registerTileEntity(TileEntityDataCable.class, "tileentityDataCable");
		GameRegistry.registerTileEntity(TileEntityPowerConnection.class, "TileEntityPowerConnection");
		GameRegistry.registerTileEntity(TileEntityReservoir.class, "Reservoir");
		GameRegistry.registerTileEntity(TileEntityLiquidPipe.class, "LiquidPipe");
		GameRegistry.registerTileEntity(TileEntityRedstonePipe.class, "RedstonePipe");
		GameRegistry.registerTileEntity(TileEntityRotary.class, "TileEntityRotary");
		GameRegistry.registerTileEntity(TileEntityIndustrialMotorContact.class, "IndustrialMotorContact");
		GameRegistry.registerTileEntity(TileEntityIndustrialMotorShaft.class, "IndustrialMotorShaft");
		GameRegistry.registerTileEntity(TileEntitySolenoidAxel.class, "SolenoidAxel");
		GameRegistry.registerTileEntity(TileEntityPlasmaTap.class, "TileEntityPlasmaTap");
		GameRegistry.registerTileEntity(TileEntityToroid.class, "TileEntityToroid");
		GameRegistry.registerTileEntity(TileEntityHeatExchange.class, "TileEntityHeatExchange");
		GameRegistry.registerTileEntity(TileEntityCharger.class, "TileEntityCharger");
		GameRegistry.registerTileEntity(TileEntityTurbine.class, "TileEntityTurbine");
		GameRegistry.registerTileEntity(TileEntityTurbineValve.class, "TileEntityTurbineValve");
		GameRegistry.registerTileEntity(TileEntityTurbineVent.class, "TileEntityTurbineVent");
		GameRegistry.registerTileEntity(TileEntityBattery.class, "TileEntityBattery");
		GameRegistry.registerTileEntity(TileEntityVent.class, "TileEntityVent");
		GameRegistry.registerTileEntity(TileEntityAcceleratorControlPanel.class, "TileEntityAcceleratorControlPanel");
		GameRegistry.registerTileEntity(TileEntityAcceleratorTube.class, "TileEntityAcceleratorTube");
		GameRegistry.registerTileEntity(TileEntityAcceleratorDetectorCore.class, "TileEntityAcceleratorDetectorCore");
		GameRegistry.registerTileEntity(TileEntityAcceleratorPowerInput.class, "TileEntityPowerInput");
		GameRegistry.registerTileEntity(TileEntityRibbonCable.class, "TileEntityRibbonCable");
		GameRegistry.registerTileEntity(TileEntityDSCCore.class, "TileEntityDSCCore");
		GameRegistry.registerTileEntity(DSCRam.class, "DSCRam");
		GameRegistry.registerTileEntity(DSCOS.class, "DSCOS");
		GameRegistry.registerTileEntity(DSCDrive.class, "DSCDrive");
		GameRegistry.registerTileEntity(DSCPart.class, "DSCPart");
		GameRegistry.registerTileEntity(DSCBench.class, "DSCBench");
		GameRegistry.registerTileEntity(TileEntityDLLaptop.class, "TileEntityDLLaptop");
		GameRegistry.registerTileEntity(TileEntityGravityManipulater.class, "tileEntityGravity");		
		GameRegistry.registerTileEntity(TileEntityAcceleratorInterface.class, "TileEntityAcceleratorInterface");
		GameRegistry.registerTileEntity(TileEntityEnricher.class, "TileEntityEnricher");
		GameRegistry.registerTileEntity(TileEntityMatterCollector.class, "TileEntityMatterCollector");
		//Packets
		packetPipeline.initalise();
		packetPipeline.registerPacket(PacketCircuitDesignTable.class);
		packetPipeline.registerPacket(PacketComputer.class);
		packetPipeline.registerPacket(PacketCircuitMaker.class);
		packetPipeline.registerPacket(PacketACP.class);
		packetPipeline.registerPacket(PacketDSCDrive.class);
		packetPipeline.registerPacket(PacketDSCBench.class);
		packetPipeline.registerPacket(PacketDLLaptopUSB.class);
		packetPipeline.registerPacket(PacketDLLaptopWeb.class);
		packetPipeline.registerPacket(PacketGravity.class);
		packetPipeline.registerPacket(PacketMatterCollector.class);
	    NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
	    
	    GameRegistry.registerWorldGenerator(new LabStuffOreGen(), 0);
	    GameRegistry.registerWorldGenerator(new TreeManager(), 1);
	}
	
	private void oreDict()
	{
		OreDictionary.registerOre("oreCopper", new ItemStack(blockCopperOre));
		OreDictionary.registerOre("oreManganese", new ItemStack(blockMangOre));
		OreDictionary.registerOre("oreZinc", new ItemStack(blockZincOre));
		OreDictionary.registerOre("ingotCopper", new ItemStack(itemCopperIngot));
		OreDictionary.registerOre("ingotManganese", new ItemStack(itemManganese));
		OreDictionary.registerOre("ingotZinc", new ItemStack(itemZinc));
		OreDictionary.registerOre("circuitBasic", new ItemStack(itemBasicCircuitBoard));
		OreDictionary.registerOre("circuitComputer", new ItemStack(itemComputerCircuitBoard));
		OreDictionary.registerOre("ingotSteel", new ItemStack(itemSteel));
		OreDictionary.registerOre("circuitIntermediate", itemInterCircuitBoard);
		OreDictionary.registerOre("dustZinc", new ItemStack(itemZincDust));
		OreDictionary.registerOre("dustManganese", new ItemStack(itemMangDust));
		OreDictionary.registerOre("dustCopper", new ItemStack(itemCopperDust));
		OreDictionary.registerOre("dustIron", new ItemStack(itemIronDust));
		OreDictionary.registerOre("dustGold", new ItemStack(itemGoldDust));		
		OreDictionary.registerOre("blockSteel", new ItemStack(blockSteel));
	}
	
	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
		//Enrichments
		/**So placed because other mods load on their own time**/
		Recipes.addEnrichment(get(blockRubberLog), itemRubber);
		for(String dust : OreDictionary.getOreNames())
		{
			if(dust.startsWith("dust"))
			{
				String ore = "ore" + dust.substring(4);
				if(OreDictionary.doesOreNameExist(ore))
				{
					List<ItemStack> ores = OreDictionary.getOres(ore);
					List<ItemStack> dusts = OreDictionary.getOres(dust);
					for(int i = 0; i < dusts.size(); i++)
					{
						if(ores.size() >= i+1 && dusts.size() >= i+1)
							Recipes.addEnrichment(ores.get(i).getItem(), OreDictionary.getOres("dust" + ore.substring(3)).get(i).getItem());
					}
				}
			}
		}
	}

	public static Item get(Block block) {
		// TODO Auto-generated method stub
		return Item.getItemFromBlock(block);
	}
	
}
