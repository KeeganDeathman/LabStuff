package keegan.labstuff.tileentity;

import java.util.*;

import keegan.labstuff.network.IEnergyWrapper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityMatterCollector extends TileEntity implements ITickable, IEnergyWrapper
{
	public ItemStack[] chestContents = new ItemStack[12];
	private ArrayList<ItemStack> ores;
	private EnumFacing chuck;
	private Random rand = new Random();
	private Random rando = new Random();
	private int buffer = 0;

	public TileEntityMatterCollector()
	{
		ores = new ArrayList<ItemStack>();
		chuck = null;
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
		return "Matter Collector";
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
	public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
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
		
		return tagCompound;
		
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
		chuck = EnumFacing.getFront(tagCompound.getInteger("chuck"));
	}
	
	@Override
	public void update()
	{
		if(!(worldObj.getTileEntity(pos.down()) instanceof TileEntityMatterCollector))
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
			if(!ores.isEmpty()  && buffer >= 1000)
			{
				int random = rand.nextInt(500);
				if(random == 1)
				{
					
					for(int i =0; i < chestContents.length; i++)
					{
						int ore = rando.nextInt(ores.size());
						if(buffer >= 4000)
						{
							if(getStackInSlot(i) == null)
							{
								buffer -= 4000;
								setInventorySlotContents(i, new ItemStack(ores.get(ore).getItem(), 1));
								break;
							}
							else if(getStackInSlot(i).isItemEqual(ores.get(ore)))
							{
								buffer -= 4000;
								setInventorySlotContents(i, new ItemStack(ores.get(ore).getItem(), getStackInSlot(i).stackSize+1));
								break;
							}
						}
					}
				}
			}
			if(chuck != null)
			{
				for(int i = 0; i < chestContents.length; i++)
				{ 
					if(getStackInSlot(i) != null)
					{
						if(!worldObj.getBlockState(pos.offset(chuck)).getBlock().equals(Blocks.AIR))
						{
							if(worldObj.getTileEntity(pos.offset(chuck)) != null && worldObj.getTileEntity(pos.offset(chuck)) instanceof IInventory)
							{
								IInventory chest = (IInventory) worldObj.getTileEntity(pos.offset(chuck));
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
							worldObj.spawnEntityInWorld(new EntityItem(worldObj, pos.offset(chuck).getX(), pos.offset(chuck).getY(), pos.offset(chuck).getZ(), new ItemStack(getStackInSlot(i).getItem(), 1)));
							decrStackSize(i, 1);
							break;
						}
					}
				}
			}
		}
	}

	public void setChuck(EnumFacing dir) 
	{
		chuck = dir;
	}

	public String getDirAsButton() 
	{
		if(chuck.equals(EnumFacing.EAST))
			return "East";
		if(chuck.equals(EnumFacing.NORTH))
			return "North";
		if(chuck.equals(EnumFacing.WEST))
			return "West";
		if(chuck.equals(EnumFacing.SOUTH))
			return "South";
		return "Store";
	}

	public boolean coreBlock() 
	{
		// TODO Auto-generated method stub
		return (worldObj.getTileEntity(pos.up()) != null && worldObj.getTileEntity(pos.up()) instanceof TileEntityMatterCollector);
	}

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub
		buffer = (int) energy;
	}

	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 1000000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return buffer += amount;
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canOutputTo(EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return EnumSet.noneOf(EnumFacing.class);
	}

	@Override
	public EnumSet<EnumFacing> getConsumingSides() {
		// TODO Auto-generated method stub
		return EnumSet.allOf(EnumFacing.class);
	}

	@Override
	public double getMaxOutput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double removeEnergyFromProvider(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return 0;
	}
}
