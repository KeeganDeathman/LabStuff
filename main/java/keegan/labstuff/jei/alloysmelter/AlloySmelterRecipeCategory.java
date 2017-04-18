package keegan.labstuff.jei.alloysmelter;


import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class AlloySmelterRecipeCategory extends BlankRecipeCategory<AlloySmelterRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public AlloySmelterRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.alloysmelter";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull AlloySmelterRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		
		guiItemStacks.init(0, true, 1, 1);
		guiItemStacks.init(1, true, 19, 1);
		guiItemStacks.init(2, true, 37, 1);
		guiItemStacks.init(3, false, 19, 77);
		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getInputs().get(1));
		guiItemStacks.set(2, recipeWrapper.getInputs().get(2));
		guiItemStacks.set(3, recipeWrapper.getOutputs().get(0));

		
		
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Alloy Smelter";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/alloysmelter-jei.png"), 0, 0, 54, 94);
	}
}
