package keegan.labstuff.dimension;

import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.*;

public class OrbitSpinSaveData extends WorldSavedData
{
    public static final String saveDataID = "GCSpinData";
    public NBTTagCompound datacompound;
    private NBTTagCompound alldata;
    private int dim = 0;

    public OrbitSpinSaveData(String s)
    {
        super(OrbitSpinSaveData.saveDataID);
        this.datacompound = new NBTTagCompound();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        this.alldata = nbt;
        //world.loadItemData calls this but can't extract from alldata until we know the dimension ID
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (this.dim != 0)
        {
            nbt.setTag("" + this.dim, this.datacompound);
        }

        return nbt;
    }

    public static OrbitSpinSaveData initWorldData(World world)
    {
        OrbitSpinSaveData worldData = (OrbitSpinSaveData) world.loadItemData(OrbitSpinSaveData.class, OrbitSpinSaveData.saveDataID);

        if (worldData == null)
        {
            worldData = new OrbitSpinSaveData("");
            world.setItemData(OrbitSpinSaveData.saveDataID, worldData);
            if (world.provider instanceof WorldProviderSpaceStation)
            {
                worldData.dim = LabStuffUtils.getDimensionID(world);
                ((WorldProviderSpaceStation) world.provider).getSpinManager().writeToNBT(worldData.datacompound);
            }
            worldData.markDirty();
        }
        else if (world.provider instanceof WorldProviderSpaceStation)
        {
            worldData.dim = LabStuffUtils.getDimensionID(world);

            worldData.datacompound = null;
            if (worldData.alldata != null)
            {
                worldData.datacompound = worldData.alldata.getCompoundTag("" + worldData.dim);
            }
            if (worldData.datacompound == null)
            {
                worldData.datacompound = new NBTTagCompound();
            }
        }

        return worldData;
    }
}