package keegan.labstuff.jei.electrifier;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class ElectrifierRecipeCategory extends BlankRecipeCategory<ElectrifierRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public ElectrifierRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.electrifier";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ElectrifierRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 25, 3);
		guiItemStacks.init(1, true, 25, 30);
		guiItemStacks.init(2, true, 4, 30);
		guiItemStacks.init(3, true, 45, 30);
		
		guiItemStacks.init(4, false, 25, 55);
		guiItemStacks.init(5, false, 25, 82);
		guiItemStacks.init(6, false, 4, 82);
		guiItemStacks.init(7, false, 45, 82);
		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getInputs().get(1));
		guiItemStacks.set(2, recipeWrapper.getInputs().get(2));
		guiItemStacks.set(3, recipeWrapper.getInputs().get(3));
		
		guiItemStacks.set(4, recipeWrapper.getOutputs().get(0));
		guiItemStacks.set(5, recipeWrapper.getOutputs().get(1));
		guiItemStacks.set(6, recipeWrapper.getOutputs().get(2));
		guiItemStacks.set(7, recipeWrapper.getOutputs().get(3));
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Electrifier";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/electrifier-nei.png"), 0, 0, 64, 101);
	}
}
