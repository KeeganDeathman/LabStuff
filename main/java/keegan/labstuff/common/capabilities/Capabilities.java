package keegan.labstuff.common.capabilities;

import keegan.labstuff.network.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.*;

public class Capabilities {
	@CapabilityInject(LSPlayerStats.class)
	public static Capability<LSPlayerStats> LS_STATS_CAPABILITY = null;

	@CapabilityInject(LSPlayerStatsClient.class)
	public static Capability<LSPlayerStatsClient> LS_STATS_CLIENT_CAPABILITY = null;
	
	@CapabilityInject(IStrictEnergyStorage.class)
	public static Capability<IStrictEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

	@CapabilityInject(IStrictEnergyAcceptor.class)
	public static Capability<IStrictEnergyAcceptor> ENERGY_ACCEPTOR_CAPABILITY = null;

	@CapabilityInject(IGridTransmitter.class)
	public static Capability<IGridTransmitter> GRID_TRANSMITTER_CAPABILITY = null;

	@CapabilityInject(IBlockableConnection.class)
	public static Capability<IBlockableConnection> BLOCKABLE_CONNECTION_CAPABILITY = null;

	@CapabilityInject(ICableOutputter.class)
	public static Capability<ICableOutputter> CABLE_OUTPUTTER_CAPABILITY = null;

	@CapabilityInject(ITileNetwork.class)
	public static Capability<ITileNetwork> TILE_NETWORK_CAPABILITY = null;

	@CapabilityInject(IWrenchable.class)
	public static Capability<IWrenchable> WRENCHABLE_CAPABILITY = null;

	@CapabilityInject(IPlasmaHandler.class)
	public static Capability<IPlasmaHandler> PLASMA_HANDLER_CAPABILITY = null;
	@CapabilityInject(IResearcher.class)
	public static Capability<IResearcher> RESEARCHER_CAPABILITY = null;

	@CapabilityInject(IDataDevice.class)
	public static Capability<IDataDevice> DATA_DEVICE_CAPABILITY = null;
	
	
	public static final ResourceLocation LS_PLAYER_PROP = new ResourceLocation("labstuff:player_stats");
    public static final ResourceLocation LS_PLAYER_CLIENT_PROP = new ResourceLocation("labstuff:player_stats_client");


	public static void registerCapabilities() {
		DefaultStrictEnergyStorage.register();
		DefaultStrictEnergyAcceptor.register();
		DefaultGridTransmitter.register();
		DefaultBlockableConnection.register();
		DefaultCableOutputter.register();
		DefaultTileNetwork.register();
		DefaultWrenchable.register();
		DefaultPlasmaHandler.register();
		DefaultResearch.register();
		DefaultDataDevice.register();
		
		CapabilityManager.INSTANCE.register(LSPlayerStats.class, new Capability.IStorage<LSPlayerStats>()
        {
            @Override
            public NBTBase writeNBT(Capability<LSPlayerStats> capability, LSPlayerStats instance, EnumFacing side)
            {
                NBTTagCompound nbt = new NBTTagCompound();
                instance.saveNBTData(nbt);
                return nbt;
            }

            @Override
            public void readNBT(Capability<LSPlayerStats> capability, LSPlayerStats instance, EnumFacing side, NBTBase nbt)
            {
                instance.loadNBTData((NBTTagCompound) nbt);
            }
        }, StatsCapability.class);

        CapabilityManager.INSTANCE.register(LSPlayerStatsClient.class, new Capability.IStorage<LSPlayerStatsClient>()
        {
            @Override
            public NBTBase writeNBT(Capability<LSPlayerStatsClient> capability, LSPlayerStatsClient instance, EnumFacing side)
            {
                return null;
            }

            @Override
            public void readNBT(Capability<LSPlayerStatsClient> capability, LSPlayerStatsClient instance, EnumFacing side, NBTBase nbt) { }
        }, StatsClientCapability.class);
    }
}