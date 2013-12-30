package keegan.LabStuff.tileentity;

import keegan.LabStuff.LabStuffMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetHandler;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCircuitDesignTable extends TileEntity implements IInventory
{
	private ItemStack[] inv  = new ItemStack[2];
	public String circuitDesign = "";
	
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < inv.length; i++) 
		{
			ItemStack stack = inv[i];
			if (stack != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setByte("Slot", (byte) i);
			stack.writeToNBT(tag);
			itemList.appendTag(tag);
			}
			}
			tagCompound.setTag("Inventory", itemList);
			tagCompound.setString("Design", circuitDesign);
	}
	
	public void drawCircuit(String design)
	{
		System.out.println("Drawing begun");
		if(design.equals("Basic"))
		{
			ItemStack basicDesign = new ItemStack(Items.paper);
			setInventorySlotContents(1, basicDesign);
			setInventorySlotContents(0, null);
			System.out.println("Circuit Drawn");
		}
		else
		{
			System.out.println("We found the unregonized design: " + design);
		}
	}
	
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
	}

	@Override
    public void onDataPacket(INetHandler net, Packet132TileEntityData pkt)
    {
        NBTTagCompound tag = pkt.data;
        System.out.println("Design recieved");
        this.readFromNBT(tag);
        System.out.println(circuitDesign);
        this.drawCircuit(circuitDesign);
    }

	public void readFromNBT(NBTTagCompound tagCompound)
	{
		NBTTagList tagList = tagCompound.getTagList("Inventory");
		for (int i = 0; i < tagList.tagCount(); i++) 
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inv.length) 
			{
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		this.circuitDesign = tagCompound.getString("Design");
	}

	@Override
	public int getSizeInventory() 
	{
		// TODO Auto-generated method stub
		return inv.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		// TODO Auto-generated method stub
		return inv[slot];
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
		inv[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		
	}

	@Override
	public String getInvName() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInvNameLocalized() 
	{
		// TODO Auto-generated method stub
		return false;
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
	public void openChest() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeChest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot == 0 && itemstack.getItem() == Item.paper)
		{
			return true;
		}
		else if(slot == 1 && itemstack.getItem() == LabStuffMain.itemBasicCircuitDesign)
		{
			return true;
		}
		return false;
	}

	@Override
	public String func_145825_b() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean func_145818_k_() {
		// TODO Auto-generated method stub
		return false;
	}
}
