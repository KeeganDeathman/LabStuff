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
		System.out.println("Info recieved.");
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		ByteBufUtils.writeUTF8String(buffer, message);
		System.out.println("buffer encoded");
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) 
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		message = ByteBufUtils.readUTF8String(buffer);
		System.out.println("Buffer decoded");
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
		System.out.println("Validating tile entity.");
		if(te instanceof TileEntityCircuitMaker)
		{
			System.out.println("Checking command.");
			if(message.equals("Drill"))
			{
				System.out.println("Checking design");
				if(((TileEntityCircuitMaker) te).getStackInSlot(0).getItem() == LabStuffMain.itemBasicCircuitDesign)
				{
					System.out.println("Drilling basic.");
					if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == LabStuffMain.itemCircuitBoardPlate)
					{
						((TileEntityCircuitMaker) te).decrStackSize(1, 1);
						((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(LabStuffMain.itemBasicDrilledCircuitBoard));
					}
					else if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == LabStuffMain.itemBasicEtchedCircuitBoard)
					{
						((TileEntityCircuitMaker) te).decrStackSize(1, 1);
						((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(LabStuffMain.itemBasicCircuitBoard));
					}
				}
				else if(((TileEntityCircuitMaker) te).getStackInSlot(0).getItem() == LabStuffMain.itemComputerCircuitDesign)
				{
					System.out.println("Drilling computer.");
					if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == LabStuffMain.itemCircuitBoardPlate)
					{
						((TileEntityCircuitMaker) te).decrStackSize(1, 1);
						((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(LabStuffMain.itemComputerDrilledCircuitBoard));
					}
					else if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == LabStuffMain.itemComputerEtchedCircuitBoard)
					{
						((TileEntityCircuitMaker) te).decrStackSize(1, 1);
						((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(LabStuffMain.itemBasicCircuitBoard));
					}
				}
			}
			if(message.equals("Etch"))
			{
				System.out.println("Checking design");
				if(((TileEntityCircuitMaker) te).getStackInSlot(0).getItem() == LabStuffMain.itemBasicCircuitDesign)
				{
					System.out.println("Etching basic.");
					if(((TileEntityCircuitMaker) te).getStackInSlot(3).getItem() == LabStuffMain.itemCircuitBoardPlate)
					{
						((TileEntityCircuitMaker) te).decrStackSize(3, 1);
						((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(LabStuffMain.itemBasicEtchedCircuitBoard));
					}
					else if(((TileEntityCircuitMaker) te).getStackInSlot(3).getItem() == LabStuffMain.itemBasicDrilledCircuitBoard)
					{
						((TileEntityCircuitMaker) te).setInventorySlotContents(3, null);
						((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(LabStuffMain.itemBasicCircuitBoard));
					}
				}
				else if(((TileEntityCircuitMaker) te).getStackInSlot(0).getItem() == LabStuffMain.itemComputerCircuitDesign)
				{
					System.out.println("Etching computer.");
					if(((TileEntityCircuitMaker) te).getStackInSlot(3).getItem() == LabStuffMain.itemCircuitBoardPlate)
					{
						((TileEntityCircuitMaker) te).decrStackSize(3, 1);
						((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(LabStuffMain.itemComputerEtchedCircuitBoard));
					}
					else if(((TileEntityCircuitMaker) te).getStackInSlot(3).getItem() == LabStuffMain.itemComputerDrilledCircuitBoard)
					{
						((TileEntityCircuitMaker) te).decrStackSize(3, 1);
						((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(LabStuffMain.itemComputerCircuitBoard));
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
