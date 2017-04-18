package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.render.transmitter.RenderTransmitterBase;
import keegan.labstuff.tileentity.TileEntityRocket;
import keegan.labstuff.util.LabStuffUtils;
import keegan.labstuff.util.LabStuffUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.*;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderRocket extends TileEntitySpecialRenderer<TileEntityRocket> {

    private IModel model;
    private IBakedModel bakedModel;

    private IBakedModel getBakedModel(TileEntityRocket te) {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedModel == null) {
            try {
				model = (OBJModel)OBJLoader.INSTANCE.loadModel(LabStuffUtils.getResource(ResourceType.MODEL,  te.getModel() + 	".obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedModel = model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, RenderTransmitterBase.textureGetterFlipV);
        }
        return bakedModel;
    }
    
    protected void bindTexture(ResourceLocation location) {

        TextureManager texturemanager = this.rendererDispatcher.renderEngine;
        if (texturemanager != null) texturemanager.bindTexture(location);
    }

    @Override
    public void renderTileEntityAt(TileEntityRocket te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        if(te.formed)
        	renderHandles(te);


        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }

    private void renderHandles(TileEntityRocket te) {
    	GlStateManager.enableLighting();
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0, .5);
        GlStateManager.translate(0, -te.rocket.length, 0);
        long height = (long)te.height;
        GlStateManager.translate(0, height, 0);

        //RenderHelper.disableStandardItemLighting();
        int bright = 0xF0;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightX, brightY);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
//        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
        bindTexture(new ResourceLocation("labstuff:textures/models/" + te.getModel() + ".png"));
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(getBakedModel(te), world.getBlockState(te.getPos()), bright, true);

        //RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }


}