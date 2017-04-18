package keegan.labstuff.network;

import keegan.labstuff.common.*;
import keegan.labstuff.common.capabilities.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public abstract class EnergyAcceptorWrapper implements IStrictEnergyAcceptor
{
	public Coord4D coord;

	public static EnergyAcceptorWrapper get(TileEntity tileEntity, EnumFacing side)
	{
		if(tileEntity == null || tileEntity.getWorld() == null)
		{
			return null;
		}
		
		EnergyAcceptorWrapper wrapper = null;
		
		wrapper = new LabStuffAcceptor(CapabilityUtils.getCapability(tileEntity, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, side));
		
		if(wrapper != null)
		{
			wrapper.coord = Coord4D.get(tileEntity);
		}
		
		return wrapper;
	}

	public abstract boolean needsEnergy(EnumFacing side);

	public static class LabStuffAcceptor extends EnergyAcceptorWrapper
	{
		private IStrictEnergyAcceptor acceptor;

		public LabStuffAcceptor(IStrictEnergyAcceptor mekAcceptor)
		{
			acceptor = mekAcceptor;
		}

		@Override
		public double transferEnergyToAcceptor(EnumFacing side, double amount)
		{
			return acceptor.transferEnergyToAcceptor(side, amount);
		}

		@Override
		public boolean canReceiveEnergy(EnumFacing side)
		{
			return acceptor.canReceiveEnergy(side);
		}

		@Override
		public double getEnergy()
		{
			return acceptor.getEnergy();
		}

		@Override
		public void setEnergy(double energy)
		{
			acceptor.setEnergy(energy);
		}

		@Override
		public double getMaxEnergy()
		{
			return acceptor.getMaxEnergy();
		}

		@Override
		public boolean needsEnergy(EnumFacing side)
		{
			return acceptor.getMaxEnergy() - acceptor.getEnergy() > 0;
		}
	}
}