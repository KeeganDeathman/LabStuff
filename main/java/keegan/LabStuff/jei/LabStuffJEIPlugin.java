package keegan.labstuff.jei;

import javax.annotation.Nonnull;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.jei.circuitdesign.*;
import keegan.labstuff.jei.circuitmaker.*;
import keegan.labstuff.jei.dsc.*;
import keegan.labstuff.jei.electrifier.*;
import keegan.labstuff.jei.enricher.*;
import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class LabStuffJEIPlugin extends BlankModPlugin
{

	@Override
	public void register(@Nonnull IModRegistry registry) 
	{	
		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		
		registry.addRecipeCategories(
				new ElectrifierRecipeCategory(guiHelper),
				new CircuitMakerRecipeCategory(guiHelper),
				new EnricherRecipeCategory(guiHelper),
				new CircuitDesignCategory(guiHelper),
				new DSCRecipeCategory(guiHelper)
		);
		
		registry.addRecipeHandlers( 
				new ElectrifierRecipeHandler(jeiHelpers),
				new CircuitCreationDTCRecipeHandler(jeiHelpers),
				new CircuitCreationETCRecipeHandler(jeiHelpers),
				new CircuitCreationPTERecipeHandler(jeiHelpers),
				new CircuitCreationPTDRecipeHandler(jeiHelpers),
				new EnricherRecipeHandler(jeiHelpers),
				new CircuitDesignHandler(jeiHelpers, guiHelper),
				new DSCRecipeHandler(jeiHelpers, guiHelper)
		);
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockElectrifier)), "labstuff.electrifier");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockCircuitMaker)), "labstuff.circuitmaker");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockEnricher)), "labstuff.enricher");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockCircuitDesignTable)), "labstuff.circuitdesign");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockDSCCore)), "labstuff.dsc");
		
		registry.addRecipes(ElectrifierRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(CircuitMakerRecipeWrapper.getPTDRecipes());
		registry.addRecipes(CircuitMakerRecipeWrapper.getPTERecipes());
		registry.addRecipes(CircuitMakerRecipeWrapper.getDTCRecipes());
		registry.addRecipes(CircuitMakerRecipeWrapper.getETCRecipes());	
		registry.addRecipes(EnricherRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(CircuitDesignWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(DSCRecipeWrapper.getRecipes(jeiHelpers));
		
		registry.addDescription(new ItemStack(LabStuffMain.get(LabStuffMain.blockDSCCore)), "The Discovery Supercomputer Requires:", "One Core(Costs 0 RAM)", "One OS (Costs 8 RAM)", "At least One Drive (Costs 3 RAM)", "At least One Workbench (Costs 10 RAM)", "Each RAM block provides 5 RAM");
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{
		
	}
}
