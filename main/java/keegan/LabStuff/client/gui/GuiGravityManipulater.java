package keegan.labstuff.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketGravity;
import keegan.labstuff.container.ContainerGravity;
import keegan.labstuff.tileentity.TileEntityGravityManipulater;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;

public class GuiGravityManipulater extends GuiContainer
{
	private TileEntityGravityManipulater tile;
	private EntityPlayer player;
	private ResourceLocation Tex = new ResourceLocation("labstuff:textures/gui/gravity.png");

	private GuiTextField gravityModifierInput;
	private String gravityModifier;

	public GuiGravityManipulater(TileEntityGravityManipulater tile, InventoryPlayer inv)
	{
		super(new ContainerGravity(inv, tile));
		this.tile = tile;
		gravityModifier = "";
		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.gravityModifierInput = new GuiTextField(this.fontRendererObj, 200, 160, 80, 10);
		this.gravityModifierInput.setText(this.gravityModifier);
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int par3)
	{
		super.mouseClicked(mouseX, mouseY, par3);
		this.gravityModifierInput.mouseClicked(mouseX, mouseY, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		// Checks to see if were dealing with the console
		if (this.gravityModifierInput.isFocused())
		{
			this.gravityModifierInput.textboxKeyTyped(par1, par2);
			// Gets cmd
			this.gravityModifier = this.gravityModifierInput.getText();
		}
		if (par2 == 28)
		{
			this.gravityModifier = this.gravityModifierInput.getText();
			LabStuffMain.packetPipeline.sendToServer(new PacketGravity(tile.xCoord, tile.yCoord, tile.zCoord, Float.parseFloat(gravityModifier)));
			this.gravityModifierInput.setText(gravityModifier = "");
		}

		// Closes Screen
		if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() && !this.gravityModifierInput.isFocused())
		{
			this.mc.thePlayer.closeScreen();
		}

	}

	@Override
	public void updateScreen()
	{
		this.gravityModifierInput.updateCursorCounter();
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
		this.gravityModifierInput.drawTextBox();
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
