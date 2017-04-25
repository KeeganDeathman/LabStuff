package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.blocks.BlockTurbine;
import keegan.labstuff.models.ModelTurbineRotor;
import keegan.labstuff.tileentity.TileEntityTurbine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTurbineRotor extends TileEntitySpecialRenderer{

	public static Minecraft mc = Minecraft.getMinecraft();
	public static ModelTurbineRotor model = new ModelTurbineRotor();
	public static ResourceLocation tex = new ResourceLocation("labstuff:textures/models/turbinerotor.png");
	
	public void renderPipe(TileEntityTurbine entity, double x, double y, double z, float tick)
	{

		 
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
	        
	        
	        if(entity.getWorld().getBlockState(entity.getPos()).getValue(BlockTurbine.TURBINES) == 2)
	        	this.model.render(.0625f, true, true);
	        else if(entity.getWorld().getBlockState(entity.getPos()).getValue(BlockTurbine.TURBINES) == 1)
	        	this.model.render(.0625f, true, false);
	        else
	        this.model.render(.0625f, false, false);

	        GL11.glDisable(GL11.GL_BLEND);
	        
	        GL11.glPopMatrix();
	}
	
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f, int i) 
	{
		this.renderPipe((TileEntityTurbine) tileEntity, d, d1, d2, f);
	}

}
