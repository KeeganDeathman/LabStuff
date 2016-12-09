package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.recipes.*;
import net.minecraft.nbt.NBTTagCompound;


public class TileEntityDSCCore extends DSCPart
{

	private boolean hasOS;
	private int ram;
	private int ramNeeded;
	private int drives;
	private int workbenches;
	private ArrayList<AcceleratorDiscovery> discovered;
	
	public TileEntityDSCCore()
	{
		hasOS = false;
		ram = 0;
		ramNeeded = 0;
		drives = 0;
		workbenches = 0;
		discovered = new ArrayList<AcceleratorDiscovery>();
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		tag.setBoolean("hasOS", hasOS);
		tag.setInteger("ram", ram);
		tag.setInteger("ramNeeded", ramNeeded);
		tag.setInteger("drives", drives);
		tag.setInteger("workbenches", workbenches);
		int[] discovs = new int[discovered.size()];
		for(int i = 0; i < discovered.size(); i++)
		{
			discovs[i] = discovered.get(i).getIndex();
		}
		tag.setIntArray("discovered", discovs);
		
		return tag;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		hasOS = tag.getBoolean("hasOS");
		ram = tag.getInteger("ram");
		ramNeeded = tag.getInteger("ramNeeded");
		drives = tag.getInteger("drives");
		workbenches = tag.getInteger("workbenches");
		int[] discovs = tag.getIntArray("discovered");
		for(int i : discovs)
		{
			discovered.add(Recipes.accelDiscoveries.get(i));
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		boolean foundOS = false;
		int foundRam = 0;
		int foundNeed = 0;
		int foundDrives = 0;
		int foundWorkbenches = 0;
		if(getNetwork() != null)
		{
			for(int i = 0; i < getNetwork().getDeviceCount(); i++)
			{
				if(getNetwork().getDeviceByIndex(i) instanceof DSCOS)
				{	
					foundOS = true;
					foundNeed += 8;
					getNetwork().sendMessage(new DSCPackage(getNetwork().getDeviceByIndex(i), this, "registered"));
				}
				if(getNetwork().getDeviceByIndex(i) instanceof DSCRam)
				{	
					foundRam += 5;
					getNetwork().sendMessage(new DSCPackage(getNetwork().getDeviceByIndex(i), this, "registered"));
				}
				if(getNetwork().getDeviceByIndex(i) instanceof DSCDrive)
				{	
					foundNeed += 3;
					foundDrives += 1;
					getNetwork().sendMessage(new DSCPackage(getNetwork().getDeviceByIndex(i), this, "registered"));
				}
				if(getNetwork().getDeviceByIndex(i) instanceof DSCBench)
				{	
					foundNeed += 10;
					foundWorkbenches += 1;
					getNetwork().sendMessage(new DSCPackage(getNetwork().getDeviceByIndex(i), this, "registered"));
				}
			}
		}
		hasOS = foundOS;
		ram = foundRam;
		ramNeeded = foundNeed;
		drives = foundDrives;
		workbenches = foundWorkbenches;
		
	}
	
	@Override
	public void performAction(String msg, DSCPart sender)
	{
		if(msg.startsWith("install-") && (ram-ramNeeded) > -1)
		{
			AcceleratorDiscovery dis = Recipes.accelDiscoveries.get(Integer.parseInt(msg.replace("install-", "")));
			if(!discovered.contains(dis))
			{
				if(discovered.contains(dis.getDependency()) || dis.getDependency() == null)
				{
					discovered.add(dis);
					getNetwork().sendMessage(new DSCPackage(sender, this, "installed"));
		
				}
			}
		}
	}

	public ArrayList<AcceleratorDiscovery> getDiscovered()
	{
		return discovered;
	}
	
}
