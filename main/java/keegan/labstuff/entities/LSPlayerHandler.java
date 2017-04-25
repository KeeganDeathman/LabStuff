package keegan.labstuff.entities;

import java.util.HashMap;
import java.util.Map.Entry;

import keegan.labstuff.*;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.blocks.BlockUnlitTorch;
import keegan.labstuff.common.EnumColor;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.dimension.*;
import keegan.labstuff.event.LSCoreOxygenSuffocationEvent;
import keegan.labstuff.items.*;
import keegan.labstuff.tileentity.TileEntityTelemetry;
import keegan.labstuff.util.*;
import keegan.labstuff.world.ILabstuffWorldProvider;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.*;

public class LSPlayerHandler
{
    private static final int OXYGENHEIGHTLIMIT = 450;
//    private ConcurrentHashMap<UUID, LSPlayerStats> playerStatsMap = new ConcurrentHashMap<UUID, LSPlayerStats>();
    private HashMap<Item, Item> torchItems = new HashMap<Item, Item>();

//    public ConcurrentHashMap<UUID, LSPlayerStats> getServerStatList()
//    {
//        return this.playerStatsMap;
//    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event)
    {
        if (event.player instanceof EntityPlayerMP)
        {
            this.onPlayerLogin((EntityPlayerMP) event.player);
        }
    }

//    @SubscribeEvent
//    public void onPlayerLogout(PlayerLoggedOutEvent event)
//    {
//        if (event.player instanceof EntityPlayerMP)
//        {
//            this.onPlayerLogout((EntityPlayerMP) event.player);
//        }
//    }
//
//    @SubscribeEvent
//    public void onPlayerRespawn(PlayerRespawnEvent event)
//    {
//        if (event.player instanceof EntityPlayerMP)
//        {
//            this.onPlayerRespawn((EntityPlayerMP) event.player);
//        }
//    }

    @SubscribeEvent
    public void onPlayerCloned(PlayerEvent.Clone event)
    {
        if (event.isWasDeath())
        {
            LSPlayerStats oldStats = LSPlayerStats.get(event.getOriginal());
            LSPlayerStats newStats = LSPlayerStats.get(event.getEntityPlayer());

            newStats.copyFrom(oldStats, false);
        }
    }

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof EntityPlayerMP)
        {
            event.addCapability(Capabilities.LS_PLAYER_PROP, new CapabilityProviderStats((EntityPlayerMP) event.getObject()));
        }
        else if (event.getObject() instanceof EntityPlayer && ((EntityPlayer)event.getObject()).worldObj.isRemote)
        {
            this.onAttachCapabilityClient(event);
        }
    }
    
    @SideOnly(Side.CLIENT)
    private void onAttachCapabilityClient(AttachCapabilitiesEvent event)
    {
        if (event.getObject() instanceof EntityPlayerSP)
        {
            event.addCapability(Capabilities.LS_PLAYER_CLIENT_PROP, new CapabilityProviderStatsClient((EntityPlayerSP) event.getObject()));
        }
    }

//    @SubscribeEvent
//    public void onEntityConstructing(EntityEvent.EntityConstructing event)
//    {
//        if (event.getEntity() instanceof EntityPlayerMP && LSPlayerStats.get((EntityPlayerMP) event.getEntity()) == null)
//        {
//            LSPlayerStats.register((EntityPlayerMP) event.getEntity());
//        }
//
//        if (event.entity instanceof EntityPlayer && event.entity.worldObj.isRemote)
//        {
//            this.onEntityConstructingClient(event);
//        }
//    }
//
//    @SideOnly(Side.CLIENT)
//    public void onEntityConstructingClient(EntityEvent.EntityConstructing event)
//    {
//        if (event.getEntity() instanceof EntityPlayerSP)
//        {
//            if (LSPlayerStatsClient.get((EntityPlayerSP) event.getEntity()) == null)
//            {
//                LSPlayerStatsClient.register((EntityPlayerSP) event.getEntity());
//            }
//
//            Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
//        }
//    }

    private void onPlayerLogin(EntityPlayerMP player)
    {
//        LSPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
//        if (oldData != null)
//        {
//            oldData.saveNBTData(player.getEntityData());
//        }

        LSPlayerStats stats = LSPlayerStats.get(player);

        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_GET_CELESTIAL_BODY_LIST, LabStuffUtils.getDimensionID(player.worldObj), new Object[] {}), player);
        int repeatCount = stats.getBuildFlags() >> 9;
        if (repeatCount < 3)
        {
            stats.setBuildFlags(stats.getBuildFlags() & 1536);
        }
        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATS, LabStuffUtils.getDimensionID(player.worldObj.provider), new Object[] { stats.getBuildFlags() }), player);
        ColorUtil.sendUpdatedColorsToPlayer(stats);
    }

//    private void onPlayerLogout(EntityPlayerMP player)
//    {
//    }
//
//    private void onPlayerRespawn(EntityPlayerMP player)
//    {
//        LSPlayerStats oldData = this.playerStatsMap.remove(player.getPersistentID());
//        LSPlayerStats stats = LSPlayerStats.get(player);
//
//        if (oldData != null)
//        {
//            stats.copyFrom(oldData, false);
//        }
//
//        stats.player = new WeakReference<EntityPlayerMP>(player);
//    }

    public static void checkGear(EntityPlayerMP player, LSPlayerStats stats, boolean forceSend)
    {
        stats.setMaskInSlot(stats.getExtendedInventory().getStackInSlot(0));
        stats.setGearInSlot(stats.getExtendedInventory().getStackInSlot(1));
        stats.setTankInSlot1(stats.getExtendedInventory().getStackInSlot(2));
        stats.setTankInSlot2(stats.getExtendedInventory().getStackInSlot(3));
        stats.setParachuteInSlot(stats.getExtendedInventory().getStackInSlot(4));
        stats.setFrequencyModuleInSlot(stats.getExtendedInventory().getStackInSlot(5));
        stats.setThermalHelmetInSlot(stats.getExtendedInventory().getStackInSlot(6));
        stats.setThermalChestplateInSlot(stats.getExtendedInventory().getStackInSlot(7));
        stats.setThermalLeggingsInSlot(stats.getExtendedInventory().getStackInSlot(8));
        stats.setThermalBootsInSlot(stats.getExtendedInventory().getStackInSlot(9));
        stats.setShieldControllerInSlot(stats.getExtendedInventory().getStackInSlot(10));
        //

        if (stats.getFrequencyModuleInSlot() != stats.getLastFrequencyModuleInSlot() || forceSend)
        {
            if (FMLCommonHandler.instance().getMinecraftServerInstance() != null)
            {
                if (stats.getFrequencyModuleInSlot() == null)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.FREQUENCY_MODULE);
                    TileEntityTelemetry.frequencyModulePlayer(stats.getLastFrequencyModuleInSlot(), null);
                }
                else if (stats.getLastFrequencyModuleInSlot() == null)
                {
                    int gearID = LabStuffRegistry.findMatchingGearID(stats.getFrequencyModuleInSlot(), EnumExtendedInventorySlot.FREQUENCY_MODULE);

                    if (gearID >= 0)
                    {
                        LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.FREQUENCY_MODULE, gearID);
                        TileEntityTelemetry.frequencyModulePlayer(stats.getFrequencyModuleInSlot(), player);
                    }
                }
            }

            stats.setLastFrequencyModuleInSlot(stats.getFrequencyModuleInSlot());
        }

        //

        if (stats.getMaskInSlot() != stats.getLastMaskInSlot() || forceSend)
        {
            if (stats.getMaskInSlot() == null)
            {
                LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.MASK);
            }
            else if (stats.getLastMaskInSlot() == null || forceSend)
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getMaskInSlot(), EnumExtendedInventorySlot.MASK);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.MASK, gearID);
                }
            }

            stats.setLastMaskInSlot(stats.getMaskInSlot());
        }

        //

        if (stats.getGearInSlot() != stats.getLastGearInSlot() || forceSend)
        {
            if (stats.getGearInSlot() == null)
            {
                LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.GEAR);
            }
            else if (stats.getGearInSlot().getItem() == LabStuffMain.oxygenGear && (stats.getLastGearInSlot() == null || forceSend))
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getGearInSlot(), EnumExtendedInventorySlot.GEAR);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.GEAR, gearID);
                }
            }

            stats.setLastGearInSlot(stats.getGearInSlot());
        }

        //

        if (stats.getTankInSlot1() != stats.getLastTankInSlot1() || forceSend)
        {
            if (stats.getTankInSlot1() == null)
            {
                LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.LEFT_TANK);
                stats.setAirRemaining(0);
                LSPlayerHandler.sendAirRemainingPacket(player, stats);
            }
            else if (stats.getLastTankInSlot1() == null || forceSend)
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getTankInSlot1(), EnumExtendedInventorySlot.LEFT_TANK);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.LEFT_TANK, gearID);
                }
                stats.setAirRemaining(stats.getTankInSlot1().getMaxDamage() - stats.getTankInSlot1().getItemDamage());
                LSPlayerHandler.sendAirRemainingPacket(player, stats);
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (stats.getTankInSlot1().getItem() != stats.getLastTankInSlot1().getItem())
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getTankInSlot1(), EnumExtendedInventorySlot.LEFT_TANK);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.LEFT_TANK, gearID);
                }
                stats.setAirRemaining(stats.getTankInSlot1().getMaxDamage() - stats.getTankInSlot1().getItemDamage());
                LSPlayerHandler.sendAirRemainingPacket(player, stats);
            }

            stats.setLastTankInSlot1(stats.getTankInSlot1());
        }

        //

        if (stats.getTankInSlot2() != stats.getLastTankInSlot2() || forceSend)
        {
            if (stats.getTankInSlot2() == null)
            {
                LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.RIGHT_TANK);
                stats.setAirRemaining2(0);
                LSPlayerHandler.sendAirRemainingPacket(player, stats);
            }
            else if (stats.getLastTankInSlot2() == null || forceSend)
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getTankInSlot2(), EnumExtendedInventorySlot.RIGHT_TANK);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.RIGHT_TANK, gearID);
                }
                stats.setAirRemaining2(stats.getTankInSlot2().getMaxDamage() - stats.getTankInSlot2().getItemDamage());
                LSPlayerHandler.sendAirRemainingPacket(player, stats);
            }
            //if the else is reached then both tankInSlot and lastTankInSlot are non-null
            else if (stats.getTankInSlot2().getItem() != stats.getLastTankInSlot2().getItem())
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getTankInSlot2(), EnumExtendedInventorySlot.RIGHT_TANK);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.RIGHT_TANK, gearID);
                }
                stats.setAirRemaining2(stats.getTankInSlot2().getMaxDamage() - stats.getTankInSlot2().getItemDamage());
                LSPlayerHandler.sendAirRemainingPacket(player, stats);
            }

            stats.setLastTankInSlot2(stats.getTankInSlot2());
        }

        //

        if (stats.getParachuteInSlot() != stats.getLastParachuteInSlot() || forceSend)
        {
            if (stats.getParachuteInSlot() == null)
            {
                if (stats.isUsingParachute())
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.PARACHUTE);
                }
            }
            else if (stats.getLastParachuteInSlot() == null || forceSend)
            {
                if (stats.isUsingParachute())
                {
                    int gearID = LabStuffRegistry.findMatchingGearID(stats.getParachuteInSlot(), EnumExtendedInventorySlot.PARACHUTE);

                    if (gearID >= 0)
                    {
                        LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, stats.getParachuteInSlot().getItemDamage());
                    }
                }
            }
            else if (stats.getParachuteInSlot().getItemDamage() != stats.getLastParachuteInSlot().getItemDamage())
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getParachuteInSlot(), EnumExtendedInventorySlot.PARACHUTE);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, stats.getParachuteInSlot().getItemDamage());
                }
            }

            stats.setLastParachuteInSlot(stats.getParachuteInSlot());
        }

        //

        if (stats.getThermalHelmetInSlot() != stats.getLastThermalHelmetInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(0, stats.getThermalHelmetInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalHelmetInSlot() == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_HELMET);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalHelmetInSlot() == null || forceSend))
                {
                    int gearID = LabStuffRegistry.findMatchingGearID(stats.getThermalHelmetInSlot(), EnumExtendedInventorySlot.THERMAL_HELMET);

                    if (gearID >= 0)
                    {
                        LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_HELMET, gearID);
                    }
                }
            }

            stats.setLastThermalHelmetInSlot(stats.getThermalHelmetInSlot());
        }

        if (stats.getThermalChestplateInSlot() != stats.getLastThermalChestplateInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(1, stats.getThermalChestplateInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalChestplateInSlot() == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_CHESTPLATE);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalChestplateInSlot() == null || forceSend))
                {
                    int gearID = LabStuffRegistry.findMatchingGearID(stats.getThermalChestplateInSlot(), EnumExtendedInventorySlot.THERMAL_CHESTPLATE);

                    if (gearID >= 0)
                    {
                        LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_CHESTPLATE, gearID);
                    }
                }
            }

            stats.setLastThermalChestplateInSlot(stats.getThermalChestplateInSlot());
        }

        if (stats.getThermalLeggingsInSlot() != stats.getLastThermalLeggingsInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(2, stats.getThermalLeggingsInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalLeggingsInSlot() == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_LEGGINGS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalLeggingsInSlot() == null || forceSend))
                {
                    int gearID = LabStuffRegistry.findMatchingGearID(stats.getThermalLeggingsInSlot(), EnumExtendedInventorySlot.THERMAL_LEGGINGS);

                    if (gearID >= 0)
                    {
                        LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_LEGGINGS, gearID);
                    }
                }
            }

            stats.setLastThermalLeggingsInSlot(stats.getThermalLeggingsInSlot());
        }

        if (stats.getThermalBootsInSlot() != stats.getLastThermalBootsInSlot() || forceSend)
        {
            ThermalArmorEvent armorEvent = new ThermalArmorEvent(3, stats.getThermalBootsInSlot());
            MinecraftForge.EVENT_BUS.post(armorEvent);

            if (armorEvent.armorResult != ThermalArmorEvent.ArmorAddResult.NOTHING)
            {
                if (stats.getThermalBootsInSlot() == null || armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.REMOVE)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.THERMAL_BOOTS);
                }
                else if (armorEvent.armorResult == ThermalArmorEvent.ArmorAddResult.ADD && (stats.getLastThermalBootsInSlot() == null || forceSend))
                {
                    int gearID = LabStuffRegistry.findMatchingGearID(stats.getThermalBootsInSlot(), EnumExtendedInventorySlot.THERMAL_BOOTS);

                    if (gearID >= 0)
                    {
                        LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.THERMAL_BOOTS, gearID);
                    }
                }
            }

            stats.setLastThermalBootsInSlot(stats.getThermalBootsInSlot());
        }

        if ((stats.getShieldControllerInSlot() != stats.getLastShieldControllerInSlot() || forceSend))
        {
            if (stats.getShieldControllerInSlot() == null)
            {
                LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.SHIELD_CONTROLLER);
            }
            else if (stats.getShieldControllerInSlot().getItem() == LabStuffMain.shieldController && (stats.getLastShieldControllerInSlot() == null || forceSend))
            {
                int gearID = LabStuffRegistry.findMatchingGearID(stats.getShieldControllerInSlot(), EnumExtendedInventorySlot.SHIELD_CONTROLLER);

                if (gearID >= 0)
                {
                    LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.SHIELD_CONTROLLER, gearID);
                }
            }

            stats.setLastShieldControllerInSlot(stats.getShieldControllerInSlot());
        }
    }

    protected void checkThermalStatus(EntityPlayerMP player, LSPlayerStats playerStats)
    {
        if (player.worldObj.provider instanceof ILabstuffWorldProvider && !player.capabilities.isCreativeMode)
        {
            final ItemStack thermalPaddingHelm = playerStats.getExtendedInventory().getStackInSlot(6);
            final ItemStack thermalPaddingChestplate = playerStats.getExtendedInventory().getStackInSlot(7);
            final ItemStack thermalPaddingLeggings = playerStats.getExtendedInventory().getStackInSlot(8);
            final ItemStack thermalPaddingBoots = playerStats.getExtendedInventory().getStackInSlot(9);
            float lowestThermalStrength = 0.0F;
            if (thermalPaddingHelm != null && thermalPaddingChestplate != null && thermalPaddingLeggings != null && thermalPaddingBoots != null)
            {
                if (thermalPaddingHelm.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingHelm.getItem()).getThermalStrength();
                }
                if (thermalPaddingChestplate.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingChestplate.getItem()).getThermalStrength();
                }
                if (thermalPaddingLeggings.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingLeggings.getItem()).getThermalStrength();
                }
                if (thermalPaddingBoots.getItem() instanceof IItemThermal)
                {
                    lowestThermalStrength += ((IItemThermal) thermalPaddingBoots.getItem()).getThermalStrength();
                }
                lowestThermalStrength /= 4.0F;
            }

            ILabstuffWorldProvider provider = (ILabstuffWorldProvider) player.worldObj.provider;
            float thermalLevelMod = provider.getThermalLevelModifier();
            double absThermalLevelMod = Math.abs(thermalLevelMod);

            if (absThermalLevelMod > 0D)
            {
                int thermalLevelCooldownBase = Math.abs(MathHelper.floor_double(200 / thermalLevelMod));
                int normaliseCooldown = Math.abs(MathHelper.floor_double(150 / lowestThermalStrength));
                int thermalLevelTickCooldown = thermalLevelCooldownBase;
                if (thermalLevelTickCooldown < 1)
                {
                    thermalLevelTickCooldown = 1;   //Prevent divide by zero errors
                }

                if (thermalPaddingHelm != null && thermalPaddingChestplate != null && thermalPaddingLeggings != null && thermalPaddingBoots != null)
                {
                    thermalLevelMod /= Math.max(1.0F, lowestThermalStrength / 2.0F);
                    absThermalLevelMod = Math.abs(thermalLevelMod);
                    normaliseCooldown = MathHelper.floor_double(normaliseCooldown / absThermalLevelMod);
                    if (normaliseCooldown < 1)
                    {
                        normaliseCooldown = 1;   //Prevent divide by zero errors
                    }
                    // Player is wearing all required thermal padding items
                    if ((player.ticksExisted - 1) % normaliseCooldown == 0)
                    {
                        this.normaliseThermalLevel(player, playerStats, 1);
                    }
                }

                if (OxygenUtil.isAABBInBreathableAirBlock(player, true))
                {
                    playerStats.setThermalLevelNormalising(true);
                    this.normaliseThermalLevel(player, playerStats, 1);
                    // If player is in ambient thermal area, slowly reset to normal
                    return;
                }

                // For each piece of thermal equipment being used, slow down the the harmful thermal change slightly
                if (thermalPaddingHelm != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (thermalPaddingChestplate != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (thermalPaddingLeggings != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }
                if (thermalPaddingBoots != null)
                {
                    thermalLevelTickCooldown += thermalLevelCooldownBase;
                }

                // Instead of increasing/decreasing the thermal level by a large amount every ~200 ticks, increase/decrease
                //      by a small amount each time (still the same average increase/decrease)
                int thermalLevelTickCooldownSingle = MathHelper.floor_double(thermalLevelTickCooldown / absThermalLevelMod);
                if (thermalLevelTickCooldownSingle < 1)
                {
                    thermalLevelTickCooldownSingle = 1;   //Prevent divide by zero errors
                }

                if ((player.ticksExisted - 1) % thermalLevelTickCooldownSingle == 0)
                {
                    int last = playerStats.getThermalLevel();
                    playerStats.setThermalLevel((int) Math.min(Math.max(last + (thermalLevelMod < 0 ? -1 : 1), -22), 22));

                    if (playerStats.getThermalLevel() != last)
                    {
                        this.sendThermalLevelPacket(player, playerStats);
                    }
                }

                // If the normalisation is outpacing the freeze/overheat
                playerStats.setThermalLevelNormalising(thermalLevelTickCooldownSingle > normaliseCooldown &&
                        thermalPaddingHelm != null &&
                        thermalPaddingChestplate != null &&
                        thermalPaddingLeggings != null &&
                        thermalPaddingBoots != null);

                if (!playerStats.isThermalLevelNormalising())
                {
                    if ((player.ticksExisted - 1) % thermalLevelTickCooldown == 0)
                    {
                        if (Math.abs(playerStats.getThermalLevel()) >= 22)
                        {
                            player.attackEntityFrom(DamageSourceLS.thermal, 1.5F);
                        }
                    }

                    if (playerStats.getThermalLevel() < -15)
                    {
                        player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 5, 2, true, true));
                    }

                    if (playerStats.getThermalLevel() > 15)
                    {
                        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 5, 2, true, true));
                    }
                }
            }
            else
            //Normalise thermal level if on Space Station or non-modifier planet
            {
                playerStats.setThermalLevelNormalising(true);
                this.normaliseThermalLevel(player, playerStats, 2);
            }
        }
        else
        //Normalise thermal level if on Overworld or any non-GC dimension
        {
            playerStats.setThermalLevelNormalising(true);
            this.normaliseThermalLevel(player, playerStats, 3);
        }
    }

    public void normaliseThermalLevel(EntityPlayerMP player, LSPlayerStats stats, int increment)
    {
        final int last = stats.getThermalLevel();

        if (stats.getThermalLevel() < 0)
        {
            stats.setThermalLevel(stats.getThermalLevel() + Math.min(increment, -stats.getThermalLevel()));
        }
        else if (stats.getThermalLevel() > 0)
        {
            stats.setThermalLevel(stats.getThermalLevel() - Math.min(increment, stats.getThermalLevel()));
        }

        if (stats.getThermalLevel() != last)
        {
            this.sendThermalLevelPacket(player, stats);
        }
    }

    protected void checkShield(EntityPlayerMP playerMP, LSPlayerStats playerStats)
    {
        if (playerMP.ticksExisted % 20 == 0 && playerMP.worldObj.provider instanceof ILabstuffWorldProvider)
        {
            if (((ILabstuffWorldProvider) playerMP.worldObj.provider).shouldCorrodeArmor())
            {
                ItemStack shieldController = playerStats.getExtendedInventory().getStackInSlot(10);
                boolean valid = false;

                if (shieldController != null)
                {
                    int gearID = LabStuffRegistry.findMatchingGearID(shieldController, EnumExtendedInventorySlot.SHIELD_CONTROLLER);

                    if (gearID != -1)
                    {
                        valid = true;
                    }
                }

                if (!valid)
                {
                    for (ItemStack armor : playerMP.getArmorInventoryList())
                    {
                        if (armor != null && armor.getItem() instanceof ItemArmor)
                        {
                            armor.damageItem(1, playerMP);
                        }
                    }
                }
            }
        }
    }

    protected void checkOxygen(EntityPlayerMP player, LSPlayerStats stats)
    {
        if ((player.dimension == 0 || player.worldObj.provider instanceof ILabstuffWorldProvider) && (!(player.dimension == 0 || ((ILabstuffWorldProvider) player.worldObj.provider).hasBreathableAtmosphere()) || player.posY > LSPlayerHandler.OXYGENHEIGHTLIMIT) && !player.capabilities.isCreativeMode && !(player.getRidingEntity() instanceof EntityLanderBase) && !(player.getRidingEntity() instanceof EntityAutoRocket) && !(player.getRidingEntity() instanceof EntityCelestialFake))
        {
            final ItemStack tankInSlot = stats.getExtendedInventory().getStackInSlot(2);
            final ItemStack tankInSlot2 = stats.getExtendedInventory().getStackInSlot(3);

            final int drainSpacing = OxygenUtil.getDrainSpacing(tankInSlot, tankInSlot2);

            if (tankInSlot == null)
            {
                stats.setAirRemaining(0);
            }
            else
            {
                stats.setAirRemaining(tankInSlot.getMaxDamage() - tankInSlot.getItemDamage());
            }

            if (tankInSlot2 == null)
            {
                stats.setAirRemaining2(0);
            }
            else
            {
                stats.setAirRemaining2(tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage());
            }

            if (drainSpacing > 0)
            {
                if ((player.ticksExisted - 1) % drainSpacing == 0 && !OxygenUtil.isAABBInBreathableAirBlock(player) && !stats.isUsingPlanetSelectionGui())
                {
                    int toTake = 1;
                    //Take 1 oxygen from Tank 1
                    if (stats.getAirRemaining() > 0)
                    {
                        tankInSlot.damageItem(1, player);
                        stats.setAirRemaining(stats.getAirRemaining() - 1);
                        toTake = 0;
                    }

                    //Alternatively, take 1 oxygen from Tank 2
                    if (toTake > 0 && stats.getAirRemaining2() > 0)
                    {
                        tankInSlot2.damageItem(1, player);
                        stats.setAirRemaining2(stats.getAirRemaining2() - 1);
                        toTake = 0;
                    }
                }
            }
            else
            {
                if ((player.ticksExisted - 1) % 60 == 0)
                {
                    if (OxygenUtil.isAABBInBreathableAirBlock(player))
                    {
                        if (stats.getAirRemaining() < 90 && tankInSlot != null)
                        {
                            stats.setAirRemaining(Math.min(stats.getAirRemaining() + 1, tankInSlot.getMaxDamage() - tankInSlot.getItemDamage()));
                        }

                        if (stats.getAirRemaining2() < 90 && tankInSlot2 != null)
                        {
                            stats.setAirRemaining2(Math.min(stats.getAirRemaining2() + 1, tankInSlot2.getMaxDamage() - tankInSlot2.getItemDamage()));
                        }
                    }
                    else
                    {
                        if (stats.getAirRemaining() > 0)
                        {
                            stats.setAirRemaining(stats.getAirRemaining() - 1);
                        }

                        if (stats.getAirRemaining2() > 0)
                        {
                            stats.setAirRemaining2(stats.getAirRemaining2() - 1);
                        }
                    }
                }
            }

            final boolean airEmpty = stats.getAirRemaining() <= 0 && stats.getAirRemaining2() <= 0;

            if (player.isOnLadder())
            {
                stats.setOxygenSetupValid(stats.isLastOxygenSetupValid());
            }
            else
            {
                stats.setOxygenSetupValid(!((!OxygenUtil.hasValidOxygenSetup(player) || airEmpty) && !OxygenUtil.isAABBInBreathableAirBlock(player)));
            }

            if (!player.worldObj.isRemote && player.isEntityAlive())
            {
            	if (!stats.isOxygenSetupValid())
            	{
        			LSCoreOxygenSuffocationEvent suffocationEvent = new LSCoreOxygenSuffocationEvent.Pre(player);
        			MinecraftForge.EVENT_BUS.post(suffocationEvent);

        			if (!suffocationEvent.isCanceled())
        			{
                		if (stats.getDamageCounter() == 0)
                		{
                			stats.setDamageCounter(ConfigManagerCore.suffocationCooldown);

            				player.attackEntityFrom(DamageSourceLS.oxygenSuffocation, ConfigManagerCore.suffocationDamage * (2 + stats.getIncrementalDamage()) / 2);

            				LSCoreOxygenSuffocationEvent suffocationEventPost = new LSCoreOxygenSuffocationEvent.Post(player);
            				MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                		}
        			}
        			else
        				stats.setOxygenSetupValid(true);
            	}
        		else
        			stats.setIncrementalDamage(0);
            }
        }
        else if ((player.ticksExisted - 1) % 20 == 0 && !player.capabilities.isCreativeMode && stats.getAirRemaining() < 90)
        {
            stats.setAirRemaining(stats.getAirRemaining() + 1);
            stats.setAirRemaining2(stats.getAirRemaining2() + 1);
        }
        else if (player.capabilities.isCreativeMode)
        {
            stats.setAirRemaining(90);
            stats.setAirRemaining2(90);
        }
        else
        {
            stats.setOxygenSetupValid(true);
        }
    }

    protected void throwMeteors(EntityPlayerMP player)
    {
        World world = player.worldObj;
        if (world.provider instanceof ILabstuffWorldProvider && !world.isRemote)
        {
            if (((ILabstuffWorldProvider) world.provider).getMeteorFrequency() > 0 && ConfigManagerCore.meteorSpawnMod > 0.0)
            {
                final int f = (int) (((ILabstuffWorldProvider) world.provider).getMeteorFrequency() * 1000D * (1.0 / ConfigManagerCore.meteorSpawnMod));

                if (world.rand.nextInt(f) == 0)
                {
                    final EntityPlayer closestPlayer = world.getClosestPlayerToEntity(player, 100);

                    if (closestPlayer == null || closestPlayer.getEntityId() <= player.getEntityId())
                    {
                        int x, y, z;
                        double motX, motZ;
                        x = world.rand.nextInt(20) - 10;
                        y = world.rand.nextInt(20) + 200;
                        z = world.rand.nextInt(20) - 10;
                        motX = world.rand.nextDouble() * 5;
                        motZ = world.rand.nextDouble() * 5;

                        final EntityMeteor meteor = new EntityMeteor(world, player.posX + x, player.posY + y, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 1);

                        if (!world.isRemote)
                        {
                            world.spawnEntityInWorld(meteor);
                        }
                    }
                }

                if (world.rand.nextInt(f * 3) == 0)
                {
                    final EntityPlayer closestPlayer = world.getClosestPlayerToEntity(player, 100);

                    if (closestPlayer == null || closestPlayer.getEntityId() <= player.getEntityId())
                    {
                        int x, y, z;
                        double motX, motZ;
                        x = world.rand.nextInt(20) - 10;
                        y = world.rand.nextInt(20) + 200;
                        z = world.rand.nextInt(20) - 10;
                        motX = world.rand.nextDouble() * 5;
                        motZ = world.rand.nextDouble() * 5;

                        final EntityMeteor meteor = new EntityMeteor(world, player.posX + x, player.posY + y, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, 6);

                        if (!world.isRemote)
                        {
                            world.spawnEntityInWorld(meteor);
                        }
                    }
                }
            }
        }
    }

    protected void checkCurrentItem(EntityPlayerMP player)
    {
        ItemStack theCurrentItem = player.inventory.getCurrentItem();
        if (theCurrentItem != null)
        {
            if (OxygenUtil.noAtmosphericCombustion(player.worldObj.provider))
            {
                //Is it a type of overworld torch?
                if (torchItems.containsValue(theCurrentItem.getItem()))
                {
                    Item torchItem = null;
                    //Get space torch for this overworld torch
                    for (Item i : torchItems.keySet())
                    {
                        if (torchItems.get(i) == theCurrentItem.getItem())
                        {
                            torchItem = i;
                            break;
                        }
                    }
                    if (torchItem != null)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = new ItemStack(torchItem, theCurrentItem.stackSize, 0);
                    }
                }
            }
            else
            {
                //Is it a type of space torch?
                if (torchItems.containsKey(theCurrentItem.getItem()))
                {
                    //Get overworld torch for this space torch
                    Item torchItem = torchItems.get(theCurrentItem.getItem());
                    if (torchItem != null)
                    {
                        player.inventory.mainInventory[player.inventory.currentItem] = new ItemStack(torchItem, theCurrentItem.stackSize, 0);
                    }
                }
            }
        }
    }

    public void registerTorchType(BlockUnlitTorch spaceTorch, Block vanillaTorch)
    {
        //Space Torch registration must be unique; there may be multiple mappings for vanillaTorch
        Item itemSpaceTorch = Item.getItemFromBlock(spaceTorch);
        Item itemVanillaTorch = Item.getItemFromBlock(vanillaTorch);
        torchItems.put(itemSpaceTorch, itemVanillaTorch);
    }

    public static void setUsingParachute(EntityPlayerMP player, LSPlayerStats playerStats, boolean tf)
    {
        playerStats.setUsingParachute(tf);

        if (tf)
        {
            int subtype = -1;

            if (playerStats.getParachuteInSlot() != null)
            {
                subtype = playerStats.getParachuteInSlot().getItemDamage();
            }

            LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.ADD, EnumExtendedInventorySlot.PARACHUTE, subtype);
        }
        else
        {
            LSPlayerHandler.sendGearUpdatePacket(player, EnumModelPacketType.REMOVE, EnumExtendedInventorySlot.PARACHUTE);
        }
    }

    protected static void updateFeet(EntityPlayerMP player, double motionX, double motionZ)
    {
        double motionSqrd = motionX * motionX + motionZ * motionZ;
        if (motionSqrd > 0.001D && !player.capabilities.isFlying)
        {
            int iPosX = MathHelper.floor_double(player.posX);
            int iPosY = MathHelper.floor_double(player.posY) - 1;
            int iPosZ = MathHelper.floor_double(player.posZ);

            // If the block below is the moon block
            IBlockState state = player.worldObj.getBlockState(new BlockPos(iPosX, iPosY, iPosZ));
            if (state.getBlock() == LabStuffMain.blockLuna)
            {
                // And is the correct metadata (moon turf)
                if (state.getBlock().getMetaFromState(state) == 5)
                {
                    LSPlayerStats stats = LSPlayerStats.get(player);
                    // If it has been long enough since the last step
                    if (stats.getDistanceSinceLastStep() > 0.35D)
                    {
                        Vector3 pos = new Vector3(player);
                        // Set the footprint position to the block below and add random number to stop z-fighting
                        pos.y = MathHelper.floor_double(player.posY - 1D) + player.worldObj.rand.nextFloat() / 100.0F;

                        // Adjust footprint to left or right depending on step count
                        switch (stats.getLastStep())
                        {
                        case 0:
                            float a = (-player.rotationYaw + 90F) / 57.295779513F;
                            pos.translate(new Vector3(MathHelper.sin(a) * 0.25F, 0, MathHelper.cos(a) * 0.25F));
                            break;
                        case 1:
                            a = (-player.rotationYaw - 90F) / 57.295779513F;
                            pos.translate(new Vector3(MathHelper.sin(a) * 0.25, 0, MathHelper.cos(a) * 0.25));
                            break;
                        }

                        float rotation = player.rotationYaw - 180;
                        pos = WorldUtil.getFootprintPosition(player.worldObj, rotation, pos, new BlockVec3(player));


                        // Increment and cap step counter at 1
                        stats.setLastStep((stats.getLastStep() + 1) % 2);
                        stats.setDistanceSinceLastStep(0);
                    }
                    else
                    {
                        stats.setDistanceSinceLastStep(stats.getDistanceSinceLastStep() + motionSqrd);
                    }
                }
            }
        }
    }


    public static class ThermalArmorEvent extends Event
    {
        public ArmorAddResult armorResult = ArmorAddResult.NOTHING;
        public final int armorIndex;
        public final ItemStack armorStack;

        public ThermalArmorEvent(int armorIndex, ItemStack armorStack)
        {
            this.armorIndex = armorIndex;
            this.armorStack = armorStack;
        }

        public void setArmorAddResult(ArmorAddResult result)
        {
            this.armorResult = result;
        }

        public enum ArmorAddResult
        {
            ADD,
            REMOVE,
            NOTHING
        }
    }


    protected void sendPlanetList(EntityPlayerMP player, LSPlayerStats stats)
    {
        HashMap<String, Integer> map;
        if (player.ticksExisted % 50 == 0)
        //Check for genuine update - e.g. maybe some other player created a space station or changed permissions
        //CAUTION: possible server load due to dimension loading, if any planets or moons were (contrary to GC default) set to hotload
        {
            map = WorldUtil.getArrayOfPossibleDimensions(stats.getSpaceshipTier(), player);
        }
        else
        {
            map = WorldUtil.getArrayOfPossibleDimensionsAgain(stats.getSpaceshipTier(), player);
        }

        String temp = "";
        int count = 0;

        for (Entry<String, Integer> entry : map.entrySet())
        {
            temp = temp.concat(entry.getKey() + (count < map.entrySet().size() - 1 ? "?" : ""));
            count++;
        }

        if (!temp.equals(stats.getSavedPlanetList()) || (player.ticksExisted % 100 == 0))
        {
            LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_DIMENSION_LIST, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { player.getGameProfile().getName(), temp }), player);
            stats.setSavedPlanetList(temp);
            //GCLog.debug("Sending to " + player.getGameProfile().getName() + ": " + temp);
        }
    }

    protected static void sendAirRemainingPacket(EntityPlayerMP player, LSPlayerStats stats)
    {
        final float f1 = stats.getTankInSlot1() == null ? 0.0F : stats.getTankInSlot1().getMaxDamage() / 90.0F;
        final float f2 = stats.getTankInSlot2() == null ? 0.0F : stats.getTankInSlot2().getMaxDamage() / 90.0F;
        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_AIR_REMAINING, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { MathHelper.floor_float(stats.getAirRemaining() / f1), MathHelper.floor_float(stats.getAirRemaining2() / f2), player.getGameProfile().getName() }), player);
    }

    protected void sendThermalLevelPacket(EntityPlayerMP player, LSPlayerStats stats)
    {
        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_THERMAL_LEVEL, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { stats.getThermalLevel(), stats.isThermalLevelNormalising() }), player);
    }

    public static void sendGearUpdatePacket(EntityPlayerMP player, EnumModelPacketType packetType, EnumExtendedInventorySlot gearType)
    {
        LSPlayerHandler.sendGearUpdatePacket(player, packetType, gearType, -1);
    }

    public static void sendGearUpdatePacket(EntityPlayerMP player, EnumModelPacketType packetType, EnumExtendedInventorySlot gearType, int gearID)
    {
        MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
        if (theServer != null && PlayerUtil.getPlayerForUsernameVanilla(theServer, player.getGameProfile().getName()) != null)
        {
            LabStuffMain.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_GEAR_SLOT, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { player.getGameProfile().getName(), packetType.ordinal(), gearType.ordinal(), gearID }), new TargetPoint(LabStuffUtils.getDimensionID(player.worldObj), player.posX, player.posY, player.posZ, 50.0D));
        }
    }

    public enum EnumModelPacketType
    {
        ADD,
        REMOVE
    }

    public void onPlayerUpdate(EntityPlayerMP player)
    {
        int tick = player.ticksExisted - 1;

        //This will speed things up a little
        LSPlayerStats stats = LSPlayerStats.get(player);

        final boolean isInGCDimension = player.worldObj.provider instanceof ILabstuffWorldProvider;

        if (tick >= 25)
        {
            if (!stats.hasSentFlags())
            {
                LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_STATS, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { stats.getBuildFlags() }), player);
                stats.setSentFlags(true);
            }
        }

        if (stats.getCryogenicChamberCooldown() > 0)
        {
            stats.setCryogenicChamberCooldown(stats.getCryogenicChamberCooldown() - 1);
        }

        if (!player.onGround && stats.isLastOnGround())
        {
            stats.setTouchedGround(true);
        }

        if (stats.getTeleportCooldown() > 0)
        {
            stats.setTeleportCooldown(stats.getTeleportCooldown() - 1);
        }

        if (stats.getChatCooldown() > 0)
        {
            stats.setChatCooldown(stats.getChatCooldown() - 1);
        }

        if (stats.getOpenPlanetSelectionGuiCooldown() > 0)
        {
            stats.setOpenPlanetSelectionGuiCooldown(stats.getOpenPlanetSelectionGuiCooldown() - 1);

            if (stats.getOpenPlanetSelectionGuiCooldown() == 1 && !stats.hasOpenedPlanetSelectionGui())
            {
                WorldUtil.toCelestialSelection(player, stats, stats.getSpaceshipTier());
                stats.setHasOpenedPlanetSelectionGui(true);
            }
        }

        if (stats.isUsingParachute())
        {
            if (stats.getLastParachuteInSlot() != null)
            {
                player.fallDistance = 0.0F;
            }
            if (player.onGround)
            {
                LSPlayerHandler.setUsingParachute(player, stats, false);
            }
        }

        this.checkCurrentItem(player);

        if (stats.isUsingPlanetSelectionGui())
        {
            //This sends the planets list again periodically (forcing the Celestial Selection screen to open) in case of server/client lag
            //#PACKETSPAM
            this.sendPlanetList(player, stats);
        }

		/*		if (isInGCDimension || player.usingPlanetSelectionGui)
                {
					player.connection.ticksForFloatKick = 0;
				}	
		*/
        if (stats.getDamageCounter() > 0)
        {
            stats.setDamageCounter(stats.getDamageCounter() - 1);
        }

        if (isInGCDimension)
        {
            if (tick % 30 == 0)
            {
                LSPlayerHandler.sendAirRemainingPacket(player, stats);
                this.sendThermalLevelPacket(player, stats);
            }

            if (player.getRidingEntity() instanceof EntityLanderBase)
            {
                stats.setInLander(true);
                stats.setJustLanded(false);
            }
            else
            {
                if (stats.isInLander())
                {
                    stats.setJustLanded(true);
                }
                stats.setInLander(false);
            }

            if (player.onGround && stats.hasJustLanded())
            {
                stats.setJustLanded(false);

                //Set spawn point here if just descended from a lander for the first time
                if (player.getBedLocation(LabStuffUtils.getDimensionID(player.worldObj)) == null || stats.isNewAdventureSpawn())
                {
                    int i = 30000000;
                    int j = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posX + 0.5D)));
                    int k = Math.min(256, Math.max(0, MathHelper.floor_double(player.posY + 1.5D)));
                    int l = Math.min(i, Math.max(-i, MathHelper.floor_double(player.posZ + 0.5D)));
                    BlockPos coords = new BlockPos(j, k, l);
                    player.setSpawnChunk(coords, true, LabStuffUtils.getDimensionID(player.worldObj));
                    stats.setNewAdventureSpawn(false);
                }

                LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_RESET_THIRD_PERSON, LabStuffUtils.getDimensionID(player.worldObj), new Object[] {}), player);
            }

            if (player.worldObj.provider instanceof WorldProviderSpaceStation)
            {
            	this.preventFlyingKicks(player);
                if (stats.isNewInOrbit())
                {
                    ((WorldProviderSpaceStation) player.worldObj.provider).getSpinManager().sendPackets(player);
                    stats.setNewInOrbit(false);
                }
            }
            else
            {
                stats.setNewInOrbit(true);

                if (player.worldObj.provider instanceof WorldProviderAsteroids)
                {
                	this.preventFlyingKicks(player);
                }
            }
        }
        else
        {
            stats.setNewInOrbit(true);
        }

        checkGear(player, stats, false);


        //

        if (stats.getLaunchAttempts() > 0 && player.getRidingEntity() == null)
        {
            stats.setLaunchAttempts(0);
        }

        this.checkThermalStatus(player, stats);
        this.checkOxygen(player, stats);
        this.checkShield(player, stats);

        if (isInGCDimension && (stats.isOxygenSetupValid() != stats.isLastOxygenSetupValid() || tick % 100 == 0))
        {
            LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_OXYGEN_VALIDITY, player.worldObj.provider.getDimension(), new Object[] { stats.isOxygenSetupValid() }), player);
        }

        this.throwMeteors(player);


        if (tick % 250 == 0 && stats.getFrequencyModuleInSlot() == null && !stats.hasReceivedSoundWarning() && isInGCDimension && player.onGround && tick > 0 && ((ILabstuffWorldProvider) player.worldObj.provider).getSoundVolReductionAmount() > 1.0F)
        {
            String[] string2 = LabStuffUtils.translate("gui.frequencymodule.warning1").split(" ");
            StringBuilder sb = new StringBuilder();
            for (String aString2 : string2)
            {
                sb.append(" ").append(EnumColor.YELLOW).append(aString2);
            }
            stats.setReceivedSoundWarning(true);
        }

        stats.setLastOxygenSetupValid(stats.isOxygenSetupValid());
        stats.setLastOnGround(player.onGround);
    }
    
    public void preventFlyingKicks(EntityPlayerMP player)
    {
        player.fallDistance = 0.0F;
    }
}