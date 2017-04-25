package keegan.labstuff.dimension;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.entities.EntityLandingBalloons;
import keegan.labstuff.util.Vector3;
import keegan.labstuff.world.ITeleportType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.*;

public class TeleportTypeMars implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
    {
        if (player != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);
            double x = stats.getCoordsTeleportedFromX();
            double z = stats.getCoordsTeleportedFromZ();
            int limit = ConfigManagerCore.otherPlanetWorldBorders - 2;
            if (limit > 20)
            {
                if (x > limit)
                {
                    z *= limit / x;
                    x = limit;
                }
                else if (x < -limit)
                {   
                    z *= -limit / x;
                    x = -limit;
                }
                if (z > limit)
                {
                    x *= limit / z;
                    z = limit;
                }
                else if (z < -limit)
                {
                    x *= - limit / z;
                    z = -limit;
                }
            }
            return new Vector3(x, 900.0, z);
        }

        return null;
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity entity)
    {
        return new Vector3(entity.posX, 900.0, entity.posZ);
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        if (!ridingAutoRocket && player != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);
            CapabilityUtils.getCapability(player, Capabilities.RESEARCHER_CAPABILITY, null).research(LabStuffMain.MARS);

            if (stats.getTeleportCooldown() <= 0)
            {
                if (player.capabilities.isFlying)
                {
                    player.capabilities.isFlying = false;
                }

                EntityLandingBalloons lander = new EntityLandingBalloons(player);

                if (!newWorld.isRemote)
                {
                    newWorld.spawnEntityInWorld(lander);
                }

                stats.setTeleportCooldown(10);
            }
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub

    }
}