package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public interface IPacket
{
    void encodeInto(ByteBuf buffer);

    void decodeInto(ByteBuf buffer);

    void handleClientSide(EntityPlayer player);

    void handleServerSide(EntityPlayer player);

    int getDimensionID();
}