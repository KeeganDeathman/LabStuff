package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class PacketBase extends AbstractPacket
{
    public static final int INVALID_DIMENSION_ID = Integer.MIN_VALUE + 12;
    private int dimensionID;

    public PacketBase()
    {
        this.dimensionID = INVALID_DIMENSION_ID;
    }

    public PacketBase(int dimensionID)
    {
        this.dimensionID = dimensionID;
    }

    public void encodeInto(ByteBuf buffer)
    {
        if (dimensionID == INVALID_DIMENSION_ID)
        {
            throw new IllegalStateException("Invalid Dimension ID! [GC]");
        }
        buffer.writeInt(this.dimensionID);
    }

    public void decodeInto(ByteBuf buffer)
    {
        this.dimensionID = buffer.readInt();
    }

    public int getDimensionID()
    {
        return dimensionID;
    }
    
    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
    	this.decodeInto(buffer);
    }
    
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
    {
    	this.encodeInto(buffer);
    }
}