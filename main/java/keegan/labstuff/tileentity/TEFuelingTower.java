package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.*;

public class TEFuelingTower extends TileEntity implements IEnergyWrapper, IFluidHandlerWrapper, ITickable, IDataDevice
{
	
	private FluidTank tank = new FluidTank(1000000);
	private FluidTank tankO2 = new FluidTank(1000000);
	private double buffer;

	
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.ENERGY_STORAGE_CAPABILITY
				|| capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				|| capability == Capabilities.DATA_DEVICE_CAPABILITY
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
		else if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY || capability == Capabilities.DATA_DEVICE_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}
	
	@Override
	public void update()
	{
		if(worldObj.getTileEntity(getPos().up()) instanceof TEFuelingTower)
		{
			((TEFuelingTower)worldObj.getTileEntity(pos.up())).fill(EnumFacing.DOWN, tank.drain(tank.getFluidAmount(), true), true);
			((TEFuelingTower)worldObj.getTileEntity(pos.up())).fill(EnumFacing.DOWN, tankO2.drain(tankO2.getFluidAmount(), true), true);
		}
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
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
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
		return false;
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
		return "FuelingTower";
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
		if(resource != null && resource.getFluid().equals(LabStuffMain.kerosene))
			return tank.fill(resource, doFill);
		if(resource != null && resource.getFluid().equals(LabStuffMain.LO2))
			return tankO2.fill(resource, doFill);
		return 0;
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
		return true;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo(), tankO2.getInfo()};
	}
	
	public int getFuelLevel()
	{
		return Math.min(tank.getFluidAmount(), tankO2.getFluidAmount());
	}

	public FluidStack drainFuel(int empty) 
	{
		tankO2.drain(empty, true);
		buffer -= 1/6*empty;
		return tank.drain(empty, true);
	}
	
	@Override
	public void performAction(String string)
	{
		launchRocket(Integer.parseInt(string));
	}
	
	public void launchRocket(int range)
	{
		System.out.println("message received");
		if(worldObj.getTileEntity(pos.up()) != null && worldObj.getTileEntity(pos.up()) instanceof TEFuelingTower)
			((TEFuelingTower)worldObj.getTileEntity(pos.up())).launchRocket(range);
		else
		{

			if(worldObj.getTileEntity(pos.add(range,0,0))!= null && worldObj.getTileEntity(pos.add(range,0,0)) instanceof TileEntityRocket)
				((TileEntityRocket)worldObj.getTileEntity(pos.add(range,0,0))).launch();
			else if(worldObj.getTileEntity(pos.add(-range,0,0))!= null && worldObj.getTileEntity(pos.add(-range,0,0)) instanceof TileEntityRocket)
				((TileEntityRocket)worldObj.getTileEntity(pos.add(-range,0,0))).launch();
			else if(worldObj.getTileEntity(pos.add(0,0,range))!= null && worldObj.getTileEntity(pos.add(0,0,range)) instanceof TileEntityRocket)
				((TileEntityRocket)worldObj.getTileEntity(pos.add(0,0,range))).launch();
			else if(worldObj.getTileEntity(pos.add(0,0,-range))!= null && worldObj.getTileEntity(pos.add(0,0,-range)) instanceof TileEntityRocket)
				((TileEntityRocket)worldObj.getTileEntity(pos.add(0,0,-range))).launch();
			else
				System.out.println("No rocket");
		}
	}

	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return "fueling_tower";
	}

}
