package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockElectrifier;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityElectrifier extends TileEntity implements IInventory
{
	private ItemStack[] chestContents;
	
	
	public TileEntityElectrifier()
	{
		chestContents = new ItemStack[4];
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
	    super.writeToNBT(nbt);

	    NBTTagList list = new NBTTagList();
	    for (int i = 0; i < this.getSizeInventory(); ++i) {
	        if (this.getStackInSlot(i) != null) {
	            NBTTagCompound stackTag = new NBTTagCompound();
	            stackTag.setByte("Slot", (byte) i);
	            this.getStackInSlot(i).writeToNBT(stackTag);
	            list.appendTag(stackTag);
	        }
	    }
	    nbt.setTag("Items", list);
	    
	    return nbt;
	}


	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	    super.readFromNBT(nbt);

	    NBTTagList list = nbt.getTagList("Items", 10);
	    for (int i = 0; i < list.tagCount(); ++i) {
	        NBTTagCompound stackTag = list.getCompoundTagAt(i);
	        int slot = stackTag.getByte("Slot") & 255;
	        this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
	    }

	}
	
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState old, IBlockState newState)
	{
		super.shouldRefresh(world, pos, old, newState);
		return old.getBlock() != newState.getBlock();
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
		chestContents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		
		electrify();
		
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
	public boolean isItemValidForSlot(int arg0, ItemStack arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Electrifier";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
        return ItemStackHelper.getAndRemove(this.chestContents, index);
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

	        for (int i = 0; i < this.chestContents.length; ++i)
	        {
	            this.chestContents[i] = null;
	        }		
	}
	
	public void electrify()
	{
		if(this.getStackInSlot(0) != null)
			worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockElectrifier.WATERED, this.getStackInSlot(0).getItem().equals(Items.WATER_BUCKET)), 1+2);
		else
			worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockElectrifier.WATERED, false), 1+2);
		if (this.getStackInSlot(0) != null && this.getStackInSlot(1) != null && this.getStackInSlot(2) != null && this.getStackInSlot(3) != null) 
		{
			
			
			if (this.getStackInSlot(0).getItem() == Items.WATER_BUCKET
					&& this.getStackInSlot(1).getItem() == LabStuffMain.itemBattery
					&& this.getStackInSlot(2).getItem() == LabStuffMain.itemTestTube
					&& this.getStackInSlot(3).getItem() == LabStuffMain.itemTestTube) 
			{
				System.out.println("ZAP!");
				this.setInventorySlotContents(0, new ItemStack(Items.BUCKET, 1));
				this.setInventorySlotContents(1, new ItemStack(LabStuffMain.itemDeadBattery, 1));
				this.setInventorySlotContents(2, new ItemStack(LabStuffMain.itemOxygenTestTube, 1));
				this.setInventorySlotContents(3, new ItemStack(LabStuffMain.itemHydrogenTestTube, 1));
				System.out.println("ZAPPED!");
			}
		}
	}
	
}
