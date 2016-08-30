package keegan.labstuff;


import java.io.File;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid= "labstuff", name="LabStuff", version="2.5")
public class LabStuffMain 
{
	@SidedProxy(clientSide = "keegan.labstuff.client.LabStuffClientProxy", serverSide = "keegan.labstuff.common.LabStuffCommonProxy")
	public static LabStuffCommonProxy proxy;
	
	@Instance("labstuff")
	public static LabStuffMain instance;
	
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
	public static Block blockDataPipe;
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
	
	public static Fluid steam = new Fluid("steam");
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event)
	{
		System.out.println("Were doing stuff");
		//Blocks
		blockCopperOre = new BlockCopperOre(Material.rock).setHardness(10F).setResistance(20F).setStepSound(Block.soundTypeStone).setBlockName("blockCopperOre").setBlockTextureName("labstuff:blockCopperOre").setCreativeTab(tabLabStuff);
		blockCircuitDesignTable = new BlockCircuitDesignTable(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockCircuitDesignTable");
		blockCircuitMaker = new BlockCircuitMaker(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockCircuitMaker").setBlockTextureName("labstuff:blockCircuitMaker");
		blockComputer = new BlockComputer(Material.iron).setBlockName("blockComputer").setCreativeTab(tabLabStuff);
		blockZincOre = new BlockLabOre().setBlockName("blockZincOre").setBlockTextureName("labstuff:blockZincOre").setCreativeTab(tabLabStuff).setHardness(10F).setResistance(20F);
		blockMangOre = new BlockLabOre().setBlockName("blockMangOre").setBlockTextureName("labstuff:blockMangOre").setCreativeTab(tabLabStuff).setHardness(10F).setResistance(20F);
		blockElectrifier = new BlockElectrifier(Material.iron).setBlockName("blockElectrifier").setCreativeTab(tabLabStuff);
		blockPlasmaPipe = new BlockPlasmaPipe(Material.iron).setBlockName("plasmaPipe").setCreativeTab(tabLabStuff);
		blockGasChamberWall = new BlockGasChamberWall(Material.iron).setBlockName("blockGasChamberWall").setCreativeTab(tabLabStuff);
		blockElectronGrabber = new BlockElectronGrabber(Material.iron).setBlockName("blockElectronGrabber").setCreativeTab(tabLabStuff);
		blockGasChamberPort = new BlockGasChamberPort(Material.iron).setBlockName("blockGasChamberPort").setCreativeTab(tabLabStuff).setBlockTextureName("labstuff:gaschamberport");
		blockPowerFurnace = new BlockPowerFurnace(Material.iron).setBlockName("blockPowerFurnace").setCreativeTab(tabLabStuff).setBlockTextureName("labstuff:blockCircuitMaker");
		blockPowerCable = new BlockPowerCable(Material.cloth).setBlockName("blockPowerCable").setCreativeTab(tabLabStuff);
		blockRFToLVConverter = new BlockRFtoLV(Material.iron).setBlockName("blockRFtoLV").setBlockTextureName("labstuff:blockRFToLV").setCreativeTab(tabLabStuff);
		blockLVToRFConverter = new BlockLVToRF(Material.iron).setBlockName("blockLVToRF").setBlockTextureName("labstuff:blockLVToRF").setCreativeTab(tabLabStuff);
		blockSolarPanel = new BlockSolarPanel(Material.iron).setBlockName("blockSolarPanel").setCreativeTab(tabLabStuff);
		blockSolarGag = new BlockSolarGag(Material.iron).setBlockName("blockSolarGag");
		blockCzochralskistor = new BlockCzo(Material.iron).setBlockName("blockCzo").setCreativeTab(tabLabStuff);
		blockSiliconOre = new BlockLabOre().setBlockName("blockSiliconOre").setBlockTextureName("coal_ore").setCreativeTab(tabLabStuff);
		blockWindTurbine = new BlockWindTurbine(Material.iron).setBlockName("blockWindTurbine").setCreativeTab(tabLabStuff);
		blockWindGag = new BlockWindGag(Material.iron).setBlockName("blockWindGag");
		blockDataCable = new BlockDataCable(Material.cloth).setBlockName("blockDataCable").setCreativeTab(tabLabStuff);
		blockIndustrialMotor = new BlockIndustrialMotor(Material.iron).setBlockName("blockIndustrialMotor").setCreativeTab(tabLabStuff).setBlockTextureName("labstuff:blockIndustrialMotor");
		blockIndustrialMotorShaft = new BlockIndustrialMotorShaft(Material.iron).setBlockName("blockindustrialMotorShaft").setCreativeTab(tabLabStuff).setBlockTextureName("labstuff:blockIndustrialMotorShaft");
		blockIndustrialMotorContact = new BlockIndustrialMotorContact(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockIndustrialMotorContact").setBlockTextureName("labstuff:blockIndustrialMotorContact");
		blockFusionSolenoidAxel = new BlockSolenoidAxel(Material.iron).setBlockName("blockFusionSolenoidAxel").setBlockTextureName("labstuff:blockSolenoid").setCreativeTab(tabLabStuff);
		blockFusionSolenoidArm = new BlockSolenoid(Material.iron).setBlockName("blockFusionSolenoidArm").setBlockTextureName("labstuff:blockSolenoidArm").setCreativeTab(tabLabStuff);
		blockFusionSolenoid = new BlockSolenoid(Material.iron).setBlockName("blockFusionSolenoid").setBlockTextureName("labstuff:blockSolenoid").setCreativeTab(tabLabStuff);
		blockFusionPlasmaTap = new BlockFusionPlasmaTap(Material.iron).setBlockName("blockFusionPlasmaTap").setBlockTextureName("labstuff:blockPlasmaTap").setCreativeTab(tabLabStuff);
		blockFusionToroidalMagnet = new BlockFusionToroidalMagnet(Material.iron).setBlockName("blockToroid").setCreativeTab(tabLabStuff);
		blockFusionHeatExchange = new BlockFusionHeatExchange(Material.iron).setBlockName("blockFusionHeatExchange").setBlockTextureName("labstuff:heatexchange").setCreativeTab(tabLabStuff);
		blockLiquidPipe = new BlockLiquidPipe(Material.iron).setBlockName("blockLiquidPipe").setCreativeTab(tabLabStuff);
		blockReservoir=new BlockReservoir(Material.iron).setBlockName("blockReservoir").setCreativeTab(tabLabStuff);
		blockRedstonePipe = new BlockRedstonePipe(Material.iron).setBlockName("blockRedstonePipe").setBlockTextureName("labstuff:redstonePipe").setCreativeTab(tabLabStuff);
		blockDataPipe = new BlockDataPipe(Material.iron).setBlockName("blockDataPipe").setBlockTextureName("labstuff:dataPipe").setCreativeTab(tabLabStuff);
		blockCharger = new BlockCharger(Material.iron).setBlockName("blockCharger").setBlockTextureName("labstuff:charger").setCreativeTab(tabLabStuff);
		blockTurbineCasing = new BlockTurbine(Material.iron).setBlockName("blockTurbineCase").setBlockTextureName("labstuff:turbinecase").setCreativeTab(tabLabStuff);
		blockTurbineGlass = new BlockTurbine(Material.iron).setBlockName("blockTurbineGlass").setBlockTextureName("labstuff:turbineglass").setCreativeTab(tabLabStuff);
		blockTurbineRotor = new BlockTurbine(Material.iron).setBlockName("blockTurbineRotor").setCreativeTab(tabLabStuff);
		blockTurbineValve = new BlockTurbine(Material.iron).setBlockName("blockTurbineValve").setBlockTextureName("labstuff:turbinevalve").setCreativeTab(tabLabStuff);
		blockElectromagneticCoil = new BlockTurbine(Material.iron).setBlockName("blockEMCoil").setCreativeTab(tabLabStuff);
		blockTurbineVent = new BlockTurbine(Material.iron).setBlockName("blockTurbineVent").setBlockTextureName("labstuff:turbinevent").setCreativeTab(tabLabStuff);
		blockBattery = new BlockBattery(Material.iron).setBlockName("blockBattery").setBlockTextureName("labstuff:battery").setCreativeTab(tabLabStuff);
		blockVent = new BlockVent(Material.iron).setBlockName("blockVent").setBlockTextureName("labstuff:vent").setCreativeTab(tabLabStuff);
		blockEnricher = new BlockEnricher(Material.iron).setBlockName("blockEnricher").setCreativeTab(tabLabStuff);
		
		blockGag = new BlockGag(Material.iron).setBlockName("blockGag").setBlockTextureName("labstuff:blockGag");
		blockAcceleratorControlPanel = new BlockAcceleratorControlPanel().setBlockName("blockAcceleratorControlPanel").setCreativeTab(tabLabStuff);
		blockACPGag = new BlockACPGag(Material.iron).setBlockName("blockACPGag").setBlockTextureName("labstuff:blockGag");
		blockAcceleratorInterface = new BlockAcceleratorInterface().setBlockName("blockAcceleratorInterface").setCreativeTab(tabLabStuff).setBlockTextureName("labstuff:acceleratorInterface");
		blockAcceleratorTube = new BlockAcceleratorTube().setBlockName("blockAcceleratorTube").setCreativeTab(tabLabStuff);
		blockAcceleratorDetectorCore = new BlockAcceleratorDetectorCore(Material.iron).setBlockName("blockAccleratorDetectorCore").setBlockTextureName("labstuff:detectorCore").setCreativeTab(tabLabStuff);
		blockAcceleratorTrackingDetector = new BlockAcceleratorDetector().setBlockName("blockAcceleratorTrackingDetector").setBlockTextureName("labstuff:trackingDetector").setCreativeTab(tabLabStuff);
		blockAcceleratorSolenoid = new BlockAcceleratorDetector().setBlockName("blockAcceleratorSolenoid").setBlockTextureName("labstuff:solenoid").setCreativeTab(tabLabStuff);
		blockAcceleratorElectromagneticCalorimeter = new BlockAcceleratorDetector().setBlockName("blockAcceleratorElectromagneticCalorimeter").setBlockTextureName("labstuff:electroCalorimeter").setCreativeTab(tabLabStuff);
		blockAcceleratorHadronCalorimeter = new BlockAcceleratorDetector().setBlockName("blockAcceleratorHadronCalorimeter").setBlockTextureName("labstuff:hadron").setCreativeTab(tabLabStuff);
		blockAcceleratorMuonDetector = new BlockAcceleratorDetector().setBlockName("blockAcceleratorMuonDetector").setBlockTextureName("labstuff:muon").setCreativeTab(tabLabStuff);
		blockAcceleratorPowerInput = new BlockAcceleratorPowerInput().setBlockName("blockAcceleratorPowerInput").setBlockTextureName("labstuff:acceleratorInterface").setCreativeTab(tabLabStuff);
		
		blockDSCRibbonCable = new BlockDSCRibbonCable(Material.iron).setBlockName("blockDSCRibbonCable").setBlockTextureName("labstuff:dscribboncable").setCreativeTab(tabLabStuff);
		blockDSCCore = new BlockDSCCore(Material.iron).setBlockName("blockDSCCore").setBlockTextureName("labstuff:dsccore").setCreativeTab(tabLabStuff);
		blockDSCOS = new BlockDSCOS(Material.iron).setBlockName("blockDSCOS").setBlockTextureName("labstuff:dscos").setCreativeTab(tabLabStuff);
		blockDSCRam = new BlockDSCRam(Material.iron).setBlockName("blockDSCRam").setBlockTextureName("labstuff:dscram").setCreativeTab(tabLabStuff);
		blockDSCDrive = new BlockDSCDrive(Material.iron).setBlockName("blockDSCDrive").setBlockTextureName("labstuff:dscdrive").setCreativeTab(tabLabStuff);
		blockDSCWorkbench = new BlockDSCBench(Material.iron).setBlockName("blockdscbench").setCreativeTab(tabLabStuff);
		
		blockDLLaptop = new BlockDLLaptop(Material.iron).setBlockName("blockDLLaptop").setCreativeTab(tabLabStuff);
		blockGravityManipulater = new BlockGravityManipulater(Material.iron).setCreativeTab(tabLabStuff).setBlockName("blockGravityManipulater").setBlockTextureName("labstuff:blockGravity");	
		
		blockRubberLeaves = new BlockRubberLeaves().setBlockName("blockRubberLeaves");
		blockRubberLog = new BlockRubberLog().setBlockName("blockRubberLog");
		blockRubberSapling = new BlockRubberSapling().setBlockName("blockRubberSapling").setCreativeTab(tabLabStuff).setHardness(0f).setStepSound(Block.soundTypeGrass);
		
		blockMatterCollector = new BlockMatterCollector(Material.iron).setBlockName("blockMatterCollector").setCreativeTab(tabLabStuff);
		
		blockSteel = new BlockLabBlock(Material.iron).setBlockName("blockSteel").setCreativeTab(tabLabStuff).setBlockTextureName("labstuff:blockcircuitmaker");
		
		//Items
		itemFiberGlass = new ItemFiberGlass().setUnlocalizedName("itemFiberGlass").setCreativeTab(tabLabStuff);
		itemRubber = new ItemLabIngot().setCreativeTab(tabLabStuff).setUnlocalizedName("itemRubber").setTextureName("labstuff:itemRubber");
		itemCopperIngot = new ItemCopperIngot().setUnlocalizedName("itemCopperIngot").setCreativeTab(tabLabStuff);
		itemCircuitBoardPlate = new ItemCircuitBoardPlate().setUnlocalizedName("itemCircuitBoardPlate").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemCircuitBoardPlate");
		itemBasicDrilledCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemBasicDrilledCircuitBoard").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemBasicEtchedCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemBasicEtchedCircuitBoard").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemComputerDrilledCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemComputerDrilledCircuitBoard").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemComputerEtchedCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemComputerEtchedCircuitBoard").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemBasicCircuitBoard = new ItemCircuitBoard().setUnlocalizedName("itemBasicCircuitboard").setTextureName("labstuff:itemCircuitBoard").setCreativeTab(tabLabStuff);
		itemComputerCircuitBoard = new ItemCircuitBoard().setUnlocalizedName("itemComputerCircuitboard").setTextureName("labstuff:itemCircuitBoard").setCreativeTab(tabLabStuff);
		itemBasicCircuitDesign = new ItemCircuitDesign().setUnlocalizedName("itemBasicCircuitDesign").setCreativeTab(tabLabStuff);
		itemComputerCircuitDesign = new ItemCircuitDesign().setUnlocalizedName("itemComputerCircuitDesign").setCreativeTab(tabLabStuff);
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
		itemSteel = new ItemLabIngot().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemSteel").setUnlocalizedName("itemSteel");
		itemInterCircuitDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setUnlocalizedName("itemIntermediateCircuitDesign");
		itemInterDrilledCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemIntermediateDrilledCircuitBoard").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemInterEtchedCircuitBoard = new ItemPartialCircuitBoard().setUnlocalizedName("itemIntermediateEtchedCircuitBoard").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemInterCircuitBoard = new ItemCircuitBoard().setUnlocalizedName("itemIntermediateCircuitboard").setTextureName("labstuff:itemCircuitBoard").setCreativeTab(tabLabStuff);
		itemSiliconIngot = new ItemLabIngot().setUnlocalizedName("itemSiliconIngot").setTextureName("labstuff:itemSilicon").setCreativeTab(tabLabStuff);
		itemSiliconCrystalSeed = new ItemSiliconBoulePart().setUnlocalizedName("siliconSeed").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemSiliconSeed");
		itemSteelRod = new ItemSiliconBoulePart().setUnlocalizedName("itemSteelRod").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemSteelRod");
		itemRodMountedSiliconSeed = new ItemSiliconBoulePart().setUnlocalizedName("itemRodMountedSiliconSeed").setTextureName("labstuff:itemRodMountedSiliconSeed").setCreativeTab(tabLabStuff);
		itemSiliconBoule = new ItemSiliconBoule().setUnlocalizedName("itemSiliconBoule").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemSiliconBoule");
		itemSaw = new ItemSaw().setUnlocalizedName("saw").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemSaw");
		itemSiliconWafer = new ItemSolarPanelPart().setUnlocalizedName("itemSiliconWafer").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemSiliconWafer");
		itemSolarCell = new ItemSolarPanelPart().setUnlocalizedName("itemSolarCell").setTextureName("labstuff:solarCell").setCreativeTab(tabLabStuff);
		itemElectromagnet = new ItemElectromagnet().setCreativeTab(tabLabStuff).setUnlocalizedName("itemElectromagnet").setTextureName("labstuff:itemElectromagent");
		itemWrench = new ItemWrench().setCreativeTab(tabLabStuff).setUnlocalizedName("itemWrench").setTextureName("labstuff:itemWrench");
		itemDiscoveryDrive = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDiscovery");
		itemDiscoveryDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDiscoveryDriveCircuitDesign");
		itemDrilledDiscovery = new ItemPartialCircuitBoard().setUnlocalizedName("itemDrilledDiscovery").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemEtchedDiscovery = new ItemPartialCircuitBoard().setUnlocalizedName("itemEtchedDiscovery").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemDSCCoreDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDSCCoreDesign");
		itemDrilledDSCCore = new ItemPartialCircuitBoard().setUnlocalizedName("itemDrilledDSCCore").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemEtchedDSCCore = new ItemPartialCircuitBoard().setUnlocalizedName("itemEtchedDSCCore").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemDSCRamDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDSCRamDesign");
		itemDrilledDSCRam = new ItemPartialCircuitBoard().setUnlocalizedName("itemDrilledDSCRam").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemEtchedDSCRam = new ItemPartialCircuitBoard().setUnlocalizedName("itemEtchedDSCRam").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemDSCOSDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDSCOSDesign");
		itemDrilledDSCOS = new ItemPartialCircuitBoard().setUnlocalizedName("itemDrilledDSCOS").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemEtchedDSCOS = new ItemPartialCircuitBoard().setUnlocalizedName("itemEtchedDSCOS").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemDSCBenchDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDSCBenchDesign");
		itemDrilledDSCBench = new ItemPartialCircuitBoard().setUnlocalizedName("itemDrilledDSCBench").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemEtchedDSCBench = new ItemPartialCircuitBoard().setUnlocalizedName("itemEtchedDSCBench").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemDSCDriveDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDSCDriveCircuitDesign");
		itemDrilledDSCDrive = new ItemPartialCircuitBoard().setUnlocalizedName("itemDrilledDSCDrive").setTextureName("labstuff:itemDrilledCircuitBoard").setCreativeTab(tabLabStuff);
		itemEtchedDSCDrive = new ItemPartialCircuitBoard().setUnlocalizedName("itemEtchedDSCDrive").setTextureName("labstuff:itemEtchedCircuitBoard").setCreativeTab(tabLabStuff);
		itemGluonDetector = new ItemGluonDetector().setCreativeTab(tabLabStuff).setUnlocalizedName("itemGluonDetector").setTextureName("labstuff:gluon");
		itemDPad = new ItemDPad().setUnlocalizedName("itemDPad").setTextureName("labstuff:itemDPad").setCreativeTab(tabLabStuff);
		itemTouchMesh = new ItemDPadPart().setUnlocalizedName("itemTouchMesh").setCreativeTab(tabLabStuff).setTextureName("labstuff:itemTouchMesh");
		itemTouchScreen = new ItemDPadPart().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemTouchScreen").setUnlocalizedName("itemTouchScreen");
		itemUnProgrammedDPad = new ItemDPadPart().setUnlocalizedName("itemBiosFailDPad").setTextureName("labstuff:itemBiosFailDPad").setCreativeTab(tabLabStuff);
		itemWarpDriveBattery = new ItemWarpDriveBattery().setCreativeTab(tabLabStuff).setUnlocalizedName("itemWarpDriveBattery").setTextureName("labstuff:warpBattery");
		itemEmptyWarpDriveBattery = new ItemEmptyWarpDriveBattery().setCreativeTab(tabLabStuff).setUnlocalizedName("itemEmptyWarpDriveBattery").setTextureName("labstuff:warpBattery");
		itemDiscoveryAntiMatter = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDiscoveryAntiMatter");
		itemDiscoveryNegativeEnergy = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDiscoveryNegativeEnergy");
		itemDiscoveryWarp = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDiscoveryWarp");
		itemAdvancedAccelInterface = new ItemAcceInterfaceUpgrade().setCreativeTab(tabLabStuff).setUnlocalizedName("itemAccelInterfaceUpgrade").setTextureName("labstuff:discovDrive");
		itemDiscoveryQuantum = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff).setUnlocalizedName("itemDiscoveryQuantum");
		
		itemZincDust = new ItemDust().setCreativeTab(tabLabStuff).setTextureName("labstuff:zincDust").setUnlocalizedName("itemZincDust");
		itemMangDust = new ItemDust().setCreativeTab(tabLabStuff).setTextureName("labstuff:mangDust").setUnlocalizedName("itemMangDust");
		itemCopperDust = new ItemDust().setCreativeTab(tabLabStuff).setTextureName("labstuff:copperDust").setUnlocalizedName("itemCopperDust");
		itemIronDust = new ItemDust().setCreativeTab(tabLabStuff).setTextureName("labstuff:ironDust").setUnlocalizedName("itemIronDust");
		itemGoldDust = new ItemDust().setCreativeTab(tabLabStuff).setTextureName("labstuff:goldDust").setUnlocalizedName("itemGoldDust");
		
		itemMatterCollectorCore = new ItemMatterCollectorCore().setCreativeTab(tabLabStuff).setTextureName("labstuff:matterCollectorCore").setUnlocalizedName("itemMatterCollectorCore");
		itemTurbineBlades = new ItemTurbineBlades().setCreativeTab(tabLabStuff).setTextureName("labstuff:itemTurbineBlades").setUnlocalizedName("itemTurbineBlades");
		
		//Registries
		//Blocks
		GameRegistry.registerBlock(blockCopperOre, blockCopperOre.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(blockZincOre, "blockZincOre");
		GameRegistry.registerBlock(blockMangOre, "blockMangOre");
		GameRegistry.registerBlock(blockSiliconOre, "blockSiliconOre");
		GameRegistry.registerBlock(blockCircuitDesignTable, "blockCircuitDesignTable");
		GameRegistry.registerBlock(blockCircuitMaker, blockCircuitMaker.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(blockElectrifier, "blockElectrifier");
		GameRegistry.registerBlock(blockPlasmaPipe, "plasmaPipe");
		GameRegistry.registerBlock(blockGasChamberWall, "blockGasChamberWall");
		GameRegistry.registerBlock(blockGasChamberPort, "blockGasChamberPort");
		GameRegistry.registerBlock(blockElectronGrabber, "blockElectronGrabber");
		GameRegistry.registerBlock(blockPowerFurnace, "blockPowerFurnace");
		GameRegistry.registerBlock(blockPowerCable, "blockPowerCable");
		GameRegistry.registerBlock(blockRFToLVConverter, "blockRFToLV");
		GameRegistry.registerBlock(blockLVToRFConverter, "blockLVToRF");
		GameRegistry.registerBlock(blockSolarPanel, "blockSolarPanel");
		GameRegistry.registerBlock(blockSolarGag, "blockSolarGag");
		GameRegistry.registerBlock(blockWindTurbine, "blockWindTurbine");
		GameRegistry.registerBlock(blockBattery, "blockBattery");
		GameRegistry.registerBlock(blockCharger, "blockCharger");
		GameRegistry.registerBlock(blockWindGag, "blockWindGag");
		GameRegistry.registerBlock(blockCzochralskistor, "blockCzo");
		GameRegistry.registerBlock(blockIndustrialMotor, "blockIndustrialMotor");
		GameRegistry.registerBlock(blockIndustrialMotorContact, "blockIndustrialMotorContact");
		GameRegistry.registerBlock(blockIndustrialMotorShaft, "blockIndustrialMotorShaft");
		GameRegistry.registerBlock(blockFusionSolenoidAxel, "blockFusionSolenoidAxel");
		GameRegistry.registerBlock(blockFusionSolenoidArm, "blockFusionSolenoidArm");
		GameRegistry.registerBlock(blockFusionSolenoid, "blockFusionSolenoid");
		GameRegistry.registerBlock(blockFusionPlasmaTap, "blockFusionPlasmaTap");
		GameRegistry.registerBlock(blockFusionToroidalMagnet, "blockFusionToroidalMagnet");
		GameRegistry.registerBlock(blockFusionHeatExchange, "blockFusionHeatExchange");
		GameRegistry.registerBlock(blockTurbineCasing, "blockTurbineCase");
		GameRegistry.registerBlock(blockTurbineGlass, "blockTurbineGlass");
		GameRegistry.registerBlock(blockTurbineRotor, "blockTurbineRotor");
		GameRegistry.registerBlock(blockTurbineValve, "blockTurbineValve");
		GameRegistry.registerBlock(blockElectromagneticCoil, "blockEMCoil");
		GameRegistry.registerBlock(blockTurbineVent, "blockTurbineVent");
		GameRegistry.registerBlock(blockVent, "blockVent");
		GameRegistry.registerBlock(blockGag, "gag");
		GameRegistry.registerBlock(blockAcceleratorControlPanel, "blockAcceleratorControlPanel");
		GameRegistry.registerBlock(blockACPGag, "blockACPGag");
		GameRegistry.registerBlock(blockAcceleratorInterface, "blockAcceleratorInterface");
		GameRegistry.registerBlock(blockAcceleratorTube, "blockAcceleratorTube");
		GameRegistry.registerBlock(blockAcceleratorDetectorCore, "blockAccleratorDetectorCore");
		GameRegistry.registerBlock(blockAcceleratorTrackingDetector, "blockAcceleratorTrackingDetector");
		GameRegistry.registerBlock(blockAcceleratorSolenoid, "blockAcceleratorSolenoid");
		GameRegistry.registerBlock(blockAcceleratorElectromagneticCalorimeter, "blockAcceleratorElectromagneticCalorimeter");
		GameRegistry.registerBlock(blockAcceleratorHadronCalorimeter, "blockAcceleratorHadronCalorimeter");
		GameRegistry.registerBlock(blockAcceleratorMuonDetector, "blockAcceleratorMuonDetector");
		GameRegistry.registerBlock(blockAcceleratorPowerInput, "blockAcceleratorPowerInput");
		GameRegistry.registerBlock(blockDSCRibbonCable, "blockDSCRibbonCable");
		GameRegistry.registerBlock(blockDSCCore, "blockDSCCore");
		GameRegistry.registerBlock(blockDSCOS, "blockDSCOS");
		GameRegistry.registerBlock(blockDSCRam, "blockDSCRam");
		GameRegistry.registerBlock(blockDSCDrive, "blockDSCDrive");
		GameRegistry.registerBlock(blockDSCWorkbench, "blockDSCBench");
		GameRegistry.registerBlock(blockReservoir, "reservoir");
		GameRegistry.registerBlock(blockLiquidPipe, "blockLiquidPipe");
		GameRegistry.registerBlock(blockGravityManipulater, "blockGravityManipulater");
		GameRegistry.registerBlock(blockComputer, "Computer");
		GameRegistry.registerBlock(blockDataCable, "blockDataCable");
		GameRegistry.registerBlock(blockDLLaptop, "blockDLLaptop");	
		GameRegistry.registerBlock(blockEnricher, "blockEnricher");
		GameRegistry.registerBlock(blockRubberLog, "blockRubberLog");
		GameRegistry.registerBlock(blockRubberLeaves, "blockRubberLeaves");
		GameRegistry.registerBlock(blockRubberSapling, "blockRubberSapling");
		GameRegistry.registerBlock(blockMatterCollector, "blockMatterCollector");
		GameRegistry.registerBlock(blockSteel, "blockSteel");
		//GameRegistry.registerBlock(blockRedstonePipe, "blockRedstonePipe");
		//GameRegistry.registerBlock(blockDataPipe, "blockDataPipe");
		
		//Items
		GameRegistry.registerItem(itemFiberGlass, "FiberGlass");
		GameRegistry.registerItem(itemRubber, "itemRubber");
		GameRegistry.registerItem(itemCopperIngot, "CopperIngot");
		GameRegistry.registerItem(itemPlastic, "Plastic");
		GameRegistry.registerItem(itemZinc, "itemZinc");
		GameRegistry.registerItem(itemManganese, "itemMang");
		GameRegistry.registerItem(itemSiliconIngot, "itemSiliconIngot");
		GameRegistry.registerItem(itemSteel, "itemSteel");
		GameRegistry.registerItem(itemCircuitBoardPlate, "CircuitBoardPlate");
		GameRegistry.registerItem(itemBasicCircuitDesign, "BasicCircuitDesign");
		GameRegistry.registerItem(itemBasicDrilledCircuitBoard, "BasicDrilledCircuitBoard");
		GameRegistry.registerItem(itemBasicEtchedCircuitBoard, "BasicEtchedCircuitBoard");
		GameRegistry.registerItem(itemBasicCircuitBoard, "BasicCircuitBoard");
		GameRegistry.registerItem(itemInterCircuitDesign, "itemInterCircuitDesign");
		GameRegistry.registerItem(itemInterEtchedCircuitBoard, "InterEtchedCircuitBoard");
		GameRegistry.registerItem(itemInterDrilledCircuitBoard, "InterDrilledCircuitBoard");
		GameRegistry.registerItem(itemInterCircuitBoard, "InterCircuitBoard");
		GameRegistry.registerItem(itemComputerCircuitDesign, "ComputerCircuitDesign");
		GameRegistry.registerItem(itemComputerDrilledCircuitBoard, "ComputerDrilledCircuitBoard");
		GameRegistry.registerItem(itemComputerEtchedCircuitBoard, "ComputerEtchedCircuitBoard");
		GameRegistry.registerItem(itemComputerCircuitBoard, "CompuuterCircuitBoard");
		GameRegistry.registerItem(itemMonitor, "Monitor");
		GameRegistry.registerItem(itemKeyboard, "Keyboard");
		GameRegistry.registerItem(itemComputerTower, "ComputerTower");
		GameRegistry.registerItem(itemBattery, "itemBattery");
		GameRegistry.registerItem(itemDeadBattery, "itemDeadBattery");
		GameRegistry.registerItem(itemTestTube, "itemTestTube");
		GameRegistry.registerItem(itemHydrogenTestTube, "itemHydrogenTestTube");
		GameRegistry.registerItem(itemOxygenTestTube, "itemOxygenTestTube");
		GameRegistry.registerItem(itemSiliconCrystalSeed, "siliconSeed");
		GameRegistry.registerItem(itemSteelRod, "itemSteelRod");
		GameRegistry.registerItem(itemRodMountedSiliconSeed, "siliconOnRod");
		GameRegistry.registerItem(itemSiliconBoule, "siliconBoule");
		GameRegistry.registerItem(itemSaw, "saw");
		GameRegistry.registerItem(itemSiliconWafer, "itemSiliconWafer");
		GameRegistry.registerItem(itemSolarCell, "itemSolarCell");
		GameRegistry.registerItem(itemElectromagnet, "itemElectromagnet");
		GameRegistry.registerItem(itemWrench, "itemWrench");
		GameRegistry.registerItem(itemDPad, "dPad");
		GameRegistry.registerItem(itemTouchMesh, "touchMesh");
		GameRegistry.registerItem(itemTouchScreen, "touchScreen");
		GameRegistry.registerItem(itemUnProgrammedDPad, "biosFailDPad");
		GameRegistry.registerItem(itemGluonDetector, "itemGluonDetector");
		GameRegistry.registerItem(itemDiscoveryDrive, "itemDiscovery");
		GameRegistry.registerItem(itemDiscoveryDesign, "itemDiscoveryDriveDesign");
		GameRegistry.registerItem(itemDrilledDiscovery, "itemDrilledDiscovery");
		GameRegistry.registerItem(itemEtchedDiscovery, "itemEtchedDiscovery");
		GameRegistry.registerItem(itemDSCCoreDesign, "itemDSCCoreDesign");
		GameRegistry.registerItem(itemDrilledDSCCore, "itemDrilledDSCCore");
		GameRegistry.registerItem(itemEtchedDSCCore, "itemEtchedDSCCore");
		GameRegistry.registerItem(itemDSCRamDesign, "itemDSCRamDesign");
		GameRegistry.registerItem(itemDrilledDSCRam, "itemDrilledDSCRam");
		GameRegistry.registerItem(itemEtchedDSCRam, "itemEtchedDSCRam");
		GameRegistry.registerItem(itemDSCOSDesign, "itemDSCOSDesign");
		GameRegistry.registerItem(itemDrilledDSCOS, "itemDrilledDSCOS");
		GameRegistry.registerItem(itemEtchedDSCOS, "itemEtchedDSCOS");
		GameRegistry.registerItem(itemDSCBenchDesign, "itemDSCBenchDesign");
		GameRegistry.registerItem(itemDrilledDSCBench, "itemDrilledDSCBench");
		GameRegistry.registerItem(itemEtchedDSCBench, "itemEtchedDSCBench");
		GameRegistry.registerItem(itemDSCDriveDesign, "itemDSCDriveDesign");
		GameRegistry.registerItem(itemDrilledDSCDrive, "itemDrilledDSCDrive");
		GameRegistry.registerItem(itemEtchedDSCDrive, "itemEtchedDSCDrive");
		GameRegistry.registerItem(itemDiscoveryAntiMatter, "itemDiscoveryAntiMatter");
		GameRegistry.registerItem(itemDiscoveryNegativeEnergy, "itemDiscoveryNegativeEnergy");
		GameRegistry.registerItem(itemDiscoveryWarp, "itemDiscoveryWarp");
		GameRegistry.registerItem(itemAdvancedAccelInterface, "itemAccelInterfaceUpgrade");
		GameRegistry.registerItem(itemWarpDriveBattery, "itemWarpDriveBattery");
		GameRegistry.registerItem(itemEmptyWarpDriveBattery, "itemEmptyWarpDriveBattery");
		GameRegistry.registerItem(itemZincDust, "itemZincDust");
		GameRegistry.registerItem(itemMangDust, "itemMangDust");
		GameRegistry.registerItem(itemCopperDust, "itemCopperDust");
		GameRegistry.registerItem(itemIronDust, "itemIronDust");
		GameRegistry.registerItem(itemGoldDust, "itemGoldDust");
		GameRegistry.registerItem(itemDiscoveryQuantum, "itemDiscoveryQuantum");
		GameRegistry.registerItem(itemMatterCollectorCore, "itemMatterCollectorCore");
		GameRegistry.registerItem(itemTurbineBlades, "itemTurbineBlades");
		
		//steam liquid
		steam.setLuminosity(5);
		steam.setTemperature(20);
		FluidRegistry.registerFluid(steam);
		blockSteamBlock = new BlockSteam(steam, Material.water).setBlockName("blockSteam");
		GameRegistry.registerBlock(blockSteamBlock, "blockSteam");

	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		System.out.println("Hi yah we get here.");		
		
		oreDict();
		
		//Proxy junk
		proxy.registerRenders();
		
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
		Recipes.addCircuitCreation("DSCCore", itemEtchedDSCCore, itemDrilledDSCCore, Item.getItemFromBlock(blockDSCCore));
		Recipes.addCircuitDesign("DSCRam", itemDSCRamDesign);
		Recipes.addCircuitCreation("DSCRam", itemEtchedDSCRam, itemDrilledDSCRam, Item.getItemFromBlock(blockDSCRam));
		Recipes.addCircuitDesign("DSCOS", itemDSCOSDesign);
		Recipes.addCircuitCreation("DSCOS", itemEtchedDSCOS, itemDrilledDSCOS, Item.getItemFromBlock(blockDSCOS));
		Recipes.addCircuitDesign("DSCBench", itemDSCBenchDesign);
		Recipes.addCircuitCreation("DSCBench", itemEtchedDSCBench, itemDrilledDSCBench, Item.getItemFromBlock(blockDSCWorkbench));
		Recipes.addCircuitDesign("DSCDrive", itemDSCDriveDesign);
		Recipes.addCircuitCreation("DSCDrive", itemEtchedDSCDrive, itemDrilledDSCDrive, Item.getItemFromBlock(blockDSCDrive));
		
		//Charges
		Recipes.addCharge(itemDeadBattery, itemBattery, 50);
		
		Recipes.addAccelDiscovery(null, new ItemStack(itemDiscoveryAntiMatter));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter), new ItemStack(itemDiscoveryNegativeEnergy));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryNegativeEnergy), new ItemStack(itemDiscoveryWarp));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter), new ItemStack(itemDiscoveryQuantum));
		Recipes.addDiscovItem(new ItemStack(itemAdvancedAccelInterface, 1), new ItemStack(LabStuffMain.itemElectromagnet, 1), new ItemStack(LabStuffMain.itemCopperIngot,2), new ItemStack(Items.iron_ingot, 5), Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter), "Advanced Accelerator Interface Upgrade");
		Recipes.addDiscovItem(new ItemStack(itemEmptyWarpDriveBattery, 1), new ItemStack(LabStuffMain.itemElectromagnet, 1), new ItemStack(LabStuffMain.itemCopperIngot,2), new ItemStack(Items.iron_ingot, 5), Recipes.getDiscovFromDrive(itemDiscoveryWarp), "Warp Drive Battery (Empty)");
		Recipes.addDiscovItem(new ItemStack(blockGravityManipulater), new ItemStack(Items.iron_ingot, 8), new ItemStack(LabStuffMain.itemElectromagnet), new ItemStack(itemTouchScreen), Recipes.getDiscovFromDrive(itemDiscoveryWarp), "Gravity Manipulater");
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
		GameRegistry.registerTileEntity(TileEntityPowerFurnace.class, "TileEntityPowerFurnace");
		GameRegistry.registerTileEntity(TileEntityPowerCable.class, "TileEntityPowerCable");
		GameRegistry.registerTileEntity(TileEntitySolarPanel.class, "TileEntitySolarPanel");
		GameRegistry.registerTileEntity(TileEntityRFToLV.class, "TileEntityRFToLV");
		GameRegistry.registerTileEntity(TileEntityLVToRF.class, "TileEntityLVToRF");
		GameRegistry.registerTileEntity(TileEntityCzo.class, "TileEntityCzo");
		GameRegistry.registerTileEntity(DataConnectedDevice.class, "DataConnectedDevice");
		GameRegistry.registerTileEntity(TileEntityDataCable.class, "tileentityDataCable");
		GameRegistry.registerTileEntity(TileEntityPowerConnection.class, "TileEntityPowerConnection");
		//GameRegistry.registerTileEntity(TileEntityPlasmaConnection.class, "TileEntityPlasmaConnection");
		GameRegistry.registerTileEntity(TileEntityReservoir.class, "Reservoir");
		GameRegistry.registerTileEntity(TileEntityLiquidPipe.class, "LiquidPipe");
		GameRegistry.registerTileEntity(TileEntityRedstonePipe.class, "RedstonePipe");
		GameRegistry.registerTileEntity(TileEntityDataPipe.class, "DataPipe");
		GameRegistry.registerTileEntity(TileEntityIndustrialMotorContact.class, "IndustrialMotorContact");
		GameRegistry.registerTileEntity(TileEntityIndustrialMotorShaft.class, "IndustrialMotorShaft");
		GameRegistry.registerTileEntity(TileEntitySolenoidAxel.class, "SolenoidAxel");
		GameRegistry.registerTileEntity(TileEntityFusionToroidalMagnet.class,"TileEntityToroid");
		GameRegistry.registerTileEntity(TileEntityPlasmaTap.class, "TileEntityPlasmaTap");
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
	}
	
	@EventHandler
	public void postLoad(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
	}
	
}
