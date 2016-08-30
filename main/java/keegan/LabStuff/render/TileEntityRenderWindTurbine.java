package keegan.labstuff.render;

import keegan.labstuff.models.ModelWindTurbine;
import keegan.labstuff.tileentity.TileEntityWindTurbine;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class TileEntityRenderWindTurbine extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{
	//This method is called when minecraft renders a tile entity

	public static Minecraft mc = Minecraft.getMinecraft();
	public ModelWindTurbine model = new ModelWindTurbine();
	
	public void renderTurbine(TileEntityWindTurbine entity, double x, double y, double z, float tick)
	{
		 int i = entity.blockMetadata;

	        // Binds the texture
		 	mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/models/WindTurbine.png"));
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float) x, (float) y, (float) z);
	        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
	        // Use this or else model renders upside-down.
	        GL11.glRotatef(180, 180F, 0F, 1F);
	        short rotate = 0;
	        
	        if (i == 0)
	            rotate = 180;

	        if (i == 1)
	            rotate = -90;

	        if (i == 2)
	            rotate = 0;

	        if (i == 3)
	            rotate = 90;
	        GL11.glRotatef(rotate, 0F, 1F, 0F);
	        
	        
	        if(entity.isProperPosition())
	        {
	        	model.spin();
	        }
	        this.model.render(null, (float) x, (float) y, (float) z, 0.0F, 0.0F, 0.0625F);
	        GL11.glPopMatrix();
	}
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) 
	{
		this.renderTurbine((TileEntityWindTurbine) tileEntity, d, d1, d2, f);
	}
	
	@Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        float scale = 0.875F;

        GL11.glPushMatrix();
        // Binds the texture
        mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/models/WindTurbine.png"));

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
