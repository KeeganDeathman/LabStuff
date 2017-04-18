package keegan.labstuff.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.*;
import keegan.labstuff.container.*;
import keegan.labstuff.recipes.Recipes;
import keegan.labstuff.tileentity.*;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;

public class GuiKeyPunchRead extends GuiContainer
{
	private KeyPunch tile;
	private EntityPlayer player;
	private ResourceLocation Tex = new ResourceLocation("labstuff:textures/gui/keypunch.png");
	


	public GuiKeyPunchRead(InventoryPlayer inventory, KeyPunch tileEntity)
	{
		super(new ContainerKeyPunch(inventory, tileEntity));
		this.tile = tileEntity;
		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		
		int x = (this.width - 256) / 2;
		int y = (this.height - 256) / 2;
		
		this.buttonList.add(new GuiButton(0, x+5, y+245, 70, 20, "Manual"));
		this.buttonList.add(new GuiButton(1, x+230, y+245, 40, 20, "Presets"));
		
	}

	@Override
    public void updateScreen()
    {
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int par3) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, par3);
	}

	@Override
	public void actionPerformed(GuiButton btn)
	{
		if(btn.id == 0)
		{
			LabStuffMain.packetPipeline.sendToServer(new GuiChangePacket(tile.getPos(), 30));
		}
		else if(btn.id == 1)
		{
			LabStuffMain.packetPipeline.sendToServer(new GuiChangePacket(tile.getPos(), 24));
		}
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
		this.fontRendererObj.drawSplitString(tile.readCard(), 15, 120, 150, 0xFEFEFE);
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
