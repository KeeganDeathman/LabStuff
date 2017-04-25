package keegan.labstuff.jei.circuitmaker;

import keegan.labstuff.recipes.Recipes;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.*;

public class CircuitCreationDTCRecipeHandler implements IRecipeHandler<CircuitCreationDTC> {

	public CircuitCreationDTCRecipeHandler(IJeiHelpers jeiHelpers) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Class<CircuitCreationDTC> getRecipeClass() {
		// TODO Auto-generated method stub
		return CircuitCreationDTC.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public String getRecipeCategoryUid(CircuitCreationDTC recipe) {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CircuitCreationDTC recipe) {
		// TODO Auto-generated method stub
		return new CircuitMakerRecipeWrapper(Recipes.getDesignFromName(recipe.getDesignName()), recipe.getDrilled(), recipe.getCircuit());
	}

	@Override
	public boolean isRecipeValid(CircuitCreationDTC recipe) {
		// TODO Auto-generated method stub
		return true;
	}

}
