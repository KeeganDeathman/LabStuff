package keegan.labstuff.network;

import java.util.EnumSet;

import net.minecraft.util.EnumFacing;

public interface IPlasmaHandler 
{
	public int getPlasma(EnumFacing from);
	
	public int drainPlasma(int amount, EnumFacing from);
	
	public int transferPlasma(int amount, EnumFacing from);
	
	public boolean canReceivePlasma(EnumFacing from);
	
	public boolean canDrainPlasma(EnumFacing from);

	public boolean canConnectPlasma(EnumFacing from);

	public void setPlasma(int d, EnumFacing from);

	public EnumSet<EnumFacing> getOutputtingSides();

}
