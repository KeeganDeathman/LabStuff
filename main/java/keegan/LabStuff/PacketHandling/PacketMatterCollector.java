package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityMatterCollector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketMatterCollector extends PacketBase
{

	private int x, y, z;
	private EnumFacing dir;
	
	public PacketMatterCollector() {}
	
	public PacketMatterCollector(int x, int y, int z, EnumFacing dir)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.dir = dir;
	}
	
	@Override
	public void encodeInto(ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(dir.getIndex());

	}

	@Override
	public void decodeInto(ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		dir = EnumFacing.getFront(buffer.readInt());
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
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if(te instanceof TileEntityMatterCollector)
		{
			((TileEntityMatterCollector)te).setChuck(dir);
		}
	}

}
