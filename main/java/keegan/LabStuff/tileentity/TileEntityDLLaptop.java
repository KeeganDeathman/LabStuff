package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockDLLaptop;
import keegan.labstuff.recipes.Recipes;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDLLaptop extends TileEntity
{
	
	public boolean noTablet;
	public int beenOpened;
	
	public ItemStack tabletItem;
	
	public TileEntityDLLaptop()
	{
		noTablet = true;
		setBeenOpened(1);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setBoolean("Tablet", isTablet());
		if(getTabletItem() != null)
			getTabletItem().writeToNBT(tag);
		tag.setInteger("BeenOpened", getBeenOpened());
		
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		setTablet(tag.getBoolean("Tablet"));
		setTabletItem(ItemStack.loadItemStackFromNBT(tag));
		setBeenOpened(tag.getInteger("BeenOpened"));
	}

	public boolean isTablet() {
		return this.noTablet;
	}

	public void setTablet(boolean tabletToBe) {
		this.noTablet = tabletToBe;
	}

	public ItemStack getTabletItem() {
		return tabletItem;
	}

	public void setTabletItem(ItemStack tabletItem) {
		this.tabletItem = tabletItem;
	}
	
	public void plugInTablet(int slot, InventoryPlayer inv)
	{
		if(slot < 9 && inv.getStackInSlot(slot) != null && isTablet())
		{
			System.out.println("Plugging in tablet");
			setTabletItem(inv.getStackInSlot(slot));
			inv.decrStackSize(slot, 1);
			setTablet(false);
			worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockDLLaptop.TABLET, true));
		}
	}
	
	public void unPlugTablet(InventoryPlayer inv)
	{
		if(!isTablet())
		{
			System.out.println("Unplugging tablet");
			inv.addItemStackToInventory(getTabletItem());
			setTabletItem(null);
			setTablet(true);
			worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockDLLaptop.TABLET, false));
		}
	}
	
	public void performWebAction(String action)
	{
		Recipes.performAction(action, this);
	}
	
	public void setUpTablet()
	{
		System.out.println("Programming dPad");
		if(!isTablet())
		{
			setTabletItem(new ItemStack(LabStuffMain.itemDPad));
		}
	}

	public int getBeenOpened() {
		return beenOpened;
	}

	public void setBeenOpened(int beenOpened) {
		this.beenOpened = beenOpened;
	}
}
