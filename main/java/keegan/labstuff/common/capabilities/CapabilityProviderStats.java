package keegan.labstuff.common.capabilities;

import java.lang.ref.WeakReference;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;

public class CapabilityProviderStats implements ICapabilitySerializable<NBTTagCompound>
{
    private EntityPlayerMP owner;
    private LSPlayerStats statsCapability;

    public CapabilityProviderStats(EntityPlayerMP owner)
    {
        this.owner = owner;
        this.statsCapability = Capabilities.LS_STATS_CAPABILITY.getDefaultInstance();
        this.statsCapability.setPlayer(new WeakReference<>(this.owner));
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == Capabilities.LS_STATS_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (Capabilities.LS_STATS_CAPABILITY != null && capability == Capabilities.LS_STATS_CAPABILITY)
        {
            return Capabilities.LS_STATS_CAPABILITY.cast(statsCapability);
        }

        return null;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        statsCapability.saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        statsCapability.loadNBTData(nbt);
    }
}