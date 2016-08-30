package keegan.labstuff.PacketHandling;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityDLLaptop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketDLLaptopWeb extends AbstractPacket {

	private int x, y, z;
	private String action;
	
	public PacketDLLaptopWeb(){}
	
	public PacketDLLaptopWeb(int x, int y, int z, String action)
	{
		this.x = x;
		this.y = y ;
		this.z = z;
		this.action = action;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeUTF8String(buffer, action);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.action = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) 
	{
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(x,y,z);
		if(te instanceof TileEntityDLLaptop)
		{
			System.out.println("Performing action");
			((TileEntityDLLaptop) te).performWebAction(action);
		}
		else
		{
			System.out.println("Nope, its something else");
		}

	}

}
