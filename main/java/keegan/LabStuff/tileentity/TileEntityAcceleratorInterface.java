package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.items.ItemDiscoveryDrive;
import keegan.labstuff.recipes.Recipes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

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
	public void updateEntity()
	{
		super.updateEntity();
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
					TileEntity core = worldObj.getTileEntity(xCoord - 6, yCoord, zCoord);
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
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "AceleratorInterface";
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
	public void writeToNBT(NBTTagCompound tag)
	{
		if(getNetwork()!=null)
		{
			int netX = getNetwork().xCoord;
			int netY = getNetwork().yCoord;
			int netZ = getNetwork().zCoord;
			int[] networkLoc = {netX,netY,netZ};

			tag.setIntArray("network", networkLoc);
			
			tag.setBoolean("upgraded", upgraded);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		int[] net = tag.getIntArray("network");
		this.register(net[0], net[1], net[2]);
		upgraded = tag.getBoolean("upgraded");
	}
}
