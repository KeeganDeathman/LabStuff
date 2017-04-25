package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract class PacketBase implements IPacket
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

    @Override
    public void encodeInto(ByteBuf buffer)
    {
        if (dimensionID == INVALID_DIMENSION_ID)
        {
            throw new IllegalStateException("Invalid Dimension ID! [LS]");
        }
        buffer.writeInt(this.dimensionID);
    }

    public int getDimensionID()
    {
        return dimensionID;
    }
    
    @Override
    public void decodeInto(ByteBuf buffer)
    {
        this.dimensionID = buffer.readInt();
    }
    
}