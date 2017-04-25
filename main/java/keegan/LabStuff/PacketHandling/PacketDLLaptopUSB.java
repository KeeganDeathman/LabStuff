package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityDLLaptop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketDLLaptopUSB extends PacketBase
{
	
	
	private int x,y,z,slot;
	private String deviceName;
	
	public PacketDLLaptopUSB(){}
	
	public PacketDLLaptopUSB(int x, int y, int z, int slot, String deviceName)
	{
		//0-8 as slot will be plugging in device, 42 will be unplugging
		this.x = x;
		this.y = y ;
		this.z = z;
		this.slot = slot;
		this.deviceName = deviceName;
	}
	
	
	@Override
	public void encodeInto(ByteBuf buffer) 
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(slot);
		ByteBufUtils.writeUTF8String(buffer, deviceName);
	}

	@Override
	public void decodeInto(ByteBuf buffer) 
	{
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
		this.slot = buffer.readInt();
		this.deviceName = ByteBufUtils.readUTF8String(buffer);
	}

	@Override
	public void handleClientSide(EntityPlayer player) {}

	@Override
	public void handleServerSide(EntityPlayer player) {
		World world = player.worldObj;
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if(te instanceof TileEntityDLLaptop)
		{
			if(slot == 42)
			{
				//Unplug
				switch(deviceName)
				{
					case "dPad":
						((TileEntityDLLaptop)te).unPlugTablet(player.inventory);
						break;
					default:
						System.out.println("Unrecognized Device");
				}
			}
			else if(slot >= 0 && slot <= 8)
			{
				//Plugin
				if(player.inventory.getStackInSlot(slot).isItemEqual(new ItemStack(LabStuffMain.itemUnProgrammedDPad)))
				{
					((TileEntityDLLaptop)te).plugInTablet(slot, player.inventory);
				}
			}
			else
			{
				//No...
			}
		}
		else
		{
			System.out.println("Nope, its something else");
		}
	}

}
