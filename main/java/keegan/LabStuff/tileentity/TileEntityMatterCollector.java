package keegan.labstuff.tileentity;

import java.util.*;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityMatterCollector extends TileEntityPowerConnection implements IInventory 
{
	public ItemStack[] chestContents = new ItemStack[12];
	private ArrayList<ItemStack> ores;
	private ForgeDirection chuck;
	private Random rand = new Random();
	private Random rando = new Random();

	public TileEntityMatterCollector()
	{
		ores = new ArrayList<ItemStack>();
		chuck = ForgeDirection.UNKNOWN;
	}
	
	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return chestContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		// TODO Auto-generated method stub
		return chestContents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			if (stack.stackSize <= amt) {
				setInventorySlotContents(slot, null);
			} else {
				stack = stack.splitStack(amt);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		chestContents[slot] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}

	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack par1ItemStack) {
		return true;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "Enricher";
	}

	@Override
	public void closeInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasCustomInventoryName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void openInventory() {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < this.chestContents.length; i++) {
			ItemStack stack = this.chestContents[i];
			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		tagCompound.setTag("Inventory", itemList);
		tagCompound.setInteger("chuck", chuck.ordinal());
		
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);
		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < this.chestContents.length)
				this.chestContents[slot] = ItemStack.loadItemStackFromNBT(tag);
		}
		chuck = ForgeDirection.getOrientation(tagCompound.getInteger("chuck"));
	}
	
	@Override
	public void updateEntity()
	{
		if(!(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityMatterCollector))
		{
			rand.nextInt();
			rando.nextInt();
			if(ores.isEmpty())
			{
				for(String name : OreDictionary.getOreNames())
				{
					if(name.startsWith("ore") && !name.toLowerCase().contains("nether") &&  !OreDictionary.getOres(name).isEmpty())
					{
						ores.addAll(OreDictionary.getOres(name));
					}
				}
			}
			if(!ores.isEmpty() && getPowerSource() != null && getPowerSource().powerInt >= 1000)
			{
				int random = rand.nextInt(500);
				if(random == 1)
				{
					
					for(int i =0; i < chestContents.length; i++)
					{
						int ore = rando.nextInt(ores.size());
						if(getPowerSource().getPower() >= 4000)
						{
							if(getStackInSlot(i) == null)
							{
								getPowerSource().subtractPower(4000, this);
								setInventorySlotContents(i, new ItemStack(ores.get(ore).getItem(), 1));
								break;
							}
							else if(getStackInSlot(i).isItemEqual(ores.get(ore)))
							{
								getPowerSource().subtractPower(4000, this);
								setInventorySlotContents(i, new ItemStack(ores.get(ore).getItem(), getStackInSlot(i).stackSize+1));
								break;
							}
						}
					}
				}
			}
			if(!chuck.equals(ForgeDirection.UNKNOWN))
			{
				for(int i = 0; i < chestContents.length; i++)
				{ 
					if(getStackInSlot(i) != null)
					{
						if(!worldObj.getBlock(xCoord+chuck.offsetX, yCoord+chuck.offsetY, zCoord+chuck.offsetZ).equals(Blocks.air))
						{
							if(worldObj.getTileEntity(xCoord+chuck.offsetX, yCoord+chuck.offsetY, zCoord+chuck.offsetZ) != null && worldObj.getTileEntity(xCoord+chuck.offsetX, yCoord+chuck.offsetY, zCoord+chuck.offsetZ) instanceof IInventory)
							{
								IInventory chest = (IInventory) worldObj.getTileEntity(xCoord+chuck.offsetX, yCoord+chuck.offsetY, zCoord+chuck.offsetZ);
								for(int j = 0; i < chest.getSizeInventory(); j++)
								{
									if(chest.getStackInSlot(j) != null)
									{
										if(chest.getStackInSlot(j).isItemEqual(getStackInSlot(i)) && chest.getStackInSlot(j).stackSize < chest.getStackInSlot(j).getMaxStackSize())
										{
											chest.setInventorySlotContents(i, new ItemStack(getStackInSlot(i).getItem(), chest.getStackInSlot(i).stackSize+1));
											decrStackSize(i, 1);
											break;
										}
									}
									else if(!(chest.getStackInSlot(j) != null))
									{
										chest.setInventorySlotContents(j, new ItemStack(getStackInSlot(i).getItem(), 1));
										decrStackSize(i, 1);
										break;
									}
									
								}
								break;
							}
						}
						else
						{
							worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord+chuck.offsetX, yCoord+chuck.offsetY, zCoord+chuck.offsetZ, new ItemStack(getStackInSlot(i).getItem(), 1)));
							decrStackSize(i, 1);
							break;
						}
					}
				}
			}
		}
	}

	public void setChuck(ForgeDirection dir) 
	{
		chuck = dir;
	}

	public String getDirAsButton() 
	{
		if(chuck.equals(ForgeDirection.EAST))
			return "East";
		if(chuck.equals(ForgeDirection.NORTH))
			return "North";
		if(chuck.equals(ForgeDirection.WEST))
			return "West";
		if(chuck.equals(ForgeDirection.SOUTH))
			return "South";
		return "Store";
	}

	public boolean coreBlock() 
	{
		// TODO Auto-generated method stub
		return (worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityMatterCollector);
	}
}
