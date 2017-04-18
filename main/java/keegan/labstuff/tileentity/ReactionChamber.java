package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import keegan.labstuff.recipes.*;
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

public class ReactionChamber extends TileEntity implements IEnergyWrapper, IFluidHandlerWrapper, ITickable
{
	
	private FluidTank input1 = new FluidTank(10000);
	private FluidTank input2 = new FluidTank(10000);
	private FluidTank output1 = new FluidTank(10000);
	private FluidTank output2 = new FluidTank(10000);
	private double buffer;
	private ItemStack[] contents = new ItemStack[4];

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
		if(index == 2 || index == 3)
			return false;
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
		return "Reaction Chamber";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return EnumSet.allOf(EnumFacing.class);
	}

	@Override
	public EnumSet<EnumFacing> getConsumingSides() {
		return EnumSet.allOf(EnumFacing.class);
	}

	@Override
	public double getMaxOutput() {
		// TODO Auto-generated method stub
		return 1000;
	}

	@Override
	public double removeEnergyFromProvider(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		int returnthis = 0;
		if(from.equals(EnumFacing.DOWN) || from.equals(EnumFacing.UP))
		{
			returnthis = input1.fill(resource, doFill);
		}
		else
		{
			returnthis = input2.fill(resource, doFill);
		}
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		return returnthis;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		FluidStack returnthis = null;
		if(from.equals(EnumFacing.DOWN) || from.equals(EnumFacing.UP))
		{
			returnthis = output1.drain(resource.amount, doDrain);
		}
		else
		{
			returnthis = output2.drain(resource.amount, doDrain);
		}
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		return returnthis;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		FluidStack returnthis = null;
		if(from.equals(EnumFacing.DOWN) || from.equals(EnumFacing.UP))
		{
			returnthis = output1.drain(maxDrain, doDrain);
		}
		else
		{
			returnthis = output2.drain(maxDrain, doDrain);
		}
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		return returnthis;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{input1.getInfo(), input2.getInfo(), output1.getInfo(), output2.getInfo()};
	}
	
	
	//Sync
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound syncData = super.getUpdateTag();
		syncData.setDouble("energy", buffer);
		input1.writeToNBT(syncData);
		input2.writeToNBT(syncData);
		output1.writeToNBT(syncData);
		output2.writeToNBT(syncData);
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
		input1.readFromNBT(pkt.getNbtCompound());
		input2.readFromNBT(pkt.getNbtCompound());
		output1.readFromNBT(pkt.getNbtCompound());
		output2.readFromNBT(pkt.getNbtCompound());

	}

	
	public int getScaledFluidLevel(int tank, int i)
	{
		if(tank == 1)
		{
			if(input1.getCapacity() == 0 || input1.getFluid() == null)
			{
				return 0;
			}

			return input1.getFluid().amount*i / input1.getCapacity();

		}
		if(tank == 2)
		{

			if(input2.getCapacity() == 0 || input2.getFluid() == null)
			{
				return 0;
			}

			return input2.getFluid().amount*i / input2.getCapacity();
		}
		if(tank == 3)
		{
			if(output1.getCapacity() == 0 || output1.getFluid() == null)
			{
				return 0;
			}

			return output1.getFluid().amount*i / output1.getCapacity();

		}
		if(tank == 4)
		{

			if(output2.getCapacity() == 0 || output2.getFluid() == null)
			{
				return 0;
			}

			return output2.getFluid().amount*i / output2.getCapacity();
		}
		return 0;

	}
		
	@Override
	public void update()
	{
		for(Reaction reaction : Recipes.getReactions())
		{
			if(reaction.getInput1() == null || (input1.getFluid() != null && reaction.getInput1().getFluid().equals(input1.getFluid().getFluid()) && reaction.getInput1().amount <= input1.getFluidAmount()))
			{
				if(reaction.getInput2() == null || (input2.getFluid() != null && reaction.getInput2().getFluid().equals(input2.getFluid().getFluid()) && reaction.getInput2().amount <= input2.getFluidAmount()))
				{
					if(reaction.getInput1Item() == null || (getStackInSlot(0) != null && getStackInSlot(0).isItemEqual(reaction.getInput1Item())))
					{
						if(reaction.getInput2Item() == null || (getStackInSlot(1) != null && getStackInSlot(1).isItemEqual(reaction.getInput2Item())))
						{
							if(reaction.getPower() <= buffer)
							{
								if(reaction.getInput1() != null)
									input1.drain(reaction.getInput1(), true);
								if(reaction.getInput2() != null)
									input2.drain(reaction.getInput2(), true);
								if(reaction.getOutput1() != null)
									output1.fill(reaction.getOutput1(), true);
								if(reaction.getOutput2() != null)
									output2.fill(reaction.getOutput2(), true);
								if(reaction.getInput1Item() != null)
									decrStackSize(0, reaction.getInput1Item().stackSize);
								if(reaction.getInput2Item() != null)
									decrStackSize(1, reaction.getInput2Item().stackSize);
								if(reaction.getOutput1Item() != null)
									incrStackSize(2, reaction.getOutput1Item().getItem(), reaction.getOutput1Item().stackSize);
								if(reaction.getOutput2Item() != null)
									incrStackSize(3, reaction.getOutput2Item().getItem(), reaction.getOutput2Item().stackSize);
								buffer -= reaction.getPower();
							}
						}
					}
				}
			}
		}
	}
	

	private void incrStackSize(int slot, Item item, int amt) 
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
