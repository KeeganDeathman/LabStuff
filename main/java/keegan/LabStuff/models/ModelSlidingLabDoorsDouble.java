package keegan.labstuff.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelSlidingLabDoorsDouble extends ModelBase
{
  //fields
    ModelRenderer ClosedLeftDoor;
    ModelRenderer ClosedRightDoor;
    ModelRenderer OpenLeftDoor;
    ModelRenderer OpenRightDoor;
  
  public ModelSlidingLabDoorsDouble()
  {
    textureWidth = 64;
    textureHeight = 128;
    
      ClosedLeftDoor = new ModelRenderer(this, 0, 0);
      ClosedLeftDoor.addBox(0F, 0F, 0F, 16, 32, 2);
      ClosedLeftDoor.setRotationPoint(-8F, -8F, -1F);
      ClosedLeftDoor.setTextureSize(64, 128);
      ClosedLeftDoor.mirror = true;
      setRotation(ClosedLeftDoor, 0F, 0F, 0F);
      ClosedRightDoor = new ModelRenderer(this, 0, 0);
      ClosedRightDoor.addBox(0F, 0F, 0F, 16, 32, 2);
      ClosedRightDoor.setRotationPoint(8F, -8F, -1F);
      ClosedRightDoor.setTextureSize(64, 128);
      ClosedRightDoor.mirror = true;
      setRotation(ClosedRightDoor, 0F, 0F, 0F);
      OpenLeftDoor = new ModelRenderer(this, 38, 0);
      OpenLeftDoor.addBox(0F, 0F, 0F, 1, 32, 2);
      OpenLeftDoor.setRotationPoint(-8F, -8F, -1F);
      OpenLeftDoor.setTextureSize(64, 128);
      OpenLeftDoor.mirror = true;
      setRotation(OpenLeftDoor, 0F, 0F, 0F);
      OpenRightDoor = new ModelRenderer(this, 38, 0);
      OpenRightDoor.addBox(0F, 0F, 0F, 1, 32, 2);
      OpenRightDoor.setRotationPoint(23F, -8F, -1F);
      OpenRightDoor.setTextureSize(64, 128);
      OpenRightDoor.mirror = true;
      setRotation(OpenRightDoor, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5,entity);
    ClosedLeftDoor.render(f5);
    ClosedRightDoor.render(f5);
    OpenLeftDoor.render(f5);
    OpenRightDoor.render(f5);
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
