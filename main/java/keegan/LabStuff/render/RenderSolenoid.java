package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.render.transmitter.RenderTransmitterBase;
import keegan.labstuff.tileentity.TileEntitySolenoidAxel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class RenderSolenoid extends TileEntitySpecialRenderer<TileEntitySolenoidAxel> {

    private IModel model;
    private IBakedModel bakedModel;

    private IBakedModel getBakedModel() {
        // Since we cannot bake in preInit() we do lazy baking of the model as soon as we need it
        // for rendering
        if (bakedModel == null) {
            try {
                model = OBJLoader.INSTANCE.loadModel(new ResourceLocation("labstuff", "models/block/solenoid.obj"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            bakedModel = model.bake(TRSRTransformation.identity(), Attributes.DEFAULT_BAKED_FORMAT, RenderTransmitterBase.textureGetterFlipV);
        }
        return bakedModel;
    }

    @Override
    public void renderTileEntityAt(TileEntitySolenoidAxel te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Render the rotating handles
        if(te.isMultiBlock())
        	renderHandles(te);


        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

    }
    
    protected void bindTexture(ResourceLocation location) {

        TextureManager texturemanager = this.rendererDispatcher.renderEngine;
        if (texturemanager != null) texturemanager.bindTexture(location);
    }

    private void renderHandles(TileEntitySolenoidAxel te) {
    	GlStateManager.enableLighting();
        GlStateManager.pushMatrix();

        GlStateManager.translate(.5, 0, .5);
        long angle = (long)te.angle;
        GlStateManager.rotate(angle, 0, 1, 0);

        //RenderHelper.disableStandardItemLighting();
        int bright = 0xF0;
        int brightX = bright % 65536;
        int brightY = bright / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightX, brightY);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = te.getWorld();
        // Translate back to local view coordinates so that we can do the acual rendering here
//        GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
        bindTexture(new ResourceLocation("labstuff:textures/models/solenoid.png"));
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightness(getBakedModel(), world.getBlockState(te.getPos()), bright, true);

        //RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }


}