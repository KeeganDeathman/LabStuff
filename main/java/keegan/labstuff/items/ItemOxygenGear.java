package keegan.labstuff.items;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemOxygenGear extends Item implements ISortableItem
{
    public ItemOxygenGear(String assetName)
    {
        super();
        this.setRegistryName("labstuff",assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
    	return LabStuffClientProxy.labStuffItem;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GEAR;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World worldIn, EntityPlayer player, EnumHand hand)
    {
        if (player instanceof EntityPlayerMP)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);
            ItemStack gear = stats.getExtendedInventory().getStackInSlot(1);

            if (gear == null)
            {
                stats.getExtendedInventory().setInventorySlotContents(1, itemStack.copy());
                itemStack.stackSize = 0;
                return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}