package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.BlockGasChamberPort;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGasChamberPort extends TileEntity implements IInventory
{
	private ItemStack[] chestContents;
	
	private boolean input = false;
	public int testtubes;
	
	public TileEntityGasChamberPort()
	{
		chestContents = new ItemStack[1];
	}
	
	public void onEntityUpdate()
	{
		//get input.output state
		this.input = ((BlockGasChamberPort)worldObj.getBlock(xCoord, yCoord, zCoord)).input;
		TileEntity remoteTile = worldObj.getTileEntity(xCoord, yCoord+2, zCoord);
		if(remoteTile instanceof TileEntityGasChamberPort)
		{
			this.testtubes = ((TileEntityGasChamberPort)remoteTile).getStackInSlot(0).stackSize;
		}
	}

	@Override
	public int getSizeInventory() 
	{
		// TODO Auto-generated method stub
		return chestContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		// TODO Auto-generated method stub
		return chestContents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) 
	{
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
				if (stack.stackSize <= amt)
				{
					setInventorySlotContents(slot, null);
				}
				else
				{
					stack = stack.splitStack(amt);
					if (stack.stackSize == 0)
					{
						setInventorySlotContents(slot, null);
					}
				}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) 
	{
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) 
	{
		chestContents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		
	}

	@Override
	public int getInventoryStackLimit() 
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
	{
		// TODO Auto-generated method stub
		return true;
	}



	@Override
	public String getInventoryName() {
		return "GasChamberPort";
	}



	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public void openInventory() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int arg0, ItemStack arg1) {
		// TODO Auto-generated method stub
		return true;
	}
}
