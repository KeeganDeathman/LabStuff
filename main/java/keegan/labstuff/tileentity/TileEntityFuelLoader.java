package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.ditty.Annotations.NetworkedField;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.entities.IFuelable;
import keegan.labstuff.items.ItemCanisterGeneric;
import keegan.labstuff.network.IEnergyWrapper;
import keegan.labstuff.util.*;
import keegan.labstuff.util.FluidUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityFuelLoader extends TileEntityAdvanced implements ISidedInventory, ILandingPadAttachable, IEnergyWrapper
{
    private final int tankCapacity = 12000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank fuelTank = new FluidTank(this.tankCapacity);
    private ItemStack[] containingItems = new ItemStack[2];
    public IFuelable attachedFuelable;
    private boolean loadedFuelLastTick = false;
    private double buffer = 0;
    private boolean hasEnoughEnergyToRun;

    public TileEntityFuelLoader()
    {
    }

    public int getScaledFuelLevel(int i)
    {
        final double fuelLevel = this.fuelTank.getFluid() == null ? 0 : this.fuelTank.getFluid().amount;

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.worldObj.isRemote)
        {
        	hasEnoughEnergyToRun = buffer >= 60;
            this.loadedFuelLastTick = false;

            if (this.containingItems[1] != null)
            {
                if (this.containingItems[1].getItem() instanceof ItemCanisterGeneric)
                {
                    if (this.containingItems[1].getItem() == LabStuffMain.fuelCanister)
                    {
                        int originalDamage = this.containingItems[1].getItemDamage();
                        int used = this.fuelTank.fill(new FluidStack(LabStuffMain.kerosene, ItemCanisterGeneric.EMPTY - originalDamage), true);
                        this.containingItems[1] = new ItemStack(LabStuffMain.fuelCanister, 1, originalDamage + used);
                    }
                }
                else
                {
                    final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[1]);

                    if (liquid != null)
                    {
                        boolean isFuel = "kerosene".equals(FluidRegistry.getFluidName(liquid));

                        if (isFuel)
                        {
                            if (this.fuelTank.getFluid() == null || this.fuelTank.getFluid().amount + liquid.amount <= this.fuelTank.getCapacity())
                            {
                                this.fuelTank.fill(new FluidStack(LabStuffMain.kerosene, liquid.amount), true);

                                if (FluidContainerRegistry.isBucket(this.containingItems[1]) && FluidContainerRegistry.isFilledContainer(this.containingItems[1]))
                                {
                                    final int amount = this.containingItems[1].stackSize;
                                    if (amount > 1)
                                    {
                                        this.fuelTank.fill(new FluidStack(LabStuffMain.kerosene, (amount - 1) * FluidContainerRegistry.BUCKET_VOLUME), true);
                                    }
                                    this.containingItems[1] = new ItemStack(Items.BUCKET, amount);
                                }
                                else
                                {
                                    this.containingItems[1].stackSize--;

                                    if (this.containingItems[1].stackSize == 0)
                                    {
                                        this.containingItems[1] = null;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (this.ticks % 100 == 0)
            {
                this.attachedFuelable = null;

                for (final EnumFacing dir : EnumFacing.values())
                {
                    final TileEntity pad = new BlockVec3(this).getTileEntityOnSide(this.worldObj, dir);

                    if (pad instanceof TileEntityMulti)
                    {
                        final TileEntity mainTile = ((TileEntityMulti) pad).getMainBlockTile();

                        if (mainTile instanceof IFuelable)
                        {
                            this.attachedFuelable = (IFuelable) mainTile;
                            break;
                        }
                    }
                    else if (pad instanceof IFuelable)
                    {
                        this.attachedFuelable = (IFuelable) pad;
                        break;
                    }
                }

            }

            if (this.fuelTank != null && this.fuelTank.getFluid() != null && this.fuelTank.getFluid().amount > 0)
            {
                final FluidStack liquid = new FluidStack(LabStuffMain.kerosene, 2);

                if (this.attachedFuelable != null && this.hasEnoughEnergyToRun)
                {
                    int filled = this.attachedFuelable.addFuel(liquid, true);
                    this.loadedFuelLastTick = filled > 0;
                    this.fuelTank.drain(filled, true);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        this.containingItems = this.readStandardItemsFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(par1NBTTagCompound.getCompoundTag("fuelTank"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt);

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }
        return nbt;
    }
    
    public ItemStack[] readStandardItemsFromNBT(NBTTagCompound nbt)
    {
        final NBTTagList var2 = nbt.getTagList("Items", 10);
        int length = this.getSizeInventory();
        ItemStack[] result = new ItemStack[length];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < length)
            {
                result[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
        return result;
    }

    public void writeStandardItemsToNBT(NBTTagCompound nbt)
    {
        final NBTTagList list = new NBTTagList();
        int length = this.getSizeInventory();
        ItemStack containingItems[] = this.containingItems;

        for (int var3 = 0; var3 < length; ++var3)
        {
            if (containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        nbt.setTag("Items", list);
    }

    @Override
    public String getName()
    {
        return LabStuffUtils.translate("container.fuelloader.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 1 && itemstack != null)
        {
            return FluidUtil.isEmptyContainer(itemstack);
        }
        return false;
    }

//    @Override
//    public boolean hasCustomName()
//    {
//        return true;
//    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
    	return true;
    }

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return this.containingItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return this.containingItems[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
				if (stack.stackSize <= amt)
				{
					setInventorySlotContents(slot, null);
				}
				else
				{
					stack = stack.splitStack(amt);
					if (stack.stackSize == 0)
					{
						setInventorySlotContents(slot, null);
					}
				}
		}
		return stack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.containingItems, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.containingItems[index] = stack;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getEnergy() {
		return buffer;
	}

	@Override
	public void setEnergy(double energy) {
		buffer = energy;
	}

	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 100000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		return buffer+=amount;
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canOutputTo(EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return EnumSet.noneOf(EnumFacing.class);
	}

	@Override
	public EnumSet<EnumFacing> getConsumingSides() {
		// TODO Auto-generated method stub
		return EnumSet.allOf(EnumFacing.class);
	}

	@Override
	public double getMaxOutput() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double removeEnergyFromProvider(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public double getPacketRange() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPacketCooldown() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isNetworkedTile() {
		// TODO Auto-generated method stub
		return false;
	}
    
}