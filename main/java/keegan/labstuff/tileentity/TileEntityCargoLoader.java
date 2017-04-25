package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.ditty.Annotations.NetworkedField;
import keegan.labstuff.entities.ICargoEntity;
import keegan.labstuff.entities.ICargoEntity.*;
import keegan.labstuff.network.IEnergyWrapper;
import keegan.labstuff.util.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityCargoLoader extends TileEntityAdvanced implements IEnergyWrapper, ILandingPadAttachable
{
    private ItemStack[] containingItems = new ItemStack[15];
    public boolean outOfItems;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetFull;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean targetNoInventory;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean noTarget;
    private boolean hasEnoughEnergyToRun;
    private double buffer = 0;

    public ICargoEntity attachedFuelable;

    public TileEntityCargoLoader()
    {
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.worldObj.isRemote)
        {
        	hasEnoughEnergyToRun = buffer >= 90;
            if (this.ticks % 100 == 0)
            {
                this.checkForCargoEntity();
            }

            if (this.attachedFuelable != null)
            {
                this.noTarget = false;
                ItemStack stack = this.removeCargo(false).resultStack;

                if (stack != null)
                {
                    this.outOfItems = false;

                    EnumCargoLoadingState state = this.attachedFuelable.addCargo(stack, false);

                    this.targetFull = state == EnumCargoLoadingState.FULL;
                    this.targetNoInventory = state == EnumCargoLoadingState.NOINVENTORY;
                    this.noTarget = state == EnumCargoLoadingState.NOTARGET;

                    if (this.ticks % 15 == 0 && state == EnumCargoLoadingState.SUCCESS && this.hasEnoughEnergyToRun)
                    {
                        this.attachedFuelable.addCargo(this.removeCargo(true).resultStack, true);
                    }
                }
                else
                {
                    this.outOfItems = true;
                }
            }
            else
            {
                this.noTarget = true;
            }
        }
    }

    public void checkForCargoEntity()
    {
        boolean foundFuelable = false;

        for (final EnumFacing dir : EnumFacing.VALUES)
        {
            final TileEntity pad = new BlockVec3(this).getTileEntityOnSide(this.worldObj, dir);

            if (pad != null && pad instanceof TileEntityMulti)
            {
                final TileEntity mainTile = ((TileEntityMulti) pad).getMainBlockTile();

                if (mainTile instanceof ICargoEntity)
                {
                    this.attachedFuelable = (ICargoEntity) mainTile;
                    foundFuelable = true;
                    break;
                }
            }
            else if (pad != null && pad instanceof ICargoEntity)
            {
                this.attachedFuelable = (ICargoEntity) pad;
                foundFuelable = true;
                break;
            }
        }

        if (!foundFuelable)
        {
            this.attachedFuelable = null;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.containingItems = this.readStandardItemsFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt);
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
        return LabStuffUtils.translate("container.cargoloader.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return (this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]));
    }

    // ISidedInventory Implementation:


    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
    	return true;
    }


    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        int count = 1;

        for (count = 1; count < this.containingItems.length; count++)
        {
            ItemStack stackAt = this.containingItems[count];

            if (stackAt != null && stackAt.getItem() == stack.getItem() && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (stackAt.stackSize + stack.stackSize <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        this.containingItems[count].stackSize += stack.stackSize;
                        this.markDirty();
                    }

                    return EnumCargoLoadingState.SUCCESS;
                }
                else
                {
                    //Part of the stack can fill this slot but there will be some left over
                    int origSize = stackAt.stackSize;
                    int surplus = origSize + stack.stackSize - stackAt.getMaxStackSize();

                    if (doAdd)
                    {
                        this.containingItems[count].stackSize = stackAt.getMaxStackSize();
                        this.markDirty();
                    }

                    stack.stackSize = surplus;
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    this.containingItems[count].stackSize = origSize;
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 1; count < this.containingItems.length; count++)
        {
            ItemStack stackAt = this.containingItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.containingItems[count] = stack;
                    this.markDirty();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        return EnumCargoLoadingState.FULL;
    }

    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 1; i < this.containingItems.length; i++)
        {
            ItemStack stackAt = this.containingItems[i];

            if (stackAt != null)
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.stackSize = 1;

                if (doRemove && --stackAt.stackSize <= 0)
                {
                    this.containingItems[i] = null;
                }

                if (doRemove)
                {
                    this.markDirty();
                }

                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }

    @Override
    public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos)
    {
        return true;
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
		return 100000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		return buffer+=amount;
	}

	@Override
	public boolean canReceiveEnergy(EnumFacing side) {
		return true;
	}

	@Override
	public boolean canOutputTo(EnumFacing side) {
		return false;
	}

	@Override
	public int getSizeInventory() {
		return this.containingItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.containingItems[index];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) {
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
		return ItemStackHelper.getAndRemove(containingItems, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.containingItems[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
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
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return EnumSet.noneOf(EnumFacing.class);
	}

	@Override
	public EnumSet<EnumFacing> getConsumingSides() {
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