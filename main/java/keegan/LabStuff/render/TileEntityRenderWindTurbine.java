package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.blocks.BlockWindTurbine;
import keegan.labstuff.models.ModelWindTurbine;
import keegan.labstuff.tileentity.TileEntityWindTurbine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class TileEntityRenderWindTurbine extends TileEntitySpecialRenderer{
	//This method is called when minecraft renders a tile entity

	public static Minecraft mc = Minecraft.getMinecraft();
	public ModelWindTurbine model = new ModelWindTurbine();
	
	public void renderTurbine(TileEntityWindTurbine entity, double x, double y, double z, float tick)
	{

	        // Binds the texture
		 	mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/models/WindTurbine.png"));
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float) x, (float) y, (float) z);
	        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
	        // Use this or else model renders upside-down.
	        GL11.glRotatef(180, 180F, 0F, 1F);
	        float rotate = 0;
	        
	        rotate = entity.getWorld().getBlockState(entity.getPos()).getValue(BlockWindTurbine.FACING).getHorizontalAngle();
	        GL11.glRotatef(rotate, 0F, 1F, 0F);
	        
	        
	        if(entity.isProperPosition())
	        {
	        	model.spin();
	        }
	        this.model.render(null, (float) x, (float) y, (float) z, 0.0F, 0.0F, 0.0625F);
	        GL11.glPopMatrix();
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f, int i) 
	{
		this.renderTurbine((TileEntityWindTurbine) tileEntity, d, d1, d2, f);
	}
	
}
