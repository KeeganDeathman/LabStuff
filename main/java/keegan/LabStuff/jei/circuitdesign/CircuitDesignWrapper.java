package keegan.labstuff.jei.circuitdesign;

import java.awt.Color;
import java.util.*;

import javax.annotation.Nonnull;

import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CircuitDesignWrapper extends BlankRecipeWrapper
{
	
	private final CircuitDesign recipe;
	private final IDrawable slotDrawable;
	
	public CircuitDesignWrapper(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers, CircuitDesign recipe) {
		this.recipe = recipe;
		slotDrawable = guiHelpers.getSlotDrawable();
	}
	
	public CircuitDesign getRecipe()
	{
		return recipe;
	}

	@Nonnull
	@Override
	public List<ItemStack> getInputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(Items.PAPER));
		return list;
	}

	@Nonnull
	@Override
	public List<ItemStack> getOutputs() {
		List<ItemStack> list = new ArrayList<>();
		list.add(new ItemStack(getRecipe().getDesignSheet()));
		return list;
	}

	public static List<CircuitDesign> getRecipes(IJeiHelpers jeiHelpers) 
	{
		List<CircuitDesign> list = new ArrayList<CircuitDesign>();
		for(CircuitDesign design : Recipes.cicruitDesigns)
			list.add(design);
		return list;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		int xPos = (recipeWidth - slotDrawable.getWidth()) / 2;
		int yPos = 0;
		xPos = 0;
		yPos += slotDrawable.getHeight() + 4;

		minecraft.fontRendererObj.drawString(getRecipe().getName(), xPos, yPos, Color.black.getRGB());
	}

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, getInputs());
		ingredients.setOutputs(ItemStack.class, getOutputs());
	}
}
