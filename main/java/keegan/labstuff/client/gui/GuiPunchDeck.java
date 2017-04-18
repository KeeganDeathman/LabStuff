package keegan.labstuff.client.gui;

import java.util.*;

import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class GuiPunchDeck extends GuiScreen 
{
	private EntityPlayer player;
	
	public GuiPunchDeck(EntityPlayer p)
	{
		player = p;
	}
	
	private int page = 0;
	private ArrayList<String> deck = new ArrayList<String>();
	
	
	@Override
	public void initGui()
	{
		this.buttonList.add(new GuiButton(0, 0, height-20, 40, 20, "<"));
		this.buttonList.add(new GuiButton(0, width-41, height-20, 40, 20, ">"));
	}
	
	@Override
	public void actionPerformed(GuiButton btn)
	{
		if(btn.id == 0)
			page-=1;
		if(btn.id == 1)
			page+=1;
		if(page == -1)
			page = deck.size()-1;
		if(page == deck.size())
			page = 0;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTick)
	{
		this.drawDefaultBackground();
		
		NBTTagCompound cards = (NBTTagCompound)player.getHeldItemMainhand().getTagCompound().getTag("cards");
		
		deck.clear();
		for(String key : cards.getKeySet())
			deck.add(key);
		
		this.drawString(Minecraft.getMinecraft().fontRendererObj, deck.get(page), (width/2)-20, height-20, 0xFDFDFD);
		int[] card = LabStuffUtils.convertToBinary(cards.getString(deck.get(page)));
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
