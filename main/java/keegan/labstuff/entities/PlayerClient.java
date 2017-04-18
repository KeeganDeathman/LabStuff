package keegan.labstuff.entities;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.client.ClientTickHandler;
import keegan.labstuff.client.sounds.LSSounds;
import keegan.labstuff.common.EnumColor;
import keegan.labstuff.common.capabilities.LSPlayerStatsClient;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.event.EventWakePlayer;
import keegan.labstuff.models.ModelPlayerLS;
import keegan.labstuff.tileentity.TileEntityAdvanced;
import keegan.labstuff.util.*;
import keegan.labstuff.world.*;
import keegan.labstuff.wrappers.PlayerGearData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;

public class PlayerClient implements IPlayerClient
{
    private boolean saveSneak;
	private double downMot2;
	public static boolean startup;

    @Override
    public void moveEntity(EntityPlayerSP player, double par1, double par3, double par5)
    {
    }

    @Override
    public boolean wakeUpPlayer(EntityPlayerSP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn)
    {
        return this.wakeUpPlayer(player, immediately, updateWorldFlag, setSpawn, false);
    }

    @Override
    public void onUpdate(EntityPlayerSP player)
    {
        LSPlayerStatsClient stats = LSPlayerStatsClient.get(player);
        stats.setTick(stats.getTick() + 1);

        if (stats.isUsingParachute() && !player.capabilities.isFlying && !player.handleWaterMovement())
        {
            player.motionY = -0.5D;
            player.motionX *= 0.5F;
            player.motionZ *= 0.5F;
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock(EntityPlayerSP player, boolean vanillaInside)
    {
        LSPlayerStatsClient stats = LSPlayerStatsClient.get(player);
        if (vanillaInside && stats.isInFreefall())
        {
            stats.setInFreefall(false);
            return false;
        }
        return !(player.getRidingEntity() instanceof EntityLanderBase) && vanillaInside;
    }

    @Override
    public void onLivingUpdatePre(EntityPlayerSP player)
    {
        LSPlayerStatsClient stats = LSPlayerStatsClient.get(player);

        if (player.worldObj.provider instanceof ILabstuffWorldProvider)
        {
            if (!startup)
            {
                stats.setInFreefallLast(stats.isInFreefall());
                stats.setInFreefall(stats.getFreefallHandler().testFreefall(player));
                startup = true;
            }
            if (player.worldObj.provider instanceof IZeroGDimension)
            {
                stats.setInFreefallLast(stats.isInFreefall());
                stats.setInFreefall(stats.getFreefallHandler().testFreefall(player));
                this.downMot2 = stats.getDownMotionLast();
                stats.setDownMotionLast(player.motionY);
                stats.getFreefallHandler().preVanillaMotion(player);
            }
        }

//        if (player.boundingBox != null && stats.boundingBoxBefore == null)
//        {
//            GCLog.debug("Changed player BB from " + player.boundingBox.minY);
//            stats.boundingBoxBefore = player.boundingBox;
//            player.boundingBox.setBounds(stats.boundingBoxBefore.minX + 0.4, stats.boundingBoxBefore.minY + 0.9, stats.boundingBoxBefore.minZ + 0.4, stats.boundingBoxBefore.maxX - 0.4, stats.boundingBoxBefore.maxY - 0.9, stats.boundingBoxBefore.maxZ - 0.4);
//            GCLog.debug("Changed player BB to " + player.boundingBox.minY);
//        }
//        else if (player.boundingBox != null && stats.boundingBoxBefore != null)
//        {
//            player.boundingBox.setBB(stats.boundingBoxBefore);
//            GCLog.debug("Changed player BB to " + player.boundingBox.minY);
//        }
    }

    @Override
    public void onLivingUpdatePost(EntityPlayerSP player)
    {
        LSPlayerStatsClient stats = LSPlayerStatsClient.get(player);

        if (player.worldObj.provider instanceof IZeroGDimension)
        {
            stats.getFreefallHandler().postVanillaMotion(player);

            if (stats.isInFreefall())
            {
                //No limb swing
                player.limbSwing -= player.limbSwingAmount;
                player.limbSwingAmount = player.prevLimbSwingAmount;
                float adjust = Math.min(Math.abs(player.limbSwing), Math.abs(player.limbSwingAmount) / 3);
                if (player.limbSwing < 0)
                {
                    player.limbSwing += adjust;
                }
                else if (player.limbSwing > 0)
                {
                    player.limbSwing -= adjust;
                }
                player.limbSwingAmount *= 0.9;
            }
            else
            {
		    	if (stats.isInFreefallLast() && this.downMot2 < -0.008D)
                {
		    		stats.setLandingTicks(5 - (int)(Math.min(this.downMot2, stats.getDownMotionLast()) * 40));
		    		if (stats.getLandingTicks() > stats.getMaxLandingticks())
		    		{
	                    if (stats.getLandingTicks() > stats.getMaxLandingticks() + 4)
	                    {
	                        stats.getFreefallHandler().pjumpticks = stats.getLandingTicks() - stats.getMaxLandingticks() - 5;
                        }
		    		    stats.setLandingTicks(stats.getMaxLandingticks());
		    		}
		    		float dYmax = 0.3F * stats.getLandingTicks() / stats.getMaxLandingticks();
		    		float factor = 1F;
		    		for (int i = 0; i <= stats.getLandingTicks(); i++)
		    		{
    	                stats.getLandingYOffset()[i] = dYmax * MathHelper.sin(i * 3.1415926F / stats.getLandingTicks()) * factor;
    	                factor *= 0.97F;
                    }
		    	}
	        }

	        if (stats.getLandingTicks() > 0)
	        {
	            stats.setLandingTicks(stats.getLandingTicks() - 1);
                player.limbSwing *= 0.8F;
                player.limbSwingAmount = 0F;
            }
        }
        else
        {
            stats.setInFreefall(false);
        }

        boolean ridingThirdPersonEntity = player.getRidingEntity() instanceof ICameraZoomEntity && ((ICameraZoomEntity) player.getRidingEntity()).defaultThirdPerson();

        if (ridingThirdPersonEntity && !stats.isLastRidingCameraZoomEntity())
        {
            if(!ConfigManagerCore.disableVehicleCameraChanges)
                FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = 1;
        }

        if (player.getRidingEntity() != null && player.getRidingEntity() instanceof ICameraZoomEntity)
        {
            if(!ConfigManagerCore.disableVehicleCameraChanges)
            {
                stats.setLastZoomed(true);
                ClientTickHandler.zoom(((ICameraZoomEntity) player.getRidingEntity()).getCameraZoom());
            }
        }
        else if (stats.isLastZoomed())
        {
        	if(!ConfigManagerCore.disableVehicleCameraChanges)
            {
	            stats.setLastZoomed(false);
	            ClientTickHandler.zoom(4.0F);
            }
        }

        stats.setLastRidingCameraZoomEntity(ridingThirdPersonEntity);

        if (stats.isUsingParachute())
        {
            player.fallDistance = 0.0F;
        }

        PlayerGearData gearData = ModelPlayerLS.getGearData(player);

        stats.setUsingParachute(false);

        if (gearData != null)
        {
            stats.setUsingParachute(gearData.getParachute() != null);
            if(!LabStuffMain.isHeightConflictingModInstalled)
            {
                if (gearData.getMask() >= 0)
                {
                	player.height = 1.9375F;
                }
                else
                {
                	player.height = 1.8F;
                }
                AxisAlignedBB bounds = player.getEntityBoundingBox();
                player.setEntityBoundingBox(new AxisAlignedBB(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.minY + (double) player.height, bounds.maxZ));
            }
        }

        if (stats.isUsingParachute() && player.onGround)
        {
            stats.setUsingParachute(false);
            stats.setLastUsingParachute(false);
            FMLClientHandler.instance().getClient().gameSettings.thirdPersonView = stats.getThirdPersonView();
        }

        if (!stats.isLastUsingParachute() && stats.isUsingParachute())
        {
            FMLClientHandler.instance().getClient().getSoundHandler().playSound(new PositionedSoundRecord(LSSounds.parachute, SoundCategory.PLAYERS, 0.95F + player.getRNG().nextFloat() * 0.1F, 1.0F, (float) player.posX, (float) player.posY, (float) player.posZ));
        }

        stats.setLastUsingParachute(stats.isUsingParachute());
        stats.setLastOnGround(player.onGround);
    }

    @Override
    public float getBedOrientationInDegrees(EntityPlayerSP player, float vanillaDegrees)
    {
        if (player.getBedLocation() != null)
        {
            if (player.worldObj.getTileEntity(player.getBedLocation()) instanceof TileEntityAdvanced)
            {
//                int j = player.worldObj.getBlock(x, y, z).getBedDirection(player.worldObj, x, y, z);
                IBlockState state = player.worldObj.getBlockState(player.getBedLocation());
                switch (state.getBlock().getMetaFromState(state) - 4)
                {
                case 0:
                    return 90.0F;
                case 1:
                    return 270.0F;
                case 2:
                    return 180.0F;
                case 3:
                    return 0.0F;
                }
            }
            else
            {
                return vanillaDegrees;
            }
        }

        return vanillaDegrees;
    }


    public boolean wakeUpPlayer(EntityPlayerSP player, boolean immediately, boolean updateWorldFlag, boolean setSpawn, boolean bypass)
    {
        BlockPos c = player.getBedLocation();

        if (c != null)
        {
            EventWakePlayer event = new EventWakePlayer(player, c, immediately, updateWorldFlag, setSpawn, bypass);
            MinecraftForge.EVENT_BUS.post(event);

            if (bypass || event.result == null || event.result == EntityPlayer.SleepResult.OK)
            {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onBuild(int i, EntityPlayerSP player)
    {
        // 0 : opened LS inventory tab
        // 1,2,3 : Compressor, CF, Standard Wrench
        // 4,5,6 : Fuel loader, Launchpad
        // 7: oil found 8: placed rocket

        LSPlayerStatsClient stats = LSPlayerStatsClient.get(player);
        int flag = stats.getBuildFlags();
        if (flag == -1)
        {
            flag = 0;
        }
        int repeatCount = flag >> 9;
        if (repeatCount <= 3)
        {
            repeatCount++;
        }
        if ((flag & 1 << i) > 0)
        {
            return;
        }
        flag |= 1 << i;
        stats.setBuildFlags((flag & 511) + (repeatCount << 9));
        LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_BUILDFLAGS_UPDATE, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { stats.getBuildFlags() }));
        switch (i)
        {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        }
    }
}