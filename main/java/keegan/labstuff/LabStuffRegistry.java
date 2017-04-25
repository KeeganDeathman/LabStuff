package keegan.labstuff;

import java.util.*;

import com.google.common.collect.Lists;

import keegan.labstuff.client.IGameScreen;
import keegan.labstuff.items.EnumExtendedInventorySlot;
import keegan.labstuff.util.LSLog;
import keegan.labstuff.world.*;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraftforge.fml.relauncher.*;

public class LabStuffRegistry
{
    private static Map<Class<? extends WorldProvider>, ITeleportType> teleportTypeMap = new HashMap<Class<? extends WorldProvider>, ITeleportType>();
    private static List<SpaceStationType> spaceStations = new ArrayList<SpaceStationType>();
    private static Map<Class<? extends WorldProvider>, ResourceLocation> rocketGuiMap = new HashMap<Class<? extends WorldProvider>, ResourceLocation>();
    private static Map<Integer, List<ItemStack>> dungeonLootMap = new HashMap<Integer, List<ItemStack>>();
    private static List<Integer> dimensionTypeIDs = new ArrayList<Integer>();
    private static List<IGameScreen> gameScreens = new ArrayList<IGameScreen>();
    private static int maxScreenTypes;
    private static Map<Integer, List<Object>> gearMap = new HashMap<>();
    private static Map<Integer, List<EnumExtendedInventorySlot>> gearSlotMap = new HashMap<>();

    /**
     * Register a new Teleport type for the world provider passed
     *
     * @param clazz the world provider class that you wish to customize
     *              teleportation for
     * @param type  an ITeleportType-implemented class that will be used for the
     *              provided world type
     */
    public static void registerTeleportType(Class<? extends WorldProvider> clazz, ITeleportType type)
    {
        if (!LabStuffRegistry.teleportTypeMap.containsKey(clazz))
        {
            LabStuffRegistry.teleportTypeMap.put(clazz, type);
        }
    }

    /**
     * Link a world provider to a gui texture. This texture will be shown on the
     * left-side of the screen while the player is in the rocket.
     *
     * @param clazz     The World Provider class
     * @param rocketGui Resource Location for the gui texture
     */
    public static void registerRocketGui(Class<? extends WorldProvider> clazz, ResourceLocation rocketGui)
    {
        if (!LabStuffRegistry.rocketGuiMap.containsKey(clazz))
        {
            LabStuffRegistry.rocketGuiMap.put(clazz, rocketGui);
        }
    }

    /**
     * Add loot to the list of items that can possibly spawn in dungeon chests,
     * but it is guaranteed that one will always spawn
     *
     * @param tier Tier of dungeon chest to add loot to. For example Moon is 1
     *             and Mars is 2
     * @param loot The itemstack to add to the possible list of items
     */
    public static void addDungeonLoot(int tier, ItemStack loot)
    {
        List<ItemStack> dungeonStacks = null;

        if (LabStuffRegistry.dungeonLootMap.containsKey(tier))
        {
            dungeonStacks = LabStuffRegistry.dungeonLootMap.get(tier);
            dungeonStacks.add(loot);
        }
        else
        {
            dungeonStacks = new ArrayList<ItemStack>();
            dungeonStacks.add(loot);
        }

        LabStuffRegistry.dungeonLootMap.put(tier, dungeonStacks);
    }


    public static ITeleportType getTeleportTypeForDimension(Class<? extends WorldProvider> clazz)
    {
        if (!ILabstuffWorldProvider.class.isAssignableFrom(clazz))
        {
            clazz = WorldProviderSurface.class;
        }
        return LabStuffRegistry.teleportTypeMap.get(clazz);
    }

    public static void registerSpaceStation(SpaceStationType type)
    {
        for (SpaceStationType type1 : LabStuffRegistry.spaceStations)
        {
            if (type1.getWorldToOrbitID() == type.getWorldToOrbitID())
            {
                throw new RuntimeException("Two space station types registered with the same home planet ID: " + type.getWorldToOrbitID());
            }
        }

        LabStuffRegistry.spaceStations.add(type);
    }

    public SpaceStationType getTypeFromPlanetID(int planetID)
    {
        return LabStuffRegistry.spaceStations.get(planetID);
    }

    public static List<SpaceStationType> getSpaceStationData()
    {
        return LabStuffRegistry.spaceStations;
    }

	@SideOnly(Side.CLIENT)
    public static ResourceLocation getResouceLocationForDimension(Class<? extends WorldProvider> clazz)
    {
        if (!ILabstuffWorldProvider.class.isAssignableFrom(clazz))
        {
            clazz = WorldProviderSurface.class;
        }
        return LabStuffRegistry.rocketGuiMap.get(clazz);
    }

    public static List<ItemStack> getDungeonLoot(int tier)
    {
        return LabStuffRegistry.dungeonLootMap.get(tier);
    }
    
    /***
     * Register a Galacticraft dimension
     */
    public static DimensionType registerDimension(String name, String suffix, int id, Class<? extends WorldProvider> provider, boolean keepLoaded) throws IllegalArgumentException
    {
        for (DimensionType other : DimensionType.values())
        {
            if (other.getId() == id)
            {
                return null;
            }
        }

        DimensionType type = DimensionType.register(name, suffix, id, provider, keepLoaded);
        LabStuffRegistry.dimensionTypeIDs.add(type == null ? 0 : id);
        if (type == null)
        {
            LSLog.severe("Problem registering dimension type " + id + ".  May be fixable by changing config.");
        }
        
        return type;
    }

    public static int getDimensionTypeID(int index)
    {
    	return LabStuffRegistry.dimensionTypeIDs.get(index);
    }
    
    public static boolean isDimensionTypeIDRegistered(int typeId)
    {
        return LabStuffRegistry.dimensionTypeIDs.contains(typeId);
    }
    
    /**
     * Register an IGameScreen so the Display Screen can access it
     * 
     * @param screen  The IGameScreen to be registered
     * @return   The type ID assigned to this screen type
     */
    public static int registerScreen(IGameScreen screen)
    {
    	LabStuffRegistry.gameScreens.add(screen);
        maxScreenTypes++;
    	screen.setFrameSize(0.098F);
    	return maxScreenTypes - 1;
    }

    public static int getMaxScreenTypes() {
        return maxScreenTypes;
    }

    public static IGameScreen getGameScreen(int type)
    {
    	return LabStuffRegistry.gameScreens.get(type);
    }

    /**
     * Adds a custom item for 'extended inventory' slots
     *
     * Gear IDs must be unique, and should be configurable for user convenience
     *
     * Please do not use values less than 100, to avoid conflicts with future Galacticraft core additions
     *
     * @param gearID Unique ID for this gear item, please use values greater than 100
     * @param type Slot this item can be placed in
     * @param item Item to register, not metadata-sensitive
     */
    public static void registerGear(int gearID, EnumExtendedInventorySlot type, Item item)
    {
        addGearObject(gearID, type, item);
    }

    /**
     * Adds a custom item for 'extended inventory' slots
     *
     * Gear IDs must be unique, and should be configurable for user convenience
     *
     * Please do not use values less than 100, to avoid conflicts with future Galacticraft core additions
     *
     * @param gearID Unique ID for this gear item, please use values greater than 100
     * @param type Slot this item can be placed in
     * @param itemStack ItemStack to register, metadata-sensitive
     */
    public static void registerGear(int gearID, EnumExtendedInventorySlot type, ItemStack itemStack)
    {
        addGearObject(gearID, type, itemStack);
    }

    private static void addGearObject(int gearID, EnumExtendedInventorySlot type, Object obj)
    {
        if (LabStuffRegistry.gearMap.containsKey(gearID))
        {
            if (!LabStuffRegistry.gearMap.get(gearID).contains(obj))
            {
                LabStuffRegistry.gearMap.get(gearID).add(obj);
            }
        }
        else
        {
            List<Object> gear = Lists.newArrayList();
            gear.add(obj);
            LabStuffRegistry.gearMap.put(gearID, gear);
        }

        if (LabStuffRegistry.gearSlotMap.containsKey(gearID))
        {
            if (!LabStuffRegistry.gearSlotMap.get(gearID).contains(type))
            {
                LabStuffRegistry.gearSlotMap.get(gearID).add(type);
            }
        }
        else
        {
            List<EnumExtendedInventorySlot> gearType = Lists.newArrayList();
            gearType.add(type);
            LabStuffRegistry.gearSlotMap.put(gearID, gearType);
        }
    }

    public static int findMatchingGearID(ItemStack stack, EnumExtendedInventorySlot slotType)
    {
        for (Map.Entry<Integer, List<Object>> entry : LabStuffRegistry.gearMap.entrySet())
        {
            List<EnumExtendedInventorySlot> slotType1 = getSlotType(entry.getKey());
            List<Object> objectList = entry.getValue();

            if (!slotType1.contains(slotType))
            {
                continue;
            }

            for (Object o : objectList)
            {
                if (o instanceof Item)
                {
                    if (stack.getItem() == o)
                    {
                        return entry.getKey();
                    }
                }
                else if (o instanceof ItemStack)
                {
                    if (stack.getItem() == ((ItemStack) o).getItem() && stack.getItemDamage() == ((ItemStack) o).getItemDamage())
                    {
                        return entry.getKey();
                    }
                }
            }
        }

        return -1;
    }

    public static List<EnumExtendedInventorySlot> getSlotType(int gearID)
    {
        return LabStuffRegistry.gearSlotMap.get(gearID);
    }
}