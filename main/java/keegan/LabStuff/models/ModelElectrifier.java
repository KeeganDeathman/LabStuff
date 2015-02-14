package keegan.labstuff.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelElectrifier extends ModelBase
{
  //fields
    ModelRenderer Water_bay;
    ModelRenderer Pipe1;
    ModelRenderer Pipe2;
    ModelRenderer outputPos;
    ModelRenderer outputNeg;
  
  public ModelElectrifier()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Water_bay = new ModelRenderer(this, 0, 0);
      Water_bay.addBox(0F, 0F, 0F, 6, 6, 6);
      Water_bay.setRotationPoint(-3F, 6F, -3F);
      Water_bay.setTextureSize(64, 32);
      Water_bay.mirror = true;
      setRotation(Water_bay, 0F, 0F, 0F);
      Pipe1 = new ModelRenderer(this, 0, 14);
      Pipe1.addBox(0F, 0F, 0F, 4, 10, 4);
      Pipe1.setRotationPoint(-2F, 12F, -2F);
      Pipe1.setTextureSize(64, 32);
      Pipe1.mirror = true;
      setRotation(Pipe1, 0F, 0F, 0F);
      Pipe2 = new ModelRenderer(this, 29, 0);
      Pipe2.addBox(0F, 0F, 0F, 14, 2, 4);
      Pipe2.setRotationPoint(-7F, 22F, -2F);
      Pipe2.setTextureSize(64, 32);
      Pipe2.mirror = true;
      setRotation(Pipe2, 0F, 0F, 0F);
      outputPos = new ModelRenderer(this, 30, 20);
      outputPos.addBox(0F, 0F, 0F, 1, 1, 3);
      outputPos.setRotationPoint(-7F, 23F, 2F);
      outputPos.setTextureSize(64, 32);
      outputPos.mirror = true;
      setRotation(outputPos, 0F, 0F, 0F);
      outputNeg = new ModelRenderer(this, 20, 20);
      outputNeg.addBox(0F, 0F, 0F, 1, 1, 3);
      outputNeg.setRotationPoint(6F, 23F, 2F);
      outputNeg.setTextureSize(64, 32);
      outputNeg.mirror = true;
      setRotation(outputNeg, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5,entity);
    Water_bay.render(f5);
    Pipe1.render(f5);
    Pipe2.render(f5);
    outputPos.render(f5);
    outputNeg.render(f5);
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
