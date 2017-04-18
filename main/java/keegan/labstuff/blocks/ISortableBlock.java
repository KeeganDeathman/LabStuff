package keegan.labstuff.blocks;

import keegan.labstuff.util.EnumSortCategoryBlock;

public interface ISortableBlock
{
    EnumSortCategoryBlock getCategory(int meta);
}