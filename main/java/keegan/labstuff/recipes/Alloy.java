package keegan.labstuff.recipes;

import net.minecraft.item.ItemStack;

public class Alloy 
{
	public ItemStack in1;
	public ItemStack in2;
	public ItemStack in3;
	public ItemStack out;
	
	public Alloy(ItemStack in1, ItemStack in2, ItemStack in3, ItemStack out) {
		super();
		this.in1 = in1;
		this.in2 = in2;
		this.in3 = in3;
		this.out = out;
	}

	public ItemStack getIn1() {
		return in1;
	}

	public void setIn1(ItemStack in1) {
		this.in1 = in1;
	}

	public ItemStack getIn2() {
		return in2;
	}

	public void setIn2(ItemStack in2) {
		this.in2 = in2;
	}

	public ItemStack getIn3() {
		return in3;
	}

	public void setIn3(ItemStack in3) {
		this.in3 = in3;
	}

	public ItemStack getOut() {
		return out;
	}

	public void setOut(ItemStack out) {
		this.out = out;
	}
	
}
