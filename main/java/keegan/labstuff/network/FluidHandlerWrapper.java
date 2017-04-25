package keegan.labstuff.network;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;

public class FluidHandlerWrapper implements IFluidHandler
{
	public IFluidHandlerWrapper wrapper;
	
	public EnumFacing side;
	
	public FluidHandlerWrapper(IFluidHandlerWrapper w, EnumFacing s)
	{
		wrapper = w;
		side = s;
	}

	@Override
	public IFluidTankProperties[] getTankProperties() 
	{
		return wrapper.getTankInfo(side) != null ? FluidTankProperties.convert(wrapper.getTankInfo(side))
				: new IFluidTankProperties[]{};
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) 
	{
		if(wrapper.canFill(side, resource != null ? resource.getFluid() : null))
		{
			return wrapper.fill(side, resource, doFill);
		}
		
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) 
	{
		if(wrapper.canDrain(side, resource != null ? resource.getFluid() : null))
		{
			return wrapper.drain(side, resource, doDrain);
		}
		
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) 
	{
		if(wrapper.canDrain(side, null))
		{
			return wrapper.drain(side, maxDrain, doDrain);
		}
		
		return null;
	}
}