package keegan.labstuff.tileentity;

import java.util.*;

import keegan.labstuff.recipes.*;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityAcceleratorControlPanel extends DataConnectedDevice
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
		if (getNetwork() != null)
		{
			isRunning = launched && isPowered;
			for (int i = 0; i < getNetwork().getDeviceCount(); i++)
			{
				if (getNetwork().getDeviceByIndex(i) instanceof TileEntityAcceleratorInterface)
				{
					if (isRunning)
					{
						launched = false;
						Random r = new Random();
						int R = r.nextInt(3);
						if (R == 2)
						{
							System.out.println("Cheese~!");
							for(int j = 0; j < Recipes.accelDiscoveries.size(); j++)
							{
								System.out.println("Cheese~~!");
								AcceleratorDiscovery discov = Recipes.accelDiscoveries.get(j);
								if(!discovered.contains(discov))
								{
									System.out.println("Cheese~~~!");
									if(discov.getDependency() == null)
										getNetwork().sendMessage(new DataPackage(getNetwork().getDeviceByIndex(i), "discovery_" + j));
									else
									{
										if(discovered.contains(discov.getDependency()))
											getNetwork().sendMessage(new DataPackage(getNetwork().getDeviceByIndex(i), "discovery_" + j));
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void update()
	{
		super.update();
	}

	public void launch()
	{
		if (getNetwork() != null)
		{
			for (int i = 0; i < getNetwork().getDeviceCount(); i++)
			{
				if (getNetwork().getDeviceByIndex(i) instanceof TileEntityAcceleratorInterface && hasMatter && isPowered && !launched)
				{
					getNetwork().sendMessage(new DataPackage(getNetwork().getDeviceByIndex(i), "launch"));
					worldObj.scheduleUpdate(pos, getBlockType(), 100);
					launched = true;
				}
			}
		}
	}
}
