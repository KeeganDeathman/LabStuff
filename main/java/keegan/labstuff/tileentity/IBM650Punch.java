package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.*;
import keegan.labstuff.common.capabilities.Capabilities;
import keegan.labstuff.network.IDataDevice;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

public class IBM650Punch extends TileEntity implements IInventory, ITickable, IDataDevice
{

	@Override
	public void update() {
		isValid();
	}
	
	public boolean isValid()
	{
		if(worldObj.getBlockState(pos.add(1,0,0)) != null && worldObj.getBlockState(pos.add(1,0,0)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(-1,0,0)) != null && worldObj.getBlockState(pos.add(-1,0,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Punch.RENDER, 1));
				return true;
			}
		}
		if(worldObj.getBlockState(pos.add(0,0,1)) != null && worldObj.getBlockState(pos.add(0,0,1)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(0,0,-1)) != null && worldObj.getBlockState(pos.add(0,0,-1)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Punch.RENDER, 2));
				return true;
			}
		}
		else
			worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Punch.RENDER, 0));
		return false;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
				|| capability == Capabilities.DATA_DEVICE_CAPABILITY
				|| super.hasCapability(capability, facing);
	}


	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == Capabilities.DATA_DEVICE_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}
	
	private ItemStack[] chestContents = new ItemStack[1];


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return chestContents[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
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
	public ItemStack removeStackFromSlot(int index) {
		
        return ItemStackHelper.getAndRemove(this.chestContents, index);
		}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		chestContents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	public String readCard()
	{
		if(this.getStackInSlot(0) != null && ItemStack.areItemsEqual(getStackInSlot(0), new ItemStack(LabStuffMain.punchCard)))
		{
			return getStackInSlot(0).getTagCompound().getString("card");
		}
		if(this.getStackInSlot(0) != null && ItemStack.areItemsEqual(getStackInSlot(0), new ItemStack(LabStuffMain.punchDeck)))
		{
			String data = "";
			NBTTagCompound cards = (NBTTagCompound)getStackInSlot(0).getTagCompound().getTag("cards");
			for(int i = 0; i < cards.getKeySet().size(); i++)
			{
				data = data + cards.getString("card"+i);
			}
		}
		return "";
	
	}
	
	public void writeCard(int[] data)
	{
		if(data.length <= 960)
		{
			ItemStack punchDeck = new ItemStack(LabStuffMain.punchCard, 1);
			if(punchDeck.getTagCompound() == null)
				punchDeck.setTagCompound(new NBTTagCompound());
			punchDeck.getTagCompound().setString("card", LabStuffUtils.convertToString(data));
			setInventorySlotContents(0, punchDeck);
		}
		else
		{
			int numberOfCards = (int) Math.ceil(data.length/960);
			NBTTagCompound deck = new NBTTagCompound();
			for(int i = 0; i < numberOfCards; i++)
			{
				deck.setString("card"+(deck.getSize()+1), LabStuffUtils.convertToString(data).substring(i*960, i*960+960));
			}
			ItemStack punchDeck = new ItemStack(LabStuffMain.punchDeck, 1);
			punchDeck.getTagCompound().setTag("cards", deck);
			setInventorySlotContents(0, punchDeck);
		}
	
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return true;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return "ibm_650_punch_unit";
	}

	@Override
	public void performAction(String command) {
		// TODO Auto-generated method stub
		
	}

}
