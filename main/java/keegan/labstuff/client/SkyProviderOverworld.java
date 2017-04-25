package keegan.labstuff.client;

import java.util.Random;

import org.lwjgl.opengl.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.client.FMLClientHandler;

public class SkyProviderOverworld extends IRenderHandler
{
    private static final ResourceLocation moonTexture = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation sunTexture = new ResourceLocation("textures/environment/sun.png");

    private static boolean optifinePresent = false;

    static
    {
        try
        {
            optifinePresent = Launch.classLoader.getClassBytes("CustomColorizer") != null;
        }
        catch (final Exception e)
        {
        }
    }

    private int starGLCallList = GLAllocation.generateDisplayLists(7);
    private int glSkyList;
    private int glSkyList2;
    private final ResourceLocation planetToRender = new ResourceLocation("labstuff:textures/celestialbodies/earth.png");

    public SkyProviderOverworld()
    {
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer worldrenderer = tessellator.getBuffer();
        final Random rand = new Random(10842L);
        GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 1, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 2, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 3, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glNewList(this.starGLCallList + 4, GL11.GL_COMPILE);
        this.renderStars(worldrenderer, rand);
        tessellator.draw();
        GL11.glEndList();
        GL11.glPopMatrix();
        this.glSkyList = this.starGLCallList + 5;
        GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
        final byte byte2 = 5;
        final int i = 256 / byte2 + 2;
        float f = 16F;
        VertexBuffer worldRenderer = tessellator.getBuffer();

        for (int j = -byte2 * i; j <= byte2 * i; j += byte2)
        {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2)
            {
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
                worldRenderer.pos(j, f, l).endVertex();
                worldRenderer.pos(j + byte2, f, l).endVertex();
                worldRenderer.pos(j + byte2, f, l + byte2).endVertex();
                worldRenderer.pos(j, f, l + byte2).endVertex();
                tessellator.draw();
            }
        }

        GL11.glEndList();
        this.glSkyList2 = this.starGLCallList + 6;
        GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int k = -byte2 * i; k <= byte2 * i; k += byte2)
        {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2)
            {
                worldRenderer.pos(k + byte2, f, i1).endVertex();
                worldRenderer.pos(k, f, i1).endVertex();
                worldRenderer.pos(k, f, i1 + byte2).endVertex();
                worldRenderer.pos(k + byte2, f, i1 + byte2).endVertex();
            }
        }

        tessellator.draw();
        GL11.glEndList();
    }

    private final Minecraft minecraft = FMLClientHandler.instance().getClient();

    
    
    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc)
    {
    	if (!LabStuffClientProxy.overworldTextureRequestSent)
        {
            LabStuffMain.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_OVERWORLD_IMAGE, LabStuffUtils.getDimensionID(mc.theWorld), new Object[] {}));
            LabStuffClientProxy.overworldTextureRequestSent = true;
        }
    	
        final float var20 = 400.0F + (float) this.minecraft.thePlayer.posY / 2F;

        // if (this.minecraft.thePlayer.getRidingEntity() != null)
        {
            // var20 = (float) (this.minecraft.thePlayer.posY - 200.0F);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        final Vec3d var2 = this.minecraft.theWorld.getSkyColor(this.minecraft.getRenderViewEntity(), partialTicks);
        float var3 = (float) var2.xCoord;
        float var4 = (float) var2.yCoord;
        float var5 = (float) var2.zCoord;
        float var8;

        if (this.minecraft.gameSettings.anaglyph)
        {
            final float var6 = (var3 * 30.0F + var4 * 59.0F + var5 * 11.0F) / 100.0F;
            final float var7 = (var3 * 30.0F + var4 * 70.0F) / 100.0F;
            var8 = (var3 * 30.0F + var5 * 70.0F) / 100.0F;
            var3 = var6;
            var4 = var7;
            var5 = var8;
        }

        GL11.glColor3f(var3, var4, var5);
        final Tessellator var23 = Tessellator.getInstance();
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(var3, var4, var5);
        GL11.glCallList(this.glSkyList);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        final float[] var24 = this.minecraft.theWorld.provider.calcSunriseSunsetColors(this.minecraft.theWorld.getCelestialAngle(partialTicks), partialTicks);
        float var9;
        float var10;
        float var11;
        float var12;

        if (var24 != null)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(this.minecraft.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            var8 = var24[0];
            var9 = var24[1];
            var10 = var24[2];
            float var13;

            if (this.minecraft.gameSettings.anaglyph)
            {
                var11 = (var8 * 30.0F + var9 * 59.0F + var10 * 11.0F) / 100.0F;
                var12 = (var8 * 30.0F + var9 * 70.0F) / 100.0F;
                var13 = (var8 * 30.0F + var10 * 70.0F) / 100.0F;
                var8 = var11;
                var9 = var12;
                var10 = var13;
            }

            VertexBuffer worldRenderer = var23.getBuffer();
            worldRenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
            worldRenderer.pos(0.0D, 100.0D, 0.0D).color(var8, var9, var10, var24[3]).endVertex();
            final byte var26 = 16;

            for (int var27 = 0; var27 <= var26; ++var27)
            {
                var13 = var27 * (float) Math.PI * 2.0F / var26;
                final float var14 = MathHelper.sin(var13);
                final float var15 = MathHelper.cos(var13);
                worldRenderer.pos(var14 * 120.0F, var15 * 120.0F, -var15 * 40.0F * var24[3]).color(var24[0], var24[1], var24[2], 0.0F).endVertex();
            }

            var23.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glPushMatrix();
        var8 = 1.0F - this.minecraft.theWorld.getRainStrength(partialTicks);
        var9 = 0.0F;
        var10 = 0.0F;
        var11 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, var8);
        GL11.glTranslatef(var9, var10, var11);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        //At 0.8, these will look bright against a black sky - allows some headroom for them to
        //look even brighter in outer dimensions (further from the sun)
        GL11.glColor4f(0.8F, 0.8F, 0.8F, 0.8F);
        GL11.glCallList(this.starGLCallList);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glPushMatrix();
        float celestialAngle = this.minecraft.theWorld.getCelestialAngle(partialTicks);
        GL11.glRotatef(celestialAngle * 360.0F, 1.0F, 0.0F, 0.0F);
        

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        var12 = 8.0F;
        VertexBuffer worldRenderer = var23.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(-var12, 99.9D, -var12).endVertex();
        worldRenderer.pos(var12, 99.9D, -var12).endVertex();
        worldRenderer.pos(var12, 99.9D, var12).endVertex();
        worldRenderer.pos(-var12, 99.9D, var12).endVertex();
        var23.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var12 = 28.0F;
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.sunTexture);
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-var12, 100.0D, -var12).tex(0.0D, 0.0D).endVertex();
        worldRenderer.pos(var12, 100.0D, -var12).tex(1.0D, 0.0D).endVertex();
        worldRenderer.pos(var12, 100.0D, var12).tex(1.0D, 1.0D).endVertex();
        worldRenderer.pos(-var12, 100.0D, var12).tex(0.0D, 1.0D).endVertex();
        var23.draw();
    

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(0.0F, 0.0F, 0.0F, 1.0F);
        var12 = 11.3F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        worldRenderer.pos(-var12, -99.9D, var12).endVertex();
        worldRenderer.pos(var12, -99.9D, var12).endVertex();
        worldRenderer.pos(var12, -99.9D, -var12).endVertex();
        worldRenderer.pos(-var12, -99.9D, -var12).endVertex();
        var23.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var12 = 40.0F;
        this.minecraft.renderEngine.bindTexture(SkyProviderOverworld.moonTexture);
        float var28 = this.minecraft.theWorld.getMoonPhase();
        final int var30 = (int) (var28 % 4);
        final int var29 = (int) (var28 / 4 % 2);
        final float var16 = (var30 + 0) / 4.0F;
        final float var17 = (var29 + 0) / 2.0F;
        final float var18 = (var30 + 1) / 4.0F;
        final float var19 = (var29 + 1) / 2.0F;
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(-var12, -100.0D, var12).tex(var18, var19).endVertex();
        worldRenderer.pos(var12, -100.0D, var12).tex(var16, var19).endVertex();
        worldRenderer.pos(var12, -100.0D, -var12).tex(var16, var17).endVertex();
        worldRenderer.pos(-var12, -100.0D, -var12).tex(var18, var17).endVertex();
        var23.draw();
    

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_BLEND);

        if (this.planetToRender != null)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, -var20 / 10, 0.0F);
            float scale = 100 * (0.3F - var20 / 10000.0F);
            scale = Math.max(scale, 0.2F);
            GL11.glScalef(scale, 0.0F, scale);
            GL11.glTranslatef(0.0F, -var20, 0.0F);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            this.minecraft.renderEngine.bindTexture(this.planetToRender);

            var10 = 1.0F;
            final float alpha = 0.5F;
            GL11.glColor4f(Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F), Math.min(alpha, 1.0F));
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            worldRenderer.pos(-var10, 0, var10).tex(0.0F, 1.0F).endVertex();
            worldRenderer.pos(var10, 0, var10).tex(1.0F, 1.0F).endVertex();
            worldRenderer.pos(var10, 0, -var10).tex(1.0F, 0.0F).endVertex();
            worldRenderer.pos(-var10, 0, -var10).tex(0.0F, 0.0F).endVertex();
            var23.draw();
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);

        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glColor3f(0.0F, 0.0F, 0.0F);

		/* This all does nothing!
        double var25 = 0.0D;

		// if (this.minecraft.thePlayer.getRidingEntity() != null)
		{
			var25 = this.minecraft.thePlayer.posY - 64;

			if (var25 < 0.0D)
			{
				// GL11.glPushMatrix();
				// GL11.glTranslatef(0.0F, 12.0F, 0.0F);
				// GL11.glCallList(this.glSkyList2);
				// GL11.glPopMatrix();
				// var10 = 1.0F;
				// var11 = -((float)(var25 + 65.0D));
				// var12 = -var10;
				// var23.startDrawingQuads();
				// var23.setColorRGBA_I(0, 255);
				// var23.addVertex(-var10, var11, var10);
				// var23.addVertex(var10, var11, var10);
				// var23.addVertex(var10, var12, var10);
				// var23.addVertex(-var10, var12, var10);
				// var23.addVertex(-var10, var12, -var10);
				// var23.addVertex(var10, var12, -var10);
				// var23.addVertex(var10, var11, -var10);
				// var23.addVertex(-var10, var11, -var10);
				// var23.addVertex(var10, var12, -var10);
				// var23.addVertex(var10, var12, var10);
				// var23.addVertex(var10, var11, var10);
				// var23.addVertex(var10, var11, -var10);
				// var23.addVertex(-var10, var11, -var10);
				// var23.addVertex(-var10, var11, var10);
				// var23.addVertex(-var10, var12, var10);
				// var23.addVertex(-var10, var12, -var10);
				// var23.addVertex(-var10, var12, -var10);
				// var23.addVertex(-var10, var12, var10);
				// var23.addVertex(var10, var12, var10);
				// var23.addVertex(var10, var12, -var10);
				// var23.draw();
			}
		}

		if (this.minecraft.theWorld.provider.isSkyColored())
		{
			GL11.glColor3f(0.0f, 0.0f, 0.0f);
		}
		else
		{
			GL11.glColor3f(var3, var4, var5);
		}
		GL11.glColor3f(0.0f, 0.0f, 0.0f);

		GL11.glPushMatrix();
		GL11.glTranslatef(0.0F, -((float) (var25 - 16.0D)), 0.0F);
		GL11.glPopMatrix();
		*/
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glDepthMask(true);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void renderStars(VertexBuffer worldRenderer, Random rand)
    {
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

        for (int i = 0; i < (1200); ++i)
        {
            double x = rand.nextFloat() * 2.0F - 1.0F;
            double y = rand.nextFloat() * 2.0F - 1.0F;
            double z = rand.nextFloat() * 2.0F - 1.0F;
            final double size = 0.15F + rand.nextFloat() * 0.1F;
            double r = x * x + y * y + z * z;

            if (r < 1.0D && r > 0.01D)
            {
                r = 1.0D / Math.sqrt(r);
                x *= r;
                y *= r;
                z *= r;
                final double xx = x * (100.0D);
                final double zz = z * (100.0D);
                if (Math.abs(xx) < 29D && Math.abs(zz) < 29D)
                {
                    continue;
                }
                final double yy = y * (100.0D);
                final double theta = Math.atan2(x, z);
                final double sinth = Math.sin(theta);
                final double costh = Math.cos(theta);
                final double phi = Math.atan2(Math.sqrt(x * x + z * z), y);
                final double sinphi = Math.sin(phi);
                final double cosphi = Math.cos(phi);
                final double rho = rand.nextDouble() * Math.PI * 2.0D;
                final double sinrho = Math.sin(rho);
                final double cosrho = Math.cos(rho);

                for (int j = 0; j < 4; ++j)
                {
                    final double a = 0.0D;
                    final double b = ((j & 2) - 1) * size;
                    final double c = ((j + 1 & 2) - 1) * size;
                    final double d = b * cosrho - c * sinrho;
                    final double e = c * cosrho + b * sinrho;
                    final double dy = d * sinphi + a * cosphi;
                    final double ff = a * sinphi - d * cosphi;
                    final double dx = ff * sinth - e * costh;
                    final double dz = e * sinth + ff * costh;
                    worldRenderer.pos(xx + dx, yy + dy, zz + dz).endVertex();
                }
            }
        }
    }
}