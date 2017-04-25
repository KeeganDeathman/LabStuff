package keegan.labstuff.client.gui;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketACP;
import keegan.labstuff.container.ContainerACP;
import keegan.labstuff.tileentity.TileEntityAcceleratorControlPanel;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;

public class GuiACP extends GuiContainer
{

	private ResourceLocation Tex;
	private TileEntityAcceleratorControlPanel tile;
	
	public GuiACP(TileEntityAcceleratorControlPanel te)
	{
		super(new ContainerACP());
		this.xSize = 256;
		this.ySize = 256;
		Tex = new ResourceLocation("labstuff:textures/gui/acp.png");
		tile = te;
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int par3)
	{
		

		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		int texX = mouseX - x;
		int texY = mouseY - y;
		System.out.println(x + "," + y);
		System.out.println("Mouse: " + mouseX + "," + mouseY);
		System.out.println("Texture: " + texX + "," + texY);
		if(mouseX >= x && mouseX <= x+xSize && mouseY >= y && mouseY <= y+ySize)
		{
			//Start button
			if(texX > 216 && texX < 245 && texY > 55 && texY < 87)
			{
				LabStuffMain.packetPipeline.sendToServer(new PacketACP(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), "launch", LabStuffUtils.getDimensionID(tile.getWorld())));
			}
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(Tex);
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
}
