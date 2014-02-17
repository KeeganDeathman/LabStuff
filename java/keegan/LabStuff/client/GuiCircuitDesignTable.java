package keegan.LabStuff.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import keegan.LabStuff.LabStuffMain;
import keegan.LabStuff.PacketHandling.PacketCircuitDesignTable;
import keegan.LabStuff.PacketHandling.PacketPipeline;
import keegan.LabStuff.container.ContainerCircuitDesignTable;
import keegan.LabStuff.tileentity.TileEntityCircuitDesignTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

public class GuiCircuitDesignTable extends GuiContainer
{

	private TileEntityCircuitDesignTable tile;

	private GuiButton draw;
	private GuiButton left;
	private GuiButton right;
	private String circuitDesign = "";
	private EntityPlayer player;
	private Minecraft mc = Minecraft.getMinecraft();


	
	public GuiCircuitDesignTable(InventoryPlayer inv, TileEntityCircuitDesignTable tileEntity) 
	{
		super(new ContainerCircuitDesignTable(inv, tileEntity));
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
		 this.buttonList.add(this.draw = new GuiButton(0, xSize + 55 + 2, ySize - 33 + 2, 100, 20, "Draw"));
		 this.buttonList.add(this.left = new GuiButton(1, xSize + 10 + 2, ySize - 58 + 2, 20, 20, "<"));
		 this.buttonList.add(this.right = new GuiButton(2, xSize + 125 + 2, ySize - 58 + 2, 20, 20, ">"));
		 circuitDesign = "Basic";
	}
	
	@Override 
	protected void actionPerformed(GuiButton par1GuiButton)
	 { 
	    	if (par1GuiButton.id == 2)
	        {
	    		if(circuitDesign == "Basic")
	    		{
	    			circuitDesign = "Intermidiate";
	    		}
	    		else if(circuitDesign == "Intermidiate")
	    		{
	    			circuitDesign = "Advanced";
	    		}
	    		else if(circuitDesign == "Advanced")
	    		{
	    			circuitDesign = "Basic";
	    		}
	        }
	    	else if(par1GuiButton.id == 1)
	    	{
	    		if(circuitDesign == "Basic")
	    		{
	    			circuitDesign = "Advanced";
	    		}
	    		else if(circuitDesign == "Intermidiate")
	    		{
	    			circuitDesign = "Basic";
	    		}
	    		else if(circuitDesign == "Advanced")
	    		{
	    			circuitDesign = "Intermidiate";
	    		}
	    	}
	    	else if(par1GuiButton.id == 0)
	    	{
	    		if(tile.getStackInSlot(0).getItem() == Items.paper)
	    		{
	    			if(circuitDesign == "Basic")
	    			{
	    				LabStuffMain.packetPipeline.sendToServer(new PacketCircuitDesignTable(tile.xCoord, tile.yCoord, tile.zCoord, circuitDesign));
	    			}
	    		}
	    	}
	    	else
	    	{
	    		
	    	}
	            
	 }
	
	
	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 255 + 2, 4210752);
		this.fontRendererObj.drawString(StatCollector.translateToLocal(circuitDesign), xSize - 105 + 2, ySize - 100 + 2, 4210752);
	}



	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/CircuitDesignTable.png"));
		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	

}
