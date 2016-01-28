package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import keegan.labstuff.models.ModelPlasmaPipe;
import keegan.labstuff.tileentity.TileEntityLiquid;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
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
	
	public void renderPipe(TileEntityLiquid entity, double x, double y, double z, float tick)
	{
		 int i = entity.blockMetadata;

		 	if(entity.tank.getFluid() != null)
		 	{
		 		GL11.glPushMatrix();
			 	GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
				FluidStack fluidStack = entity.tank.getFluid();
				Fluid fluid = fluidStack.getFluid();
	            IIcon fluidIcon = fluid.getStillIcon();

	            GL11.glColor3f(1, 1, 1);
	            mc.renderEngine.bindTexture(mc.renderEngine.getResourceLocation(fluid.getSpriteNumber()));
	            fillAreaWithIcon(fluidIcon, (int)fluidIcon.getMinU(), (int)fluidIcon.getMinV(), (int)fluidIcon.getMaxU(), (int)fluidIcon.getMaxV());
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
	
	
	
	public static void fillAreaWithIcon(IIcon icon, int x, int y, int width, int height) {
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();

        float zLevel = 0;

        int iconWidth = icon.getIconWidth();
        int iconHeight = icon.getIconHeight();

        // number of rows & cols of full size icons
        int fullCols = width / iconWidth;
        int fullRows = height / iconHeight;

        float minU = icon.getMinU();
        float maxU = icon.getMaxU();
        float minV = icon.getMinV();
        float maxV = icon.getMaxV();

        int excessWidth = width % iconWidth;
        int excessHeight = height % iconHeight;

        // interpolated max u/v for the excess row / col
        float partialMaxU = minU + (maxU - minU) * ((float) excessWidth / iconWidth);
        float partialMaxV = minV + (maxV - minV) * ((float) excessHeight / iconHeight);

        int xNow;
        int yNow;
        for (int row = 0; row < fullRows; row++) {
            yNow = y + row * iconHeight;
            for (int col = 0; col < fullCols; col++) {
                // main part, only full icons
                xNow = x + col * iconWidth;
                drawRect(xNow, yNow, iconWidth, iconHeight, zLevel, minU, minV, maxU, maxV);
            }
            if (excessWidth != 0) {
                // last not full width column in every row at the end
                xNow = x + fullCols * iconWidth;
                drawRect(xNow, yNow, iconWidth, iconHeight, zLevel, minU, minV, maxU, maxV);
            }
        }
        if (excessHeight != 0) {
            // last not full height row
            for (int col = 0; col < fullCols; col++) {
                xNow = x + col * iconWidth;
                yNow = y + fullRows * iconHeight;
                drawRect(xNow, yNow, iconWidth, excessHeight, zLevel, minU, minV, maxU, partialMaxV);
            }
            if (excessWidth != 0) {
                // missing quad in the bottom right corner of neither full height nor full width
                xNow = x + fullCols * iconWidth;
                yNow = y + fullRows * iconHeight;
                drawRect(xNow, yNow, excessWidth, excessHeight, zLevel, minU, minV, partialMaxU, partialMaxV);
            }
        }

        t.draw();
    }
	
	private static void drawRect(float x, float y, float width, float height, float z, float u, float v, float maxU, float maxV) {
        Tessellator t = Tessellator.instance;

        t.addVertexWithUV(x, y + height, z, u, maxV);
        t.addVertexWithUV(x + width, y + height, z, maxU, maxV);
        t.addVertexWithUV(x + width, y, z, maxU, v);
        t.addVertexWithUV(x, y, z, u, v);
    }
	
	
	 public void configureSides(TileEntityLiquid tile)
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
		this.renderPipe((TileEntityLiquid) tileEntity, d, d1, d2, f);
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
