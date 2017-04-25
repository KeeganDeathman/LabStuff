package keegan.labstuff.network;

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
