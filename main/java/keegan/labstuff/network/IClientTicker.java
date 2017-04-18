package keegan.labstuff.network;

public interface IClientTicker
{
	public void clientTick();

	public boolean needsTicks();
}