package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.*;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityIndustrialMotorShaft extends TileEntityRotary
{
	public TileEntityIndustrialMotorShaft()
	{
		setDirIn(ForgeDirection.DOWN);
		setDirOut(ForgeDirection.UP);
	}
	
	@Override
	public void updateEntity()
	{
		if(worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) instanceof TileEntityIndustrialMotorContact)
		{
			if(((TileEntityIndustrialMotorContact)worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).isPowered && isMultiblock())
				addEnergy(500);
		}
	}
	
	public boolean isMultiblock()
	{
		if(worldObj.getBlock(xCoord-1, yCoord-1, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord-1, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord-1, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord-1, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord-1, zCoord) instanceof BlockIndustrialMotorContact
				&&worldObj.getBlock(xCoord, yCoord-1, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord-1, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord-1, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord-1, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord, zCoord+1) instanceof BlockIndustrialMotor
				//&&worldObj.getBlock(xCoord, yCoord, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord+1, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord+1, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord+1, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord+1, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord+1, zCoord) instanceof BlockIndustrialMotorShaft
				&&worldObj.getBlock(xCoord, yCoord+1, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord+1, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord+1, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord+1, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord+2, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord+2, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord-1, yCoord+2, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord+2, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord+2, zCoord) instanceof BlockIndustrialMotorShaft
				&&worldObj.getBlock(xCoord, yCoord+2, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord+2, zCoord-1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord+2, zCoord) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord+1, yCoord+2, zCoord+1) instanceof BlockIndustrialMotor
				&&worldObj.getBlock(xCoord, yCoord+3, zCoord) instanceof BlockIndustrialMotorShaft)
		{
			
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord-1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord+1, 1, 1+2);
			worldObj.setBlockMetadataWithNotify(xCoord, yCoord+3, zCoord, 1, 1+2);
				return true;
		}
		return false;	

	}
}
