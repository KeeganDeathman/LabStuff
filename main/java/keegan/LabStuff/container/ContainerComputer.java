package keegan.labstuff.container;

import keegan.labstuff.tileentity.TileEntityComputer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerComputer extends Container {

	public ContainerComputer(InventoryPlayer inv, TileEntityComputer tileEntity) 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canInteractWith(EntityPlayer var1) {
		// TODO Auto-generated method stub
		return true;
	}

}
