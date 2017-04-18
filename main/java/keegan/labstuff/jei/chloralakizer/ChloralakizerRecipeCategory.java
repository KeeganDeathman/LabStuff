package keegan.labstuff.jei.chloralakizer;


import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class ChloralakizerRecipeCategory extends BlankRecipeCategory<ChloralakizerRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public ChloralakizerRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.chloralakizer";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ChloralakizerRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		
		guiItemStacks.init(0, true, 33, 53);
		guiItemStacks.init(1, false, 77, 31);
		guiItemStacks.init(2, false, 77, 75);
		guiFluidStacks.init(0, true, 2, 2, 16, 100, 10000, true, null);
		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getOutputs().get(0));
		guiItemStacks.set(2, recipeWrapper.getOutputs().get(1));
		guiFluidStacks.set(0, recipeWrapper.getFluidInputs().get(0));
		
		
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Chloralakizer";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/chloralakizer-jei.png"), 0, 0, 96, 104);
	}
}
