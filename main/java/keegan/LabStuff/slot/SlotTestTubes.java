package keegan.labstuff.slot;

import keegan.labstuff.container.*;
import net.minecraft.inventory.*;

public class SlotTestTubes extends Slot 
{

	public SlotTestTubes(ContainerGasChamberPort par1ContainerCircuitDesignTable, IInventory par2iInventory, int par3, int par4, int par5) 
	{
		super(par2iInventory, par3, par4, par5);
	}
	
	public SlotTestTubes(ContainerVent containerVent, IInventory tileEntity, int par3, int par4, int par5) 
	{
		super(tileEntity, par3, par4, par5);
	}

	@Override
	public int getSlotStackLimit()
	{
		return 64;
	}

}
