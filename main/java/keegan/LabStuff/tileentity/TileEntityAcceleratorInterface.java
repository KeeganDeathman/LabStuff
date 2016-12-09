package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.ItemDiscoveryDrive;
import keegan.labstuff.recipes.Recipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;

public class TileEntityAcceleratorInterface extends DataConnectedDevice implements IInventory
{

	private ItemStack[] chestContents = new ItemStack[3];
	private TileEntityAcceleratorControlPanel control;
	private int tickCount;
	public boolean upgraded;
	
	public TileEntityAcceleratorInterface()
	{
		tickCount = 0;
		control = null;
		upgraded = false;
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
		return chestContents[slot];
	}
	
	public void activateAntiMatter()
	{
		upgraded = true;
		
	}
	
	@Override
	public void update()
	{
		super.update();
		tickCount++;
		if(tickCount>=400)
		{
			tickCount = 0;
			if(!worldObj.isRemote)
			{
				if(this.getId() == null)
					registerWithNetwork();
				if(getNetwork() != null)
				{
					while(control == null)
					{
						if(getNetwork().getDeviceCount() == 0)
							break;
						for(int i = 0; i < getNetwork().getDeviceCount(); i++)
						{
							detectControl(getNetwork().getDeviceByIndex(i).getId());
						}
					}
				}
				else
					System.out.println("no network");
				if(control != null)
				{
					if(getStackInSlot(0) != null)
					{
						DataPackage hasMatter = new DataPackage(control, "particlesLoaded");
						getNetwork().sendMessage(hasMatter);
					}
					else
					{
						DataPackage hasMatter = new DataPackage(control, "particlesNotLoaded");
						getNetwork().sendMessage(hasMatter);
					}
					TileEntity core = worldObj.getTileEntity(pos.subtract(new Vec3i(6,0,0)));
					if(core instanceof TileEntityAcceleratorDetectorCore)
					{
						if(((TileEntityAcceleratorDetectorCore) core).isGoodForLaunch())
						{
							DataPackage isPowered = new DataPackage(control, "powered");
							getNetwork().sendMessage(isPowered);
						}
						else
						{
							DataPackage isPowered = new DataPackage(control, "notPowered");
							getNetwork().sendMessage(isPowered);
						}
					}
					else
					{
						System.out.println("Wheres the core?");
					}
				}
			}
		}
	}
	
	private void detectControl(String i)
	{
		if(getNetwork().getDeviceById(i) instanceof TileEntityAcceleratorControlPanel)
		{
			System.out.println("Control detected");
			control = (TileEntityAcceleratorControlPanel) getNetwork().getDeviceById(i);
		}
	}
	
	@Override
	public void performAction(String command)
	{
		if(command.startsWith("discovery_"))
		{
			int discovery = Integer.parseInt(command.substring(command.indexOf("_")+1));
			if(getStackInSlot(1) != null && getStackInSlot(1).isItemEqual(new ItemStack(LabStuffMain.itemDiscoveryDrive)))
			{
				setInventorySlotContents(1, Recipes.accelDiscoveries.get(discovery).getDiscoveryFlashDrive());
				if(upgraded)
				{
					if(getStackInSlot(2).getItem().equals(LabStuffMain.itemEmptyWarpDriveBattery))
						setInventorySlotContents(2, new ItemStack(LabStuffMain.itemWarpDriveBattery));
				}
			}
		}
		if(command.startsWith("launch"))
		{
			System.out.println("Accelerator active");
			decrStackSize(0, 1);
		}
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
		if(slot == 0)
			return true;
		if(slot == 2 && (itemstack.isItemEqual(new ItemStack(LabStuffMain.itemWarpDriveBattery)) || itemstack.isItemEqual(new ItemStack(LabStuffMain.itemEmptyWarpDriveBattery))))
			return true;
		if(slot == 1 && itemstack.getItem() instanceof ItemDiscoveryDrive)
			return true;
		return false;
	}

	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		NBTTagList itemList = new NBTTagList();
		for (int i = 0; i < chestContents.length; i++) 
		{
			ItemStack stack = chestContents[i];
			if (stack != null) 
			{
				NBTTagCompound tagCompound = new NBTTagCompound();
				tagCompound.setByte("Slot", (byte) i);
				stack.writeToNBT(tagCompound);
				itemList.appendTag(tagCompound);
			}
		}
		tag.setTag("Inventory", itemList);
		if(getNetwork()!=null)
		{
			int netX = getNetwork().getPos().getX();
			int netY = getNetwork().getPos().getY();
			int netZ = getNetwork().getPos().getZ();
			int[] networkLoc = {netX,netY,netZ};

			tag.setIntArray("network", networkLoc);
			
			tag.setBoolean("upgraded", upgraded);
		}
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTTagList tagList = tag.getTagList("Inventory", 10);
		for (int i = 0; i < tagList.tagCount(); i++) 
		{
			NBTTagCompound tagCompound = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tagCompound.getByte("Slot");
			if (slot >= 0 && slot < chestContents.length) 
			{
				chestContents[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
			}
		}
		int[] net = tag.getIntArray("network");
		this.register(new BlockPos(net[0], net[1], net[2]));
		upgraded = tag.getBoolean("upgraded");
	}

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
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = chestContents[index];
		chestContents[index] = null;
		return stack;
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
		// TODO Auto-generated method stub
		
	}
}
