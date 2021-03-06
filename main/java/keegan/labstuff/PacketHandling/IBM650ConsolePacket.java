package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.IBM650Console;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class IBM650ConsolePacket extends PacketBase {

	public IBM650ConsolePacket(){}
	
	private BlockPos pos;
	
	public IBM650ConsolePacket(BlockPos pos, int dimID)
	{
		super(dimID);
		this.pos = pos;
	}
	
	@Override
	public void encodeInto(ByteBuf buffer) {
		buffer.writeInt(pos.getX());
		buffer.writeInt(pos.getY());
		buffer.writeInt(pos.getZ());
	}

	@Override
	public void decodeInto(ByteBuf buffer) {
		pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) 
	{
		((IBM650Console)player.worldObj.getTileEntity(pos)).runProgram();
	}

}
