package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.tileentity.TileEntity;

public class TileEntityIndustrialMotorShaft extends TileEntityRotary
{
	public TileEntityIndustrialMotorShaft()
	{
		setDirIn(EnumFacing.DOWN);
		setDirOut(EnumFacing.UP);
	}
	
	@Override
	public void update()
	{
		TileEntity down = worldObj.getTileEntity(pos.down());
		if(down instanceof TileEntityIndustrialMotorContact)
		{
			TileEntityIndustrialMotorContact contact = (TileEntityIndustrialMotorContact)down;
			if(isMultiblock() && contact.isPowered)
			{
				addEnergy(250000); 
				contact.isPowered = false;
			}
		}
	}
	
	public int numberInStack()
	{
		TileEntity down = worldObj.getTileEntity(pos.down());
		if(down != null)
		{
			if(down instanceof TileEntityIndustrialMotorContact)
			{
				return 1;
			}
			else if(down instanceof TileEntityIndustrialMotorShaft)
				return ((TileEntityIndustrialMotorShaft)down).numberInStack() + 1;
		}
		return 0;
	}
	
	public boolean isMultiblock()
	{
		
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();
		
		if(this.getBlock(xCoord, yCoord-1, zCoord).equals(LabStuffMain.blockIndustrialMotorContact))
		{

			if(this.getBlock(xCoord-1, yCoord-1, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord-1, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord-1, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord-1, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord-1, zCoord) instanceof BlockIndustrialMotorContact
					&&this.getBlock(xCoord, yCoord-1, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord-1, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord-1, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord-1, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord, zCoord+1) instanceof BlockIndustrialMotor
					//&&this.getBlock(xCoord, yCoord, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord+1, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord+1, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord+1, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord+1, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord+1, zCoord) instanceof BlockIndustrialMotorShaft
					&&this.getBlock(xCoord, yCoord+1, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord+1, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord+1, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord+1, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord+2, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord+2, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord-1, yCoord+2, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord+2, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord+2, zCoord) instanceof BlockIndustrialMotorShaft
					&&this.getBlock(xCoord, yCoord+2, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord+2, zCoord-1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord+2, zCoord) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord+1, yCoord+2, zCoord+1) instanceof BlockIndustrialMotor
					&&this.getBlock(xCoord, yCoord+3, zCoord) instanceof BlockIndustrialMotorShaft)
			{
				
				this.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord-1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord+1, 1, 1+2);
				this.setBlockMetadataWithNotify(xCoord, yCoord+3, zCoord, 1, 1+2);
				return true;
			}
			this.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord-1, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord-1, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord-1, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord-1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord+1, 0, 1+2);
			this.setBlockMetadataWithNotify(xCoord, yCoord+3, zCoord, 0, 1+2);
			return false;	
		}
		return false;
	}

	private void setBlockMetadataWithNotify(int i, int j, int k, int l, int m) {
		BlockPos remote = new BlockPos(i,j,k);
		if(worldObj.getBlockState(remote) != null && (worldObj.getBlockState(remote).getBlock() instanceof BlockIndustrialMotor || worldObj.getBlockState(remote).getBlock() instanceof BlockIndustrialMotorContact || worldObj.getBlockState(remote).getBlock() instanceof BlockIndustrialMotorShaft))
		{
			if(l == 0)
				worldObj.setBlockState(remote, worldObj.getBlockState(remote).withProperty(BlockIndustrialMotor.COMPLETE, false));
			else
				worldObj.setBlockState(remote, worldObj.getBlockState(remote).withProperty(BlockIndustrialMotor.COMPLETE, true));
		}
		
	}

	private Block getBlock(int xCoord, int yCoord, int zCoord) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(new BlockPos(xCoord, yCoord, zCoord)).getBlock();
	}
}
