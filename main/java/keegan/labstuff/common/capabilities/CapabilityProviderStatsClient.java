package keegan.labstuff.common.capabilities;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;

public class CapabilityProviderStatsClient implements ICapabilityProvider
{
    private EntityPlayerSP owner;
    private LSPlayerStatsClient statsCapability;

    public CapabilityProviderStatsClient(EntityPlayerSP owner)
    {
        this.owner = owner;
        this.statsCapability = Capabilities.LS_STATS_CLIENT_CAPABILITY.getDefaultInstance();
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        return capability == Capabilities.LS_STATS_CLIENT_CAPABILITY;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if (Capabilities.LS_STATS_CLIENT_CAPABILITY != null && capability == Capabilities.LS_STATS_CLIENT_CAPABILITY)
        {
            return Capabilities.LS_STATS_CLIENT_CAPABILITY.cast(statsCapability);
        }

        return null;
    }
}