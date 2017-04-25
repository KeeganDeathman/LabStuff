package keegan.labstuff.client.gui;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.container.ContainerGasChamberPort;
import keegan.labstuff.tileentity.TileEntityGasChamberPort;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiGasChamberPort extends GuiContainer
{
	
	private TileEntityGasChamberPort tile;
	private World world;
	private EntityPlayer player;
	private boolean input = false;
	
	public GuiGasChamberPort(InventoryPlayer inv, TileEntityGasChamberPort tileEntity, World world)
	{
		super(new ContainerGasChamberPort(inv, tileEntity));
		this.world = world;
		player = inv.player;
		tile = tileEntity;
		xSize = 256;
		ySize = xSize;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		input = tile.testTubeSlot();
		if(!input)
		{
			this.fontRendererObj.drawString("Stats:", 10, 50, 4210752);
			this.fontRendererObj.drawString("Hydrogen atoms: " + ((TileEntityGasChamberPort)tile).testtubes*20, 10, 80, 4210752);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) 
	{
		if(world.isRemote)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			if(!input)
			{
				//output screen
				this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/gaschamberportoutput.png"));
			}
			else
			{
				if(tile.getStackInSlot(0) != null)
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/gaschamberportinputfull.png"));
				else
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/gaschamberportinput.png"));
			}
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		}
		
	}
}
