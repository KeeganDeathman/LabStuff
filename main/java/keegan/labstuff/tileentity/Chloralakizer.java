package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class Chloralakizer extends TileEntity implements IEnergyWrapper, IFluidHandlerWrapper, ITickable
{
	
	private FluidTank tank = new FluidTank(10000);
	private double buffer;
	private ItemStack[] contents = new ItemStack[3];

	@Override
	public double getEnergy() {
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub
		buffer = energy;
	}

	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		double returnthis = buffer += amount;
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		return returnthis;
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
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return contents[index];
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
        return ItemStackHelper.getAndRemove(this.contents, index);
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		contents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
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
	public String getName() {
		// TODO Auto-generated method stub
		return "Chloralakizier";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		int returnthis = tank.fill(resource, doFill);
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		return returnthis;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return FluidRegistry.WATER.equals(fluid);
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	
	//Sync
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound syncData = super.getUpdateTag();
		syncData.setDouble("energy", buffer);
		tank.writeToNBT(syncData);
		return syncData;
	}
		
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}
					
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		buffer = pkt.getNbtCompound().getDouble("energy");
		tank.readFromNBT(pkt.getNbtCompound());
	}

	
	public int getScaledFluidLevel(int i)
	{
		if(tank.getCapacity() == 0 || tank.getFluid() == null)
		{
			return 0;
		}

		return tank.getFluid().amount*i / tank.getCapacity();
	}
		
	@Override
	public void update()
	{
		if(getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(new ItemStack(LabStuffMain.salt)) && getStackInSlot(0).stackSize >= 2 && tank.getFluidAmount() >= 40 && buffer >= 50)
		{
			decrStackSize(0, 2);
			tank.drain(40, true);
			addStackSize(1,LabStuffMain.lye, 2);
			addStackSize(2,LabStuffMain.itemHydrogenTestTube, 2);
			buffer -= 50;
		}
	}
	

	private void addStackSize(int slot, Item item, int amt) 
	{
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
			if (stack.stackSize + amt > stack.getMaxStackSize())
			{
				setInventorySlotContents(slot, new ItemStack(item, stack.getMaxStackSize()));
			}
			else
			{
				setInventorySlotContents(slot, new ItemStack(item, stack.stackSize + amt));
			}
		}
		else
		{
			setInventorySlotContents(slot, new ItemStack(item, amt));
		}
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.ENERGY_STORAGE_CAPABILITY
				|| capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	public CapabilityWrapperManager manager = new CapabilityWrapperManager(IFluidHandlerWrapper.class, FluidHandlerWrapper.class);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return (T)manager.getWrapper(this, side);
		}
		else if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}


}
