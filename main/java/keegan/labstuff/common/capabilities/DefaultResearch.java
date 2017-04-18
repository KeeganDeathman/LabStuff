package keegan.labstuff.common.capabilities;

import java.util.ArrayList;

import keegan.labstuff.common.capabilities.DefaultStorageHelper.NullStorage;
import keegan.labstuff.network.IPlasmaHandler;
import keegan.labstuff.recipes.Research;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultResearch implements IResearcher
	{
		private ArrayList<Research> r = new ArrayList<Research>();

		@Override
		public boolean hasResearch(Research r) {
			// TODO Auto-generated method stub
			return this.r.contains(r);
		}

		@Override
		public ArrayList<Research> getResearched() {
			// TODO Auto-generated method stub
			return r;
		}

		@Override
		public void research(Research r) {
			// TODO Auto-generated method stub
			this.r.add(r);
		}

		public static void register()
	    {
	        CapabilityManager.INSTANCE.register(IResearcher.class, new Storage(), DefaultResearch.class);
	    }
	}
