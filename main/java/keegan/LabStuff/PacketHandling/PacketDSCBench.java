package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.recipes.*;
import keegan.labstuff.tileentity.DSCBench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PacketDSCBench extends AbstractPacket
{
	private DiscoveryItem discov;
	private int x,y,z;
	
	public PacketDSCBench(){}
	public PacketDSCBench(DiscoveryItem dis, int x, int y, int z)
	{
		discov = dis;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	@Override
	public void decodeInto(ChannelHandlerContext arg0, ByteBuf arg1)
	{
		int dis = arg1.readInt();
		x = arg1.readInt();
		y = arg1.readInt();
		z = arg1.readInt();
		discov = Recipes.discoveryItems.get(dis);
		System.out.println("Only 60's kids.");

	}

	@Override
	public void encodeInto(ChannelHandlerContext arg0, ByteBuf arg1)
	{
		int dis = discov.getIndex();
		arg1.writeInt(dis);
		arg1.writeInt(x);
		arg1.writeInt(y);
		arg1.writeInt(z);
		System.out.println("Only 70's kids.");

	}

	@Override
	public void handleClientSide(EntityPlayer arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer arg0)
	{
		TileEntity tile = arg0.worldObj.getTileEntity(x, y, z);
		System.out.println(x);
		System.out.println(y);
		System.out.println(z);
		if(tile != null & tile instanceof DSCBench)
		{
			System.out.println("Only 80's kids.");
			((DSCBench)tile).make(discov);
		}
	}

}
