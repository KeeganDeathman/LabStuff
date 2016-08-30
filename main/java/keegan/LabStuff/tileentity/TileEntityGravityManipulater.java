package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGravityManipulater extends TileEntity implements IInventory
{
	
	private float gravityModifier;
	private int warpTime;
	private int tickCount;
	
	private ItemStack[] chestContents  = new ItemStack[1];
	
	public TileEntityGravityManipulater()
	{
		gravityModifier = 0;
		warpTime = 0;
		tickCount = 0;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tagCompound)
	{
		super.writeToNBT(tagCompound);
		tagCompound.setFloat("gravityManipulater", gravityModifier);
		tagCompound.setInteger("warpTime", warpTime);
		tagCompound.setInteger("tickCount", tickCount);
		NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < this.chestContents.length; i++)
        {
            ItemStack stack = this.chestContents[i];
            if (stack != null)
            {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tagCompound)
	{
		super.readFromNBT(tagCompound);
		gravityModifier = tagCompound.getFloat("gravityModifier");
		warpTime = tagCompound.getInteger("warpTime");
		tickCount = tagCompound.getInteger("tickCount");
		NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
        for (int i = 0; i < tagList.tagCount(); i++)
        {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.chestContents.length)
                this.chestContents[slot] = ItemStack.loadItemStackFromNBT(tag);
        }
	}
	
	@Override
	public void updateEntity()
	{
		tickCount += 1;
		if(getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(new ItemStack(LabStuffMain.itemWarpDriveBattery)) && warpTime == 0)
		{
			decrStackSize(0, 1);
			warpTime = 36000;
		}
		if(tickCount >= 10)
		{
			tickCount = 0;
			if(warpTime > 0)
			{
				System.out.println("Remaining warp: " + warpTime);
				for(int i = 0; i < worldObj.loadedEntityList.size(); i++)
				{
					Entity entity = (Entity) worldObj.loadedEntityList.get(i);
					float distX = (float) Math.abs(entity.posX - xCoord);
					float distY = (float) Math.abs(entity.posY - yCoord);
					float distZ = (float) Math.abs(entity.posZ - zCoord);
					float distHor = (float) Math.sqrt((distX * distX) + (distZ * distZ));
					int dist = (int) Math.sqrt((distHor * distHor) + (distY * distY));
					if(gravityModifier != 0 && dist <= 25)
					{	
						if(entity instanceof EntityPlayer)
						{
							EntityPlayer player = (EntityPlayer) entity;
							if(!player.capabilities.isFlying)
							{
								player.addVelocity(0, gravityModifier, 0);
								((EntityPlayerMP) player).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(player));
								System.out.println(gravityModifier);
							}
						}
						else if(!(entity instanceof EntityPainting || entity instanceof EntityItemFrame))
						{
							entity.addVelocity(0, gravityModifier, 0);
							System.out.println(gravityModifier);
						}
					}
				}
				warpTime -= 10;
				if(warpTime == 0)
				{
				worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord+1, zCoord, new ItemStack(LabStuffMain.itemEmptyWarpDriveBattery)));
				}
			}
		}
	}

	public float getGravityModifier()
	{
		return gravityModifier;
	}

	public void setGravityModifier(float gravityModifier)
	{
		System.out.println("Changing gravity " + gravityModifier);
		this.gravityModifier = -1 * gravityModifier;
	}

	@Override
	public int getSizeInventory()
	{
		return chestContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
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
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		if(slot == 0 && (itemstack.isItemEqual(new ItemStack(LabStuffMain.itemWarpDriveBattery)) || itemstack.isItemEqual(new ItemStack(LabStuffMain.itemEmptyWarpDriveBattery))))
			return true;
		return false;
	}

	@Override
	public String getInventoryName() {
		// TODO Auto-generated method stub
		return "Gravity";
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
}
