package keegan.labstuff.client.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.*;
import keegan.labstuff.container.ContainerKeyPunch;
import keegan.labstuff.recipes.Recipes;
import keegan.labstuff.tileentity.KeyPunch;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;

public class GuiKeyPunch extends GuiContainer
{
	private KeyPunch tile;
	private EntityPlayer player;
	private ResourceLocation Tex = new ResourceLocation("labstuff:textures/gui/keypunch.png");


	public GuiKeyPunch(InventoryPlayer inventory, KeyPunch tileEntity)
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
		
		this.buttonList.add(new GuiButton(0, x+5, y+140, 20, 20, "<"));
		this.buttonList.add(new GuiButton(1, x+230, y+140, 20,20, ">"));
		this.buttonList.add(new GuiButton(2, x+40, y+150, 50,20, "Write"));
		this.buttonList.add(new GuiButton(3, x+5, y+245, 70, 20, "Manual"));
		this.buttonList.add(new GuiButton(4, x+230, y+245, 50, 20, "Read"));
		
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int par3) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, par3);
	}

	private String program = Recipes.punchProgramsKeys.get(0);
	
	@Override
	public void actionPerformed(GuiButton btn)
	{
		
		if(btn.id == 0)
		{
			if(Recipes.punchProgramsKeys.indexOf(program)-1 == -1)
				program = Recipes.punchProgramsKeys.get(Recipes.punchProgramsKeys.size()-1);
			else
				program = Recipes.punchProgramsKeys.get(Recipes.punchProgramsKeys.indexOf(program)-1);
		}
		if(btn.id == 1)
		{
			if(Recipes.punchProgramsKeys.indexOf(program)+1 == Recipes.punchProgramsKeys.size())
				program = Recipes.punchProgramsKeys.get(0);
			else
				program = Recipes.punchProgramsKeys.get(Recipes.punchProgramsKeys.indexOf(program)+1);
		}
		else if(btn.id == 2)
			LabStuffMain.packetPipeline.sendToServer(new KeyPunchPacket(tile.getPos(), Recipes.punchPrograms.get(program), LabStuffUtils.getDimensionID(tile.getWorld())));
		else if(btn.id == 3)
			LabStuffMain.packetPipeline.sendToServer(new GuiChangePacket(tile.getPos(), 29, LabStuffUtils.getDimensionID(tile.getWorld())));
		else if(btn.id == 4)
			LabStuffMain.packetPipeline.sendToServer(new GuiChangePacket(tile.getPos(), 30, LabStuffUtils.getDimensionID(tile.getWorld())));
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
		
		this.drawString(fontRendererObj, program, 20, 140, 0xFEFEFE);
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
