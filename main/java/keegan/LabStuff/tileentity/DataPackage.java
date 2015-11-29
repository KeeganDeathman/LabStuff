package keegan.labstuff.tileentity;

import keegan.labstuff.tileentity.DataConnectedDevice;

public class DataPackage
{
	private DataConnectedDevice target;
	private String message;
	
	public DataPackage(DataConnectedDevice target, String message) {
		super();
		this.target = target;
		this.message = message;
	}

	public DataConnectedDevice getTarget() {
		return target;
	}

	public String getMessage() {
		return message;
	}
}
