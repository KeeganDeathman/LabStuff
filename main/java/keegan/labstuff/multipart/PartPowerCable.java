package keegan.labstuff.multipart;

import java.util.*;

import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import mcmultipart.raytrace.PartMOP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PartPowerCable extends PartTransmitter<EnergyAcceptorWrapper, EnergyNetwork> implements IStrictEnergyAcceptor
{

	public double currentPower = 0;
	public int lastWrite = 0;

	public int buffer = 0;

	public PartPowerCable()
	{
		super();
	}

	@Override
	public void update()
	{
		clamp();
		if(getWorld().isRemote)
		{
			double targetPower = getTransmitter().hasTransmitterNetwork() ? getTransmitter().getTransmitterNetwork().clientEnergyScale : 0;

			if(Math.abs(currentPower - targetPower) > 0.01)
			{
				currentPower = (9 * currentPower + targetPower) / 10;
			}
		} 
		else {
			updateShare();

			List<EnumFacing> sides = getConnections(ConnectionType.PULL);

			if(!sides.isEmpty())
			{
				TileEntity[] connectedOutputters = CableUtils.getConnectedOutputters(getPos(), getWorld());
				double canDraw = 1000F;

				for(EnumFacing side : sides)
				{
					if(connectedOutputters[side.ordinal()] != null)
					{
						TileEntity outputter = connectedOutputters[side.ordinal()];

						if(CapabilityUtils.hasCapability(outputter, Capabilities.CABLE_OUTPUTTER_CAPABILITY, side.getOpposite()) && CapabilityUtils.hasCapability(outputter, Capabilities.ENERGY_STORAGE_CAPABILITY, side.getOpposite()))
						{
							IStrictEnergyStorage storage = CapabilityUtils.getCapability(outputter, Capabilities.ENERGY_STORAGE_CAPABILITY, side.getOpposite());
							double received = Math.min(storage.getEnergy(), canDraw);
							double toDraw = received;

							if(received > 0)
							{
								toDraw -= takeEnergy(received, true);
							}

							storage.setEnergy(storage.getEnergy() - toDraw);
						}
					}
				}
			}
		}

		super.update();
	}
	
	public void clamp()
	{
		if(getTransmitter().hasTransmitterNetwork())
			this.getTransmitter().getTransmitterNetwork().clampBuffer();
		else if(buffer > getCapacity())
			buffer = getCapacity();
		else if(buffer < 0)
			buffer = 0;if(getTransmitter().hasTransmitterNetwork())
				this.getTransmitter().getTransmitterNetwork().clampBuffer();
			else if(buffer > getCapacity())
				buffer = getCapacity();
			else if(buffer < 0)
				buffer = 0;
	}

    @Override
    public void updateShare()
    {
        if(getTransmitter().hasTransmitterNetwork() && getTransmitter().getTransmitterNetworkSize() > 0)
        {
            int last = getSaveShare();

            if(last != lastWrite)
            {
                lastWrite = last;
                markDirty();
            }
        }
    }

	private int getSaveShare()
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			return (int) EnergyNetwork.round(getTransmitter().getTransmitterNetwork().buffer * (1F / getTransmitter().getTransmitterNetwork().transmitters.size()));
		}
		else {
			return buffer;
		}
	}

	@Override
	public TransmitterType getTransmitterType()
	{
		return TransmitterType.POWERCABLE;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags)
	{
		super.readFromNBT(nbtTags);

		buffer = nbtTags.getInteger("cacheEnergy");
		if(buffer < 0) buffer = 0;

	}
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack stack, PartMOP hit)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			if(this.getTransmitter().getTransmitterNetwork() != null)
			{
				player.addChatMessage(new TextComponentString("Network is holding " + this.getTransmitter().getTransmitterNetwork().buffer + " LV"));
			}
			else
			{
				player.addChatMessage(new TextComponentString("Cable is holding " + this.buffer + " LV"));
			}
		}
		return false;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTags)
	{
		super.writeToNBT(nbtTags);
		
		nbtTags.setDouble("cacheEnergy", lastWrite);
		
		return nbtTags;
	}

	@Override
	public ResourceLocation getType()
	{
		return new ResourceLocation("labstuff:powercable");
	}

	@Override
	public TransmissionType getTransmissionType()
	{
		return TransmissionType.ENERGY;
	}

	@Override
	public EnergyNetwork createNetworkByMerging(Collection<EnergyNetwork> networks)
	{
		return new EnergyNetwork(networks);
	}

	@Override
	public boolean isValidAcceptor(TileEntity acceptor, EnumFacing side)
	{
		return CableUtils.isValidAcceptorOnSide(getWorld().getTileEntity(getPos()), acceptor, side);
	}

	@Override
	public EnergyNetwork createNewNetwork()
	{
		return new EnergyNetwork();
	}

	@Override
	public Object getBuffer()
	{
		this.clamp();
		return buffer;
	}

	@Override
	public void takeShare()
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			getTransmitter().getTransmitterNetwork().clampBuffer();
			getTransmitter().getTransmitterNetwork().buffer -= lastWrite;
			buffer = lastWrite;
		}
	}


	@Override
	public int getCapacity()
	{
		return Integer.MAX_VALUE - 20;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount)
	{
		this.clamp();
		if(!canReceiveEnergy(side))
		{
			return 0;
		}

		double toUse = Math.min(getMaxEnergy() - getEnergy(), amount);
		setEnergy(getEnergy() + toUse);

		return toUse;
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side)
	{
		return getConnectionType(side) == ConnectionType.NORMAL;
	}

	@Override
	public double getMaxEnergy()
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			return getTransmitter().getTransmitterNetwork().getCapacity();
		} 
		else {
			return getCapacity();
		}
	}

	@Override
	public double getEnergy()
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			return getTransmitter().getTransmitterNetwork().buffer;
		} 
		else {
			return buffer;
		}
	}

	@Override
	public void setEnergy(double energy)
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			getTransmitter().getTransmitterNetwork().buffer = (int) energy;
		} 
		else {
			buffer = (int) energy;
		}
		
		this.clamp();
	}
	

	public double takeEnergy(double energy, boolean doEmit)
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			return getTransmitter().getTransmitterNetwork().emit(energy, doEmit);
		}
		else {
			double used = Math.min(getCapacity() - buffer, energy);
			
			if(doEmit)
			{
				buffer += used;
			}
			
			return energy - used;
		}
	}

	@Override
	public EnergyAcceptorWrapper getCachedAcceptor(EnumFacing side)
	{
		return EnergyAcceptorWrapper.get(getCachedTile(side), side.getOpposite());
	}
	
	@Override
	public void readUpdatePacket(PacketBuffer packet)
	{
		
		super.readUpdatePacket(packet);
	}

	@Override
	public void writeUpdatePacket(PacketBuffer packet)
	{
		
		super.writeUpdatePacket(packet);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.ENERGY_STORAGE_CAPABILITY
				|| capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY)
		{
			return (T)this;
		}
		
		return super.getCapability(capability, facing);
	}
}