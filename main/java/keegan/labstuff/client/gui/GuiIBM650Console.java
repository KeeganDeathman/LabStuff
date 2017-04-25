package keegan.labstuff.client.gui;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.IBM650ConsolePacket;
import keegan.labstuff.container.ContainerIBM650Console;
import keegan.labstuff.tileentity.IBM650Console;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiIBM650Console extends GuiContainer 
{
	private IBM650Console tile;
	
	public GuiIBM650Console(InventoryPlayer inv, IBM650Console tile)
	{
		super(new ContainerIBM650Console(inv,tile));
		this.tile = tile;
	}

	private GuiButton btn = new GuiButton(0,85,220,200,20,"Run Program");

	
	public void initGui()
	{
		this.buttonList.add(btn);
	}
	
	@Override
	public void actionPerformed(GuiButton btn)
	{
		if(btn.id == 0)
		{
			LabStuffMain.packetPipeline.sendToServer(new IBM650ConsolePacket(tile.getPos(), LabStuffUtils.getDimensionID(tile.getWorld())));
		}
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.btn.enabled = tile.running;
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY)
	{
		this.drawDefaultBackground();
				
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/ibm650console.png"));
		int x = (this.width - 256) / 2;
		int y = (this.height - 256) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, 256, 256);
		
	}
	
	
}
