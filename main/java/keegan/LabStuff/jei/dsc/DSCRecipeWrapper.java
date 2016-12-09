package keegan.labstuff.jei.dsc;

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

public class DSCRecipeWrapper extends BlankRecipeWrapper
{
	
	private final DiscoveryItem recipe;
	private final IDrawable slotDrawable;
	
	public DSCRecipeWrapper(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers, DiscoveryItem recipe) {
		this.recipe = recipe;
		slotDrawable = guiHelpers.getSlotDrawable();
	}
	
	public DiscoveryItem getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(recipe.getIngredients1());
		list.add(recipe.getIngredients2());
		list.add(recipe.getIngredients3());
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(getRecipe().getResult());
		return list;
	}

	public static List<DiscoveryItem> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<DiscoveryItem> list = new ArrayList<DiscoveryItem>();
		for(DiscoveryItem design : Recipes.discoveryItems)
			list.add(design);
		return list;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		int xPos = (recipeWidth - slotDrawable.getWidth());
		int yPos = 0;
		xPos = -(recipeWidth - slotDrawable.getWidth()) / 2;
		yPos += slotDrawable.getHeight() + 4;

		minecraft.fontRendererObj.drawString(getRecipe().getName(), xPos, yPos, Color.black.getRGB());
		minecraft.fontRendererObj.drawString(getRecipe().getDependency().getDiscoveryFlashDrive().getDisplayName(), xPos, yPos+12, Color.black.getRGB());
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
