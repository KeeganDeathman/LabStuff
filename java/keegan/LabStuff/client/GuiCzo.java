package keegan.labstuff.client;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.container.ContainerCzo;
import keegan.labstuff.tileentity.TileEntityCzo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCzo extends GuiContainer 
{
	private TileEntityCzo tile;
	
	
	public GuiCzo(InventoryPlayer inventory, TileEntityCzo tileEntity) 
	{
		super(new ContainerCzo(inventory, tileEntity));
		tile = tileEntity;
		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/czo.png"));
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);	
	}

}
