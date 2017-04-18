package keegan.labstuff.jei.fermenter;


import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class FermenterRecipeCategory extends BlankRecipeCategory<FermenterRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public FermenterRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.fermenter";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull FermenterRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		
		guiItemStacks.init(0, true, 21,37);
		guiItemStacks.init(1, false, 78, 37);
		
		guiFluidStacks.init(0, false, 159, 1, 16, 100, 10000, true, null);
		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getOutputs().get(0));
		guiFluidStacks.set(0, recipeWrapper.getFluidOutputs().get(0));
		
		
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Fermenter";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/squeezer-jei.png"), 0, 0, 176, 101);
	}
}
