package keegan.labstuff.network;


import java.util.EnumSet;

import net.minecraft.inventory.IInventory;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.InterfaceList;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;

public interface IEnergyWrapper extends IStrictEnergyStorage, IStrictEnergyAcceptor, ICableOutputter, IInventory
{
	public EnumSet<EnumFacing> getOutputtingSides();

	public EnumSet<EnumFacing> getConsumingSides();

	public double getMaxOutput();
	
	public double removeEnergyFromProvider(EnumFacing side, double amount);
}