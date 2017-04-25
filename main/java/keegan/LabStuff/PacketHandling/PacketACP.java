package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityAcceleratorControlPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketACP extends PacketBase
{

	private int x, y, z;
	private String button;
	
	public PacketACP() {}
	
	public PacketACP(int x, int y, int z, String button, int dim)
	{
		super(dim);
		this.x = x;
		this.y = y;
		this.z = z;
		this.button = button;
	}
	
	@Override
	public void encodeInto(ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeUTF8String(buffer, button);

	}

	@Override
	public void decodeInto(ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		button = ByteBufUtils.readUTF8String(buffer);

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
		if(te instanceof TileEntityAcceleratorControlPanel)
		{
			if(button.equals("launch"))
				((TileEntityAcceleratorControlPanel)te).launch();
		}
	}

}
