package keegan.labstuff.jei.reactionchamber;

import java.awt.Color;
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

public class ReactionChamberRecipeWrapper extends BlankRecipeWrapper
{
	
	private final Reaction recipe;
	private final IDrawable slotDrawable;
	
	public ReactionChamberRecipeWrapper(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers, Reaction recipe) {
		this.recipe = recipe;
		slotDrawable = guiHelpers.getSlotDrawable();
	}
	
	public Reaction getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.getInput1Item());
		list.add(recipe.getInput2Item());
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.getOutput1Item());
		list.add(recipe.getOutput2Item());
		return list;
	}
	
	@Nonnull
	@Override
	public List<FluidStack> getFluidInputs() {
		List<FluidStack> list = new ArrayList<>();
		list.add(recipe.getInput1());
		list.add(recipe.getInput2());
		return list;
	}
	
	@Nonnull
	@Override
	public List<FluidStack> getFluidOutputs() {
		List<FluidStack> list = new ArrayList<>();
		list.add(recipe.getOutput1());
		list.add(recipe.getOutput2());
		return list;
	}

	public static List<Reaction> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<Reaction> list = new ArrayList<Reaction>();
		for(Reaction design : Recipes.reactions)
			list.add(design);
		return list;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		int xPos = (-(recipeWidth - slotDrawable.getWidth()) / 2) + 200;
		int yPos = -16;
		yPos += slotDrawable.getHeight() + 4;

		minecraft.fontRendererObj.drawString("Requires:", xPos, yPos, Color.black.getRGB());
		minecraft.fontRendererObj.drawString(getRecipe().getPower() + " LV", xPos, yPos+32, Color.BLACK.getRGB());
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setInputs(FluidStack.class, getFluidInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
		ingredients.setOutputs(FluidStack.class, getFluidOutputs());
	}
}
