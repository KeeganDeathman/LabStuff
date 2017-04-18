package keegan.labstuff.jei.fermenter;


import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class FermenterRecipeHandler implements IRecipeHandler<Ferment> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public FermenterRecipeHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<Ferment> getRecipeClass() {
		return Ferment.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.fermenter";
	}

	@Override
	public String getRecipeCategoryUid(Ferment recipe) {
		return "labstuff.fermenter";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(Ferment recipe) {
		return new FermenterRecipeWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(Ferment recipe) {
		return true;
	}
}
