package keegan.labstuff.models;

import net.minecraft.client.model.*;
import net.minecraft.entity.Entity;

public class ModelWindTurbine extends ModelBase
{
  //fields
    ModelRenderer BaseX;
    ModelRenderer BaseY;
    ModelRenderer BaseCenter;
    ModelRenderer Pole;
    ModelRenderer Generator;
    ModelRenderer BladeHub;
    ModelRenderer Blade1;
    ModelRenderer Blade2;
    ModelRenderer Blade3;
  
  public ModelWindTurbine()
  {
    textureWidth = 256;
    textureHeight = 128;
    
      BaseX = new ModelRenderer(this, 0, 0);
      BaseX.addBox(0F, 0F, 0F, 8, 8, 16);
      BaseX.setRotationPoint(-4F, 16F, -8F);
      BaseX.setTextureSize(256, 128);
      BaseX.mirror = true;
      setRotation(BaseX, 0F, 0F, 0F);
      BaseY = new ModelRenderer(this, 48, 0);
      BaseY.addBox(0F, 0F, 0F, 16, 8, 8);
      BaseY.setRotationPoint(-8F, 16F, -4F);
      BaseY.setTextureSize(256, 128);
      BaseY.mirror = true;
      setRotation(BaseY, 0F, 0F, 0F);
      BaseCenter = new ModelRenderer(this, 0, 24);
      BaseCenter.addBox(0F, 0F, 0F, 12, 8, 12);
      BaseCenter.setRotationPoint(-6F, 16F, -6F);
      BaseCenter.setTextureSize(256, 128);
      BaseCenter.mirror = true;
      setRotation(BaseCenter, 0F, 0F, 0F);
      Pole = new ModelRenderer(this, 87, 22);
      Pole.addBox(0F, 0F, 0F, 10, 32, 10);
      Pole.setRotationPoint(-5F, -16F, -5F);
      Pole.setTextureSize(256, 128);
      Pole.mirror = true;
      setRotation(Pole, 0F, 0F, 0F);
      Generator = new ModelRenderer(this, 15, 47);
      Generator.addBox(0F, 0F, 0F, 8, 8, 12);
      Generator.setRotationPoint(-4F, -24F, -7F);
      Generator.setTextureSize(256, 128);
      Generator.mirror = true;
      setRotation(Generator, 0F, 0F, 0F);
      BladeHub = new ModelRenderer(this, 62, 21);
      BladeHub.addBox(0F, 0F, 0F, 4, 4, 2);
      BladeHub.setRotationPoint(-2F, -22F, 6F);
      BladeHub.setTextureSize(256, 128);
      BladeHub.mirror = true;
      setRotation(BladeHub, 0F, 0F, 0F);
      Blade1 = new ModelRenderer(this, 141, 10);
      Blade1.addBox(0F, 0F, 0F, 2, 20, 2);
      Blade1.setRotationPoint(0.25F, -20.5F, 5F);
      Blade1.setTextureSize(256, 128);
      Blade1.mirror = true;
      setRotation(Blade1, 0F, 0F, 0F);
      Blade2 = new ModelRenderer(this, 135, 0);
      Blade2.addBox(0F, 0F, 0F, 20, 2, 2);
      Blade2.setRotationPoint(0.25F, -20.5F, 5F);
      Blade2.setTextureSize(256, 128);
      Blade2.mirror = true;
      setRotation(Blade2, 0F, 0F, -135F);
      Blade3 = new ModelRenderer(this, 135, 0);
      Blade3.addBox(0F, 0F, 0F, 20, 2, 2);
      Blade3.setRotationPoint(0.25F, -20.5F, 5F);
      Blade3.setTextureSize(256, 128);
      Blade3.mirror = true;
      setRotation(Blade3, 0F, 0F, -45F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    BaseX.render(f5);
    BaseY.render(f5);
    BaseCenter.render(f5);
    Pole.render(f5);
    Generator.render(f5);
    BladeHub.render(f5);
    Blade1.render(f5);
    Blade2.render(f5);
    Blade3.render(f5);
  }
  
  public void spin()
  {
	  
	  setRotation(Blade1, 0f, 0f, Blade1.rotateAngleZ + 0.1f);
	  setRotation(Blade2, 0f, 0f, Blade2.rotateAngleZ + 0.1f);
	  setRotation(Blade3, 0f, 0f, Blade3.rotateAngleZ + 0.1f);
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
