package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.recipes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;

public class TileEntityCircuitDesignTable extends TileEntity implements IInventory
{
	private ItemStack[] chestContents  = new ItemStack[1];
	public String circuitDesign = "";
	
	
	public TileEntityCircuitDesignTable()
	{
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < chestContents.length; i++) 
		{
			ItemStack stack = chestContents[i];
			if (stack != null) 
			{
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		tagCompound.setString("Design", circuitDesign);
		
		return tagCompound;
	}
	
	public void drawCircuit(String design)
	{
		ArrayList<CircuitDesign> designs = Recipes.getCircuitDeisgns();
		if(!worldObj.isRemote && getStackInSlot(0) != null)
		{
			System.out.println("Drawing begun");
			for(int i=0; i < designs.size(); i++)
			{
				if(design.equals(designs.get(i).getName()))
					setInventorySlotContents(0, new ItemStack(designs.get(i).getDesignSheet(), getStackInSlot(0).stackSize));
			}
		}
	}
	
	
//	@Override
//	public Packet getDescriptionPacket()
//	{
//		NBTTagCompound tag = new NBTTagCompound();
//		this.writeToNBT(tag);
//		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
//	}

//	@Override
//    public void onDataPacket(INetHandler net, Packet132TileEntityData pkt)
//    {
//        NBTTagCompound tag = pkt.data;
//        System.out.println("Design recieved");
//        this.readFromNBT(tag);
//      System.out.println(circuitDesign);
//        this.drawCircuit(circuitDesign);
//   }
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); i++) 
		{
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < chestContents.length) 
			{
				chestContents[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		this.circuitDesign = tagCompound.getString("Design");
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
	public void setInventorySlotContents(int slot, ItemStack itemstack) 
	{
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		chestContents[slot] = itemstack;
		
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
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot == 0 && itemstack.getItem() == Items.PAPER)
		{
			return true;
		}
		else if(slot == 0 && itemstack.getItem() == LabStuffMain.itemBasicCircuitDesign)
		{
			return true;
		}
		return false;
	}



	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Circuit Design Table";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return ItemStackHelper.getAndRemove(chestContents, index);
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		for(ItemStack stack : chestContents)
			stack = null;
	}
}
