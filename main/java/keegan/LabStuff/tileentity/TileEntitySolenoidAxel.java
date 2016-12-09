package keegan.labstuff.tileentity;

import org.apache.commons.lang3.BooleanUtils;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.*;
import keegan.labstuff.blocks.BlockSolenoidAxel.EnumRender;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;

public class TileEntitySolenoidAxel extends TileEntityRotary
{
	
	public float angle;
	
	int xCoord = pos.getX();
	int yCoord = pos.getY();
	int zCoord = pos.getZ();
	
	public TileEntitySolenoidAxel()
	{
		setDirIn(EnumFacing.DOWN);
		setDirOut(null);
		angle = 0;
		xCoord = pos.getX();
		yCoord = pos.getY();
		zCoord = pos.getZ();
	}
	
	//Sync
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound syncData = new NBTTagCompound();
		syncData.setFloat("angle", angle);
		return new SPacketUpdateTileEntity(pos, 1, syncData);
	}
		
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		angle = pkt.getNbtCompound().getFloat("angle");
	}
	
	@Override
	public void update()
	{
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();
		if(worldObj.getBlockState(pos.down()).getBlock() != null && worldObj.getBlockState(pos.down()).getBlock().equals(LabStuffMain.blockIndustrialMotorShaft))
//			System.out.println(isMultiBlock() + "," + (getEnergy() >= 250000) + "," + torusComplete());
		if(isMultiBlock() && this.getEnergy() >= 250000)
		{
			subEnergy(250000);
			angle += 20f;
			if(getBlock(pos.add(10,1,0)) != null && getBlock(pos.add(10,1,0)).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)getTileEntity(pos.add(10,1,0))).setRunning(true);
			}
			if(getBlock(xCoord-10, yCoord+1, zCoord) != null && getBlock(xCoord-10, yCoord+1, zCoord).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)getTileEntity(pos.subtract(new Vec3i(10,0,0)).add(0,1,0))).setRunning(true);
			}
			if(getBlock(pos.add(0,1,10)) != null && getBlock(pos.add(0,1,10)).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)getTileEntity(pos.add(0,1,10))).setRunning(true);
			}
			if(getBlock(pos.subtract(new Vec3i(0,0,10)).add(0,1,0)) != null && getBlock(pos.subtract(new Vec3i(0,0,10)).add(0,1,0)).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)getTileEntity(pos.subtract(new Vec3i(0,0,10)).add(0,1,0))).setRunning(true);
			}
			//Get the power
			
			if(getBlock(pos.add(10,1,0)) != null && torusComplete() && getBlock(pos.add(10,1,0)).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)getTileEntity(pos.add(10,1,0))).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)getTileEntity(pos.add(10,1,0))).burnPlasma();
				if(getBlock(xCoord+10, yCoord, zCoord) != null && getBlock(pos.add(10,0,0)).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)getTileEntity(xCoord+10, yCoord, zCoord)).addPower();
				}
			}
			if(getBlock(xCoord-10, yCoord+1, zCoord) != null && torusComplete() && getBlock(xCoord-10, yCoord+1, zCoord).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)getTileEntity(pos.add(0,1,0).subtract(new Vec3i(0,0,10)))).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)getTileEntity(pos.subtract(new Vec3i(0,0,10)).add(0,1,0))).burnPlasma();
				if(getBlock(xCoord-10, yCoord, zCoord) != null && getBlock(xCoord-10, yCoord, zCoord).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)getTileEntity(xCoord-10, yCoord, zCoord)).addPower();
				}
			}
			if(getBlock(pos.add(0,1,10)) != null && torusComplete() && getBlock(pos.add(0,1,10)).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)getTileEntity(pos.add(0,1,10))).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)getTileEntity(pos.add(0,1,10))).burnPlasma();
				if(getBlock(xCoord, yCoord, zCoord+10) != null && getBlock(xCoord, yCoord, zCoord+10).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)getTileEntity(xCoord, yCoord, zCoord+10)).addPower();
				}
			}
			if(getBlock(pos.subtract(new Vec3i(0,0,10)).add(0,1,0)) != null && torusComplete() && getBlock(pos.subtract(new Vec3i(0,0,10)).add(0,1,0)).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)getTileEntity(pos.subtract(new Vec3i(0,0,10)).add(0,1,0))).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)getTileEntity(pos.subtract(new Vec3i(0,0,10)).add(0,1,0))).burnPlasma();
				if(getBlock(xCoord, yCoord, zCoord-10) != null && getBlock(xCoord, yCoord, zCoord-10).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)getTileEntity(xCoord, yCoord, zCoord-10)).addPower();
				}
			}
		}
			
	}
	
	private TileEntity getTileEntity(BlockPos p) {
		// TODO Auto-generated method stub
		return worldObj.getTileEntity(p);
	}
	
	private TileEntity getTileEntity(int x, int y, int z) {
		// TODO Auto-generated method stub
		return worldObj.getTileEntity(new BlockPos(x,y,z));
	}


	private Block getBlock(BlockPos p) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(p).getBlock();
	}

	private boolean torusComplete() 
	{	
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();
		if(getBlock(xCoord+10,yCoord+1,zCoord-1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+10,yCoord+1,zCoord+1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-10, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-10, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+1, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-1, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+1, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-1, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+7, yCoord+1, zCoord+7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-7, yCoord+1, zCoord+7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+7, yCoord+1, zCoord-7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-7, yCoord+1, zCoord-7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+9, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-9, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+9, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-9, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+4, yCoord+1, zCoord+9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-4, yCoord+1, zCoord+9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord+4, yCoord+1, zCoord-9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlock(xCoord-4, yCoord+1, zCoord-9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& getBlockMetadata(xCoord+10,yCoord+1,zCoord-1)==4
			&& getBlockMetadata(xCoord+10,yCoord+1,zCoord+1)==4
			&& getBlockMetadata(xCoord-10, yCoord+1, zCoord-1)==4
			&& getBlockMetadata(xCoord-10, yCoord+1, zCoord+1)==4
			&& getBlockMetadata(xCoord+1, yCoord+1, zCoord-10)==0
			&& getBlockMetadata(xCoord-1, yCoord+1, zCoord-10)==0
			&& getBlockMetadata(xCoord+1, yCoord+1, zCoord+10)==0
			&& getBlockMetadata(xCoord-1, yCoord+1, zCoord+10)==0
			&& getBlockMetadata(xCoord+7, yCoord+1, zCoord+7)==2
			&& getBlockMetadata(xCoord-7, yCoord+1, zCoord+7)==6
			&& getBlockMetadata(xCoord+7, yCoord+1, zCoord-7)==6
			&& getBlockMetadata(xCoord-7, yCoord+1, zCoord-7)==2
			&& getBlockMetadata(xCoord+9, yCoord+1, zCoord+4)==3
			&& getBlockMetadata(xCoord-9, yCoord+1, zCoord+4)==5
			&& getBlockMetadata(xCoord+9, yCoord+1, zCoord-4)==5
			&& getBlockMetadata(xCoord-9, yCoord+1, zCoord-4)==3
			&& getBlockMetadata(xCoord+4, yCoord+1, zCoord+9)==1
			&& getBlockMetadata(xCoord-4, yCoord+1, zCoord+9)==7
			&& getBlockMetadata(xCoord+4, yCoord+1, zCoord-9)==7
			&& getBlockMetadata(xCoord-4, yCoord+1, zCoord-9)==1)
			return true;
		if(getBlock(xCoord+10,yCoord+1,zCoord-1).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+10,yCoord+1,zCoord+1).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-10, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-10, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+1, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-1, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+1, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-1, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+7, yCoord+1, zCoord+7).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-7, yCoord+1, zCoord+7).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+7, yCoord+1, zCoord-7).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-7, yCoord+1, zCoord-7).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+9, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-9, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+9, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-9, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+4, yCoord+1, zCoord+9).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-4, yCoord+1, zCoord+9).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord+4, yCoord+1, zCoord-9).equals(LabStuffMain.blockFusionToroidalMagnet)
				&& getBlock(xCoord-4, yCoord+1, zCoord-9).equals(LabStuffMain.blockFusionToroidalMagnet))
		{
				System.out.println("Toroid out of line");
		}
		else
			System.out.println("Toroid broken");
			
		return false;
	}

	private int getBlockMetadata(int i, int j, int k) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(new BlockPos(i,j,k)).getValue(BlockFusionToroidalMagnet.ANGLE);
	}

	private Block getBlock(int i, int j, int k) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(new BlockPos(i,j,k)).getBlock();
	}

	public boolean isMultiBlock()
	{
		
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();
		
		if(!(getBlock(xCoord,  yCoord-1, zCoord).equals(LabStuffMain.blockFusionSolenoidAxel)))
		{
			if(getBlock(xCoord, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidAxel)
					&& getBlock(xCoord, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidAxel)
					&& getBlock(xCoord+1, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+4, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-4, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord-3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& getBlock(xCoord+5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+3, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+2, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+1, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+3, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+2, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+1, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-3, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-2, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-1, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-3, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-2, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-1, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+1, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+1, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+3, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+2, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+1, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+1, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+1, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+3, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+2, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+1, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+1, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+1, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-3, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-2, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-1, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+1, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+1, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-3, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-2, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-1, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+3, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+2, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+1, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+5, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+4, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+3, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+2, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord+1, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-3, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-2, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-1, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-5, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-4, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-3, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-2, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord-1, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& getBlock(xCoord, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid))
				{
					worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockSolenoidAxel.RENDER, EnumRender.SOLENOID));
					worldObj.setBlockState(pos.up(), worldObj.getBlockState(pos.up()).withProperty(BlockSolenoidAxel.RENDER, EnumRender.AIR));
					worldObj.setBlockState(pos.up(2), worldObj.getBlockState(pos.up(2)).withProperty(BlockSolenoidAxel.RENDER, EnumRender.AIR));
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord-3,1,1+2);
					
					setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord+1,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord+2,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord+3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord+4,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+5,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord,1,1+2);
					setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord-1,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord-2,1,1+2);
					setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord-3,1,1+2);
					setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord-4,1,1+2);
					setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord-5,1,1+2);
					setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-5,1,1+2);
					return true;
				}
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockSolenoidAxel.RENDER, EnumRender.BLOCK));
				for(int x = -5; x < 6; x++)
				{
					for(int z = -5; z < 6; z++)
					{
						for(int y =0; y < 3; y++)
						{
							if(getBlock(xCoord+x, yCoord+y, zCoord+z) instanceof BlockSolenoid || getBlock(xCoord+x, yCoord+y, zCoord+z) instanceof BlockSolenoidAxel)
								setBlockMetadataWithNotify(xCoord+x, yCoord+y, zCoord+z, 0, 1+2);
						}
					}
				}
				return false;
		}
		return false;
	}

	private void setBlockMetadataWithNotify(int i, int j, int k, int l, int m) {
		BlockPos remote = new BlockPos(i,j,k);
		if(worldObj.getBlockState(remote) != null && (worldObj.getBlockState(remote).getBlock() instanceof BlockSolenoid))
		{
			if(l == 0)
				worldObj.setBlockState(remote, worldObj.getBlockState(remote).withProperty(BlockSolenoid.COMPLETE, false));
			else
				worldObj.setBlockState(remote, worldObj.getBlockState(remote).withProperty(BlockSolenoid.COMPLETE, true));
		}
		else if(worldObj.getBlockState(remote) != null && worldObj.getBlockState(remote).getBlock() instanceof BlockSolenoidAxel)
		{
			if(l == 0)
				worldObj.setBlockState(remote, worldObj.getBlockState(remote).withProperty(BlockSolenoidAxel.RENDER, EnumRender.BLOCK));
			else
				worldObj.setBlockState(remote, worldObj.getBlockState(remote).withProperty(BlockSolenoidAxel.RENDER, EnumRender.AIR));

		}
		
	}
	
}
