package keegan.labstuff.models;

import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class ModelTurbineRotor extends ModelBase
{
  //fields
    ModelRenderer Shape1;
    ModelRenderer Blade1;
    ModelRenderer Blade2;
    ModelRenderer Blade3;
    ModelRenderer Blade4;
  
  public ModelTurbineRotor()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Shape1 = new ModelRenderer(this, 0, 0);
      Shape1.addBox(0F, 0F, 0F, 2, 16, 2);
      Shape1.setRotationPoint(-1F, 8F, -1F);
      Shape1.setTextureSize(64, 32);
      Shape1.mirror = true;
      setRotation(Shape1, 0F, 0F, 0F);
      Blade1 = new ModelRenderer(this, 0, 0);
      Blade1.addBox(0F, 0F, 0F, 16, 1, 2);
      Blade1.setRotationPoint(-8F, 12F, -1F);
      Blade1.setTextureSize(64, 32);
      Blade1.mirror = true;
      setRotation(Blade1, 0F, 0F, 0F);
      Blade2 = new ModelRenderer(this, 0, 0);
      Blade2.addBox(0F, 0F, 0F, 2, 1, 16);
      Blade2.setRotationPoint(-1F, 12F, -8F);
      Blade2.setTextureSize(64, 32);
      Blade2.mirror = true;
      setRotation(Blade2, 0F, 0F, 0F);
      Blade3 = new ModelRenderer(this, 0, 0);
      Blade3.addBox(0F, 0F, 0F, 16, 1, 2);
      Blade3.setRotationPoint(-8F, 17F, -1F);
      Blade3.setTextureSize(64, 32);
      Blade3.mirror = true;
      setRotation(Blade3, 0F, 0F, 0F);
      Blade4 = new ModelRenderer(this, 0, 0);
      Blade4.addBox(0F, 0F, 0F, 2, 1, 16);
      Blade4.setRotationPoint(-1F, 17F, -8F);
      Blade4.setTextureSize(64, 32);
      Blade4.mirror = true;
      setRotation(Blade4, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Shape1.render(f5);
    Blade1.render(f5);
    Blade2.render(f5);
    Blade3.render(f5);
    Blade4.render(f5);
  }
  
  public void render(float f, boolean bottom, boolean top)
  {
	  Shape1.render(f);
	  if(bottom)
	  {
		  Blade1.render(f);
		  Blade2.render(f);
	  }
	  if(top)
	  {
		  Blade3.render(f);
		  Blade4.render(f);
	  }
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

}
