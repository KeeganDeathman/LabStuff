package keegan.labstuff.jei.enricher;

import java.util.*;

import javax.annotation.Nonnull;

import keegan.labstuff.recipes.*;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class EnricherRecipeWrapper extends BlankRecipeWrapper
{
	
	private final Enrichment recipe;
	
	public EnricherRecipeWrapper(IJeiHelpers jeiHelpers, Enrichment recipe) {
		this.recipe = recipe;
	}
	
	public Enrichment getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(getRecipe().getInput()));
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(getRecipe().getOutput(), 2));
		return list;
	}

	public static List<Enrichment> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<Enrichment> list = new ArrayList<Enrichment>();
		for(Enrichment enrichment : Recipes.enrichments)
			list.add(enrichment);
		return list;
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
