package keegan.labstuff.recipes;

import net.minecraft.item.Item;

public class CircuitDesign 
{
	private String name;
	private Item designSheet;
	
	public CircuitDesign(String n, Item d)
	{
		name = n;
		designSheet = d;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Item getDesignSheet()
	{
		return designSheet;
	}
}
