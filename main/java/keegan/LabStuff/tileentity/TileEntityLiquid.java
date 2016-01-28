package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public class TileEntityLiquid extends TileEntity implements IFluidHandler
{

	public FluidTank tank;
	private boolean networked = false;
	
	public TileEntityLiquid()
	{
		tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME*3);
	}
	
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		
		if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityLiquid && from != ForgeDirection.EAST && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)).fill(ForgeDirection.WEST, resource.copy(), doFill);
		}
		if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityLiquid && from != ForgeDirection.WEST && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)).fill(ForgeDirection.EAST, resource.copy(), doFill);
		}
		if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityLiquid && from != ForgeDirection.UP && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).fill(ForgeDirection.DOWN, resource.copy(), doFill);
		}
		if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityLiquid && from != ForgeDirection.DOWN && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).fill(ForgeDirection.UP, resource.copy(), doFill);
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityLiquid && from != ForgeDirection.SOUTH && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)).fill(ForgeDirection.NORTH, resource.copy(), doFill);
		}
		if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityLiquid && from != ForgeDirection.NORTH && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1)).fill(ForgeDirection.SOUTH, resource.copy(), doFill);
		}
		return tank.fill(resource, doFill);

	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from)
	{
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{tank.getInfo()};
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tank.writeToNBT(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		tank = tank.readFromNBT(tag);
	}
	
	@Override
	public void updateEntity()
	{
		if(!networked)
			equalize();
	}
	
	//ONLY call when the block is added!
	public void equalize()
	{
		if(!worldObj.isRemote) {
			if (worldObj.getBlock(xCoord + 1, yCoord, zCoord) != null && tank.getFluid() == null) {
				eqaulizeWith(xCoord + 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord - 1, yCoord, zCoord) != null && tank.getFluid() == null) {
				eqaulizeWith(xCoord - 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) != null && tank.getFluid() == null) {
					eqaulizeWith(xCoord, yCoord + 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) != null && tank.getFluid() == null) {
				 eqaulizeWith(xCoord, yCoord - 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord + 1) != null && tank.getFluid() == null) {
				 eqaulizeWith(xCoord, yCoord, zCoord + 1);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord - 1) != null && tank.getFluid() == null) {
				 eqaulizeWith(xCoord, yCoord, zCoord - 1);
			}
		}
	}
	
	private void eqaulizeWith(int x, int y, int z)
	{
		if(worldObj.getBlock(x,y,z) == LabStuffMain.blockLiquidPipe)
		{
			TileEntityLiquid tile = (TileEntityLiquid)worldObj.getTileEntity(x,y,z);
			tile.add(this, this);
			tank.setCapacity(tile.getTankInfo(ForgeDirection.UNKNOWN)[0].capacity);
			if(tile.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid != null)
				tank.fill(tile.getTankInfo(ForgeDirection.UNKNOWN)[0].fluid.copy(), true);
			if(tank.getFluidAmount() > 0)
				networked = true;
		}
	}

	private void add(TileEntityLiquid issuer, TileEntityLiquid tileEntityLiquid)
	{
		tank.setCapacity(tank.getCapacity() + tileEntityLiquid.tank.getCapacity());
		if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityLiquid && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != tileEntityLiquid && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)).add(this, tileEntityLiquid);
		}
		if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityLiquid && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != tileEntityLiquid && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)).add(this, tileEntityLiquid);
		}
		if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != tileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).add(this, tileEntityLiquid);
		}
		if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != tileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).add(this, tileEntityLiquid);
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != tileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)).add(this, tileEntityLiquid);
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != tileEntityLiquid && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null) 
		{
			((TileEntityLiquid) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1)).add(this, tileEntityLiquid);
		}
	}

}
