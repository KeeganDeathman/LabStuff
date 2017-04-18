package keegan.labstuff.PacketHandling;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketDataRequest.DataRequestMessage;
import keegan.labstuff.PacketHandling.PacketTileEntity.TileEntityMessage;
import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketDataRequest implements IMessageHandler<DataRequestMessage, IMessage>
{
	@Override
	public IMessage onMessage(DataRequestMessage message, MessageContext context) 
	{
		EntityPlayer player = LabStuffMain.packetPipeline.getPlayer(context);
		
		LabStuffMain.packetPipeline.handlePacket(new Runnable() {
			@Override
			public void run()
			{
				World worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.coord4D.dimensionId);
				TileEntity tileEntity = message.coord4D.getTileEntity(worldServer);
				
				if(worldServer != null)
				{
		
					if(CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
					{
						IGridTransmitter transmitter = CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null);
		
						transmitter.setRequestsUpdate();
						
						if(transmitter.hasTransmitterNetwork())
						{
							transmitter.getTransmitterNetwork().addUpdate(player);
						}
					}
		
					if(CapabilityUtils.hasCapability(tileEntity, Capabilities.TILE_NETWORK_CAPABILITY, null))
					{
						ITileNetwork network = CapabilityUtils.getCapability(tileEntity, Capabilities.TILE_NETWORK_CAPABILITY, null);
						
						LabStuffMain.packetPipeline.sendTo(new TileEntityMessage(Coord4D.get(tileEntity), network.getNetworkedData(new ArrayList<Object>())), (EntityPlayerMP)player);
					}
				}
			}
		}, player);
		
		return null;
	}
	
	public static class DataRequestMessage implements IMessage
	{
		public Coord4D coord4D;
		
		public DataRequestMessage() {}
	
		public DataRequestMessage(Coord4D coord)
		{
			coord4D = coord;
		}
		
		@Override
		public void toBytes(ByteBuf dataStream)
		{
			coord4D.write(dataStream);
		}
	
		@Override
		public void fromBytes(ByteBuf dataStream)
		{
			coord4D = Coord4D.read(dataStream);
		}
	}
}