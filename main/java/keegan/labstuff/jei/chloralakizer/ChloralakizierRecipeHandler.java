package keegan.labstuff.jei.chloralakizer;


import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class ChloralakizierRecipeHandler implements IRecipeHandler<ChloralakizerRecipe> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public ChloralakizierRecipeHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<ChloralakizerRecipe> getRecipeClass() {
		return ChloralakizerRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.chloralakizer";
	}

	@Override
	public String getRecipeCategoryUid(ChloralakizerRecipe recipe) {
		return "labstuff.chloralakizer";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ChloralakizerRecipe recipe) {
		return new ChloralakizerRecipeWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(ChloralakizerRecipe recipe) {
		return true;
	}
}
