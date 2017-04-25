package keegan.labstuff.jei.alloysmelter;


import keegan.labstuff.recipes.Alloy;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class AlloySmelterRecipeHandler implements IRecipeHandler<Alloy> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public AlloySmelterRecipeHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<Alloy> getRecipeClass() {
		return Alloy.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.alloysmelter";
	}

	@Override
	public String getRecipeCategoryUid(Alloy recipe) {
		return "labstuff.alloysmelter";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(Alloy recipe) {
		return new AlloySmelterRecipeWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(Alloy recipe) {
		return true;
	}
}
