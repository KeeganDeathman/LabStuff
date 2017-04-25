package keegan.labstuff.jei.reactionchamber;


import keegan.labstuff.recipes.Reaction;
import mezz.jei.api.*;
import mezz.jei.api.recipe.*;

public class ReactionChamberRecipeHandler implements IRecipeHandler<Reaction> {
	private final IJeiHelpers jeiHelpers;
	private final IGuiHelper guiHelpers;

	public ReactionChamberRecipeHandler(IJeiHelpers jeiHelpers, IGuiHelper guiHelpers) {
		this.jeiHelpers = jeiHelpers;
		this.guiHelpers = guiHelpers;
	}

	@Override
	public Class<Reaction> getRecipeClass() {
		return Reaction.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return "labstuff.reactionchamber";
	}

	@Override
	public String getRecipeCategoryUid(Reaction recipe) {
		return "labstuff.reactionchamber";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(Reaction recipe) {
		return new ReactionChamberRecipeWrapper(jeiHelpers, guiHelpers, recipe);
	}

	@Override
	public boolean isRecipeValid(Reaction recipe) {
		return true;
	}
}
