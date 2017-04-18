package keegan.labstuff.world;

import java.util.Random;

import keegan.labstuff.util.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.*;

/**
 * Implement into WorldProvider for customizing spawning players and other
 * entities into space dimension
 * <p/>
 * You can also create a separate class, implement it there, then register it in @LabStuffRegistry
 */
public interface ITeleportType
{
    /**
     * This method is used to determine if a player will open parachute upon
     * entering the dimension
     *
     * @return whether player will set parachute open upon entering this
     * dimension
     */
    public boolean useParachute();

    /**
     * Gets the player spawn location when entering this dimension
     *
     * @param world  The world to be spawned into
     * @param player The player to be teleported
     * @return a vector3 object containing the coordinates to be spawned into
     * the world with
     */
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player);

    /**
     * Gets the entity (non-player) spawn location when entering this dimension
     *
     * @param world  The world to be spawned into
     * @param entity the non-player entity to be teleported
     * @return a vector3 object containing the coordinates to be spawned into
     * the world with
     */
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity);


    /**
     * Called when player is transferred to a space dimension
     *
     * @param newWorld         The world object of the entered world
     * @param player           The player that has transferred dimensions
     * @param ridingAutoRocket If the player is riding an auto rocket. Do not spawn in
     *                         landers if so.
     */
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket);
    
    
    /**
     * Used by Asteroids Survival game mode to set up the initial lander inventory
     * 
     * @param player
     */
    public void setupAdventureSpawn(EntityPlayerMP player);
}