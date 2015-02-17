package keegan.labstuff.slot;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.container.ContainerCircuitMaker;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCircuitMakerCircuitBoardOutput extends Slot 
{
	
	public SlotCircuitMakerCircuitBoardOutput(ContainerCircuitMaker par1ContainerCircuitMaker, IInventory par2iInventory, int par3, int par4, int par5) 
	{
		super(par2iInventory, par3, par4, par5);
	}
	
	@Override
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		if(par1ItemStack.getItem() == LabStuffMain.itemBasicDrilledCircuitBoard || par1ItemStack.getItem() == LabStuffMain.itemBasicEtchedCircuitBoard || par1ItemStack.getItem() == LabStuffMain.itemBasicCircuitBoard)
		{
			return true;
		}
		return false;
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}

}
