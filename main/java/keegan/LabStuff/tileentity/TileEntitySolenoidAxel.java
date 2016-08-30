package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntitySolenoidAxel extends TileEntityRotary
{
	
	public float angle;
	
	public TileEntitySolenoidAxel()
	{
		setDirIn(ForgeDirection.DOWN);
		setDirOut(null);
		angle = 0;
	}
	
	//Sync
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound syncData = new NBTTagCompound();
		syncData.setFloat("angle", angle);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, syncData);
	}
		
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		angle = pkt.func_148857_g().getFloat("angle");
	}
	
	@Override
	public void updateEntity()
	{
		if(isMultiBlock() && this.getEnergy() >= 250000)
		{
			subEnergy(250000);
			angle += 20f;
			if(worldObj.getBlock(xCoord+10, yCoord+1, zCoord) != null && worldObj.getBlock(xCoord+10, yCoord+1, zCoord).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord+10, yCoord+1, zCoord)).setRunning(true);
			}
			if(worldObj.getBlock(xCoord-10, yCoord+1, zCoord) != null && worldObj.getBlock(xCoord-10, yCoord+1, zCoord).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord-10, yCoord+1, zCoord)).setRunning(true);
			}
			if(worldObj.getBlock(xCoord, yCoord+1, zCoord+10) != null && worldObj.getBlock(xCoord, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord, yCoord+1, zCoord+10)).setRunning(true);
			}
			if(worldObj.getBlock(xCoord, yCoord+1, zCoord-10) != null && worldObj.getBlock(xCoord, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionPlasmaTap) && torusComplete())
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord, yCoord+1, zCoord-10)).setRunning(true);
			}
			//Get the power
			
			if(worldObj.getBlock(xCoord+10, yCoord+1, zCoord) != null && torusComplete() && worldObj.getBlock(xCoord+10, yCoord+1, zCoord).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord+10, yCoord+1, zCoord)).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord+10, yCoord+1, zCoord)).burnPlasma();
				if(worldObj.getBlock(xCoord+10, yCoord, zCoord) != null && worldObj.getBlock(xCoord+10, yCoord+1, zCoord).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)worldObj.getTileEntity(xCoord+10, yCoord, zCoord)).addPower();
				}
			}
			if(worldObj.getBlock(xCoord-10, yCoord+1, zCoord) != null && torusComplete() && worldObj.getBlock(xCoord-10, yCoord+1, zCoord).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord, yCoord+1, zCoord+10)).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord-10, yCoord+1, zCoord)).burnPlasma();
				if(worldObj.getBlock(xCoord-10, yCoord, zCoord) != null && worldObj.getBlock(xCoord-10, yCoord, zCoord).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)worldObj.getTileEntity(xCoord-10, yCoord, zCoord)).addPower();
				}
			}
			if(worldObj.getBlock(xCoord, yCoord+1, zCoord+10) != null && torusComplete() && worldObj.getBlock(xCoord, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord, yCoord+1, zCoord+10)).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord, yCoord+1, zCoord+10)).burnPlasma();
				if(worldObj.getBlock(xCoord, yCoord, zCoord+10) != null && worldObj.getBlock(xCoord, yCoord, zCoord+10).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)worldObj.getTileEntity(xCoord, yCoord, zCoord+10)).addPower();
				}
			}
			if(worldObj.getBlock(xCoord, yCoord+1, zCoord-10) != null && torusComplete() && worldObj.getBlock(xCoord, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionPlasmaTap) && ((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord, yCoord+1, zCoord+10)).getPlasma() > 250)
			{
				((TileEntityPlasmaTap)worldObj.getTileEntity(xCoord, yCoord+1, zCoord-10)).burnPlasma();
				if(worldObj.getBlock(xCoord, yCoord, zCoord-10) != null && worldObj.getBlock(xCoord, yCoord, zCoord-10).equals(LabStuffMain.blockFusionHeatExchange))
				{
					((TileEntityHeatExchange)worldObj.getTileEntity(xCoord, yCoord, zCoord-10)).addPower();
				}
			}
		}
			
	}
	
	private boolean torusComplete() 
	{
		if(worldObj.getBlock(xCoord+10, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+10, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-10, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-10, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+1, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-1, yCoord+1, zCoord-10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+1, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-1, yCoord+1, zCoord+10).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+7, yCoord+1, zCoord+7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-7, yCoord+1, zCoord+7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+7, yCoord+1, zCoord-7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-7, yCoord+1, zCoord-7).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+9, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-9, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+9, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-9, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+4, yCoord+1, zCoord+9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-4, yCoord+1, zCoord+9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord+4, yCoord+1, zCoord-9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlock(xCoord-4, yCoord+1, zCoord-9).equals(LabStuffMain.blockFusionToroidalMagnet)
			&& worldObj.getBlockMetadata(xCoord+10, yCoord+1, zCoord-1)==4
			&& worldObj.getBlockMetadata(xCoord+10, yCoord+1, zCoord+1)==4
			&& worldObj.getBlockMetadata(xCoord-10, yCoord+1, zCoord-1)==4
			&& worldObj.getBlockMetadata(xCoord-10, yCoord+1, zCoord+1)==4
			&& worldObj.getBlockMetadata(xCoord+1, yCoord+1, zCoord-10)==0
			&& worldObj.getBlockMetadata(xCoord-1, yCoord+1, zCoord-10)==0
			&& worldObj.getBlockMetadata(xCoord+1, yCoord+1, zCoord+10)==0
			&& worldObj.getBlockMetadata(xCoord-1, yCoord+1, zCoord+10)==0
			&& worldObj.getBlockMetadata(xCoord+7, yCoord+1, zCoord+7)==2
			&& worldObj.getBlockMetadata(xCoord-7, yCoord+1, zCoord+7)==6
			&& worldObj.getBlockMetadata(xCoord+7, yCoord+1, zCoord-7)==6
			&& worldObj.getBlockMetadata(xCoord-7, yCoord+1, zCoord-7)==2
			&& worldObj.getBlockMetadata(xCoord+9, yCoord+1, zCoord+4)==3
			&& worldObj.getBlockMetadata(xCoord-9, yCoord+1, zCoord+4)==5
			&& worldObj.getBlockMetadata(xCoord+9, yCoord+1, zCoord-4)==5
			&& worldObj.getBlockMetadata(xCoord-9, yCoord+1, zCoord-4)==3
			&& worldObj.getBlockMetadata(xCoord+4, yCoord+1, zCoord+9)==1
			&& worldObj.getBlockMetadata(xCoord-4, yCoord+1, zCoord+9)==7
			&& worldObj.getBlockMetadata(xCoord+4, yCoord+1, zCoord-9)==7
			&& worldObj.getBlockMetadata(xCoord-4, yCoord+1, zCoord-9)==1)
			return true;
		return false;
	}

	public boolean isMultiBlock()
	{
		if(!(worldObj.getBlock(xCoord,  yCoord-1, zCoord).equals(LabStuffMain.blockFusionSolenoidAxel)))
		{
			if(worldObj.getBlock(xCoord, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidAxel)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidAxel)
					&& worldObj.getBlock(xCoord+1, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+4, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-4, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-4, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoidArm)
					&& worldObj.getBlock(xCoord+5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+3, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+2, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+1, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+3, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+2, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+1, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-3, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-2, yCoord, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-1, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-3, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-2, yCoord, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-1, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+1, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+1, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+3, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+2, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+1, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+1, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+1, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+3, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+2, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+1, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+1, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+1, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+1, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-3, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-2, yCoord+1, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-1, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+1, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+1, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+1, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+1, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+1, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-3, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-2, yCoord+1, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-1, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+1, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+5, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+4, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+3, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+2, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord+1, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+2, zCoord-1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+2, zCoord-2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+2, zCoord-3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord-4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord-5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+2, zCoord).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-5, yCoord+2, zCoord+1).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+2, zCoord+2).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-4, yCoord+2, zCoord+3).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-3, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-2, yCoord+2, zCoord+4).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord-1, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid)
					&& worldObj.getBlock(xCoord, yCoord+2, zCoord+5).equals(LabStuffMain.blockFusionSolenoid))
				{
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord-3,1,1+2);
					
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+1, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+1, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+1, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+1, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+1, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+1, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+1, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+1, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+1, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+1, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+1, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+5, yCoord+2, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+4, yCoord+2, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+3, yCoord+2, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+2, yCoord+2, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord+1, yCoord+2, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord+1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord+2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord+3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord+4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord+5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-5, yCoord+2, zCoord-1,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord-2,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-4, yCoord+2, zCoord-3,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-3, yCoord+2, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-2, yCoord+2, zCoord-4,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord-1, yCoord+2, zCoord-5,1,1+2);
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord+2, zCoord-5,1,1+2);
					return true;
				}
				
				for(int x = -5; x < 6; x++)
				{
					for(int z = -5; z < 6; z++)
					{
						for(int y =0; y < 3; y++)
						{
							if(worldObj.getBlock(xCoord+x, yCoord+y, zCoord+z) instanceof BlockSolenoid || worldObj.getBlock(xCoord+x, yCoord+y, zCoord+z) instanceof BlockSolenoidAxel)
								worldObj.setBlockMetadataWithNotify(xCoord+x, yCoord+y, zCoord+z, 0, 1+2);
						}
					}
				}
				return false;
		}
		return false;
	}
	
}
