package keegan.labstuff.client.gui;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.container.ContainerVent;
import keegan.labstuff.tileentity.TileEntityVent;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiVent extends GuiContainer 
{
	private TileEntityVent tile;
	private World worldObj;
	
	
	public GuiVent(InventoryPlayer inventory, TileEntityVent tileEntity) 
	{
		super(new ContainerVent(inventory, tileEntity));
		tile = tileEntity;
		this.xSize = 256;
		this.ySize = 256;
		worldObj = inventory.player.worldObj;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) 
	{
		if(worldObj.isRemote)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/vent.png"));
			this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);	
		}
	}

}
