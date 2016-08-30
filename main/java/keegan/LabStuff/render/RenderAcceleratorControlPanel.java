package keegan.labstuff.render;


import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import keegan.labstuff.tileentity.TileEntityAcceleratorControlPanel;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.*;

public class RenderAcceleratorControlPanel extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler
{
	//This method is called when minecraft renders a tile entity

	public static Minecraft mc = Minecraft.getMinecraft();
	public IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation("labstuff:models/acceleratorcontrolpanel.obj"));
	
	public RenderAcceleratorControlPanel()
	{
		
	}
	
	public void renderComputer(TileEntityAcceleratorControlPanel entity, double x, double y, double z, float tick)
	{

	        // Binds the texture
		 	mc.renderEngine.bindTexture(new ResourceLocation("labstuff:textures/models/acceleratorcontrolpanel.png"));
		 	GL11.glPushMatrix();
	        GL11.glTranslatef((float) x + 1 , (float) y, (float) z);
	        //GL11.glTranslatef(0.5F, 1.5F, 0.5F);
	        short rotate = 0;
	        
	        GL11.glRotatef(rotate, 0F, 1F, 0F);
	        GL11.glScalef(20f, 20f, 20f);
	        
	        this.model.renderAll();
	        
	        GL11.glPopMatrix();
	}
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) 
	{
		this.renderComputer((TileEntityAcceleratorControlPanel) tileEntity, d, d1, d2, f);
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
