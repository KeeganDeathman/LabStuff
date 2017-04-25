package keegan.labstuff.PacketHandling;

import java.io.IOException;
import java.util.*;

import com.google.common.collect.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.netty.buffer.ByteBuf;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.*;
import keegan.labstuff.client.fx.ParticleSparks;
import keegan.labstuff.client.gui.*;
import keegan.labstuff.common.TickHandlerServer;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.dimension.*;
import keegan.labstuff.entities.*;
import keegan.labstuff.galaxies.*;
import keegan.labstuff.items.*;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import keegan.labstuff.wrappers.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.*;
import net.minecraftforge.fml.server.FMLServerHandler;

public class PacketSimple extends PacketBase implements Packet<INetHandler>
{
    public enum EnumSimplePacket
    {
        // SERVER
        S_RESPAWN_PLAYER(Side.SERVER, String.class),
        S_TELEPORT_ENTITY(Side.SERVER, String.class),
        S_IGNITE_ROCKET(Side.SERVER),
        S_OPEN_FUEL_GUI(Side.SERVER, String.class),
        S_UPDATE_SHIP_YAW(Side.SERVER, Float.class),
        S_UPDATE_SHIP_PITCH(Side.SERVER, Float.class),
        S_SET_ENTITY_FIRE(Side.SERVER, Integer.class),
        S_BIND_SPACE_STATION_ID(Side.SERVER, Integer.class),
        S_UPDATE_DISABLEABLE_BUTTON(Side.SERVER, BlockPos.class, Integer.class),
        S_RENAME_SPACE_STATION(Side.SERVER, String.class, Integer.class),
        S_OPEN_EXTENDED_INVENTORY(Side.SERVER),
        S_ON_ADVANCED_GUI_CLICKED_INT(Side.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_ON_ADVANCED_GUI_CLICKED_STRING(Side.SERVER, Integer.class, BlockPos.class, String.class),
        S_UPDATE_SHIP_MOTION_Y(Side.SERVER, Integer.class, Boolean.class),
        S_COMPLETE_CBODY_HANDSHAKE(Side.SERVER, String.class),
        S_UPDATE_ADVANCED_GUI(Side.SERVER, Integer.class, BlockPos.class, Integer.class),
        S_REQUEST_GEAR_DATA(Side.SERVER, String.class),
        S_REQUEST_OVERWORLD_IMAGE(Side.SERVER),
        S_REQUEST_MAP_IMAGE(Side.SERVER, Integer.class, Integer.class, Integer.class),
        S_REQUEST_PLAYERSKIN(Side.SERVER, String.class),
        S_BUILDFLAGS_UPDATE(Side.SERVER, Integer.class),
        S_CONTROL_ENTITY(Side.SERVER, Integer.class),
        S_REQUEST_DATA(Side.SERVER, Integer.class, BlockPos.class),
        S_UPDATE_CHECKLIST(Side.SERVER, NBTTagCompound.class),
        S_REQUEST_MACHINE_DATA(Side.SERVER, BlockPos.class),
        S_UPDATE_CARGO_ROCKET_STATUS(Side.SERVER, Integer.class, Integer.class),
        // CLIENT
        C_AIR_REMAINING(Side.CLIENT, Integer.class, Integer.class, String.class),
        C_UPDATE_DIMENSION_LIST(Side.CLIENT, String.class, String.class),
        C_SPAWN_SPARK_PARTICLES(Side.CLIENT, BlockPos.class),
        C_UPDATE_GEAR_SLOT(Side.CLIENT, String.class, Integer.class, Integer.class, Integer.class),
        C_CLOSE_GUI(Side.CLIENT),
        C_RESET_THIRD_PERSON(Side.CLIENT),
        C_UPDATE_SPACESTATION_LIST(Side.CLIENT, Integer[].class),
        C_UPDATE_SPACESTATION_DATA(Side.CLIENT, Integer.class, NBTTagCompound.class),
        C_UPDATE_SPACESTATION_CLIENT_ID(Side.CLIENT, String.class),
        C_UPDATE_PLANETS_LIST(Side.CLIENT, Integer[].class),
        C_UPDATE_CONFIGS(Side.CLIENT, Integer.class, Double.class, Integer.class, Integer.class, Integer.class, String.class, Float.class, Float.class, Float.class, Float.class, Integer.class, String[].class),
        C_UPDATE_STATS(Side.CLIENT, Integer.class),
        C_UPDATE_OXYGEN_VALIDITY(Side.CLIENT, Boolean.class),
        C_OPEN_PARACHEST_GUI(Side.CLIENT, Integer.class, Integer.class, Integer.class),
        C_UPDATE_STATION_SPIN(Side.CLIENT, Float.class, Boolean.class),
        C_UPDATE_STATION_DATA(Side.CLIENT, Double.class, Double.class),
        C_UPDATE_STATION_BOX(Side.CLIENT, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_UPDATE_THERMAL_LEVEL(Side.CLIENT, Integer.class, Boolean.class),
        C_DISPLAY_ROCKET_CONTROLS(Side.CLIENT),
        C_GET_CELESTIAL_BODY_LIST(Side.CLIENT),
        C_RESPAWN_PLAYER(Side.CLIENT, String.class, Integer.class, String.class, Integer.class),
        C_UPDATE_TELEMETRY(Side.CLIENT, BlockPos.class, String.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class),
        C_SEND_PLAYERSKIN(Side.CLIENT, String.class, String.class, String.class, String.class),
        C_SEND_OVERWORLD_IMAGE(Side.CLIENT, Integer.class, Integer.class, byte[].class),
        C_RECOLOR_ALL_GLASS(Side.CLIENT, Integer.class, Integer.class, Integer.class),  //Number of integers to match number of different blocks of PLAIN glass individually instanced and registered in GCBlocks
        C_UPDATE_MACHINE_DATA(Side.CLIENT, BlockPos.class, Integer.class, Integer.class, Integer.class, Integer.class),
        C_UPDATE_GRAPPLE_POS(Side.CLIENT, Integer.class, Vector3.class),
        C_TELEPAD_SEND(Side.CLIENT, BlockVec3.class, Integer.class),
        C_BEGIN_CRYOGENIC_SLEEP(Side.CLIENT, BlockPos.class),
        C_OPEN_CUSTOM_GUI(Side.CLIENT, Integer.class, Integer.class, Integer.class);

        private Side targetSide;
        private Class<?>[] decodeAs;

        EnumSimplePacket(Side targetSide, Class<?>... decodeAs)
        {
            this.targetSide = targetSide;
            this.decodeAs = decodeAs;
        }

        public Side getTargetSide()
        {
            return this.targetSide;
        }

        public Class<?>[] getDecodeClasses()
        {
            return this.decodeAs;
        }
    }

    private EnumSimplePacket type;
    private List<Object> data;
    static private String spamCheckString;

    public PacketSimple()
    {
        super();
    }

    public PacketSimple(EnumSimplePacket packetType, int dimID, Object[] data)
    {
        this(packetType, dimID, Arrays.asList(data));
    }

    public PacketSimple(EnumSimplePacket packetType, World world, Object[] data)
    {
        this(packetType, LabStuffUtils.getDimensionID(world), Arrays.asList(data));
    }

    public PacketSimple(EnumSimplePacket packetType, int dimID, List<Object> data)
    {
        super(dimID);
        if (packetType.getDecodeClasses().length != data.size())
        {
            LSLog.info("Simple Packet Core found data length different than packet type");
            new RuntimeException().printStackTrace();
        }

        this.type = packetType;
        this.data = data;
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        super.encodeInto(buffer);
        buffer.writeInt(this.type.ordinal());

        try
        {
            NetworkUtil.encodeData(buffer, this.data);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
        super.decodeInto(buffer);
        this.type = EnumSimplePacket.values()[buffer.readInt()];

        try
        {
            if (this.type.getDecodeClasses().length > 0)
            {
                this.data = NetworkUtil.decodeData(this.type.getDecodeClasses(), buffer);
            }
            if (buffer.readableBytes() > 0)
            {
                LSLog.severe("LabStuff packet length problem for packet type " + this.type.toString());
            }
        }
        catch (Exception e)
        {
            System.err.println("[LabStuff] Error handling simple packet type: " + this.type.toString() + " " + buffer.toString());
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientSide(EntityPlayer player)
    {
        EntityPlayerSP playerBaseClient = null;
        LSPlayerStatsClient stats = null;

        if (player instanceof EntityPlayerSP)
        {
            playerBaseClient = (EntityPlayerSP) player;
            stats = LSPlayerStatsClient.get(playerBaseClient);
        }
        else
        {
            if (type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && type != EnumSimplePacket.C_UPDATE_CONFIGS)
            {
                return;
            }
        }

        switch (this.type)
        {
        case C_AIR_REMAINING:
            if (String.valueOf(this.data.get(2)).equals(String.valueOf(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName())))
            {
                ClientTickHandler.airRemaining = (Integer) this.data.get(0);
                ClientTickHandler.airRemaining2 = (Integer) this.data.get(1);
            }
            break;
        case C_UPDATE_DIMENSION_LIST:
            if (String.valueOf(this.data.get(0)).equals(FMLClientHandler.instance().getClient().thePlayer.getGameProfile().getName()))
            {
                String dimensionList = (String) this.data.get(1);
                final String[] destinations = dimensionList.split("\\?");
                List<CelestialBody> possibleCelestialBodies = Lists.newArrayList();
                Map<Integer, Map<String, GuiCelestialSelection.StationDataGUI>> spaceStationData = Maps.newHashMap();
//                Map<String, String> spaceStationNames = Maps.newHashMap();
//                Map<String, Integer> spaceStationIDs = Maps.newHashMap();
//                Map<String, Integer> spaceStationHomes = Maps.newHashMap();

                for (String str : destinations)
                {
                    CelestialBody celestialBody = WorldUtil.getReachableCelestialBodiesForName(str);

                    if (celestialBody == null && str.contains("$"))
                    {
                        String[] values = str.split("\\$");

                        int homePlanetID = Integer.parseInt(values[4]);

                        for (Satellite satellite : GalaxyRegistry.getRegisteredSatellites().values())
                        {
                            if (satellite.getParentPlanet().getDimensionID() == homePlanetID)
                            {
                                celestialBody = satellite;
                                break;
                            }
                        }

                        if (!spaceStationData.containsKey(homePlanetID))
                        {
                            spaceStationData.put(homePlanetID, new HashMap<String, GuiCelestialSelection.StationDataGUI>());
                        }

                        spaceStationData.get(homePlanetID).put(values[1], new GuiCelestialSelection.StationDataGUI(values[2], Integer.parseInt(values[3])));

//                        spaceStationNames.put(values[1], values[2]);
//                        spaceStationIDs.put(values[1], Integer.parseInt(values[3]));
//                        spaceStationHomes.put(values[1], Integer.parseInt(values[4]));
                    }

                    if (celestialBody != null)
                    {
                        possibleCelestialBodies.add(celestialBody);
                    }
                }

                if (FMLClientHandler.instance().getClient().theWorld != null)
                {
                    if (!(FMLClientHandler.instance().getClient().currentScreen instanceof GuiCelestialSelection))
                    {
                        GuiCelestialSelection gui = new GuiCelestialSelection(false, possibleCelestialBodies);
                        gui.spaceStationMap = spaceStationData;
//                        gui.spaceStationNames = spaceStationNames;
//                        gui.spaceStationIDs = spaceStationIDs;
                        FMLClientHandler.instance().getClient().displayGuiScreen(gui);
                    }
                    else
                    {
                        ((GuiCelestialSelection) FMLClientHandler.instance().getClient().currentScreen).possibleBodies = possibleCelestialBodies;
                        ((GuiCelestialSelection) FMLClientHandler.instance().getClient().currentScreen).spaceStationMap = spaceStationData;
//                        ((GuiCelestialSelection) FMLClientHandler.instance().getClient().currentScreen).spaceStationNames = spaceStationNames;
//                        ((GuiCelestialSelection) FMLClientHandler.instance().getClient().currentScreen).spaceStationIDs = spaceStationIDs;
                    }
                }
            }
            break;
        case C_SPAWN_SPARK_PARTICLES:
            BlockPos pos = (BlockPos) this.data.get(0);
            Minecraft mc = Minecraft.getMinecraft();

            for (int i = 0; i < 4; i++)
            {
                if (mc.getRenderViewEntity() != null && mc.effectRenderer != null && mc.theWorld != null)
                {
                    final ParticleSparks fx = new ParticleSparks(mc.theWorld, pos.getX() - 0.15 + 0.5, pos.getY() + 1.2, pos.getZ() + 0.15 + 0.5, mc.theWorld.rand.nextDouble() / 20 - mc.theWorld.rand.nextDouble() / 20, mc.theWorld.rand.nextDouble() / 20 - mc.theWorld.rand.nextDouble() / 20);

                    mc.effectRenderer.addEffect(fx);
                }
            }
            break;
        case C_OPEN_CUSTOM_GUI:
            int entityID = 0;
            Entity entity = null;

            switch ((Integer) this.data.get(1))
            {
            case 0:
                entityID = (Integer) this.data.get(2);
                entity = player.worldObj.getEntityByID(entityID);

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            case 1:
                entityID = (Integer) this.data.get(2);
                entity = player.worldObj.getEntityByID(entityID);

                if (entity != null && entity instanceof EntityCargoRocket)
                {
                    FMLClientHandler.instance().getClient().displayGuiScreen(new GuiCargoRocket(player.inventory, (EntityCargoRocket) entity));
                }

                player.openContainer.windowId = (Integer) this.data.get(0);
                break;
            }
            break;
        case C_UPDATE_GEAR_SLOT:
            int subtype = (Integer) this.data.get(3);
            EntityPlayer gearDataPlayer;
            MinecraftServer server = player.worldObj.getMinecraftServer();
            String gearName = (String) this.data.get(0);

            if (server != null)
            {
                gearDataPlayer = PlayerUtil.getPlayerForUsernameVanilla(server, gearName);
            }
            else
            {
                gearDataPlayer = player.worldObj.getPlayerEntityByName(gearName);
            }

            if (gearDataPlayer != null)
            {
                PlayerGearData gearData = LabStuffClientProxy.playerItemData.get(gearDataPlayer.getGameProfile().getName());

                if (gearData == null)
                {
                    gearData = new PlayerGearData(player);
                    if (!LabStuffClientProxy.gearDataRequests.contains(gearName))
                    {
                        LabStuffMain.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA, getDimensionID(), new Object[] { gearName }));
                        LabStuffClientProxy.gearDataRequests.add(gearName);
                    }
                }
                else
                {
                    LabStuffClientProxy.gearDataRequests.remove(gearName);
                }

                EnumExtendedInventorySlot type = EnumExtendedInventorySlot.values()[(Integer) this.data.get(2)];
                LSPlayerHandler.EnumModelPacketType typeChange = LSPlayerHandler.EnumModelPacketType.values()[(Integer) this.data.get(1)];

                switch (type)
                {
                case MASK:
                    gearData.setMask(subtype);
                    break;
                case GEAR:
                    gearData.setGear(subtype);
                    break;
                case LEFT_TANK:
                    gearData.setLeftTank(subtype);
                    break;
                case RIGHT_TANK:
                    gearData.setRightTank(subtype);
                    break;
                case PARACHUTE:
                    if (typeChange == LSPlayerHandler.EnumModelPacketType.ADD)
                    {
                        String name;

                        if (subtype != -1)
                        {
                            name = ItemParaChute.names[subtype];
                            gearData.setParachute(new ResourceLocation("labstuff:textures/model/parachute/" + name + ".png"));
                        }
                    }
                    else
                    {
                        gearData.setParachute(null);
                    }
                    break;
                case FREQUENCY_MODULE:
                    gearData.setFrequencyModule(subtype);
                    break;
                case THERMAL_HELMET:
                    gearData.setThermalPadding(0, subtype);
                    break;
                case THERMAL_CHESTPLATE:
                    gearData.setThermalPadding(1, subtype);
                    break;
                case THERMAL_LEGGINGS:
                    gearData.setThermalPadding(2, subtype);
                    break;
                case THERMAL_BOOTS:
                    gearData.setThermalPadding(3, subtype);
                    break;
                case SHIELD_CONTROLLER:
                    gearData.setShieldController(subtype);
                    break;
                default:
                    break;
                }

                LabStuffClientProxy.playerItemData.put(gearName, gearData);
            }

            break;
        case C_CLOSE_GUI:
            FMLClientHandler.instance().getClient().displayGuiScreen(null);
            break;
        case C_RESET_THIRD_PERSON:
            FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = stats.getThirdPersonView();
            break;
        case C_UPDATE_SPACESTATION_LIST:
            WorldUtil.decodeSpaceStationListClient(data);
            break;
        case C_UPDATE_SPACESTATION_DATA:
            SpaceStationWorldData var4 = SpaceStationWorldData.getMPSpaceStationData(player.worldObj, (Integer) this.data.get(0), player);
            var4.readFromNBT((NBTTagCompound) this.data.get(1));
            break;
        case C_UPDATE_SPACESTATION_CLIENT_ID:
            LabStuffClientProxy.clientSpaceStationID = WorldUtil.stringToSpaceStationData((String) this.data.get(0));
            break;
        case C_UPDATE_PLANETS_LIST:
            WorldUtil.decodePlanetsListClient(data);
            break;
        case C_UPDATE_CONFIGS:
            ConfigManagerCore.saveClientConfigOverrideable();
            ConfigManagerCore.setConfigOverride(data);
            break;
        case C_UPDATE_OXYGEN_VALIDITY:
            stats.setOxygenSetupValid((Boolean) this.data.get(0));
            break;
        case C_OPEN_PARACHEST_GUI:
        	 if (player.getRidingEntity() instanceof EntityBuggy)
             {
                 FMLClientHandler.instance().getClient().displayGuiScreen(new GuiBuggy(player.inventory, (EntityBuggy) player.getRidingEntity(), ((EntityBuggy) player.getRidingEntity()).getType()));
                 player.openContainer.windowId = (Integer) this.data.get(0);
             }
             break;
        case C_UPDATE_STATION_SPIN:
            if (playerBaseClient.worldObj.provider instanceof WorldProviderSpaceStation)
            {
                ((WorldProviderSpaceStation) playerBaseClient.worldObj.provider).getSpinManager().setSpinRate((Float) this.data.get(0), (Boolean) this.data.get(1));
            }
            break;
        case C_UPDATE_STATION_DATA:
            if (playerBaseClient.worldObj.provider instanceof WorldProviderSpaceStation)
            {
                ((WorldProviderSpaceStation) playerBaseClient.worldObj.provider).getSpinManager().setSpinCentre((Double) this.data.get(0), (Double) this.data.get(1));
            }
            break;
        case C_UPDATE_STATION_BOX:
            if (playerBaseClient.worldObj.provider instanceof WorldProviderSpaceStation)
            {
                ((WorldProviderSpaceStation) playerBaseClient.worldObj.provider).getSpinManager().setSpinBox((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2), (Integer) this.data.get(3), (Integer) this.data.get(4), (Integer) this.data.get(5));
            }
            break;
        case C_UPDATE_THERMAL_LEVEL:
            stats.setThermalLevel((Integer) this.data.get(0));
            stats.setThermalLevelNormalising((Boolean) this.data.get(1));
            break;
        case C_DISPLAY_ROCKET_CONTROLS:
            player.addChatMessage(new TextComponentString(GameSettings.getKeyDisplayString(KeyHandlerClient.spaceKey.getKeyCode()) + "  - " + LabStuffUtils.translate("gui.rocket.launch.name")));
            player.addChatMessage(new TextComponentString(GameSettings.getKeyDisplayString(KeyHandlerClient.leftKey.getKeyCode()) + " / " + GameSettings.getKeyDisplayString(KeyHandlerClient.rightKey.getKeyCode()) + "  - " + LabStuffUtils.translate("gui.rocket.turn.name")));
            player.addChatMessage(new TextComponentString(GameSettings.getKeyDisplayString(KeyHandlerClient.accelerateKey.getKeyCode()) + " / " + GameSettings.getKeyDisplayString(KeyHandlerClient.decelerateKey.getKeyCode()) + "  - " + LabStuffUtils.translate("gui.rocket.updown.name")));
            player.addChatMessage(new TextComponentString(GameSettings.getKeyDisplayString(KeyHandlerClient.openFuelGui.getKeyCode()) + "       - " + LabStuffUtils.translate("gui.rocket.inv.name")));
            break;
        case C_GET_CELESTIAL_BODY_LIST:
            String str = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                str = str.concat(cBody.getUnlocalizedName() + ";");
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                str = str.concat(solarSystem.getUnlocalizedName() + ";");
            }

            LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_COMPLETE_CBODY_HANDSHAKE, getDimensionID(), new Object[] { str }));
            break;
        case C_RESPAWN_PLAYER:
            final WorldProvider provider = WorldUtil.getProviderForNameClient((String) this.data.get(0));
            final int dimID = LabStuffUtils.getDimensionID(provider);
            int par2 = (Integer) this.data.get(1);
            String par3 = (String) this.data.get(2);
            int par4 = (Integer) this.data.get(3);
            WorldUtil.forceRespawnClient(dimID, par2, par3, par4);
            break;
        case C_UPDATE_STATS:
            stats.setBuildFlags((Integer) this.data.get(0));
            break;
        case C_UPDATE_TELEMETRY:
            TileEntity tile = player.worldObj.getTileEntity((BlockPos) this.data.get(0));
            tile = player.worldObj.getTileEntity((BlockPos) this.data.get(0));
            if (tile instanceof TileEntityTelemetry)
            {
                ((TileEntityTelemetry) tile).receiveUpdate(data, this.getDimensionID());
            }
            break;
        case C_SEND_PLAYERSKIN:
            String strName = (String) this.data.get(0);
            String s1 = (String) this.data.get(1);
            String s2 = (String) this.data.get(2);
            String strUUID = (String) this.data.get(3);
            GameProfile gp = PlayerUtil.getOtherPlayerProfile(strName);
            if (gp == null)
            {
                gp = PlayerUtil.makeOtherPlayerProfile(strName, strUUID);
            }
            gp.getProperties().put("textures", new Property("textures", s1, s2));
            break;
        case C_SEND_OVERWORLD_IMAGE:
            try
            {
                int cx = (Integer) this.data.get(0);
                int cz = (Integer) this.data.get(1);
                byte[] bytes = (byte[]) this.data.get(2);
                MapUtil.receiveOverworldImageCompressed(cx, cz, bytes);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            break;
        case C_RECOLOR_ALL_GLASS:
            ColorUtil.updateGlassColors((Integer) this.data.get(0), (Integer) this.data.get(1), (Integer) this.data.get(2));
            break;
        case C_UPDATE_MACHINE_DATA:
            TileEntity tile3 = player.worldObj.getTileEntity((BlockPos) this.data.get(0));
            if (tile3 instanceof ITileClientUpdates)
            {
                ((ITileClientUpdates)tile3).updateClient(this.data);
            }
            break;
        case C_BEGIN_CRYOGENIC_SLEEP:
        	pos = (BlockPos) this.data.get(0);
            tile = player.worldObj.getTileEntity(pos);
            
            if (tile instanceof TileEntityCryogenicChamber)
            {
                ((TileEntityCryogenicChamber) tile).sleepInBedAt(player, pos.getX(), pos.getY(), pos.getZ());
            }
        case C_TELEPAD_SEND:
            Entity entity2 = playerBaseClient.worldObj.getEntityByID((Integer) this.data.get(1));

            if (entity2 != null && entity2 instanceof EntityLivingBase)
            {
                BlockVec3 pos3 = (BlockVec3) this.data.get(0);
                entity2.setPosition(pos3.x + 0.5, pos3.y + 2.2, pos3.z + 0.5);
            }
            break;
        case C_UPDATE_GRAPPLE_POS:
            entity = playerBaseClient.worldObj.getEntityByID((Integer) this.data.get(0));
            if (entity != null && entity instanceof EntityGrapple)
            {
                Vector3 vec = (Vector3) this.data.get(1);
                entity.setPosition(vec.x, vec.y, vec.z);
            }
            break;
        default:
            break;
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        final EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase == null)
        {
            return;
        }

        final LSPlayerStats stats = LSPlayerStats.get(playerBase);

        switch (this.type)
        {
        case S_RESPAWN_PLAYER:
            playerBase.connection.sendPacket(new SPacketRespawn(player.dimension, player.worldObj.getDifficulty(), player.worldObj.getWorldInfo().getTerrainType(), playerBase.interactionManager.getGameType()));
            break;
        case S_UPDATE_ADVANCED_GUI:
            TileEntity tile = player.worldObj.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setFrequency((Integer) this.data.get(2));
                }
                break;
            case 1:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setLaunchDropdownSelection((Integer) this.data.get(2));
                }
                break;
            case 2:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setDestinationFrequency((Integer) this.data.get(2));
                }
                break;
            case 3:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.launchPadRemovalDisabled = (Integer) this.data.get(2) == 1;
                }
                break;
            case 4:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.setLaunchSchedulingEnabled((Integer) this.data.get(2) == 1);
                }
                break;
            case 5:
                if (tile instanceof TileEntityLaunchController)
                {
                    TileEntityLaunchController launchController = (TileEntityLaunchController) tile;
                    launchController.requiresClientUpdate = true;
                }
                break;
            default:
                break;
            }
            break;
        case S_TELEPORT_ENTITY:
            TickHandlerServer.scheduleNewDimensionChange(new ScheduledDimensionChange(playerBase, (String) PacketSimple.this.data.get(0)));
            break;
        case S_IGNITE_ROCKET:
            if (!player.worldObj.isRemote && !player.isDead && player.getRidingEntity() != null && !player.getRidingEntity().isDead && player.getRidingEntity() instanceof EntityTieredRocket)
            {
                final EntityTieredRocket ship = (EntityTieredRocket) player.getRidingEntity();

                if (!ship.landing)
                {
                    if (ship.hasValidFuel())
                    {
                        ItemStack stack2 = stats.getExtendedInventory().getStackInSlot(4);

                        if (stack2 != null && stack2.getItem() instanceof ItemParaChute || stats.getLaunchAttempts() > 0)
                        {
                            ship.igniteCheckingCooldown();
                            stats.setLaunchAttempts(0);
                        }
                        else if (stats.getChatCooldown() == 0 && stats.getLaunchAttempts() == 0)
                        {
                            player.addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.rocket.warning.noparachute")));
                            stats.setChatCooldown(250);
                            stats.setLaunchAttempts(1);
                        }
                    }
                    else if (stats.getChatCooldown() == 0)
                    {
                        player.addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.rocket.warning.nofuel")));
                        stats.setChatCooldown(250);
                    }
                }
            }
            break;
        case S_OPEN_FUEL_GUI:
            if (player.getRidingEntity() instanceof EntityBuggy)
            {
                LabStuffUtils.openBuggyInv(playerBase, (EntityBuggy) player.getRidingEntity(), ((EntityBuggy) player.getRidingEntity()).getType());
            }
            else if (player.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                player.openGui(LabStuffMain.instance, 33, player.worldObj, (int) player.posX, (int) player.posY, (int) player.posZ);
            }
            break;
        case S_UPDATE_SHIP_YAW:
            if (player.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getRidingEntity();

                if (ship != null)
                {
                    ship.rotationYaw = (Float) this.data.get(0);
                }
            }
            break;
        case S_UPDATE_SHIP_PITCH:
            if (player.getRidingEntity() instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) player.getRidingEntity();

                if (ship != null)
                {
                    ship.rotationPitch = (Float) this.data.get(0);
                }
            }
            break;
        case S_SET_ENTITY_FIRE:
            Entity entity = player.worldObj.getEntityByID((Integer) this.data.get(0));

            if (entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase) entity).setFire(3);
            }
            break;
        case S_BIND_SPACE_STATION_ID:
            int homeID = (Integer) this.data.get(0);
            if ((!stats.getSpaceStationDimensionData().containsKey(homeID) || stats.getSpaceStationDimensionData().get(homeID) == -1 || stats.getSpaceStationDimensionData().get(homeID) == 0))
            {
                if (playerBase.capabilities.isCreativeMode || WorldUtil.getSpaceStationRecipe(homeID).matches(playerBase, true))
                {
                    WorldUtil.bindSpaceStationToNewDimension(playerBase.worldObj, playerBase, homeID);
                }
            }
            break;
        case S_UPDATE_DISABLEABLE_BUTTON:
            final TileEntity tileAt = player.worldObj.getTileEntity((BlockPos) this.data.get(0));

            if (tileAt instanceof IDisableableMachine)
            {
                final IDisableableMachine machine = (IDisableableMachine) tileAt;

                machine.setDisabled((Integer) this.data.get(1), !machine.getDisabled((Integer) this.data.get(1)));
            }
            break;
        case S_RENAME_SPACE_STATION:
            final SpaceStationWorldData ssdata = SpaceStationWorldData.getStationData(playerBase.worldObj, (Integer) this.data.get(1), playerBase);

            if (ssdata != null && ssdata.getOwner().equalsIgnoreCase(player.getGameProfile().getName()))
            {
                ssdata.setSpaceStationName((String) this.data.get(0));
                ssdata.setDirty(true);
            }
            break;
        case S_OPEN_EXTENDED_INVENTORY:
            player.openGui(LabStuffMain.instance, 32, player.worldObj, 0, 0, 0);
            break;
        case S_ON_ADVANCED_GUI_CLICKED_INT:
            TileEntity tile1 = player.worldObj.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.redstoneActivation = (Integer) this.data.get(2) == 1;
                }
                break;
            case 1:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceActivation = (Integer) this.data.get(2) == 1;
                }
                break;
            case 2:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerDistanceSelection = (Integer) this.data.get(2);
                }
                break;
            case 3:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.playerNameMatches = (Integer) this.data.get(2) == 1;
                }
                break;
            case 4:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.invertSelection = (Integer) this.data.get(2) == 1;
                }
                break;
            case 5:
                if (tile1 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile1;
                    airlockController.lastHorizontalModeEnabled = airlockController.horizontalModeEnabled;
                    airlockController.horizontalModeEnabled = (Integer) this.data.get(2) == 1;
                }
                break;
            case 6:
                if (tile1 instanceof IBubbleProvider)
                {
                    IBubbleProvider distributor = (IBubbleProvider) tile1;
                    distributor.setBubbleVisible((Integer) this.data.get(2) == 1);
                }
                break;
            default:
                break;
            }
            break;
        case S_ON_ADVANCED_GUI_CLICKED_STRING:
            TileEntity tile2 = player.worldObj.getTileEntity((BlockPos) this.data.get(1));

            switch ((Integer) this.data.get(0))
            {
            case 0:
                if (tile2 instanceof TileEntityAirLockController)
                {
                    TileEntityAirLockController airlockController = (TileEntityAirLockController) tile2;
                    airlockController.playerToOpenFor = (String) this.data.get(2);
                }
                break;
            default:
                break;
            }
            break;
        case S_UPDATE_SHIP_MOTION_Y:
            int entityID = (Integer) this.data.get(0);
            boolean up = (Boolean) this.data.get(1);

            Entity entity2 = player.worldObj.getEntityByID(entityID);

            if (entity2 instanceof EntityAutoRocket)
            {
                EntityAutoRocket autoRocket = (EntityAutoRocket) entity2;
                autoRocket.motionY += up ? 0.02F : -0.02F;
            }

            break;
        case S_COMPLETE_CBODY_HANDSHAKE:
            String completeList = (String) this.data.get(0);
            List<String> clientObjects = Arrays.asList(completeList.split(";"));
            List<String> serverObjects = Lists.newArrayList();
            String missingObjects = "";

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredPlanets().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredMoons().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (CelestialBody cBody : GalaxyRegistry.getRegisteredSatellites().values())
            {
                serverObjects.add(cBody.getUnlocalizedName());
            }

            for (SolarSystem solarSystem : GalaxyRegistry.getRegisteredSolarSystems().values())
            {
                serverObjects.add(solarSystem.getUnlocalizedName());
            }

            for (String str : serverObjects)
            {
                if (!clientObjects.contains(str))
                {
                    missingObjects = missingObjects.concat(str + "\n");
                }
            }

            if (missingObjects.length() > 0)
            {
                playerBase.connection.kickPlayerFromServer("Missing LabStuff Celestial Objects:\n\n " + missingObjects);
            }

            break;
        case S_REQUEST_GEAR_DATA:
            String name = (String) this.data.get(0);
            EntityPlayerMP e = PlayerUtil.getPlayerBaseServerFromPlayerUsername(name, true);
            if (e != null)
            {
                LSPlayerHandler.checkGear(e, LSPlayerStats.get(e), true);
            }
            break;
        case S_BUILDFLAGS_UPDATE:
            stats.setBuildFlags((Integer) this.data.get(0));
            break;
        case S_REQUEST_OVERWORLD_IMAGE:
            MapUtil.sendOverworldToClient(playerBase);
            break;
        case S_REQUEST_MAP_IMAGE:
            int dim = (Integer) this.data.get(0);
            int cx = (Integer) this.data.get(1);
            int cz = (Integer) this.data.get(2);
            MapUtil.sendOrCreateMap(WorldUtil.getWorldForDimensionServer(dim), cx, cz, playerBase);
            break;
        case S_REQUEST_PLAYERSKIN:
            String strName = (String) this.data.get(0);
            EntityPlayerMP playerRequested = FMLServerHandler.instance().getServer().getPlayerList().getPlayerByUsername(strName);

            //Player not online
            if (playerRequested == null)
            {
                return;
            }

            GameProfile gp = playerRequested.getGameProfile();
            if (gp == null)
            {
                return;
            }

            Property property = (Property) Iterables.getFirst(gp.getProperties().get("textures"), (Object) null);
            if (property == null)
            {
                return;
            }
            LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SEND_PLAYERSKIN, getDimensionID(), new Object[] { strName, property.getValue(), property.getSignature(), playerRequested.getUniqueID().toString() }), playerBase);
            break;
        case S_CONTROL_ENTITY:
            if (player.getRidingEntity() != null && player.getRidingEntity() instanceof IControllableEntity)
            {
                ((IControllableEntity) player.getRidingEntity()).pressKey((Integer) this.data.get(0));
            }
            break;
        case S_REQUEST_DATA:
            WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension((Integer) this.data.get(0));
            if (worldServer != null)
            {
            }
            break;
        case S_UPDATE_CHECKLIST:
            for (EnumHand enumhand : EnumHand.values())
            {
                ItemStack stack = player.getHeldItem(enumhand);
                if (stack != null && stack.getItem() == LabStuffMain.prelaunchChecklist)
                {
                    NBTTagCompound tagCompound = stack.getTagCompound();
                    if (tagCompound == null)
                    {
                        tagCompound = new NBTTagCompound();
                    }
                    NBTTagCompound tagCompoundRead = (NBTTagCompound) this.data.get(0);
                    tagCompound.setTag("checklistData", tagCompoundRead);
                    stack.setTagCompound(tagCompound);
                }
            }
            break;
        case S_REQUEST_MACHINE_DATA:
            TileEntity tile3 = player.worldObj.getTileEntity((BlockPos) this.data.get(0));
            if (tile3 instanceof ITileClientUpdates)
            {
                ((ITileClientUpdates)tile3).sendUpdateToClient(playerBase);
            }
            break;
        case S_UPDATE_CARGO_ROCKET_STATUS:
            Entity entity3 = player.worldObj.getEntityByID((Integer) this.data.get(0));

            if (entity3 instanceof EntityCargoRocket)
            {
                EntityCargoRocket rocket = (EntityCargoRocket) entity3;

                int subType = (Integer) this.data.get(1);

                switch (subType)
                {
                default:
                    rocket.statusValid = rocket.checkLaunchValidity();
                    break;
                }
            }
            break;
        default:
            break;
        }
    }

	/*
     *
	 * BEGIN "net.minecraft.network.Packet" IMPLEMENTATION
	 * 
	 * This is for handling server->client packets before the player has joined the world
	 * 
	 */

    @Override
    public void readPacketData(PacketBuffer var1)
    {
        this.decodeInto(var1);
    }

    @Override
    public void writePacketData(PacketBuffer var1)
    {
        this.encodeInto(var1);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void processPacket(INetHandler var1)
    {
        if (this.type != EnumSimplePacket.C_UPDATE_SPACESTATION_LIST && this.type != EnumSimplePacket.C_UPDATE_PLANETS_LIST && this.type != EnumSimplePacket.C_UPDATE_CONFIGS)
        {
            return;
        }

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            this.handleClientSide(FMLClientHandler.instance().getClientPlayerEntity());
        }
    }

	/*
     *
	 * END "net.minecraft.network.Packet" IMPLEMENTATION
	 * 
	 * This is for handling server->client packets before the player has joined the world
	 * 
	 */
}