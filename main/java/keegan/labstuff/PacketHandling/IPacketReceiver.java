package keegan.labstuff.PacketHandling;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;

public interface IPacketReceiver
{
    void getNetworkedData(ArrayList<Object> sendData);

    void decodePacketdata(ByteBuf buffer);
}