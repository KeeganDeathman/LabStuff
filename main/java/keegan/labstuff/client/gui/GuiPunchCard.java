package keegan.labstuff.client.gui;

import java.util.Arrays;

import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GuiPunchCard extends GuiScreen 
{
	private EntityPlayer player;
	
	public GuiPunchCard(EntityPlayer p)
	{
		player = p;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		this.drawDefaultBackground();
		
		int[] card = LabStuffUtils.convertToBinary(player.getHeldItemMainhand().getTagCompound().getString("card"));
		card = Arrays.copyOf(card, 960);
		for(int j = 0; j < 12; j++)
		{
			int[] row = Arrays.copyOfRange(card, j*80, j*80+79);
			for(int i = 0; i < row.length; i++)
				this.drawString(Minecraft.getMinecraft().fontRendererObj, "" + row[i], (i*8), 10 + (j*10), 0xFFFFFF);
			
		}
		super.drawScreen(mouseX, mouseY, partialTick);
	}
	
	
}
