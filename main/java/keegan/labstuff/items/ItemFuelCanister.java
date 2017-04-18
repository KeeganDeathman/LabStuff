package keegan.labstuff.items;

import java.util.List;

import keegan.labstuff.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.*;

public class ItemFuelCanister extends ItemCanisterGeneric implements ISortableItem
{
//    protected IIcon[] icons = new IIcon[7];

    public ItemFuelCanister(String assetName)
    {
        super(assetName);
        this.setAllowedFluid("kerosene");
//        this.setTextureName(Constants.TEXTURE_PREFIX + assetName);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        for (int i = 0; i < this.icons.length; i++)
        {
            this.icons[i] = iconRegister.registerIcon(this.getIconString() + "_" + i);
        }
    }*/

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        if (itemStack.getItemDamage() == 1)
        {
            return "item.fuel_canister";
        }

        return "item.fuel_canister_partial";
    }

    /*@Override
    public IIcon getIconFromDamage(int par1)
    {
        final int damage = 6 * par1 / this.getMaxDamage();

        if (this.icons.length > damage)
        {
            return this.icons[this.icons.length - damage - 1];
        }

        return super.getIconFromDamage(damage);
    }*/

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List<String> tooltip, boolean par4)
    {
        if (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage() > 0)
        {
            tooltip.add(LabStuffUtils.translate("gui.message.fuel.name") + ": " + (par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage()));
        }
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.CANISTER;
    }
}