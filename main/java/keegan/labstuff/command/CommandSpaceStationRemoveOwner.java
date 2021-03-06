package keegan.labstuff.command;

import java.util.*;

import com.google.common.collect.Sets;

import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.dimension.SpaceStationWorldData;
import keegan.labstuff.util.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandSpaceStationRemoveOwner extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " <player>";
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
        return "ssuninvite";
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
                playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), false);

                if (playerBase != null)
                {
                    LSPlayerStats stats = LSPlayerStats.get(playerBase);

                    if (stats.getSpaceStationDimensionData().isEmpty())
                    {
                        throw new WrongUsageException(LabStuffUtils.translate("commands.ssinvite.not_found"), new Object[0]);
                    }
                    else
                    {
                        for (Map.Entry<Integer, Integer> e : stats.getSpaceStationDimensionData().entrySet())
                        {
                            final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, e.getValue(), playerBase);

                            String str = null;
                            for (String name : data.getAllowedPlayers())
                            {
                                if (name.equalsIgnoreCase(var3))
                                {
                                    str = name;
                                    break;
                                }
                            }

                            if (str != null)
                            {
                                data.getAllowedPlayers().remove(str);
                                data.markDirty();
                            }
                            else
                            {
                                throw new CommandException(LabStuffUtils.translateWithFormat("commands.ssuninvite.no_player", "\"" + var3 + "\""), new Object[0]);
                            }
                        }
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
            playerBase.addChatMessage(new TextComponentString(LabStuffUtils.translateWithFormat("gui.spacestation.removesuccess", var3)));
        }
    }


    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, this.getPlayers(server, sender)) : null;
    }

    protected String[] getPlayers(MinecraftServer server, ICommandSender sender)
    {
        EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), false);

        if (playerBase != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(playerBase);
            if (!stats.getSpaceStationDimensionData().isEmpty())
            {
                String[] allNames = server.getAllUsernames();
                //data.getAllowedPlayers may include some in lowercase
                //Convert to correct case at least for those players who are online
                HashSet<String> allowedNames = Sets.newHashSet();

                for (Map.Entry<Integer, Integer> e : stats.getSpaceStationDimensionData().entrySet())
                {
                    final SpaceStationWorldData data = SpaceStationWorldData.getStationData(playerBase.worldObj, e.getValue(), playerBase);
                    allowedNames.addAll(data.getAllowedPlayers());
                }

                Iterator<String> itName = allowedNames.iterator();
                ArrayList<String> replaceNames = new ArrayList<String>();
                while (itName.hasNext())
                {
                    String name = itName.next();
                    for (String allEntry : allNames)
                    {
                        if (name.equalsIgnoreCase(allEntry))
                        {
                            itName.remove();
                            replaceNames.add(allEntry);
                        }
                    }
                }
                //This does the conversion to correct case
                allowedNames.addAll(replaceNames);
                String[] rvsize = new String[allowedNames.size()];
                return allowedNames.toArray(rvsize);
            }
        }

        String[] returnvalue = { "" };
        return returnvalue;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 0;
    }
}