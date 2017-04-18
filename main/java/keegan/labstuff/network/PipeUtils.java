package keegan.labstuff.network;

import java.util.*;

import keegan.labstuff.common.capabilities.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.fluids.capability.IFluidHandler;

public final class PipeUtils
{
	public static final FluidTankInfo[] EMPTY = new FluidTankInfo[] {};

	public static boolean isValidAcceptorOnSide(TileEntity tile, EnumFacing side)
	{
		if(tile == null || CapabilityUtils.hasCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite()) || 
				!CapabilityUtils.hasCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite()))
		{
			return false;
		}

		IFluidHandler container = CapabilityUtils.getCapability(tile, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
		
		if(container == null)
		{
			return false;
		}
		
		IFluidTankProperties[] infoArray = container.getTankProperties();

		if(infoArray != null && infoArray.length > 0)
		{
			for(IFluidTankProperties info : infoArray)
			{
				if(info != null)
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public static String localizeFluidStack(FluidStack fluidStack)
	{
		return (fluidStack == null || fluidStack.getFluid() == null ) ? null : fluidStack.getFluid().getLocalizedName(fluidStack);
	}

	
	
	/**
	 * Gets all the acceptors around a tile entity.
	 * @param tileEntity - center tile entity
	 * @return array of IFluidHandlers
	 */
	public static IFluidHandler[] getConnectedAcceptors(TileEntity tileEntity)
	{
		return getConnectedAcceptors(tileEntity.getPos(), tileEntity.getWorld());
	}

	public static IFluidHandler[] getConnectedAcceptors(BlockPos pos, World world)
	{
		IFluidHandler[] acceptors = new IFluidHandler[] {null, null, null, null, null, null};

		for(EnumFacing orientation : EnumFacing.VALUES)
		{
			TileEntity acceptor = world.getTileEntity(pos.offset(orientation));

			if(acceptor != null && CapabilityUtils.hasCapability(acceptor, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, orientation.getOpposite()))
			{
				IFluidHandler handler = CapabilityUtils.getCapability(acceptor, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, orientation.getOpposite());
				acceptors[orientation.ordinal()] = handler;
			}
		}

		return acceptors;
	}
	
	/**
	 * Emits fluid from a central block by splitting the received stack among the sides given.
	 * @param sides - the list of sides to output from
	 * @param stack - the stack to output
	 * @param from - the TileEntity to output from
	 * @return the amount of gas emitted
	 */
	public static int emit(List<EnumFacing> sides, FluidStack stack, TileEntity from)
	{
		if(stack == null)
		{
			return 0;
		}
		
		List<IFluidHandler> availableAcceptors = new ArrayList<IFluidHandler>();
		IFluidHandler[] possibleAcceptors = getConnectedAcceptors(from);
		
		for(int i = 0; i < possibleAcceptors.length; i++)
		{
			IFluidHandler handler = possibleAcceptors[i];
			
			if(handler != null && canFill(handler, stack))
			{
				availableAcceptors.add(handler);
			}
		}

		Collections.shuffle(availableAcceptors);

		int toSend = stack.amount;
		int prevSending = toSend;

		if(!availableAcceptors.isEmpty())
		{
			int divider = availableAcceptors.size();
			int remaining = toSend % divider;
			int sending = (toSend-remaining)/divider;

			for(IFluidHandler acceptor : availableAcceptors)
			{
				int currentSending = sending;

				if(remaining > 0)
				{
					currentSending++;
					remaining--;
				}
				
				EnumFacing dir = EnumFacing.getFront(Arrays.asList(possibleAcceptors).indexOf(acceptor)).getOpposite();
				toSend -= acceptor.fill(copy(stack, currentSending), true);
			}
		}

		return prevSending-toSend;
	}
	
	public static FluidStack copy(FluidStack fluid, int amount)
	{
		FluidStack ret = fluid.copy();
		ret.amount = amount;
		
		return ret;
	}
	
	public static boolean canFill(IFluidHandler handler, FluidStack stack)
	{
		for(IFluidTankProperties props : handler.getTankProperties())
		{
			if(props.canFillFluidType(stack))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean canDrain(IFluidHandler handler, FluidStack stack)
	{
		for(IFluidTankProperties props : handler.getTankProperties())
		{
			if(props.canDrainFluidType(stack))
			{
				return true;
			}
		}
		
		return false;
	}
}