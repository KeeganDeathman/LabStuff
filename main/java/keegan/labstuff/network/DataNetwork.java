package keegan.labstuff.network;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import keegan.labstuff.common.Coord4D;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class DataNetwork extends DynamicNetwork<DataHandlerWrapper, DataNetwork>
{

	public DataNetwork() {}

	public DataNetwork(Collection<DataNetwork> networks)
	{
		for(DataNetwork net : networks)
		{
			if(net != null)
			{
				adoptTransmittersAndAcceptorsFrom(net);
				net.deregister();
			}
		}

		register();
	}
	
	@Override
	public String getNeededInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStoredInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFlowInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void absorbBuffer(IGridTransmitter<DataHandlerWrapper, DataNetwork> transmitter) {
		
	}

	@Override
	public void clampBuffer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Pair<Coord4D, DataHandlerWrapper>> getAcceptors(Object data) {
		Set<Pair<Coord4D, DataHandlerWrapper>> toReturn = new HashSet<>();
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			return toReturn;
		}
		
		return toReturn;
	}
	
	@Override
	public String toString()
	{
		return "[DataNetwork] " + transmitters.size() + " transmitters, " + possibleAcceptors.size() + " devices.";
	}

}
