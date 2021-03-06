package keegan.labstuff.client.gui;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.container.ContainerDSCDrive;
import keegan.labstuff.tileentity.DSCDrive;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDSCDrive extends GuiContainer
{

	private ResourceLocation Tex;
	private DSCDrive tile;
	
	public GuiDSCDrive(InventoryPlayer inv, DSCDrive te)
	{
		super(new ContainerDSCDrive(inv,te));
		tile = te;
		Tex = new ResourceLocation("labstuff:textures/gui/dscdrive.png");
		this.xSize = 256;
		this.ySize = this.xSize;
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
