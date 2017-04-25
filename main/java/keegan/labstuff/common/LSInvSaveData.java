package keegan.labstuff.common;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.world.WorldSavedData;

public class LSInvSaveData extends WorldSavedData
{
    public static final String SAVE_ID = "LSInv_savefile";

    public LSInvSaveData()
    {
        super(SAVE_ID);
    }

    public LSInvSaveData(String name)
    {
        super(name);
    }

    @Override
    public void readFromNBT(NBTTagCompound filedata)
    {
        for (Object obj : filedata.getKeySet())
        {
            if (obj instanceof NBTTagList)
            {
                NBTTagList entry = (NBTTagList) obj;
                String name = entry.toString(); // TODO See if this is equivilent to 1.6's getName function
                ItemStack[] saveinv = new ItemStack[6];
                if (entry.tagCount() > 0)
                {
                    for (int j = 0; j < entry.tagCount(); j++)
                    {
                        NBTTagCompound obj1 = entry.getCompoundTagAt(j);

                        if (obj1 != null)
                        {
                            int i = obj1.getByte("Slot") & 7;
                            if (i >= 6)
                            {
                                System.out.println("LSInv error retrieving savefile: slot was outside range 0-5");
                                return;
                            }
                            saveinv[i] = ItemStack.loadItemStackFromNBT(obj1);
                        }
                    }
                }
                CommandLSInv.savedata.put(name.toLowerCase(), saveinv);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound toSave)
    {
        for (String name : CommandLSInv.savedata.keySet())
        {
            NBTTagList par1NBTTagList = new NBTTagList();
            ItemStack[] saveinv = CommandLSInv.savedata.get(name);

            for (int i = 0; i < 6; i++)
            {
                if (saveinv[i] != null)
                {
                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setByte("Slot", (byte) (i + 200));
                    saveinv[i].writeToNBT(nbttagcompound);
                    par1NBTTagList.appendTag(nbttagcompound);
                }
            }
            toSave.setTag(name, par1NBTTagList);
        }

        return toSave;
    }
}