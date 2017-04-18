package keegan.labstuff.config;

import com.google.common.primitives.Ints;

import keegan.labstuff.util.BlockTuple;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

public class ConfigManagerCore
{
    static Configuration config;

    // GAME CONTROL
    public static boolean forceOverworldRespawn;
    public static boolean disableUpdateCheck;
    public static boolean enableSealerEdgeChecks;

    // DIMENSIONS
    public static int idDimensionOverworld;
    public static int idDimensionOverworldOrbit;
    public static int idDimensionOverworldOrbitStatic;
    public static int idDimensionMoon;
    public static boolean disableBiomeTypeRegistrations;
    public static int[] staticLoadDimensions = {};
    public static int[] disableRocketLaunchDimensions = { -1, 1 };
    public static boolean disableRocketLaunchAllNonls;
    public static int otherPlanetWorldBorders = 0;

    
    // GENERAL
    public static boolean launchControllerChunkLoad;

    // CLIENT / VISUAL FX
    public static boolean disableSpaceshipParticles;
    public static boolean disableVehicleCameraChanges;
    public static boolean oxygenIndicatorLeft;
    public static boolean oxygenIndicatorBottom;

    //DIFFICULTY
    public static int suffocationCooldown;
    public static int suffocationDamage;
    public static int rocketFuelFactor;
    public static double meteorSpawnMod;
    public static boolean meteorBlockDamageEnabled;
    public static boolean disableSpaceshipGrief;
    public static double spaceStationEnergyScalar;


    //COMPATIBILITY
    public static String[] sealableIDs = {};
    public static String[] detectableIDs = {};

    //KEYBOARD AND MOUSE
    public static String keyOverrideMap;
    public static String keyOverrideFuelLevel;
    public static String keyOverrideToggleAdvGoggles;
    public static int keyOverrideMapI;
    public static int keyOverrideFuelLevelI;
    public static int keyOverrideToggleAdvGogglesI;
    public static float mapMouseScrollSensitivity;
    public static boolean invertMapMouseScroll;

    public static ArrayList<Object> clientSave = null;

    public static void initialize(File file)
    {
        ConfigManagerCore.config = new Configuration(file);
        ConfigManagerCore.syncConfig(true);
    }

    public static void forceSave()
    {
        ConfigManagerCore.config.save();
    }

    public static void syncConfig(boolean load)
    {
        List<String> propOrder = new ArrayList<String>();

        try
        {
            Property prop;

            if (!config.isChild)
            {
                if (load)
                {
                    config.load();
                }
            }


            prop = config.get("dimensions", "idDimensionOverworld", 0);
            prop.setComment("Dimension ID for the Overworld (as seen in the Celestial Map)");
            prop.setLanguageKey("ls.configgui.idDimensionOverworld").setRequiresMcRestart(true);
            idDimensionOverworld = prop.getInt();
            propOrder.add(prop.getName());

            prop = config.get("dimensions", "idDimensionMoon", -28);
            prop.setComment("Dimension ID for the Moon");
            prop.setLanguageKey("ls.configgui.idDimensionMoon").setRequiresMcRestart(true);
            idDimensionMoon = prop.getInt();
            propOrder.add(prop.getName());

            prop = config.get("dimensions", "idDimensionOverworldOrbit", -27);
            prop.setComment("WorldProvider ID for Overworld Space Stations (advanced: do not change unless you have conflicts)");
            prop.setLanguageKey("ls.configgui.idDimensionOverworldOrbit").setRequiresMcRestart(true);
            idDimensionOverworldOrbit = prop.getInt();
            propOrder.add(prop.getName());

            prop = config.get("dimensions", "idDimensionOverworldOrbitStatic", -26);
            prop.setComment("WorldProvider ID for Static Space Stations (advanced: do not change unless you have conflicts)");
            prop.setLanguageKey("ls.configgui.idDimensionOverworldOrbitStatic").setRequiresMcRestart(true);
            idDimensionOverworldOrbitStatic = prop.getInt();
            propOrder.add(prop.getName());

            propOrder.add(prop.getName());

            prop = config.get("dimensions", "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
            prop.setComment("IDs to load at startup, and keep loaded until server stops. Can be added via /lskeeploaded");
            prop.setLanguageKey("ls.configgui.staticLoadedDimensions");
            staticLoadDimensions = prop.getIntList();
            propOrder.add(prop.getName());

            prop = config.get("dimensions", "Dimensions where rockets cannot launch", new String[] { "1", "-1" });
            prop.setComment("IDs of dimensions where rockets should not launch - this should always include the Nether.");
            prop.setLanguageKey("ls.configgui.rocketDisabledDimensions");
            disableRocketLaunchDimensions = prop.getIntList();
            disableRocketLaunchAllNonls = searchAsterisk(prop.getStringList());
            propOrder.add(prop.getName());


            prop = config.get("dimensions", "World border for landing location on other planets (Moon, Mars, etc)", false);
            prop.setComment("Set this to 0 for no borders (default).  If set to e.g. 2000, players will land on the Moon inside the x,z range -2000 to 2000.)");
            prop.setLanguageKey("ls.configgui.planet_worldborders");
            otherPlanetWorldBorders = prop.getInt(0);
            propOrder.add(prop.getName());

            prop = config.get("general", "Force Overworld Spawn", false);
            prop.setComment("By default, you will respawn on galacticraft dimensions if you die. If you set this to true, you will respawn back on earth.");
            prop.setLanguageKey("ls.configgui.forceOverworldRespawn");
            forceOverworldRespawn = prop.getBoolean(false);
            propOrder.add(prop.getName());
            
//General
            prop = config.get("general", "launchControllerChunkLoad", true);
            prop.setComment("Whether or not the launch controller acts as a chunk loader. Will cause issues if disabled!");
            prop.setLanguageKey("ls.configgui.launchControllerChunkLoad");
            launchControllerChunkLoad = prop.getBoolean(true);

//Client side


            prop = config.get("general", "Disable Spaceship Particles", false);
            prop.setComment("If you have FPS problems, setting this to true will help if rocket particles are in your sights");
            prop.setLanguageKey("ls.configgui.disableSpaceshipParticles");
            disableSpaceshipParticles = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get("general", "Disable Vehicle Third-Person and Zoom", false);
            prop.setComment("If you're using this mod in virtual reality, or if you don't want the camera changes when entering a Galacticraft vehicle, set this to true.");
            prop.setLanguageKey("ls.configgui.disableVehicleCameraChanges");
            disableVehicleCameraChanges = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get("general", "Minimap Left", false);
            prop.setComment("If true, this will move the Oxygen Indicator to the left side. You can combine this with \"Minimap Bottom\"");
            prop.setLanguageKey("ls.configgui.oxygenIndicatorLeft");
            oxygenIndicatorLeft = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get("general", "Minimap Bottom", false);
            prop.setComment("If true, this will move the Oxygen Indicator to the bottom. You can combine this with \"Minimap Left\"");
            prop.setLanguageKey("ls.configgui.oxygenIndicatorBottom");
            oxygenIndicatorBottom = prop.getBoolean(false);
            propOrder.add(prop.getName());


//Server side

            prop = config.get("general", "Space Station Solar Energy Multiplier", 2.0);
            prop.setComment("Solar panels will work (default 2x) more effective on space stations.");
            prop.setLanguageKey("ls.configgui.spaceStationEnergyScalar");
            spaceStationEnergyScalar = prop.getDouble(2.0);
            propOrder.add(prop.getName());

            try
            {
                prop = config.get("general", "External Sealable IDs", new String[] { GameData.getBlockRegistry().getNameForObject(Blocks.GLASS_PANE) + ":0" });
                prop.setComment("List non-opaque blocks from other mods (for example, special types of glass) that the Oxygen Sealer should recognize as solid seals. Format is BlockName or BlockName:metadata");
                prop.setLanguageKey("ls.configgui.sealableIDs").setRequiresMcRestart(true);
                sealableIDs = prop.getStringList();
                propOrder.add(prop.getName());
            }
            catch (Exception e)
            {
            }

            prop = config.get("general", "External Detectable IDs", new String[] {
                    ((ResourceLocation) GameData.getBlockRegistry().getNameForObject(Blocks.COAL_ORE)).getResourcePath(),
                    ((ResourceLocation) GameData.getBlockRegistry().getNameForObject(Blocks.DIAMOND_ORE)).getResourcePath(),
                    ((ResourceLocation) GameData.getBlockRegistry().getNameForObject(Blocks.GOLD_ORE)).getResourcePath(),
                    ((ResourceLocation) GameData.getBlockRegistry().getNameForObject(Blocks.IRON_ORE)).getResourcePath(),
                    ((ResourceLocation) GameData.getBlockRegistry().getNameForObject(Blocks.LAPIS_ORE)).getResourcePath(),
                    ((ResourceLocation) GameData.getBlockRegistry().getNameForObject(Blocks.REDSTONE_ORE)).getResourcePath(),
                    ((ResourceLocation) GameData.getBlockRegistry().getNameForObject(Blocks.LIT_REDSTONE_ORE)).getResourcePath() });
            prop.setComment("List blocks from other mods that the Sensor Glasses should recognize as solid blocks. Format is BlockName or BlockName:metadata.");
            prop.setLanguageKey("ls.configgui.detectableIDs").setRequiresMcRestart(true);
            detectableIDs = prop.getStringList();
            propOrder.add(prop.getName());

            prop = config.get("general", "Suffocation Cooldown", 100);
            prop.setComment("Lower/Raise this value to change time between suffocation damage ticks");
            prop.setLanguageKey("ls.configgui.suffocationCooldown");
            suffocationCooldown = prop.getInt(100);
            propOrder.add(prop.getName());

            prop = config.get("general", "Suffocation Damage", 2);
            prop.setComment("Change this value to modify the damage taken per suffocation tick");
            prop.setLanguageKey("ls.configgui.suffocationDamage");
            suffocationDamage = prop.getInt(2);
            propOrder.add(prop.getName());

            prop = config.get("general", "Enable Sealed edge checks", true);
            prop.setComment("If this is enabled, areas sealed by Oxygen Sealers will run a seal check when the player breaks or places a block (or on block updates).  This should be enabled for a 100% accurate sealed status, but can be disabled on servers for performance reasons.");
            prop.setLanguageKey("ls.configgui.enableSealerEdgeChecks");
            enableSealerEdgeChecks = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get("general", "Open Galaxy Map", "KEY_M");
            prop.setComment("Leave 'KEY_' value, adding the intended keyboard character to replace the letter. Values 0-9 and A-Z are accepted");
            prop.setLanguageKey("ls.configgui.overrideMap").setRequiresMcRestart(true);
            keyOverrideMap = prop.getString();
            keyOverrideMapI = parseKeyValue(keyOverrideMap);
            propOrder.add(prop.getName());

            prop = config.get("general", "Open Fuel GUI", "KEY_F");
            prop.setComment("Leave 'KEY_' value, adding the intended keyboard character to replace the letter. Values 0-9 and A-Z are accepted");
            prop.setLanguageKey("ls.configgui.keyOverrideFuelLevel").setRequiresMcRestart(true);
            keyOverrideFuelLevel = prop.getString();
            keyOverrideFuelLevelI = parseKeyValue(keyOverrideFuelLevel);
            propOrder.add(prop.getName());

            prop = config.get("general", "Toggle Advanced Goggles", "KEY_K");
            prop.setComment("Leave 'KEY_' value, adding the intended keyboard character to replace the letter. Values 0-9 and A-Z are accepted");
            prop.setLanguageKey("ls.configgui.keyOverrideToggleAdvGoggles").setRequiresMcRestart(true);
            keyOverrideToggleAdvGoggles = prop.getString();
            keyOverrideToggleAdvGogglesI = parseKeyValue(keyOverrideToggleAdvGoggles);
            propOrder.add(prop.getName());

            prop = config.get("general", "Rocket fuel factor", 1);
            prop.setComment("The normal factor is 1.  Increase this to 2 - 5 if other mods with a lot of oil (e.g. BuildCraft) are installed to increase ls rocket fuel requirement.");
            prop.setLanguageKey("ls.configgui.rocketFuelFactor");
            rocketFuelFactor = prop.getInt(1);
            propOrder.add(prop.getName());

//            prop = config.get("general", "Map factor", 1);
//            prop.comment = "Allowed values 1-4 etc";
//            prop.setLanguageKey("ls.configgui.mapFactor");
//            mapfactor = prop.getInt(1);
//            propOrder.add(prop.getName());
//            
//            prop = config.get("general", "Map size", 400);
//            prop.comment = "Suggested value 400";
//            prop.setLanguageKey("ls.configgui.mapSize");
//            mapsize = prop.getInt(400);
//            propOrder.add(prop.getName());
//            
            prop = config.get("general", "Map Scroll Mouse Sensitivity", 1.0);
            prop.setComment("Increase to make the mouse drag scroll more sensitive, decrease to lower sensitivity.");
            prop.setLanguageKey("ls.configgui.mapScrollSensitivity");
            mapMouseScrollSensitivity = (float) prop.getDouble(1.0);
            propOrder.add(prop.getName());

            prop = config.get("general", "Map Scroll Mouse Invert", false);
            prop.setComment("Set to true to invert the mouse scroll feature on the galaxy map.");
            prop.setLanguageKey("ls.configgui.mapScrollInvert");
            invertMapMouseScroll = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get("general", "Meteor Spawn Modifier", 1.0);
            prop.setComment("Set to a value between 0.0 and 1.0 to decrease meteor spawn chance (all dimensions).");
            prop.setLanguageKey("ls.configgui.meteorSpawnMod");
            meteorSpawnMod = prop.getDouble(1.0);
            propOrder.add(prop.getName());

            prop = config.get("general", "Meteor Block Damage Enabled", true);
            prop.setComment("Set to false to stop meteors from breaking blocks on contact.");
            prop.setLanguageKey("ls.configgui.meteorBlockDamage");
            meteorBlockDamageEnabled = prop.getBoolean(true);
            propOrder.add(prop.getName());

            prop = config.get("general", "Disable Update Check", false);
            prop.setComment("Update check will not run if this is set to true.");
            prop.setLanguageKey("ls.configgui.disableUpdateCheck");
            disableUpdateCheck = prop.getBoolean(false);
            propOrder.add(prop.getName());

            prop = config.get("general", "Disable Biome Type Registrations", false);
            prop.setComment("Biome Types will not be registered in the BiomeDictionary if this is set to true. Ignored (always true) for MC 1.7.2.");
            prop.setLanguageKey("ls.configgui.disableBiomeTypeRegistrations");
            disableBiomeTypeRegistrations = prop.getBoolean(false);
            propOrder.add(prop.getName());


            config.setCategoryPropertyOrder(CATEGORY_GENERAL, propOrder);

            if (config.hasChanged())
            {
                config.save();
            }
            
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean setLoaded(int newID)
    {
        boolean found = false;

        for (int staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
        {
            if (staticLoadDimension == newID)
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            int[] oldIDs = ConfigManagerCore.staticLoadDimensions;
            ConfigManagerCore.staticLoadDimensions = new int[ConfigManagerCore.staticLoadDimensions.length + 1];
            System.arraycopy(oldIDs, 0, staticLoadDimensions, 0, oldIDs.length);

            ConfigManagerCore.staticLoadDimensions[ConfigManagerCore.staticLoadDimensions.length - 1] = newID;
            String[] values = new String[ConfigManagerCore.staticLoadDimensions.length];
            Arrays.sort(ConfigManagerCore.staticLoadDimensions);

            for (int i = 0; i < values.length; i++)
            {
                values[i] = String.valueOf(ConfigManagerCore.staticLoadDimensions[i]);
            }

            Property prop = config.get("dimensions", "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
            prop.setComment("IDs to load at startup, and keep loaded until server stops. Can be added via /lskeeploaded");
            prop.setLanguageKey("ls.configgui.staticLoadedDimensions");
            prop.set(values);

            ConfigManagerCore.config.save();
        }

        return !found;
    }

    public static boolean setUnloaded(int idToRemove)
    {
        int foundCount = 0;

        for (int staticLoadDimension : ConfigManagerCore.staticLoadDimensions)
        {
            if (staticLoadDimension == idToRemove)
            {
                foundCount++;
            }
        }

        if (foundCount > 0)
        {
            List<Integer> idArray = new ArrayList<Integer>(Ints.asList(ConfigManagerCore.staticLoadDimensions));
            idArray.removeAll(Collections.singleton(idToRemove));

            ConfigManagerCore.staticLoadDimensions = new int[idArray.size()];

            for (int i = 0; i < idArray.size(); i++)
            {
                ConfigManagerCore.staticLoadDimensions[i] = idArray.get(i);
            }

            String[] values = new String[ConfigManagerCore.staticLoadDimensions.length];
            Arrays.sort(ConfigManagerCore.staticLoadDimensions);

            for (int i = 0; i < values.length; i++)
            {
                values[i] = String.valueOf(ConfigManagerCore.staticLoadDimensions[i]);
            }

            Property prop = config.get("dimensions", "Static Loaded Dimensions", ConfigManagerCore.staticLoadDimensions);
            prop.setComment("IDs to load at startup, and keep loaded until server stops. Can be added via /lskeeploaded");
            prop.setLanguageKey("ls.configgui.staticLoadedDimensions");
            prop.set(values);

            ConfigManagerCore.config.save();
        }

        return foundCount > 0;
    }

    
    /**
     * Note for this to be effective, the prop = config.get() call has to provide a String[] as the default values
     * If you use an Integer[] then the config parser deletes all non-numerical lines from the config before ls even sees them
     */
    private static boolean searchAsterisk(String[] strings)
    {
        for (String s : strings)
        {
            if (s != null && "*".equals(s.trim()))
            {
                return true;
            }
        }
        return false;
    }

    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory("dimensions")).getChildElements());
        list.addAll(new ConfigElement(config.getCategory("achievements")).getChildElements());
        list.addAll(new ConfigElement(config.getCategory("entities")).getChildElements());
        list.addAll(new ConfigElement(config.getCategory("general")).getChildElements());
        return list;
    }

    public static BlockTuple stringToBlock(String s, String caller, boolean logging)
    {
        int lastColon = s.lastIndexOf(':');
        int meta = -1;
        String name;

        if (lastColon > 0)
        {
            try
            {
                meta = Integer.parseInt(s.substring(lastColon + 1, s.length()));
            }
            catch (NumberFormatException ex)
            {
            }
        }

        if (meta == -1)
        {
            name = s;
        }
        else
        {
            name = s.substring(0, lastColon);
        }

        Block block = Block.getBlockFromName(name);
        if (block == null)
        {
            Item item = (Item) Item.REGISTRY.getObject(new ResourceLocation(name));
            if (item instanceof ItemBlock)
            {
                block = ((ItemBlock) item).block;
            }
            if (block == null)
            {
                if (logging)
                {
                }
                return null;
            }
        }
        try
        {
            Integer.parseInt(name);
            String bName = (String) GameData.getBlockRegistry().getNameForObject(block).toString();
            if (logging)
            {
            }
        }
        catch (NumberFormatException ex)
        {
        }
        if (Blocks.AIR == block)
        {
            if (logging)
            {
            }
            return null;
        }

        return new BlockTuple(block, meta);
    }

    public static List<Object> getServerConfigOverride()
    {
    	ArrayList<Object> returnList = new ArrayList();
    	returnList.add(ConfigManagerCore.suffocationDamage);
    	returnList.add(ConfigManagerCore.suffocationCooldown);
    	returnList.add(ConfigManagerCore.rocketFuelFactor);
    	
    	returnList.add(ConfigManagerCore.detectableIDs.clone());  	
    	return returnList;
    }

    @SideOnly(Side.CLIENT)
    public static void setConfigOverride(List<Object> configs)
    {
        int dataCount = 0;
    	int modeFlag = (Integer) configs.get(dataCount++);
    	ConfigManagerCore.suffocationDamage = (Integer) configs.get(dataCount++);
    	ConfigManagerCore.suffocationCooldown = (Integer) configs.get(dataCount++);
    	ConfigManagerCore.rocketFuelFactor = (Integer) configs.get(dataCount++);
    	//If adding any additional data objects here, also remember to update the packet definition of EnumSimplePacket.C_UPDATE_CONFIGS in PacketSimple
    	//Current working packet definition: Integer.class, Double.class, Integer.class, Integer.class, Integer.class, String.class, Float.class, Float.class, Float.class, Float.class, Integer.class, String[].class
    	
    	
    	int sizeIDs = configs.size() - dataCount;
    	if (sizeIDs > 0)
    	{
    	    Object dataLast = configs.get(dataCount); 
    		if (dataLast instanceof String)
    		{
    			ConfigManagerCore.detectableIDs = new String[sizeIDs];
		    	for (int j = 0; j < sizeIDs; j++)
		    	ConfigManagerCore.detectableIDs[j] = new String((String) configs.get(dataCount++));
    		}
    		else if (dataLast instanceof String[])
    		{
    			ConfigManagerCore.detectableIDs = ((String[])dataLast);
    		}
    	}
    	
    }

    public static void saveClientConfigOverrideable()
    {
        if (ConfigManagerCore.clientSave == null)
        {
            ConfigManagerCore.clientSave = (ArrayList<Object>) ConfigManagerCore.getServerConfigOverride();
        }
    }

    public static void restoreClientConfigOverrideable()
    {
        if (ConfigManagerCore.clientSave != null)
        {
            ConfigManagerCore.setConfigOverride(clientSave);
        }
    }

    private static int parseKeyValue(String key)
    {
        if (key.equals("KEY_A"))
        {
            return Keyboard.KEY_A;
        }
        else if (key.equals("KEY_B"))
        {
            return Keyboard.KEY_B;
        }
        else if (key.equals("KEY_C"))
        {
            return Keyboard.KEY_C;
        }
        else if (key.equals("KEY_D"))
        {
            return Keyboard.KEY_D;
        }
        else if (key.equals("KEY_E"))
        {
            return Keyboard.KEY_E;
        }
        else if (key.equals("KEY_F"))
        {
            return Keyboard.KEY_F;
        }
        else if (key.equals("KEY_G"))
        {
            return Keyboard.KEY_G;
        }
        else if (key.equals("KEY_H"))
        {
            return Keyboard.KEY_H;
        }
        else if (key.equals("KEY_I"))
        {
            return Keyboard.KEY_I;
        }
        else if (key.equals("KEY_J"))
        {
            return Keyboard.KEY_J;
        }
        else if (key.equals("KEY_K"))
        {
            return Keyboard.KEY_K;
        }
        else if (key.equals("KEY_L"))
        {
            return Keyboard.KEY_L;
        }
        else if (key.equals("KEY_M"))
        {
            return Keyboard.KEY_M;
        }
        else if (key.equals("KEY_N"))
        {
            return Keyboard.KEY_N;
        }
        else if (key.equals("KEY_O"))
        {
            return Keyboard.KEY_O;
        }
        else if (key.equals("KEY_P"))
        {
            return Keyboard.KEY_P;
        }
        else if (key.equals("KEY_Q"))
        {
            return Keyboard.KEY_Q;
        }
        else if (key.equals("KEY_R"))
        {
            return Keyboard.KEY_R;
        }
        else if (key.equals("KEY_S"))
        {
            return Keyboard.KEY_S;
        }
        else if (key.equals("KEY_T"))
        {
            return Keyboard.KEY_T;
        }
        else if (key.equals("KEY_U"))
        {
            return Keyboard.KEY_U;
        }
        else if (key.equals("KEY_V"))
        {
            return Keyboard.KEY_V;
        }
        else if (key.equals("KEY_W"))
        {
            return Keyboard.KEY_W;
        }
        else if (key.equals("KEY_X"))
        {
            return Keyboard.KEY_X;
        }
        else if (key.equals("KEY_Y"))
        {
            return Keyboard.KEY_Y;
        }
        else if (key.equals("KEY_Z"))
        {
            return Keyboard.KEY_Z;
        }
        else if (key.equals("KEY_1"))
        {
            return Keyboard.KEY_1;
        }
        else if (key.equals("KEY_2"))
        {
            return Keyboard.KEY_2;
        }
        else if (key.equals("KEY_3"))
        {
            return Keyboard.KEY_3;
        }
        else if (key.equals("KEY_4"))
        {
            return Keyboard.KEY_4;
        }
        else if (key.equals("KEY_5"))
        {
            return Keyboard.KEY_5;
        }
        else if (key.equals("KEY_6"))
        {
            return Keyboard.KEY_6;
        }
        else if (key.equals("KEY_7"))
        {
            return Keyboard.KEY_7;
        }
        else if (key.equals("KEY_8"))
        {
            return Keyboard.KEY_8;
        }
        else if (key.equals("KEY_9"))
        {
            return Keyboard.KEY_9;
        }
        else if (key.equals("KEY_0"))
        {
            return Keyboard.KEY_0;
        }


        return 0;
    }
}