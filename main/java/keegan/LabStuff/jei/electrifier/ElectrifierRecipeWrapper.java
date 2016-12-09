package keegan.labstuff.jei.electrifier;

import java.util.*;

import javax.annotation.Nonnull;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class ElectrifierRecipeWrapper extends BlankRecipeWrapper
{
	
	private final ElectrifierRecipe recipe;
	
	public ElectrifierRecipeWrapper(IJeiHelpers jeiHelpers, ElectrifierRecipe recipe) {
		this.recipe = recipe;
	}
	
	public ElectrifierRecipe getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		return getRecipe().getInputs();
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		return getRecipe().getOutputs();
	}

	public static List<ElectrifierRecipe> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<ElectrifierRecipe> list = new ArrayList<ElectrifierRecipe>();
		list.add(new ElectrifierRecipe());
		return list;
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
