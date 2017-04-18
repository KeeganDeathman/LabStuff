package keegan.labstuff.PacketHandling;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketTileEntity.TileEntityMessage;
import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.ITileNetwork;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class PacketTileEntity implements IMessageHandler<TileEntityMessage, IMessage>
{
	@Override
	public IMessage onMessage(TileEntityMessage message, MessageContext context) 
	{
		EntityPlayer player = LabStuffMain.packetPipeline.getPlayer(context);
		
		if(player == null)
		{
			return null;
		}
		
		LabStuffMain.packetPipeline.handlePacket(new Runnable() {
			@Override
			public void run()
			{
				TileEntity tileEntity = message.coord4D.getTileEntity(player.worldObj);
				
				if(CapabilityUtils.hasCapability(tileEntity, Capabilities.TILE_NETWORK_CAPABILITY, null))
				{
					ITileNetwork network = CapabilityUtils.getCapability(tileEntity, Capabilities.TILE_NETWORK_CAPABILITY, null);
					
					try {
						network.handlePacketData(message.storedBuffer);
					} catch(Exception e) {
						e.printStackTrace();
					}
					
					message.storedBuffer.release();
				}
			}
		}, player);
		
		return null;
	}
	
	public static class TileEntityMessage implements IMessage
	{
		public Coord4D coord4D;
	
		public ArrayList<Object> parameters;
		
		public ByteBuf storedBuffer = null;
		
		public TileEntityMessage() {}
	
		public TileEntityMessage(Coord4D coord, ArrayList<Object> params)
		{
			coord4D = coord;
			parameters = params;
		}
	
		@Override
		public void toBytes(ByteBuf dataStream)
		{
			coord4D.write(dataStream);
			
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			
			if(server != null)
			{
				World world = server.worldServerForDimension(coord4D.dimensionId);
			}
			
			 LabStuffMain.packetPipeline.encode(new Object[] {parameters}, dataStream);
		}
	
		@Override
		public void fromBytes(ByteBuf dataStream)
		{
			coord4D = Coord4D.read(dataStream);
			
			storedBuffer = dataStream.copy();
		}
	}
}