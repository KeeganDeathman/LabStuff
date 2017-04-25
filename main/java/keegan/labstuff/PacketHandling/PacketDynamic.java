package keegan.labstuff.PacketHandling;

import java.io.IOException;
import java.util.ArrayList;

import io.netty.buffer.*;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.relauncher.Side;

public class PacketDynamic extends PacketBase
{
    private int type;
    private Object[] data;
    private ArrayList<Object> sendData;
    private ByteBuf payloadData;

    public PacketDynamic()
    {
        super();
    }

    public PacketDynamic(Entity entity)
    {
    	super(LabStuffUtils.getDimensionID(entity.worldObj));
        assert entity instanceof IPacketReceiver : "Entity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 0;
        this.data = new Object[] { entity.getEntityId() };
        this.sendData = new ArrayList<Object>();
        ((IPacketReceiver) entity).getNetworkedData(this.sendData);
    }

    public PacketDynamic(TileEntity tile)
    {
    	super(LabStuffUtils.getDimensionID(tile.getWorld()));
        assert tile instanceof IPacketReceiver : "TileEntity does not implement " + IPacketReceiver.class.getSimpleName();
        this.type = 1;
        this.data = new Object[] { tile.getPos() };
        this.sendData = new ArrayList<Object>();
        ((IPacketReceiver) tile).getNetworkedData(this.sendData);
    }

    public boolean isEmpty()
    {
        return sendData.isEmpty();
    }

    @Override
    public void encodeInto(ByteBuf buffer)
    {
    	super.encodeInto(buffer);
        buffer.writeInt(this.type);

        switch (this.type)
        {
        case 0:
            buffer.writeInt((Integer) this.data[0]);
            break;
        case 1:
            buffer.writeInt(((BlockPos) this.data[0]).getX());
            buffer.writeInt(((BlockPos) this.data[0]).getY());
            buffer.writeInt(((BlockPos) this.data[0]).getZ());
            break;
        }

        ByteBuf payloadData = Unpooled.buffer();

        try
        {
            NetworkUtil.encodeData(payloadData, this.sendData);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        int readableBytes = payloadData.readableBytes();
        buffer.writeInt(readableBytes);
        buffer.writeBytes(payloadData);
    }

    @Override
    public void decodeInto(ByteBuf buffer)
    {
    	super.decodeInto(buffer);
        this.type = buffer.readInt();

        World world = LabStuffMain.proxy.getWorldForID(this.getDimensionID());

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
        {
            world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(this.getDimensionID());
        }

        if (world == null)
        {
            FMLLog.severe("Failed to get world for dimension ID: " + this.getDimensionID());
        }

        switch (this.type)
        {
        case 0:
            this.data = new Object[1];
            this.data[0] = buffer.readInt();

            int length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));
            break;
        case 1:
            this.data = new Object[1];
            this.data[0] = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());

            length = buffer.readInt();
            payloadData = Unpooled.copiedBuffer(buffer.readBytes(length));

            break;
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        this.handleData(Side.CLIENT, player);
    }

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        this.handleData(Side.SERVER, player);
    }

    private void handleData(Side side, EntityPlayer player)
    {
        switch (this.type)
        {
        case 0:
            Entity entity = player.worldObj.getEntityByID((Integer) this.data[0]);

            if (entity instanceof IPacketReceiver)
            {
                ((IPacketReceiver) entity).decodePacketdata(payloadData);
            }

            break;
        case 1:
            if (player.worldObj.isBlockLoaded((BlockPos) this.data[0]))
            {
                TileEntity tile = player.worldObj.getTileEntity((BlockPos) this.data[0]);

                if (tile instanceof IPacketReceiver)
                {
                    ((IPacketReceiver) tile).decodePacketdata(payloadData);
                }
            }
            break;
        }
    }

}