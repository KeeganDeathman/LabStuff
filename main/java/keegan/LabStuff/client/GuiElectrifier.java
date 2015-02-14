package keegan.labstuff.client;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketElectrifier;
import keegan.labstuff.container.ContainerElectrifier;
import keegan.labstuff.slot.SlotElectrifier;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

public class GuiElectrifier extends GuiContainer
{

	private TileEntityElectrifier tile;

	
	private EntityPlayer player;	
	private World world;
	private static ContainerElectrifier electrocontainer;
	
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
	public void initGui()
	{
		 super.initGui();
	}
	
	@Override
	public void mouseClicked(int x, int y, int arg3)
	{
		super.mouseClicked(x, y, arg3);
		LabStuffMain.packetPipeline.sendToServer(new PacketElectrifier(tile.xCoord, tile.yCoord, tile.zCoord, false));
		if (tile.getStackInSlot(0) != null && tile.getStackInSlot(1) != null && tile.getStackInSlot(2) != null && tile.getStackInSlot(3) != null) {
			if (tile.getStackInSlot(0).getItem() == Items.water_bucket
					&& tile.getStackInSlot(1).getItem() == LabStuffMain.itemBattery
					&& tile.getStackInSlot(2).getItem() == LabStuffMain.itemTestTube
					&& tile.getStackInSlot(3).getItem() == LabStuffMain.itemTestTube
					&& !electrifing) {
				electrifing = true;
			}
		}
		System.out.println("ZAP?");
	}
	
	
	
	
	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 255 + 2, 4210752);
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
