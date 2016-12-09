package keegan.labstuff.jei.circuitdesign;


import keegan.labstuff.recipes.CircuitDesign;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class CircuitDesignHandler implements IRecipeHandler<CircuitDesign> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public CircuitDesignHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<CircuitDesign> getRecipeClass() {
		return CircuitDesign.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.circuitdesign";
	}

	@Override
	public String getRecipeCategoryUid(CircuitDesign recipe) {
		return "labstuff.circuitdesign";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CircuitDesign recipe) {
		return new CircuitDesignWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(CircuitDesign recipe) {
		return true;
	}
}
