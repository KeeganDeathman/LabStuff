package keegan.labstuff.common.capabilities;

import keegan.labstuff.common.capabilities.DefaultStorageHelper.DefaultStorage;
import keegan.labstuff.network.IStrictEnergyStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Created by ben on 30/04/16.
 */
public class DefaultStrictEnergyStorage implements IStrictEnergyStorage, INBTSerializable<NBTTagCompound>
{
    private double energyStored = 0;
    private double maxEnergy;

    public DefaultStrictEnergyStorage()
    {
        this(0);
    }

    public DefaultStrictEnergyStorage(double capacity)
    {
        maxEnergy = capacity;
    }

    @Override
    public double getEnergy()
    {
        return energyStored;
    }

    @Override
    public void setEnergy(double energy)
    {
        energyStored = energy;
    }

    @Override
    public double getMaxEnergy()
    {
        return maxEnergy;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setDouble("maxEnergy", getMaxEnergy());
        tag.setDouble("energyStored", getEnergy());
        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        setEnergy(nbt.getDouble("energyStored"));
        maxEnergy = nbt.getDouble("maxEnergy");
    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IStrictEnergyStorage.class, new DefaultStorage<>(), DefaultStrictEnergyStorage.class);
    }
}