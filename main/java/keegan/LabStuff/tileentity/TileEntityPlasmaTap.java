package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.labstuff.common.capabilities.Capabilities;
import keegan.labstuff.network.IPlasmaHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityPlasmaTap extends TileEntity implements IPlasmaHandler
{
	private boolean running;
	private int plasma = 0;

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing side)
	{
		return capability == Capabilities.PLASMA_HANDLER_CAPABILITY || super.hasCapability(capability, side);
	}
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == Capabilities.PLASMA_HANDLER_CAPABILITY)
		{
			return (T) this;
		}
		
		return super.getCapability(capability, side);
	}
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public void burnPlasma() 
	{
		if(running)
			drainPlasma(250, null);
	}

	@Override
	public int getPlasma(EnumFacing from) {
		// TODO Auto-generated method stub
		return plasma;
	}

	@Override
	public int drainPlasma(int amount, EnumFacing from) {
		// TODO Auto-generated method stub
		if(amount < plasma)
		{
			plasma -= amount;
			return 0;
		}
		else
		{
			int oldPlasma = plasma;
			plasma = 0;
			return amount - oldPlasma;
		}
	}

	@Override
	public int transferPlasma(int amount, EnumFacing from) {
		if(Integer.MAX_VALUE - 20 - plasma < amount)
		{
			plasma = Integer.MAX_VALUE - 20;
			return amount - plasma;
		}
		plasma+=amount;
		return 0;
	}

	@Override
	public boolean canReceivePlasma(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDrainPlasma(EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean canConnectPlasma(EnumFacing opposite) {
		// TODO Auto-generated method stub
		return true;
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
