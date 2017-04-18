package keegan.labstuff.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class Reaction 
{
	private FluidStack input1;
	private FluidStack input2;
	private FluidStack output1;
	private FluidStack output2;
	private ItemStack input1Item;
	private ItemStack input2Item;
	private ItemStack output1Item;
	private ItemStack output2Item;
	private int power;
	
	public Reaction(FluidStack input1, FluidStack input2, FluidStack output1, FluidStack output2, ItemStack input1Item,
			ItemStack input2Item, ItemStack output1Item, ItemStack output2Item, int power) {
		super();
		this.input1 = input1;
		this.input2 = input2;
		this.output1 = output1;
		this.output2 = output2;
		this.input1Item = input1Item;
		this.input2Item = input2Item;
		this.output1Item = output1Item;
		this.output2Item = output2Item;
		this.power = power;
	}
	
	public FluidStack getInput1() {
		return input1;
	}
	public void setInput1(FluidStack input1) {
		this.input1 = input1;
	}
	public FluidStack getInput2() {
		return input2;
	}
	public void setInput2(FluidStack input2) {
		this.input2 = input2;
	}
	public FluidStack getOutput1() {
		return output1;
	}
	public void setOutput1(FluidStack output1) {
		this.output1 = output1;
	}
	public FluidStack getOutput2() {
		return output2;
	}
	public void setOutput2(FluidStack output2) {
		this.output2 = output2;
	}
	public ItemStack getInput1Item() {
		return input1Item;
	}
	public void setInput1Item(ItemStack input1Item) {
		this.input1Item = input1Item;
	}
	public ItemStack getInput2Item() {
		return input2Item;
	}
	public void setInput2Item(ItemStack input2Item) {
		this.input2Item = input2Item;
	}
	public ItemStack getOutput1Item() {
		return output1Item;
	}
	public void setOutput1Item(ItemStack output1Item) {
		this.output1Item = output1Item;
	}
	public ItemStack getOutput2Item() {
		return output2Item;
	}
	public void setOutput2Item(ItemStack output2Item) {
		this.output2Item = output2Item;
	}
	
	public void setPower(int p)
	{
		power = p;
	}
	public int getPower()
	{
		return power;
	}
	
	
}
