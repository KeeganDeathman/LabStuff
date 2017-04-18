package keegan.labstuff.tileentity;

import java.util.HashMap;

import keegan.labstuff.blocks.*;
import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.Capabilities;
import keegan.labstuff.network.*;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;

public class IBM650Console extends TileEntity implements IDataDevice, ITickable
{

	public boolean running = false;
	private IBM650PowerUnit power = null;
	private IBM650Punch punch = null;
	
	@Override
	public void update() {
		isValid();
		worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		if(DataUtils.getNetwork(pos,worldObj) != null)
		{
			for(Coord4D key : DataUtils.getDevices(pos, worldObj).keySet())
			{
				if(worldObj.getTileEntity(key.getPos()) instanceof IBM650PowerUnit)
					power = (IBM650PowerUnit)worldObj.getTileEntity(key.getPos());
				else if(worldObj.getTileEntity(key.getPos()) instanceof IBM650Punch)
					punch = (IBM650Punch)worldObj.getTileEntity(key.getPos());
				if(punch != null && power != null)
					break;
			}
			if(punch != null && power != null && power.getEnergy() >= 70)
			{
				running = true;
				worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
				power.tick();
			}
		}
		
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.DATA_DEVICE_CAPABILITY
				|| super.hasCapability(capability, facing);
	}


	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == Capabilities.DATA_DEVICE_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("running", running);
		return tag;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		running = pkt.getNbtCompound().getBoolean("running");
	}
	
	public boolean isValid()
	{
		if(worldObj.getBlockState(pos.add(1,0,0)) != null && worldObj.getBlockState(pos.add(1,0,0)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(1,-1,0)) != null && worldObj.getBlockState(pos.add(1,-1,0)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,-1,0)) != null && worldObj.getBlockState(pos.add(0,-1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Console.RENDER, 1));
				return true;
			}
		}
		if(worldObj.getBlockState(pos.add(0,0,1)) != null && worldObj.getBlockState(pos.add(0,0,1)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(0,-1,1)) != null && worldObj.getBlockState(pos.add(0,-1,1)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,-1,0)) != null && worldObj.getBlockState(pos.add(0,-1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Console.RENDER, 2));
				return true;
			}
		}
		if(worldObj.getBlockState(pos.add(-1,0,0)) != null && worldObj.getBlockState(pos.add(-1,0,0)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(-1,-1,0)) != null && worldObj.getBlockState(pos.add(-1,-1,0)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,-1,0)) != null && worldObj.getBlockState(pos.add(0,-1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Console.RENDER, 3));
				return true;
			}
		}
		if(worldObj.getBlockState(pos.add(0,0,-1)) != null && worldObj.getBlockState(pos.add(0,0,-1)).getBlock() instanceof IBM650)
		{
			if(worldObj.getBlockState(pos.add(0,-1,-1)) != null && worldObj.getBlockState(pos.add(0,-1,-1)).getBlock() instanceof IBM650 && worldObj.getBlockState(pos.add(0,-1,0)) != null && worldObj.getBlockState(pos.add(0,-1,0)).getBlock() instanceof IBM650)
			{
				worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Console.RENDER, 4));
				return true;
			}		
		}
		else
			worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockIBM650Console.RENDER, 0));
		return false;
	}

	public void runProgram() {
		if(running)
		{
			String program = punch.readCard();
			String[] lines = program.split(";");
			HashMap<String, Double> reals = new HashMap<String, Double>();
			HashMap<String, Integer> integers = new HashMap<String, Integer>();
			for(String line : lines)
			{
				//peripheral
				if(line.startsWith("requires"))
				{
					
					boolean found = false;
					for(Coord4D key : DataUtils.getDevices(pos, worldObj).keySet())
					{
						if(DataUtils.getDevices(pos, worldObj).get(key).getDeviceType().equals(line.replace("requires ", "")))
						{
							found = true;
							break;
						}
					}
					if(!found)
					{
						punch.writeCard(LabStuffUtils.convertToBinary("Missing peripheral " + line));
						break;
						
					}
				}
				if(line.startsWith("int"))
				{
					String[] words = line.split(" ");
					if(words[2].equals("="))
						integers.put(words[1], Integer.parseInt(words[3]));
					if(words.length == 2)
						integers.put(words[1], 0);
				}
				if(line.startsWith("real"))
				{
					String[] words = line.split(" ");
					if(words[2].equals("="))
						reals.put(words[1], Double.parseDouble(words[3]));
					if(words.length == 2)
						reals.put(words[1], 0.0);
				}
				for(String key : reals.keySet())
				{
					if(integers.containsKey(key))
					{
						punch.writeCard(LabStuffUtils.convertToBinary("Name " + key + " already used"));
						break;
					}
				}
				for(String key : integers.keySet())
				{
					if(reals.containsKey(key))
					{
						punch.writeCard(LabStuffUtils.convertToBinary("Name " + key + "already used"));
						break;
					}
				}
				if(line.startsWith("add"))
				{
					line.replace("add", "");
					String[] variables = line.split(",");
					if(reals.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) + Double.parseDouble(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) + Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) + Integer.parseInt(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) + Integer.parseInt(variables[1])));
							}
						}
					}
					else if(integers.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2],  Double.parseDouble(variables[1]) + Integer.parseInt(variables[0]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) + Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], (double)(Integer.parseInt(variables[0]) + Integer.parseInt(variables[1])));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) + Integer.parseInt(variables[1])));
							}
						}
					}
				}
				if(line.startsWith("sub"))
				{

					line.replace("sub", "");
					String[] variables = line.split(",");
					if(reals.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) - Double.parseDouble(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) - Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) - Integer.parseInt(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) - Integer.parseInt(variables[1])));
							}
						}
					}
					else if(integers.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2],  Double.parseDouble(variables[1]) - Integer.parseInt(variables[0]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) - Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], (double)(Integer.parseInt(variables[0]) - Integer.parseInt(variables[1])));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) - Integer.parseInt(variables[1])));
							}
						}
					}
				}
				if(line.startsWith("mul"))
				{


					line.replace("mul", "");
					String[] variables = line.split(",");
					if(reals.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) * Double.parseDouble(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) * Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) * Integer.parseInt(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) * Integer.parseInt(variables[1])));
							}
						}
					}
					else if(integers.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2],  Double.parseDouble(variables[1]) * Integer.parseInt(variables[0]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) * Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], (double)(Integer.parseInt(variables[0]) * Integer.parseInt(variables[1])));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) * Integer.parseInt(variables[1])));
							}
						}
					}
				}
				if(line.startsWith("div"))
				{


					line.replace("div", "");
					String[] variables = line.split(",");
					if(reals.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) / Double.parseDouble(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) / Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], Double.parseDouble(variables[0]) / Integer.parseInt(variables[1]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Double.parseDouble(variables[0]) / Integer.parseInt(variables[1])));
							}
						}
					}
					else if(integers.containsKey(variables[0]))
					{
						if(reals.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2],  Double.parseDouble(variables[1]) / Integer.parseInt(variables[0]));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) / Double.parseDouble(variables[1])));
							}
						}
						else if(integers.containsKey(variables[1]))
						{
							if(reals.containsKey(variables[2]))
							{
								reals.remove(variables[2]);
								reals.put(variables[2], (double)(Integer.parseInt(variables[0]) / Integer.parseInt(variables[1])));
							}
							else if(integers.containsKey(variables[2]))
							{
								integers.remove(variables[2]);
								integers.put(variables[2], (int)(Integer.parseInt(variables[0]) / Integer.parseInt(variables[1])));
							}
						}
					}
				}
				
				if(line.startsWith("run"))
				{
					String[] variables = line.replace("run ", "").split(",");
					System.out.println(variables[0]);
					for(Coord4D key : DataUtils.getDevices(pos, worldObj).keySet())
					{
						System.out.println(DataUtils.getDevices(pos, worldObj).get(key).getDeviceType());
						if(DataUtils.getDevices(pos, worldObj).get(key).getDeviceType().equals(variables[0]))
						{
							System.out.println("Found him!");
							DataUtils.sendMessage(new DataPackage(DataUtils.getDevices(pos,worldObj).get(key), variables[1]));
							break;
						}
					}
				
				}
				
				if(line.startsWith("write"))
				{
					line.replace("write", "");
					punch.writeCard(LabStuffUtils.convertToBinary(line));
					break;
				}
				
			}
		}
	}


	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return "ibm_650_console";
	}

	@Override
	public void performAction(String command) {
		// TODO Auto-generated method stub
		
	}

}
