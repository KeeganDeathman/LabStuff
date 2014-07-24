package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketElectrifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class TileEntityElectrifier extends TileEntity implements IInventory
{
	public static ResourceLocation Tex = new ResourceLocation("labstuff:textures/models/Electrifier.png");
	private ItemStack[] chestContents;
	public boolean electrifing = false;
	
	public TileEntityElectrifier()
	{
		chestContents  = new ItemStack[4];
	}
	
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < chestContents.length; i++) 
		{
			ItemStack stack = chestContents[i];
			if (stack != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setByte("Slot", (byte) i);
			stack.writeToNBT(tag);
			itemList.appendTag(tag);
			}
			}
			tagCompound.setTag("Inventory", itemList);
	}
	
	public void readFromNBT(NBTTagCompound tagCompound)
	{
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
		// TODO Auto-generated method stub
		return "Electrifier";
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
	public boolean isItemValidForSlot(int slot, ItemStack item) {
		if(slot == 0 && (item.getItem() == Items.water_bucket || item.getItem() == Items.bucket))
			return true;
		if(slot == 1 && item.getItem() == LabStuffMain.itemBattery)
			return true;
		if(slot == 2 && (item.getItem() == LabStuffMain.itemTestTube || item.getItem() == LabStuffMain.itemHydrogenTestTube))
			return  true;
		if(slot == 3 && (item.getItem() == LabStuffMain.itemTestTube || item.getItem() == LabStuffMain.itemOxygenTestTube))
			return  true;
		return false;
	}

	
	public void electrify(World world, boolean done)
	{
		if (this.getStackInSlot(0) != null && this.getStackInSlot(1) != null && this.getStackInSlot(2) != null && this.getStackInSlot(3) != null) 
		{
			System.out.println("slot 0:" + this.getStackInSlot(0).getUnlocalizedName());
			System.out.println("slot 1:" + this.getStackInSlot(1).getUnlocalizedName());
			System.out.println("slot 2:" + this.getStackInSlot(2).getUnlocalizedName());
			System.out.println("slot 3:" + this.getStackInSlot(3).getUnlocalizedName());
			if (!done) {
				if (this.getStackInSlot(0).getItem() == Items.water_bucket
						&& this.getStackInSlot(1).getItem() == LabStuffMain.itemBattery
						&& this.getStackInSlot(2).getItem() == LabStuffMain.itemTestTube
						&& this.getStackInSlot(3).getItem() == LabStuffMain.itemTestTube
						&& !electrifing) {
					electrifing = true;
					System.out.println("ZAP!");
					LabStuffMain.packetPipeline.sendToServer(new PacketElectrifier(this.xCoord, this.yCoord, this.zCoord, false));
				}
			} else {
				if (this.getStackInSlot(0).getItem() == Items.water_bucket
						&& this.getStackInSlot(1).getItem() == LabStuffMain.itemBattery
						&& this.getStackInSlot(2).getItem() == LabStuffMain.itemTestTube
						&& this.getStackInSlot(3).getItem() == LabStuffMain.itemTestTube
						&& electrifing) {
					electrifing = false;
					this.setInventorySlotContents(0, new ItemStack(Items.bucket, 1));
					this.setInventorySlotContents(1, new ItemStack(LabStuffMain.itemCopperIngot, 1));
					this.setInventorySlotContents(2, new ItemStack(LabStuffMain.itemOxygenTestTube, 1));
					this.setInventorySlotContents(3, new ItemStack(LabStuffMain.itemHydrogenTestTube, 1));
					System.out.println("ZAPPED!");
				}
			}
		}
	}
	
	
}
