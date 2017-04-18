package keegan.labstuff.PacketHandling;

import java.util.*;

import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.handler.codec.MessageToMessageCodec;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketDataRequest.DataRequestMessage;
import keegan.labstuff.PacketHandling.PacketTileEntity.TileEntityMessage;
import keegan.labstuff.PacketHandling.PacketTransmitterUpdate.TransmitterUpdateMessage;
import keegan.labstuff.network.Range4D;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Packet pipeline class. Directs all registered packet data to be handled by the packets themselves.
 * @author sirgingalot
 * some code from: cpw
 */
@ChannelHandler.Sharable
public class PacketPipeline extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket> {

    private EnumMap<Side, FMLEmbeddedChannel>           channels;
    private LinkedList<Class<? extends AbstractPacket>> packets           = new LinkedList<Class<? extends AbstractPacket>>();
    private boolean                                     isPostInitialised = false;
    
	public SimpleNetworkWrapper netHandler = NetworkRegistry.INSTANCE.newSimpleChannel("LAB");

	
	
    /**
     * Register your packet with the pipeline. Discriminators are automatically set.
     *
     * @param clazz the class to register
     *
     * @return whether registration was successful. Failure may occur if 256 packets have been registered or if the registry already contains this packet
     */
    public boolean registerPacket(Class<? extends AbstractPacket> clazz) {
        if (this.packets.size() > 256) {
            // You should log here!!
            return false;
        }

        if (this.packets.contains(clazz)) {
            // You should log here!!
            return false;
        }

        if (this.isPostInitialised) {
            // You should log here!!
            return false;
        }

        this.packets.add(clazz);
        return true;
    }

    // In line encoding of the packet, including discriminator setting
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends AbstractPacket> clazz = msg.getClass();
        if (!this.packets.contains(msg.getClass())) {
            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
        }

        byte discriminator = (byte) this.packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(new PacketBuffer(buffer.copy()), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    // In line decoding and handling of the packet
	@Override
	protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
		ByteBuf payload = msg.payload();
		byte discriminator = payload.readByte();
		Class<? extends AbstractPacket> clazz = this.packets.get(discriminator);
		if (clazz == null) {
			throw new NullPointerException("No packet registered for discriminator: " + discriminator);
		}

		AbstractPacket pkt = clazz.newInstance();
		pkt.decodeInto(ctx, payload.slice());

		EntityPlayer player;
		/*
		if(msg.getTarget().isClient())
		{
			player = Minecraft.getMinecraft().thePlayer;
			pkt.handleClientSide(player);
		}
		*/
		if(msg.getTarget().isServer())
		{
			INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
			player = ((NetHandlerPlayServer) netHandler).playerEntity;
			pkt.handleServerSide(player);
		}
		else
			out.add(pkt);
	}
	
    // Method to call from FMLInitializationEvent
    public void initalise() {
        this.channels = NetworkRegistry.INSTANCE.newChannel("LabStuff", this);
		netHandler.registerMessage(PacketTransmitterUpdate.class, TransmitterUpdateMessage.class, 1, Side.CLIENT);
		netHandler.registerMessage(PacketTileEntity.class, TileEntityMessage.class, 5, Side.CLIENT);
		netHandler.registerMessage(PacketTileEntity.class, TileEntityMessage.class, 5, Side.SERVER);
		netHandler.registerMessage(PacketDataRequest.class, DataRequestMessage.class, 7, Side.SERVER);
    }

    // Method to call from FMLPostInitializationEvent
    // Ensures that packet discriminators are common between server and client by using logical sorting
    public void postInitialise() {
        if (this.isPostInitialised) {
            return;
        }

        this.isPostInitialised = true;
        Collections.sort(this.packets, new Comparator<Class<? extends AbstractPacket>>() {

            @Override
            public int compare(Class<? extends AbstractPacket> clazz1, Class<? extends AbstractPacket> clazz2) {
                int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
                if (com == 0) {
                    com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
                }

                return com;
            }
        });
    }

    /**
     * Send this message to everyone.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToAll(AbstractPacket message) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the specified player.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    public void sendTo(AbstractPacket message, EntityPlayerMP player) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param point   The {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint} around which to send
     */
    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    public void sendToDimension(AbstractPacket message, int dimensionId) {
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        this.channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        this.channels.get(Side.SERVER).writeAndFlush(message);
    }
    
    

    /**
     * Send this message to the server.
     * <p/>
     * Adapted from CPW's code in cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToServer(AbstractPacket message) {
        this.channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        this.channels.get(Side.CLIENT).writeAndFlush(message);
    }

    public void sendToReceivers(IMessage message, Range4D range)
	{
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

		if(server != null)
		{
			for(EntityPlayerMP player : (List<EntityPlayerMP>)server.getPlayerList().getPlayerList())
			{
				if(player.dimension == range.dimensionId && Range4D.getChunkRange(player).intersects(range))
				{
					netHandler.sendTo(message, player);
				}
			}
		}
	}

	public static String readString(ByteBuf dataStream) {
		// TODO Auto-generated method stub
		return ByteBufUtils.readUTF8String(dataStream);
	}

	public void sendTo(IMessage message, EntityPlayerMP player)
	{
		netHandler.sendTo(message, player);
	}
	
	public void sendToServer(IMessage message)
	{
		netHandler.sendToServer(message);
	}
	
	public static void writeString(ByteBuf dataStream, String text) {
		// TODO Auto-generated method stub
		ByteBufUtils.writeUTF8String(dataStream, text);
	}

	public void handlePacket(Runnable runnable, EntityPlayer player) {
		// TODO Auto-generated method stub
		LabStuffMain.proxy.handlePacket(runnable, player);
	}

	public EntityPlayer getPlayer(MessageContext context) {
		// TODO Auto-generated method stub
		return LabStuffMain.proxy.getPlayer(context);
	}

	public static void encode(Object[] dataValues, ByteBuf output)
	{
		try {
			for(Object data : dataValues)
			{
				if(data instanceof Integer)
				{
					output.writeInt((Integer)data);
				}
				else if(data instanceof Short)
				{
					output.writeShort((Short)data);
				}
				else if(data instanceof Long)
				{
					output.writeLong((Long)data);
				}
				else if(data instanceof Boolean)
				{
					output.writeBoolean((Boolean)data);
				}
				else if(data instanceof Double)
				{
					output.writeDouble((Double)data);
				}
				else if(data instanceof Float)
				{
					output.writeFloat((Float)data);
				}
				else if(data instanceof String)
				{
					writeString(output, (String)data);
				}
				else if(data instanceof Byte)
				{
					output.writeByte((Byte)data);
				}
				else if(data instanceof EnumFacing)
				{
					output.writeInt(((EnumFacing)data).ordinal());
				}
				else if(data instanceof ItemStack)
				{
					writeStack(output, (ItemStack)data);
				}
				else if(data instanceof NBTTagCompound)
				{
					writeNBT(output, (NBTTagCompound)data);
				}
				else if(data instanceof int[])
				{
					for(int i : (int[])data)
					{
						output.writeInt(i);
					}
				}
				else if(data instanceof byte[])
				{
					for(byte b : (byte[])data)
					{
						output.writeByte(b);
					}
				}
				else if(data instanceof ArrayList)
				{
					encode(((ArrayList)data).toArray(), output);
				}
				else {
					throw new RuntimeException("Un-encodable data passed to encode(): " + data + ", full data: " + Arrays.toString(dataValues));
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeStack(ByteBuf output, ItemStack stack)
	{
		ByteBufUtils.writeItemStack(output, stack);
	}
	
	public static ItemStack readStack(ByteBuf input)
	{
		return ByteBufUtils.readItemStack(input);
	}
	
	public static void writeNBT(ByteBuf output, NBTTagCompound nbtTags)
	{
		ByteBufUtils.writeTag(output, nbtTags);
	}
	
	public static NBTTagCompound readNBT(ByteBuf input)
	{
		return ByteBufUtils.readTag(input);
	}
}