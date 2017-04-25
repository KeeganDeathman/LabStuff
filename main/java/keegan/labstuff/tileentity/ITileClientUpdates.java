package keegan.labstuff.tileentity;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.*;


/***
 * Sends basic tile load configuration data
 * e.g. facing  (if not obtainable from blockState)
 * to the client.
 * 
 * IMPORTANT: call this.clientValidate() from the tile's validate() method
 */
public interface ITileClientUpdates
{
    /**
     * The supplied data array of ints
     * ALWAYS has length 4.  You don't
     * have to use all of them!
     */
    public void buildDataPacket(int[] data);
    
    /**
     * The supplied data list has 4 ints
     * of data to use at positions 1 through 4.
     */
    @SideOnly(Side.CLIENT)
    public void updateClient(List<Object> data);
    
    /**
     * Implement validate() in the tile and call this!
     */
    public default void clientValidate()
    {
        TileEntity tile = (TileEntity)this;
        if (tile.getWorld().isRemote)
        {
            LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_REQUEST_MACHINE_DATA, tile.getWorld(), new Object[] { tile.getPos() }));
        }
    }
    
    /**
     * Do not override unless you want to use a custom data packet
     * with more than 4 ints of data.
     * (If overriding this you must override all other methods in
     * ITileClientUpdates as well ... in which case, why are you using it?)
     */
    public default void sendUpdateToClient(EntityPlayerMP player)
    {
        int[] data = new int[4];
        this.buildDataPacket(data);
        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_UPDATE_MACHINE_DATA, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { ((TileEntity)this).getPos(), data[0], data[1], data[2], data[3] }), player);
    }
    
    /**
     * Used to push updates out to clients
     */
    public default void updateAllInDimension()
    {
        int[] data = new int[4];
        this.buildDataPacket(data);
        int dimID = LabStuffUtils.getDimensionID(((TileEntity)this).getWorld());
        LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(EnumSimplePacket.C_UPDATE_MACHINE_DATA, dimID, new Object[] { ((TileEntity)this).getPos(), data[0], data[1], data[2], data[3] }), dimID);
    }
}