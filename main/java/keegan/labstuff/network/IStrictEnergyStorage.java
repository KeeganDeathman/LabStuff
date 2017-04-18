package keegan.labstuff.network;

public interface IStrictEnergyStorage
{
	/**
	 * Gets the amount of energy this TileEntity is currently storing.
	 * @return stored energy
	 */
	public double getEnergy();

	/**
	 * Sets the amount of stored energy of this TileEntity to a new amount.
	 * @param energy - new energy value
	 */
	public void setEnergy(double energy);

	/**
	 * Gets the maximum amount of energy this TileEntity can store.
	 * @return maximum energy
	 */
	public double getMaxEnergy();
}