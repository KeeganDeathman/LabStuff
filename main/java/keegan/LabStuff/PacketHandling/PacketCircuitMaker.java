package keegan.labstuff.PacketHandling;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.recipes.CircuitCreation;
import keegan.labstuff.recipes.Recipes;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;
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
		ArrayList<CircuitCreation> recipes = Recipes.getCircuitCreations();
		System.out.println("Validating tile entity.");
		if(te instanceof TileEntityCircuitMaker)
		{
			System.out.println("Checking command.");
			if(message.equals("Drill"))
			{
				System.out.println("Checking design");
				for(int i = 0; i < recipes.size(); i++)
				{
					if(((TileEntityCircuitMaker) te).getStackInSlot(0).getItem().getUnlocalizedName().contains(recipes.get(i).getDesignName()))
					{
						System.out.println("Drilling " + recipes.get(i).getDesignName());
						if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == LabStuffMain.itemCircuitBoardPlate)
						{
							((TileEntityCircuitMaker) te).decrStackSize(1, 1);
							if(((TileEntityCircuitMaker) te).getStackInSlot(2) == null)
								((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(recipes.get(i).getDrilled()));
							else
								((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(recipes.get(i).getDrilled(), ((TileEntityCircuitMaker)te).getStackInSlot(2).stackSize));
						}
						else if(((TileEntityCircuitMaker) te).getStackInSlot(1).getItem() == recipes.get(i).getEtched())
						{
							((TileEntityCircuitMaker) te).decrStackSize(1, 1);
							if(((TileEntityCircuitMaker) te).getStackInSlot(2) == null)
								((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(recipes.get(i).getCircuit()));
							else
								((TileEntityCircuitMaker) te).setInventorySlotContents(2, new ItemStack(recipes.get(i).getCircuit(), ((TileEntityCircuitMaker)te).getStackInSlot(2).stackSize));
						}
					}
				}
			}
			if(message.equals("Etch"))
			{
				System.out.println("Checking design");
				for(int i = 0; i < recipes.size(); i++)
				{
					if(((TileEntityCircuitMaker) te).getStackInSlot(0).getItem().getUnlocalizedName().contains(recipes.get(i).getDesignName()))
					{
						System.out.println("Drilling " + recipes.get(i).getDesignName());
						if(((TileEntityCircuitMaker) te).getStackInSlot(3).getItem() == LabStuffMain.itemCircuitBoardPlate)
						{
							((TileEntityCircuitMaker) te).decrStackSize(3, 1);
							if(((TileEntityCircuitMaker) te).getStackInSlot(4) == null)
								((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(recipes.get(i).getEtched()));
							else
								((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(recipes.get(i).getEtched(), ((TileEntityCircuitMaker)te).getStackInSlot(2).stackSize));
						}
						else if(((TileEntityCircuitMaker) te).getStackInSlot(3).getItem() == recipes.get(i).getDrilled())
						{
							((TileEntityCircuitMaker) te).decrStackSize(3, 1);
							if(((TileEntityCircuitMaker) te).getStackInSlot(4) == null)
								((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(recipes.get(i).getCircuit()));
							else
								((TileEntityCircuitMaker) te).setInventorySlotContents(4, new ItemStack(recipes.get(i).getCircuit(), ((TileEntityCircuitMaker)te).getStackInSlot(2).stackSize));						}
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
