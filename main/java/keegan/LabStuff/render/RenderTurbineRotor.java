package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import keegan.labstuff.models.*;
import keegan.labstuff.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;

public class RenderTurbineRotor extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{

	public static Minecraft mc = Minecraft.getMinecraft();
	public static ModelTurbineRotor model = new ModelTurbineRotor();
	public static ResourceLocation tex = new ResourceLocation("labstuff:textures/models/turbinerotor.png");
	
	public void renderPipe(TileEntityTurbine entity, double x, double y, double z, float tick)
	{
		 int i = entity.blockMetadata;

		 
			// Binds the texture
		 	GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		 	mc.renderEngine.bindTexture(tex);
		 	GL11.glPushMatrix();
		 	GL11.glTranslatef((float) x + 1 , (float) y, (float) z);
	        GL11.glTranslatef(0.5F, 0F, 1.5F);
	        
	        //GL11.glRotatef(rotate, 0F, 1F, 0F);
	        GL11.glTranslatef(-1f, -.5f, -1f);
	        //GL11.glScalef(20f, 20f, 20f);
	        //GL11.glScalef(45f, 45f, 45f);
	        //GL11.glScalef(0f, 0f, 1/3f);
	        GL11.glRotatef((short)entity.angle, 0f, 1f, 0f);	
	        
	        
	        if(entity.getWorldObj().getBlockMetadata((int)entity.xCoord, (int)entity.yCoord, (int)entity.zCoord) == 2)
	        	this.model.render(.0625f, true, true);
	        else if(entity.getWorldObj().getBlockMetadata((int)entity.xCoord, (int)entity.yCoord, (int)entity.zCoord) == 1)
	        	this.model.render(.0625f, true, false);
	        else
	        this.model.render(.0625f, false, false);

	        GL11.glDisable(GL11.GL_BLEND);
	        
	        GL11.glPopMatrix();
	}
	
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) 
	{
		this.renderPipe((TileEntityTurbine) tileEntity, d, d1, d2, f);
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
