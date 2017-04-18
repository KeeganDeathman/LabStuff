package keegan.labstuff.recipes;

import net.minecraft.item.ItemStack;

public class ResearchItem
{
	private int power;
	private Research dependency;
	private ItemStack[] in;
	private ItemStack out;
	
	
	public ResearchItem(int power, Research dependency, ItemStack[] in, ItemStack out)
	{
		this.power = power;
		this.dependency = dependency;
		this.in = in;
		this.out = out;
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

	    if (object != null && object instanceof ResearchItem)
	    {
	        isEqual = (dependency.equals(((ResearchItem)object).dependency)) && (this.power == ((ResearchItem)object).power) && (this.in.equals(((ResearchItem)object).in) && (this.out.equals(((ResearchItem)object).out)));
	    }

	    return isEqual;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public ItemStack[] getIn() {
		return in;
	}

	public void setIn(ItemStack[] in) {
		this.in = in;
	}

	public ItemStack getOut() {
		return out;
	}

	public void setOut(ItemStack out) {
		this.out = out;
	}


	
}
