package keegan.labstuff.models;

import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class ModelDLLaptop extends ModelBase
{
  //fields
    ModelRenderer Keyboard;
    ModelRenderer Screen;
    ModelRenderer Cord;
     ModelRenderer Tablet;
  
  public ModelDLLaptop()
  {
    textureWidth = 64;
    textureHeight = 64;
    
      Keyboard = new ModelRenderer(this, 0, 0);
      Keyboard.addBox(0F, 0F, 0F, 13, 1, 12);
      Keyboard.setRotationPoint(-5F, 23F, 6F);
      Keyboard.setTextureSize(64, 64);
      Keyboard.mirror = true;
      setRotation(Keyboard, 0F, 1.570796F, 0F);
      Screen = new ModelRenderer(this, 0, 19);
      Screen.addBox(0F, 0F, 0F, 13, 1, 12);
      Screen.setRotationPoint(7F, 23F, -7F);
      Screen.setTextureSize(64, 64);
      Screen.mirror = true;
      setRotation(Screen, 1.673038F, -1.570796F, 0F);
      Cord = new ModelRenderer(this, 48, 38);
      Cord.addBox(0F, 0F, 0F, 3, 1, 1);
      Cord.setRotationPoint(6F, 23F, 0F);
      Cord.setTextureSize(64, 64);
      Cord.mirror = true;
      setRotation(Cord, 0F, 0F, 0F);
      Tablet = new ModelRenderer(this, 40, 0);
      Tablet.addBox(0F, 0F, 0F, 5, 1, 7);
      Tablet.setRotationPoint(9F, 23F, 0F);
      Tablet.setTextureSize(64, 64);
      Tablet.mirror = true;
      setRotation(Tablet, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Keyboard.render(f5);
    Screen.render(f5);
    Cord.render(f5);
    Tablet.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity ent)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5, ent);
  }
  
  public void renderModel(float f, boolean tablet)
  {
	  if(tablet)
	  {
		  Cord.render(f);
		  Tablet.render(f);
	  }
	  Keyboard.render(f);
	  Screen.render(f);
  }

}
