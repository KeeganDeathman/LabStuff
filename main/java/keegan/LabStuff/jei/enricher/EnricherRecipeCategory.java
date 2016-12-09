package keegan.labstuff.jei.enricher;

import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class EnricherRecipeCategory extends BlankRecipeCategory<EnricherRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public EnricherRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.enricher";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull EnricherRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 9, 25);
		guiItemStacks.init(1, false, 39, 25);		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getOutputs().get(0));
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Enricher";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/enricher-jei.png"), 0, 0, 64, 64);
	}
}
