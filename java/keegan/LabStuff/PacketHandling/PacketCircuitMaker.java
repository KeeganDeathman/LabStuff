package keegan.labstuff.PacketHandling;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketCircuitMaker extends AbstractPacket {

	int x,y,z;
	String message;
	
	public PacketCircuitMaker(){
	}
	
	public PacketCircuitMaker(int x, int y, int z, String cmd)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.message = cmd;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeUTF8String(buffer, message);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		message = ByteBufUtils.readUTF8String(buffer);
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
		if(te instanceof TileEntityCircuitMaker)
		{
			if(message == "Drill")
			{
				if(((TileEntityCircuitMaker) te).getStackInSlot(0).getItem() == LabStuffMain.itemBasicCircuitDesign)
				{
					if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == LabStuffMain.itemCircuitBoardPlate)
					{
						((TileEntityCircuitMaker) te).setInventorySlotContents(1, null);
						((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(LabStuffMain.itemBasicDrilledCircuitBoard));
					}
					else if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == LabStuffMain.itemBasicEtchedCircuitBoard)
					{
						((TileEntityCircuitMaker) te).setInventorySlotContents(1, null);
						((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(LabStuffMain.itemBasicCircuitBoard));
					}
				}
			}
		}
		else
		{
			System.out.println("Nope, its something else");
		}
	}

}
