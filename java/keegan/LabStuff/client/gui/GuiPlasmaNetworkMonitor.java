package keegan.labstuff.client.gui;

import keegan.labstuff.container.ContainerPlasmaNetworkMonitor;
import keegan.labstuff.tileentity.TileEntityPlasmaNetworkMonitor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class GuiPlasmaNetworkMonitor extends GuiContainer
{
	TileEntityPlasmaNetworkMonitor tile;
	EntityPlayer player;
	World world;
	
	
	public GuiPlasmaNetworkMonitor(InventoryPlayer inv, TileEntityPlasmaNetworkMonitor tileEntity)
	{
		super(new ContainerPlasmaNetworkMonitor(inv, tileEntity));
		tile = tileEntity;
		player = inv.player;
		world = tile.getWorldObj();
		this.xSize = 256;
		this.ySize = 256;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int arg0, int arg1)
	{
		this.fontRendererObj.drawString("Plasma in pipes: " + tile.getPlasma(), 8, ySize - 255 + 2, 4210752);

	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/plasmanetworkmonitor.png"));
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
	
	
}
