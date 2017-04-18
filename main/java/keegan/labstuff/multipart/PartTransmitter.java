package keegan.labstuff.multipart;

import java.util.*;

import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.Capabilities;
import keegan.labstuff.network.*;
import keegan.labstuff.network.DynamicNetwork.NetworkClientRequest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;

public abstract class PartTransmitter<A, N extends DynamicNetwork<A, N>> extends PartSidedPipe
{
	public MultipartTransmitter<A, N> transmitterDelegate;

	public boolean unloaded = true;
	
	public boolean dataRequest = false;

	public PartTransmitter()
	{
		transmitterDelegate = new MultipartTransmitter<>(this);
	}

	public MultipartTransmitter<A, N> getTransmitter()
	{
		return transmitterDelegate;
	}

	public abstract N createNewNetwork();

	public abstract N createNetworkByMerging(Collection<N> networks);

	@Override
	public void onWorldJoin()
	{
		if(!getWorld().isRemote)
		{
			TransmitterNetworkRegistry.registerOrphanTransmitter(getTransmitter());
		}

		unloaded = false;
	}
	
	@Override
	public void update()
	{
		super.update();
		
		if(getWorld().isRemote)
		{
			if(!dataRequest)
			{
				dataRequest = true;
				MinecraftForge.EVENT_BUS.post(new NetworkClientRequest(getWorld().getTileEntity(getPos())));
			}
		}
	}
	
	@Override
	public void onUnloaded()
	{
		if(!getWorld().isRemote)
		{
			getTransmitter().takeShare();
		}
		
		super.onUnloaded();
	}
	
	@Override
	public void onWorldSeparate() 
	{
		unloaded = true;
		
		if(!getWorld().isRemote)
		{
			TransmitterNetworkRegistry.invalidateTransmitter(getTransmitter());
		}
		else {
			getTransmitter().setTransmitterNetwork(null);
		}
	}

	@Override
	public void markDirtyTransmitters()
	{
		super.markDirtyTransmitters();
		
		if(getTransmitter().hasTransmitterNetwork())
		{
			TransmitterNetworkRegistry.invalidateTransmitter(getTransmitter());
		}
	}

	@Override
	public void markDirtyAcceptor(EnumFacing side)
	{
		super.markDirtyAcceptor(side);
		
		if(getTransmitter().hasTransmitterNetwork())
		{
			getTransmitter().getTransmitterNetwork().acceptorChanged(getTransmitter(), side);
		}
	}

	public abstract A getCachedAcceptor(EnumFacing side);
	
	protected TileEntity getCachedTile(EnumFacing side)
	{
		ConnectionType type = connectionTypes[side.ordinal()];
		
		if(type == ConnectionType.PULL || type == ConnectionType.NONE)
		{
			return null;
		}
		
		return connectionMapContainsSide(currentAcceptorConnections, side) ? cachedAcceptors[side.ordinal()] : null;
	}
		
	public abstract int getCapacity();

	public abstract Object getBuffer();

	public abstract void takeShare();

    public abstract void updateShare();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing side)
	{
		return capability == Capabilities.GRID_TRANSMITTER_CAPABILITY
				|| super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == Capabilities.GRID_TRANSMITTER_CAPABILITY)
		{
			return (T)getTransmitter();
		}
		
		return super.getCapability(capability, side);
	}
}