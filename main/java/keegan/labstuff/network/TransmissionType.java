package keegan.labstuff.network;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.translation.I18n;

public enum TransmissionType
{
	ENERGY("EnergyNetwork", "Energy"),
	FLUID("FluidNetwork", "Fluids"),
	PLASMA("PlasmaNetwork", "Plasma"), 
	DATA("DataNetwork", "Data");
	
	private String name;
	private String transmission;
	
	private TransmissionType(String n, String t)
	{
		name = n;
		transmission = t;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getTransmission()
	{
		return transmission;
	}
	
	public String localize()
	{
		return I18n.translateToLocal("transmission." + getTransmission());
	}

	public static boolean checkTransmissionType(ITransmitter sideTile, TransmissionType type)
	{
		return type.checkTransmissionType(sideTile);
	}

	public static boolean checkTransmissionType(TileEntity tile1, TransmissionType type)
	{
		return checkTransmissionType(tile1, type, null);
	}

	public static boolean checkTransmissionType(TileEntity tile1, TransmissionType type, TileEntity tile2)
	{
		return type.checkTransmissionType(tile1, tile2);
	}

	public boolean checkTransmissionType(ITransmitter transmitter)
	{
		return transmitter.getTransmissionType() == this;
	}

	public boolean checkTransmissionType(TileEntity sideTile, TileEntity currentTile)
	{
		if(sideTile instanceof ITransmitter)
		{
			if(((ITransmitter)sideTile).getTransmissionType() == this)
			{
				return true;
			}
		}

		return false;
	}
}