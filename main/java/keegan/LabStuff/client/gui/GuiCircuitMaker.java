package keegan.labstuff.client.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketCircuitMaker;
import keegan.labstuff.container.ContainerCircuitMaker;
import keegan.labstuff.recipes.*;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiCircuitMaker extends GuiContainer
{
	
	private TileEntityCircuitMaker tile;
	private EntityPlayer player;
	private World worldObj;
	
	private GuiButton drill;
	private GuiButton etch;
	
	public GuiCircuitMaker(InventoryPlayer inv, TileEntityCircuitMaker tileEntity) 
	{
		super(new ContainerCircuitMaker(inv, tileEntity));
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
		this.buttonList.add(this.drill = new GuiButton(0, 62, 180, 100, 20, "Drill"));
		this.buttonList.add(this.etch = new GuiButton(1, 96, 219, 100, 20, "Etch"));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) 
	{
		if(worldObj.isRemote)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/CircuitMaker.png"));
			int x = (this.width - xSize) / 2;
			int y = (this.height - ySize) / 2;
			this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			if(tile.getStackInSlot(0) != null && tile.getStackInSlot(1) != null);
			{
				ArrayList<CircuitCreation> recipes = Recipes.getCircuitCreations();
				for(int i = 0; i < recipes.size(); i++)
				{
					if(tile.getStackInSlot(0) != null && tile.getStackInSlot(0).getItem().getUnlocalizedName().contains(recipes.get(i).getDesignName()))
					{
						if(tile.getStackInSlot(1) != null &&(tile.getStackInSlot(1).getItem() == LabStuffMain.itemCircuitBoardPlate || tile.getStackInSlot(1).getItem().equals(recipes.get(i).getEtched())))
						{
							drill();
						}
					}
				}
			}
		}
		else if(button.id == 1)
		{
			if(tile.getStackInSlot(0) != null && tile.getStackInSlot(3) != null)
			{
				ArrayList<CircuitCreation> recipes = Recipes.getCircuitCreations();
				for(int i = 0; i < recipes.size(); i++)
				{
					if(tile.getStackInSlot(0) != null && tile.getStackInSlot(0).getItem().getUnlocalizedName().contains(recipes.get(i).getDesignName()))
					{
						if(tile.getStackInSlot(3) != null && (tile.getStackInSlot(3).getItem() == LabStuffMain.itemCircuitBoardPlate || tile.getStackInSlot(3).getItem().equals(recipes.get(i).getDrilled())))
						{
							etch();
						}
					}
				}
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int var1, int var2)
	{
		
	}
	
	private void drill()
	{
		/*for(int progress = 4; progress < 18; progress++)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/CircuitMakerDrill.png"));
			int x = 76;
			int y = 145;
			this.drawTexturedModalRect(x, y, 46, 128, progress, 17);
		}*/
		System.out.println("Sending packet now.");
		LabStuffMain.packetPipeline.sendToServer(new PacketCircuitMaker(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), "Drill"));
	}
	
	private void etch()
	{
		/*for(int progress = 4; progress < 18; progress++)
		{
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/gui/CircuitMakerDrill.png"));
			int x = 76;
			int y = 145;
			this.drawTexturedModalRect(x, y, 46, 128, progress, 17);
		}*/
		System.out.println("Sending packet now.");
		LabStuffMain.packetPipeline.sendToServer(new PacketCircuitMaker(tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ(), "Etch"));
	}
}
