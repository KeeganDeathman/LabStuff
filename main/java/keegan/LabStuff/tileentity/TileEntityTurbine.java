package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockTurbine;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.*;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.fluids.*;

public class TileEntityTurbine extends TileEntityPowerConnection{
	private boolean top = false;
	private boolean isMultiblock = false;
	private int width = 0;
	private int length = 0;
	private int height = 0;
	public float angle = 0;
	private ArrayList<TileEntityTurbineValve> valves = new ArrayList<TileEntityTurbineValve>();
	private ArrayList<TileEntityTurbineVent> vents = new ArrayList<TileEntityTurbineVent>();

	public TileEntityTurbine() {
	}

	
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
	public void updateEntity() {
		if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) != null && !worldObj.getBlock(xCoord, yCoord + 1, zCoord).equals(LabStuffMain.blockTurbineRotor)) 
		{
			top = true;
			isMultiblock = isMultiblock();
			if (isMultiblock) {
				int stored = 0;
				for(TileEntityTurbineValve valve : valves)
				{
					if(valve.getTankInfo(null)[0].fluid != null && valve.getTankInfo(null)[0].fluid.getFluid().equals(LabStuffMain.steam) && valve.getTankInfo(null)[0].fluid.amount > 0)
					{
						stored += valve.drain(null, valve.getTankInfo(null)[0].fluid.amount, true).amount;
					}
				}
				
				int energy = stored*height*4;
				if(energy > 0)
				{
					for(int i = 0; i < height; i++)
					{
						if(worldObj.getBlock(xCoord, yCoord-i, zCoord).equals(LabStuffMain.blockTurbineRotor))
							((TileEntityTurbine)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).angle += 10;
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
	
	public boolean isMultiblock() 
	{
		int posWidth = 0;
		int negWidth = 0;
		for(int i = 1; i < 10; i++)
		{
			if(worldObj.getBlock(xCoord+i, yCoord+1, zCoord) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord+i, yCoord+1, zCoord)).validCasing())
					posWidth += 1;
			if(worldObj.getBlock(xCoord-i, yCoord+1, zCoord) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord-i, yCoord+1, zCoord)).validCasing())
					negWidth += 1;
		}
		
		if(posWidth != negWidth)
		{
			return false;
		}
		if(!(worldObj.getBlock(xCoord+posWidth, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing) && worldObj.getBlock(xCoord-negWidth, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing)))
		{
			return false;
		}
		
		int posLength = 0;
		int negLength = 0;
		for(int i = 1; i < 10; i++)
		{
			if(worldObj.getBlock(xCoord, yCoord+1, zCoord+i) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord, yCoord+1, zCoord+i)).validCasing())
					posLength += 1;
			if(worldObj.getBlock(xCoord, yCoord+1, zCoord-i) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord, yCoord+1, zCoord-i)).validCasing())
					negLength += 1;
		}
		
		if(posLength != negLength)
		{
			return false;
		}
		if(!(worldObj.getBlock(xCoord+posLength, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing) && worldObj.getBlock(xCoord-negLength, yCoord+1, zCoord).equals(LabStuffMain.blockTurbineCasing)))
		{
			return false;
		}
		if(posLength != posWidth)
		{
			return false;
		}
		if(posLength % 2 != 0)
		{
			return false;
		}
		//Check Roof Frame
		for(int i = 0; i < posWidth+1; i++)
		{
			if(!worldObj.getBlock(xCoord-i, yCoord+1, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+i, yCoord+1, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}			
			if(!worldObj.getBlock(xCoord-i, yCoord+1, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+i, yCoord+1, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord-posWidth, yCoord+1, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+posWidth, yCoord+1, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord-posWidth, yCoord+1, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+posWidth, yCoord+1, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
		}
		//Check Roof
		for(int i = 0; i < posWidth; i++)
		{
			for(int j = 0; j < posWidth; j++)
			{
				if(worldObj.getBlock(xCoord+i, yCoord+1, zCoord+j) == null || !(worldObj.getBlock(xCoord+i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-i, yCoord+1, zCoord+j) == null || !(worldObj.getBlock(xCoord-i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord+i, yCoord+1, zCoord-j) == null || !(worldObj.getBlock(xCoord+i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+i, yCoord+1, zCoord-j)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-i, yCoord+1, zCoord-j) == null || !(worldObj.getBlock(xCoord-i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-i, yCoord+1, zCoord-j)).validCasing())
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
			if(worldObj.getBlock(xCoord+posWidth, yCoord-i, zCoord+posLength) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord+posWidth, yCoord-i, zCoord+posLength)).validCasing())
					posposHeight += 1;
			if(worldObj.getBlock(xCoord+posWidth, yCoord-i, zCoord-posLength) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord+posWidth, yCoord-i, zCoord-posWidth)).validCasing())
					posnegHeight += 1;
			if(worldObj.getBlock(xCoord-posWidth, yCoord-i, zCoord+posLength) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord-posWidth, yCoord-i, zCoord+posLength)).validCasing())
					negposHeight += 1;
			if(worldObj.getBlock(xCoord-posWidth, yCoord-i, zCoord-posLength) instanceof BlockTurbine)
				if(((BlockTurbine)worldObj.getBlock(xCoord-posWidth, yCoord-i, zCoord-posLength)).validCasing())
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
			if(!worldObj.getBlock(xCoord-i, yCoord-height, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+i, yCoord-height, zCoord+posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord-i, yCoord-height, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+i, yCoord-height, zCoord-posWidth).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord-posWidth, yCoord-height, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+posWidth, yCoord-height, zCoord+i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord-posWidth, yCoord-height, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
			if(!worldObj.getBlock(xCoord+posWidth, yCoord-height, zCoord-i).equals(LabStuffMain.blockTurbineCasing))
			{
				return false;
			}
		}
		//Check Floor
		for(int i = 0; i < posWidth; i++)
		{
			for(int j = 0; j < posWidth; j++)
			{
				if(worldObj.getBlock(xCoord+i, yCoord-height, zCoord+j) == null || !(worldObj.getBlock(xCoord+i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-i, yCoord-height, zCoord+j) == null || !(worldObj.getBlock(xCoord-i, yCoord+1, zCoord+j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-i, yCoord+1, zCoord+j)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord+i, yCoord-height, zCoord-j) == null || !(worldObj.getBlock(xCoord+i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+i, yCoord+1, zCoord-j)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-i, yCoord-height, zCoord-j) == null || !(worldObj.getBlock(xCoord-i, yCoord+1, zCoord-j) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-i, yCoord+1, zCoord-j)).validCasing())
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
				if(worldObj.getBlock(xCoord+i, yCoord-j, zCoord+posLength) == null || !(worldObj.getBlock(xCoord+i, yCoord-j, zCoord+posLength) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+i, yCoord-j, zCoord+posLength)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-i, yCoord-j, zCoord+posLength) == null || !(worldObj.getBlock(xCoord-i, yCoord-j, zCoord+posLength) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-i, yCoord-j, zCoord+posLength)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord+i, yCoord-j, zCoord-posLength) == null || !(worldObj.getBlock(xCoord+i, yCoord-j, zCoord-posLength) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+i, yCoord-j, zCoord-posLength)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-i, yCoord-j, zCoord-posLength) == null || !(worldObj.getBlock(xCoord-i, yCoord-j, zCoord-posLength) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-i, yCoord-j, zCoord-posLength)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord+i) == null || !(worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord+i) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord+i)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord+i) == null || !(worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord+i) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord+i)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord-i) == null || !(worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord-i) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord-i)).validCasing())
				{
					return false;
				}
				if(worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord-i) == null || !(worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord-i) instanceof BlockTurbine) || !((BlockTurbine)worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord-i)).validCasing())
				{
					return false;
				}
			}
		}
		
		//Rotor
		for(int i = 1; i < height; i++)
		{
			if(!(worldObj.getBlock(xCoord, yCoord-i, zCoord) != null && worldObj.getBlock(xCoord, yCoord-i, zCoord).equals(LabStuffMain.blockTurbineRotor) && worldObj.getBlockMetadata(xCoord, yCoord-i, zCoord) == 2))
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
				else if(worldObj.getBlock(xCoord+i, yCoord, zCoord+j) == null || !worldObj.getBlock(xCoord+i, yCoord, zCoord+j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
				else if(worldObj.getBlock(xCoord-i, yCoord, zCoord+j) == null || !worldObj.getBlock(xCoord-i, yCoord, zCoord+j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
				else if(worldObj.getBlock(xCoord+i, yCoord, zCoord-j) == null || !worldObj.getBlock(xCoord+i, yCoord, zCoord-j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
				else if(worldObj.getBlock(xCoord-i, yCoord, zCoord-j) == null || !worldObj.getBlock(xCoord-i, yCoord, zCoord-j).equals(LabStuffMain.blockElectromagneticCoil))
				{
					return false;
				}
			}
		}
		//Vents and Valves search
		for(int i = 0; i < posWidth; i++)
		{
			for(int j = 1; j < height; j++)
			{
				if(worldObj.getBlock(xCoord+i, yCoord-j, zCoord+posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord+i, yCoord-j, zCoord+posWidth));
				if(worldObj.getBlock(xCoord+i, yCoord-j, zCoord-posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord+i, yCoord-j, zCoord-posWidth));
				if(worldObj.getBlock(xCoord-i, yCoord-j, zCoord+posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord-i, yCoord-j, zCoord+posWidth));
				if(worldObj.getBlock(xCoord-i, yCoord-j, zCoord-posWidth).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord-i, yCoord-j, zCoord-posWidth));
				if(worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord+i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord+posWidth, yCoord-j, zCoord+i));
				if(worldObj.getBlock(xCoord+posWidth, yCoord-j, zCoord-i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord+posWidth, yCoord-j, zCoord-i));
				if(worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord+i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord-posWidth, yCoord-j, zCoord+i));
				if(worldObj.getBlock(xCoord-posWidth, yCoord-j, zCoord-i).equals(LabStuffMain.blockTurbineValve))
					valves.add((TileEntityTurbineValve)worldObj.getTileEntity(xCoord-posWidth, yCoord-j, zCoord-i));
				
				if(worldObj.getBlock(xCoord+i, yCoord, zCoord+posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+i, yCoord, zCoord+posWidth));
				if(worldObj.getBlock(xCoord+i, yCoord, zCoord-posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+i, yCoord, zCoord-posWidth));
				if(worldObj.getBlock(xCoord-i, yCoord, zCoord+posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-i, yCoord, zCoord+posWidth));
				if(worldObj.getBlock(xCoord-i, yCoord, zCoord-posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-i, yCoord, zCoord-posWidth));
				if(worldObj.getBlock(xCoord+posWidth, yCoord, zCoord+i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+posWidth, yCoord, zCoord+i));
				if(worldObj.getBlock(xCoord+posWidth, yCoord, zCoord-i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+posWidth, yCoord, zCoord-i));
				if(worldObj.getBlock(xCoord-posWidth, yCoord, zCoord+i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-posWidth, yCoord, zCoord+i));
				if(worldObj.getBlock(xCoord-posWidth, yCoord, zCoord-i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-posWidth, yCoord, zCoord-i));
				
				if(worldObj.getBlock(xCoord+i, yCoord+1, zCoord+posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+i, yCoord+1, zCoord+posWidth));
				if(worldObj.getBlock(xCoord+i, yCoord+1, zCoord-posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+i, yCoord+1, zCoord-posWidth));
				if(worldObj.getBlock(xCoord-i, yCoord+1, zCoord+posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-i, yCoord+1, zCoord+posWidth));
				if(worldObj.getBlock(xCoord-i, yCoord+1, zCoord-posWidth).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-i, yCoord+1, zCoord-posWidth));
				if(worldObj.getBlock(xCoord+posWidth, yCoord+1, zCoord+i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+posWidth, yCoord+1, zCoord+i));
				if(worldObj.getBlock(xCoord+posWidth, yCoord+1, zCoord-i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord+posWidth, yCoord+1, zCoord-i));
				if(worldObj.getBlock(xCoord-posWidth, yCoord+1, zCoord+i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-posWidth, yCoord+1, zCoord+i));
				if(worldObj.getBlock(xCoord-posWidth, yCoord+1, zCoord-i).equals(LabStuffMain.blockTurbineVent))
					vents.add((TileEntityTurbineVent)worldObj.getTileEntity(xCoord-posWidth, yCoord+1, zCoord-i));
				
			}
		}
		
		if(valves.size() == 0 || vents.size() < 2)
		{
			return false;
		}
		
		return true;
	}


}
