package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityGravityManipulater;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PacketGravity extends PacketBase
{

	private int x,y,z;
	private float gravity;
	
	public PacketGravity(){}
	
	public PacketGravity(int x, int y, int z, float gravity)
	{
		this.x = x;
		this.y = y ;
		this.z = z;
		this.gravity = gravity;
	}
	
	@Override
	public void encodeInto(ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeFloat(gravity);
	}

	@Override
	public void decodeInto(ByteBuf buffer)
	{
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.gravity = buffer.readFloat();

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
		if(te instanceof TileEntityGravityManipulater)
		{
			((TileEntityGravityManipulater) te).setGravityModifier(gravity);
		}

	}

}
