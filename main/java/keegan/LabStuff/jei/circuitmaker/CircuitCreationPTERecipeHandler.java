package keegan.labstuff.jei.circuitmaker;

import keegan.labstuff.recipes.*;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.*;

public class CircuitCreationPTERecipeHandler implements IRecipeHandler<CircuitCreationPTE> {

	public CircuitCreationPTERecipeHandler(IJeiHelpers jeiHelpers) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Class<CircuitCreationPTE> getRecipeClass() {
		// TODO Auto-generated method stub
		return CircuitCreationPTE.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public String getRecipeCategoryUid(CircuitCreationPTE recipe) {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CircuitCreationPTE recipe) {
		// TODO Auto-generated method stub
		return new CircuitMakerRecipeWrapper(Recipes.getDesignFromName(recipe.getDesignName()), recipe.getPlate(), recipe.getEtched());
	}

	@Override
	public boolean isRecipeValid(CircuitCreationPTE recipe) {
		// TODO Auto-generated method stub
		return true;
	}

}
