package keegan.labstuff.network;

import java.util.*;

import cofh.api.energy.*;
import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class CableUtils
{
	public static boolean isCable(TileEntity tileEntity)
	{
		if(tileEntity != null && CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null))
		{
			return TransmissionType.checkTransmissionType(CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, null), TransmissionType.ENERGY);
		}
		
		return false;
	}

	/**
	 * Gets the adjacent connections to a TileEntity, from a subset of its sides.
	 * @param tileEntity - center TileEntity
	 * @param sides - set of sides to check
	 * @return boolean[] of adjacent connections
	 */
	public static boolean[] getConnections(TileEntity tileEntity, Set<EnumFacing> sides)
	{
		boolean[] connectable = new boolean[] {false, false, false, false, false, false};
		Coord4D coord = Coord4D.get(tileEntity);

		for(EnumFacing side : sides)
		{
			TileEntity tile = coord.offset(side).getTileEntity(tileEntity.getWorld());

			connectable[side.ordinal()] = isValidAcceptorOnSide(tileEntity, tile, side);
			connectable[side.ordinal()] |= isCable(tile);
		}

		return connectable;
	}

	/**
	 * Gets the adjacent connections to a TileEntity, from a subset of its sides.
	 * @param cableEntity - TileEntity that's trying to connect
	 * @param side - side to check
	 * @return boolean whether the acceptor is valid
	 */
	public static boolean isValidAcceptorOnSide(TileEntity cableEntity, TileEntity tile, EnumFacing side)
	{
		if(tile == null || isCable(tile))
		{
			return false;
		}
		
		return isAcceptor(cableEntity, tile, side) || isOutputter(tile, side);
	}

	/**
	 * Gets all the connected cables around a specific tile entity.
	 * @param tileEntity - center tile entity
	 * @return TileEntity[] of connected cables
	 */
	public static TileEntity[] getConnectedOutputters(TileEntity tileEntity)
	{
		return getConnectedOutputters(tileEntity.getPos(), tileEntity.getWorld());
	}

	public static TileEntity[] getConnectedOutputters(BlockPos pos, World world)
	{
		TileEntity[] outputters = new TileEntity[] {null, null, null, null, null, null};

		for(EnumFacing orientation : EnumFacing.VALUES)
		{
			TileEntity outputter = world.getTileEntity(pos.offset(orientation));

			if(isOutputter(outputter, orientation))
			{
				outputters[orientation.ordinal()] = outputter;
			}
		}

		return outputters;
	}

	public static boolean isOutputter(TileEntity tileEntity, EnumFacing side)
	{
		if(tileEntity == null)
		{
			return false;
		}
		
		if(CapabilityUtils.hasCapability(tileEntity, Capabilities.CABLE_OUTPUTTER_CAPABILITY, side.getOpposite()))
		{
			ICableOutputter outputter = CapabilityUtils.getCapability(tileEntity, Capabilities.CABLE_OUTPUTTER_CAPABILITY, side.getOpposite());
			
			if(outputter != null && outputter.canOutputTo(side.getOpposite()))
			{
				return true;
			}
		}
		
		return false;
	}

	public static boolean isAcceptor(TileEntity orig, TileEntity tileEntity, EnumFacing side)
	{
		if(CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite()))
		{
			return false;
		}

		if(CapabilityUtils.hasCapability(tileEntity, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, side.getOpposite()))
		{
			if(CapabilityUtils.getCapability(tileEntity, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, side.getOpposite()).canReceiveEnergy(side.getOpposite()))
			{
				return true;
			}
		}

		return false;
	}

	public static void emit(IEnergyWrapper emitter)
	{
		if(!((TileEntity)emitter).getWorld().isRemote)
		{
			double energyToSend = Math.min(emitter.getEnergy(), emitter.getMaxOutput());

			if(energyToSend > 0)
			{
				List<EnumFacing> outputtingSides = new ArrayList<EnumFacing>();
				boolean[] connectable = getConnections((TileEntity)emitter, emitter.getOutputtingSides());

				for(EnumFacing side : emitter.getOutputtingSides())
				{
					if(connectable[side.ordinal()])
					{
						outputtingSides.add(side);
					}
				}

				if(outputtingSides.size() > 0)
				{
					double sent = 0;
					boolean tryAgain = false;
					int i = 0;

					do {
						double prev = sent;
						sent += emit_do(emitter, outputtingSides, energyToSend-sent, tryAgain);

						tryAgain = energyToSend-sent > 0 && sent-prev > 0 && i < 100;

						i++;
					} while(tryAgain);

					emitter.setEnergy(emitter.getEnergy() - sent);
				}
			}
		}
	}

	private static double emit_do(IEnergyWrapper emitter, List<EnumFacing> outputtingSides, double totalToSend, boolean tryAgain)
	{
		double remains = totalToSend%outputtingSides.size();
		double splitSend = (totalToSend-remains)/outputtingSides.size();
		double sent = 0;

		List<EnumFacing> toRemove = new ArrayList<EnumFacing>();

		for(EnumFacing side : outputtingSides)
		{
			TileEntity tileEntity = Coord4D.get((TileEntity)emitter).offset(side).getTileEntity(((TileEntity)emitter).getWorld());
			double toSend = splitSend+remains;
			remains = 0;

			double prev = sent;
			sent += emit_do_do(emitter, tileEntity, side, toSend, tryAgain);

			if(sent-prev == 0)
			{
				toRemove.add(side);
			}
		}

		for(EnumFacing side : toRemove)
		{
			outputtingSides.remove(side);
		}

		return sent;
	}

	private static double emit_do_do(IEnergyWrapper from, TileEntity tileEntity, EnumFacing side, double currentSending, boolean tryAgain)
	{
		double sent = 0;

		if(CapabilityUtils.hasCapability(tileEntity, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, side.getOpposite()))
		{
			IStrictEnergyAcceptor acceptor = CapabilityUtils.getCapability(tileEntity, Capabilities.ENERGY_ACCEPTOR_CAPABILITY, side.getOpposite());

			if(acceptor.canReceiveEnergy(side.getOpposite()))
			{
				sent += acceptor.transferEnergyToAcceptor(side.getOpposite(), currentSending);
			}
		}

		return sent;
	}
}