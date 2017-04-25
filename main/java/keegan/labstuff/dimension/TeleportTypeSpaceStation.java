package keegan.labstuff.dimension;

import keegan.labstuff.common.EnumColor;
import keegan.labstuff.util.*;
import keegan.labstuff.world.ITeleportType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.*;

public class TeleportTypeSpaceStation implements ITeleportType
{
    @Override
    public boolean useParachute()
    {
        return false;
    }

    @Override
    public Vector3 getPlayerSpawnLocation(WorldServer world, EntityPlayerMP player)
    {
        return new Vector3(0.5, 65.0, 0.5);
    }

    @Override
    public Vector3 getEntitySpawnLocation(WorldServer world, Entity player)
    {
        return new Vector3(0.5, 65.0, 0.5);
    }

    @Override
    public void onSpaceDimensionChanged(World newWorld, EntityPlayerMP player, boolean ridingAutoRocket)
    {
        if (!newWorld.isRemote)
        {
            player.addChatMessage(new TextComponentString(EnumColor.YELLOW + LabStuffUtils.translate("gui.spacestation.type_command") + " " + EnumColor.AQUA + "/ssinvite " + LabStuffUtils.translate("gui.spacestation.playername") + " " + EnumColor.YELLOW + LabStuffUtils.translate("gui.spacestation.to_allow_entry")));
        }
    }

    @Override
    public void setupAdventureSpawn(EntityPlayerMP player)
    {
        // TODO Auto-generated method stub

    }
}