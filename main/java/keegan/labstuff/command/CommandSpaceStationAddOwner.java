package keegan.labstuff.command;

import java.util.*;

import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.dimension.SpaceStationWorldData;
import keegan.labstuff.util.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandSpaceStationAddOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [ <player> | +all | -all ]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }

    @Override
    public String getCommandName()
    {
        return "ssinvite";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String var3 = null;
        EntityPlayerMP playerBase = null;

        if (args.length > 0)
        {
            var3 = args[0];

            try
            {
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);

                if (playerBase != null)
                {
                    LSPlayerStats stats = LSPlayerStats.get(playerBase);

                    if (stats.getSpaceStationDimensionData().isEmpty())
                    {
                        throw new WrongUsageException(LabStuffUtils.translate("commands.ssinvite.not_found"), new Object[0]);
                    }
                    else
                    {
                        for (Map.Entry<Integer, Integer> ownedStations : stats.getSpaceStationDimensionData().entrySet())
                        {
                            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, ownedStations.getValue(), playerBase);

                            if (var3.equalsIgnoreCase("+all"))
                            {
                                data.setAllowedAll(true);
                                playerBase.addChatMessage(new TextComponentString(LabStuffUtils.translateWithFormat("gui.spacestation.allow_all_true")));
                                return;
                            }
                            if (var3.equalsIgnoreCase("-all"))
                            {
                                data.setAllowedAll(false);
                                playerBase.addChatMessage(new TextComponentString(LabStuffUtils.translateWithFormat("gui.spacestation.allow_all_false", var3)));
                                return;
                            }

                            if (!data.getAllowedPlayers().contains(var3))
                            {
                                data.getAllowedPlayers().add(var3);
                                data.markDirty();
                            }
                        }
                    }

                    final EntityPlayerMP playerToAdd = PlayerUtil.getPlayerBaseServerFromPlayerUsername(var3, true);

                    if (playerToAdd != null)
                    {
                        playerToAdd.addChatMessage(new TextComponentString(LabStuffUtils.translateWithFormat("gui.spacestation.added", playerBase.getGameProfile().getName())));
                    }
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

        if (playerBase != null)
        {
            playerBase.addChatMessage(new TextComponentString(LabStuffUtils.translateWithFormat("gui.spacestation.addsuccess", var3)));
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getAllUsernames()) : null;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}