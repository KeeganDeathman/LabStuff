package keegan.labstuff.network;

import keegan.labstuff.LabStuffMain;
import net.minecraft.item.*;

public enum TransmitterType
{
	POWERCABLE("powercable", Size.SMALL, TransmissionType.ENERGY, false, 0, 0),
	LIQUIDPIPE("liquidpipe", Size.LARGE, TransmissionType.FLUID, false, 0, 0),
	PLASMAPIPE("plasmapipe", Size.LARGE, TransmissionType.PLASMA, false, 0,0),
	DATACABLE("datacable", Size.LARGE, TransmissionType.DATA, false, 0,0);

	private String unlocalizedName;
	private Size size;
	private TransmissionType transmissionType;
	private boolean transparencyRender;
	private int[] indexes;

	private TransmitterType(String name, Size s, TransmissionType type, boolean transparency, int... is)
	{
		unlocalizedName = name;
		size = s;
		transmissionType = type;
		transparencyRender = transparency;
		indexes = is;
	}
	
	public String getName()
	{
		return unlocalizedName;
	}

	public Size getSize()
	{
		return size;
	}
	
	public boolean hasTransparency()
	{
		return transparencyRender;
	}

	public TransmissionType getTransmission()
	{
		return transmissionType;
	}

	public static enum Size
	{
		SMALL(6),
		LARGE(8);

		public int centerSize;

		private Size(int size)
		{
			centerSize = size;
		}
	}
}