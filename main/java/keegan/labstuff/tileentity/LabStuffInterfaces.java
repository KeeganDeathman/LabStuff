package keegan.labstuff.tileentity;

import java.util.Collection;

import javax.annotation.Nullable;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;

public class LabStuffInterfaces
{

	public interface IDirectionalTile
	{
		EnumFacing getFacing();
		void setFacing(EnumFacing facing);
		/**
		 * @return 0 = side clicked, 1=piston behaviour,  2 = horizontal, 3 = vertical, 4 = x/z axis, 5 = horizontal based on quadrant
		 */
		int getFacingLimitation();
		default EnumFacing getFacingForPlacement(EntityLivingBase placer, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
		{
			EnumFacing f = EnumFacing.DOWN;
			int limit = getFacingLimitation();
			if(limit==0)
				f = side;
			else if(limit==1)
				f = BlockPistonBase.getFacingFromEntity(pos, placer);
			else if(limit==2)
				f = EnumFacing.fromAngle(placer.rotationYaw);
			else if(limit==3)
				f = (side!=EnumFacing.DOWN&&(side==EnumFacing.UP||hitY<=.5))?EnumFacing.UP : EnumFacing.DOWN;
			else if(limit==4)
			{
				f = EnumFacing.fromAngle(placer.rotationYaw);
				if(f==EnumFacing.SOUTH || f==EnumFacing.WEST)
					f = f.getOpposite();
			} else if(limit == 5)
			{
				if(side.getAxis() != Axis.Y)
					f = side.getOpposite();
				else
				{
					float xFromMid = hitX - .5f;
					float zFromMid = hitZ - .5f;
					float max = Math.max(Math.abs(xFromMid), Math.abs(zFromMid));
					if(max == Math.abs(xFromMid))
						f = xFromMid < 0 ? EnumFacing.WEST : EnumFacing.EAST;
					else
						f = zFromMid < 0 ? EnumFacing.NORTH : EnumFacing.SOUTH;
				}
			}
			return mirrorFacingOnPlacement(placer)?f.getOpposite():f;
		}
		boolean mirrorFacingOnPlacement(EntityLivingBase placer);
		boolean canHammerRotate(EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity);
		boolean canRotate(EnumFacing axis);
		default void afterRotation(EnumFacing oldDir, EnumFacing newDir){}
	}

	public interface IBlockBounds
	{
		float[] getBlockBounds();
	}
	
	public interface ITileDrop
	{
		ItemStack getTileDrop(EntityPlayer player, IBlockState state);

		void readOnPlacement(@Nullable EntityLivingBase placer, ItemStack stack);

		default boolean preventInventoryDrop() { return false; }
	}
	
	public interface IUsesBooleanProperty
	{
		PropertyBoolInverted getBoolProperty(Class<? extends IUsesBooleanProperty> inf);
	}
	
	public interface IMirrorAble extends IUsesBooleanProperty
	{
		boolean getIsMirrored();
	}
	

	public static class PropertyBoolInverted extends PropertyHelper<Boolean>
	{
		private final ImmutableSet<Boolean> allowedValues = ImmutableSet.of(Boolean.valueOf(false), Boolean.valueOf(true));
		protected PropertyBoolInverted(String name)
		{
			super(name, Boolean.class);
		}
		@Override
		public Collection<Boolean> getAllowedValues()
		{
			return this.allowedValues;
		}
		@Override
		public Optional<Boolean> parseValue(String value)
		{
			return Optional.of(Boolean.getBoolean(value));
		}
		public static PropertyBoolInverted create(String name)
		{
			return new PropertyBoolInverted(name);
		}
		@Override
		public String getName(Boolean value)
		{
			return value.toString();
		}
	}
	
	public static final PropertyBoolInverted[] BOOLEANS = {
			PropertyBoolInverted.create("boolean0"),
			PropertyBoolInverted.create("boolean1"),
			PropertyBoolInverted.create("boolean2")
	};
}