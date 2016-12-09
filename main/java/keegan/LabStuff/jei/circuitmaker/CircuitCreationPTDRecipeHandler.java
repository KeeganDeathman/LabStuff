package keegan.labstuff.jei.circuitmaker;

import keegan.labstuff.recipes.*;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.*;

public class CircuitCreationPTDRecipeHandler implements IRecipeHandler<CircuitCreationPTD> {

	public CircuitCreationPTDRecipeHandler(IJeiHelpers jeiHelpers) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Class<CircuitCreationPTD> getRecipeClass() {
		// TODO Auto-generated method stub
		return CircuitCreationPTD.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public String getRecipeCategoryUid(CircuitCreationPTD recipe) {
		// TODO Auto-generated method stub
		return "labstuff.circuitmaker";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CircuitCreationPTD recipe) {
		// TODO Auto-generated method stub
		return new CircuitMakerRecipeWrapper(Recipes.getDesignFromName(recipe.getDesignName()), recipe.getPlate(), recipe.getDrilled());
	}

	@Override
	public boolean isRecipeValid(CircuitCreationPTD recipe) {
		// TODO Auto-generated method stub
		return true;
	}

}
