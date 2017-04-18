package keegan.labstuff.network;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import keegan.labstuff.common.Coord4D;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EnergyNetwork extends DynamicNetwork<EnergyAcceptorWrapper, EnergyNetwork>
{
	private double lastPowerScale = 0;
	private double labVoltsTransmitted = 0;
	private double labVoltBufferLastTick = 0;

	public double clientEnergyScale = 0;

	public int buffer = 0;

	public EnergyNetwork() {}

	public EnergyNetwork(Collection<EnergyNetwork> networks)
	{
		for(EnergyNetwork net : networks)
		{
			if(net != null)
			{
				if(net.labVoltBufferLastTick > labVoltBufferLastTick || net.clientEnergyScale > clientEnergyScale)
				{
					clientEnergyScale = net.clientEnergyScale;
					labVoltBufferLastTick = net.labVoltBufferLastTick;
					labVoltsTransmitted = net.labVoltsTransmitted;
					lastPowerScale = net.lastPowerScale;
				}

				buffer += net.buffer;

				adoptTransmittersAndAcceptorsFrom(net);
				net.deregister();
			}
		}

		register();
	}
	
	public static double round(double d)
	{
		return Math.round(d * 10000)/10000;
	}

	@Override
	public void absorbBuffer(IGridTransmitter<EnergyAcceptorWrapper, EnergyNetwork> transmitter)
	{
		int energy = (int)transmitter.getBuffer();
		buffer += energy;
		energy = 0;
	}

	@Override
	public void clampBuffer()
	{
		if(buffer > getCapacity())
		{
			buffer = getCapacity();
		}

		if(buffer < 0)
		{
			buffer = 0;
		}
	}

	@Override
	protected void updateMeanCapacity()
	{
        int numCables = transmitters.size();
        double reciprocalSum = 0;
        
        for(IGridTransmitter<EnergyAcceptorWrapper, EnergyNetwork> cable : transmitters)
        {
            reciprocalSum += 1.0/(double)cable.getCapacity();
        }

        meanCapacity = (double)numCables / reciprocalSum;            
	}
    
	public double getEnergyNeeded()
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return 0;
		}

		return getCapacity()-buffer;
	}

	public double tickEmit(double energyToSend)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return 0;
		}

		double sent = 0;
		boolean tryAgain;
		int i = 0;

		do {
			double prev = sent;
			sent += doEmit(energyToSend-sent);

			tryAgain = energyToSend-sent > 0 && sent-prev > 0 && i < 100;

			i++;
		} while(tryAgain);

		labVoltsTransmitted = sent;
		
		return sent;
	}

	public double emit(double energyToSend, boolean doEmit)
	{
		double toUse = Math.min(getEnergyNeeded(), energyToSend);
		
		if(doEmit)
		{
			buffer += toUse;
		}
		
		return energyToSend-toUse;
	}

	/**
	 * @return sent
	 */
	public double doEmit(double energyToSend)
	{
		double sent = 0;

		List<Pair<Coord4D, EnergyAcceptorWrapper>> availableAcceptors = new ArrayList<>();
		availableAcceptors.addAll(getAcceptors(null));

		Collections.shuffle(availableAcceptors);

		if(!availableAcceptors.isEmpty())
		{
			int divider = availableAcceptors.size();
			double remaining = energyToSend % divider;
			double sending = (energyToSend-remaining)/divider;

			for(Pair<Coord4D, EnergyAcceptorWrapper> pair : availableAcceptors)
			{
				EnergyAcceptorWrapper acceptor = pair.getRight();
				double currentSending = sending+remaining;
				EnumSet<EnumFacing> sides = acceptorDirections.get(pair.getLeft());

				if(sides == null || sides.isEmpty())
				{
					continue;
				}

				for(EnumFacing side : sides)
				{
					double prev = sent;

					sent += acceptor.transferEnergyToAcceptor(side, currentSending);

					if(sent > prev)
					{
						break;
					}
				}
			}
		}

		return sent;
	}

	@Override
	public Set<Pair<Coord4D, EnergyAcceptorWrapper>> getAcceptors(Object data)
	{
		Set<Pair<Coord4D, EnergyAcceptorWrapper>> toReturn = new HashSet<>();

		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return toReturn;
		}

		for(Coord4D coord : possibleAcceptors.keySet())
		{
			EnumSet<EnumFacing> sides = acceptorDirections.get(coord);

			if(sides == null || sides.isEmpty())
			{
				continue;
			}

			TileEntity tile = coord.getTileEntity(getWorld());

			for(EnumFacing side : sides)
			{
				EnergyAcceptorWrapper acceptor = EnergyAcceptorWrapper.get(tile, side);
				
				if(acceptor != null)
				{
					if(acceptor.canReceiveEnergy(side) && acceptor.needsEnergy(side))
					{
						toReturn.add(Pair.of(coord, acceptor));
						break;
					}
				}
			}
		}

		return toReturn;
	}

	public static class EnergyTransferEvent extends Event
	{
		public final EnergyNetwork energyNetwork;

		public final double power;

		public EnergyTransferEvent(EnergyNetwork network, double currentPower)
		{
			energyNetwork = network;
			power = currentPower;
		}
	}

	@Override
	public String toString()
	{
		return "[EnergyNetwork] " + transmitters.size() + " transmitters, " + possibleAcceptors.size() + " acceptors.";
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		clearLabVoltsTransmitted();

		double currentPowerScale = getPowerScale();

		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			if(Math.abs(currentPowerScale-lastPowerScale) > 0.01 || (currentPowerScale != lastPowerScale && (currentPowerScale == 0 || currentPowerScale == 1)))
			{
				needsUpdate = true;
			}

			if(needsUpdate)
			{
				MinecraftForge.EVENT_BUS.post(new EnergyTransferEvent(this, currentPowerScale));
				lastPowerScale = currentPowerScale;
				needsUpdate = false;
			}

			if(buffer > 0)
			{
				buffer -= tickEmit(buffer);
			}
		}
	}

	public double getPowerScale()
	{
		return Math.max(labVoltBufferLastTick == 0 ? 0 : Math.min(Math.ceil(Math.log10(getPower())*2)/10, 1), getCapacity() == 0 ? 0 : buffer/getCapacity());
	}

	public void clearLabVoltsTransmitted()
	{
		labVoltBufferLastTick = buffer;
		labVoltsTransmitted = 0;
	}

	public double getPower()
	{
		return labVoltBufferLastTick * 20;
	}

	@Override
	public String getNeededInfo()
	{
		return getEnergyDisplay(getEnergyNeeded());
	}

	@Override
	public String getStoredInfo()
	{
		return getEnergyDisplay(buffer);
	}

	@Override
	public String getFlowInfo()
	{
		return getEnergyDisplay(labVoltsTransmitted) + "/t";
	}
	
	public static String getEnergyDisplay(double energy)
	{	
		return Math.abs(energy) + "LV";
	}
}