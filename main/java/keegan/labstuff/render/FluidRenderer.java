package keegan.labstuff.render;

import java.util.*;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.common.Coord4D;
import keegan.labstuff.render.LabStuffRenderer.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.Fluid;

public final class FluidRenderer 
{
	private static final int BLOCK_STAGES = 1000;
	
	private static Map<RenderData, DisplayInteger[]> cachedCenterFluids = new HashMap<RenderData, DisplayInteger[]>();
	
	public static void translateToOrigin(Coord4D origin)
	{
		GL11.glTranslated(getX(origin.xCoord), getY(origin.yCoord), getZ(origin.zCoord));
	}
	
	public static int getStages(RenderData data)
	{
		return data.height*BLOCK_STAGES;
	}
	
	public static void pop()
	{
		GL11.glPopAttrib();
		GlStateManager.popMatrix();
	}

	public static void push()
	{
		GlStateManager.pushMatrix();
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static DisplayInteger getTankDisplay(RenderData data)
	{
		return getTankDisplay(data, 1);
	}
	
	public static DisplayInteger getTankDisplay(RenderData data, double scale)
	{
		int maxStages = getStages(data);
		int stage = Math.min(maxStages, (int)(scale*(float)maxStages));
				
		if(cachedCenterFluids.containsKey(data))
		{
			DisplayInteger[] ret = cachedCenterFluids.get(data);
			
			if(ret[stage] != null)
			{
				return ret[stage];
			}
		}

		Model3D toReturn = new Model3D();
		toReturn.baseBlock = Blocks.WATER;
		toReturn.setTexture(LabStuffRenderer.getFluidTexture(data.fluidType, FluidType.STILL));

		DisplayInteger display = DisplayInteger.createAndStart();
		
		if(!cachedCenterFluids.containsKey(data))
		{
			cachedCenterFluids.put(data, new DisplayInteger[maxStages+1]);
		}
		
		cachedCenterFluids.get(data)[stage] = display;
		
		if(maxStages == 0)
		{
			maxStages = stage = 1;
		}

		if(data.fluidType.getStill() != null)
		{
			toReturn.minX = 0 + .01;
			toReturn.minY = 0 + .01;
			toReturn.minZ = 0 + .01;

			toReturn.maxX = data.length - .01;
			toReturn.maxY = ((float)stage/(float)maxStages)*data.height - .01;
			toReturn.maxZ = data.width - .01;

			LabStuffRenderer.renderObject(toReturn);
		}

		display.endList();

		return display;
	}
	
	public static class RenderData
	{
		public Coord4D location;

		public int height;
		public int length;
		public int width;
		
		public Fluid fluidType;

		@Override
		public int hashCode()
		{
			int code = 1;
			code = 31 * code + location.hashCode();
			code = 31 * code + height;
			code = 31 * code + length;
			code = 31 * code + width;
			code = 31 * code + fluidType.getName().hashCode();
			return code;
		}

		@Override
		public boolean equals(Object data)
		{
			return data instanceof RenderData && ((RenderData)data).height == height &&
					((RenderData)data).length == length && ((RenderData)data).width == width && ((RenderData)data).fluidType == fluidType;
		}
	}
	
	
	private static double getX(int x)
	{
		return x - TileEntityRendererDispatcher.staticPlayerX;
	}

	private static double getY(int y)
	{
		return y - TileEntityRendererDispatcher.staticPlayerY;
	}

	private static double getZ(int z)
	{
		return z - TileEntityRendererDispatcher.staticPlayerZ;
	}

	
	public static void resetDisplayInts()
	{
		cachedCenterFluids.clear();
	}
}