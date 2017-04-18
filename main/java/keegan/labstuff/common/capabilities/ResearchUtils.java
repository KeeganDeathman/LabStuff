package keegan.labstuff.common.capabilities;

import keegan.labstuff.recipes.Research;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public class ResearchUtils 
{
	public static void research(EntityPlayer player, Research r)
	{
		player.getCapability(Capabilities.RESEARCHER_CAPABILITY, null).research(r);
		player.addChatComponentMessage(new TextComponentString("[LabStuff] You have researched " + r.getName()));
	}
}
