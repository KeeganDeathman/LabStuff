package keegan.labstuff.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import keegan.labstuff.models.ModelPlasmaPipe;
import keegan.labstuff.tileentity.TileEntityLiquidPipe;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraftforge.fluids.*;

public class TileEntityRenderLiquidPipe extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
	
	public static Minecraft mc = Minecraft.getMinecraft();
	public static ModelPlasmaPipe model = new ModelPlasmaPipe();
	public static ResourceLocation tex = new ResourceLocation("labstuff:textures/models/LiquidPipe.png");
	
	
	private boolean up = false;
	private boolean down = false;
	private boolean north = false;
	private boolean east = false;
	private boolean south = false;
	private boolean west = false;
	private RenderBlocks renderer;
	
	public void renderPipe(TileEntityLiquidPipe entity, double x, double y, double z, float tick)
	{
		 int i = entity.blockMetadata;


		 	
			// Binds the texture
		 	GL11.glPushMatrix();
		 	GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		 	mc.renderEngine.bindTexture(tex);
	        GL11.glTranslatef((float) x, (float) y, (float) z);
	        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
	        // Use this or else model renders upside-down.
	        GL11.glRotatef(180, 180F, 0F, 1F);
	        short rotate = 0;
	        if (i == 0)
	            rotate = 0;

	        if (i == 1)
	            rotate = 90;

	        if (i == 2)
	            rotate = 180;

	        if (i == 3)
	            rotate = -90;
	        GL11.glRotatef(rotate, 0F, 1F, 0F);
	        
	        this.configureSides(entity);
	        
	        this.model.renderCable(0.0625F, north, east, south, west, up, down);

	        GL11.glDisable(GL11.GL_BLEND);
	        
	        GL11.glPopMatrix();
	        
	}
	
	public static void colorFluid(Fluid fluid)
	{
	    int color = fluid.getColor();
	    
	    float cR = (color >> 16 & 0xFF) / 255.0F;
	    float cG = (color >> 8 & 0xFF) / 255.0F;
	    float cB = (color & 0xFF) / 255.0F;
	    
	    GL11.glColor3f(cR, cG, cB);
	}
		
	@Override
	public void func_147496_a(World world)
	{
		this.renderer = new RenderBlocks(world);
		System.out.println("Renderer set.");
	}
	
	
	 public void configureSides(TileEntityLiquidPipe tile)
	 {
		 int x = tile.xCoord;
	     int y = tile.yCoord;
	     int z = tile.zCoord;
	     this.up = this.configSide(tile.getWorldObj(), x, y + 1, z);
	     this.down = this.configSide(tile.getWorldObj(), x, y - 1, z);
	     this.east = this.configSide(tile.getWorldObj(), x + 1, y, z);
	     this.west = this.configSide(tile.getWorldObj(), x - 1, y, z);
	     this.south = this.configSide(tile.getWorldObj(), x, y, z + 1);
	     this.north = this.configSide(tile.getWorldObj(), x, y, z - 1);
	 }
	 
	 public boolean configSide(World world, int x, int y, int z)
	 {
		 if(world.getTileEntity(x, y, z) instanceof IFluidHandler)
			 return true;
		 return false;
	 }
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) 
	{
		this.renderPipe((TileEntityLiquidPipe) tileEntity, d, d1, d2, f);
	}
	
	@Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        float scale = 0.875F;

        GL11.glPushMatrix();
        // Binds the texture
        mc.renderEngine.bindTexture(tex);

        
        GL11.glRotatef(180, 0F, 1F, 0F);
        GL11.glTranslatef(0F, 0.5F, 0F);
        GL11.glScalef(scale, scale, scale);
        this.model.render(null, 0F, 0F, 0F, 0.0F, 0.0F, 0.0625F);

        GL11.glPopMatrix();
    }
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		return true;
	}
	@Override
	public int getRenderId() {
		// TODO Auto-generated method stub
		return this.getRenderId();
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		// TODO Auto-generated method stub
		return true;
	}

}
