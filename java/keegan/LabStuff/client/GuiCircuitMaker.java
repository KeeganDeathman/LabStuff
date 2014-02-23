package keegan.labstuff.client;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketCircuitDesignTable;
import keegan.labstuff.PacketHandling.PacketCircuitMaker;
import keegan.labstuff.container.ContainerCircuitMaker;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiCircuitMaker extends GuiContainer
{
	
	private TileEntityCircuitMaker tile;
	private EntityPlayer player;
	private Minecraft mc = Minecraft.getMinecraft();
	
	private GuiButton drill;
	private GuiButton etch;
	
	public GuiCircuitMaker(InventoryPlayer inv, TileEntityCircuitMaker tileEntity) 
	{
		super(new ContainerCircuitMaker(inv, tileEntity));
		tile = tileEntity;
		player = inv.player;
		
		this.xSize = 256;
		this.ySize = 256;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonList.clear();
		this.buttonList.add(this.drill = new GuiButton(0, xSize - 228 + 2, ySize - 149 + 2, 100, 20, "Drill"));
		this.buttonList.add(this.etch = new GuiButton(1, xSize - 190 + 2, ySize - 98 + 2, 100, 20, "Etch"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/CircuitMaker.png"));
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			if(tile.getStackInSlot(0).getItem() == LabStuffMain.itemBasicCircuitDesign)
			{
				if(tile.getStackInSlot(1).getItem() == LabStuffMain.itemCircuitBoardPlate || tile.getStackInSlot(1).getItem() == LabStuffMain.itemBasicEtchedCircuitBoard)
				{
					drill();
				}
			}
			else
			{
				System.out.println("I got nothing");
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		
	}
	
	private void drill()
	{
		for(int progress = 4; progress < 18; progress++)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/CircuitMakerDrill.png"));
			int x = 76;
			int y = 145;
			this.drawTexturedModalRect(x, y, 46, 128, progress, 17);
		}
		LabStuffMain.packetPipeline.sendToServer(new PacketCircuitMaker(tile.xCoord, tile.yCoord, tile.zCoord, "Drill"));
	}
	
}
