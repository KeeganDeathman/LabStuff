package keegan.labstuff.tileentity;

import java.util.*;

import keegan.labstuff.common.Coord4D;
import keegan.labstuff.network.*;
import keegan.labstuff.recipes.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityAcceleratorControlPanel extends TileEntity implements IDataDevice
{
	private boolean hasMatter;
	private boolean isPowered;
	private boolean isRunning;
	private boolean launched;
	private ArrayList<AcceleratorDiscovery> discovered = new ArrayList<AcceleratorDiscovery>();
	private int tickCount;

	public TileEntityAcceleratorControlPanel()
	{
		hasMatter = false;
		isPowered = false;
		isRunning = false;
		launched = false;
		tickCount = 0;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		for (int i = 0; i < discovered.size(); i++)
		{
			tag.setInteger("discovered_" + i, discovered.get(i).getIndex());
		}
		
		return tag;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		for (int i = 0; i < Recipes.accelDiscoveries.size(); i++)
		{
			if (tag.hasKey("discovered_" + i)) discovered.add(Recipes.accelDiscoveries.get(tag.getInteger("discovered_" + i)));
		}
	}

	@Override
	public void performAction(String command)
	{
		if (command.equals("particlesLoaded"))
		{
			hasMatter = true;
			System.out.println("loaded");
		}
		if (command.equals("particlesNotLoaded"))
		{
			hasMatter = false;
		}
		if (command.equals("powered"))
		{
			isPowered = true;
			System.out.println("powered");
		}
		if (command.equals("notPowered"))
		{
			isPowered = false;
		}
	}

	public void collision()
	{
		System.out.println(launched + " launched");
		if (DataUtils.getDevices(pos, worldObj) != null)
		{
			isRunning = launched && isPowered;
			for (Coord4D coord : DataUtils.getDevices(pos, worldObj).keySet())
			{
				if (worldObj.getTileEntity(coord.getPos()) instanceof TileEntityAcceleratorInterface)
				{
					if (isRunning)
					{
						launched = false;
						Random r = new Random();
						int R = r.nextInt(3);
						if (R == 2)
						{
							for(int j = 0; j < Recipes.accelDiscoveries.size(); j++)
							{
								AcceleratorDiscovery discov = Recipes.accelDiscoveries.get(j);
								if(!discovered.contains(discov))
								{
									if(discov.getDependency() == null)
										DataUtils.sendMessage(new DataPackage(DataUtils.getDevices(pos, worldObj).get(coord), "discovery_" + j));
									else if(discovered.contains(discov.getDependency()))
										DataUtils.sendMessage(new DataPackage(DataUtils.getDevices(pos, worldObj).get(coord), "discovery_" + j));
								}
							}
						}
					}
				}
			}
		}
	}


	public void launch()
	{
		if (DataUtils.getNetwork(pos, worldObj) != null)
		{
			for (Coord4D coord : DataUtils.getDevices(pos, worldObj).keySet())
			{
				if (worldObj.getTileEntity(coord.getPos()) instanceof TileEntityAcceleratorInterface && hasMatter && isPowered && !launched)
				{
					DataUtils.sendMessage(new DataPackage(DataUtils.getDevices(pos, worldObj).get(coord), "launch"));
					worldObj.scheduleUpdate(pos, getBlockType(), 100);
					launched = true;
				}
			}
		}
	}

	@Override
	public String getDeviceType() {
		// TODO Auto-generated method stub
		return "accelerator_control_panel";
	}
}
