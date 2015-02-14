package keegan.labstuff.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelCzo extends ModelBase
{
  //fields
    ModelRenderer Base;
    ModelRenderer LeftWall;
    ModelRenderer RightWall;
    ModelRenderer Back;
    ModelRenderer Top;
    ModelRenderer GlassLeft;
    ModelRenderer GlassRight;
    ModelRenderer GlassFront;
    ModelRenderer GlassBack;
  
  public ModelCzo()
  {
    textureWidth = 128;
    textureHeight = 64;
    
      Base = new ModelRenderer(this, 0, 0);
      Base.addBox(0F, 0F, 0F, 16, 1, 16);
      Base.setRotationPoint(-8F, 23F, -8F);
      Base.setTextureSize(128, 64);
      Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      LeftWall = new ModelRenderer(this, 67, 0);
      LeftWall.addBox(0F, 0F, 0F, 3, 15, 16);
      LeftWall.setRotationPoint(-8F, 8F, -8F);
      LeftWall.setTextureSize(128, 64);
      LeftWall.mirror = true;
      setRotation(LeftWall, 0F, 0F, 0F);
      RightWall = new ModelRenderer(this, 67, 0);
      RightWall.addBox(0F, 0F, 0F, 3, 15, 16);
      RightWall.setRotationPoint(5F, 8F, -8F);
      RightWall.setTextureSize(128, 64);
      RightWall.mirror = true;
      setRotation(RightWall, 0F, 0F, 0F);
      Back = new ModelRenderer(this, 60, 37);
      Back.addBox(0F, 0F, 0F, 10, 15, 7);
      Back.setRotationPoint(-5F, 8F, 1F);
      Back.setTextureSize(128, 64);
      Back.mirror = true;
      setRotation(Back, 0F, 0F, 0F);
      Top = new ModelRenderer(this, 25, 29);
      Top.addBox(0F, 0F, 0F, 10, 7, 9);
      Top.setRotationPoint(-5F, 8F, -8F);
      Top.setTextureSize(128, 64);
      Top.mirror = true;
      setRotation(Top, 0F, 0F, 0F);
      GlassLeft = new ModelRenderer(this, 1, 47);
      GlassLeft.addBox(0F, 0F, 0F, 1, 8, 6);
      GlassLeft.setRotationPoint(-3F, 15F, -6F);
      GlassLeft.setTextureSize(128, 64);
      GlassLeft.mirror = true;
      setRotation(GlassLeft, 0F, 0F, 0F);
      GlassRight = new ModelRenderer(this, 1, 47);
      GlassRight.addBox(0F, 0F, 0F, 1, 8, 6);
      GlassRight.setRotationPoint(2F, 15F, -6F);
      GlassRight.setTextureSize(128, 64);
      GlassRight.mirror = true;
      setRotation(GlassRight, 0F, 0F, 0F);
      GlassFront = new ModelRenderer(this, 1, 31);
      GlassFront.addBox(0F, 0F, 0F, 6, 8, 1);
      GlassFront.setRotationPoint(-3F, 15F, -7F);
      GlassFront.setTextureSize(128, 64);
      GlassFront.mirror = true;
      setRotation(GlassFront, 0F, 0F, 0F);
      GlassBack = new ModelRenderer(this, 1, 31);
      GlassBack.addBox(0F, 0F, 0F, 6, 8, 1);
      GlassBack.setRotationPoint(-3F, 15F, 0F);
      GlassBack.setTextureSize(128, 64);
      GlassBack.mirror = true;
      setRotation(GlassBack, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Base.render(f5);
    LeftWall.render(f5);
    RightWall.render(f5);
    Back.render(f5);
    Top.render(f5);
    GlassLeft.render(f5);
    GlassRight.render(f5);
    GlassFront.render(f5);
    GlassBack.render(f5);
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
