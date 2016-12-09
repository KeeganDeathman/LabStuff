package keegan.labstuff.client.gui;

import java.io.File;
import java.io.IOException;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketComputer;
import keegan.labstuff.container.ContainerComputer;
import keegan.labstuff.io.ReadFile;
import keegan.labstuff.io.WriteFile;
import keegan.labstuff.tileentity.TileEntityComputer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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
	
	private File documents;
	private File log;
	
	public GuiComputer(InventoryPlayer inv, TileEntityComputer tileEntity, EntityPlayer player)
	{
		super(new ContainerComputer(inv, tileEntity));
		tile = tileEntity;
		world = player.worldObj;
		player = inv.player;
		documents = new File(LabStuffMain.filesDir, player.getDisplayName().getUnformattedText());
		{documents.mkdirs();}
		log = new File(documents, "computer.json");
		this.cmd = "";
		this.xSize = 256;
		this.ySize = 256;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.consoleInput = new GuiTextField(0, this.fontRendererObj, 110, 190, 80, 10);
		this.consoleInput.setText(this.cmd);
		readConsoleFile();
	}
	
	public void readConsoleFile()
	{
		ReadFile rf = new ReadFile();
		try {
			String[] lines = rf.readLines(log);
			if(lines.length == 6)
			{
				ConsoleLogLine1=lines[1];
				ConsoleLogLine2=lines[2];
				ConsoleLogLine3=lines[3];
				ConsoleLogLine4=lines[4];
				ConsoleLogLine5=lines[5];
			}
			else if(lines.length == 5)
			{
				ConsoleLogLine2=lines[1];
				ConsoleLogLine3=lines[2];
				ConsoleLogLine4=lines[3];
				ConsoleLogLine5=lines[4];
			}
			else if(lines.length == 4)
			{
				ConsoleLogLine3=lines[1];
				ConsoleLogLine4=lines[2];
				ConsoleLogLine5=lines[3];
			}
			else if(lines.length == 3)
			{
				ConsoleLogLine4=lines[1];
				ConsoleLogLine5=lines[2];
			}
			else if(lines.length == 2)
			{
				ConsoleLogLine5=lines[1];
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeConsoleToFile()
	{
		WriteFile wf = new WriteFile();
		wf.write(log, "--KDDOS--", false);
		if(ConsoleLogLine1 != null)
			wf.write(log, ConsoleLogLine1, true);
		if(ConsoleLogLine2 != null)
			wf.write(log, ConsoleLogLine2, true);
		if(ConsoleLogLine3 != null)
			wf.write(log, ConsoleLogLine3, true);	
		if(ConsoleLogLine4 != null)
			wf.write(log, ConsoleLogLine4, true);
		if(ConsoleLogLine5 != null)
			wf.write(log, ConsoleLogLine5, true);
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
    protected void mouseClicked(int par1, int par2, int par3) throws IOException
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
        }
    	
    	//Closes Screen
    	if(par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.getKeyCode() && !this.consoleInput.isFocused())
    	{	
    		this.writeConsoleToFile();
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
		this.mc.fontRendererObj.drawString(ConsoleLogLine1, 30, 40, 4210752);
		this.mc.fontRendererObj.drawString(ConsoleLogLine2, 30, 50, 4210752);
		this.mc.fontRendererObj.drawString(ConsoleLogLine3, 30, 60, 4210752);
		this.mc.fontRendererObj.drawString(ConsoleLogLine4, 30, 70, 4210752);
		this.mc.fontRendererObj.drawString(ConsoleLogLine5, 30, 80, 4210752);
	}
    
    @Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
		if(world.isRemote)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/Computer.png"));
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		}
	}

}
