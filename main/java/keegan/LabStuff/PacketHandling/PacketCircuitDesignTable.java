package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityCircuitDesignTable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketCircuitDesignTable extends PacketBase {

	
	public int x,y,z;
	public String design;
	
	public PacketCircuitDesignTable(){
	}
	
	public PacketCircuitDesignTable(int x, int y, int z, String design)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.design = design;
	}
	
	@Override
	public void encodeInto(ByteBuf buffer) 
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeUTF8String(buffer, design);
	}

	@Override
	public void decodeInto(ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		design = ByteBufUtils.readUTF8String(buffer);

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) 
	{
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if(te instanceof TileEntityCircuitDesignTable)
		{
			((TileEntityCircuitDesignTable) te).drawCircuit(design);
		}
		else
		{
			System.out.println("Nope, its something else");
		}
		
	}

}
