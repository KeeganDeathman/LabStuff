package keegan.labstuff.network;

import keegan.labstuff.network.IDataDevice;

public class DataPackage
{
	private IDataDevice target;
	private String message;
	
	public DataPackage(IDataDevice target, String message) {
		this.target = target;
		this.message = message;
	}

	public IDataDevice getTarget() {
		return target;
	}

	public String getMessage() {
		return message;
	}
}
