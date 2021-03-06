package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class WorkbenchPacket extends PacketBase {

	private BlockPos pos;
	
	public WorkbenchPacket(){}
	
	public WorkbenchPacket(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void encodeInto(ByteBuf buffer) {
		// TODO Auto-generated method stub
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
	public void handleServerSide(EntityPlayer player) {
		// TODO Auto-generated method stub
		((TileEntityWorkbench)player.worldObj.getTileEntity(pos)).recipeEntered(player);
	}

}
