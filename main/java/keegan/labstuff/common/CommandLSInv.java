package keegan.labstuff.common;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.container.InventoryExtended;
import keegan.labstuff.util.PlayerUtil;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandLSInv extends CommandBase
{
    protected static final Map<String, ItemStack[]> savedata = new HashMap<String, ItemStack[]>();
    private static final Set<String> dontload = new HashSet<String>();
    private static boolean firstuse = true;
    private static LSInvSaveData savefile;

    @Override
    public String getCommandUsage(ICommandSender var1)
    {
        return "/" + this.getCommandName() + " [save|restore|drop|clear] <playername>";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public String getCommandName()
    {
        return "gcinv";
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "save", "restore", "drop", "clear");
        }
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] par1ArrayOfStr, int par2)
    {
        return par2 == 1;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (CommandLSInv.firstuse)
        {
            CommandLSInv.firstuse = false;
            CommandLSInv.initialise();
        }

        if (args.length == 2)
        {
            try
            {
                EntityPlayerMP thePlayer = PlayerUtil.getPlayerBaseServerFromPlayerUsername(args[1], true, server.getEntityWorld());
                if (thePlayer != null && !thePlayer.isDead && thePlayer.worldObj != null)
                {
                    LSPlayerStats stats = LSPlayerStats.get(thePlayer);

                    if (args[0].equalsIgnoreCase("drop"))
                    {
                        InventoryExtended gcInventory = stats.getExtendedInventory();
                        gcInventory.dropExtendedItems(thePlayer);
                    }
                    else if (args[0].equalsIgnoreCase("save"))
                    {
                        InventoryExtended gcInventory = stats.getExtendedInventory();
                        ItemStack[] saveinv = new ItemStack[gcInventory.getSizeInventory()];
                        for (int i = 0; i < gcInventory.getSizeInventory(); i++)
                        {
                            saveinv[i] = gcInventory.getStackInSlot(i);
                            gcInventory.setInventorySlotContents(i, null);
                        }

                        CommandLSInv.savedata.put(args[1].toLowerCase(), saveinv);
                        CommandLSInv.dontload.add(args[1].toLowerCase());
                        CommandLSInv.writefile();
                        System.out.println("[GCInv] Saving and clearing GC inventory slots of " + thePlayer.getGameProfile().getName());
                    }
                    else if (args[0].equalsIgnoreCase("restore"))
                    {
                        ItemStack[] saveinv = CommandLSInv.savedata.get(args[1].toLowerCase());
                        CommandLSInv.dontload.remove(args[1].toLowerCase());
                        if (saveinv == null)
                        {
                            System.out.println("[GCInv] Tried to restore but player " + thePlayer.getGameProfile().getName() + " had no saved GC inventory items.");
                            return;
                        }

                        CommandLSInv.doLoad(thePlayer);
                    }
                    else if (args[0].equalsIgnoreCase("clear"))
                    {
                        InventoryExtended gcInventory = stats.getExtendedInventory();
                        for (int i = 0; i < gcInventory.getSizeInventory(); i++)
                        {
                            gcInventory.setInventorySlotContents(i, null);
                        }
                    }
                    else
                    {
                        throw new WrongUsageException("Invalid GCInv command. Usage: " + this.getCommandUsage(sender), new Object[0]);
                    }
                }
                else
                {
                    // Special rule for 'restore' command if player not found -
                    // look to see if the player is offline (i.e. had a saved
                    // inventory already)
                    if (args[0].equalsIgnoreCase("restore"))
                    {
                        ItemStack[] saveinv = CommandLSInv.savedata.get(args[1].toLowerCase());
                        if (saveinv != null)
                        {
                            System.out.println("[GCInv] Restore command for offline player " + args[1] + ", setting to restore GCInv on next login.");
                            CommandLSInv.dontload.remove(args[1].toLowerCase());
                            // Now it can autoload on next player logon
                            return;
                        }
                    }

                    // No player found, and not a 'restore' command
                    if (args[0].equalsIgnoreCase("clear") || args[0].equalsIgnoreCase("save") || args[0].equalsIgnoreCase("drop"))
                    {
                        System.out.println("GCInv command: player " + args[1] + " not found.");
                    }
                    else
                    {
                        throw new WrongUsageException("Invalid GCInv command. Usage: " + this.getCommandUsage(sender), new Object[0]);
                    }
                }
            }
            catch (final Exception e)
            {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
        else
        {
            throw new WrongUsageException("Not enough command arguments! Usage: " + this.getCommandUsage(sender), new Object[0]);
        }
    }

    public static void doLoad(EntityPlayerMP thePlayer)
    {
        String theName = thePlayer.getGameProfile().getName().toLowerCase();
        if (!CommandLSInv.dontload.contains(theName))
        // This is a simple flag: if the playername is in dontload then no
        // restore command has yet been run.
        // Dontload resets to nothing on server restart so that all will
        // auto-restore on a server restart.
        {
            ItemStack[] saveinv = CommandLSInv.savedata.get(theName);
            InventoryExtended gcInventory = LSPlayerStats.get(thePlayer).getExtendedInventory();
            for (int i = 0; i < gcInventory.getSizeInventory(); i++)
            {
                gcInventory.setInventorySlotContents(i, saveinv[i]);
            }
            CommandLSInv.savedata.remove(theName);
            CommandLSInv.writefile();
            System.out.println("[GCInv] Restored GC inventory slots of " + thePlayer.getGameProfile().getName());

        }
        else
        {
            System.out.println("[GCInv] Player " + thePlayer.getGameProfile().getName() + " was spawned without restoring the GCInv save.  Run /gcinv restore playername to restore it.");
        }
    }

    private static void writefile()
    {
        CommandLSInv.savefile.writeToNBT(new NBTTagCompound());
        CommandLSInv.savefile.markDirty();
    }

    private static void initialise()
    {
        World world0 = LabStuffMain.proxy.getWorldForID(0);
        if (world0 == null)
        {
            return;
        }
        CommandLSInv.savefile = (LSInvSaveData) world0.loadItemData(LSInvSaveData.class, LSInvSaveData.SAVE_ID);
        if (CommandLSInv.savefile == null)
        {
            CommandLSInv.savefile = new LSInvSaveData();
            world0.setItemData(LSInvSaveData.SAVE_ID, CommandLSInv.savefile);
        }
    }

    public static ItemStack[] getSaveData(String p)
    {
        if (CommandLSInv.firstuse)
        {
            CommandLSInv.firstuse = false;
            CommandLSInv.initialise();
        }

        return CommandLSInv.savedata.get(p);
    }
}