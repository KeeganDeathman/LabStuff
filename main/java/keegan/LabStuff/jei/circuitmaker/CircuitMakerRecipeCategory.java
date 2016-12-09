package keegan.labstuff.jei.circuitmaker;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.*;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CircuitMakerRecipeCategory extends BlankRecipeCategory<CircuitMakerRecipeWrapper> {

	private IGuiHelper guiHelper;
	
	public CircuitMakerRecipeCategory(IGuiHelper guiHelper) {
		this.guiHelper = guiHelper;
	}

	@Override
	public String getUid() {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return "Circuit Maker";
	}

	@Override
	public IDrawable getBackground() {
		// TODO Auto-generated method stub
		return guiHelper.createDrawable(new ResourceLocation("labstuff:textures/gui/circuit-maker-jei.png"), 0, 0, 64, 64);
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, CircuitMakerRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(0, true, 8, 4);
		guiItemStacks.init(1, true, 38, 4);
		guiItemStacks.init(2, false, 23, 42);
		
		guiItemStacks.set(0, recipeWrapper.getInputs().get(0));
		guiItemStacks.set(1, recipeWrapper.getInputs().get(1));
		guiItemStacks.set(2, recipeWrapper.getOutputs().get(0));
		
	}

	

}
