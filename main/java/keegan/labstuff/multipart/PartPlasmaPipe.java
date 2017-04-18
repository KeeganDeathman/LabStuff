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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PartPlasmaPipe extends PartTransmitter<PlasmaHandlerWrapper, PlasmaNetwork> implements IPlasmaHandler
{

	public double currentPlasma = 0;
	public int lastWrite = 0;

	public int buffer = 0;

	public PartPlasmaPipe()
	{
		super();
	}

	@Override
	public void update()
	{
		if(getWorld().isRemote)
		{
			double targetPower = getTransmitter().hasTransmitterNetwork() ? getTransmitter().getTransmitterNetwork().clientPlasmaScale : 0;

			if(Math.abs(currentPlasma - targetPower) > 0.01)
			{
				currentPlasma = (9 * currentPlasma + targetPower) / 10;
			}
		} 
		else {
			updateShare();

			List<EnumFacing> sides = getConnections(ConnectionType.PULL);

			if(!sides.isEmpty())
			{
				TileEntity[] connectedOutputters = getConnectedOutputters(getPos(), getWorld());
				double canDraw = 1000F;

				for(EnumFacing side : sides)
				{
					if(connectedOutputters[side.ordinal()] != null)
					{
						TileEntity outputter = connectedOutputters[side.ordinal()];

						if(CapabilityUtils.hasCapability(outputter, Capabilities.PLASMA_HANDLER_CAPABILITY, side.getOpposite()) && CapabilityUtils.hasCapability(outputter, Capabilities.PLASMA_HANDLER_CAPABILITY, side.getOpposite()))
						{
							IPlasmaHandler storage = CapabilityUtils.getCapability(outputter, Capabilities.PLASMA_HANDLER_CAPABILITY, side.getOpposite());
							int received = (int) Math.min(storage.getPlasma(side), canDraw);
							int toDraw = received;

							if(received > 0)
							{
								toDraw -= takePlasma(received, true);
							}

							storage.setPlasma(storage.getPlasma(side) - toDraw, side);
						}
					}
				}
			}
		}

		super.update();
	}
	
	public static TileEntity[] getConnectedOutputters(BlockPos pos, World world)
	{
		TileEntity[] outputters = new TileEntity[] {null, null, null, null, null, null};

		for(EnumFacing orientation : EnumFacing.VALUES)
		{
			TileEntity outputter = world.getTileEntity(pos.offset(orientation));

			if(CapabilityUtils.hasCapability(outputter, Capabilities.PLASMA_HANDLER_CAPABILITY, orientation))
			{
				if(CapabilityUtils.getCapability(outputter, Capabilities.PLASMA_HANDLER_CAPABILITY, orientation).canConnectPlasma(orientation.getOpposite()))
					outputters[orientation.ordinal()] = outputter;
			}
		}

		return outputters;
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
			return (int) PlasmaNetwork.round(getTransmitter().getTransmitterNetwork().buffer * (1F / getTransmitter().getTransmitterNetwork().transmitters.size()));
		}
		else {
			return buffer;
		}
	}

	@Override
	public TransmitterType getTransmitterType()
	{
		return TransmitterType.PLASMAPIPE;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags)
	{
		super.readFromNBT(nbtTags);

		buffer = nbtTags.getInteger("cacheEnergy");

	}
	
	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack stack, PartMOP hit)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			if(this.getTransmitter().getTransmitterNetwork() != null)
			{
				player.addChatMessage(new TextComponentString("Network is holding " + this.getTransmitter().getTransmitterNetwork().getStoredInfo()));
			}
			else
			{
				player.addChatMessage(new TextComponentString("Cable is holding " + this.buffer + " PU"));
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
		return new ResourceLocation("labstuff:plasmapipe");
	}

	@Override
	public TransmissionType getTransmissionType()
	{
		return TransmissionType.PLASMA;
	}

	@Override
	public PlasmaNetwork createNetworkByMerging(Collection<PlasmaNetwork> networks)
	{
		return new PlasmaNetwork(networks);
	}

	@Override
	public boolean isValidAcceptor(TileEntity acceptor, EnumFacing side)
	{
		return CapabilityUtils.hasCapability(acceptor, Capabilities.PLASMA_HANDLER_CAPABILITY, side.getOpposite());
	}

	@Override
	public PlasmaNetwork createNewNetwork()
	{
		return new PlasmaNetwork();
	}

	@Override
	public Object getBuffer()
	{
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
	public int transferPlasma(int amount, EnumFacing side)
	{
		if(!canReceivePlasma(side))
		{
			return 0;
		}

		int toUse = (int) Math.min(getMaxPlasma() - getPlasma(side), amount);
		setPlasma(getPlasma(side) + toUse, side);

		return toUse;
	}

	@Override
	public boolean canReceivePlasma(EnumFacing side)
	{
		return getConnectionType(side) == ConnectionType.NORMAL;
	}

	public double getMaxPlasma()
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
	public int getPlasma(EnumFacing side)
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
	public void setPlasma(int plasma, EnumFacing side)
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			getTransmitter().getTransmitterNetwork().buffer = plasma;
		} 
		else {
			buffer = (int) plasma;
		}
		
	}
	

	public int takePlasma(int plasma, boolean doEmit)
	{
		if(getTransmitter().hasTransmitterNetwork())
		{
			return getTransmitter().getTransmitterNetwork().emit(plasma, doEmit);
		}
		else {
			int used = (int) Math.min(getCapacity() - buffer, plasma);
			
			if(doEmit)
			{
				buffer += used;
			}
			
			return plasma - used;
		}
	}

	@Override
	public PlasmaHandlerWrapper getCachedAcceptor(EnumFacing side)
	{
		return PlasmaHandlerWrapper.get(getCachedTile(side), side.getOpposite());
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
		return capability == Capabilities.PLASMA_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == Capabilities.PLASMA_HANDLER_CAPABILITY)
		{
			return (T)this;
		}
		
		return super.getCapability(capability, facing);
	}

	@Override
	public int drainPlasma(int amount, EnumFacing from) {
		// TODO Auto-generated method stub
		return takePlasma(amount, true);
	}

	@Override
	public boolean canDrainPlasma(EnumFacing from) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canConnectPlasma(EnumFacing from) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return EnumSet.allOf(EnumFacing.class);
	}
}