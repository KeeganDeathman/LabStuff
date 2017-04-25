package keegan.labstuff.items;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.util.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemOxygenTank extends Item implements ISortableItem
{
    public ItemOxygenTank(int tier, String assetName)
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(tier * 900);
        this.setRegistryName("labstuff",assetName);
        this.setNoRepair();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, this.getMaxDamage()));
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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List<String> tooltip, boolean b)
    {
        tooltip.add(LabStuffUtils.translate("gui.tank.oxygen_remaining") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
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
        return new ActionResult<>(EnumActionResult.PASS, itemStack);
    }
}