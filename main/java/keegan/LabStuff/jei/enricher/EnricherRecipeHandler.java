package keegan.labstuff.jei.enricher;


import keegan.labstuff.recipes.Enrichment;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.recipe.*;

public class EnricherRecipeHandler implements IRecipeHandler<Enrichment> {
	private final IJeiHelpers jeiHelpers;

	public EnricherRecipeHandler(IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public Class<Enrichment> getRecipeClass() {
		return Enrichment.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.enricher";
	}

	@Override
	public String getRecipeCategoryUid(Enrichment recipe) {
		return "labstuff.enricher";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(Enrichment recipe) {
		return new EnricherRecipeWrapper(jeiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(Enrichment recipe) {
		return true;
	}
}
