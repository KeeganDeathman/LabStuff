package keegan.labstuff.tileentity;

import java.util.*;

import keegan.ditty.Annotations.NetworkedField;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.OxygenPressureProtocol;
import keegan.labstuff.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityOxygenSealer extends TileEntityOxygen implements IInventory, ISidedInventory
{
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean sealed;
    public boolean lastSealed = false;

    public boolean lastDisabled = false;

    @NetworkedField(targetSide = Side.CLIENT)
    public boolean active;
    private ItemStack[] containingItems = new ItemStack[3];
    public ThreadFindSeal threadSeal;
    @NetworkedField(targetSide = Side.CLIENT)
    public int stopSealThreadCooldown;
    @NetworkedField(targetSide = Side.CLIENT)
    public int threadCooldownTotal;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean calculatingSealed;
    public static int countEntities = 0;
    private static int countTemp = 0;
    private static boolean sealerCheckedThisTick = false;
    public static ArrayList<TileEntityOxygenSealer> loadedTiles = new ArrayList();
    private static final int UNSEALED_OXYGENPERTICK = 12;
    private boolean hasEnoughEnergyToRun;


    public TileEntityOxygenSealer()
    {
        super(10000, UNSEALED_OXYGENPERTICK);
        this.noRedstoneControl = true;
    }

    @Override
    public void validate()
    {
        super.validate();
        if (!this.worldObj.isRemote)
        {
            if (!TileEntityOxygenSealer.loadedTiles.contains(this))
            {
                TileEntityOxygenSealer.loadedTiles.add(this);
            }
            this.stopSealThreadCooldown = 126 + countEntities;
        }
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote)
        {
            TileEntityOxygenSealer.loadedTiles.remove(this);
        }
        super.invalidate();
    }

    @Override
    public void onChunkUnload()
    {
        if (!this.worldObj.isRemote)
        {
            TileEntityOxygenSealer.loadedTiles.remove(this);
        }
        super.onChunkUnload();
    }

    public int getScaledThreadCooldown(int i)
    {
        if (this.active)
        {
            return Math.min(i, (int) Math.floor(this.stopSealThreadCooldown * i / (double) this.threadCooldownTotal));
        }
        return 0;
    }

    public int getFindSealChecks()
    {
        if (!this.active || tank.getFluidAmount() < this.oxygenPerTick || !(hasEnoughEnergyToRun))
        {
            return 0;
        }
        BlockPos posAbove = new BlockPos(this.getPos().getX(), this.getPos().getY() + 1, this.getPos().getZ());
        IBlockState stateAbove = this.worldObj.getBlockState(posAbove);
        if (!(stateAbove.getBlock().isAir(stateAbove, this.worldObj, posAbove)) && !OxygenPressureProtocol.canBlockPassAir(this.worldObj, stateAbove.getBlock(), this.getPos().up(), EnumFacing.UP))
        {
            // The vent is blocked
            return 0;
        }

        return 1250;
    }

    public boolean thermalControlEnabled()
    {
        ItemStack oxygenItemStack = this.getStackInSlot(2);
        return oxygenItemStack != null && oxygenItemStack.getItem() == LabStuffMain.thermalControl && this.hasEnoughEnergyToRun;
    }

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote)
        {
            ItemStack oxygenItemStack = this.getStackInSlot(1);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IFluidHandler && ((IFluidHandler)oxygenItemStack.getItem()).getTankProperties()[0].canDrainFluidType(new FluidStack(LabStuffMain.LO2, 5)))
            {
            	IFluidHandler oxygenItem = (IFluidHandler) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(UNSEALED_OXYGENPERTICK * 2.5F, this.tank.getCapacity() - this.tank.getFluidAmount()));
                tank.fill(oxygenItem.drain(oxygenDraw, true), true);
            }
            int power = 10;

            if (this.thermalControlEnabled())
            {
            	power = 40;
            }
            else
            {
            	power = 10;
            }
        }

        this.oxygenPerTick = this.sealed ? 2 : UNSEALED_OXYGENPERTICK;
        super.update();

        if (!this.worldObj.isRemote)
        {
            // Some code to count the number of Oxygen Sealers being updated,
            // tick by tick - needed for queueing
            TileEntityOxygenSealer.countTemp++;

            this.active = tank.getFluidAmount() >= 1 && this.hasEnoughEnergyToRun;

            //TODO: if multithreaded, this codeblock should not run if the current threadSeal is flagged looping
            if (this.stopSealThreadCooldown > 0)
            {
                this.stopSealThreadCooldown--;
            }
            else if (!TileEntityOxygenSealer.sealerCheckedThisTick)
            {
                // This puts any Sealer which is updated to the back of the queue for updates
                this.threadCooldownTotal = this.stopSealThreadCooldown = 75 + TileEntityOxygenSealer.countEntities;
                if (this.active || this.sealed)
                {
                TileEntityOxygenSealer.sealerCheckedThisTick = true;
                OxygenPressureProtocol.updateSealerStatus(this);
            }
            }

            //TODO: if multithreaded, this.threadSeal needs to be atomic
            if (this.threadSeal != null)
            {
            	if (this.threadSeal.looping.get())
            	{
            		this.calculatingSealed = this.active;
            	}
            	else
            	{
            		this.calculatingSealed = false;
                this.sealed = this.active && this.threadSeal.sealedFinal.get();
            	}
            }

            this.lastSealed = this.sealed;
        }
    }
    
    public static void onServerTick()
    {
        TileEntityOxygenSealer.countEntities = TileEntityOxygenSealer.countTemp;
        TileEntityOxygenSealer.countTemp = 0;
        TileEntityOxygenSealer.sealerCheckedThisTick = false;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        final NBTTagList var2 = nbt.getTagList("Items", 10);
        this.containingItems = new ItemStack[this.getSizeInventory()];

        for (int var3 = 0; var3 < var2.tagCount(); ++var3)
        {
            final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
            final int var5 = var4.getByte("Slot") & 255;

            if (var5 < this.containingItems.length)
            {
                this.containingItems[var5] = ItemStack.loadItemStackFromNBT(var4);
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);

        final NBTTagList list = new NBTTagList();

        for (int var3 = 0; var3 < this.containingItems.length; ++var3)
        {
            if (this.containingItems[var3] != null)
            {
                final NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.containingItems[var3].writeToNBT(var4);
                list.appendTag(var4);
            }
        }

        nbt.setTag("Items", list);
        return nbt;
    }

    @Override
    public int getSizeInventory()
    {
        return this.containingItems.length;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        return this.containingItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.containingItems[par1] != null)
        {
            ItemStack var3;

            if (this.containingItems[par1].stackSize <= par2)
            {
                var3 = this.containingItems[par1];
                this.containingItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.containingItems[par1].splitStack(par2);

                if (this.containingItems[par1].stackSize == 0)
                {
                    this.containingItems[par1] = null;
                }

                return var3;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (this.containingItems[par1] != null)
        {
            final ItemStack var2 = this.containingItems[par1];
            this.containingItems[par1] = null;
            return var2;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        this.containingItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getName()
    {
        return LabStuffUtils.translate("container.oxygensealer.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return this.worldObj.getTileEntity(this.getPos()) == this && par1EntityPlayer.getDistanceSq(this.getPos().getX() + 0.5D, this.getPos().getY() + 0.5D, this.getPos().getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
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
    	return true;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
    	return true;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
    	return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    public static HashMap<BlockVec3, TileEntityOxygenSealer> getSealersAround(World world, BlockPos pos, int rSquared)
    {
        HashMap<BlockVec3, TileEntityOxygenSealer> ret = new HashMap<BlockVec3, TileEntityOxygenSealer>();

        for (TileEntityOxygenSealer tile : new ArrayList<TileEntityOxygenSealer>(TileEntityOxygenSealer.loadedTiles))
        {
            if (tile != null && tile.getWorld() == world && tile.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < rSquared)
            {
                ret.put(new BlockVec3(tile.getPos()), tile);
            }
        }

        return ret;
    }

    public static TileEntityOxygenSealer getNearestSealer(World world, double x, double y, double z)
    {
        TileEntityOxygenSealer ret = null;
        double dist = 96 * 96D;

        for (Object tile : world.loadedTileEntityList)
        {
            if (tile instanceof TileEntityOxygenSealer)
            {
                double testDist = ((TileEntityOxygenSealer) tile).getDistanceSq(x, y, z);
                if (testDist < dist)
                {
                    dist = testDist;
                    ret = (TileEntityOxygenSealer) tile;
                }
            }
        }

        return ret;
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