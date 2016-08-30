package keegan.labstuff.client.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketMatterCollector;
import keegan.labstuff.container.ContainerMatterCollector;
import keegan.labstuff.tileentity.TileEntityMatterCollector;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiMatterCollector extends GuiContainer
{
	private TileEntityMatterCollector tile;
	private EntityPlayer player;
	private ResourceLocation Tex = new ResourceLocation("labstuff:textures/gui/mattercollector.png");

	private GuiButton chuckDirection;

	public GuiMatterCollector(TileEntityMatterCollector tile, InventoryPlayer inv)
	{
		super(new ContainerMatterCollector(inv, tile));
		this.tile = tile;
		this.xSize = 256;
		this.ySize = 256;
	}

	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.add(chuckDirection = new GuiButton(0, xSize + 40 + 2, ySize + 2, 100, 20, tile.getDirAsButton()));
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int par3)
	{
		super.mouseClicked(mouseX, mouseY, par3);
	}

	@Override
	protected void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
		// Checks to see if were dealing with the console
		

		// Closes Screen
		if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.getKeyCode())
		{
			this.mc.thePlayer.closeScreen();
		}

	}
	
	@Override
	protected void actionPerformed(GuiButton par1)
	{
		if(par1.id == 0)
		{
			if(par1.displayString == "Store")
			{
				chuckDirection.displayString = "West";
				LabStuffMain.packetPipeline.sendToServer(new PacketMatterCollector(tile.xCoord, tile.yCoord, tile.zCoord, ForgeDirection.WEST));
			}
			else if(par1.displayString == "West")
			{
				chuckDirection.displayString = "North";
				LabStuffMain.packetPipeline.sendToServer(new PacketMatterCollector(tile.xCoord, tile.yCoord, tile.zCoord, ForgeDirection.NORTH));
			}
			else if(par1.displayString == "North")
			{
				chuckDirection.displayString = "East";
				LabStuffMain.packetPipeline.sendToServer(new PacketMatterCollector(tile.xCoord, tile.yCoord, tile.zCoord, ForgeDirection.EAST));
			}
			else if(par1.displayString == "East")
			{
				chuckDirection.displayString = "South";
				LabStuffMain.packetPipeline.sendToServer(new PacketMatterCollector(tile.xCoord, tile.yCoord, tile.zCoord, ForgeDirection.SOUTH));
			}
			else if(par1.displayString == "South")
			{
				chuckDirection.displayString = "Store";
				LabStuffMain.packetPipeline.sendToServer(new PacketMatterCollector(tile.xCoord, tile.yCoord, tile.zCoord, ForgeDirection.UNKNOWN));
			}
		}
	}

	@Override
	public void updateScreen()
	{
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
	protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(Tex);
		int x = (this.width - 256) / 2;
		int y = (this.height - 256) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, 256, 256);
	}

}
