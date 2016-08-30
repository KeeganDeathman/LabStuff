package keegan.labstuff.recipes;

import net.minecraft.item.Item;

public class Charge 
{
	private Item deadItem;
	private Item chargedItem;
	private int power;
	
	public Charge(Item deadItem, Item chargedItem, int power) {
		super();
		this.deadItem = deadItem;
		this.chargedItem = chargedItem;
		this.power = power;
	}
	
	public Item getDeadItem() {
		return deadItem;
	}
	public void setDeadItem(Item deadItem) {
		this.deadItem = deadItem;
	}
	public Item getChargedItem() {
		return chargedItem;
	}
	public void setChargedItem(Item chargedItem) {
		this.chargedItem = chargedItem;
	}
	public int getPower() {
		return power;
	}
	public void setPower(int power) {
		this.power = power;
	}
}
