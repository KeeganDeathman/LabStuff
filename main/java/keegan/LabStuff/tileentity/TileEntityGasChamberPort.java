package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.*;
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
	TileEntityGasChamberPort outputTile;
	
	@Override
	public void updateEntity()
	{
		//get input.output state
		BlockGasChamberPort me = (BlockGasChamberPort) worldObj.getBlock(xCoord, yCoord, zCoord);
		this.input = worldObj.getBlock(xCoord, yCoord - 2, zCoord) instanceof BlockGasChamberPort;
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
			if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPlasmaPipe && worldObj.getTileEntity(xCoord, yCoord + 2, zCoord) instanceof TileEntityGasChamberPort)
			{
				TileEntityPlasmaPipe pipe = (TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord, yCoord-1, zCoord);
				remoteTile = (TileEntityGasChamberPort)worldObj.getTileEntity(xCoord, yCoord+2, zCoord);
				
				if(getLaser() != null)
				{
					if(worldObj.getBlock(xCoord-1, yCoord+1, zCoord) != null && worldObj.getBlock(xCoord-1, yCoord+1, zCoord).equals(LabStuffMain.blockGasChamberPort))
						outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(xCoord-1, yCoord+1, zCoord);
					if(worldObj.getBlock(xCoord+1, yCoord+1, zCoord) != null && worldObj.getBlock(xCoord+1, yCoord+1, zCoord).equals(LabStuffMain.blockGasChamberPort))
						outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(xCoord+1, yCoord+1, zCoord);
					if(worldObj.getBlock(xCoord, yCoord+1, zCoord+1) != null && worldObj.getBlock(xCoord, yCoord+1, zCoord+1).equals(LabStuffMain.blockGasChamberPort))
						outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(xCoord, yCoord+1, zCoord+1);
					if(worldObj.getBlock(xCoord, yCoord+1, zCoord-1) != null && worldObj.getBlock(xCoord, yCoord-1, zCoord+1).equals(LabStuffMain.blockGasChamberPort))
						outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(xCoord, yCoord+1, zCoord-1);

					
					if(testtubes > 0 && getLaser().isMultiblock())
					{
						pipe.addPlasma(500, this);
						remoteTile.decrStackSize(0, 1);
						outputTile.setInventorySlotContents(0, new ItemStack(LabStuffMain.itemTestTube, outputTile.emptyTubes()+1));
						testtubes-=1;
					}
				}
			}
		}
	}
	
	public TileEntityElectronGrabber getLaser()
	{
		TileEntityElectronGrabber laser = null;
		
		if(input)
		{
			if(worldObj.getTileEntity(xCoord+1, yCoord-1, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord-1, zCoord) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord+1, yCoord-1, zCoord);
			if(worldObj.getTileEntity(xCoord-1, yCoord-1, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord-1, zCoord) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord-1, yCoord-1, zCoord);
			if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord-1, zCoord+1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord, yCoord-1, zCoord+1);
			if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord-1, zCoord-1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord, yCoord-1, zCoord-1);
		}
		else if(worldObj.getBlock(xCoord, yCoord-1, zCoord) instanceof BlockGasChamberWall && worldObj.getBlock(xCoord, yCoord+1, zCoord) instanceof BlockGasChamberWall)
		{
			if(worldObj.getTileEntity(xCoord+2, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord+2, yCoord, zCoord) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord+2, yCoord, zCoord);
			if(worldObj.getTileEntity(xCoord-2, yCoord, zCoord) != null && worldObj.getTileEntity(xCoord-2, yCoord, zCoord) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord-2, yCoord, zCoord);
			if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord+1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord+1, yCoord, zCoord+1);
			if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord+1, yCoord, zCoord-1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord+1, yCoord, zCoord-1);
			if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord+1) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord+1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord-1, yCoord, zCoord+1);
			if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord-1) != null && worldObj.getTileEntity(xCoord-1, yCoord, zCoord-1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord-1, yCoord, zCoord-1);
		}
		else
		{
			if(worldObj.getTileEntity(xCoord+1, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord+1, yCoord+1, zCoord) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord+1, yCoord+1, zCoord);
			if(worldObj.getTileEntity(xCoord-1, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord-1, yCoord+1, zCoord) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord-1, yCoord+1, zCoord);
			if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord+1) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord+1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord, yCoord+1, zCoord+1);
			if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord-1) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord-1) instanceof TileEntityElectronGrabber)
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(xCoord, yCoord+1, zCoord-1);
		}
		return laser;
	}
	
	public int emptyTubes()
	{
		if(getStackInSlot(0) != null)
			return getStackInSlot(0).stackSize;
		return 0;
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

	public boolean testTubeSlot() {
		return input || (worldObj.getBlock(xCoord, yCoord-1, zCoord).equals(LabStuffMain.blockGasChamberWall) && worldObj.getBlock(xCoord, yCoord + 1, zCoord).equals(LabStuffMain.blockGasChamberWall))
;	}
}
