package keegan.labstuff.items;

import keegan.labstuff.util.EnumSortCategoryItem;

public interface ISortableItem
{
    EnumSortCategoryItem getCategory(int meta);
}