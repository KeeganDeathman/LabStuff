package keegan.labstuff.jei.electrifier;


import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.*;

public class ElectrifierRecipeHandler implements IRecipeHandler<ElectrifierRecipe> {
	private final IJeiHelpers jeiHelpers;

	public ElectrifierRecipeHandler(IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public Class<ElectrifierRecipe> getRecipeClass() {
		return ElectrifierRecipe.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.electrifier";
	}

	@Override
	public String getRecipeCategoryUid(ElectrifierRecipe recipe) {
		return "labstuff.electrifier";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ElectrifierRecipe recipe) {
		return new ElectrifierRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(ElectrifierRecipe recipe) {
		return true;
	}
}
