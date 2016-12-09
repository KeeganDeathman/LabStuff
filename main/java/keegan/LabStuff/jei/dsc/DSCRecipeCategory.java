package keegan.labstuff.jei.dsc;


import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class DSCRecipeCategory extends BlankRecipeCategory<DSCRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public DSCRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.dsc";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull DSCRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 4, 42);
		guiItemStacks.init(1, true, 25, 42);
		guiItemStacks.init(2, true, 46, 42);
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getInputs().get(1));
		guiItemStacks.set(2, recipeWrapper.getInputs().get(2));
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Discovery Supercomputer";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/dsc-jei.png"), 0, 0, 64, 64);
	}
}
