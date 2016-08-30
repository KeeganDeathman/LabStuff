package keegan.labstuff.container;

import keegan.labstuff.tileentity.DSCBench;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;

public class ContainerDSCBench extends Container
{

	private DSCBench tile;
	private EntityPlayer player;

	public ContainerDSCBench(InventoryPlayer inv, DSCBench tile)
	{
		this.tile = tile;
		this.player = inv.player;
		
		this.bindPlayerInventory(inv);
		
		this.addSlotToContainer(new Slot(tile, 0, 114, 188));
		this.addSlotToContainer(new Slot(tile, 1, 130, 188));
		this.addSlotToContainer(new Slot(tile, 2, 146, 188));
		this.addSlotToContainer(new Slot(tile, 3, 130, 194));
		
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
