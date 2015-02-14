package keegan.labstuff.container;

import keegan.labstuff.tileentity.TileEntityPlasmaNetworkMonitor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class ContainerPlasmaNetworkMonitor extends Container 
{
	protected TileEntityPlasmaNetworkMonitor tileEntity;
	
	public ContainerPlasmaNetworkMonitor(InventoryPlayer invPlayer, TileEntityPlasmaNetworkMonitor te)
	{
		tileEntity = te;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) 
	{
		return true;
	}


}
