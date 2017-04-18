package keegan.labstuff.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketGravity;
import keegan.labstuff.container.ContainerEnricher;
import keegan.labstuff.tileentity.*;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;

public class GuiEnricher extends GuiContainer
{
	private TileEntityEnricher tile;
	private EntityPlayer player;
	private ResourceLocation Tex = new ResourceLocation("labstuff:textures/gui/enricher.png");


	public GuiEnricher(InventoryPlayer inventory, TileEntityEnricher tileEntity)
	{
		super(new ContainerEnricher(inventory, tileEntity));
		this.tile = tileEntity;
		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int par3) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, par3);
	}



	@Override
	public void onGuiClosed()
	{
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int arg0, int arg1)
	{
		this.fontRendererObj.drawString("Stats:", 10, 100, 4210752);
		this.fontRendererObj.drawString("Enriching: " + tile.isEnriching(), 10, 130, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(Tex);
		int x = (this.width - 256) / 2;
		int y = (this.height - 256) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, 256, 256);
	}

}
