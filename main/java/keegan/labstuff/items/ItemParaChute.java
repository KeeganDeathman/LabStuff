package keegan.labstuff.items;

import static net.minecraft.item.EnumDyeColor.*;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.util.EnumSortCategoryItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraftforge.fml.relauncher.*;

public class ItemParaChute extends Item implements ISortableItem
{
    public static final String[] names = { "plain", // 0
            "black", // 1
            "blue", // 2
            "lime", // 3
            "brown", // 4
            "darkblue", // 5
            "darkgray", // 6
            "darkgreen", // 7
            "gray", // 8
            "magenta", // 9
            "orange", // 10
            "pink", // 11
            "purple", // 12
            "red", // 13
            "teal", // 14
            "yellow" }; // 15


    public ItemParaChute(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setRegistryName("labstuff",assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < ItemParaChute.names.length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return this.getUnlocalizedName() + "_" + ItemParaChute.names[itemStack.getItemDamage()];
    }

    public static EnumDyeColor getDyeEnumFromParachuteDamage(int damage)
    {
        switch (damage)
        {
            case 1:
                return BLACK;
            case 13:
                return RED;
            case 7:
                return GREEN;
            case 4:
                return BROWN;
            case 5:
                return BLUE;
            case 12:
                return PURPLE;
            case 14:
                return CYAN;
            case 8:
                return SILVER;
            case 6:
                return GRAY;
            case 11:
                return PINK;
            case 3:
                return LIME;
            case 15:
                return YELLOW;
            case 2:
                return LIGHT_BLUE;
            case 9:
                return MAGENTA;
            case 10:
                return ORANGE;
            case 0:
                return WHITE;
        }

        return WHITE;
    }

    public static int getParachuteDamageValueFromDyeEnum(EnumDyeColor color)
    {
        switch (color)
        {
            case BLACK:
                return 1;
            case RED:
                return 13;
            case GREEN:
                return 7;
            case BROWN:
                return 4;
            case BLUE:
                return 5;
            case PURPLE:
                return 12;
            case CYAN:
                return 14;
            case SILVER:
                return 8;
            case GRAY:
                return 6;
            case PINK:
                return 11;
            case LIME:
                return 3;
            case YELLOW:
                return 15;
            case LIGHT_BLUE:
                return 2;
            case MAGENTA:
                return 9;
            case ORANGE:
                return 10;
            case WHITE:
                return 0;
        }

        return -1;
    }

    public static int getParachuteDamageValueFromDye(int meta)
    {
        return getParachuteDamageValueFromDyeEnum(EnumDyeColor.byDyeDamage(meta));
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
}