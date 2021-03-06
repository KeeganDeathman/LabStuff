package keegan.labstuff.jei;

import javax.annotation.Nonnull;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.jei.alloysmelter.*;
import keegan.labstuff.jei.chloralakizer.*;
import keegan.labstuff.jei.circuitdesign.*;
import keegan.labstuff.jei.circuitmaker.*;
import keegan.labstuff.jei.dsc.*;
import keegan.labstuff.jei.electrifier.*;
import keegan.labstuff.jei.enricher.*;
import keegan.labstuff.jei.fermenter.*;
import keegan.labstuff.jei.reactionchamber.*;
import keegan.labstuff.jei.squeezer.*;
import keegan.labstuff.jei.workbench.*;
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
				new DSCRecipeCategory(guiHelper),
				new WorkbenchRecipeCategory(guiHelper),
				new ChloralakizerRecipeCategory(guiHelper),
				new ReactionChamberRecipeCategory(guiHelper),
				new AlloySmelterRecipeCategory(guiHelper),
				new SqueezerRecipeCategory(guiHelper),
				new FermenterRecipeCategory(guiHelper)
		);
		
		registry.addRecipeHandlers( 
				new ElectrifierRecipeHandler(jeiHelpers),
				new CircuitCreationDTCRecipeHandler(jeiHelpers),
				new CircuitCreationETCRecipeHandler(jeiHelpers),
				new CircuitCreationPTERecipeHandler(jeiHelpers),
				new CircuitCreationPTDRecipeHandler(jeiHelpers),
				new EnricherRecipeHandler(jeiHelpers),
				new CircuitDesignHandler(jeiHelpers, guiHelper),
				new DSCRecipeHandler(jeiHelpers, guiHelper),
				new WorkbenchRecipeHandler(jeiHelpers, guiHelper),
				new ChloralakizierRecipeHandler(jeiHelpers, guiHelper),
				new ReactionChamberRecipeHandler(jeiHelpers, guiHelper),
				new AlloySmelterRecipeHandler(jeiHelpers, guiHelper),
				new SqueezerRecipeHandler(jeiHelpers, guiHelper),
				new FermenterRecipeHandler(jeiHelpers, guiHelper)
		);
		
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockElectrifier)), "labstuff.electrifier");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockCircuitMaker)), "labstuff.circuitmaker");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockEnricher)), "labstuff.enricher");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockCircuitDesignTable)), "labstuff.circuitdesign");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockDSCCore)), "labstuff.dsc");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.get(LabStuffMain.blockWorkbench)), "labstuff.workbench");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.chloralakizer), "labstuff.chloralakizer");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.reactionChamber), "labstuff.reactionchamber");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.alloySmelter), "labstuff.alloysmelter");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.squeezer), "labstuff.squeezer");
		registry.addRecipeCategoryCraftingItem(new ItemStack(LabStuffMain.fermenter), "labstuff.fermenter");
		
		
		registry.addRecipes(ElectrifierRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(CircuitMakerRecipeWrapper.getPTDRecipes());
		registry.addRecipes(CircuitMakerRecipeWrapper.getPTERecipes());
		registry.addRecipes(CircuitMakerRecipeWrapper.getDTCRecipes());
		registry.addRecipes(CircuitMakerRecipeWrapper.getETCRecipes());	
		registry.addRecipes(EnricherRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(CircuitDesignWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(DSCRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(WorkbenchRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(ChloralakizerRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(ReactionChamberRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(AlloySmelterRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(SqueezerRecipeWrapper.getRecipes(jeiHelpers));
		registry.addRecipes(FermenterRecipeWrapper.getRecipes(jeiHelpers));
		
		registry.addDescription(new ItemStack(LabStuffMain.get(LabStuffMain.blockDSCCore)), "The Discovery Supercomputer Requires:", "One Core(Costs 0 RAM)", "One OS (Costs 8 RAM)", "At least One Drive (Costs 3 RAM)", "At least One Workbench (Costs 10 RAM)", "Each RAM block provides 5 RAM");
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration registry)
	{
		
	}
}
