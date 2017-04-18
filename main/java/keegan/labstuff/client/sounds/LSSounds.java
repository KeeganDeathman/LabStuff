package keegan.labstuff.client.sounds;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class LSSounds
{
    public static SoundEvent bossDeath;
    public static SoundEvent bossLaugh;
    public static SoundEvent bossOoh;
    public static SoundEvent bossOuch;
    public static SoundEvent slimeDeath;
    public static SoundEvent astroMiner;
    public static SoundEvent shuttle;
    public static SoundEvent parachute;
    public static SoundEvent openAirLock;
    public static SoundEvent closeAirLock;
    public static SoundEvent singleDrip;
    public static SoundEvent scaryScape;

    public static SoundEvent music;

    public static void registerSounds()
    {
        slimeDeath = registerSound("entity.slime_death");
        astroMiner = registerSound("entity.astrominer");
        shuttle = registerSound("shuttle.shuttle");
        parachute = registerSound("player.parachute");
        openAirLock = registerSound("player.openairlock");
        closeAirLock = registerSound("player.closeairlock");
        singleDrip = registerSound("ambience.singledrip");
        music = registerSound("labstuff.musicSpace");
    }

    private static SoundEvent registerSound(String soundName)
    {
        ResourceLocation soundID = new ResourceLocation("labstuff", soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }
}