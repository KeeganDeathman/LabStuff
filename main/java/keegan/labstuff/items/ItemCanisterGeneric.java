package keegan.labstuff.items;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.*;

public abstract class ItemCanisterGeneric extends ItemFluidContainer
{
    private String allowedFluid = null;
    public final static int EMPTY = FluidContainerRegistry.BUCKET_VOLUME + 1;
    private static boolean isTELoaded = Loader.isModLoaded("ThermalExpansion");

    public ItemCanisterGeneric(String assetName)
    {
        super(0, FluidContainerRegistry.BUCKET_VOLUME);
        this.setMaxDamage(ItemCanisterGeneric.EMPTY);
        this.setMaxStackSize(1);
        this.setNoRepair();
        this.setRegistryName("labstuff",assetName);
        this.setHasSubtypes(true);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return LabStuffMain.tabLabStuffSpace;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 1));
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        //Workaround for strange behaviour in TE Transposer
        if (isTELoaded)
        {
            StackTraceElement[] st = Thread.currentThread().getStackTrace();
            int imax = Math.max(st.length, 5);
            for (int i = 1; i < imax; i++)
            {
                String ste = st[i].getClassName();
                if (ste.equals("thermalexpansion.block.machine.TileTransposer"))
                {
                    return null;
                }
            }
        }

        return new ItemStack(this.getContainerItem(), 1, ItemCanisterGeneric.EMPTY);
    }

    @Override
    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5)
    {
        if (ItemCanisterGeneric.EMPTY == par1ItemStack.getItemDamage())
        {
            par1ItemStack.setTagCompound(null);
        }
        else if (par1ItemStack.getItemDamage() <= 0)
        {
            par1ItemStack.setItemDamage(1);
        }
    }

    public void setAllowedFluid(String name)
    {
        this.allowedFluid = name;
    }

    public String getAllowedFluid()
    {
        return this.allowedFluid;
    }

    @Override
    public int fill(ItemStack container, FluidStack resource, boolean doFill)
    {
        if (resource == null || resource.getFluid() == null || resource.amount == 0 || container == null || container.getItemDamage() <= 1 || !(container.getItem() instanceof ItemCanisterGeneric))
        {
            return 0;
        }

        String fluidName = resource.getFluid().getName();
        if (container.getItemDamage() == ItemCanisterGeneric.EMPTY)
        {
            //Empty canister - find a new canister to match the fluid
            for (Item item : LabStuffMain.labstuffItems)
            {
            	String key = item.getRegistryName().getResourcePath();
                if (key.contains("CanisterFull"))
                {
                    if (item instanceof ItemCanisterGeneric && fluidName.equalsIgnoreCase(((ItemCanisterGeneric) item).allowedFluid))
                    {
                        if (!doFill)
                        {
                            return Math.min(resource.amount, this.capacity);
                        }

                        this.replaceEmptyCanisterItem(container, item);
                        break;
                    }
                }
            }
            //Delete any Forge fluid contents
            container.setTagCompound(null);
        }
        else
        {
            //Refresh the Forge fluid contents
            container.setTagCompound(null);
            super.fill(container, this.getFluid(container), true);
        }

        if (fluidName.equalsIgnoreCase(((ItemCanisterGeneric) container.getItem()).allowedFluid))
        {
            int added = super.fill(container, resource, doFill);
            if (doFill && added > 0)
            {
                container.setItemDamage(Math.max(1, container.getItemDamage() - added));
            }
            return added;
        }

        return 0;
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain)
    {
        if (this.allowedFluid == null || container.getItemDamage() >= ItemCanisterGeneric.EMPTY)
        {
            return null;
        }

        //Refresh the Forge fluid contents
        container.setTagCompound(null);
        super.fill(container, this.getFluid(container), true);

        FluidStack used = super.drain(container, maxDrain, doDrain);
        if (doDrain && used != null && used.amount > 0)
        {
            this.setNewDamage(container, container.getItemDamage() + used.amount);
        }
        return used;
    }

    protected void setNewDamage(ItemStack container, int newDamage)
    {
        newDamage = Math.min(newDamage, ItemCanisterGeneric.EMPTY);
        if (newDamage == ItemCanisterGeneric.EMPTY)
        {
            container.setTagCompound(null);
        }

        container.setItemDamage(newDamage);
    }

    private void replaceEmptyCanisterItem(ItemStack container, Item newItem)
    {
        //This is a neat trick to change the item ID in an ItemStack
        final int stackSize = container.stackSize;
        NBTTagCompound tag = new NBTTagCompound();
        tag.setShort("id", (short) Item.getIdFromItem(newItem));
        tag.setByte("Count", (byte) stackSize);
        tag.setShort("Damage", (short) ItemCanisterGeneric.EMPTY);
        container.readFromNBT(tag);
    }

    @Override
    public FluidStack getFluid(ItemStack container)
    {
        String fluidName = ((ItemCanisterGeneric) container.getItem()).allowedFluid;
        if (fluidName == null || ItemCanisterGeneric.EMPTY == container.getItemDamage())
        {
            return null;
        }

        Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid == null)
        {
            return null;
        }

        return new FluidStack(fluid, ItemCanisterGeneric.EMPTY - container.getItemDamage());
    }
}