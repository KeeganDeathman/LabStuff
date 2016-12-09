package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.models.ModelPlasmaPipe;
import keegan.labstuff.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityRenderDataCable extends TileEntitySpecialRenderer{

	public static Minecraft mc = Minecraft.getMinecraft();
	public static ModelPlasmaPipe model = new ModelPlasmaPipe();
	public static ResourceLocation tex = new ResourceLocation("labstuff:textures/models/datacable.png");
	
	
	private boolean up = false;
	private boolean down = false;
	private boolean north = false;
	private boolean east = false;
	private boolean south = false;
	private boolean west = false;
	
	public void renderPipe(TileEntityDataCable entity, double x, double y, double z, float tick)
	{
		 int i = entity.getBlockMetadata();

		 
		 
			// Binds the texture
		 	GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		 	mc.renderEngine.bindTexture(tex);
	        GL11.glPushMatrix();
	        GL11.glTranslatef((float) x, (float) y, (float) z);
	        GL11.glTranslatef(0.5F, 1.5F, 0.5F);
	        // Use this or else model renders upside-down.
	        GL11.glRotatef(180, 180F, 0F, 1F);
	        
	        this.configureSides(entity);
	        
	        this.model.renderCable(null, (float) x, (float) y, (float) z, 0.0F, 0.0F, 0.0625F, north, east, south, west, up, down);

	        GL11.glDisable(GL11.GL_BLEND);
	        
	        GL11.glPopMatrix();
	}
	
	
	 public void configureSides(TileEntityDataCable tile)
	 {
		 int x = tile.getPos().getX();
	     int y = tile.getPos().getY();
	     int z = tile.getPos().getZ();
	     this.up = this.configSide(tile.getWorld(), x, y + 1, z);
	     this.down = this.configSide(tile.getWorld(), x, y - 1, z);
	     this.east = this.configSide(tile.getWorld(), x + 1, y, z);
	     this.west = this.configSide(tile.getWorld(), x - 1, y, z);
	     this.south = this.configSide(tile.getWorld(), x, y, z + 1);
	     this.north = this.configSide(tile.getWorld(), x, y, z - 1);
	 }
	 
	 public boolean configSide(World world, int x, int y, int z)
	 {
		 if(world.getTileEntity(new BlockPos(x, y, z)) instanceof TileEntityDataCable || world.getTileEntity(new BlockPos(x, y, z)) instanceof DataConnectedDevice)
			 return true;
		 return false;
	 }
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f, int i) 
	{
		this.renderPipe((TileEntityDataCable) tileEntity, d, d1, d2, f);
	}

}
