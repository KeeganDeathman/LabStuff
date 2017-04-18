package keegan.labstuff.common.capabilities;

import keegan.labstuff.common.capabilities.DefaultStorageHelper.NullStorage;
import keegan.labstuff.network.IDataDevice;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultDataDevice implements IDataDevice
	{

		public static void register()
	    {
	        CapabilityManager.INSTANCE.register(IDataDevice.class, new NullStorage<>(), DefaultDataDevice.class);
	    }

		@Override
		public void performAction(String command) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getDeviceType() {
			// TODO Auto-generated method stub
			return null;
		}
	}
