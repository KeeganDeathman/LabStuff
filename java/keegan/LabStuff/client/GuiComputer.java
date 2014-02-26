package keegan.labstuff.client;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketComputer;
import keegan.labstuff.blocks.BlockComputer;
import keegan.labstuff.container.ContainerComputer;
import keegan.labstuff.tileentity.TileEntityComputer;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiComputer extends GuiContainer
{
	
	private String ConsoleLogLine1;
	private String ConsoleLogLine2;
	private String ConsoleLogLine3;
	private String ConsoleLogLine4;
	private String ConsoleLogLine5;
	private GuiTextField consoleInput;
	private String cmd;
	
	private EntityPlayer player;
	private TileEntityComputer tile;
	private World world;
	
	public GuiComputer(InventoryPlayer inv, TileEntityComputer tileEntity, EntityPlayer player)
	{
		super(new ContainerComputer(inv, tileEntity));
		tile = tileEntity;
		world = player.worldObj;
		player = inv.player;
		
		
		this.ConsoleLogLine1 = tile.ConsoleLogLine1;
		this.ConsoleLogLine2 = tile.ConsoleLogLine2;
		this.ConsoleLogLine3 = tile.ConsoleLogLine3;
		this.ConsoleLogLine4 = tile.ConsoleLogLine4;
		this.ConsoleLogLine5 = tile.ConsoleLogLine5;
		this.cmd = "";
		this.xSize = 300;
		this.ySize = 200;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.consoleInput = new GuiTextField(this.fontRendererObj, 110, 190, 80, 10);
		this.consoleInput.setText(this.cmd);
	}
	
	@Override
    public void updateScreen()
    {
        this.consoleInput.updateCursorCounter();
    }
    
    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);
        
        this.consoleInput.mouseClicked(par1, par2, par3);
    }
    
    @Override
    protected void keyTyped(char par1, int par2)
    {
    	//Checks to see if were dealing with the console
    	if(this.consoleInput.isFocused())
    	{
    		this.consoleInput.textboxKeyTyped(par1, par2);
    		//Gets cmd
    		this.cmd = this.consoleInput.getText();
    	}
    	if (par2 == 28)
        {
            //Say command
    		if (this.cmd.startsWith("say"))
            {
                this.ConsoleLogLine1 = this.ConsoleLogLine2;
                this.ConsoleLogLine2 = this.ConsoleLogLine3;
                this.ConsoleLogLine3 = this.ConsoleLogLine4;
                this.ConsoleLogLine4 = this.ConsoleLogLine5;
                this.ConsoleLogLine5 = this.cmd.substring(4);
            }
            
            this.consoleInput.writeText("");
        	this.consoleInput.setText("");
        	System.out.println(this.ConsoleLogLine5);
        	LabStuffMain.packetPipeline.sendToServer(new PacketComputer(tile.xCoord, tile.yCoord, tile.zCoord, this.ConsoleLogLine1, this.ConsoleLogLine2, this.ConsoleLogLine3, this.ConsoleLogLine4, this.ConsoleLogLine5));
        }
    	
    	//Closes Screen
    	if(par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() && !this.consoleInput.isFocused())
    	{	
    		this.mc.thePlayer.closeScreen();
    	}
        
    }
    
    
    
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        this.consoleInput.drawTextBox();
    }

	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		this.mc.fontRenderer.drawString(ConsoleLogLine1, 30, 40, 4210752);
		this.mc.fontRenderer.drawString(ConsoleLogLine2, 30, 50, 4210752);
		this.mc.fontRenderer.drawString(ConsoleLogLine3, 30, 60, 4210752);
		this.mc.fontRenderer.drawString(ConsoleLogLine4, 30, 70, 4210752);
		this.mc.fontRenderer.drawString(ConsoleLogLine5, 30, 80, 4210752);
	}
    
    @Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/Computer.png"));
    	int x = (this.width - xSize) / 2;
		int y = (this.height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		
	}

}
