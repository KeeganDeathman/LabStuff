package keegan.labstuff.render;


import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import keegan.labstuff.models.ModelDLLaptop;
import keegan.labstuff.tileentity.TileEntityDLLaptop;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

public class RenderDLLaptop extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{
	//This method is called when minecraft renders a tile entity

	public static Minecraft mc = Minecraft.getMinecraft();
	public ModelDLLaptop model = new ModelDLLaptop();
	
	public RenderDLLaptop()
	{
		
	}
	
	public void renderComputer(TileEntityDLLaptop entity, double x, double y, double z, float tick)
	{
		int i = entity.blockMetadata;

	        // Binds the texture
		 	mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/models/blockdllaptop.png"));
		 	GL11.glPushMatrix();
	        GL11.glTranslatef((float) x, (float) y, (float) z);
	        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
	        // Use this or else model renders upside-down.
	        GL11.glRotatef(180, 180F, 0F, 1F);
	        short rotate = 0;
	        
	        if (i == 0)
	            rotate = -90;

	        if (i == 1)
	            rotate = 0;

	        if (i == 2)
	            rotate = 90;

	        if (i == 3)
	            rotate = 180;
	        GL11.glRotatef(rotate, 0F, 1F, 0F);
	        if(entity.getBeenOpened() == 1)
	        {
	        	if(entity.isTablet())
		        {
		        	model.renderModel(0.0625F, true);
		        	//System.out.println("Rendering tablet because " + entity.tablet);
		        }
		        else
		        {
		        	//System.out.println("Rendering no tablet because " + entity.tablet);
		        	model.renderModel(0.0625F, false);
		        }
	        }
	        else
	        {
	        	model.renderModel(0.0625F, false);
	        }
	        GL11.glPopMatrix();
	}
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) 
	{
		this.renderComputer((TileEntityDLLaptop) tileEntity, d, d1, d2, f);
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
		return true;
	}
	@Override
	public int getRenderId() {
		// TODO Auto-generated method stub
		return getRenderId();
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void renderInventoryBlock(Block arg0, int arg1, int arg2,
			RenderBlocks arg3) {
		// TODO Auto-generated method stub
		
	}

}
