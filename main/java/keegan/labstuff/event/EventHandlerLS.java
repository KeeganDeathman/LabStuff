package keegan.labstuff.event;

import java.lang.reflect.Field;
import java.util.*;

import keegan.labstuff.*;
import keegan.labstuff.client.*;
import keegan.labstuff.common.TickHandlerServer;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.dimension.WorldProviderSpaceStation;
import keegan.labstuff.entities.*;
import keegan.labstuff.util.*;
import keegan.labstuff.world.*;
import keegan.labstuff.wrappers.PlayerGearData;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.terraingen.*;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent.Load;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.relauncher.*;

public class EventHandlerLS
{
    public static Map<Block, Item> bucketList = new HashMap<Block, Item>();
    public static boolean bedActivated;

    @SubscribeEvent
    public void playerJoinWorld(EntityJoinWorldEvent event)
    {
        TickHandlerServer.markWorldNeedsUpdate(LabStuffUtils.getDimensionID(event.getWorld()));
    }

    @SubscribeEvent
    public void onRocketLaunch(EntitySpaceshipBase.RocketLaunchEvent event)
    {
//        if (!event.getEntity().worldObj.isRemote && event.getEntity().worldObj.provider.dimensionId == 0)
//        {
//            if (event.rocket.riddenByEntity instanceof EntityPlayerMP)
//            {
//                TickHandlerServer.playersRequestingMapData.add((EntityPlayerMP) event.rocket.riddenByEntity);
//            }
//        }
    }

    @SubscribeEvent
    public void onConfiLShanged(ConfigChangedEvent event)
    {
        if (event.getModID().equals("labstuff"))
        {
            ConfigManagerCore.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onWorldSave(Save event)
    {
        ChunkLoadingCallback.save((WorldServer) event.getWorld());
    }

    @SubscribeEvent
    public void onChunkDataLoad(ChunkDataEvent.Load event)
    {
        ChunkLoadingCallback.load((WorldServer) event.getWorld());
    }

    @SubscribeEvent
    public void onWorldLoad(Load event)
    {
        if (!event.getWorld().isRemote)
        {
            ChunkLoadingCallback.load((WorldServer) event.getWorld());
        }
    }

    @SubscribeEvent
    public void onEntityDamaged(LivingHurtEvent event)
    {
        if (event.getSource().damageType.equals(DamageSource.onFire.damageType))
        {
            if (OxygenUtil.noAtmosphericCombustion(event.getEntityLiving().worldObj.provider))
            {
                if (OxygenUtil.isAABBInBreathableAirBlock(event.getEntityLiving().worldObj, event.getEntityLiving().getEntityBoundingBox()))
                {
                    return;
                }

                if (event.getEntityLiving().worldObj instanceof WorldServer)
                {
                    ((WorldServer) event.getEntityLiving().worldObj).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, event.getEntityLiving().posX, event.getEntityLiving().posY + event.getEntityLiving().getEntityBoundingBox().maxY - event.getEntityLiving().getEntityBoundingBox().minY, event.getEntityLiving().posZ, 50, 0.0, 0.05, 0.0, 0.001);
                }

                event.getEntityLiving().extinguish();
            }
        }
    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (player.getRidingEntity() instanceof EntityAutoRocket || player.getRidingEntity() instanceof EntityLanderBase)
            {
                event.setDistance(0.0F);
                event.setCanceled(true);
                return;
            }
        }

        if (event.getEntityLiving().worldObj.provider instanceof ILabstuffWorldProvider)
        {
            event.setDistance(event.getDistance() * ((ILabstuffWorldProvider) event.getEntityLiving().worldObj.provider).getFallDamageModifier());
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClickedBlock(PlayerInteractEvent.LeftClickBlock event)
    {
        //Skip events triggered from Thaumcraft Golems and other non-players
        if (event.getEntityPlayer() == null || event.getEntityPlayer().inventory == null || event.getPos() == null || (event.getPos().getX() == 0 && event.getPos().getY() == 0 && event.getPos().getZ() == 0))
        {
            return;
        }

        final World worldObj = event.getEntityPlayer().worldObj;
        if (worldObj == null)
        {
            return;
        }

        final ItemStack heldStack = event.getEntityPlayer().inventory.getCurrentItem();
        final TileEntity tileClicked = worldObj.getTileEntity(event.getPos());

    }

    @SubscribeEvent
    public void onPlayerRightClickedBlock(PlayerInteractEvent.RightClickBlock event)
    {
        //Skip events triggered from Thaumcraft Golems and other non-players
        if (event.getEntityPlayer() == null || event.getEntityPlayer().inventory == null || event.getPos() == null || (event.getPos().getX() == 0 && event.getPos().getY() == 0 && event.getPos().getZ() == 0))
        {
            return;
        }

        final World worldObj = event.getEntityPlayer().worldObj;
        if (worldObj == null)
        {
            return;
        }

        final Block idClicked = worldObj.getBlockState(event.getPos()).getBlock();

        if (idClicked == Blocks.BED && worldObj.provider instanceof ILabstuffWorldProvider && !worldObj.isRemote && !((ILabstuffWorldProvider) worldObj.provider).hasBreathableAtmosphere())
        {
                LSPlayerStats stats = LSPlayerStats.get(event.getEntityPlayer());
                if (!stats.hasReceivedBedWarning())
                {
                    event.getEntityPlayer().addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.bed_fail.message")));
                    stats.setReceivedBedWarning(true);
                }

            if (worldObj.provider instanceof WorldProviderSpaceStation)
            {
                //On space stations simply block the bed activation => no explosion
                event.setCanceled(true);
                return;
            }


            //Optionally prevent beds from exploding - depends on canRespawnHere() in the WorldProvider interacting with this
            EventHandlerLS.bedActivated = true;
            if (worldObj.provider.canRespawnHere() && !EventHandlerLS.bedActivated)
            {
                EventHandlerLS.bedActivated = true;

                //On planets allow the bed to be used to designate a player spawn point
                event.getEntityPlayer().setSpawnChunk(event.getPos(), false, LabStuffUtils.getDimensionID(event.getWorld()));
            }
            else
            {
                EventHandlerLS.bedActivated = false;
            }
        }

        final ItemStack heldStack = event.getEntityPlayer().inventory.getCurrentItem();
        final TileEntity tileClicked = worldObj.getTileEntity(event.getPos());

        if (heldStack != null)
        {

            if (heldStack.getItem() instanceof ItemFlintAndSteel || heldStack.getItem() instanceof ItemFireball)
            {
                if (!worldObj.isRemote)
                {
                    if (idClicked != Blocks.TNT && OxygenUtil.noAtmosphericCombustion(event.getEntityPlayer().worldObj.provider) && !OxygenUtil.isAABBInBreathableAirBlock(event.getEntityLiving().worldObj, new AxisAlignedBB(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), event.getPos().getX() + 1, event.getPos().getY() + 2, event.getPos().getZ() + 1)))
                    {
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void entityLivingEvent(LivingEvent.LivingUpdateEvent event)
    {
        final EntityLivingBase entityLiving = event.getEntityLiving();
        if (entityLiving instanceof EntityPlayerMP)
        {
            LabStuffMain.handler.onPlayerUpdate((EntityPlayerMP) entityLiving);
            return;
        }

        if (entityLiving.ticksExisted % 100 == 0)
        {
            if (entityLiving.worldObj.provider instanceof ILabstuffWorldProvider)
            {
                if (!(entityLiving instanceof EntityPlayer) && (!(entityLiving instanceof IEntityBreathable) || !((IEntityBreathable) entityLiving).canBreath()) && !((ILabstuffWorldProvider) entityLiving.worldObj.provider).hasBreathableAtmosphere())
                {

                    if (!OxygenUtil.isAABBInBreathableAirBlock(entityLiving))
                    {
                        LSCoreOxygenSuffocationEvent suffocationEvent = new LSCoreOxygenSuffocationEvent.Pre(entityLiving);
                        MinecraftForge.EVENT_BUS.post(suffocationEvent);

                        if (suffocationEvent.isCanceled())
                        {
                            return;
                        }

                        entityLiving.attackEntityFrom(DamageSourceLS.oxygenSuffocation, 1);

                        LSCoreOxygenSuffocationEvent suffocationEventPost = new LSCoreOxygenSuffocationEvent.Post(entityLiving);
                        MinecraftForge.EVENT_BUS.post(suffocationEventPost);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void entityUpdateCancelInFreefall(EntityEvent.CanUpdate event)
    {
        if (event.getEntity().worldObj.provider instanceof IZeroGDimension)
        {
            if (((IZeroGDimension)event.getEntity().worldObj.provider).inFreefall(event.getEntity()))
            {
                event.setCanUpdate(true);
//                event.entity.moveEntity(event.entity.motionX, event.entity.motionY, event.entity.motionZ);
            }
        }
    }

    private ItemStack fillBucket(World world, RayTraceResult position)
    {
        IBlockState state = world.getBlockState(position.getBlockPos());
        Block block = state.getBlock();

        Item bucket = bucketList.get(block);

        if (bucket != null && block.getMetaFromState(state) == 0)
        {
            world.setBlockToAir(position.getBlockPos());
            return new ItemStack(bucket);
        }

        return null;
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event)
    {
        RayTraceResult pos = event.getTarget();
        if (pos == null)
        {
            return;
        }

        ItemStack ret = fillBucket(event.getWorld(), pos);

        if (ret == null)
        {
            return;
        }

        event.setFilledBucket(ret);
        event.setResult(Result.ALLOW);
    }

    @SubscribeEvent
    public void populate(PopulateChunkEvent.Post event)
    {
        final boolean doGen = TerrainGen.populate(event.getGenerator(), event.getWorld(), event.getRand(), event.getChunkX(), event.getChunkZ(), event.isHasVillageGenerated(), PopulateChunkEvent.Populate.EventType.CUSTOM);

        if (!doGen)
        {
            return;
        }

        final int worldX = event.getChunkX() << 4;
        final int worldZ = event.getChunkZ() << 4;

    }




    private static boolean checkBlockAbove(World w, BlockPos pos)
    {
        Block b = w.getBlockState(pos).getBlock();
        if (b instanceof BlockSand)
        {
            return true;
        }
        if (b instanceof BlockGravel)
        {
            return true;
        }
        return false;
    }


    @SubscribeEvent
    public void onPlayerDeath(PlayerDropsEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayerMP)
        {
            LSPlayerStats stats = LSPlayerStats.get(event.getEntityPlayer());
            if (!event.getEntityPlayer().worldObj.getGameRules().getBoolean("keepInventory"))
            {
                event.getEntityPlayer().captureDrops = true;
                for (int i = stats.getExtendedInventory().getSizeInventory() - 1; i >= 0; i--)
                {
                    ItemStack stack = stats.getExtendedInventory().getStackInSlot(i);

                    if (stack != null)
                    {
                        event.getEntityPlayer().dropItem(stack, true, false);
                        stats.getExtendedInventory().setInventorySlotContents(i, null);
                    }
                }
                event.getEntityLiving().captureDrops = false;
            }
        }
    }

    // @SideOnly(Side.CLIENT)
    // @SubscribeEvent
    // public void onMinecraftLoaded(MinecraftLoadedEvent event)
    // {
    // ;
    // }

    //	@SideOnly(Side.CLIENT)
    //	@SubscribeEvent
    //	public void onSoundLoad(SoundLoadEvent event)
    //	{
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/scaryscape.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip1.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip2.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip3.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip4.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip5.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip6.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip7.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "ambience/singledrip8.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/bossdeath.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/bosslaugh.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/bossliving.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "entity/slime_death.ogg");
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/mars_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/mimas_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/orbit_JC.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/scary_ambience.ogg"));
    //		ClientProxyCore.newMusic.add(this.func_110654_c(event.manager.soundPoolMusic, Constants.TEXTURE_PREFIX + "music/spacerace_JC.ogg"));
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/closeairlock.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/openairlock.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/parachute.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "player/unlockchest.ogg");
    //		event.manager.addSound(Constants.TEXTURE_PREFIX + "shuttle/shuttle.ogg");
    //	}
    //
    //	@SideOnly(Side.CLIENT)
    //	private SoundPoolEntry func_110654_c(SoundPool pool, String par1Str)
    //	{
    //		try
    //		{
    //			ResourceLocation resourcelocation = new ResourceLocation(par1Str);
    //			String s1 = String.format("%s:%s:%s/%s", new Object[] { "mcsounddomain", resourcelocation.getResourceDomain(), "sound", resourcelocation.getResourcePath() });
    //			SoundPoolProtocolHandler soundpoolprotocolhandler = new SoundPoolProtocolHandler(pool);
    //			return new SoundPoolEntry(par1Str, new URL((URL) null, s1, soundpoolprotocolhandler));
    //		}
    //		catch (MalformedURLException e)
    //		{
    //			e.printStackTrace();
    //		}
    //
    //		return null;
    //	} TODO Fix sounds

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onLeaveBedButtonClicked(SleepCancelledEvent event)
    {
        EntityPlayer player = FMLClientHandler.instance().getClient().thePlayer;

        BlockPos c = player.getBedLocation();

        if (c != null)
        {
            EventWakePlayer event0 = new EventWakePlayer(player, c, false, true, true, true);
            MinecraftForge.EVENT_BUS.post(event0);
            player.wakeUpPlayer(false, true, true);
        }
    }


    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void overrideSkyColor(EntityViewRenderEvent.FogColors event)
    {
        //Disable any night vision effects on the sky, if the planet has no atmosphere
        if (event.getEntity() instanceof EntityLivingBase && ((EntityLivingBase) event.getEntity()).isPotionActive(MobEffects.NIGHT_VISION))
        {
            WorldClient worldclient = Minecraft.getMinecraft().theWorld;

            if (worldclient.provider instanceof ILabstuffWorldProvider && ((ILabstuffWorldProvider) worldclient.provider).getCelestialBody().atmosphere.size() == 0 && event.getState().getBlock().getMaterial(event.getState()) == Material.AIR && !((ILabstuffWorldProvider) worldclient.provider).hasBreathableAtmosphere())
            {
                Vec3d vec = worldclient.getFogColor(1.0F);
                event.setRed((float) vec.xCoord);
                event.setGreen((float) vec.yCoord);
                event.setBlue((float) vec.zCoord);
                return;
            }

            if (worldclient.provider.getSkyRenderer() instanceof SkyProviderOverworld && event.getEntity().posY > 200)
            {
                Vec3d vec = TransformerHooks.getFogColorHook(event.getEntity().worldObj);
                event.setRed((float) vec.xCoord);
                event.setGreen((float) vec.yCoord);
                event.setBlue((float) vec.zCoord);
                return;
            }
        }
    }

    private List<SoundPlayEntry> soundPlayList = new ArrayList<SoundPlayEntry>();

    private static Field volumeField;
    private static Field pitchField;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onSoundPlayed(PlaySoundEvent event)
    {
        //The event.result starts off equal to event.sound, but could have been altered or set to null by another mod
        if (event.getResultSound() == null)
        {
            return;
        }

        EntityPlayerSP player = FMLClientHandler.instance().getClient().thePlayer;

        if (player != null && player.worldObj != null && player.worldObj.provider instanceof ILabstuffWorldProvider)
        {
            //Only modify standard game sounds, not music
            if (event.getResultSound().getAttenuationType() != ISound.AttenuationType.NONE)
            {
                PlayerGearData gearData = LabStuffClientProxy.playerItemData.get(player.getGameProfile().getName());

                float x = event.getResultSound().getXPosF();
                float y = event.getResultSound().getYPosF();
                float z = event.getResultSound().getZPosF();

                if (gearData == null || gearData.getFrequencyModule() == -1)
                {
                    // If the player doesn't have a frequency module, and the player isn't in an oxygenated environment
                    // Note: this is a very simplistic approach, and nowhere near realistic, but required for performance reasons
                    AxisAlignedBB bb = new AxisAlignedBB(x - 0.0015D, y - 0.0015D, z - 0.0015D, x + 0.0015D, y + 0.0015D, z + 0.0015D);
                    boolean playerInAtmosphere = OxygenUtil.isAABBInBreathableAirBlock(player);
                    boolean soundInAtmosphere = OxygenUtil.isAABBInBreathableAirBlock(player.worldObj, bb);
                    if ((!playerInAtmosphere || !soundInAtmosphere))
                    {
                        float volume = 1.0F;
                        float pitch = 1.0F;

                        if (volumeField == null)
                        {
                            try
                            {
                                volumeField = PositionedSound.class.getDeclaredField(LabStuffUtils.isDeobfuscated() ? "volume" : "field_147662_b"); // TODO Obfuscated environment support
                                volumeField.setAccessible(true);
                                pitchField = PositionedSound.class.getDeclaredField(LabStuffUtils.isDeobfuscated() ? "pitch" : "field_147663_c"); // TODO Obfuscated environment support
                                pitchField.setAccessible(true);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        if (volumeField != null && pitchField != null)
                        {
                            try
                            {
                                volume = event.getSound() instanceof PositionedSound ? (float) volumeField.get(event.getSound()) : 1.0F;
                                pitch = event.getSound() instanceof PositionedSound ? (float) pitchField.get(event.getSound()) : 1.0F;
                            }
                            catch (IllegalAccessException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        //First check for duplicate firing of PlaySoundEvent17 on this handler's own playing of a reduced volume sound (see below)
                        for (int i = 0; i < this.soundPlayList.size(); i++)
                        {
                            SoundPlayEntry entry = this.soundPlayList.get(i);

                            if (entry.name.equals(event.getName()) && entry.x == x && entry.y == y && entry.z == z && entry.volume == volume)
                            {
                                this.soundPlayList.remove(i);
                                return;
                            }
                        }

                        //If it's not a duplicate: play the same sound but at reduced volume
                        float newVolume = volume / Math.max(0.01F, ((ILabstuffWorldProvider) player.worldObj.provider).getSoundVolReductionAmount());

                        this.soundPlayList.add(new SoundPlayEntry(event.getName(), x, y, z, newVolume));
                        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(event.getResultSound().getSoundLocation());
                        if (soundEvent != null)
                        {
                            ISound newSound = new PositionedSoundRecord(soundEvent, SoundCategory.NEUTRAL, newVolume, pitch, x, y, z);
                            event.getManager().playSound(newSound);
                            event.setResultSound(null);
                        }
                        else
                        {
                            LSLog.severe("Sound event null! " + event.getName() + " " + event.getResultSound().getSoundLocation());
                        }
                    }
                }
            }
        }
    }

    private static class SoundPlayEntry
    {
        private final String name;
        private final float x;
        private final float y;
        private final float z;
        private final float volume;

        private SoundPlayEntry(String name, float x, float y, float z, float volume)
        {
            this.name = name;
            this.volume = volume;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class SleepCancelledEvent extends Event
    {
    }

    public static class OrientCameraEvent extends Event
    {
    }

}