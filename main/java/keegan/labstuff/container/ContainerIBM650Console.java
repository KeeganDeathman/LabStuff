package keegan.labstuff.container;

import keegan.labstuff.tileentity.IBM650Console;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;

public class ContainerIBM650Console extends Container {

	public ContainerIBM650Console(InventoryPlayer player, IBM650Console console){}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return true;
	}

}
