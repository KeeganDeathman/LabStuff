package keegan.labstuff.dimension;

import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandDimTele extends CommandBase
{
	@Override
	public String getCommandName()
	{
		return "dtp";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "dtp <player> <x> <y> <z> <dim>";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 4)
		{
			//intra-dim
			double x;
			double y;
			double z;
			EntityPlayer player = server.getEntityWorld().getPlayerEntityByName(args[0]);
			if(args[1].startsWith("~"))
				x = player.posX +  Double.parseDouble(args[0].replace("~", ""));
			else
				x = Double.parseDouble(args[0]);
			if(args[2].startsWith("~"))
				y = player.posY +  Double.parseDouble(args[1].replace("~", ""));
			else
				y = Double.parseDouble(args[1]);
			if(args[2].startsWith("~"))
				z = player.posX +  Double.parseDouble(args[2].replace("~", ""));
			else
				z = Double.parseDouble(args[2]);
			player.setPositionAndUpdate(x, y, z);
		}
		else if(args.length == 5)
		{
			//inter-dim

			//intra-dim
			double x;
			double y;
			double z;
			EntityPlayer player = server.getEntityWorld().getPlayerEntityByName(args[0]);
			if(args[1].startsWith("~"))
				x = player.posX +  Double.parseDouble(args[0].replace("~", ""));
			else
				x = Double.parseDouble(args[0]);
			if(args[2].startsWith("~"))
				y = player.posY +  Double.parseDouble(args[1].replace("~", ""));
			else
				y = Double.parseDouble(args[1]);
			if(args[2].startsWith("~"))
				z = player.posX +  Double.parseDouble(args[2].replace("~", ""));
			else
				z = Double.parseDouble(args[2]);
			player.changeDimension(Integer.parseInt(args[3]));
			player.setPositionAndUpdate(x, y, z);
		}
		else
			throw new CommandException("Improper Usage", null);
	}
}