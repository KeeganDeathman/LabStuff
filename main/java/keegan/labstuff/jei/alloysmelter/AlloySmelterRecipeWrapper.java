package keegan.labstuff.jei.alloysmelter;

import java.util.*;

import javax.annotation.Nonnull;

import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class AlloySmelterRecipeWrapper extends BlankRecipeWrapper
{
	
	private final Alloy recipe;
	private final IDrawable slotDrawable;
	
	public AlloySmelterRecipeWrapper(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers, Alloy recipe) {
		this.recipe = recipe;
		slotDrawable = guiHelpers.getSlotDrawable();
	}
	
	public Alloy getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.in1);
		list.add(recipe.in2);
		list.add(recipe.in3);
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.out);
		return list;
	}
	
	
	public static List<Alloy> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<Alloy> list = new ArrayList<Alloy>();
		for(Alloy alloy : Recipes.alloys)
			list.add(alloy);
		return list;
	}
	

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
