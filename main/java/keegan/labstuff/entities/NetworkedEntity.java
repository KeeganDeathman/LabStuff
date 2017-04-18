package keegan.labstuff.entities;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.*;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public abstract class NetworkedEntity extends Entity implements IPacketReceiver
{
    public NetworkedEntity(World par1World)
    {
        super(par1World);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            LabStuffMain.packetPipeline.sendToAllAround(new PacketDynamic(this), new TargetPoint(this.worldObj.provider.getDimension(), this.posX, this.posY, this.posZ, this.getPacketRange()));
            // PacketDispatcher.sendPacketToAllAround(this.posX, this.posY,
            // this.posZ, this.getPacketRange(),
            // this.worldObj.provider.getDimension(),
            // GCCorePacketManager.getPacket(GalacticraftCore.CHANNELENTITIES,
            // this, this.getNetworkedData(new ArrayList<Object>())));
        }
    }

    public abstract double getPacketRange();
}