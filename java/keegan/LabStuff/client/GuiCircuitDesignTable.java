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
	private int ySize = this.field_146999_f;
	private int xSize = this.field_147000_g;
	private Minecraft mc = Minecraft.getMinecraft();


	
	public GuiCircuitDesignTable(InventoryPlayer inv, TileEntityCircuitDesignTable tileEntity) 
	{
		super(new ContainerCircuitDesignTable(inv, tileEntity));
		tile = tileEntity;
		player = inv.player;
		
		this.field_146999_f = 256;
		this.field_147000_g = 256;
	}

	
	@Override
	public void initGui()
	{
		 super.initGui();
		 this.field_146292_n.clear();
		 this.field_146292_n.add(this.draw = new GuiButton(0, xSize + 55 + 2, ySize - 33 + 2, 100, 20, "Draw"));
		 this.field_146292_n.add(this.left = new GuiButton(1, xSize + 10 + 2, ySize - 58 + 2, 20, 20, "<"));
		 this.field_146292_n.add(this.right = new GuiButton(2, xSize + 125 + 2, ySize - 58 + 2, 20, 20, ">"));
		 circuitDesign = "Basic";
	}
	
	@Override 
	protected void func_146284_a(GuiButton par1GuiButton)
	 { 
	    	if (par1GuiButton.field_146127_k == 2)
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
	    	else if(par1GuiButton.field_146127_k == 1)
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
	    	else if(par1GuiButton.field_146127_k == 0)
	    	{
	    		if(tile.getStackInSlot(0).getItem() == Items.paper)
	    		{
	    			if(circuitDesign == "Basic")
	    			{
	    				LabStuffMain.packetPipeline.sendToServer(new PacketCircuitDesignTable(tile.field_145851_c,tile.field_145848_d,tile.field_145849_e, circuitDesign));
	    			}
	    		}
	    	}
	    	else
	    	{
	    		this.field_146297_k.func_147108_a((GuiScreen)null);
	    	}
	            
	 }
	
	
	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		this.field_146289_q.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 255 + 2, 4210752);
		this.field_146289_q.drawString(StatCollector.translateToLocal(circuitDesign), xSize - 105 + 2, ySize - 100 + 2, 4210752);
	}

	@Override
	protected void func_146976_a(float var1, int var2, int var3) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/container/CircuitDesignTable.png"));
		int x = (this.field_146295_m - xSize) / 2;
		int y = (this.field_146294_l - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
