package keegan.labstuff.jei.workbench;


import keegan.labstuff.recipes.*;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class WorkbenchRecipeHandler implements IRecipeHandler<ResearchItem> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public WorkbenchRecipeHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<ResearchItem> getRecipeClass() {
		return ResearchItem.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.workbench";
	}

	@Override
	public String getRecipeCategoryUid(ResearchItem recipe) {
		return "labstuff.workbench";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ResearchItem recipe) {
		return new WorkbenchRecipeWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(ResearchItem recipe) {
		return true;
	}
}
