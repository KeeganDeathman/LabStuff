package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.LabStuffMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class GuiChangePacket extends PacketBase {

	public GuiChangePacket(){}
	
	private BlockPos pos;
	private int id;
	
	public GuiChangePacket(BlockPos pos, int id, int dimID)
	{
		super(dimID);
		this.pos = pos;
		this.id = id;
	}
	
	@Override
	public void encodeInto(ByteBuf buffer) {
		buffer.writeInt(pos.getX());
		buffer.writeInt(pos.getY());
		buffer.writeInt(pos.getZ());
		buffer.writeInt(id);
	}

	@Override
	public void decodeInto(ByteBuf buffer) {
		pos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
		id = buffer.readInt();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		player.openGui(LabStuffMain.instance, id, player.worldObj, pos.getX(), pos.getY(), pos.getZ());
	}

}
