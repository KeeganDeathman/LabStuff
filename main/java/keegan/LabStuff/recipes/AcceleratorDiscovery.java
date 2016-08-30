package keegan.labstuff.recipes;

import net.minecraft.item.ItemStack;

public class AcceleratorDiscovery
{
	private AcceleratorDiscovery dependency;
	private ItemStack discoveryFlashDrive;
	private int index;
	
	public AcceleratorDiscovery(AcceleratorDiscovery dependency, ItemStack discoveryFlashDrive, int index)
	{
		this.dependency = dependency;
		this.discoveryFlashDrive = discoveryFlashDrive;
		this.index = index;
	}
	
	public AcceleratorDiscovery getDependency()
	{
		return dependency;
	}
	public void setDependency(AcceleratorDiscovery dependency)
	{
		this.dependency = dependency;
	}
	public ItemStack getDiscoveryFlashDrive()
	{
		return discoveryFlashDrive;
	}
	public void setDiscoveryFlashDrive(ItemStack discoveryFlashDrive)
	{
		this.discoveryFlashDrive = discoveryFlashDrive;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}
	
	@Override
	public boolean equals(Object object)
	{
	    boolean isEqual= false;

	    if (object != null && object instanceof AcceleratorDiscovery)
	    {
	        isEqual = (getIndex() == ((AcceleratorDiscovery) object).getIndex());
	    }

	    return isEqual;
	}

	@Override
	public int hashCode() {
	    return this.index;
	}
	
}
