package keegan.labstuff.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import keegan.labstuff.container.ContainerSqueezer;
import keegan.labstuff.render.LabStuffRenderer;
import keegan.labstuff.render.LabStuffRenderer.FluidType;
import keegan.labstuff.tileentity.Squeezer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class GuiSqueezer extends GuiContainer
{
	private Squeezer tile;
	private EntityPlayer player;
	private ResourceLocation Tex = new ResourceLocation("labstuff:textures/gui/squeezer.png");


	public GuiSqueezer(InventoryPlayer inventory, Squeezer tileEntity)
	{
		super(new ContainerSqueezer(inventory, tileEntity));
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
		this.fontRendererObj.drawString("Energy: " + tile.getEnergy(), 10, 110, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(Tex);
		int x = (this.width - 256) / 2;
		int y = (this.height - 256) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, 256, 256);
		if(tile.getTankInfo(null)[0].fluid != null)
		{
			displayGauge(213, 134, tile.getScaledFluidLevel(100), tile.getTankInfo(null)[0].fluid);
		}
	}
	
	public void displayGauge(int xPos, int yPos, int scale, FluidStack fluid /*0-left, 1-right*/)
	{
		if(fluid == null)
		{
			return;
		}

		int guiWidth = (width - xSize) / 2;
		int guiHeight = (height - ySize) / 2;

		int start = 0;

		while(true)
		{
			int renderRemaining = 0;

			if(scale > 16)
			{
				renderRemaining = 16;
				scale -= 16;
			}
			else {
				renderRemaining = scale;
				scale = 0;
			}

			mc.renderEngine.bindTexture(LabStuffRenderer.getBlocksTexture());
			drawTexturedModalRect(guiWidth + xPos, guiHeight + yPos + 100 - renderRemaining - start, LabStuffRenderer.getFluidTexture(fluid.getFluid(), FluidType.STILL), 16, 16 - (16 - renderRemaining));
			start+=16;

			if(renderRemaining == 0 || scale == 0)
			{
				break;
			}
		}

	}

}
