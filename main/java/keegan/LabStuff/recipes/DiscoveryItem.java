package keegan.labstuff.recipes;

import net.minecraft.item.ItemStack;

public class DiscoveryItem
{
	private ItemStack result;
	private ItemStack ingredients1;
	private ItemStack ingredients2;
	private ItemStack ingredients3;
	private AcceleratorDiscovery dependency;
	private String name;
	private int index;
	
	public DiscoveryItem(ItemStack r, ItemStack i1, ItemStack i2, ItemStack i3, AcceleratorDiscovery d, String name, int index)
	{
		result = r;
		ingredients1 = i1;
		ingredients2 = i2;
		ingredients3 = i3;
		dependency = d;
		this.name = name;
		this.index = index;
	}

	public ItemStack getResult()
	{
		return result;
	}

	public void setResult(ItemStack result)
	{
		this.result = result;
	}

	public ItemStack getIngredients1()
	{
		return ingredients1;
	}

	public void setIngredients1(ItemStack ingredients1)
	{
		this.ingredients1 = ingredients1;
	}

	public ItemStack getIngredients2()
	{
		return ingredients2;
	}

	public void setIngredients2(ItemStack ingredients2)
	{
		this.ingredients2 = ingredients2;
	}

	public ItemStack getIngredients3()
	{
		return ingredients3;
	}

	public void setIngredients3(ItemStack ingredients3)
	{
		this.ingredients3 = ingredients3;
	}

	public AcceleratorDiscovery getDependency()
	{
		return dependency;
	}

	public void setDependency(AcceleratorDiscovery dependency)
	{
		this.dependency = dependency;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getIndex()
	{
		// TODO Auto-generated method stub
		return index;
	}
}
