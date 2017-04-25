package keegan.labstuff.command;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.EnumColor;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.entities.IRocketType;
import keegan.labstuff.util.*;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public class CommandDimTele extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [<player>]";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "dimensiontp";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP playerBase = null;

        if (args.length < 2)
        {
            try
            {
                if (args.length == 1)
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[0], true);
                }
                else
                {
                    playerBase = PlayerUtil.getPlayerBaseServerFromPlayerUsername(sender.getName(), true);
                }

                if (playerBase != null)
                {
                    WorldServer worldserver = server.worldServerForDimension(LabStuffUtils.getDimensionID(server.worldServers[0]));
                    BlockPos spawnPoint = worldserver.getSpawnPoint();
                    LSPlayerStats stats = LSPlayerStats.get(playerBase);
                    stats.setRocketStacks(new ItemStack[2]);
                    stats.setRocketType(IRocketType.EnumRocketType.DEFAULT.ordinal());
                    stats.setRocketItem(LabStuffMain.eagle);
                    stats.setFuelLevel(1000);
                    stats.setCoordsTeleportedFromX(spawnPoint.getX());
                    stats.setCoordsTeleportedFromZ(spawnPoint.getZ());

                    try
                    {
                        WorldUtil.toCelestialSelection(playerBase, stats, Integer.MAX_VALUE);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        throw e;
                    }

                    CommandBase.notifyCommandListener(sender, this, "commands.dimensionteleport", new Object[] { String.valueOf(EnumColor.GREY + "[" + playerBase.getName()), "]" });
                }
                else
                {
                    throw new Exception("Could not find player with name: " + args[0]);
                }
            }
            catch (final Exception var6)
            {
                throw new CommandException(var6.getMessage(), new Object[0]);
            }
        }
        else
        {
            throw new WrongUsageException(LabStuffUtils.translateWithFormat("commands.dimensiontp.too_many", this.getCommandUsage(sender)), new Object[0]);
        }
    }
}