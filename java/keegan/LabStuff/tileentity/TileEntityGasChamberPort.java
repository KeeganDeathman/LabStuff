package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.BlockGasChamberPort;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGasChamberPort extends TileEntity implements IInventory
{
	private ItemStack[] chestContents;
	
	public boolean input = false;
	public int testtubes;
	
	public TileEntityGasChamberPort()
	{
		chestContents = new ItemStack[1];
	}
	
	TileEntityGasChamberPort remoteTile;
	
	@Override
	public void updateEntity()
	{
		//get input.output state
		System.out.println("Let's do this.");
		this.input = ((BlockGasChamberPort)worldObj.getBlock(xCoord, yCoord, zCoord)).getInputState() || worldObj.getBlock(xCoord, yCoord - 2, zCoord) instanceof BlockGasChamberPort;
		if(input && worldObj.getBlock(xCoord, yCoord - 2, zCoord) instanceof BlockGasChamberPort)
		{
			remoteTile = (TileEntityGasChamberPort)worldObj.getTileEntity(xCoord, yCoord-2, zCoord);
			if (this.getStackInSlot(0) != null) 
			{
				if(this.getStackInSlot(0).stackSize > 0)
				{
					remoteTile.testtubes = this.getStackInSlot(0).stackSize;
				}
			}
		}
		if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null && !input)
		{
			if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPlasmaPipe)
			{
				TileEntityPlasmaPipe pipe = (TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
				remoteTile = (TileEntityGasChamberPort)worldObj.getTileEntity(xCoord, yCoord+2, zCoord);
				if(testtubes > 0)
				{
					pipe.addPlasma(20, this);
					remoteTile.decrStackSize(0, 1);
					testtubes-=1;
				}
			}
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
		return 64;
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
