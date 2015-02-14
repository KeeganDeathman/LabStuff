package keegan.labstuff.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSolarPanel extends ModelBase
{
  //fields
    ModelRenderer Panel;
    ModelRenderer Support;
    ModelRenderer Base;
    ModelRenderer Pole;
    ModelRenderer Hookup;
  
  public ModelSolarPanel()
  {
    textureWidth = 256;
    textureHeight = 128;
    
      Panel = new ModelRenderer(this, 0, 0);
      Panel.addBox(0F, 0F, 0F, 48, 1, 48);
      Panel.setRotationPoint(-24F, 7F, -24F);
      Panel.setTextureSize(256, 128);
      Panel.mirror = true;
      setRotation(Panel, 0F, 0F, 0F);
      Support = new ModelRenderer(this, 0, 52);
      Support.addBox(0F, 0F, 0F, 40, 1, 40);
      Support.setRotationPoint(-20F, 8F, -20F);
      Support.setTextureSize(256, 128);
      Support.mirror = true;
      setRotation(Support, 0F, 0F, 0F);
      Base = new ModelRenderer(this, 187, 63);
      Base.addBox(0F, 0F, 0F, 16, 4, 16);
      Base.setRotationPoint(-8F, 20F, -8F);
      Base.setTextureSize(256, 128);
      Base.mirror = true;
      setRotation(Base, 0F, 0F, 0F);
      Pole = new ModelRenderer(this, 205, 0);
      Pole.addBox(0F, 0F, 0F, 1, 11, 1);
      Pole.setRotationPoint(0F, 9F, 0F);
      Pole.setTextureSize(256, 128);
      Pole.mirror = true;
      setRotation(Pole, 0F, 0F, 0F);
      //Hookup = new ModelRenderer(this, 100, 100);
      //Hookup.addBox(0F, 0F, 0F, 8, 8, 3);
      //Hookup.setRotationPoint(-4F, 16F, 5F);
      //Hookup.setTextureSize(256, 128);
      //Hookup.mirror = true;
      //setRotation(Hookup, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    Panel.render(f5);
    Support.render(f5);
    Base.render(f5);
    Pole.render(f5);
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
