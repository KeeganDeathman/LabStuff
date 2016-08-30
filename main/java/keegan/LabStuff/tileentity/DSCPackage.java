package keegan.labstuff.tileentity;

public class DSCPackage
{
	private DSCPart target;
	private DSCPart sender;
	private String message;

	public DSCPackage(DSCPart target, DSCPart sender, String message)
	{
		super();
		this.target = target;
		this.sender = sender;
		this.message = message;
	}

	public DSCPart getTarget()
	{
		return target;
	}

	public String getMessage()
	{
		return message;
	}

	public DSCPart getSender()
	{
		return sender;
	}
}
