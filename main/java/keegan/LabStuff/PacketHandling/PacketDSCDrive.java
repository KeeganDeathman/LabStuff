package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.recipes.*;
import keegan.labstuff.tileentity.DSCDrive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PacketDSCDrive extends AbstractPacket
{
	private AcceleratorDiscovery discov;
	private int x,y,z;
	
	public PacketDSCDrive(){}
	public PacketDSCDrive(AcceleratorDiscovery dis, int x, int y, int z)
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
		discov = Recipes.accelDiscoveries.get(dis);

	}

	@Override
	public void encodeInto(ChannelHandlerContext arg0, ByteBuf arg1)
	{
		int dis = discov.getIndex();
		arg1.writeInt(dis);
		arg1.writeInt(x);
		arg1.writeInt(y);
		arg1.writeInt(z);

	}

	@Override
	public void handleClientSide(EntityPlayer arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer arg0)
	{
		TileEntity tile = arg0.worldObj.getTileEntity(new BlockPos(x,y,z));
		System.out.println(x);
		System.out.println(y);
		System.out.println(z);
		if(tile != null & tile instanceof DSCDrive)
		{
			((DSCDrive)tile).install(discov);
		}
	}

}
