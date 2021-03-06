package keegan.labstuff.network;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;

public interface IFluidHandlerWrapper 
{
    int fill(EnumFacing from, FluidStack resource, boolean doFill);

    FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain);

    FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain);

    boolean canFill(EnumFacing from, Fluid fluid);

    boolean canDrain(EnumFacing from, Fluid fluid);
    
    FluidTankInfo[] getTankInfo(EnumFacing from);
}