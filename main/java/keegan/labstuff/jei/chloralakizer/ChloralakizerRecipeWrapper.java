package keegan.labstuff.jei.chloralakizer;

import java.util.*;

import javax.annotation.Nonnull;

import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ChloralakizerRecipeWrapper extends BlankRecipeWrapper
{
	
	private final ChloralakizerRecipe recipe;
	private final IDrawable slotDrawable;
	
	public ChloralakizerRecipeWrapper(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers, ChloralakizerRecipe recipe) {
		this.recipe = recipe;
		slotDrawable = guiHelpers.getSlotDrawable();
	}
	
	public ChloralakizerRecipe getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.input);
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.output1);
		list.add(recipe.output2);
		return list;
	}
	
	@Nonnull
	@Override
	public List<FluidStack> getFluidInputs() {
		List<FluidStack> list = new ArrayList<>();
		list.add(recipe.inwater);
		return list;
	}
	
	public static List<ChloralakizerRecipe> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<ChloralakizerRecipe> list = new ArrayList<ChloralakizerRecipe>();
		list.add(ChloralakizerRecipe.INSTANCE);
		return list;
	}
	

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setInputs(FluidStack.class, getFluidInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
