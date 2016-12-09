package keegan.labstuff.jei.electrifier;

import java.util.*;

import javax.annotation.Nonnull;

import keegan.labstuff.LabStuffMain;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ElectrifierRecipe 
{
	
	private List<ItemStack> inputs;
	private List<ItemStack> outputs;
	
	public ElectrifierRecipe()
	{
		inputs = new ArrayList<ItemStack>();
		inputs.add(new ItemStack(Items.WATER_BUCKET));
		inputs.add(new ItemStack(LabStuffMain.itemBattery));
		inputs.add(new ItemStack(LabStuffMain.itemTestTube));
		inputs.add(new ItemStack(LabStuffMain.itemTestTube));
		outputs = new ArrayList<ItemStack>();
		outputs.add(new ItemStack(Items.BUCKET));
		outputs.add(new ItemStack(LabStuffMain.itemDeadBattery));
		outputs.add(new ItemStack(LabStuffMain.itemHydrogenTestTube));
		outputs.add(new ItemStack(LabStuffMain.itemOxygenTestTube));
	}
	
	@Nonnull
	public List<ItemStack> getInputs() {
		return inputs;
	}

	@Nonnull
	public List<ItemStack> getOutputs() {
		return outputs;	
	}
}
