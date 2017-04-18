package keegan.labstuff.jei.chloralakizer;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.render.LabStuffOBJLoader;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;

public class ChloralakizerRecipe 
{
	public ItemStack input = new ItemStack(LabStuffMain.salt, 2);
	public ItemStack output1 = new ItemStack(LabStuffMain.lye, 2);
	public ItemStack output2 = new ItemStack(LabStuffMain.itemHydrogenTestTube, 2);
	public FluidStack inwater = new FluidStack(FluidRegistry.WATER, 40);
	public static final ChloralakizerRecipe INSTANCE = new ChloralakizerRecipe();
}
