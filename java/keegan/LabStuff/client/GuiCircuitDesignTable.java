package LabStuff.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import LabStuff.LabStuffMain;
import LabStuff.container.ContainerCircuitDesignTable;
import LabStuff.tileentity.TileEntityCircuitDesignTable;

public class GuiCircuitDesignTable extends GuiContainer 
{

	private TileEntityCircuitDesignTable tile;

	private GuiButton draw;
	private GuiButton left;
	private GuiButton right;
	private String circuitDesign = "";
	private EntityPlayer player;


	
	public GuiCircuitDesignTable(InventoryPlayer inv, TileEntityCircuitDesignTable tileEntity) 
	{
		super(new ContainerCircuitDesignTable(inv, tileEntity));
		tile = tileEntity;
		player = inv.player;
		
		this.ySize = 256;
		this.xSize = 256;
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
	
	 protected void actionPerformed(GuiButton par1GuiButton)
	 {
	     if (par1GuiButton.enabled)
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
	    		if(tile.getStackInSlot(0).getItem() == Item.paper)
	    		{
	    			if(circuitDesign == "Basic")
	    			{
	    				tile.setInventorySlotContents(0, new ItemStack(LabStuffMain.itemBasicCircuitDesign));
	    				ByteArrayOutputStream bos = new ByteArrayOutputStream(this.circuitDesign.length() * 2 + 12);
	    	            DataOutputStream outputStream = new DataOutputStream(bos);
	    	            try
	    	            {
	    	                outputStream.writeUTF(this.circuitDesign);
	    	                outputStream.writeInt(this.tile.xCoord);
	    	                outputStream.writeInt(this.tile.yCoord);
	    	                outputStream.writeInt(this.tile.zCoord);
	    	                System.out.println("Design sent");
	    	                System.out.println(circuitDesign);
	    	            } catch (Exception ex)
	    	            {
	    	                ex.printStackTrace();
	    	            }

	    	            Packet250CustomPayload packet = new Packet250CustomPayload();
	    	            packet.channel = "LabStuff";
	    	            packet.data = bos.toByteArray();
	    	            packet.length = bos.size();

						((EntityClientPlayerMP)player).sendQueue.addToSendQueue(packet);
	    			}
	    		}
	    	}
	    	else
	    	{
	    		this.mc.displayGuiScreen((GuiScreen)null);
	    	}
	            
	    }
	 }
	
	
	@Override
	public void drawGuiContainerForegroundLayer(int param1, int param2)
	{
		fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 255 + 2, 4210752);
		fontRenderer.drawString(StatCollector.translateToLocal(circuitDesign), xSize - 105 + 2, ySize - 100 + 2, 4210752);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		//draw your Gui here, only thing you need to change is the path
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/container/CircuitDesignTable.png"));
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
	}

}
