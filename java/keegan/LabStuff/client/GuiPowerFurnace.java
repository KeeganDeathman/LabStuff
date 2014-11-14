package keegan.labstuff.client;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.container.ContainerPowerFurnace;
import keegan.labstuff.tileentity.TileEntityGasChamberPort;
import keegan.labstuff.tileentity.TileEntityPowerFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiPowerFurnace extends GuiContainer
{

	private EntityPlayer player;
	private World worldObj;
	private TileEntityPowerFurnace tile;
	
	
	public GuiPowerFurnace(InventoryPlayer inv, TileEntityPowerFurnace tileEntity) 
	{
		super(new ContainerPowerFurnace(inv, tileEntity));
		player = inv.player;
		worldObj = player.worldObj;
		tile = tileEntity;
		this.xSize = 256;
		this.ySize = 256;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int arg0, int arg1)
	{
		this.fontRendererObj.drawString("Stats:", 10, 100, 4210752);
		this.fontRendererObj.drawString("Burn time: " + tile.getBurnTime(), 10, 130, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float arg0, int arg1, int arg2) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/powerFurnace.png"));
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);	
	}

}
