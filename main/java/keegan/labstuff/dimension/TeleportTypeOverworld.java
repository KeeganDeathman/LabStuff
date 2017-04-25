package keegan.labstuff.dimension;

import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.util.Vector3;
import keegan.labstuff.world.ITeleportType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.*;

public class TeleportTypeOverworld implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return true;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
    {
        if (player != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);
            return new Vector3(stats.getCoordsTeleportedFromX(), 250.0, stats.getCoordsTeleportedFromZ());
        }

        return null;
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
    {
        return new Vector3(entity.posX, 250.0, entity.posZ);
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub

    }
}