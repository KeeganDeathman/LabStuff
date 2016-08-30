package keegan.labstuff.container;

import keegan.labstuff.tileentity.TileEntityAcceleratorInterface;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;

public class ContainerAcceleratorInterface extends Container
{

	private TileEntityAcceleratorInterface tile;
	private EntityPlayer player;

	public ContainerAcceleratorInterface(InventoryPlayer inv, TileEntityAcceleratorInterface tile)
	{
		this.tile = tile;
		this.player = inv.player;
		
		this.bindPlayerInventory(inv);
		
		this.addSlotToContainer(new Slot(tile, 0, 114, 172));
		this.addSlotToContainer(new Slot(tile, 1, 211, 116));
		
//		if(tile.upgraded)
//			this.addSlotToContainer(new Slot(tile, 2, 240, 240));
		
	}
	
	protected void bindPlayerInventory(InventoryPlayer inventoryPlayer)
	{
			for (int i = 0; i < 3; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 10 + i * 18));
				}
			}

			for (int i = 0; i < 9; i++)
			{
				addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 70));
			}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_)
	{
		return true;
	}

}
