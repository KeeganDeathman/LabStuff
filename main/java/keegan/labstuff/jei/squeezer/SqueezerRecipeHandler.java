package keegan.labstuff.jei.squeezer;


import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class SqueezerRecipeHandler implements IRecipeHandler<Squeeze> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public SqueezerRecipeHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<Squeeze> getRecipeClass() {
		return Squeeze.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.squeezer";
	}

	@Override
	public String getRecipeCategoryUid(Squeeze recipe) {
		return "labstuff.squeezer";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(Squeeze recipe) {
		return new SqueezerRecipeWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(Squeeze recipe) {
		return true;
	}
}
