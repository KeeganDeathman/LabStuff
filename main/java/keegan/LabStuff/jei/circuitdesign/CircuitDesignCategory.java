package keegan.labstuff.jei.circuitdesign;


import javax.annotation.Nonnull;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class CircuitDesignCategory extends BlankRecipeCategory<CircuitDesignWrapper>
{
	
	private final IGuiHelper guiHelper;
	
	public CircuitDesignCategory(IGuiHelper guiHelper)
	{
		this.guiHelper = guiHelper;
	}
	
	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.circuitdesign";
	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull CircuitDesignWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 24, 5);
		guiItemStacks.init(1, false, 24, 43);		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getOutputs().get(0));
		
		
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Circuit Design Table";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/circuit-design-jei.png"), 0, 0, 64, 64);
	}
}
