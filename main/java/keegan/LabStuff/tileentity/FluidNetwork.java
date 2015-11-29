package keegan.labstuff.tileentity;

import java.util.ArrayList;

import net.minecraftforge.fluids.FluidStack;

public class FluidNetwork
{
	
	public ArrayList<FluidHandler> pipes;
	
	public FluidNetwork(FluidHandler pipe)
	{
		pipes = new ArrayList<FluidHandler>();
		pipes.add(pipe);
	}
	
	public void merge(FluidHandler network)
	{
		if(pipes.get(0).tank.getFluid() == null || network.network.pipes.get(0).tank.getFluid() == null ||pipes.get(0).tank.getFluid().isFluidEqual(network.network.pipes.get(0).tank.getFluid()))
		{
			for(FluidHandler pipe: network.network.pipes)
			{
				pipes.add(pipe);
			}
			network.network = this;
		}
	}

	public int fill(FluidStack resource, boolean doFill)
	{
		int volume = 0;
		int maxVolume = 0;
		FluidStack resourceN = resource;
		for(FluidHandler pipe: pipes)
		{
			volume += pipe.tank.getFluidAmount();
			maxVolume += pipe.tank.getCapacity();
		}
		if(volume + resourceN.amount > maxVolume)
		{
			resourceN.amount -= (maxVolume-(volume+resource.amount));
		}
		int perTank = resourceN.amount/pipes.size();
		for(FluidHandler pipe:pipes)
		{
			FluidStack resourceM = new FluidStack(resourceN.getFluid(), perTank);
			pipe.tank.fill(resourceM, doFill);
		}
		return resourceN.amount;
	}

	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		int volume = 0;
		FluidStack resourceN = resource;
		FluidStack drained = new FluidStack(resource.getFluid(), 0);
		for(FluidHandler pipe: pipes)
		{
			volume = pipe.tank.getFluidAmount();
		}
		if(volume - resourceN.amount < 0)
		{
			resourceN.amount -= volume-resource.amount;
		}
		int perTank = resourceN.amount/pipes.size();
		for(FluidHandler pipe:pipes)
		{
			pipe.tank.drain(perTank, doDrain);
		}
		
		return new FluidStack(resourceN.getFluid(), resourceN.amount);
	}
	
	
}
