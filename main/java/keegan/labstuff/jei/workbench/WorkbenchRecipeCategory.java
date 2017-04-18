package keegan.labstuff.jei.workbench;


import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class WorkbenchRecipeCategory extends BlankRecipeCategory<WorkbenchRecipeWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public WorkbenchRecipeCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.workbench";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull WorkbenchRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		
		for(int y = 0; y < 5; y++)
		{
			for(int x = 0; x < 5; x++)
			{
				guiItemStacks.init((5*y)+x, true, 4+(18*x), 4+(18*y));
			}
		}
		guiItemStacks.init(25, false, 161, 40);
		
		for(int i = 0; i < 25; i++)
		{
			guiItemStacks.set(i, recipeWrapper.getInputs().get(i));
		}
		guiItemStacks.set(25, recipeWrapper.getOutputs().get((0)));
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Workbench";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/workbench-jei.png"), 0, 0, 194, 98);
	}
}
