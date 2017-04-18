package keegan.labstuff.common.capabilities;

import java.util.ArrayList;

import io.netty.buffer.ByteBuf;
import keegan.labstuff.common.capabilities.DefaultStorageHelper.NullStorage;
import keegan.labstuff.network.ITileNetwork;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultTileNetwork implements ITileNetwork
{
	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {}

	@Override
	public ArrayList<Object> getNetworkedData(ArrayList<Object> data) 
	{
		return data;
	}
	
	public static void register()
	{
        CapabilityManager.INSTANCE.register(ITileNetwork.class, new NullStorage<>(), DefaultTileNetwork.class);
	}
}