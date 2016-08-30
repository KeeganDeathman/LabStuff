package keegan.labstuff.render;


import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import keegan.labstuff.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.*;
import net.minecraftforge.client.model.*;

public class TileEntityRenderToroid extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
	//This method is called when minecraft renders a tile entity

	public static Minecraft mc = Minecraft.getMinecraft();
	public IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("labstuff","models/toroid.obj"));
	
	public TileEntityRenderToroid()
	{
		
	}
	
	public void renderComputer(TileEntityFusionToroidalMagnet entity, double x, double y, double z, float tick)
	{
		// Binds the texture
	 	mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/models/toroid.png"));
	 	GL11.glPushMatrix();
	 	GL11.glTranslatef((float) x , (float) y, (float) z);
        GL11.glTranslatef(0.5F, 0F, 1.5F);
        
        //GL11.glRotatef(rotate, 0F, 1F, 0F);
        GL11.glTranslatef(0f, 0.5f, -1f);
        GL11.glRotatef(90f, 0f, 1f, 0f);
        int meta = entity.getWorldObj().getBlockMetadata(entity.xCoord, entity.yCoord, entity.zCoord);
        if(meta==1)
        	GL11.glRotatef(22, 0f, 1f, 0f);
        if(meta==2)
        	GL11.glRotatef(45, 0f, 1f, 0f);
        if(meta==3)
        	GL11.glRotatef(67, 0f, 1f, 0f);
        if(meta==4)
        	GL11.glRotatef(90, 0f, 1f, 0f);
        if(meta==5)
        	GL11.glRotatef(112, 0f, 1f, 0f);
        if(meta==6)
        	GL11.glRotatef(135, 0f, 1f, 0f);
        if(meta==7)
        	GL11.glRotatef(157, 0f, 1f, 0f);
        //GL11.glScalef(20f, 20f, 20f);
        //GL11.glScalef(45f, 45f, 45f);
        //GL11.glScalef(0f, 0f, 1/3f);
        this.model.renderAll();
        
        GL11.glPopMatrix();
	}
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) 
	{
		this.renderComputer((TileEntityFusionToroidalMagnet) tileEntity, d, d1, d2, f);
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
