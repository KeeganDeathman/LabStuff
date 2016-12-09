package keegan.labstuff.jei.circuitmaker;

import keegan.labstuff.recipes.*;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.*;

public class CircuitCreationETCRecipeHandler implements IRecipeHandler<CircuitCreationETC> {

	public CircuitCreationETCRecipeHandler(IJeiHelpers jeiHelpers) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Class<CircuitCreationETC> getRecipeClass() {
		// TODO Auto-generated method stub
		return CircuitCreationETC.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public String getRecipeCategoryUid(CircuitCreationETC recipe) {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CircuitCreationETC recipe) {
		// TODO Auto-generated method stub
		return new CircuitMakerRecipeWrapper(Recipes.getDesignFromName(recipe.getDesignName()), recipe.getEtched(), recipe.getCircuit());
	}

	@Override
	public boolean isRecipeValid(CircuitCreationETC recipe) {
		// TODO Auto-generated method stub
		return true;
	}

}
