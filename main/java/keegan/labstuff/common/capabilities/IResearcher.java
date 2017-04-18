package keegan.labstuff.common.capabilities;

import java.util.ArrayList;

import keegan.labstuff.recipes.*;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public interface IResearcher {
		boolean hasResearch(Research r);
		ArrayList<Research> getResearched();
		void research(Research r);
	
	public static class Storage implements IStorage<IResearcher>
	{

		@Override
		public NBTBase writeNBT(Capability<IResearcher> capability, IResearcher instance, EnumFacing side) {
			NBTTagIntArray list = new NBTTagIntArray(new int[instance.getResearched().size()]);
			for(int i = 0; i < instance.getResearched().size(); i++)
			{
				list.getIntArray()[i] = Recipes.getResearches().indexOf(instance.getResearched().get(i));
			}
			return list;
		}

		@Override
		public void readNBT(Capability<IResearcher> capability, IResearcher instance, EnumFacing side, NBTBase nbt) {
			if(nbt instanceof NBTTagIntArray)
			{
				NBTTagIntArray list = (NBTTagIntArray) nbt;
				for(int i = 0; i < list.getIntArray().length; i++)
				{
					instance.research(Recipes.getResearchFromIndex(list.getIntArray()[i]));
				}
			}
			
		}
	}
}