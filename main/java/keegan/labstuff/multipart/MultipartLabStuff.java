package keegan.labstuff.multipart;

import mcmultipart.multipart.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class MultipartLabStuff implements IPartFactory
{
	public MultipartLabStuff()
	{
		init();
	}

	public void init()
	{
		MultipartRegistry.registerPartFactory(this, "labstuff:powercable", "labstuff:liquidpipe", "labstuff:plasmapipe", "labstuff:datacable");
		registerMicroMaterials();
	}

	@Override
	public IMultipart createPart(ResourceLocation resource, boolean client)
	{
		String name = resource.toString();
		
		if(name.equals("labstuff:powercable"))
		{
			return new PartPowerCable();
		}
		if(name.equals("labstuff:liquidpipe"))
		{
			return new PartLiquidPipe();
		}
		if(name.equals("labstuff:plasmapipe"))
		{
			return new PartPlasmaPipe();
		}
		if(name.equals("labstuff:datacable"))
			return new PartDataCable();

		return null;
	}

	public void registerMicroMaterials()
	{}
	
	public static void dropItem(ItemStack stack, Multipart multipart)
	{
		EntityItem item = new EntityItem(multipart.getWorld(), multipart.getPos().getX()+0.5, multipart.getPos().getY()+0.5, multipart.getPos().getZ()+0.5, stack);
        item.motionX = multipart.getWorld().rand.nextGaussian() * 0.05;
        item.motionY = multipart.getWorld().rand.nextGaussian() * 0.05 + 0.2;
        item.motionZ = multipart.getWorld().rand.nextGaussian() * 0.05;
        item.setDefaultPickupDelay();
        multipart.getWorld().spawnEntityInWorld(item);
	}
	
	public static void dropItems(Multipart multipart)
	{
		for(ItemStack stack : multipart.getDrops())
		{
			dropItem(stack, multipart);
		}
	}
	
	public static AxisAlignedBB rotate(AxisAlignedBB aabb, EnumFacing side) 
	{
        Vec3d v1 = rotate(new Vec3d(aabb.minX, aabb.minY, aabb.minZ), side);
        Vec3d v2 = rotate(new Vec3d(aabb.maxX, aabb.maxY, aabb.maxZ), side);
        
        return new AxisAlignedBB(v1.xCoord, v1.yCoord, v1.zCoord, v2.xCoord, v2.yCoord, v2.zCoord);
    }

    public static Vec3d rotate(Vec3d vec, EnumFacing side)
    {
        switch(side) 
        {
	        case DOWN:
	            return new Vec3d(vec.xCoord, vec.yCoord, vec.zCoord);
	        case UP:
	            return new Vec3d(vec.xCoord, -vec.yCoord, -vec.zCoord);
	        case NORTH:
	            return new Vec3d(vec.xCoord, -vec.zCoord, vec.yCoord);
	        case SOUTH:
	            return new Vec3d(vec.xCoord, vec.zCoord, -vec.yCoord);
	        case WEST:
	            return new Vec3d(vec.yCoord, -vec.xCoord, vec.zCoord);
	        case EAST:
	            return new Vec3d(-vec.yCoord, vec.xCoord, vec.zCoord);
        }
        
        return null;
    }
}