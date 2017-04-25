package keegan.labstuff.util;

import net.minecraft.block.Block;
import net.minecraft.item.*;

public class BlockTuple
{
    public Block block;
    public int meta;

    public BlockTuple(Block b, int m)
    {
        this.block = b;
        this.meta = m;
    }
    
    public String toString()
    {
    	Item item = Item.getItemFromBlock(this.block);
    	if (item == null) return "unknown"; 
    	return new ItemStack(item, 1, this.meta).getUnlocalizedName() + ".name";
    }
}