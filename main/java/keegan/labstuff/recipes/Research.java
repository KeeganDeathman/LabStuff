package keegan.labstuff.recipes;


public class Research
{
	private Research dependency;
	private String name;
	
	public Research(Research dependency,String nom)
	{
		this.dependency = dependency;
		this.name = nom;
	}
	
	public Research getDependency()
	{
		return dependency;
	}
	public void setDependency(Research dependency)
	{
		this.dependency = dependency;
	}

	
	@Override
	public boolean equals(Object object)
	{
	    boolean isEqual= false;

	    if (object != null && object instanceof Research && name.equals(((Research)object).name))
	    {
	        isEqual = (dependency.equals(((Research)object).dependency));
	    }

	    return isEqual;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
