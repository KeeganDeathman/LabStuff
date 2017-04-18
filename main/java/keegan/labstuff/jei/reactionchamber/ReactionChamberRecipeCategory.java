package keegan.labstuff.jei.reactionchamber;


import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class ReactionChamberRecipeCategory extends BlankRecipeCategory<ReactionChamberRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public ReactionChamberRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.reactionchamber";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ReactionChamberRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		
		guiItemStacks.init(0, true, 54,45);
		guiItemStacks.init(1, true, 72, 45);
		guiItemStacks.init(2, false, 101, 45);
		guiItemStacks.init(3, false, 119, 45);
		
		guiFluidStacks.init(0, true, 1, 1, 16, 100, 10000, true, null);
		guiFluidStacks.init(1, true, 19, 1, 16, 100, 10000, true, null);
		guiFluidStacks.init(2, false, 152, 1, 16, 100, 10000, true, null);
		guiFluidStacks.init(3, false, 170, 1, 16, 100, 10000, true, null);
		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getInputs().get(1));
		guiFluidStacks.set(0, recipeWrapper.getFluidInputs().get(0));
		guiFluidStacks.set(1, recipeWrapper.getFluidInputs().get(1));
		
		guiItemStacks.set(2, recipeWrapper.getOutputs().get(0));
		guiItemStacks.set(3, recipeWrapper.getOutputs().get(1));
		guiFluidStacks.set(2, recipeWrapper.getFluidOutputs().get(0));
		guiFluidStacks.set(3, recipeWrapper.getFluidOutputs().get(1));
		
		
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Reaction Chamber";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/reactionchamber-jei.png"), 0, 0, 187, 102);
	}
}
