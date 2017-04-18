package keegan.labstuff.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class Squeeze 
{
	private ItemStack input;
	private FluidStack fluidOut;
	private ItemStack itemOut;
	
	
	
	public Squeeze(ItemStack input, FluidStack fluidOut, ItemStack itemOut) {
		this.input = input;
		this.fluidOut = fluidOut;
		this.itemOut = itemOut;
	}
	public ItemStack getInput() {
		return input;
	}
	public void setInput(ItemStack input) {
		this.input = input;
	}
	public FluidStack getFluidOut() {
		return fluidOut;
	}
	public void setFluidOut(FluidStack fluidOut) {
		this.fluidOut = fluidOut;
	}
	public ItemStack getItemOut() {
		return itemOut;
	}
	public void setItemOut(ItemStack itemOut) {
		this.itemOut = itemOut;
	}
}
