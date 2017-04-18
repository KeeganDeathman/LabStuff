package keegan.labstuff.common.capabilities;

import java.util.EnumSet;

import keegan.labstuff.common.capabilities.DefaultStorageHelper.NullStorage;
import keegan.labstuff.network.IPlasmaHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DefaultPlasmaHandler implements IPlasmaHandler
{

	private int plasma = 0;
	
	@Override
	public int getPlasma(EnumFacing from) {
		// TODO Auto-generated method stub
		return plasma;
	}

	@Override
	public int drainPlasma(int amount, EnumFacing from) {
		// TODO Auto-generated method stub
		if(plasma < amount)
		{
			int oldPlasma = plasma;
			plasma = 0;
			return amount - oldPlasma;
		}
		else
		{
			plasma-=amount;
			return 0;
		}
	}

	@Override
	public int transferPlasma(int amount, EnumFacing from) {
		if(!canReceivePlasma(from))
		{
			return 0;
		}

		setPlasma(getPlasma(from) + amount, from);

		return amount;
	}
	
	public static void register()
    {
        CapabilityManager.INSTANCE.register(IPlasmaHandler.class, new NullStorage<>(), DefaultPlasmaHandler.class);
    }

	@Override
	public boolean canReceivePlasma(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDrainPlasma(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canConnectPlasma(EnumFacing opposite) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPlasma(int d, EnumFacing from) {
		plasma = d;
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return EnumSet.noneOf(EnumFacing.class);
	}
}
