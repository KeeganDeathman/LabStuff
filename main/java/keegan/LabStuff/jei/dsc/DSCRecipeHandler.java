package keegan.labstuff.jei.dsc;


import keegan.labstuff.jei.dsc.DSCRecipeWrapper;
import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class DSCRecipeHandler implements IRecipeHandler<DiscoveryItem> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public DSCRecipeHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<DiscoveryItem> getRecipeClass() {
		return DiscoveryItem.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.dsc";
	}

	@Override
	public String getRecipeCategoryUid(DiscoveryItem recipe) {
		return "labstuff.dsc";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(DiscoveryItem recipe) {
		return new DSCRecipeWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(DiscoveryItem recipe) {
		return true;
	}
}
