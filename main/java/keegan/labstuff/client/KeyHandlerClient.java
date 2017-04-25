package keegan.labstuff.client;

import org.lwjgl.input.Keyboard;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.common.capabilities.LSPlayerStatsClient;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.entities.*;
import keegan.labstuff.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;

public class KeyHandlerClient extends KeyHandler
{
    public static KeyBinding galaxyMap;
    public static KeyBinding openFuelGui;
    public static KeyBinding toggleAdvGoggles;

    static
    {
        galaxyMap = new KeyBinding(LabStuffUtils.translate("keybind.map.name"), ConfigManagerCore.keyOverrideMapI == 0 ? Keyboard.KEY_M : ConfigManagerCore.keyOverrideMapI, "labstuff");
        openFuelGui = new KeyBinding(LabStuffUtils.translate("keybind.spaceshipinv.name"), ConfigManagerCore.keyOverrideFuelLevelI == 0 ? Keyboard.KEY_F : ConfigManagerCore.keyOverrideFuelLevelI, "labstuff");
        // See ConfigManagerCore.class for actual defaults. These do nothing
    }

    public static KeyBinding accelerateKey;
    public static KeyBinding decelerateKey;
    public static KeyBinding leftKey;
    public static KeyBinding rightKey;
    public static KeyBinding upKey;
    public static KeyBinding downKey;
    public static KeyBinding spaceKey;
    public static KeyBinding leftShiftKey;
    private static Minecraft mc = Minecraft.getMinecraft();

    public KeyHandlerClient()
    {
        super(new KeyBinding[] { KeyHandlerClient.galaxyMap, KeyHandlerClient.openFuelGui, KeyHandlerClient.toggleAdvGoggles }, new boolean[] { false, false, false }, KeyHandlerClient.getVanillaKeyBindings(), new boolean[] { false, true, true, true, true, true, true });
    }

    private static KeyBinding[] getVanillaKeyBindings()
    {
        KeyBinding invKey = KeyHandlerClient.mc.gameSettings.keyBindInventory;
        KeyHandlerClient.accelerateKey = KeyHandlerClient.mc.gameSettings.keyBindForward;
        KeyHandlerClient.decelerateKey = KeyHandlerClient.mc.gameSettings.keyBindBack;
        KeyHandlerClient.leftKey = KeyHandlerClient.mc.gameSettings.keyBindLeft;
        KeyHandlerClient.rightKey = KeyHandlerClient.mc.gameSettings.keyBindRight;
        KeyHandlerClient.upKey = KeyHandlerClient.mc.gameSettings.keyBindForward;
        KeyHandlerClient.downKey = KeyHandlerClient.mc.gameSettings.keyBindBack;
        KeyHandlerClient.spaceKey = KeyHandlerClient.mc.gameSettings.keyBindJump;
        KeyHandlerClient.leftShiftKey = KeyHandlerClient.mc.gameSettings.keyBindSneak;
        return new KeyBinding[] { invKey, KeyHandlerClient.accelerateKey, KeyHandlerClient.decelerateKey, KeyHandlerClient.leftKey, KeyHandlerClient.rightKey, KeyHandlerClient.spaceKey, KeyHandlerClient.leftShiftKey };
    }

    @Override
    public void keyDown(Type types, KeyBinding kb, boolean tickEnd, boolean isRepeat)
    {
        if (KeyHandlerClient.mc.thePlayer != null && tickEnd)
        {
            EntityPlayerSP playerBase = PlayerUtil.getPlayerBaseClientFromPlayer(KeyHandlerClient.mc.thePlayer, false);

            if (playerBase == null)
            {
                return;
            }

            LSPlayerStatsClient stats = LSPlayerStatsClient.get(playerBase);

            if (kb.getKeyCode() == KeyHandlerClient.galaxyMap.getKeyCode())
            {
                if (KeyHandlerClient.mc.currentScreen == null)
                {
                    KeyHandlerClient.mc.thePlayer.openGui(LabStuffMain.instance, 33, KeyHandlerClient.mc.theWorld, (int) KeyHandlerClient.mc.thePlayer.posX, (int) KeyHandlerClient.mc.thePlayer.posY, (int) KeyHandlerClient.mc.thePlayer.posZ);
                }
            }
            else if (kb.getKeyCode() == KeyHandlerClient.openFuelGui.getKeyCode())
            {
                if (playerBase.getRidingEntity() instanceof EntitySpaceshipBase || playerBase.getRidingEntity() instanceof EntityBuggy)
                {
                    LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_FUEL_GUI, mc.theWorld.provider.getDimension(), new Object[] { playerBase.getGameProfile().getName() }));
                }
            }
            else if (kb.getKeyCode() == KeyHandlerClient.toggleAdvGoggles.getKeyCode())
            {
                if (playerBase != null)
                {
                    stats.setUsingAdvancedGoggles(!stats.isUsingAdvancedGoggles());
                }
            }
        }

        if (KeyHandlerClient.mc.thePlayer != null && KeyHandlerClient.mc.currentScreen == null)
        {
            int keyNum = -1;

            if (kb == KeyHandlerClient.accelerateKey)
            {
                keyNum = 0;
            }
            else if (kb == KeyHandlerClient.decelerateKey)
            {
                keyNum = 1;
            }
            else if (kb == KeyHandlerClient.leftKey)
            {
                keyNum = 2;
            }
            else if (kb == KeyHandlerClient.rightKey)
            {
                keyNum = 3;
            }
            else if (kb == KeyHandlerClient.spaceKey)
            {
                keyNum = 4;
            }
            else if (kb == KeyHandlerClient.leftShiftKey)
            {
                keyNum = 5;
            }

            Entity entityTest = KeyHandlerClient.mc.thePlayer.getRidingEntity();
            if (entityTest != null && entityTest instanceof IControllableEntity && keyNum != -1)
            {
                IControllableEntity entity = (IControllableEntity) entityTest;

                if (kb.getKeyCode() == KeyHandlerClient.mc.gameSettings.keyBindInventory.getKeyCode())
                {
                    KeyBinding.setKeyBindState(KeyHandlerClient.mc.gameSettings.keyBindInventory.getKeyCode(), false);
                }

                entity.pressKey(keyNum);
            }
            else if (entityTest != null && entityTest instanceof EntityAutoRocket)
            {
                EntityAutoRocket autoRocket = (EntityAutoRocket) entityTest;

                if (autoRocket.landing)
                {
                    if (kb == KeyHandlerClient.leftShiftKey)
                    {
                        autoRocket.motionY -= 0.02D;
                        LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, LabStuffUtils.getDimensionID(mc.theWorld), new Object[] { autoRocket.getEntityId(), false }));
                    }

                    if (kb == KeyHandlerClient.spaceKey)
                    {
                        autoRocket.motionY += 0.02D;
                        LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_SHIP_MOTION_Y, LabStuffUtils.getDimensionID(mc.theWorld), new Object[] { autoRocket.getEntityId(), true }));
                    }
                }
            }
        }
    }

    @Override
    public void keyUp(Type types, KeyBinding kb, boolean tickEnd)
    {
    }
}