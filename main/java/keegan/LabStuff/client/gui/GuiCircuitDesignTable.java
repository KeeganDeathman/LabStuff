package keegan.labstuff.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketCircuitDesignTable;
import keegan.labstuff.container.ContainerCircuitDesignTable;
import keegan.labstuff.recipes.*;
import keegan.labstuff.tileentity.TileEntityCircuitDesignTable;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class GuiCircuitDesignTable extends GuiContainer
{

	private TileEntityCircuitDesignTable tile;

	private GuiButton draw;
	private GuiButton left;
	private GuiButton right;
	private int circuitNumber = 0;
	private ArrayList<CircuitDesign> designs;
	private String circuitDesign = "";
	private EntityPlayer player;
	private World worldObj;


	
	public GuiCircuitDesignTable(InventoryPlayer inv, TileEntityCircuitDesignTable tileEntity) 
	{
		super(new ContainerCircuitDesignTable(inv, tileEntity));
		tile = tileEntity;
		player = inv.player;
		worldObj = player.worldObj;
		
		this.xSize = 256;
		this.ySize = 256;
	}

	
	@Override
	public void initGui()
	{
		 super.initGui();
		 this.buttonList.clear();
		 this.buttonList.add(this.draw = new GuiButton(0, xSize + 40 + 2, ySize - 33 + 2, 100, 20, "Draw"));
		 this.buttonList.add(this.left = new GuiButton(1, xSize + 2, ySize - 58 + 2, 20, 20, "<"));
		 this.buttonList.add(this.right = new GuiButton(2, xSize + 115 + 2, ySize - 58 + 2, 20, 20, ">"));
		 designs = Recipes.getCircuitDeisgns();
		 circuitDesign = designs.get(0).getName();
	}
	
	@Override 
	protected void actionPerformed(GuiButton par1GuiButton)
	 { 
	    	if (par1GuiButton.id == 2)
	        {
	    		circuitNumber += 1;
	    		if(circuitNumber > (designs.size() - 1))
	    			circuitNumber = 0;
	    		circuitDesign = designs.get(circuitNumber).getName();
	        }
	    	else if(par1GuiButton.id == 1)
	    	{
	    		circuitNumber -= 1;
	    		if(circuitNumber < 0)
	    			circuitNumber = (designs.size() - 1);
	    		circuitDesign = designs.get(circuitNumber).getName();
	    	}
	    	else if(par1GuiButton.id == 0)
	    	{
	    		if(tile.getStackInSlot(0) != null && tile.getStackInSlot(0).getItem() == Items.PAPER)
	    		{
	    			LabStuffMain.packetPipeline.sendToServer(new PacketCircuitDesignTable(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), circuitDesign));
	    		}
	    	}
	    	else
	    	{
	    		
	    	}
	            
	 }
	
	
	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		this.fontRendererObj.drawString(new TextComponentTranslation("container.inventory").getFormattedText(), 8, ySize - 255 + 2, 4210752);
		this.fontRendererObj.drawString(circuitDesign, xSize - 105 + 2, ySize - 100 + 2, 4210752);
	}



	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		if(worldObj.isRemote)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/CircuitDesignTable.png"));
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		}
	}
	

}
