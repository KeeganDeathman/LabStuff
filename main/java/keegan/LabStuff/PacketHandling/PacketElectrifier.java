package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.client.GuiElectrifier;
import keegan.labstuff.client.GuiHandler;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketElectrifier extends AbstractPacket {

	
	private int x,y,z;
	private boolean done;
	
	public PacketElectrifier(int x, int y, int z, boolean done)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.done = done;
	}
	
	public PacketElectrifier()
	{		
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeBoolean(done);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		done = buffer.readBoolean();

	}

	@Override
	public void handleClientSide(EntityPlayer player) 
	{
	}

	@Override
	public void handleServerSide(EntityPlayer player) 
	{
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(x,y,z);
		if(te instanceof TileEntityElectrifier)
		{
			((TileEntityElectrifier) te).electrify(world, done);
				
		}
		else
		{
			System.out.println("Nope, its something else");
		}
	}

}
