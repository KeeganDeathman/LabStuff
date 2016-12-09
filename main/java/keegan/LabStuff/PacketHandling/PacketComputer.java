package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.tileentity.TileEntityComputer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketComputer extends AbstractPacket {

	public PacketComputer()
	{
		
	}
	
	public int x, y, z;
	public String line1, line2, line3, line4, line5;
	
	public PacketComputer(int x, int y, int z, String consoleline1, String consoleline2, String consoleline3, String consoleline4, String consoleline5)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.line1 = consoleline1;
		this.line2 = consoleline2;
		this.line3 = consoleline3;
		this.line4 = consoleline4;
		this.line5 = consoleline5;
		System.out.println(line5);
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		if(!(line1 == null && line2 == null && line3 == null && line4 == null && line5 == null))
		{
			System.out.println("Encoding...");
			System.out.println(line5);
			buffer.writeInt(x);
			buffer.writeInt(y);
			buffer.writeInt(z);
			ByteBufUtils.writeUTF8String(buffer, line1);
			ByteBufUtils.writeUTF8String(buffer, line2);
			ByteBufUtils.writeUTF8String(buffer, line3);
			ByteBufUtils.writeUTF8String(buffer, line4);
			ByteBufUtils.writeUTF8String(buffer, line5);
			System.out.println("Encoded");
		}

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		System.out.println("Decoding...");
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		line1 = ByteBufUtils.readUTF8String(buffer);
		line2 = ByteBufUtils.readUTF8String(buffer);
		line3 = ByteBufUtils.readUTF8String(buffer);
		line4 = ByteBufUtils.readUTF8String(buffer);
		line5 = ByteBufUtils.readUTF8String(buffer);
		System.out.println("Decoded");
		System.out.println(line5);

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleServerSide(EntityPlayer player) 
	{
		System.out.println("Handling Server Side");
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if(te instanceof TileEntityComputer)
		{
			System.out.println("Setting tile enitity's log...");
			((TileEntityComputer) te).ConsoleLogLine1 = line1;
			((TileEntityComputer) te).ConsoleLogLine2 = line2;
			((TileEntityComputer) te).ConsoleLogLine3 = line3;
			((TileEntityComputer) te).ConsoleLogLine4 = line4;
			((TileEntityComputer) te).ConsoleLogLine5 = line5;
			((TileEntityComputer) te).writeToNBT(new NBTTagCompound());
			System.out.println("Log set");
		}
	}

}
