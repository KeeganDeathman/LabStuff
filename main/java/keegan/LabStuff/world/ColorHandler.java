package keegan.labstuff.world;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.*;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.*;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.*;


@SideOnly(Side.CLIENT)
public class ColorHandler 
{
	private static final Minecraft minecraft = Minecraft.getMinecraft();


	public static void init() 
	{
		final BlockColors blockColors = minecraft.getBlockColors();
		final ItemColors itemColors = minecraft.getItemColors();

		registerBlockColourHandlers(blockColors);
		registerItemColourHandlers(blockColors, itemColors);
	}


	private static void registerBlockColourHandlers(final BlockColors blockColors) 
	{
		// Use the grass colour of the biome or the default grass colour
		final IBlockColor foliageColourHandler = (state, blockAccess, pos, tintIndex) -> 
		{
			if (blockAccess != null && pos != null) {
				return BiomeColorHelper.getFoliageColorAtPos(blockAccess, pos);
			}
			
			return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
		};
		
		@SuppressWarnings("unused")
		final IBlockColor grassColourHandler = (state, blockAccess, pos, tintIndex) -> 
		{
			if (blockAccess != null && pos != null) {
				return BiomeColorHelper.getGrassColorAtPos(blockAccess, pos);
			}

			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
		};

		blockColors.registerBlockColorHandler(foliageColourHandler, LabStuffMain.blockRubberLeaves);
	}


	@SuppressWarnings("deprecation")
	private static void registerItemColourHandlers(final BlockColors blockColors, final ItemColors itemColors) 
	{
		// Use the Block's colour handler for an ItemBlock
		final IItemColor itemBlockColourHandler = (stack, tintIndex) -> 
		{
			IBlockState iblockstate = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			return blockColors.colorMultiplier(iblockstate, null, null, tintIndex);
		};

		itemColors.registerItemColorHandler(itemBlockColourHandler, LabStuffMain.blockRubberLeaves);
	}
}