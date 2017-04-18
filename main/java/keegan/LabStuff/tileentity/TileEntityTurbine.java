package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockTurbine;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.*;

public class TileEntityTurbine extends TileEntity implements ITickable{
	private boolean top;
	private boolean isMultiblock;
	private int width;
	private int length;
	private int height;
	public float angle;
	private ArrayList<TileEntityTurbineValve> valves;
	private ArrayList<TileEntityTurbineVent> vents;

	public TileEntityTurbine() {
		width = 0;
		length = 0;
		height = 0;
		angle = 0;
		valves = new ArrayList<TileEntityTurbineValve>();
		vents = new ArrayList<TileEntityTurbineVent>();
	}

	
	//Sync
	
		@Override
		public NBTTagCompound getUpdateTag()
		{
			NBTTagCompound syncData = super.getUpdateTag();
			syncData.setFloat("angle", angle);
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
			angle = pkt.getNbtCompound().getFloat("angle");
		}
	
	@Override
	public void update() {
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();
		if (worldObj.getBlockState(pos.up()) != null && !worldObj.getBlockState(pos.up()).getBlock().equals(LabStuffMain.blockTurbineRotor)) 
		{
			top = true;
			isMultiblock = isMultiblock();
			if (isMultiblock) {
				int stored = 0;
				for(TileEntityTurbineValve valve : valves)
				{
					if(valve.getTankInfo(null)[0].fluid != null)
					{
						if(valve.getTankInfo(null)[0].fluid != null && valve.getTankInfo(null)[0].fluid.getUnlocalizedName().equals("fluid.steam"))
						{
							int amount = valve.drain(null, valve.getTankInfo(null)[0].fluid.amount, true).amount;
							stored += amount;
						}
					}
				}
				int energy = stored*(height - 2)*width*length*3;
				if(energy > 0)
				{
					for(int i = 0; i < height; i++)
					{
						if(getBlock(xCoord, yCoord-i, zCoord).equals(LabStuffMain.blockTurbineRotor))
						{
							((TileEntityTurbine)getTileEntity(xCoord, yCoord-i, zCoord)).angle += 10;
							worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
						}
					}
					for(TileEntityTurbineVent vent : vents)
					{
						vent.fill(null, new FluidStack(FluidRegistry.WATER, stored/vents.size()), true);
					}
					for(TileEntityTurbineValve valve : valves)
					{
						valve.buffer.receiveEnergy(energy/valves.size(), false);
					}
				}
			}
		}
	}
	
	private TileEntity getTileEntity(int x, int y, int z) {
		// TODO Auto-generated method stub
		return worldObj.getTileEntity(new BlockPos(x, y, z));
	}

	private Block getBlock(int x, int y, int z) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(new BlockPos(x, y, z)).getBlock();
	}

	public boolean isMultiblock() 
	{
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();
		int posWidth = 0;
		int negWidth = 0;
		for(int i = 1; i < 10; i++)
		{
			if(getBlock(xCoord+i, yCoord+1, zCoord) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord+i, yCoord+1, zCoord)).validCasing())
					posWidth += 1;
			if(getBlock(xCoord-i, yCoord+1, zCoord) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord-i, yCoord+1, zCoord)).validCasing())
					negWidth += 1;
		}
		
		if(posWidth != negWidth)
		{
			return false;
		}
		if(!(getBlock(xCoord+posWidth, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing) && getBlock(xCoord-negWidth, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing)))
		{
			return false;
		}
		
		int posLength = 0;
		int negLength = 0;
		for(int i = 1; i < 10; i++)
		{
			if(getBlock(xCoord, yCoord+1, zCoord+i) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord, yCoord+1, zCoord+i)).validCasing())
					posLength += 1;
			if(getBlock(xCoord, yCoord+1, zCoord-i) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord, yCoord+1, zCoord-i)).validCasing())
					negLength += 1;
		}
		
		if(posLength != negLength)
		{
			return false;
		}
		if(!(getBlock(xCoord+posLength, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing) && getBlock(xCoord-negLength, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing)))
		{
			return false;
		}
		if(posLength != posWidth)
		{
			return false;
		}
		
		width = posWidth*2+1;
		length = posLength*2+1;
		if(width != length)
			return false;
		if(width % 2 == 0)
			return false;
		
		//Check Roof Frame
		for(int i = 0; i < posWidth+1; i++)
		{
			if(!getBlock(xCoord-i, yCoord+1, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+i, yCoord+1, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}			
			if(!getBlock(xCoord-i, yCoord+1, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+i, yCoord+1, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord-posWidth, yCoord+1, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+posWidth, yCoord+1, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord-posWidth, yCoord+1, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+posWidth, yCoord+1, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
		}
		//Check Roof
		for(int i = 0; i < posWidth; i++)
		{
			for(int j = 0; j < posWidth; j++)
			{
				if(getBlock(xCoord+i, yCoord+1, zCoord+j) == null || !(getBlock(xCoord+i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-i, yCoord+1, zCoord+j) == null || !(getBlock(xCoord-i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord+i, yCoord+1, zCoord-j) == null || !(getBlock(xCoord+i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+i, yCoord+1, zCoord-j)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-i, yCoord+1, zCoord-j) == null || !(getBlock(xCoord-i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-i, yCoord+1, zCoord-j)).validCasing())
				{
					return false;
				}
			}
		}
		
		//Pillars
		int posnegHeight = 0;
		int posposHeight = 0;
		int negposHeight = 0;
		int negnegHeight = 0;
		for(int i = 1; i < 17; i++)
		{
			if(getBlock(xCoord+posWidth, yCoord-i, zCoord+posLength) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord+posWidth, yCoord-i, zCoord+posLength)).validCasing())
					posposHeight += 1;
			if(getBlock(xCoord+posWidth, yCoord-i, zCoord-posLength) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord+posWidth, yCoord-i, zCoord-posWidth)).validCasing())
					posnegHeight += 1;
			if(getBlock(xCoord-posWidth, yCoord-i, zCoord+posLength) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord-posWidth, yCoord-i, zCoord+posLength)).validCasing())
					negposHeight += 1;
			if(getBlock(xCoord-posWidth, yCoord-i, zCoord-posLength) instanceof BlockTurbine)
				if(((BlockTurbine)getBlock(xCoord-posWidth, yCoord-i, zCoord-posLength)).validCasing())
					negnegHeight += 1;
		}
		if(posnegHeight != posposHeight && negposHeight != negnegHeight &&  posposHeight != negposHeight && posnegHeight != negposHeight && posnegHeight != negnegHeight && posposHeight != negnegHeight)
		{
			return false;
		}
		height = posposHeight;
		
		//Check Floor frame
		for(int i = 0; i < posWidth+1; i++)
		{
			if(!getBlock(xCoord-i, yCoord-height, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+i, yCoord-height, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord-i, yCoord-height, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+i, yCoord-height, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord-posWidth, yCoord-height, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+posWidth, yCoord-height, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord-posWidth, yCoord-height, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!getBlock(xCoord+posWidth, yCoord-height, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
		}
		//Check Floor
		for(int i = 0; i < posWidth; i++)
		{
			for(int j = 0; j < posWidth; j++)
			{
				if(getBlock(xCoord+i, yCoord-height, zCoord+j) == null || !(getBlock(xCoord+i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-i, yCoord-height, zCoord+j) == null || !(getBlock(xCoord-i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord+i, yCoord-height, zCoord-j) == null || !(getBlock(xCoord+i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+i, yCoord+1, zCoord-j)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-i, yCoord-height, zCoord-j) == null || !(getBlock(xCoord-i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-i, yCoord+1, zCoord-j)).validCasing())
				{
					return false;
				}
			}
		}
		
		//Check Walls
		for(int i = 0; i < posWidth; i++)
		{
			for(int j = 1; j < height; j++)
			{
				if(getBlock(xCoord+i, yCoord-j, zCoord+posLength) == null || !(getBlock(xCoord+i, yCoord-j, zCoord+posLength) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+i, yCoord-j, zCoord+posLength)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-i, yCoord-j, zCoord+posLength) == null || !(getBlock(xCoord-i, yCoord-j, zCoord+posLength) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-i, yCoord-j, zCoord+posLength)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord+i, yCoord-j, zCoord-posLength) == null || !(getBlock(xCoord+i, yCoord-j, zCoord-posLength) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+i, yCoord-j, zCoord-posLength)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-i, yCoord-j, zCoord-posLength) == null || !(getBlock(xCoord-i, yCoord-j, zCoord-posLength) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-i, yCoord-j, zCoord-posLength)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord+posWidth, yCoord-j, zCoord+i) == null || !(getBlock(xCoord+posWidth, yCoord-j, zCoord+i) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+posWidth, yCoord-j, zCoord+i)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-posWidth, yCoord-j, zCoord+i) == null || !(getBlock(xCoord-posWidth, yCoord-j, zCoord+i) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-posWidth, yCoord-j, zCoord+i)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord+posWidth, yCoord-j, zCoord-i) == null || !(getBlock(xCoord+posWidth, yCoord-j, zCoord-i) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord+posWidth, yCoord-j, zCoord-i)).validCasing())
				{
					return false;
				}
				if(getBlock(xCoord-posWidth, yCoord-j, zCoord-i) == null || !(getBlock(xCoord-posWidth, yCoord-j, zCoord-i) instanceof BlockTurbine) || !((BlockTurbine)getBlock(xCoord-posWidth, yCoord-j, zCoord-i)).validCasing())
				{
					return false;
				}
			}
		}
		
		//Rotor
		for(int i = 1; i < height; i++)
		{
			if(!(getBlock(xCoord, yCoord-i, zCoord) != null && getBlock(xCoord, yCoord-i, zCoord).equals(LabStuffMain.blockTurbineRotor) && worldObj.getBlockState(new BlockPos(xCoord, yCoord-i, zCoord)).getValue(((BlockTurbine)blockType).TURBINES) == 2))
			{
				return false;
			}
		}
		
		//Coil
		for(int i = 1; i < posWidth; i++)
		{
			for(int j = 1; j < posWidth; j++)
			{
				if(i == 0 && j == 0){}
				else if(getBlock(xCoord+i, yCoord, zCoord+j) == null || !getBlock(xCoord+i, yCoord, zCoord+j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
				else if(getBlock(xCoord-i, yCoord, zCoord+j) == null || !getBlock(xCoord-i, yCoord, zCoord+j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
				else if(getBlock(xCoord+i, yCoord, zCoord-j) == null || !getBlock(xCoord+i, yCoord, zCoord-j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
				else if(getBlock(xCoord-i, yCoord, zCoord-j) == null || !getBlock(xCoord-i, yCoord, zCoord-j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
			}
		}
		vents.clear();
		valves.clear();
		//Vents and Valves search
		for(int i = 0; i < posWidth; i++)
		{
			for(int j = 1; j < height; j++)
			{
				if(getBlock(xCoord+i, yCoord-j,zCoord+posWidth) != null && getBlock(xCoord+i,yCoord-j,zCoord+posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve) getTileEntity(xCoord+i,yCoord-j,zCoord+posWidth));
				if(getBlock(xCoord+i, yCoord-j,zCoord-posWidth) != null && getBlock(xCoord+i,yCoord-j,zCoord-posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve) getTileEntity(xCoord+i,yCoord-j,zCoord-posWidth));
				if(getBlock(xCoord-i, yCoord-j,zCoord+posWidth) != null && getBlock(xCoord-i,yCoord-j,zCoord+posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve) getTileEntity(xCoord-i,yCoord-j,zCoord+posWidth));
				if(getBlock(xCoord-i, yCoord-j,zCoord-posWidth) != null && getBlock(xCoord-i,yCoord-j,zCoord-posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve) getTileEntity(xCoord-i,yCoord-j,zCoord-posWidth));
				
				if(getBlock(xCoord+posWidth,yCoord-j,zCoord+i) != null && getBlock(xCoord+posWidth,yCoord-j,zCoord+i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)getTileEntity(xCoord+posWidth,yCoord-j,zCoord+i));
				if(getBlock(xCoord+posWidth,yCoord-j,zCoord-i) != null && getBlock(xCoord+posWidth,yCoord-j,zCoord-i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)getTileEntity(xCoord+posWidth,yCoord-j,zCoord-i));
				if(getBlock(xCoord-posWidth,yCoord-j,zCoord+i) != null && getBlock(xCoord-posWidth,yCoord-j,zCoord+i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)getTileEntity(xCoord-posWidth,yCoord-j,zCoord+i));
				if(getBlock(xCoord-posWidth,yCoord-j,zCoord-i) != null && getBlock(xCoord-posWidth,yCoord-j,zCoord-i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)getTileEntity(xCoord-posWidth,yCoord-j,zCoord-i));
			}
			if(getBlock(xCoord+posWidth,yCoord,zCoord+i) != null && getBlock(xCoord+posWidth,yCoord,zCoord+i).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord+posWidth,yCoord,zCoord+i));
			if(getBlock(xCoord+posWidth,yCoord,zCoord-i) != null && getBlock(xCoord+posWidth,yCoord,zCoord-i).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord+posWidth,yCoord,zCoord-i));
			if(getBlock(xCoord-posWidth,yCoord,zCoord+i) != null && getBlock(xCoord-posWidth,yCoord,zCoord+i).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord-posWidth,yCoord,zCoord+i));
			if(getBlock(xCoord-posWidth,yCoord,zCoord-i) != null && getBlock(xCoord-posWidth,yCoord,zCoord-i).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord-posWidth,yCoord,zCoord-i));
			
			if(getBlock(xCoord+i,yCoord,zCoord+posWidth) != null && getBlock(xCoord+i,yCoord,zCoord+posWidth).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord+i,yCoord,zCoord+posWidth));
			if(getBlock(xCoord+i,yCoord,zCoord-posWidth) != null && getBlock(xCoord+i,yCoord,zCoord-posWidth).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord+i,yCoord,zCoord-posWidth));
			if(getBlock(xCoord-i,yCoord,zCoord+posWidth) != null && getBlock(xCoord-i,yCoord,zCoord+posWidth).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord-i,yCoord,zCoord+posWidth));
			if(getBlock(xCoord-i,yCoord,zCoord-posWidth) != null && getBlock(xCoord-i,yCoord,zCoord-posWidth).equals(LabStuffMain.blockTurbineVent))
				vents.add((TileEntityTurbineVent)getTileEntity(xCoord-i,yCoord,zCoord-posWidth));
		}
		
		if(valves.size() == 0 || vents.size() < 2)
		{
			return false;
		}
		
		return true;
	}


}
