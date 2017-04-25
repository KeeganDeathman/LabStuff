package keegan.labstuff.command;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.dimension.SpaceStationWorldData;
import keegan.labstuff.util.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandSpaceStationChangeOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " <dim#> <player>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "ssnewowner";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String oldOwner = null;
        String newOwner = "ERROR";
        int stationID = -1;
        EntityPlayerMP playerAdmin = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);

        if (args.length > 1)
        {
            newOwner = args[1];

            try
            {
                stationID = Integer.parseInt(args[0]);
            }
            catch (final Exception var6)
            {
                throw new WrongUsageException(LabStuffUtils.translateWithFormat("commands.ssnewowner.wrong_usage", this.getCommandUsage(sender)), new Object[0]);
            }

            if (stationID < 2)
            {
                throw new WrongUsageException(LabStuffUtils.translateWithFormat("commands.ssnewowner.wrong_usage", this.getCommandUsage(sender)), new Object[0]);
            }

            try
            {
                SpaceStationWorldData stationData = SpaceStationWorldData.getMPSpaceStationData(null, stationID, null);
                if (stationData == null)
                {
                    throw new WrongUsageException(LabStuffUtils.translateWithFormat("commands.ssnewowner.wrong_usage", this.getCommandUsage(sender)), new Object[0]);
                }

                oldOwner = stationData.getOwner();
                stationData.getAllowedPlayers().remove(oldOwner);
                if (stationData.getSpaceStationName().equals("Station: " + oldOwner))
                {
                    stationData.setSpaceStationName("Station: " + newOwner);
                }
                stationData.getAllowedPlayers().add(newOwner);
                stationData.setOwner(newOwner);

                final EntityPlayerMP oldPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(oldOwner, true);
                final EntityPlayerMP newPlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(newOwner, true);
                if (oldPlayer != null)
                {
                    LSPlayerStats stats = LSPlayerStats.get(oldPlayer);
                    SpaceStationWorldData.updateSSOwnership(oldPlayer, oldOwner, stats, stationID, stationData);
                    LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, LabStuffUtils.getDimensionID(oldPlayer.worldObj), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), oldPlayer);
                }
                if (newPlayer != null)
                {
                    LSPlayerStats stats = LSPlayerStats.get(newPlayer);
                    SpaceStationWorldData.updateSSOwnership(newPlayer, newOwner.replace(".", ""), stats, stationID, stationData);
                    LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_SPACESTATION_CLIENT_ID, LabStuffUtils.getDimensionID(oldPlayer.worldObj), new Object[] { WorldUtil.spaceStationDataToString(stats.getSpaceStationDimensionData()) }), newPlayer);
                }
            }
            catch (final Exception var6)
            {
                throw new CommandException(var6.getMessage(), new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException(LabStuffUtils.translateWithFormat("commands.ssinvite.wrong_usage", this.getCommandUsage(sender)), new Object[0]);
        }

        if (playerAdmin != null)
        {
            playerAdmin.addChatMessage(new TextComponentString(LabStuffUtils.translateWithFormat("gui.spacestation.changesuccess", oldOwner, newOwner)));
        }
        else
        //Console
        {
            System.out.println(LabStuffUtils.translateWithFormat("gui.spacestation.changesuccess", oldOwner, newOwner));
        }
    }
}