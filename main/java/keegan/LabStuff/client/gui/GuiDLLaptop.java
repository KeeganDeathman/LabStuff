package keegan.labstuff.client.gui;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.*;
import keegan.labstuff.container.ContainerDLLaptop;
import keegan.labstuff.tileentity.TileEntityDLLaptop;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;

public class GuiDLLaptop extends GuiContainer
{
	
	private TileEntityDLLaptop tile;
	private EntityPlayer player;
	private ResourceLocation Tex = new ResourceLocation("labstuff:textures/gui/laptop/desktop.png");
	private boolean startMenu = false;
	
	//USB
	private boolean usbControl = false;
	private String usbScreen;
	private GuiTextField deviceInput;
	private String deviceUSB;
	
	//Internet
	private boolean internet;
	private String website;
	
	private int overlayXSize = 512;
	private int overlayYSize = 512;
	
	public GuiDLLaptop(TileEntityDLLaptop te, InventoryPlayer inv, EntityPlayer player) 
	{
		super(new ContainerDLLaptop(inv, te));
		tile = te;
		this.xSize = 256;
		this.ySize = 256;
		
		this.deviceUSB = "";
		this.website = "hub";
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
			this.deviceInput = new GuiTextField(this.fontRendererObj, 200, 160, 80, 10);
			this.deviceInput.setText(this.deviceUSB);
	}
	
	@Override
    public void updateScreen()
    {
		if(usbScreen == "plugin" || usbScreen == "unplug")
		{
			this.deviceInput.updateCursorCounter();
		}
    }
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int par3)
	{
		

		int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		int texX = mouseX - x;
		int texY = mouseY - y;
		System.out.println(x + "," + y);
		if(mouseX >= x && mouseX <= x+xSize && mouseY >= y && mouseY <= y+ySize)
		{
			//Start button
			if(texX > 2 && texX < 17 && texY > 239 && texY < 1018)
			{
				if(!startMenu)
				{
					startMenu = true;
				}
				else
				{
					startMenu = false;
				}
			}
			//Internet
			if(texX > 3 && texX < 56 && startMenu && texY > 159 && texY < 169)
			{
				internet = true;
				website="hub";
				startMenu = false;
			}
			//USB control
			if(texX > 3 && texX < 56 && startMenu && texY > 189 && texY < 204)
			{
				usbControl = true;
				usbScreen = "main";
				startMenu = false;
			}
			if(usbControl)
			{
				switch(usbScreen)
				{
					case "main":
						if(texX > 75 && texX < 104 && texY > 98 && texY < 118)
						{
							usbScreen = "plugin";
						}
						else if(texX > 114 && texX < 146 && texY > 98 && texY < 118)
						{
							usbScreen = "view";
						}
						else if(texX > 154 && texX < 186 && texY > 98 && texY < 118)
						{
							usbScreen = "unplug";
						}
						break;
					case "plugin":
						this.deviceInput.mouseClicked(mouseX, mouseY, par3);
						if(texX > 159 && texX < 191 && texY > 137 && texY < 150)
						{
							usbScreen = "main";
							LabStuffMain.packetPipeline.sendToServer(new PacketDLLaptopUSB(tile.xCoord, tile.yCoord, tile.zCoord, Integer.parseInt(deviceUSB), ""));
							this.deviceInput.setText("");
						}
						break;
					case "unplug":
						this.deviceInput.mouseClicked(mouseX, mouseY, par3);
						if(texX > 159 && texX < 191 && texY > 137 && texY < 150)
						{
							usbScreen = "main";
							LabStuffMain.packetPipeline.sendToServer(new PacketDLLaptopUSB(tile.xCoord, tile.yCoord, tile.zCoord, 42, deviceUSB));
							this.deviceInput.setText("");
						}
						break;
					case "view":
						if(texX > 159 && texX < 191 && texY > 137 && texY < 150)
						{
							usbScreen = "main";
						}
						break;
				}
				if(texX > 182 && texX < 192 && texY > 66 && texY < 76)
				{
					usbControl = false;
					usbScreen = "none";
				}
			}
			if(internet)
			{
				if(texX > 229 && texX < 249 && texY > 7 && texY < 27)
				{
					internet = false;
				}
				if(website.equals("hub"))
				{
					if(texX > 19 && texX < 101 && texY > 52 && texY < 114)
					{
						website = "downloads";
					}
				}
				if(website.equals("downloads"))
				{
					if(texX > 1 && texX < 25 && texY > 44 && texY < 69)
					{
						website = "hub";
					}
					else if(texX > 113 && texX < 137 && texY > 99 && texY < 143)
					{
						LabStuffMain.packetPipeline.sendToServer(new PacketDLLaptopWeb(tile.xCoord, tile.yCoord, tile.zCoord, "download-dPadOS"));
					}
				}
			}
		}
		System.out.println("Mouse: " + mouseX + "," + mouseY);
		System.out.println("Texture: " + texX + "," + texY);
	}
	

	@Override
    protected void keyTyped(char par1, int par2)
    {
    	//Checks to see if were dealing with the console
    	if(this.deviceInput.isFocused())
    	{
    		this.deviceInput.textboxKeyTyped(par1, par2);
    		//Gets cmd
    		this.deviceUSB = this.deviceInput.getText();
    	}
    	
    	//Closes Screen
    	if(par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() && !this.deviceInput.isFocused())
    	{	
    		this.mc.thePlayer.closeScreen();
    	}
        
    }
	
	 @Override
	    public void drawScreen(int par1, int par2, float par3)
	    {
	        super.drawScreen(par1, par2, par3);
	        if(usbScreen == "plugin" || usbScreen == "unplug")
	        {
	        	this.deviceInput.drawTextBox();
	        }
	    }
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		if(startMenu)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/laptop/startmenu.png"));
	    	if(xSize == 512)
	    	{
	    		GL11.glScalef(overlayXSize/512,overlayYSize/512,0f);
	    		overlayXSize=512;
	    		overlayYSize=512;
	    	}
	    	if(xSize == 256)
	    	{
	    		GL11.glScalef(overlayXSize/256,overlayYSize/256,0f);
	    		overlayXSize=256;
	    		overlayYSize=256;
	    	}
	    	int x = (this.width - overlayXSize) / 2;
			int y = (this.height - overlayYSize) / 2;
			
			this.drawTexturedModalRect(0, 0, 0, 0, overlayXSize, overlayYSize);
		}
		if(usbControl)
		{
			
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			//System.out.println(usbScreen);
			switch(usbScreen)
			{
				case "main":
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/laptop/usbcontrol.png"));
					break;
				case "plugin":	
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/laptop/usbcontrolplugin.png"));
					break;
				case "view":
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/laptop/usbcontrolview.png"));
					break;
				case "unplug":	
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/laptop/usbcontrolunplug.png"));
			}
				
			if(xSize == 512)
		    {
		    	GL11.glScalef(overlayXSize/512,overlayYSize/512,0f);
		   		overlayXSize=512;
		   		overlayYSize=512;
		   	}
		   	if(xSize == 256)
	    	{
	    		GL11.glScalef(overlayXSize/256,overlayYSize/256,0f);
		    	overlayXSize=256;
		    	overlayYSize=256;
		   	}
		   	int x = (this.width - overlayXSize) / 2;
			int y = (this.height - overlayYSize) / 2;
			
			this.drawTexturedModalRect(0, 0, 0, 0, overlayXSize, overlayYSize);
			
			if(usbScreen == "view" && !tile.isTablet())
			{
				this.fontRendererObj.drawString("Connected", xSize - 185 + 2, ySize - 154 + 2, 0xFFFFFF);
			}
		}
		if(internet)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			switch(website)
			{
				case "hub":
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/laptop/internethub.png"));
					break;
				case "downloads":	
					this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/laptop/internetdownloads.png"));
					break;
			}
	    	if(xSize == 512)
	    	{
	    		GL11.glScalef(overlayXSize/512,overlayYSize/512,0f);
	    		overlayXSize=512;
	    		overlayYSize=512;
	    	}
	    	if(xSize == 256)
	    	{
	    		GL11.glScalef(overlayXSize/256,overlayYSize/256,0f);
	    		overlayXSize=256;
	    		overlayYSize=256;
	    	}
	    	int x = (this.width - overlayXSize) / 2;
			int y = (this.height - overlayYSize) / 2;
			
			this.drawTexturedModalRect(0, 0, 0, 0, overlayXSize, overlayYSize);
		}
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	this.mc.renderEngine.bindTexture(Tex);
    	if(width < xSize || height < ySize)
    	{
    		GL11.glScalef(xSize/512, ySize/512, 0f);
    		ySize = 512;
    		xSize = 512;
    		if(width < xSize || height < ySize)
    		{
    			GL11.glScalef(xSize/256, ySize/256, 0f);
    			xSize = 256;
    			ySize = 256;
    		}
    	}
    	int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

}
