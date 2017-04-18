package keegan.labstuff.entities;

/**
 * Implement into entities to allow transmission of data via telemetry
 */
public interface ITelemetry
{
	public void transmitData(int[] data);
	
	public void receiveData(int[] data, String[] str);
	
	public void adjustDisplay(int[] data);
}