package keegan.labstuff;

import java.io.File;
import java.util.*;
import java.util.function.*;

import javax.imageio.*;

import keegan.labstuff.PacketHandling.*;
import keegan.labstuff.PacketHandling.PacketDataRequest.DataRequestMessage;
import keegan.labstuff.PacketHandling.PacketTransmitterUpdate.*;
import keegan.labstuff.blocks.*;
import keegan.labstuff.blocks.BlockSpaceGlass.*;
import keegan.labstuff.client.*;
import keegan.labstuff.common.*;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.dimension.*;
import keegan.labstuff.entities.LSPlayerHandler;
import keegan.labstuff.galaxies.*;
import keegan.labstuff.items.*;
import keegan.labstuff.multipart.*;
import keegan.labstuff.network.DynamicNetwork.*;
import keegan.labstuff.network.EnergyNetwork.EnergyTransferEvent;
import keegan.labstuff.network.FluidNetwork.FluidTransferEvent;
import keegan.labstuff.network.PlasmaNetwork.PlasmaTransferEvent;
import keegan.labstuff.network.TransmitterNetworkRegistry;
import keegan.labstuff.recipes.*;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import keegan.labstuff.world.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = "labstuff", name = "LabStuff", version = "2.5", dependencies = "after:mcmultipart")
public class LabStuffMain {
	@SidedProxy(clientSide = "keegan.labstuff.client.LabStuffClientProxy", serverSide = "keegan.labstuff.common.LabStuffCommonProxy")
	public static LabStuffCommonProxy proxy;

	@Instance("labstuff")
	public static LabStuffMain instance;

	public static ArrayList<Block> labstuffBlocks = new ArrayList<Block>();
	public static ArrayList<Item> labstuffItems = new ArrayList<Item>();
	public static final Set<Fluid> FLUIDS = new HashSet<>();
	public static ArrayList<IFluidBlock> labstuffFluidBlocks = new ArrayList<IFluidBlock>();

	// Blocks
	public static Block blockCopperOre;
	public static Block blockCircuitDesignTable;
	public static Block blockCircuitMaker;
	public static Block blockElectrifier;
	public static Block blockZincOre;
	public static Block blockMangOre;
	public static IFluidBlock blockSteamBlock;
	public static Block blockElectronGrabber;
	public static Block blockGasChamberWall;
	public static Block blockGasChamberPort;
	public static Block blockPowerFurnace;
	public static Block blockRFToLVConverter;
	public static Block blockLVToRFConverter;
	public static Block blockSolarPanel;
	public static Block blockSolarGag;
	public static Block blockCzochralskistor;
	public static Block blockSiliconOre;
	public static Block blockWindTurbine;
	public static Block blockWindGag;

	// Transport
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

	public static Block blockCharger;
	public static Block blockBattery;
	public static Block blockVent;

	public static Block blockDLLaptop;
	public static Block blockGravityManipulater;

	public static Block blockEnricher;
	public static Block blockElectricFurnace;

	// Turbine
	public static Block blockTurbineCasing;
	public static Block blockTurbineGlass;
	public static Block blockTurbineRotor;
	public static Block blockTurbineValve;
	public static Block blockElectromagneticCoil;
	public static Block blockTurbineVent;

	// Particle accelerator
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

	// DiscoverySupercomputer
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

	public static Block blockWorkbench;
	public static Block alloySmelter;
	public static Block reactionChamber;
	public static Block chloralakizer;

	public static Block titaniumOre;
	public static Block marble;
	public static Block bauxiteOre;
	public static Block saltBlock;

	// Sputnik
	public static Block sputnik;
	public static Block rd107;
	public static Block rd108;
	public static Block block8k71p5O;
	public static Block fuelTank;
	public static Block gimbalIGS;
	public static Block rocketStrap;
	public static Block radioKit;
	public static Block r7;

	// RocketFuel
	public static IFluidBlock ethanolBlock;
	public static IFluidBlock vegetableOilBlock;
	public static IFluidBlock blockLO2;
	public static IFluidBlock blockBioDiesel;
	public static IFluidBlock blockKerosene;
	public static IFluidBlock blockSulfolane;
	public static Block fermenter;
	public static Block squeezer;
	public static Block fuelingTower;

	// IBM-650
	public static Block ibm650;
	public static Block ibm650PowerUnit;
	public static Block ibm650Console;
	public static Block ibm650Punch;
	public static Block keyPunch;

	// Space
	public static Block blockLuna;
	public static Block brightAir;
	public static Block breatheableAir;
	public static Block brightBreatheableAir;
	public static Block landingPad;
	public static Block landingPadFull;
	public static Block fakeBlock;
	public static Block cyroChamber;
	public static BlockSpaceGlass spaceGlassVanilla;
	public static BlockSpaceGlass spaceGlassClear;
	public static BlockSpaceGlass spaceGlassStrong;
	public static BlockSpaceGlass spaceGlassTinVanilla;
	public static BlockSpaceGlass spaceGlassTinClear;
	public static BlockSpaceGlass spaceGlassTinStrong;
	public static Block fallenMeteor;

	// Items
	public static Item itemFiberGlass;
	public static Item itemRubber;
	public static Item itemCopperIngot;
	public static Item itemCircuitBoardPlate;
	public static Item itemBasicCircuitDesign;
	public static Item itemBasicDrilledCircuitBoard;
	public static Item itemBasicEtchedCircuitBoard;
	public static Item itemBasicCircuitBoard;
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

	// Dust
	public static Item itemZincDust;
	public static Item itemMangDust;
	public static Item itemCopperDust;
	public static Item itemIronDust;
	public static Item itemGoldDust;

	public static Item itemMatterCollectorCore;

	public static Item itemPartTransmitter;

	public static Item magnesium;
	public static Item titanium;
	public static Item titaniumDust;
	public static Item aluminum;
	public static Item amg6t;
	public static Item salt;
	public static Item lye;

	public static Item fuelCanister;
	public static Item oxygenGear;
	public static Item shieldController;
	public static Item buggy; 

	// Sputnik
	public static Item designRadio;
	public static Item etchedRadio;
	public static Item drilledRadio;
	public static Item boardRadio;
	public static Item gyroscopicRing;
	public static Item radioAntenna;
	public static Item designSputnik;
	public static Item etchedSputnik;
	public static Item drilledSputnik;
	public static Item boardSputnik;
	public static Item designrd107;
	public static Item designrd108;
	public static Item designr7;
	public static Item design8k71p5O;

	public static Item vaccumTube;
	public static Item drumStorage;

	// Punch
	public static Item punchCard;
	public static Item punchDeck;

	// Other
	public static CreativeTabs tabLabStuff = new TabLabStuff("tabLabStuff", "LabStuff");
	public static CreativeTabs tabLabStuffSpace = new TabLabStuff("tabLabStuffSpace", "LabStuff Space");
	public static CreativeTabs tabLabStuffComputers = new TabLabStuff("tabLabStuffComputers", "LabStuff Computing");

	public static boolean isHeightConflictingModInstalled;
	public static LSPlayerHandler handler;

	public static SolarSystem solarSystemSol;
	public static Planet planetMercury;
	public static Planet planetVenus;
	public static Planet planetMars;
	public static Planet planetOverworld;
	public static Planet planetJupiter;
	public static Planet planetSaturn;
	public static Planet planetUranus;
	public static Planet planetNeptune;
	public static Moon moonLuna;
	public static Satellite satelliteSpaceStation;

	public static final File minecraftDir = (File) FMLInjectionData.data()[6];
	public static final File filesDir = new File(minecraftDir, "labstuff");
	static {
		filesDir.mkdirs();
	}

	public static final PacketPipeline packetPipeline = new PacketPipeline();

	public static final Fluid steam = createFluid("steam", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockSteam(fluid, new MaterialLiquid(MapColor.ADOBE)));
	public static final Fluid ethanol = createFluid("ethanol", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));
	public static final Fluid plantoil = createFluid("plantoil", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockPlantOil(fluid, new MaterialLiquid(MapColor.ADOBE)));
	public static final Fluid bioDiesel = createFluid("bioDiesel", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));
	public static final Fluid LO2 = createFluid("oxygen", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));
	public static final Fluid kerosene = createFluid("kerosene", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));
	public static final Fluid sulfolane = createFluid("sulfolane", true,
			fluid -> fluid.setLuminosity(10).setDensity(1600).setViscosity(100),
			fluid -> new BlockFluidClassic(fluid, new MaterialLiquid(MapColor.ADOBE)));

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}

	public static ImageWriter jpgWriter;
	public static ImageWriteParam writeParam;
	public static boolean enableJPEG = false;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		if (Loader.isModLoaded("SmartMoving")) {
			isHeightConflictingModInstalled = true;
		}

		if (Loader.isModLoaded("witchery")) {
			isHeightConflictingModInstalled = true;
		}

		handler = new LSPlayerHandler();

		ConfigManagerCore.initialize(new File(event.getModConfigurationDirectory(), "labstuff/core.conf"));
		// Blocks
		labstuffBlocks.add(blockCopperOre = new BlockCopperOre(Material.ROCK).setHardness(3F).setResistance(5F)
				.setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockcopperore"));
		labstuffBlocks.add(blockCircuitDesignTable = new BlockCircuitDesignTable(Material.IRON).setHardness(3F)
				.setResistance(5F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockcircuitdesigntable"));
		labstuffBlocks.add(blockCircuitMaker = new BlockCircuitMaker(Material.IRON).setHardness(3F).setResistance(5F)
				.setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockcircuitmaker"));
		labstuffBlocks.add(blockZincOre = new BlockLabOre().setRegistryName("labstuff:blockzincore")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(blockMangOre = new BlockLabOre().setRegistryName("labstuff:blockmangore")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(blockElectrifier = new BlockElectrifier(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockelectrifier").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockGasChamberWall = new BlockGasChamberWall(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockgaschamberwall").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockElectronGrabber = new BlockElectronGrabber(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockelectrongrabber").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockGasChamberPort = new BlockGasChamberPort(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockgaschamberport").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockPowerFurnace = new BlockPowerFurnace(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockpowerfurnace").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockRFToLVConverter = new BlockRFtoLV(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockrftolv").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockLVToRFConverter = new BlockLVToRF(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blocklvtorf").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockSolarPanel = new BlockSolarPanel(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blocksolarpanel").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockSolarGag = new BlockSolarGag(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockSolarGag"));
		labstuffBlocks.add(blockCzochralskistor = new BlockCzo(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockCzo").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockSiliconOre = new BlockLabOre().setRegistryName("labstuff:blockSiliconOre")
				.setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockWindTurbine = new BlockWindTurbine(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockwindturbine").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockWindGag = new BlockWindGag(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockWindGag"));
		labstuffBlocks.add(blockIndustrialMotor = new BlockIndustrialMotor(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockIndustrialMotor").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockIndustrialMotorShaft = new BlockIndustrialMotorShaft(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockindustrialMotorShaft").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockIndustrialMotorContact = new BlockIndustrialMotorContact(Material.IRON).setHardness(3F)
				.setResistance(5F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockIndustrialMotorContact"));
		labstuffBlocks.add(blockFusionSolenoidAxel = new BlockSolenoidAxel(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockFusionSolenoidAxel").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionSolenoidArm = new BlockSolenoid(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockFusionSolenoidArm").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionSolenoid = new BlockSolenoid(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockFusionSolenoid").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionPlasmaTap = new BlockFusionPlasmaTap(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockFusionPlasmaTap").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionToroidalMagnet = new BlockFusionToroidalMagnet(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockfusiontoroid").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockFusionHeatExchange = new BlockFusionHeatExchange(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockFusionHeatExchange").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockReservoir = new BlockReservoir(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockReservoir").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockCharger = new BlockCharger(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockCharger").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineCasing = new BlockTurbine(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockturbinecasing").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineGlass = new BlockTurbine(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockturbineglass").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineRotor = new BlockTurbine(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockTurbineRotor").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineValve = new BlockTurbine(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockTurbineValve").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockElectromagneticCoil = new BlockTurbine(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockEMCoil").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockTurbineVent = new BlockTurbine(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockTurbineVent").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockBattery = new BlockBattery(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockBatteryBlock").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockVent = new BlockVent(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockVent").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockEnricher = new BlockEnricher(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockEnricher").setCreativeTab(tabLabStuff));

		labstuffBlocks.add(blockGag = new BlockGag(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockGag"));
		labstuffBlocks.add(blockAcceleratorControlPanel = new BlockAcceleratorControlPanel()
				.setRegistryName("labstuff:blockAcceleratorControlPanel").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockACPGag = new BlockACPGag(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockACPGag"));
		labstuffBlocks.add(blockAcceleratorInterface = new BlockAcceleratorInterface()
				.setRegistryName("labstuff:blockAcceleratorInterface").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorTube = new BlockAcceleratorTube()
				.setRegistryName("labstuff:blockAcceleratorTube").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorDetectorCore = new BlockAcceleratorDetectorCore(Material.IRON)
				.setHardness(3F).setResistance(5F).setRegistryName("labstuff:blockAcceleratorDetectorCore")
				.setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorTrackingDetector = new BlockAcceleratorDetector()
				.setRegistryName("labstuff:blockAcceleratorTrackingDetector").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorSolenoid = new BlockAcceleratorDetector()
				.setRegistryName("labstuff:blockAcceleratorSolenoid").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorElectromagneticCalorimeter = new BlockAcceleratorDetector()
				.setRegistryName("labstuff:blockAcceleratorElectromagneticCalorimeter").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorHadronCalorimeter = new BlockAcceleratorDetector()
				.setRegistryName("labstuff:blockAcceleratorHadronCalorimeter").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorMuonDetector = new BlockAcceleratorDetector()
				.setRegistryName("labstuff:blockAcceleratorMuonDetector").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockAcceleratorPowerInput = new BlockAcceleratorPowerInput()
				.setRegistryName("labstuff:blockAcceleratorPowerInput").setCreativeTab(tabLabStuff));

		labstuffBlocks.add(blockDSCRibbonCable = new BlockDSCRibbonCable(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockDSCRibbonCable").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCCore = new BlockDSCCore(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockDSCCore").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCOS = new BlockDSCOS(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockDSCOS").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCRam = new BlockDSCRam(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockDSCRam").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCDrive = new BlockDSCDrive(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockDSCDrive").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockDSCWorkbench = new BlockDSCBench(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockdscbench").setCreativeTab(tabLabStuff));

		labstuffBlocks.add(blockDLLaptop = new BlockDLLaptop(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockDLLaptop").setCreativeTab(tabLabStuff));
		labstuffBlocks.add(blockGravityManipulater = new BlockGravityManipulater(Material.IRON).setHardness(3F)
				.setResistance(5F).setCreativeTab(tabLabStuff).setRegistryName("labstuff:blockGravityManipulater"));

		labstuffBlocks.add(blockRubberLeaves = new BlockRubberLeaves().setRegistryName("labstuff:blockRubberLeaves"));
		labstuffBlocks.add(blockRubberLog = new BlockRubberLog().setRegistryName("labstuff:blockRubberLog"));
		labstuffBlocks.add(blockRubberSapling = new BlockRubberSapling().setRegistryName("labstuff:blockRubberSapling")
				.setCreativeTab(tabLabStuff).setHardness(0f));

		labstuffBlocks.add(blockMatterCollector = new BlockMatterCollector(Material.IRON).setHardness(3F)
				.setResistance(5F).setRegistryName("labstuff:blockMatterCollector").setCreativeTab(tabLabStuff));

		labstuffBlocks.add(blockSteel = new BlockLabBlock(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockSteelBlock").setCreativeTab(tabLabStuff));

		labstuffBlocks.add(blockWorkbench = new BlockWorkBench(Material.IRON).setHardness(3F).setResistance(5F)
				.setRegistryName("labstuff:blockWorkbench").setCreativeTab(tabLabStuff));

		labstuffBlocks.add(sputnik = new Sputnik().setRegistryName("labstuff:sputnik"));
		labstuffBlocks.add(rd107 = new BlockRocketPart().setRegistryName("labstuff:rd107"));
		labstuffBlocks.add(rd108 = new BlockRocketPart().setRegistryName("labstuff:rd108"));
		labstuffBlocks.add(block8k71p5O = new BlockRocketPart().setRegistryName("labstuff:block8k71p5O"));
		labstuffBlocks.add(fuelTank = new BlockRocketPart().setRegistryName("labstuff:fuelTank"));
		labstuffBlocks.add(rocketStrap = new BlockRocketPart().setRegistryName("labstuff:rocketStrap"));
		labstuffBlocks.add(r7 = new BlockRocketPart().setRegistryName("labstuff:r7"));

		labstuffBlocks.add(radioKit = new RadioKit(Material.IRON).setRegistryName("labstuff:radioKit"));
		labstuffBlocks.add(gimbalIGS = new BlockComputerDevice().setRegistryName("labstuff:gimbalIGS"));

		labstuffBlocks.add(titaniumOre = new BlockLabOre().setRegistryName("labstuff:titaniumOre")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(marble = new BlockLabOre().setRegistryName("labstuff:marble").setCreativeTab(tabLabStuff)
				.setHardness(3F).setResistance(5F));
		labstuffBlocks.add(bauxiteOre = new BlockLabOre().setRegistryName("labstuff:bauxiteOre")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(saltBlock = new BlockLabOre(Material.SAND).setRegistryName("labstuff:saltBlock")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));

		labstuffBlocks
				.add(chloralakizer = new BlockChloralakizer(Material.IRON).setRegistryName("labstuff:chloralakizer")
						.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(
				reactionChamber = new BlockReactionChamber(Material.IRON).setRegistryName("labstuff:reactionChamber")
						.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(alloySmelter = new BlockAlloySmelter(Material.IRON).setRegistryName("labstuff:alloySmelter")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(squeezer = new BlockSqueezer(Material.IRON).setRegistryName("labstuff:squeezer")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(fermenter = new BlockFermenter(Material.IRON).setRegistryName("labstuff:fermenter")
				.setCreativeTab(tabLabStuff).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(fuelingTower = new FuelingTower(Material.ROCK).setRegistryName("labstuff:fuelingTower")
				.setCreativeTab(tabLabStuffSpace).setHardness(3F).setResistance(5F));

		labstuffBlocks.add(ibm650 = new IBM650(Material.IRON).setRegistryName("labstuff:ibm650")
				.setCreativeTab(tabLabStuffComputers).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(
				ibm650PowerUnit = new BlockIBM650PowerUnit(Material.IRON).setRegistryName("labstuff:ibm650PowerUnit")
						.setCreativeTab(tabLabStuffComputers).setHardness(3F).setResistance(5F));
		labstuffBlocks
				.add(ibm650Console = new BlockIBM650Console(Material.IRON).setRegistryName("labstuff:ibm650Console")
						.setCreativeTab(tabLabStuffComputers).setHardness(3F).setResistance(5F));
		labstuffBlocks.add(ibm650Punch = new BlockIBM650Punch(Material.IRON).setRegistryName("labstuff:ibm650Punch")
				.setCreativeTab(tabLabStuffComputers).setHardness(3F).setResistance(5F));

		labstuffBlocks.add(keyPunch = new BlockKeyPunch(Material.IRON).setRegistryName("labstuff:keyPunch")
				.setCreativeTab(tabLabStuffComputers).setHardness(3F).setResistance(5F));

		labstuffBlocks.add(blockLuna = new BlockLuna("luna").setRegistryName("labstuff:luna")
				.setCreativeTab(tabLabStuffSpace).setHardness(2F).setResistance(2.5F));

		labstuffBlocks.add(spaceGlassVanilla = (BlockSpaceGlass) new BlockSpaceGlass("space_glass_vanilla",
				GlassType.VANILLA, GlassFrame.PLAIN, null).setHardness(0.3F).setResistance(3F));
		labstuffBlocks.add(spaceGlassClear = (BlockSpaceGlass) new BlockSpaceGlass("space_glass_clear", GlassType.CLEAR,
				GlassFrame.PLAIN, null).setHardness(0.3F).setResistance(3F));
		labstuffBlocks.add(spaceGlassStrong = (BlockSpaceGlass) new BlockSpaceGlass("space_glass_strong",
				GlassType.STRONG, GlassFrame.PLAIN, null).setHardness(4F).setResistance(35F));
		labstuffBlocks.add(spaceGlassTinVanilla = (BlockSpaceGlass) new BlockSpaceGlass("space_glass_vanilla_tin",
				GlassType.VANILLA, GlassFrame.TIN_DECO, spaceGlassVanilla).setHardness(0.3F).setResistance(4F));
		labstuffBlocks.add(spaceGlassTinClear = (BlockSpaceGlass) new BlockSpaceGlass("space_glass_clear_tin",
				GlassType.CLEAR, GlassFrame.TIN_DECO, spaceGlassClear).setHardness(0.3F).setResistance(4F));
		labstuffBlocks.add(spaceGlassTinStrong = (BlockSpaceGlass) new BlockSpaceGlass("space_glass_strong_tin",
				GlassType.STRONG, GlassFrame.TIN_DECO, spaceGlassStrong).setHardness(4F).setResistance(35F));

		// Items
		labstuffItems.add(itemFiberGlass = new ItemFiberGlass().setRegistryName("labstuff:itemFiberGlass")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(
				itemRubber = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemRubber"));
		labstuffItems.add(itemCopperIngot = new ItemCopperIngot().setRegistryName("labstuff:itemCopperIngot")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemCircuitBoardPlate = new ItemCircuitBoardPlate()
				.setRegistryName("labstuff:itemCircuitBoardPlate").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicDrilledCircuitBoard = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemBasicDrilledCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicEtchedCircuitBoard = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemBasicEtchedCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicCircuitBoard = new ItemCircuitBoard()
				.setRegistryName("labstuff:itemBasicCircuitboard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemBasicCircuitDesign = new ItemCircuitDesign()
				.setRegistryName("labstuff:itemBasicCircuitDesign").setCreativeTab(tabLabStuff));
		labstuffItems.add(
				itemPlastic = new ItemPlastic().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemPlastic"));
		labstuffItems
				.add(itemZinc = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemZinc"));
		labstuffItems.add(
				itemManganese = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemMang"));
		labstuffItems.add(
				itemBattery = new ItemBattery().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemBattery"));
		labstuffItems.add(itemDeadBattery = new ItemBattery().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDeadBattery"));
		labstuffItems.add(
				itemTestTube = new ItemTestTube().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemTestTube"));
		labstuffItems.add(itemHydrogenTestTube = new ItemTestTube().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemHydrogenTestTube"));
		labstuffItems.add(itemOxygenTestTube = new ItemTestTube().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemOxygenTestTube"));
		labstuffItems
				.add(itemSteel = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemSteel"));
		labstuffItems.add(itemInterCircuitDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemIntermediateCircuitDesign"));
		labstuffItems.add(itemInterDrilledCircuitBoard = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemIntermediateDrilledCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemInterEtchedCircuitBoard = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemIntermediateEtchedCircuitBoard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemInterCircuitBoard = new ItemCircuitBoard()
				.setRegistryName("labstuff:itemIntermediateCircuitboard").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconIngot = new ItemLabIngot().setRegistryName("labstuff:itemSiliconIngot")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconCrystalSeed = new ItemSiliconBoulePart().setRegistryName("labstuff:siliconSeed")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSteelRod = new ItemSiliconBoulePart().setRegistryName("labstuff:itemSteelRod")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemRodMountedSiliconSeed = new ItemSiliconBoulePart()
				.setRegistryName("labstuff:itemRodMountedSiliconSeed").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconBoule = new ItemSiliconBoule().setRegistryName("labstuff:itemSiliconBoule")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSaw = new ItemSaw().setRegistryName("labstuff:saw").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSiliconWafer = new ItemSolarPanelPart().setRegistryName("labstuff:itemSiliconWafer")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemSolarCell = new ItemSolarPanelPart().setRegistryName("labstuff:itemSolarCell")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemElectromagnet = new ItemElectromagnet().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemElectromagnet"));
		labstuffItems
				.add(itemWrench = new ItemWrench().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemWrench"));
		labstuffItems.add(itemDiscoveryDrive = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDiscovery"));
		labstuffItems.add(itemDiscoveryDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDiscoveryDriveCircuitDesign"));
		labstuffItems.add(itemDrilledDiscovery = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemDrilledDiscovery").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDiscovery = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemEtchedDiscovery").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCCoreDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDSCCoreDesign"));
		labstuffItems.add(itemDrilledDSCCore = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemDrilledDSCCore").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCCore = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemEtchedDSCCore").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCRamDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDSCRamDesign"));
		labstuffItems.add(itemDrilledDSCRam = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemDrilledDSCRam").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCRam = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDSCRam")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCOSDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDSCOSDesign"));
		labstuffItems.add(itemDrilledDSCOS = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemDrilledDSCOS")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCOS = new ItemPartialCircuitBoard().setRegistryName("labstuff:itemEtchedDSCOS")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCBenchDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDSCBenchDesign"));
		labstuffItems.add(itemDrilledDSCBench = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemDrilledDSCBench").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCBench = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemEtchedDSCBench").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemDSCDriveDesign = new ItemCircuitDesign().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDSCDriveCircuitDesign"));
		labstuffItems.add(itemDrilledDSCDrive = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemDrilledDSCDrive").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemEtchedDSCDrive = new ItemPartialCircuitBoard()
				.setRegistryName("labstuff:itemEtchedDSCDrive").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemGluonDetector = new ItemGluonDetector().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemGluonDetector"));
		labstuffItems.add(itemDPad = new ItemDPad().setRegistryName("labstuff:itemDPad").setCreativeTab(tabLabStuff));
		labstuffItems.add(itemTouchMesh = new ItemDPadPart().setRegistryName("labstuff:itemTouchMesh")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemTouchScreen = new ItemDPadPart().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemTouchScreen"));
		labstuffItems.add(itemUnProgrammedDPad = new ItemDPadPart().setRegistryName("labstuff:itemBiosFailDPad")
				.setCreativeTab(tabLabStuff));
		labstuffItems.add(itemWarpDriveBattery = new ItemWarpDriveBattery().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemWarpDriveBattery"));
		labstuffItems.add(itemEmptyWarpDriveBattery = new ItemEmptyWarpDriveBattery().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemEmptyWarpDriveBattery"));
		labstuffItems.add(itemDiscoveryAntiMatter = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDiscoveryAntiMatter"));
		labstuffItems.add(itemDiscoveryNegativeEnergy = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDiscoveryNegativeEnergy"));
		labstuffItems.add(itemDiscoveryWarp = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDiscoveryWarp"));
		labstuffItems.add(itemAdvancedAccelInterface = new ItemAcceInterfaceUpgrade().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemAccelInterfaceUpgrade"));
		labstuffItems.add(itemDiscoveryQuantum = new ItemDiscoveryDrive().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemDiscoveryQuantum"));

		labstuffItems.add(
				itemZincDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemZincDust"));
		labstuffItems.add(
				itemMangDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemMangDust"));
		labstuffItems.add(
				itemCopperDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemCopperDust"));
		labstuffItems.add(
				itemIronDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemIronDust"));
		labstuffItems.add(
				itemGoldDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:itemGoldDust"));

		labstuffItems.add(itemMatterCollectorCore = new ItemMatterCollectorCore().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemMatterCollectorCore"));
		labstuffItems.add(itemTurbineBlades = new ItemTurbineBlades().setCreativeTab(tabLabStuff)
				.setRegistryName("labstuff:itemTurbineBlades"));

		new MultipartLabStuff();

		itemPartTransmitter = new ItemPartTransmitter().setRegistryName("labstuff:MultipartTransmitter")
				.setUnlocalizedName("labstuff:MultipartTransmitter");
		GameRegistry.register(itemPartTransmitter);

		labstuffItems.add(designRadio = new ItemCircuitDesign().setRegistryName("labstuff:designRadio")
				.setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(drilledRadio = new ItemPartialCircuitBoard().setRegistryName("labstuff:drilledRadio")
				.setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(etchedRadio = new ItemPartialCircuitBoard().setRegistryName("labstuff:etchedRadio")
				.setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(boardRadio = new ItemCircuitBoard().setRegistryName("labstuff:boardRadio")
				.setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(gyroscopicRing = new ItemLabPart().setRegistryName("labstuff:gyroscopicRing")
				.setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(radioAntenna = new ItemLabPart().setRegistryName("labstuff:radioAntenna")
				.setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(designSputnik = new ItemCircuitDesign().setRegistryName("labstuff:designSputnik")
				.setCreativeTab(tabLabStuffSpace));
		labstuffItems.add(etchedSputnik = new ItemPartialCircuitBoard().setRegistryName("labstuff:etchedSputnik")
				.setCreativeTab(tabLabStuffSpace));
		labstuffItems.add(drilledSputnik = new ItemPartialCircuitBoard().setRegistryName("labstuff:drilledSputnik")
				.setCreativeTab(tabLabStuffSpace));
		labstuffItems.add(boardSputnik = new ItemCircuitBoard().setRegistryName("labstuff:boardSputnik")
				.setCreativeTab(tabLabStuffSpace));
		labstuffItems.add(designr7 = new ItemCircuitDesign().setRegistryName("labstuff:designr7")
				.setCreativeTab(tabLabStuffSpace));
		labstuffItems.add(designrd107 = new ItemCircuitDesign().setRegistryName("labstuff:designrd107")
				.setCreativeTab(tabLabStuffSpace));
		labstuffItems.add(designrd108 = new ItemCircuitDesign().setRegistryName("labstuff:designrd108")
				.setCreativeTab(tabLabStuffSpace));
		labstuffItems.add(design8k71p5O = new ItemCircuitDesign().setRegistryName("labstuff:design8k71p5O")
				.setCreativeTab(tabLabStuffSpace));

		labstuffItems
				.add(titanium = new ItemLabIngot().setRegistryName("labstuff:titanium").setCreativeTab(tabLabStuff));
		labstuffItems.add(
				titaniumDust = new ItemDust().setCreativeTab(tabLabStuff).setRegistryName("labstuff:titaniumDust"));
		labstuffItems
				.add(aluminum = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:aluminum"));
		labstuffItems
				.add(magnesium = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:magnesium"));
		labstuffItems.add(amg6t = new ItemLabIngot().setCreativeTab(tabLabStuff).setRegistryName("labstuff:amg6t"));
		labstuffItems.add(salt = new ItemLabIngot().setRegistryName("labstuff:salt").setCreativeTab(tabLabStuff));
		labstuffItems.add(lye = new ItemLabIngot().setRegistryName("labstuff:lye").setCreativeTab(tabLabStuff));
		labstuffItems.add(
				punchCard = new PunchCard().setRegistryName("labstuff:punchCard").setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(punchDeck = new PunchDeck().setRegistryName("labstuff:punchDeck"));

		labstuffItems.add(
				vaccumTube = new Item().setRegistryName("labstuff:vaccumTube").setCreativeTab(tabLabStuffComputers));
		labstuffItems.add(
				drumStorage = new Item().setRegistryName("labstuff:drumStorage").setCreativeTab(tabLabStuffComputers));

		labstuffItems.add(fuelCanister = new ItemFuelCanister("fuel_canister_partial"));

		proxy.preInit();

		for (Block block : labstuffBlocks) {
			if (block.getRegistryName() != null) {
				ItemBlock item = new ItemBlock(block);
				item.setRegistryName(block.getRegistryName());
				block.setUnlocalizedName(block.getRegistryName().toString());
				GameRegistry.register(block);
				GameRegistry.register(item);
				proxy.registerItemModel(item);
			}
		}

		for (Item item : labstuffItems) {
			item.setUnlocalizedName(item.getRegistryName().toString());
			GameRegistry.register(item);
			proxy.registerItemModel(item);
		}
		proxy.registerFluidModels();

		for (final Fluid fluid : FLUIDS) {
			FluidRegistry.addBucketForFluid(fluid);
		}

		Capabilities.registerCapabilities();

	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		/**
		 * Register this mod's fluid {@link Block}s.
		 *
		 * @param event
		 *            The event
		 */
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			for (final IFluidBlock fluidBlock : labstuffFluidBlocks) {
				final Block block = (Block) fluidBlock;
				block.setRegistryName("labstuff", fluidBlock.getFluid().getName());
				block.setUnlocalizedName("labstuff:" + fluidBlock.getFluid().getUnlocalizedName());
				registry.register(block);
			}
		}

		/**
		 * Register this mod's fluid {@link ItemBlock}s.
		 *
		 * @param event
		 *            The event
		 */
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final IFluidBlock fluidBlock : labstuffFluidBlocks) {
				final Block block = (Block) fluidBlock;
				final ItemBlock itemBlock = new ItemBlock(block);
				itemBlock.setRegistryName(block.getRegistryName());
				registry.register(itemBlock);
			}
		}
	}

	private static <T extends Block & IFluidBlock> Fluid createFluid(String name, boolean hasFlowIcon,
			Consumer<Fluid> fluidPropertyApplier, Function<Fluid, T> blockFactory) {
		final String texturePrefix = "labstuff:blocks/";

		final ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
		final ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

		Fluid fluid = new Fluid(name, still, flowing);
		final boolean useOwnFluid = FluidRegistry.registerFluid(fluid);

		if (useOwnFluid) {
			fluidPropertyApplier.accept(fluid);
			labstuffFluidBlocks.add(blockFactory.apply(fluid));
		} else {
			fluid = FluidRegistry.getFluid(name);
		}

		FLUIDS.add(fluid);

		return fluid;
	}

	/** Research **/
	public static Research ORBIT = new Research(null, "Orbit");

	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println("Hi yah we get here.");

		oreDict();

		MinecraftForge.EVENT_BUS.register(this);

		// Proxy junk
		proxy.registerRenders();
		proxy.initMod();

		TransmitterNetworkRegistry.initiate();

		solarSystemSol = new SolarSystem("sol", "milky_way").setMapPosition(new Vector3(0.0F, 0.0F, 0.0F));
		Star starSol = new Star("sol").setParentSolarSystem(solarSystemSol);
		starSol.setBodyIcon(new ResourceLocation("labstuff:textures/celestialBodies/sol.png"));
		solarSystemSol.setMainStar(starSol);

		planetOverworld = (Planet) new Planet("overworld").setParentSolarSystem(solarSystemSol)
				.setRingColorRGB(0.1F, 0.9F, 0.6F).setPhaseShift(0.0F);
		planetOverworld.setBodyIcon(new ResourceLocation("labstuff:textures/celestialbodies/earth.png"));
		planetOverworld.setDimensionInfo(ConfigManagerCore.idDimensionOverworld, WorldProvider.class, false);
		planetOverworld.atmosphereComponent(IAtmosphericGas.NITROGEN).atmosphereComponent(IAtmosphericGas.OXYGEN)
				.atmosphereComponent(IAtmosphericGas.ARGON).atmosphereComponent(IAtmosphericGas.WATER);
		planetOverworld.addChecklistKeys("equipParachute");

		moonLuna = (Moon) new Moon("luna").setParentPlanet(planetOverworld).setRelativeSize(0.2667F)
				.setRelativeDistanceFromCenter(new CelestialBody.ScalableDistance(13F, 13F))
				.setRelativeOrbitTime(1 / 0.01F);
		moonLuna.setDimensionInfo(ConfigManagerCore.idDimensionMoon, WorldProviderLuna.class);
		moonLuna.setBodyIcon(new ResourceLocation("labstuff:textures/celestialbodies/luna.png"));
		moonLuna.addChecklistKeys("equipOxygenSuit");

		CompatibilityManager.checkForCompatibleMods();

		GalaxyRegistry.registerSolarSystem(solarSystemSol);
		GalaxyRegistry.registerPlanet(planetOverworld);
		GalaxyRegistry.registerMoon(moonLuna);
		LabStuffRegistry.registerTeleportType(WorldProviderSurface.class, new TeleportTypeOverworld());
		LabStuffRegistry.registerTeleportType(WorldProviderOverworldOrbit.class, new TeleportTypeOrbit());
		LabStuffRegistry.registerTeleportType(WorldProviderLuna.class, new TeleportTypeLuna());

		// Crafting Recipes
		Recipes.registerShaplessCrafting();
		Recipes.registerShapedCrafting();
		// Smelting recipes
		Recipes.registerSmelting();
		// Circuit Deisgns
		Recipes.addCircuitDesign("Basic", itemBasicCircuitDesign);
		Recipes.addCircuitDesign("Intermediate", itemInterCircuitDesign);
		// CircuitMaker
		Recipes.addCircuitCreation("Basic", itemBasicEtchedCircuitBoard, itemBasicDrilledCircuitBoard,
				itemBasicCircuitBoard);
		Recipes.addCircuitCreation("Intermediate", itemInterEtchedCircuitBoard, itemInterDrilledCircuitBoard,
				itemInterCircuitBoard);

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
		Recipes.addCircuitDesign("Radio", designRadio);
		Recipes.addCircuitCreation("Radio", etchedRadio, drilledRadio, boardRadio);
		Recipes.addCircuitDesign("r7", designr7);
		Recipes.addCircuitDesign("rd107", designrd107);
		Recipes.addCircuitDesign("rd108", designrd108);
		Recipes.addCircuitDesign("8k71p5O", design8k71p5O);
		Recipes.addCircuitDesign("Sputnik", designSputnik);
		Recipes.addCircuitCreation("Sputnik", etchedSputnik, drilledSputnik, boardSputnik);

		// Charges
		Recipes.addCharge(itemDeadBattery, itemBattery, 50);

		Recipes.addAccelDiscovery(null, new ItemStack(itemDiscoveryAntiMatter));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter),
				new ItemStack(itemDiscoveryNegativeEnergy));
		Recipes.addAccelDiscovery(Recipes.getDiscovFromDrive(itemDiscoveryNegativeEnergy),
				new ItemStack(itemDiscoveryWarp));
		Recipes.addAccelDiscovery(null, new ItemStack(itemDiscoveryQuantum));
		Recipes.addDiscovItem(new ItemStack(itemAdvancedAccelInterface, 1),
				new ItemStack(LabStuffMain.itemElectromagnet, 1), new ItemStack(LabStuffMain.itemCopperIngot, 2),
				new ItemStack(Items.IRON_INGOT, 5), Recipes.getDiscovFromDrive(itemDiscoveryAntiMatter),
				"Advanced Accelerator Interface Upgrade");
		Recipes.addDiscovItem(new ItemStack(itemEmptyWarpDriveBattery, 1),
				new ItemStack(LabStuffMain.itemElectromagnet, 1), new ItemStack(LabStuffMain.itemCopperIngot, 2),
				new ItemStack(Items.IRON_INGOT, 5), Recipes.getDiscovFromDrive(itemDiscoveryWarp),
				"Warp Drive Battery (Empty)");
		Recipes.addDiscovItem(new ItemStack(blockGravityManipulater), new ItemStack(Items.IRON_INGOT, 8),
				new ItemStack(LabStuffMain.itemElectromagnet), new ItemStack(itemTouchScreen),
				Recipes.getDiscovFromDrive(itemDiscoveryWarp), "Gravity Manipulater");

		// Research
		Recipes.addResearch(ORBIT);

		/*
		 * Recipes.addResearchItem(0, null, new ItemStack[]{
		 * null,null,null,null,null, null,null,null,null,null,
		 * null,null,null,null,null, null,null,null,null,null,
		 * null,null,null,null,null}, null);
		 */

		Recipes.addResearchItem(0, null,
				new ItemStack[] { null, new ItemStack(itemCopperIngot), new ItemStack(itemPartTransmitter, 1, 0),
						new ItemStack(itemCopperIngot), null, null, null, new ItemStack(itemPartTransmitter, 1, 0),
						null, null, null, new ItemStack(itemCopperIngot), new ItemStack(itemPartTransmitter, 1, 0),
						new ItemStack(itemCopperIngot), null, null, null, new ItemStack(itemPartTransmitter, 1, 0),
						null, null, null, new ItemStack(itemPartTransmitter, 1, 0),
						new ItemStack(itemPartTransmitter, 1, 0), new ItemStack(itemPartTransmitter, 1, 0), null },
				new ItemStack(radioAntenna));
		Recipes.addResearchItem(0, null,
				new ItemStack[] { null, new ItemStack(itemSteel), new ItemStack(Items.IRON_INGOT, 3),
						new ItemStack(itemSteel), null, new ItemStack(itemSteel), null, null, null,
						new ItemStack(itemSteel), new ItemStack(Items.IRON_INGOT, 3), null, null, null,
						new ItemStack(Items.IRON_INGOT, 3), new ItemStack(itemSteel), null, null, null,
						new ItemStack(itemSteel), null, new ItemStack(itemSteel), new ItemStack(Items.IRON_INGOT, 3),
						new ItemStack(itemSteel), null },
				new ItemStack(gyroscopicRing));
		Recipes.addResearchItem(20, null,
				new ItemStack[] { new ItemStack(Items.IRON_INGOT), new ItemStack(itemSteel), new ItemStack(itemSteel),
						new ItemStack(itemSteel), new ItemStack(radioAntenna), new ItemStack(itemSteel),
						new ItemStack(itemSteel), new ItemStack(Blocks.GLASS_PANE), new ItemStack(itemSteel),
						new ItemStack(itemSteel), new ItemStack(itemSteel), new ItemStack(Items.IRON_INGOT),
						new ItemStack(boardRadio), new ItemStack(Items.IRON_INGOT), new ItemStack(itemSteel),
						new ItemStack(itemSteel), new ItemStack(itemSteel), new ItemStack(itemSteel),
						new ItemStack(itemSteel), new ItemStack(itemSteel), null, null, null, null, null },
				new ItemStack(radioKit));
		Recipes.addResearchItem(0, null,
				new ItemStack[] { new ItemStack(itemSteel), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(blockReservoir), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(itemSteel), new ItemStack(itemSteel), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(blockReservoir), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(itemSteel), new ItemStack(itemSteel), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(itemPartTransmitter, 1, 1), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(itemSteel), new ItemStack(itemSteel), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(blockReservoir), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(itemSteel), new ItemStack(itemSteel), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(blockReservoir), new ItemStack(itemPartTransmitter, 1, 1),
						new ItemStack(itemSteel) },
				new ItemStack(fuelTank));
		Recipes.addResearchItem(0, null,
				new ItemStack[] { null, new ItemStack(amg6t), new ItemStack(Items.IRON_INGOT), new ItemStack(amg6t),
						null, null, new ItemStack(amg6t), new ItemStack(itemCopperIngot), new ItemStack(amg6t), null,
						null, new ItemStack(amg6t), new ItemStack(design8k71p5O), new ItemStack(amg6t), null, null,
						new ItemStack(amg6t), new ItemStack(itemCopperIngot), new ItemStack(amg6t), null, null,
						new ItemStack(amg6t), new ItemStack(Items.IRON_INGOT), new ItemStack(amg6t), null },
				new ItemStack(block8k71p5O));
		Recipes.addResearchItem(0, null,
				new ItemStack[] { null, new ItemStack(amg6t), new ItemStack(Items.IRON_INGOT), new ItemStack(amg6t),
						null, null, new ItemStack(amg6t), new ItemStack(itemCopperIngot), new ItemStack(amg6t), null,
						null, new ItemStack(amg6t), new ItemStack(designr7), new ItemStack(amg6t), null, null,
						new ItemStack(amg6t), new ItemStack(itemCopperIngot), new ItemStack(amg6t), null, null,
						new ItemStack(amg6t), new ItemStack(Items.IRON_INGOT), new ItemStack(amg6t), null },
				new ItemStack(r7));
		Recipes.addResearchItem(0, null,
				new ItemStack[] { null, null, new ItemStack(itemPartTransmitter, 1, 1), null, null, null,
						new ItemStack(amg6t), new ItemStack(itemCopperIngot), new ItemStack(amg6t), null, null,
						new ItemStack(amg6t), new ItemStack(designrd107), new ItemStack(amg6t), null,
						new ItemStack(amg6t), new ItemStack(itemCopperIngot), null, new ItemStack(itemCopperIngot),
						new ItemStack(amg6t), null, null, null, null, null },
				new ItemStack(rd107));
		Recipes.addResearchItem(0, null,
				new ItemStack[] { new ItemStack(amg6t), new ItemStack(amg6t), new ItemStack(amg6t),
						new ItemStack(amg6t), new ItemStack(amg6t), new ItemStack(amg6t), new ItemStack(radioKit),
						new ItemStack(boardSputnik), new ItemStack(itemInterCircuitBoard), new ItemStack(amg6t),
						new ItemStack(amg6t), new ItemStack(amg6t), new ItemStack(amg6t), new ItemStack(amg6t),
						new ItemStack(amg6t), new ItemStack(radioAntenna), null, null, null,
						new ItemStack(radioAntenna), new ItemStack(radioAntenna), null, null, null,
						new ItemStack(radioAntenna) },
				new ItemStack(sputnik));
		Recipes.addResearchItem(30, null,
				new ItemStack[] { new ItemStack(Blocks.GLASS_PANE), new ItemStack(Blocks.GLASS_PANE),
						new ItemStack(Blocks.GLASS_PANE), new ItemStack(Blocks.GLASS_PANE),
						new ItemStack(Blocks.GLASS_PANE), new ItemStack(Blocks.GLASS_PANE), null,
						new ItemStack(itemCopperIngot, 2), null, new ItemStack(Blocks.GLASS_PANE),
						new ItemStack(Blocks.GLASS_PANE), null, new ItemStack(itemCopperIngot, 2), null,
						new ItemStack(Blocks.GLASS_PANE), new ItemStack(Blocks.GLASS_PANE), null,
						new ItemStack(itemCopperIngot, 2), null, new ItemStack(Blocks.GLASS_PANE),
						new ItemStack(itemCopperIngot), new ItemStack(itemCopperIngot), new ItemStack(itemCopperIngot),
						new ItemStack(itemCopperIngot), new ItemStack(itemCopperIngot) },
				new ItemStack(vaccumTube));
		Recipes.addResearchItem(30, null, new ItemStack[] { new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), null, null, null,
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(itemSteel, 2),
				new ItemStack(itemPartTransmitter, 1, 3), null, new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.IRON_INGOT), null, null, null, new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT) }, new ItemStack(drumStorage));

		Recipes.addReaction(null, null, null, null, new ItemStack(bauxiteOre), new ItemStack(lye),
				new ItemStack(aluminum), null, 50);
		Recipes.addReaction(null, null, null, null, new ItemStack(marble, 2), new ItemStack(itemSiliconIngot),
				new ItemStack(magnesium, 2), null, 300);
		Recipes.addReaction(new FluidStack(ethanol, 500), new FluidStack(plantoil, 500), new FluidStack(bioDiesel, 500),
				null, null, null, null, null, 600);
		Recipes.addReaction(null, null, new FluidStack(sulfolane, 500), null, new ItemStack(Items.COAL, 15),
				new ItemStack(itemOxygenTestTube, 4), null, null, 100);
		Recipes.addReaction(new FluidStack(LO2, 4000), null, new FluidStack(sulfolane, 1000), null,
				new ItemStack(Items.COAL, 15), null, null, null, 100);
		Recipes.addReaction(null, null, new FluidStack(LO2, 1000), null, new ItemStack(itemOxygenTestTube, 1), null,
				null, null, 100);
		Recipes.addReaction(new FluidStack(bioDiesel, 2000), new FluidStack(sulfolane, 1000),
				new FluidStack(kerosene, 2000), null, null, null, null, null, 250);

		Recipes.addAlloy(new ItemStack(magnesium), new ItemStack(titanium), new ItemStack(aluminum),
				new ItemStack(amg6t));

		Recipes.addSqueeze(new ItemStack(Items.BEETROOT_SEEDS), null, new FluidStack(plantoil, 80));
		Recipes.addSqueeze(new ItemStack(Items.PUMPKIN_SEEDS), null, new FluidStack(plantoil, 80));
		Recipes.addSqueeze(new ItemStack(Items.MELON_SEEDS), null, new FluidStack(plantoil, 80));
		Recipes.addSqueeze(new ItemStack(Items.WHEAT_SEEDS), null, new FluidStack(plantoil, 80));

		Recipes.addFerment(new ItemStack(Items.APPLE), null, new FluidStack(ethanol, 80));
		Recipes.addFerment(new ItemStack(Items.REEDS), null, new FluidStack(ethanol, 80));
		Recipes.addFerment(new ItemStack(Items.MELON), null, new FluidStack(ethanol, 80));

		Recipes.addPunchProgram("Sputnik", "requires radio_kit;" + "requires fueling_tower;" + "run fueling_tower,2;");

		// Tile Entities
		GameRegistry.registerTileEntity(TileEntityCircuitDesignTable.class, "TileEntityCircuitDesignTable");
		GameRegistry.registerTileEntity(TileEntityCircuitMaker.class, "TileEntityCircuitMaker");
		GameRegistry.registerTileEntity(TileEntityElectrifier.class, "TileEntityElectrifier");
		GameRegistry.registerTileEntity(TileEntityElectronGrabber.class, "TileEntityElectronGrabber");
		GameRegistry.registerTileEntity(TileEntityGasChamberPort.class, "TileEntityGasChamberPort");
		GameRegistry.registerTileEntity(TileEntityWindTurbine.class, "TileEntityWindTurbine");
		GameRegistry.registerTileEntity(TileEntityPowerFurnace.class, "TileEntityPowerFurnace");
		GameRegistry.registerTileEntity(TileEntitySolarPanel.class, "TileEntitySolarPanel");
		GameRegistry.registerTileEntity(TileEntityRFToLV.class, "TileEntityRFToLV");
		GameRegistry.registerTileEntity(TileEntityLVToRF.class, "TileEntityLVToRF");
		GameRegistry.registerTileEntity(TileEntityCzo.class, "TileEntityCzo");
		GameRegistry.registerTileEntity(TileEntityReservoir.class, "Reservoir");
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
		GameRegistry.registerTileEntity(TileEntityWorkbench.class, "TileEntityWorkbench");
		GameRegistry.registerTileEntity(TESputnik.class, "Sputnik");
		GameRegistry.registerTileEntity(TileEntityRocket.class, "Rocket");
		GameRegistry.registerTileEntity(Chloralakizer.class, "Chloralakizer");
		GameRegistry.registerTileEntity(ReactionChamber.class, "Reaction Chamber");
		GameRegistry.registerTileEntity(AlloySmelter.class, "AlloySmelter");
		GameRegistry.registerTileEntity(Squeezer.class, "Squeezer");
		GameRegistry.registerTileEntity(Fermenter.class, "Fermenter");
		GameRegistry.registerTileEntity(IBM650PowerUnit.class, "IBM650PowerUnit");
		GameRegistry.registerTileEntity(IBM650Console.class, "IBM650Console");
		GameRegistry.registerTileEntity(IBM650Punch.class, "IBM650Punch");
		GameRegistry.registerTileEntity(KeyPunch.class, "KeyPunch");
		GameRegistry.registerTileEntity(TEFuelingTower.class, "Fueling_Tower");
		GameRegistry.registerTileEntity(TERadioKit.class, "Radio_Kit");

		// Packets
		packetPipeline.initalise();
		packetPipeline.registerPacket(PacketCircuitDesignTable.class);
		packetPipeline.registerPacket(PacketCircuitMaker.class);
		packetPipeline.registerPacket(PacketACP.class);
		packetPipeline.registerPacket(PacketDSCBench.class);
		packetPipeline.registerPacket(PacketDLLaptopUSB.class);
		packetPipeline.registerPacket(PacketDLLaptopWeb.class);
		packetPipeline.registerPacket(PacketGravity.class);
		packetPipeline.registerPacket(PacketMatterCollector.class);
		packetPipeline.registerPacket(WorkbenchPacket.class);
		packetPipeline.registerPacket(IBM650ConsolePacket.class);
		packetPipeline.registerPacket(KeyPunchPacket.class);
		packetPipeline.registerPacket(GuiChangePacket.class);
		packetPipeline.registerPacket(PacketSimple.class);
		packetPipeline.registerPacket(PacketDynamic.class);
		packetPipeline.registerPacket(PacketEntityUpdate.class);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		GameRegistry.registerWorldGenerator(new LabStuffOreGen(), 0);
		GameRegistry.registerWorldGenerator(new TreeManager(), 1);
	}

	@SubscribeEvent
	public void onClientTickUpdate(ClientTickUpdate event) {
		try {
			if (event.operation == 0) {
				ClientTickHandler.tickingSet.remove(event.network);
			} else {
				ClientTickHandler.tickingSet.add(event.network);
			}
		} catch (Exception e) {
		}
	}

	@SubscribeEvent
	public void onEnergyTransferred(EnergyTransferEvent event) {
		try {
			packetPipeline.sendToReceivers(
					new TransmitterUpdateMessage(PacketType.ENERGY,
							event.energyNetwork.transmitters.iterator().next().coord(), event.power),
					event.energyNetwork.getPacketRange());
		} catch (Exception e) {
		}
	}

	@SubscribeEvent
	public void onLiquidTransferred(FluidTransferEvent event) {
		try {
			packetPipeline.sendToReceivers(new TransmitterUpdateMessage(PacketType.FLUID,
					event.fluidNetwork.transmitters.iterator().next().coord(), event.fluidType, event.didTransfer),
					event.fluidNetwork.getPacketRange());
		} catch (Exception e) {
		}
	}

	@SubscribeEvent
	public void onPlasmaTransferred(PlasmaTransferEvent event) {
		try {
			packetPipeline.sendToReceivers(
					new TransmitterUpdateMessage(PacketType.PLASMA,
							event.plasmaNetwork.transmitters.iterator().next().coord(), event.plasma),
					event.plasmaNetwork.getPacketRange());
		} catch (Exception e) {
		}
	}

	@SubscribeEvent
	public void onTransmittersAddedEvent(TransmittersAddedEvent event) {
		try {
			packetPipeline.sendToReceivers(new TransmitterUpdateMessage(PacketType.UPDATE,
					event.network.transmitters.iterator().next().coord(), event.newNetwork, event.newTransmitters),
					event.network.getPacketRange());
		} catch (Exception e) {
		}
	}

	@SubscribeEvent
	public void onNetworkClientRequest(NetworkClientRequest event) {
		try {
			packetPipeline.sendToServer(new DataRequestMessage(Coord4D.get(event.tileEntity)));
		} catch (Exception e) {
		}
	}

	@SubscribeEvent
	public void onEntityConstructed(AttachCapabilitiesEvent<Entity> evt) {
		if (!(evt.getObject() instanceof EntityPlayer))
			return;

		evt.addCapability(new ResourceLocation("labstuff:IResearcher"), new ICapabilitySerializable<NBTBase>() {
			IResearcher inst = Capabilities.RESEARCHER_CAPABILITY.getDefaultInstance();

			@Override
			public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
				// TODO Auto-generated method stub
				return capability == Capabilities.RESEARCHER_CAPABILITY;
			}

			@Override
			public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
				// TODO Auto-generated method stub
				return capability == Capabilities.RESEARCHER_CAPABILITY
						? Capabilities.RESEARCHER_CAPABILITY.<T>cast(inst) : null;
			}

			@Override
			public NBTBase serializeNBT() {
				// TODO Auto-generated method stub
				return Capabilities.RESEARCHER_CAPABILITY.getStorage().writeNBT(Capabilities.RESEARCHER_CAPABILITY,
						inst, null);
			}

			@Override
			public void deserializeNBT(NBTBase nbt) {
				Capabilities.RESEARCHER_CAPABILITY.getStorage().readNBT(Capabilities.RESEARCHER_CAPABILITY, inst, null,
						nbt);

			}

		});

	}

	private void oreDict() {
		OreDictionary.registerOre("oreCopper", new ItemStack(blockCopperOre));
		OreDictionary.registerOre("oreManganese", new ItemStack(blockMangOre));
		OreDictionary.registerOre("oreZinc", new ItemStack(blockZincOre));
		OreDictionary.registerOre("ingotCopper", new ItemStack(itemCopperIngot));
		OreDictionary.registerOre("ingotManganese", new ItemStack(itemManganese));
		OreDictionary.registerOre("ingotZinc", new ItemStack(itemZinc));
		OreDictionary.registerOre("circuitBasic", new ItemStack(itemBasicCircuitBoard));
		OreDictionary.registerOre("ingotSteel", new ItemStack(itemSteel));
		OreDictionary.registerOre("circuitIntermediate", itemInterCircuitBoard);
		OreDictionary.registerOre("dustZinc", new ItemStack(itemZincDust));
		OreDictionary.registerOre("dustManganese", new ItemStack(itemMangDust));
		OreDictionary.registerOre("dustCopper", new ItemStack(itemCopperDust));
		OreDictionary.registerOre("dustIron", new ItemStack(itemIronDust));
		OreDictionary.registerOre("dustGold", new ItemStack(itemGoldDust));
		OreDictionary.registerOre("blockSteel", new ItemStack(blockSteel));
		OreDictionary.registerOre("stickSteel", new ItemStack(itemSteelRod));
		OreDictionary.registerOre("dustTitanium", titaniumDust);
		OreDictionary.registerOre("oreTitanium", titaniumOre);
		OreDictionary.registerOre("ingotTitanium", titanium);
		OreDictionary.registerOre("ingotMagnesium", magnesium);
		OreDictionary.registerOre("ingotAluminum", aluminum);
		OreDictionary.registerOre("blockSalt", saltBlock);
		OreDictionary.registerOre("itemSalt", salt);
		OreDictionary.registerOre("dustSalt", salt);
		OreDictionary.registerOre("blockMarble", marble);
	}

	@EventHandler
	public void starting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDimTele());
	}

	@EventHandler
	public void postLoad(FMLPostInitializationEvent event) {
		packetPipeline.postInitialise();

		try {
			jpgWriter = ImageIO.getImageWritersByFormatName("jpg").next();
			writeParam = jpgWriter.getDefaultWriteParam();
			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			writeParam.setCompressionQuality(1.0f);
			enableJPEG = true;
		} catch (UnsatisfiedLinkError e) {
			LSLog.severe("Error initialising JPEG compressor - this is likely caused by OpenJDK");
			e.printStackTrace();
		}

		// Enrichments
		/** So placed because other mods load on their own time **/
		Recipes.addEnrichment(get(blockRubberLog), itemRubber);
		Recipes.addEnrichment(get(blockMangOre), itemMangDust);
		Recipes.addEnrichment(get(blockCopperOre), itemCopperDust);
		Recipes.addEnrichment(get(blockZincOre), itemZincDust);
		Recipes.addEnrichment(get(Blocks.IRON_ORE), itemIronDust);
		Recipes.addEnrichment(get(Blocks.GOLD_ORE), itemGoldDust);
		Recipes.addEnrichment(get(titaniumOre), titaniumDust);
		// for(String ore : OreDictionary.getOreNames()){
		// if(ore.length() > 3 && OreDictionary.getOres(ore, false).size() > 0
		// && Recipes.getEnrichmentFromInput(OreDictionary.getOres(ore,
		// false).get(0)) == null){
		// if(ore.substring(0,3).equals("ore")){
		// String output = "dust"+ore.substring(3);
		// Recipes.addEnrichments(OreDictionary.getOres(ore, false),
		// OreDictionary.getOres(output, false));
		// }
		// }
		// }
	}

	public static Item get(Block block) {
		// TODO Auto-generated method stub
		return Item.getItemFromBlock(block);
	}

	public static void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object... extraData) {
		Minecraft mc = FMLClientHandler.instance().getClient();

		if (mc != null && mc.getRenderViewEntity() != null && mc.effectRenderer != null) {
			final double dPosX = mc.getRenderViewEntity().posX - position.x;
			final double dPosY = mc.getRenderViewEntity().posY - position.y;
			final double dPosZ = mc.getRenderViewEntity().posZ - position.z;
			Particle particle = null;
			final double maxDistSqrd = 64.0D;

			if (dPosX * dPosX + dPosY * dPosY + dPosZ * dPosZ < maxDistSqrd * maxDistSqrd) {
				if (particleID.equals("sludgeDrip")) {
					// particle = new EntityDropParticleFX(mc.theWorld,
					// position.x, position.y, position.z, Material.WATER); TODO
				}
			}

			if (particle != null) {
				mc.effectRenderer.addEffect(particle);
			}
		}
	}

}
