package keegan.labstuff.render;

import keegan.labstuff.models.ModelElectrifier;
import keegan.labstuff.tileentity.TileEntityComputer;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class TileEntityRenderElectrifier extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler{
	//This method is called when minecraft renders a tile entity

	public static Minecraft mc = Minecraft.getMinecraft();
	public static ModelElectrifier model = new ModelElectrifier();
	public static ResourceLocation Tex;
	
	public void renderElectrifier(TileEntityElectrifier entity, double x, double y, double z, float tick)
	{
		 int i = entity.blockMetadata;

	        // Binds the texture
		 	Tex = entity.Tex;
		 	mc.renderEngine.bindTexture(Tex);
	        GL11.glPushMatrix();
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
	        
	        this.model.render(null, (float) x, (float) y, (float) z, 0.0F, 0.0F, 0.0625F);

	        GL11.glPopMatrix();
	}
	
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) 
	{
		this.renderElectrifier((TileEntityElectrifier) tileEntity, d, d1, d2, f);
	}
	
	@Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer)
    {
        float scale = 0.875F;

        GL11.glPushMatrix();
        // Binds the texture
        mc.renderEngine.bindTexture(Tex);

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
