package keegan.labstuff.world;

import net.minecraft.entity.Entity;

public interface IZeroGDimension
{
    boolean inFreefall(Entity entity);
    
    void setInFreefall(Entity entity);
}