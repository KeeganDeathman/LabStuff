package keegan.labstuff.container;

import cpw.mods.fml.relauncher.*;
import keegan.labstuff.tileentity.TileEntityDLLaptop;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;

public class ContainerDLLaptop extends Container
{
	
	private TileEntityDLLaptop tile;
	private boolean lastTablet;
	private int lastOpened;
	
	public ContainerDLLaptop(InventoryPlayer inv, TileEntityDLLaptop tileEntity) 
	{
		tile = tileEntity;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return true;
	}
	 public void detectAndSendChanges()
	    {
	        super.detectAndSendChanges();

	        for (int i = 0; i < this.crafters.size(); ++i)
	        {
	            ICrafting icrafting = (ICrafting)this.crafters.get(i);

	            if (this.lastTablet != this.tile.isTablet())
	            {
	            	int tabletInt;
	            	if(this.tile.isTablet())
	            		tabletInt = 1;
	   			 	else
	   			 		tabletInt = 0;
	                icrafting.sendProgressBarUpdate(this, 0, tabletInt);
	                icrafting.sendProgressBarUpdate(this, 1, tile.getBeenOpened());
	            }
	        }
	        this.lastTablet = tile.isTablet();
	        this.lastOpened = tile.getBeenOpened();
	    }
	    
		
		@SideOnly(Side.CLIENT)
	    public void updateProgressBar(int p_75137_1_, int p_75137_2_)
	    {
	        if (p_75137_1_ == 0)
	        {
	        	boolean tablet;
	        	if(p_75137_2_ == 0)
	        		tablet = true;
	        	else
	        		tablet = false;
	            this.tile.setTablet(tablet);
	            System.out.println("Updated client tablet, " + tablet);
	        }
	        else if(p_75137_1_ == 1)
	        {
	        	this.tile.setBeenOpened(p_75137_2_);
	        }
	    }
		
		 public void addCraftingToCrafters(ICrafting p_75132_1_)
		 {
			 super.addCraftingToCrafters(p_75132_1_);
			 int tabletInt;
			 if(this.tile.isTablet())
				 tabletInt = 1;
			 else
				 tabletInt = 0;
			 p_75132_1_.sendProgressBarUpdate(this, 0, tabletInt);
			 p_75132_1_.sendProgressBarUpdate(this, 01, tile.getBeenOpened());
		 }
}
