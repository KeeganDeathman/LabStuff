package keegan.labstuff.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPlasmaPipe extends ModelBase
{
  //fields
    ModelRenderer Core;
    ModelRenderer East;
    ModelRenderer West;
    ModelRenderer Down;
    ModelRenderer Up;
    ModelRenderer North;
    ModelRenderer South;
  
  public ModelPlasmaPipe()
  {
    textureWidth = 64;
    textureHeight = 128;
    
      Core = new ModelRenderer(this, 40, 0);
      Core.addBox(0F, 0F, 0F, 6, 6, 6);
      Core.setRotationPoint(-3F, 13F, -3F);
      Core.setTextureSize(64, 128);
      Core.mirror = true;
      setRotation(Core, 0F, 0F, 0F);
      East = new ModelRenderer(this, 18, 46);
      East.addBox(0F, 0F, 0F, 5, 6, 6);
      East.setRotationPoint(3F, 13F, -3F);
      East.setTextureSize(64, 128);
      East.mirror = true;
      setRotation(East, 0F, 0F, 0F);
      West = new ModelRenderer(this, 18, 46);
      West.addBox(0F, 0F, 0F, 5, 6, 6);
      West.setRotationPoint(-8F, 13F, -3F);
      West.setTextureSize(64, 128);
      West.mirror = true;
      setRotation(West, 0F, 0F, 0F);
      Down = new ModelRenderer(this, 0, 0);
      Down.addBox(0F, 0F, 0F, 6, 5, 6);
      Down.setRotationPoint(-3F, 19F, -3F);
      Down.setTextureSize(64, 128);
      Down.mirror = true;
      setRotation(Down, 0F, 0F, 0F);
      Up = new ModelRenderer(this, 0, 0);
      Up.addBox(0F, 0F, 0F, 6, 5, 6);
      Up.setRotationPoint(-3F, 8F, -3F);
      Up.setTextureSize(64, 128);
      Up.mirror = true;
      setRotation(Up, 0F, 0F, 0F);
      North = new ModelRenderer(this, 31, 21);
      North.addBox(0F, 0F, 0F, 6, 6, 5);
      North.setRotationPoint(-3F, 13F, 3F);
      North.setTextureSize(64, 128);
      North.mirror = true;
      setRotation(North, 0F, 0F, 0F);
      South = new ModelRenderer(this, 31, 21);
      South.addBox(0F, 0F, 0F, 6, 6, 5);
      South.setRotationPoint(-3F, 13F, -8F);
      South.setTextureSize(64, 128);
      South.mirror = true;
      setRotation(South, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Core.render(f5);
    East.render(f5);
    West.render(f5);
    Down.render(f5);
    Up.render(f5);
    North.render(f5);
    South.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  
  public void renderCable(float f, boolean north, boolean east, boolean south, boolean west, boolean up, boolean down)
  {
	  Core.render(f);
	  if(north)
		  North.render(f);
	  if(east)
		  East.render(f);
	  if(south)
		  South.render(f);
	  if(west)
		  West.render(f);
	  if(up)
		  Up.render(f);
	  if(down)
		  Down.render(f);
  }
  
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
  }

}
