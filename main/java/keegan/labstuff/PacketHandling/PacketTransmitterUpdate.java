package keegan.labstuff.PacketHandling;

import java.util.*;

import io.netty.buffer.ByteBuf;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketTransmitterUpdate.TransmitterUpdateMessage;
import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketTransmitterUpdate implements IMessageHandler<TransmitterUpdateMessage, IMessage>
{
	@Override
	public IMessage onMessage(TransmitterUpdateMessage message, MessageContext context) 
	{
		EntityPlayer player = LabStuffMain.packetPipeline.getPlayer(context);
		
		LabStuffMain.packetPipeline.handlePacket(new Runnable() {
			@Override
			public void run()
			{
				if(message.packetType == PacketType.UPDATE)
				{
					TileEntity tileEntity = message.coord4D.getTileEntity(player.worldObj);

					if(CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
					{
						IGridTransmitter transmitter = CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null);
						DynamicNetwork network = transmitter.hasTransmitterNetwork() && !message.newNetwork ? transmitter.getTransmitterNetwork() : transmitter.createEmptyNetwork();
						network.register();
						transmitter.setTransmitterNetwork(network);
						
						for(Coord4D coord : message.transmitterCoords)
						{
							TileEntity tile = coord.getTileEntity(player.worldObj);

							if(CapabilityUtils.hasCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
							{
								CapabilityUtils.getCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, null).setTransmitterNetwork(network);
							}
						}
						
						network.updateCapacity();
					}
				}
				
				if(message.coord4D == null || player == null)
				{
					return;
				}
				
				if(message.packetType == PacketType.ENERGY)
				{
					TileEntity tileEntity = message.coord4D.getTileEntity(player.worldObj);
					
					if(CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
					{
						IGridTransmitter transmitter = CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null);
						
						if(transmitter.hasTransmitterNetwork() && transmitter.getTransmissionType() == TransmissionType.ENERGY)
						{
							((IGridTransmitter<EnergyAcceptorWrapper, EnergyNetwork>)transmitter).getTransmitterNetwork().clientEnergyScale = message.power;
						}
					}
				}
				if(message.packetType == PacketType.PLASMA)
				{
					TileEntity tileEntity = message.coord4D.getTileEntity(player.worldObj);
					
					if(CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
					{
						IGridTransmitter transmitter = CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null);
						
						if(transmitter.hasTransmitterNetwork() && transmitter.getTransmissionType() == TransmissionType.PLASMA)
						{
							((IGridTransmitter<PlasmaHandlerWrapper, PlasmaNetwork>)transmitter).getTransmitterNetwork().clientPlasmaScale = message.plasma;
						}
					}
				}
				else if(message.packetType == PacketType.FLUID)
				{
					TileEntity tileEntity = message.coord4D.getTileEntity(player.worldObj);

					if(CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
					{
						IGridTransmitter transmitter = CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null);
						
						if(transmitter.hasTransmitterNetwork() && transmitter.getTransmissionType() == TransmissionType.FLUID)
						{
							FluidNetwork net = ((IGridTransmitter<IFluidHandler, FluidNetwork>)transmitter).getTransmitterNetwork();

							if(message.fluidType != null)
							{
								net.refFluid = message.fluidType;
							}

							net.buffer = message.fluidStack;
							net.didTransfer = message.didFluidTransfer;
						}
					}
				}
			}
		}, player);
		
		return null;
	}
	
	public static class TransmitterUpdateMessage implements IMessage
	{
		public PacketType packetType;
	
		public Coord4D coord4D;
	
		public double power;
	
		public FluidStack fluidStack;
		public Fluid fluidType;
		public float fluidScale;
		public boolean didFluidTransfer;
		
		public int plasma;

		public int amount;

		public boolean newNetwork;
		public Collection<IGridTransmitter> transmittersAdded;
		public Collection<Coord4D> transmitterCoords;
		
		public TransmitterUpdateMessage() {}
	
		public TransmitterUpdateMessage(PacketType type, Coord4D coord, Object... data)
		{
			packetType = type;
			coord4D = coord;
	
			switch(packetType)
			{
				case UPDATE:
					newNetwork = (Boolean)data[0];
					transmittersAdded = (Collection<IGridTransmitter>)data[1];
					break;
				case ENERGY:
					power = (Double)data[0];
					break;
				case FLUID:
					fluidStack = (FluidStack)data[0];
					didFluidTransfer = (Boolean)data[1];
					break;
				case PLASMA:
					plasma = (int) data[0];
					break;
				default:
					break;
			}
		}
	
		@Override
		public void toBytes(ByteBuf dataStream)
		{
			dataStream.writeInt(packetType.ordinal());
	
			coord4D.write(dataStream);
			
	
			switch(packetType)
			{
				case UPDATE:
					dataStream.writeBoolean(newNetwork);
					dataStream.writeInt(transmittersAdded.size());
					
					for(IGridTransmitter transmitter : transmittersAdded)
					{
						transmitter.coord().write(dataStream);
					}
					
					break;
				case ENERGY:
					dataStream.writeDouble(power);
					
					break;
				case PLASMA:
					dataStream.writeInt(plasma);
					
					break;
				case FLUID:
					if(fluidStack != null)
					{
						dataStream.writeBoolean(true);
						LabStuffMain.packetPipeline.writeString(dataStream, FluidRegistry.getFluidName(fluidStack));
						dataStream.writeInt(fluidStack.amount);
					}
					else {
						dataStream.writeBoolean(false);
					}
					
					dataStream.writeBoolean(didFluidTransfer);
					
					break;
				default:
					break;
			}
		}
	
		@Override
		public void fromBytes(ByteBuf dataStream)
		{
			packetType = PacketType.values()[dataStream.readInt()];
			
			coord4D = Coord4D.read(dataStream);

			if(packetType == PacketType.UPDATE)
			{
				newNetwork = dataStream.readBoolean();
				transmitterCoords = new HashSet<>();
				int numTransmitters = dataStream.readInt();

				for(int i = 0; i < numTransmitters; i++)
				{
					transmitterCoords.add(Coord4D.read(dataStream));
				}
			}
			else if(packetType == PacketType.ENERGY)
			{
				power = dataStream.readDouble();
			}
			else if(packetType == PacketType.PLASMA)
			{
				plasma = dataStream.readInt();
			}
			else if(packetType == PacketType.FLUID)
			{
				if(dataStream.readBoolean())
				{
					fluidType = FluidRegistry.getFluid(LabStuffMain.packetPipeline.readString(dataStream));
					amount = dataStream.readInt();
				}
				else {
					fluidType = null;
					amount = 0;
				}
				
				didFluidTransfer = dataStream.readBoolean();

				if(fluidType != null)
				{
					fluidStack = new FluidStack(fluidType, amount);
				}
			}
		}
	}
	
	public static enum PacketType
	{
		UPDATE,
		ENERGY,
		FLUID,
		PLASMA
	}
}