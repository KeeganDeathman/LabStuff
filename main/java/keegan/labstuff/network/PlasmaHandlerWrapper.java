package keegan.labstuff.network;

import java.util.EnumSet;

import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.EnergyAcceptorWrapper.LabStuffAcceptor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class PlasmaHandlerWrapper implements IPlasmaHandler
{
	
	Coord4D coord;
	
	public abstract boolean needsPlasma(EnumFacing side);
	
	public static PlasmaHandlerWrapper get(TileEntity tileEntity, EnumFacing side)
	{
		if(tileEntity == null || tileEntity.getWorld() == null)
		{
			return null;
		}
		
		PlasmaHandlerWrapper wrapper = null;
		
		wrapper = new PlasmaWrapper(CapabilityUtils.getCapability(tileEntity, Capabilities.PLASMA_HANDLER_CAPABILITY, side));
		
		if(wrapper != null)
		{
			wrapper.coord = Coord4D.get(tileEntity);
		}
		
		return wrapper;
	}
	
	public static class PlasmaWrapper extends PlasmaHandlerWrapper
	{
		private IPlasmaHandler acceptor;

		public PlasmaWrapper(IPlasmaHandler plasmaAcceptor)
		{
			acceptor = plasmaAcceptor;
		}
		
		public int getPlasma(EnumFacing from)
		{
			return acceptor.getPlasma(from);
		}
		
		public int drainPlasma(int amount, EnumFacing from)
		{
			return acceptor.drainPlasma(amount, from);
		}
		
		public int transferPlasma(int amount, EnumFacing from)
		{
			return acceptor.transferPlasma(amount, from);		
		}
		
		public boolean canReceivePlasma(EnumFacing side)
		{
			return acceptor.canReceivePlasma(side);
		}
		
		public boolean canDrainPlasma(EnumFacing side)
		{
			return acceptor.canDrainPlasma(side);
		}

		@Override
		public boolean needsPlasma(EnumFacing side) 
		{
			return Integer.MAX_VALUE - 20 - acceptor.getPlasma(side) > 0;
		}

		@Override
		public boolean canConnectPlasma(EnumFacing opposite) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void setPlasma(int d, EnumFacing from) {
			acceptor.setPlasma(d, from);
		}

		@Override
		public EnumSet<EnumFacing> getOutputtingSides() {
			// TODO Auto-generated method stub
			return acceptor.getOutputtingSides();
		}

	}
}
