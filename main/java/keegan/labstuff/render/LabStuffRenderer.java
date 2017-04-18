package keegan.labstuff.render;

import java.util.*;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.common.ReflectionUtils;
import keegan.labstuff.render.transmitter.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LabStuffRenderer 
{
	
	public enum FluidType
	{
		STILL,
		FLOWING;
	}
	
	public static Map<FluidType, Map<Fluid, TextureAtlasSprite>> textureMap = new HashMap<FluidType, Map<Fluid, TextureAtlasSprite>>();
	
	public static TextureAtlasSprite missingIcon;
	public static TextureAtlasSprite plasmaIcon;
	
	public static void init()
	{
		MinecraftForge.EVENT_BUS.register(new LabStuffRenderer());
	}
	
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Pre event)
	{
		TransmitterModel.registerIcons(event.getMap());
		
		plasmaIcon = event.getMap().registerSprite(new ResourceLocation("labstuff:blocks/plasma"));

		FluidRenderer.resetDisplayInts();
	}
	
	@SubscribeEvent
	public void onStitch(TextureStitchEvent.Post event)
	{
		initFluidTextures(event.getMap());
		
		RenderLiquidPipe.onStitch();
		
	}
	
	public static class DisplayInteger
    {
    	public int display;
    	
    	@Override
    	public int hashCode()
    	{
    		int code = 1;
    		code = 31 * code + display;
    		return code;
    	}
    	
    	@Override
    	public boolean equals(Object obj)
    	{
    		return obj instanceof DisplayInteger && ((DisplayInteger)obj).display == display;
    	}
    	
    	public static DisplayInteger createAndStart()
    	{
    		DisplayInteger newInteger = new DisplayInteger();
    		newInteger.display =  GLAllocation.generateDisplayLists(1);
    		GL11.glNewList(newInteger.display, GL11.GL_COMPILE);
    		return newInteger;
    	}
    	
    	public static void endList()
    	{
    		GL11.glEndList();
    	}
    	
    	public void render()
    	{
    		GL11.glCallList(display);
    	}
    }
	
	public static void colorFluid(Fluid fluid)
	{
		color(fluid.getColor());
	}
	
	public static void color(int color)
	{
		float cR = (color >> 16 & 0xFF) / 255.0F;
	    float cG = (color >> 8 & 0xFF) / 255.0F;
	    float cB = (color & 0xFF) / 255.0F;
	    
	    GL11.glColor3f(cR, cG, cB);
	}
	
	public static class Model3D
	{
		public double posX, posY, posZ;
		
		public double minX, minY, minZ;
		public double maxX, maxY, maxZ;
		
	    public double textureStartX = 0, textureStartY = 0, textureStartZ = 0;
	    public double textureSizeX = 16, textureSizeY = 16, textureSizeZ = 16;
	    public double textureOffsetX = 0, textureOffsetY = 0, textureOffsetZ = 0;
	    
	    public int[] textureFlips = new int[] {2, 2, 2, 2, 2, 2};
		
		public TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
		
		public boolean[] renderSides = new boolean[] {true, true, true, true, true, true, false};

		public Block baseBlock = Blocks.SAND;
		
	    public final void setBlockBounds(double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos)
	    {
	    	minX = xNeg;
	    	minY = yNeg;
	    	minZ = zNeg;
	    	maxX = xPos;
	    	maxY = yPos;
	    	maxZ = zPos;
	    }
	    
	    public double sizeX()
	    {
	    	return maxX-minX;
	    }
	    
	    public double sizeY()
	    {
	    	return maxY-minY;
	    }
	    
	    public double sizeZ()
	    {
	    	return maxZ-minZ;
	    }
		
		public void setSideRender(EnumFacing side, boolean value)
		{
			renderSides[side.ordinal()] = value;
		}
		
		public boolean shouldSideRender(EnumFacing side)
		{
			return renderSides[side.ordinal()];
		}

		public TextureAtlasSprite getBlockTextureFromSide(int i)
		{
			return textures[i];
		}
		
		public void setTexture(TextureAtlasSprite tex)
		{
			Arrays.fill(textures, tex);
		}
		
		public void setTextures(TextureAtlasSprite down, TextureAtlasSprite up, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite west, TextureAtlasSprite east)
		{
			textures[0] = down;
			textures[1] = up;
			textures[2] = north;
			textures[3] = south;
			textures[4] = west;
			textures[5] = east;
		}
	}
	
	private static boolean optifineBreak = false;
	private static float lightmapLastX;
    private static float lightmapLastY;
	
	public static void glowOn() 
    {
    	glowOn(15);
    }
    
    public static void glowOn(int glow)
    {
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        
        try {
        	lightmapLastX = OpenGlHelper.lastBrightnessX;
        	lightmapLastY = OpenGlHelper.lastBrightnessY;
        } catch(NoSuchFieldError e) {
        	optifineBreak = true;
        }
        
        float glowRatioX = Math.min((glow/15F)*240F + lightmapLastX, 240);
        float glowRatioY = Math.min((glow/15F)*240F + lightmapLastY, 240);
        
        if(!optifineBreak)
        {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, glowRatioX, glowRatioY);        	
        }
    }

    public static void glowOff() 
    {
    	if(!optifineBreak)
    	{
    		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapLastX, lightmapLastY);
    	}
    	
        GL11.glPopAttrib();
    }

	
	public static void renderObject(Model3D object)
	{
		if(object == null)
		{
			return;
		}
		
		GlStateManager.pushMatrix();
		GL11.glTranslated(object.minX, object.minY, object.minZ);
		RenderResizableCuboid.INSTANCE.renderCube(object);
		GlStateManager.popMatrix();
	}
	
	public static void resetColor()
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}
	
	public static void initFluidTextures(TextureMap map) 
	{
		missingIcon = map.getMissingSprite();

		textureMap.clear();

		for(FluidType type : FluidType.values()) 
		{
			textureMap.put(type, new HashMap<Fluid, TextureAtlasSprite>());
		}

		for(Fluid fluid : FluidRegistry.getRegisteredFluids().values()) 
		{
			if(fluid.getFlowing() != null) 
			{
				String flow = fluid.getFlowing().toString();
				TextureAtlasSprite sprite;
				
				if(map.getTextureExtry(flow) != null) 
				{
					sprite = map.getTextureExtry(flow);
				} 
				else {
					sprite = map.registerSprite(fluid.getStill());
				}
				
				textureMap.get(FluidType.FLOWING).put(fluid, sprite);
			}

			if(fluid.getStill() != null) 
			{
				String still = fluid.getStill().toString();
				TextureAtlasSprite sprite;
				
				if(map.getTextureExtry(still) != null) 
				{
					sprite = map.getTextureExtry(still);
				} 
				else {
					sprite = map.registerSprite(fluid.getStill());
				}
				
				textureMap.get(FluidType.STILL).put(fluid, sprite);
			}
		}
	}
	
	public static TextureAtlasSprite getFluidTexture(Fluid fluid, FluidType type) 
	{
		if(fluid == null || type == null) 
		{
			return missingIcon;
		}
		
		Map<Fluid, TextureAtlasSprite> map = textureMap.get(type);
		
		return map.containsKey(fluid) ? map.get(fluid) : missingIcon;
	}

	public static void prepFlowing(Model3D model, Fluid fluid)
	{
		TextureAtlasSprite still = getFluidTexture(fluid, FluidType.STILL);
		TextureAtlasSprite flowing = getFluidTexture(fluid, FluidType.FLOWING);
		
		model.setTextures(still, still, flowing, flowing, flowing, flowing);
	}
	
	public static boolean isDrawing(Tessellator tess)
	{
		return isDrawing(tess.getBuffer());
	}
	
	public static String[] VertexBuffer_isDrawing = new String[] {"isDrawing", "field_179010_r"};
	
	public static boolean isDrawing(VertexBuffer buffer)
	{
		return (boolean)ReflectionUtils.getPrivateValue(buffer, VertexBuffer.class, VertexBuffer_isDrawing);
	}

	public static BakedQuad iconTransform(BakedQuad quad, TextureAtlasSprite sprite) 
	{
		int[] vertices = new int[quad.getVertexData().length];
		System.arraycopy(quad.getVertexData(), 0, vertices, 0, vertices.length);
		
	    for(int i = 0; i < 4; ++i)
        {
            int j = quad.getFormat().getIntegerSize() * i;
            int uvIndex = quad.getFormat().getUvOffsetById(0) / 4;
            vertices[j + uvIndex] = Float.floatToRawIntBits(sprite.getInterpolatedU(quad.getSprite().getUnInterpolatedU(Float.intBitsToFloat(vertices[j + uvIndex]))));
            vertices[j + uvIndex + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV(quad.getSprite().getUnInterpolatedV(Float.intBitsToFloat(vertices[j + uvIndex + 1]))));
        }
	    
		return new BakedQuad(vertices, quad.getTintIndex(), quad.getFace(), sprite, quad.shouldApplyDiffuseLighting(), quad.getFormat());
	}
	
	public static ResourceLocation getBlocksTexture()
	{
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
}
