package keegan.labstuff.jei.fermenter;

import java.util.*;

import javax.annotation.Nonnull;

import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class FermenterRecipeWrapper extends BlankRecipeWrapper
{
	
	private final Ferment recipe;
	private final IDrawable slotDrawable;
	
	public FermenterRecipeWrapper(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers, Ferment recipe) {
		this.recipe = recipe;
		slotDrawable = guiHelpers.getSlotDrawable();
	}
	
	public Ferment getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.getInput());
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.getItemOut());
		return list;
	}
	
	
	@Nonnull
	@Override
	public List<FluidStack> getFluidOutputs() {
		List<FluidStack> list = new ArrayList<>();
		list.add(recipe.getFluidOut());
		return list;
	}

	public static List<Ferment> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<Ferment> list = new ArrayList<Ferment>();
		for(Ferment design : Recipes.fermentations)
			list.add(design);
		return list;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		int xPos = (-(recipeWidth - slotDrawable.getWidth()) / 2) + 200;
		int yPos = -16;
		yPos += slotDrawable.getHeight() + 4;

	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
		ingredients.setOutputs(FluidStack.class, getFluidOutputs());
	}
}
