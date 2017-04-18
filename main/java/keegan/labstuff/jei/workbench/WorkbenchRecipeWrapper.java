package keegan.labstuff.jei.workbench;

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

public class WorkbenchRecipeWrapper extends BlankRecipeWrapper
{
	
	private final ResearchItem recipe;
	private final IDrawable slotDrawable;
	
	public WorkbenchRecipeWrapper(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers, ResearchItem recipe) {
		this.recipe = recipe;
		slotDrawable = guiHelpers.getSlotDrawable();
	}
	
	public ResearchItem getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		for(ItemStack stack : recipe.getIn())
			list.add(stack);
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(getRecipe().getOut());
		return list;
	}

	public static List<ResearchItem> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<ResearchItem> list = new ArrayList<ResearchItem>();
		for(ResearchItem design : Recipes.researchItems)
			list.add(design);
		return list;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		int xPos = (-(recipeWidth - slotDrawable.getWidth()) / 2) + 200;
		int yPos = -16;
		yPos += slotDrawable.getHeight() + 4;

		minecraft.fontRendererObj.drawString("Requires:", xPos, yPos, Color.black.getRGB());
		minecraft.fontRendererObj.drawString((getRecipe().getDependency() != null ? getRecipe().getDependency().getName() : "No research"), xPos, yPos+16, Color.BLACK.getRGB());
		minecraft.fontRendererObj.drawString(getRecipe().getPower() + " LV", xPos, yPos+32, Color.BLACK.getRGB());
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
