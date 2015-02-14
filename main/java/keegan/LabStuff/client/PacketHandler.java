package LabStuff.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;
import LabStuff.tileentity.TileEntityCircuitDesignTable;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class PacketHandler implements IPacketHandler
{

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) 
	{
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
        EntityPlayer sender = (EntityPlayer) player;
        
        if(packet.channel.equals("LabStuff"))
        {
        	handlePacketCircuitDesignTable(data, sender.worldObj);
        }
		
	}
	
	private void handlePacketCircuitDesignTable(DataInputStream data, World world)
    {
        String circuitDesign;
        int x;
        int y;
        int z;
        
        try
        {
        	circuitDesign = data.readUTF();
        	x = data.readInt();
        	y = data.readInt();
        	z = data.readInt();
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        	return;
        }
        
        if(world.getBlockTileEntity(x, y, z) != null && world.getBlockTileEntity(x, y, z) instanceof TileEntityCircuitDesignTable)
        {
        	TileEntityCircuitDesignTable tecdt = (TileEntityCircuitDesignTable) world.getBlockTileEntity(x, y, z);
        	tecdt.circuitDesign = circuitDesign;
        	System.out.println("Design Handled");
        	System.out.println(circuitDesign);
        	world.markBlockForUpdate(x, y, z);
        }
    }

}
