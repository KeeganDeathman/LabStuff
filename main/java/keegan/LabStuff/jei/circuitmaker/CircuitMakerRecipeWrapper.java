package keegan.labstuff.jei.circuitmaker;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.recipes.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.*;

public class CircuitMakerRecipeWrapper extends BlankRecipeWrapper {

	private final List<ItemStack> inputs;
	private final List<ItemStack> outputs;
	
	public CircuitMakerRecipeWrapper(Item design, Item input, Item output)
	{
		inputs = new ArrayList<ItemStack>();
		outputs = new ArrayList<ItemStack>();
		inputs.add(new ItemStack(design));
		inputs.add(new ItemStack(input));
		outputs.add(new ItemStack(output));
	}
		
	@Override
	public List<ItemStack> getInputs() {
		return inputs;
	}

	@Override
	public List<ItemStack>  getOutputs() {
		// TODO Auto-generated method stub
		return outputs;
	}
	
	public static List<CircuitCreationPTD> getPTDRecipes()
	{
		List<CircuitCreationPTD> list = new ArrayList<>();
		for(CircuitCreation recipe : Recipes.getCircuitCreations())
		{
			list.add(new CircuitCreationPTD(recipe.getDesignName(), recipe.getDrilled()));
		}
		return list;
	}
	
	public static List<CircuitCreationPTE> getPTERecipes()
	{
		List<CircuitCreationPTE> list = new ArrayList<>();
		for(CircuitCreation recipe : Recipes.getCircuitCreations())
		{
			list.add(new CircuitCreationPTE(recipe.getDesignName(), recipe.getEtched()));
		}
		return list;
	}
	
	public static List<CircuitCreationETC> getETCRecipes()
	{
		List<CircuitCreationETC> list = new ArrayList<>();
		for(CircuitCreation recipe : Recipes.getCircuitCreations())
		{
			list.add(new CircuitCreationETC(recipe.getDesignName(), recipe.getEtched(), recipe.getCircuit()));
		}
		return list;
	}
	
	public static List<CircuitCreationDTC> getDTCRecipes()
	{
		List<CircuitCreationDTC> list = new ArrayList<>();
		for(CircuitCreation recipe : Recipes.getCircuitCreations())
		{
			list.add(new CircuitCreationDTC(recipe.getDesignName(), recipe.getDrilled(), recipe.getCircuit()));
		}
		return list;
	}
	

	@Override
	public void getIngredients(IIngredients ingredients) {
		// TODO Auto-generated method stub
		ingredients.setInputs(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, outputs);
	}

}
