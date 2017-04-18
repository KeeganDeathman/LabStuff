package keegan.labstuff.recipes;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.*;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class PunchCardRecipe implements IRecipe {
    
    @Override
    public boolean matches (InventoryCrafting inventory, World world) {
        
        int itemCount = 0;
        
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            
            ItemStack currentStack = inventory.getStackInSlot(slot);
            
            if (currentStack != null && (currentStack.getItem() instanceof PunchCard || currentStack.getItem() instanceof PunchDeck)) {
                
            	itemCount++;
            }
        }
        
        return itemCount > 0;
    }
    
    @Override
    public ItemStack getCraftingResult (InventoryCrafting inv) {
		int NumberOfCards = 0;
		for (int j = 0; j < inv.getSizeInventory(); j++) 
			if (inv.getStackInSlot(j) != null) 
				if (inv.getStackInSlot(j).getItem() == LabStuffMain.punchCard) 
					NumberOfCards++;
		
		NBTTagCompound tag = new NBTTagCompound();
		
		if(NumberOfCards > 1)
		{
			for(int i = 0; i < NumberOfCards; i++)
				for (int j = 0; j < inv.getSizeInventory(); j++) 
					if (inv.getStackInSlot(j) != null) 
					{
						if (inv.getStackInSlot(j).getItem() == LabStuffMain.punchCard) 
							tag.setString("card"+(tag.getSize()+1), inv.getStackInSlot(j).getTagCompound().getString("card"));
					}
		}
		
		int NumberOfDecks = 0;
		for (int j = 0; j < inv.getSizeInventory(); j++) 
			if (inv.getStackInSlot(j) != null) 
				if (inv.getStackInSlot(j).getItem() == LabStuffMain.punchDeck) 
					NumberOfDecks++;
		if(NumberOfDecks > 0)
		{
			for(int i = 0; i < NumberOfDecks; i++)
				for (int j = 0; j < inv.getSizeInventory(); j++) 
					if (inv.getStackInSlot(j) != null) 
					{
						if (inv.getStackInSlot(j).getItem() == LabStuffMain.punchDeck) 
							for(String key:((NBTTagCompound)inv.getStackInSlot(j).getTagCompound().getTag("cards")).getKeySet())
							{
								tag.setString("card"+(tag.getSize()+1), ((NBTTagCompound)inv.getStackInSlot(j).getTagCompound().getTag("cards")).getString(key));
							}
					}
		}
		
		ItemStack deck = new ItemStack(LabStuffMain.punchDeck);
		if(deck.getTagCompound() == null)
			deck.setTagCompound(new NBTTagCompound());
		deck.getTagCompound().setTag("cards", tag);
		return deck;
	}
    
    @Override
    public int getRecipeSize () {
        
        return 9;
    }
    
    @Override
    public ItemStack getRecipeOutput () {
        
        return null;
    }
    
    @Override
    public ItemStack[] getRemainingItems (InventoryCrafting inv) {
        
        ItemStack[] results = new ItemStack[inv.getSizeInventory()];
        
        for (int slot = 0; slot < results.length; ++slot) {
            
            ItemStack itemstack = inv.getStackInSlot(slot);
            results[slot] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }
        
        return results;
    }
}