package keegan.labstuff.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketDSCBench;
import keegan.labstuff.container.ContainerDSCBench;
import keegan.labstuff.recipes.*;
import keegan.labstuff.tileentity.DSCBench;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

public class GuiDSCBench extends GuiContainer
{

	private int itemNumber = 0;
	private String itemName = "";
	private ArrayList<DiscoveryItem> designs;
	private DSCBench tile;
	private GuiButton draw;
	private GuiButton left;
	private GuiButton right;

	public GuiDSCBench(InventoryPlayer inv, DSCBench tile)
	{
		super(new ContainerDSCBench(inv, tile));
		this.tile = tile;

		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(this.draw = new GuiButton(0, xSize + 40 + 2, ySize + 2, 100, 20, "Create"));
		this.buttonList.add(this.left = new GuiButton(1, xSize + 2, ySize - 20, 20, 20, "<"));
		this.buttonList.add(this.right = new GuiButton(2, xSize + 115 + 2, ySize - 20, 20, 20, ">"));
		designs = Recipes.discoveryItems;
		/*for (DiscoveryItem design : designs)
		{
			if (!tile.getInstalled().contains(design.getDependency())) 
				designs.remove(design);
		}*/
		if (designs.size() == 0) 
			mc.thePlayer.closeScreen();
		itemName = designs.get(0).getName();
	}

	@Override
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.id == 2)
		{
			itemNumber += 1;
			if (itemNumber > (designs.size() - 1)) 
				itemNumber = 0;
			itemName = designs.get(itemNumber).getName();
		}
		else if (par1GuiButton.id == 1)
		{
			itemNumber -= 1;
			if (itemNumber < 0) 
				itemNumber = (designs.size() - 1);
			itemName = designs.get(itemNumber).getName();
		}
		else if (par1GuiButton.id == 0)
		{
			DiscoveryItem item;
			for(DiscoveryItem i : designs)
			{
				if(i.getName().equals(itemName))
				{
					item = i;
					LabStuffMain.packetPipeline.sendToServer(new PacketDSCBench(item,tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ()));
				}
			}
		}
		else
		{

		}

	}

	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(new TextComponentTranslation("container.inventory").getFormattedText(), -30, ySize - 255 + 2, 4210752);
		this.fontRendererObj.drawString(itemName, xSize - 150 + 2, ySize - 100 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/dscbench.png"));
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
