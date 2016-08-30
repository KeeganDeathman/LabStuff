package keegan.labstuff.models;

import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class ModelAcceleratorControlPanel extends ModelBase
{
  //fields
    ModelRenderer Shape1;
    ModelRenderer Shape2;
    ModelRenderer Shape3;
    ModelRenderer Shape4;
    ModelRenderer Shape5;
    ModelRenderer Shape6;
    ModelRenderer Shape7;
    ModelRenderer Shape8;
  
  public ModelAcceleratorControlPanel()
  {
    textureWidth = 256;
    textureHeight = 128;
    
      Shape5 = new ModelRenderer(this, 0, 0);
      Shape5.addBox(0F, 0F, 0F, 46, 16, 16);
      Shape5.setRotationPoint(-24F, 8F, 8F);
      Shape5.setTextureSize(256, 128);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 2.007645F, 0F);
      Shape4 = new ModelRenderer(this, 129, 0);
      Shape4.addBox(0F, 0F, 0F, 46, 4, 16);
      Shape4.setRotationPoint(-24F, 8F, -8F);
      Shape4.setTextureSize(256, 128);
      Shape4.mirror = true;
      setRotation(Shape4, 0.3717861F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 129, 20);
      Shape4.addBox(0F, 0F, 0F, 46, 1, 16);
      Shape4.setRotationPoint(-42F, 12F, -33F);
      Shape4.setTextureSize(256, 128);
      Shape4.mirror = true;
      setRotation(Shape4, 1.570796F, -1.078177F, 0F);
      Shape4 = new ModelRenderer(this, 0, 40);
      Shape4.addBox(0F, 0F, 0F, 46, 12, 1);
      Shape4.setRotationPoint(-24F, -4F, 7F);
      Shape4.setTextureSize(256, 128);
      Shape4.mirror = true;
      setRotation(Shape4, 0F, 0F, 0F);
      Shape5 = new ModelRenderer(this, 0, 0);
      Shape5.addBox(0F, 0F, 0F, 46, 16, 16);
      Shape5.setRotationPoint(-24F, 8F, -8F);
      Shape5.setTextureSize(256, 128);
      Shape5.mirror = true;
      setRotation(Shape5, 0F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 129, 0);
      Shape4.addBox(0F, 0F, 0F, 46, 4, 16);
      Shape4.setRotationPoint(-29F, 8F, -40F);
      Shape4.setTextureSize(256, 128);
      Shape4.mirror = true;
      setRotation(Shape4, 0.3717861F, -1.07818F, 0F);
      Shape4 = new ModelRenderer(this, 129, 20);
      Shape4.addBox(0F, 0F, 0F, 46, 1, 16);
      Shape4.setRotationPoint(-24F, 8F, -4.5F);
      Shape4.setTextureSize(256, 128);
      Shape4.mirror = true;
      setRotation(Shape4, 0.8922867F, 0F, 0F);
      Shape4 = new ModelRenderer(this, 129, 20);
      Shape4.addBox(0F, 0F, 0F, 46, 1, 16);
      Shape4.setRotationPoint(-33F, 8F, -37F);
      Shape4.setTextureSize(256, 128);
      Shape4.mirror = true;
      setRotation(Shape4, 0.8922867F, -1.078177F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Shape5.render(f5);
    Shape4.render(f5);
    Shape4.render(f5);
    Shape4.render(f5);
    Shape5.render(f5);
    Shape4.render(f5);
    Shape4.render(f5);
    Shape4.render(f5);
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
