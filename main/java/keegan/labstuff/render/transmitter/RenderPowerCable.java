package keegan.labstuff.render.transmitter;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.multipart.PartPowerCable;

public class RenderPowerCable extends RenderTransmitterBase<PartPowerCable>
{
	public RenderPowerCable()
	{
		super();
	}
	
	@Override
	public void renderMultipartAt(PartPowerCable cable, double x, double y, double z, float partialTick, int destroyStage)
	{
		if(cable.currentPower == 0)
		{
			return;
		}

		push();
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer worldRenderer = tessellator.getBuffer();
		GL11.glTranslated(x + 0.5, y+0.5, z + 0.5);

		for(EnumFacing side : EnumFacing.VALUES)
		{
			renderEnergySide(worldRenderer, side, cable);
		}


		tessellator.draw();

		pop();
	}
	
	public void renderEnergySide(VertexBuffer renderer, EnumFacing side, PartPowerCable cable)
	{
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}
}