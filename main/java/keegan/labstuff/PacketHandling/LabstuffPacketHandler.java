package keegan.labstuff.PacketHandling;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.*;

import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.ClientTickHandler;
import keegan.labstuff.common.TickHandlerServer;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Sharable
public class LabstuffPacketHandler extends SimpleChannelInboundHandler<IPacket>
{
    private final Map<Side, Map<Integer, Queue<PacketPlayerPair>>> packetMap;
    private static volatile int livePacketCount = 0;

    public LabstuffPacketHandler()
    {
        Map<Side, Map<Integer, Queue<PacketPlayerPair>>> map = Maps.newHashMap();
        for (Side side : Side.values())
        {
            Map<Integer, Queue<PacketPlayerPair>> sideMap = new ConcurrentHashMap<Integer, Queue<PacketPlayerPair>>();
            map.put(side, sideMap);
        }

        packetMap = ImmutableMap.copyOf(map);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            ClientTickHandler.addPacketHandler(this);
        }
        TickHandlerServer.addPacketHandler(this);
    }

    public void unload(World world)
    {
        Side side = world.isRemote ? Side.CLIENT : Side.SERVER;
        int dimId = LabStuffUtils.getDimensionID(world);
        Queue<PacketPlayerPair> queue = getQueue(side, dimId);
        queue.clear();
    }

    public void tick(World world)
    {
        PacketPlayerPair pair;
        Side side = world.isRemote ? Side.CLIENT : Side.SERVER;
        int dimID = LabStuffUtils.getDimensionID(world);
        Queue<PacketPlayerPair> queue = getQueue(side, dimID);
        while ((pair = queue.poll()) != null)
        {
            switch (side)
            {
            case CLIENT:
                pair.getPacket().handleClientSide(pair.getPlayer());
                break;
            case SERVER:
                pair.getPacket().handleServerSide(pair.getPlayer());
                break;
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception
    {
        INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        Side side = ctx.channel().attr(NetworkRegistry.CHANNEL_SOURCE).get();
        EntityPlayer player = LabStuffMain.proxy.getPlayerFromNetHandler(netHandler);

        if (player == null)
        {
            return;
        }

        if (side != null)
        {
            getQueue(side, msg.getDimensionID()).add(new PacketPlayerPair(msg, player));
            livePacketCount++;
        }
    }

    private Queue<PacketPlayerPair> getQueue(Side side, int dimID)
    {
        Map<Integer, Queue<PacketPlayerPair>> map = packetMap.get(side);
        if (!map.containsKey(dimID))
        {
            map.put(dimID, Queues.<PacketPlayerPair>newConcurrentLinkedQueue());
        }
        return map.get(dimID);
    }

    private final class PacketPlayerPair
    {
        private IPacket packet;
        private EntityPlayer player;

        public PacketPlayerPair(IPacket packet, EntityPlayer player)
        {
            this.packet = packet;
            this.player = player;
        }

        public IPacket getPacket()
        {
            return packet;
        }

        public void setPacket(IPacket packet)
        {
            this.packet = packet;
        }

        public EntityPlayer getPlayer()
        {
            return player;
        }

        public void setPlayer(EntityPlayer player)
        {
            this.player = player;
        }
    }
}