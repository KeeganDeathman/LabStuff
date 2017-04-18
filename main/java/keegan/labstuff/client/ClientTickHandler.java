package keegan.labstuff.client;

import java.util.*;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import keegan.labstuff.common.capabilities.LSPlayerStatsClient;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.entities.EntitySpaceshipBase;
import keegan.labstuff.entities.EntitySpaceshipBase.EnumLaunchPhase;
import keegan.labstuff.network.IClientTicker;
import keegan.labstuff.util.*;
import keegan.labstuff.world.ILabstuffWorldProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.*;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ClientTickHandler
{

	public static Minecraft mc = FMLClientHandler.instance().getClient();

	public static Set<IClientTicker> tickingSet = new HashSet<IClientTicker>();

	public static int airRemaining;
    public static int airRemaining2;
    
	@SubscribeEvent
	public void onTick(ClientTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			tickStart();
		}
	}

	public void tickStart()
	{

		if(!mc.isGamePaused())
		{
			for(Iterator<IClientTicker> iter = tickingSet.iterator(); iter.hasNext();)
			{
				IClientTicker ticker = iter.next();

				if(ticker.needsTicks())
				{
					ticker.clientTick();
				}
				else {
					iter.remove();
				}
			}
		}
	}

	public static void zoom(float value)
    {
//        FMLClientHandler.instance().getClient().entityRenderer.thirdPersonDistance = value;
//        FMLClientHandler.instance().getClient().entityRenderer.thirdPersonDistancePrev = value;
    }

	
	public static void killDeadNetworks()
	{
		for(Iterator<IClientTicker> iter = tickingSet.iterator(); iter.hasNext();)
		{
			if(!iter.next().needsTicks())
			{
				iter.remove();
			}
		}
	}
	
	@SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
        final Minecraft minecraft = FMLClientHandler.instance().getClient();
        final EntityPlayerSP player = minecraft.thePlayer;
        final EntityPlayerSP playerBaseClient = PlayerUtil.getPlayerBaseClientFromPlayer(player, false);
        LSPlayerStatsClient stats = null;

        if (player != null)
        {
            stats = LSPlayerStatsClient.get(playerBaseClient);
        }

        if (event.phase == Phase.END)
        {
            if (minecraft.currentScreen instanceof GuiIngameMenu)

            if (player != null)
            {
                LabStuffClientProxy.playerPosX = player.prevPosX + (player.posX - player.prevPosX) * event.renderTickTime;
                LabStuffClientProxy.playerPosY = player.prevPosY + (player.posY - player.prevPosY) * event.renderTickTime;
                LabStuffClientProxy.playerPosZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.renderTickTime;
                LabStuffClientProxy.playerRotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * event.renderTickTime;
                LabStuffClientProxy.playerRotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.renderTickTime;
            }

//            if (player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityTier1Rocket)
//            {
//                float f = (((EntityTier1Rocket) player.getRidingEntity()).timeSinceLaunch - 250F) / 175F;
//
//                if (f < 0)
//                {
//                    f = 0F;
//                }
//
//                if (f > 1)
//                {
//                    f = 1F;
//                }
//
//                final ScaledResolution scaledresolution = ClientUtil.getScaledRes(minecraft, minecraft.displayWidth, minecraft.displayHeight);
//                scaledresolution.getScaledWidth();
//                scaledresolution.getScaledHeight();
//                minecraft.entityRenderer.setupOverlayRendering();
//                GL11.glEnable(GL11.GL_BLEND);
//                GL11.glDisable(GL11.GL_DEPTH_TEST);
//                GL11.glDepthMask(false);
//                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//                GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
//                GL11.glDisable(GL11.GL_ALPHA_TEST);
//                GL11.glDepthMask(true);
//                GL11.glEnable(GL11.GL_DEPTH_TEST);
//                GL11.glEnable(GL11.GL_ALPHA_TEST);
//                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            }

            if (minecraft.currentScreen == null && player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntitySpaceshipBase && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                OverlayRocket.renderSpaceshipOverlay(((EntitySpaceshipBase) player.getRidingEntity()).getSpaceshipGui());
            }

            if (minecraft.currentScreen == null && player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityLander && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                OverlayLander.renderLanderOverlay();
            }

            if (minecraft.currentScreen == null && player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntityAutoRocket && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI)
            {
                OverlayDockingRocket.renderDockingOverlay();
            }

            if (minecraft.currentScreen == null && player != null && player.getRidingEntity() != null && player.getRidingEntity() instanceof EntitySpaceshipBase && minecraft.gameSettings.thirdPersonView != 0 && !minecraft.gameSettings.hideGUI && ((EntitySpaceshipBase) minecraft.thePlayer.getRidingEntity()).launchPhase != EnumLaunchPhase.LAUNCHED.ordinal())
            {
                OverlayLaunchCountdown.renderCountdownOverlay();
            }

            if (player != null && player.worldObj.provider instanceof ILabstuffWorldProvider && OxygenUtil.shouldDisplayTankGui(minecraft.currentScreen) && OxygenUtil.noAtmosphericCombustion(player.worldObj.provider) && !playerBaseClient.isSpectator())
            {
                int var6 = (ClientTickHandler.airRemaining - 90) * -1;

                if (ClientTickHandler.airRemaining <= 0)
                {
                    var6 = 90;
                }

                int var7 = (ClientTickHandler.airRemaining2 - 90) * -1;

                if (ClientTickHandler.airRemaining2 <= 0)
                {
                    var7 = 90;
                }

                int thermalLevel = stats.getThermalLevel() + 22;
                OverlayOxygenTanks.renderOxygenTankIndicator(thermalLevel, var6, var7, !ConfigManagerCore.oxygenIndicatorLeft, !ConfigManagerCore.oxygenIndicatorBottom, Math.abs(thermalLevel - 22) >= 10 && !stats.isThermalLevelNormalising());
            }

            if (playerBaseClient != null && player.worldObj.provider instanceof ILabstuffWorldProvider && !stats.isOxygenSetupValid() && OxygenUtil.noAtmosphericCombustion(player.worldObj.provider) && minecraft.currentScreen == null && !playerBaseClient.capabilities.isCreativeMode && !playerBaseClient.isSpectator())
            {
                OverlayOxygenWarning.renderOxygenWarningOverlay();
            }

            try
            {
                Class clazz = Class.forName("keegan.labstuff.core.atoolkit.ProcessGraphic");
                clazz.getMethod("onTick").invoke(null);
            }
            catch (Exception e)
            {
            }
        }
    }

	
	private void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = (par5 >> 24 & 255) / 255.0F;
        float f1 = (par5 >> 16 & 255) / 255.0F;
        float f2 = (par5 >> 8 & 255) / 255.0F;
        float f3 = (par5 & 255) / 255.0F;
        float f4 = (par6 >> 24 & 255) / 255.0F;
        float f5 = (par6 >> 16 & 255) / 255.0F;
        float f6 = (par6 >> 8 & 255) / 255.0F;
        float f7 = (par6 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(par3, par2, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(par1, par2, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(par1, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldRenderer.pos(par3, par4, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

}