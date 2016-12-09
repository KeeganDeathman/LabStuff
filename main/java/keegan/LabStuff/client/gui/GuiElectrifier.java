package keegan.labstuff.client.gui;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.container.ContainerElectrifier;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class GuiElectrifier extends GuiContainer
{

	private TileEntityElectrifier tile;

	
	private EntityPlayer player;	
	private World world;
	private static ContainerElectrifier electrocontainer;
	private GuiButton zap;
	
	public boolean electrifing = false;
	
	

	
	public GuiElectrifier(InventoryPlayer inv, TileEntityElectrifier tileEntity, World world) 
	{
		super(electrocontainer = new ContainerElectrifier(inv, tileEntity));
		tile = tileEntity;
		player = inv.player;
		this.world = world;
		
		this.xSize = 256;
		this.ySize = 256;
	}

	
	
	
	
	
	
	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(new TextComponentTranslation("container.inventory").getFormattedText(), 8, ySize - 255 + 2, 4210752);
		//System.out.println("electrify? " + electrifing);
		if(electrifing)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/ElectrifierProgressBar.png"));
			int x = 123;
			int y = 154;
		}
	}



	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		if(world.isRemote)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/Electrifier.png"));
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		}
	}
	

}
