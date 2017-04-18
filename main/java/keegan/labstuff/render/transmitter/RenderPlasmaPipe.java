package keegan.labstuff.render.transmitter;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.common.ColourRGBA;
import keegan.labstuff.multipart.PartPlasmaPipe;
import keegan.labstuff.render.LabStuffRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

public class RenderPlasmaPipe extends RenderTransmitterBase<PartPlasmaPipe>
{
	public RenderPlasmaPipe()
	{
		super();
	}
	
	@Override
	public void renderMultipartAt(PartPlasmaPipe cable, double x, double y, double z, float partialTick, int destroyStage)
	{
		if(cable.currentPlasma == 0)
		{
			return;
		}

		push();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getBuffer();
		GL11.glTranslated(x + 0.5, y+0.5, z + 0.5);

		for(EnumFacing side : EnumFacing.VALUES)
		{
			renderPlasmaSide(worldRenderer, side, cable);
		}

		LabStuffRenderer.glowOn();

		tessellator.draw();

		LabStuffRenderer.glowOff();
		pop();
	}
	
	public void renderPlasmaSide(VertexBuffer renderer, EnumFacing side, PartPlasmaPipe cable)
	{
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		renderTransparency(renderer, LabStuffRenderer.plasmaIcon, getModelForSide(cable, side), new ColourRGBA(1.0, 1.0, 1.0, cable.currentPlasma));
	}
}