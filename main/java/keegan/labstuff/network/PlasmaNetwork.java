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

public class PlasmaNetwork extends DynamicNetwork<PlasmaHandlerWrapper, PlasmaNetwork>
{
	private int lastPlasmaScale = 0;
	private int plasmaTransmitted = 0;
	private int plasmaBufferLastTick = 0;

	public int clientPlasmaScale = 0;

	public int buffer = 0;

	public PlasmaNetwork() {}

	public PlasmaNetwork(Collection<PlasmaNetwork> networks)
	{
		for(PlasmaNetwork net : networks)
		{
			if(net != null)
			{
				if(net.plasmaBufferLastTick > plasmaBufferLastTick || net.clientPlasmaScale > clientPlasmaScale)
				{
					clientPlasmaScale = net.clientPlasmaScale;
					plasmaBufferLastTick = net.plasmaBufferLastTick;
					plasmaTransmitted = net.plasmaTransmitted;
					lastPlasmaScale = net.lastPlasmaScale;
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
	public void absorbBuffer(IGridTransmitter<PlasmaHandlerWrapper, PlasmaNetwork> transmitter)
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
        
        for(IGridTransmitter<PlasmaHandlerWrapper, PlasmaNetwork> cable : transmitters)
        {
            reciprocalSum += 1.0/(double)cable.getCapacity();
        }

        meanCapacity = (double)numCables / reciprocalSum;            
	}
    
	public int getPlasmaNeeded()
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return 0;
		}

		return getCapacity()-buffer;
	}

	public double tickEmit(double plasmaToSend)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return 0;
		}

		int sent = 0;
		boolean tryAgain;
		int i = 0;

		do {
			int prev = sent;
			sent += doEmit(plasmaToSend-sent);

			tryAgain = plasmaToSend-sent > 0 && sent-prev > 0 && i < 100;

			i++;
		} while(tryAgain);

		plasmaTransmitted = sent;
		
		return sent;
	}

	public int emit(int plasmaToSend, boolean doEmit)
	{
		int toUse = Math.min(getPlasmaNeeded(), plasmaToSend);
		
		if(doEmit)
		{
			buffer += toUse;
		}
		
		return plasmaToSend-toUse;
	}

	/**
	 * @return sent
	 */
	public double doEmit(double plasmaToSend)
	{
		double sent = 0;

		List<Pair<Coord4D, PlasmaHandlerWrapper>> availableAcceptors = new ArrayList<>();
		availableAcceptors.addAll(getAcceptors(null));

		Collections.shuffle(availableAcceptors);

		if(!availableAcceptors.isEmpty())
		{
			int divider = availableAcceptors.size();
			double remaining = plasmaToSend % divider;
			double sending = (plasmaToSend-remaining)/divider;

			for(Pair<Coord4D, PlasmaHandlerWrapper> pair : availableAcceptors)
			{
				PlasmaHandlerWrapper acceptor = pair.getRight();
				double currentSending = sending+remaining;
				EnumSet<EnumFacing> sides = acceptorDirections.get(pair.getLeft());

				if(sides == null || sides.isEmpty())
				{
					continue;
				}

				for(EnumFacing side : sides)
				{
					double prev = sent;

					sent += acceptor.transferPlasma((int) currentSending, side);

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
	public Set<Pair<Coord4D, PlasmaHandlerWrapper>> getAcceptors(Object data)
	{
		Set<Pair<Coord4D, PlasmaHandlerWrapper>> toReturn = new HashSet<>();

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
				PlasmaHandlerWrapper acceptor = PlasmaHandlerWrapper.get(tile, side);
				
				if(acceptor != null)
				{
					if(acceptor.canReceivePlasma(side) && acceptor.needsPlasma(side))
					{
						toReturn.add(Pair.of(coord, acceptor));
						break;
					}
				}
			}
		}

		return toReturn;
	}

	public static class PlasmaTransferEvent extends Event
	{
		public final PlasmaNetwork plasmaNetwork;

		public final double plasma;

		public PlasmaTransferEvent(PlasmaNetwork network, double currentPlasma)
		{
			plasmaNetwork = network;
			plasma = currentPlasma;
		}
	}

	@Override
	public String toString()
	{
		return "[PlasmaNetwork] " + transmitters.size() + " transmitters, " + possibleAcceptors.size() + " acceptors.";
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		clearPlasmaTransmitted();

		int currentPowerScale = getPowerScale();

		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			if(Math.abs(currentPowerScale-lastPlasmaScale) > 0.01 || (currentPowerScale != lastPlasmaScale && (currentPowerScale == 0 || currentPowerScale == 1)))
			{
				needsUpdate = true;
			}

			if(needsUpdate)
			{
				MinecraftForge.EVENT_BUS.post(new PlasmaTransferEvent(this, currentPowerScale));
				lastPlasmaScale = currentPowerScale;
				needsUpdate = false;
			}

			if(buffer > 0)
			{
				buffer -= tickEmit(buffer);
			}
		}
	}

	public int getPowerScale()
	{
		return (int) Math.max(plasmaBufferLastTick == 0 ? 0 : Math.min(Math.ceil(Math.log10(getPower())*2)/10, 1), getCapacity() == 0 ? 0 : buffer/getCapacity());
	}

	public void clearPlasmaTransmitted()
	{
		plasmaBufferLastTick = buffer;
		plasmaTransmitted = 0;
	}

	public double getPower()
	{
		return plasmaBufferLastTick * 20;
	}

	@Override
	public String getNeededInfo()
	{
		return getPlasmaDisplay(getPlasmaNeeded());
	}

	@Override
	public String getStoredInfo()
	{
		return getPlasmaDisplay(buffer);
	}

	@Override
	public String getFlowInfo()
	{
		return getPlasmaDisplay(plasmaTransmitted) + "/t";
	}
	
	public static String getPlasmaDisplay(int energy)
	{	
		return Math.abs(energy) + "PU";
	}
}