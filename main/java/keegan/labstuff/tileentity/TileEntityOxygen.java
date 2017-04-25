package keegan.labstuff.tileentity;

import java.util.*;

import io.netty.buffer.ByteBuf;
import keegan.ditty.Annotations.NetworkedField;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.IPacketReceiver;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

public abstract class TileEntityOxygen extends TileEntityAdvanced implements IFluidHandlerWrapper, IPacketReceiver, IDisableableMachine, ITickable, IStrictEnergyStorage, IStrictEnergyAcceptor
{
    public int oxygenPerTick;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank tank;
    public float lastStoredOxygen;
    public static int timeSinceOxygenRequest;
    public boolean noRedstoneControl;
    public double buffer = 0;

    public TileEntityOxygen(int maxOxygen, int oxygenPerTick)
    {
        this.tank = new FluidTank(maxOxygen);
        this.oxygenPerTick = oxygenPerTick;
    }

    
    public void addExtraNetworkedData(List<Object> networkedList)
    {
    }

    public void readExtraNetworkedData(ByteBuf dataStream)
    {
    }


    @Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.ENERGY_STORAGE_CAPABILITY
				|| capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				|| capability == Capabilities.DATA_DEVICE_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	public CapabilityWrapperManager manager = new CapabilityWrapperManager(IFluidHandlerWrapper.class, FluidHandlerWrapper.class);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return (T)manager.getWrapper(this, side);
		}
		else if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY || capability == Capabilities.DATA_DEVICE_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}
    
    @Override
    public void update()
    {

        if (!this.worldObj.isRemote)
        {
            if (TileEntityOxygen.timeSinceOxygenRequest > 0)
            {
                TileEntityOxygen.timeSinceOxygenRequest--;
            }


            if (this.tank.getFluid() != null)
            {
                FluidStack fluid = this.tank.getFluid().copy();
                fluid.amount = Math.max(fluid.amount - this.oxygenPerTick, 0);
                this.tank.setFluid(fluid);
            }
        
        }

        this.lastStoredOxygen = this.tank.getFluidAmount();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("storedOxygen"))
        {
            this.tank.setFluid(new FluidStack(LabStuffMain.LO2, nbt.getInteger("storedOxygen")));
        }
        else if (nbt.hasKey("storedOxygenF"))
        {
            int oxygen = (int) nbt.getFloat("storedOxygenF");
            oxygen = Math.min(this.tank.getCapacity(), oxygen);
            this.tank.setFluid(new FluidStack(LabStuffMain.LO2, oxygen));
        }
        else
        {
            this.tank.readFromNBT(nbt);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.tank.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
       return tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return drain(from, resource.amount, doDrain);
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
    	return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return fluid.getName().equals(LabStuffMain.LO2.getName());
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return fluid.getName().equals(LabStuffMain.LO2.getName());
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
    	return new FluidTankInfo[]{tank.getInfo()};
    }


	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return buffer += amount;
	}


	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return buffer;
	}


	@Override
	public void setEnergy(double energy) {
		buffer = energy;
	}


	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 100000;
	}


	@Override
	public void setDisabled(int index, boolean disabled) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean getDisabled(int index) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void getNetworkedData(ArrayList<Object> sendData) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void decodePacketdata(ByteBuf buffer) {
		// TODO Auto-generated method stub
		
	}


}