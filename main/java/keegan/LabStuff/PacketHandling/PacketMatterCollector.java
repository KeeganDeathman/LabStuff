package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityMatterCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PacketMatterCollector extends AbstractPacket
{

	private int x, y, z;
	private ForgeDirection dir;
	
	public PacketMatterCollector() {}
	
	public PacketMatterCollector(int x, int y, int z, ForgeDirection dir)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dir = dir;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(dir.ordinal());

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		dir = ForgeDirection.getOrientation(buffer.readInt());
	}

	@Override
	public void handleClientSide(EntityPlayer player)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player)
	{
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(x,y,z);
		if(te instanceof TileEntityMatterCollector)
		{
			((TileEntityMatterCollector)te).setChuck(dir);
		}
	}

}
