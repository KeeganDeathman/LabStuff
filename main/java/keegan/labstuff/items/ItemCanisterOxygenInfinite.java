package keegan.labstuff.items;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.common.EnumColor;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.util.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemCanisterOxygenInfinite extends Item implements IItemOxygenSupply, ISortableItem
{
    public ItemCanisterOxygenInfinite(String assetName)
    {
        super();
        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setRegistryName("labstuff",assetName);
        this.setContainerItem(LabStuffMain.canisterLOX);
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        tooltip.add(EnumColor.DARK_GREEN + LabStuffUtils.translate("gui.infinite_item.desc"));
        tooltip.add(EnumColor.RED + LabStuffUtils.translate("gui.creative_only.desc"));
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        this.itemIcon = iconRegister.registerIcon(Constants.TEXTURE_PREFIX + "oxygenCanisterInfinite");
    }*/

    @Override
    public CreativeTabs getCreativeTab()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemstack)
    {
        if (super.getContainerItem(itemstack) == null)
        {
            return null;
        }
        return itemstack;
    }

    @Override
    public int discharge(ItemStack itemStack, int amount)
    {
        return amount;
    }

    @Override
    public int getOxygenStored(ItemStack par1ItemStack)
    {
        return par1ItemStack.getMaxDamage();
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
            ItemStack gear = stats.getExtendedInventory().getStackInSlot(2);
            ItemStack gear1 = stats.getExtendedInventory().getStackInSlot(3);

            if (gear == null)
            {
                stats.getExtendedInventory().setInventorySlotContents(2, itemStack.copy());
                itemStack.stackSize = 0;
            }
            else if (gear1 == null)
            {
                stats.getExtendedInventory().setInventorySlotContents(3, itemStack.copy());
                itemStack.stackSize = 0;
            }
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStack);
    }
}