package keegan.labstuff.tileentity;

import java.util.*;

import keegan.labstuff.blocks.*;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.container.ContainerTerraformer;
import keegan.labstuff.entity.IBubbleProvider;
import keegan.labstuff.network.*;
import keegan.labstuff.util.Annotations.NetworkedField;
import keegan.labstuff.util.FluidUtil;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.block.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.*;

public class TileEntityTerraformer extends TileEntity implements ISidedInventory, IDisableableMachine, IBubbleProvider, IFluidHandlerWrapper, IEnergyWrapper, ITickable
{
    private final int tankCapacity = 2000;
    @NetworkedField(targetSide = Side.CLIENT)
    public FluidTank waterTank = new FluidTank(this.tankCapacity);
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean disabled = false;
    @NetworkedField(targetSide = Side.CLIENT)
    public int disableCooldown = 0;
    public boolean active;
    public boolean lastActive;
    private ItemStack[] containingItems = new ItemStack[14];
    private ArrayList<BlockPos> terraformableBlocksList = new ArrayList<BlockPos>();
    private ArrayList<BlockPos> grassBlockList = new ArrayList<BlockPos>();
    private ArrayList<BlockPos> grownTreesList = new ArrayList<BlockPos>();
    @NetworkedField(targetSide = Side.CLIENT)
    public int terraformableBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = Side.CLIENT)
    public int grassBlocksListSize = 0; // used for server->client ease
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean treesDisabled;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean grassDisabled;
    public final double MAX_SIZE = 15.0D;
    private int[] useCount = new int[2];
    private int saplingIndex = 6;
    public float bubbleSize;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRenderBubble = true;
    private boolean hasEnoughEnergyToRun = false;
    private double buffer = 0;
    private int ticks = 0;

    public TileEntityTerraformer()
    {
    }

    public int getScaledWaterLevel(int i)
    {
        final double fuelLevel = this.waterTank.getFluid() == null ? 0 : this.waterTank.getFluid().amount;

        return (int) (fuelLevel * i / this.tankCapacity);
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
    }

    public double getDistanceFromServer(double par1, double par3, double par5)
    {
        final double d3 = this.getPos().getX() + 0.5D - par1;
        final double d4 = this.getPos().getY() + 0.5D - par3;
        final double d5 = this.getPos().getZ() + 0.5D - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    @Override
    public void update()
    {
    	ticks++;
    	this.hasEnoughEnergyToRun = this.buffer >= 45;
//        if (this.terraformBubble == null)
//        {
//            if (!this.worldObj.isRemote)
//            {
//                this.terraformBubble = new EntityTerraformBubble(this.worldObj, new Vector3(this), this);
//                this.worldObj.spawnEntityInWorld(this.terraformBubble);
//            }
//        }

        if (!this.worldObj.isRemote)
        {
            if (this.containingItems[0] != null)
            {
                final FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(this.containingItems[0]);

                if (liquid != null && liquid.getFluid().getName().equals(FluidRegistry.WATER.getName()))
                {
                    if (this.waterTank.getFluid() == null || this.waterTank.getFluid().amount + liquid.amount <= this.waterTank.getCapacity())
                    {
                        this.waterTank.fill(liquid, true);

                        this.containingItems[0] = FluidUtil.getUsedContainer(this.containingItems[0]);
                    }
                }
            }

            this.active = this.bubbleSize == this.MAX_SIZE && this.hasEnoughEnergyToRun && this.getFirstBonemealStack() != null && this.waterTank.getFluid() != null && this.waterTank.getFluid().amount > 0;
        }

        if (!this.worldObj.isRemote && (this.active != this.lastActive || this.ticks % 60 == 0))
        {
            this.terraformableBlocksList.clear();
            this.grassBlockList.clear();

            if (this.active)
            {
                int bubbleSize = (int) Math.ceil(this.bubbleSize);
                double bubbleSizeSq = this.bubbleSize;
                bubbleSizeSq *= bubbleSizeSq;
                boolean doGrass = !this.grassDisabled && this.getFirstSeedStack() != null;
                boolean doTrees = !this.treesDisabled && this.getFirstSaplingStack() != null;
                for (int x = this.getPos().getX() - bubbleSize; x < this.getPos().getX() + bubbleSize; x++)
                {
                    for (int y = this.getPos().getY() - bubbleSize; y < this.getPos().getY() + bubbleSize; y++)
                    {
                        for (int z = this.getPos().getZ() - bubbleSize; z < this.getPos().getZ() + bubbleSize; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            Block blockID = this.worldObj.getBlockState(pos).getBlock();
                            if (blockID == null)
                            {
                                continue;
                            }

                            if (!(blockID.isAir(this.worldObj.getBlockState(pos), this.worldObj, pos)) && this.getDistanceFromServer(x, y, z) < bubbleSizeSq)
                            {
                                if (doGrass && blockID instanceof ITerraformableBlock && ((ITerraformableBlock) blockID).isTerraformable(this.worldObj, pos))
                                {
                                    this.terraformableBlocksList.add(new BlockPos(x, y, z));
                                }
                                else if (doTrees)
                                {
                                    Block blockIDAbove = this.worldObj.getBlockState(pos.up()).getBlock();
                                    if (blockID == Blocks.GRASS && blockIDAbove.isAir(this.worldObj.getBlockState(pos.up()), this.worldObj, pos.up()))
                                    {
                                        this.grassBlockList.add(new BlockPos(x, y, z));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!this.worldObj.isRemote && this.terraformableBlocksList.size() > 0 && this.ticks % 15 == 0)
        {
            ArrayList<BlockPos> terraformableBlocks2 = new ArrayList<BlockPos>(this.terraformableBlocksList);

            int randomIndex = this.worldObj.rand.nextInt(this.terraformableBlocksList.size());
            BlockPos vec = terraformableBlocks2.get(randomIndex);

            if (this.worldObj.getBlockState(vec).getBlock() instanceof ITerraformableBlock)
            {
                Block id;

                switch (this.worldObj.rand.nextInt(40))
                {
                case 0:
                    if (this.worldObj.isBlockFullCube(new BlockPos(vec.getX() - 1, vec.getY(), vec.getZ())) && this.worldObj.isBlockFullCube(new BlockPos(vec.getX() + 1, vec.getY(), vec.getZ())) && this.worldObj.isBlockFullCube(new BlockPos(vec.getX(), vec.getY(), vec.getZ() - 1)) && this.worldObj.isBlockFullCube(new BlockPos(vec.getX(), vec.getY(), vec.getZ() + 1)))
                    {
                        id = Blocks.FLOWING_WATER;
                    }
                    else
                    {
                        id = Blocks.GRASS;
                    }
                    break;
                default:
                    id = Blocks.GRASS;
                    break;
                }

                this.worldObj.setBlockState(vec, id.getDefaultState());

                if (id == Blocks.GRASS)
                {
                    this.useCount[0]++;
                    this.waterTank.drain(1, true);
                    this.checkUsage(1);
                }
                else if (id == Blocks.FLOWING_WATER)
                {
                    this.checkUsage(2);
                }
            }

            this.terraformableBlocksList.remove(randomIndex);
        }

        if (!this.worldObj.isRemote && !this.treesDisabled && this.grassBlockList.size() > 0 && this.ticks % 50 == 0)
        {
            int randomIndex = this.worldObj.rand.nextInt(this.grassBlockList.size());
            BlockPos vecGrass = grassBlockList.get(randomIndex);

            if (this.worldObj.getBlockState(vecGrass).getBlock() == Blocks.GRASS)
            {
                BlockPos vecSapling = vecGrass.add(0, 1, 0);
                ItemStack sapling = this.getFirstSaplingStack();
                boolean flag = false;

                //Attempt to prevent placement too close to other trees
                for (BlockPos testVec : this.grownTreesList)
                {
                    if (testVec.distanceSq(vecSapling) < 9)
                    {
                        flag = true;
                        break;
                    }
                }

                if (!flag && sapling != null)
                {
                    Block b = Block.getBlockFromItem(sapling.getItem());
                    this.worldObj.setBlockState(vecSapling, b.getStateFromMeta(sapling.getItemDamage()), 3);
                    if (b instanceof BlockSapling)
                    {
                        if (this.worldObj.getLightFromNeighbors(vecSapling) >= 9)
                        {
                            ((BlockSapling) b).grow(this.worldObj, vecSapling, this.worldObj.getBlockState(vecSapling), this.worldObj.rand);
                            this.grownTreesList.add(new BlockPos(vecSapling.getX(), vecSapling.getY(), vecSapling.getZ()));
                        }
                    }
                    else if (b instanceof BlockBush)
                    {
                        if (this.worldObj.getLightFromNeighbors(vecSapling) >= 5)
                        //Hammer the update tick a few times to try to get it to grow - it won't always
                        {
                            for (int j = 0; j < 12; j++)
                            {
                                if (this.worldObj.getBlockState(vecSapling).getBlock() == b)
                                {
                                    ((BlockBush) b).updateTick(this.worldObj, vecSapling, this.worldObj.getBlockState(vecSapling), this.worldObj.rand);
                                }
                                else
                                {
                                    this.grownTreesList.add(new BlockPos(vecSapling.getX(), vecSapling.getY(), vecSapling.getZ()));
                                    break;
                                }
                            }
                        }
                    }

                    this.useCount[1]++;
                    this.waterTank.drain(50, true);
                    this.checkUsage(0);
                }
            }

            this.grassBlockList.remove(randomIndex);
        }

        if (!this.worldObj.isRemote)
        {
            this.terraformableBlocksListSize = this.terraformableBlocksList.size();
            this.grassBlocksListSize = this.grassBlockList.size();
        }

        if (this.hasEnoughEnergyToRun && (!this.grassDisabled || !this.treesDisabled))
        {
            this.bubbleSize = (float) Math.min(Math.max(0, this.bubbleSize + 0.1F), this.MAX_SIZE);
        }
        else
        {
            this.bubbleSize = (float) Math.min(Math.max(0, this.bubbleSize - 0.1F), this.MAX_SIZE);
        }

        this.lastActive = this.active;
    }

    private void checkUsage(int type)
    {
        ItemStack stack = null;

        if ((this.useCount[0] + this.useCount[1]) % 4 == 0)
        {
            stack = this.getFirstBonemealStack();

            if (stack != null)
            {
                stack.stackSize--;

                if (stack.stackSize <= 0)
                {
                    this.containingItems[this.getSelectiveStack(2, 6)] = null;
                }
            }
        }

        switch (type)
        {
        case 0:
            stack = this.containingItems[this.saplingIndex];

            if (stack != null)
            {
                stack.stackSize--;

                if (stack.stackSize <= 0)
                {
                    this.containingItems[this.saplingIndex] = null;
                }
            }
            break;
        case 1:
            if (this.useCount[0] % 4 == 0)
            {
                stack = this.getFirstSeedStack();

                if (stack != null)
                {
                    stack.stackSize--;

                    if (stack.stackSize <= 0)
                    {
                        this.containingItems[this.getSelectiveStack(10, 14)] = null;
                    }
                }
            }
            break;
        case 2:
            this.waterTank.drain(50, true);
            break;
        }
    }

    private int getSelectiveStack(int start, int end)
    {
        for (int i = start; i < end; i++)
        {
            ItemStack stack = this.containingItems[i];

            if (stack != null)
            {
                return i;
            }
        }

        return -1;
    }

    private int getRandomStack(int start, int end)
    {
        int stackcount = 0;
        for (int i = start; i < end; i++)
        {
            if (this.containingItems[i] != null)
            {
                stackcount++;
            }
        }

        if (stackcount == 0)
        {
            return -1;
        }

        int random = this.worldObj.rand.nextInt(stackcount);
        for (int i = start; i < end; i++)
        {
            if (this.containingItems[i] != null)
            {
                if (random == 0)
                {
                    return i;
                }
                random--;
            }
        }

        return -1;
    }

    public ItemStack getFirstBonemealStack()
    {
        int index = this.getSelectiveStack(2, 6);

        if (index != -1)
        {
            return this.containingItems[index];
        }

        return null;
    }

    public ItemStack getFirstSaplingStack()
    {
        int index = this.getRandomStack(6, 10);

        if (index != -1)
        {
            this.saplingIndex = index;
            return this.containingItems[index];
        }

        return null;
    }

    public ItemStack getFirstSeedStack()
    {
        int index = this.getSelectiveStack(10, 14);

        if (index != -1)
        {
            return this.containingItems[index];
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.containingItems = this.readStandardItemsFromNBT(nbt);

        this.bubbleSize = nbt.getFloat("BubbleSize");
        this.useCount = nbt.getIntArray("UseCountArray");

        if (this.useCount.length == 0)
        {
            this.useCount = new int[2];
        }

        if (nbt.hasKey("waterTank"))
        {
            this.waterTank.readFromNBT(nbt.getCompoundTag("waterTank"));
        }

        if (nbt.hasKey("bubbleVisible"))
        {
            this.setBubbleVisible(nbt.getBoolean("bubbleVisible"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt);
        nbt.setFloat("BubbleSize", this.bubbleSize);
        nbt.setIntArray("UseCountArray", this.useCount);

        if (this.waterTank.getFluid() != null)
        {
            nbt.setTag("waterTank", this.waterTank.writeToNBT(new NBTTagCompound()));
        }

        nbt.setBoolean("bubbleVisible", this.shouldRenderBubble);
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
        ItemStack containingItems[] = this.getContainingItems();

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

    public ItemStack[] getContainingItems()
    {
        return this.containingItems;
    }

    @Override
    public String getName()
    {
        return LabStuffUtils.translate("container.tile_terraformer.name");
    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        if (slotID == 0)
        {
            return FluidContainerRegistry.isEmptyContainer(itemstack);
        }

        return false;
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
        switch (slotID)
        {
        case 0:
            return FluidContainerRegistry.containsFluid(itemstack, new FluidStack(FluidRegistry.WATER, 1));
        case 2:
        case 3:
        case 4:
        case 5:
            return itemstack.getItem() == Items.DYE && itemstack.getItemDamage() == 15;
        case 6:
        case 7:
        case 8:
        case 9:
            return ContainerTerraformer.isOnSaplingList(itemstack);
        case 10:
        case 11:
        case 12:
        case 13:
            return itemstack.getItem() == Items.WHEAT_SEEDS;
        }
        return false;
    }

    
    
    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown <= 0)
        {
            switch (index)
            {
            case 0:
                this.treesDisabled = !this.treesDisabled;
                break;
            case 1:
                this.grassDisabled = !this.grassDisabled;
                break;
            }

            this.disableCooldown = 10;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        switch (index)
        {
        case 0:
            return this.treesDisabled;
        case 1:
            return this.grassDisabled;
        }

        return false;
    }

//    @Override
//    public IBubble getBubble()
//    {
//        return this.terraformBubble;
//    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        this.shouldRenderBubble = shouldRender;
    }

    //Pipe handling
    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid)
    {
        return false;
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain)
    {
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain)
    {
        return null;
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid)
    {
        return fluid != null && "water".equals(fluid.getName());
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill)
    {
        int used = 0;

        if (resource != null && this.canFill(from, resource.getFluid()))
        {
            used = this.waterTank.fill(resource, doFill);
        }

        return used;
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from)
    {
        return new FluidTankInfo[] { new FluidTankInfo(this.waterTank) };
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(this.getPos().getX() - this.bubbleSize, this.getPos().getY() - this.bubbleSize, this.getPos().getZ() - this.bubbleSize, this.getPos().getX() + this.bubbleSize, this.getPos().getY() + this.bubbleSize, this.getPos().getZ() + this.bubbleSize);
    }

    @Override
    public float getBubbleSize()
    {
        return this.bubbleSize;
    }

    @Override
    public boolean getBubbleVisible()
    {
        return this.shouldRenderBubble;
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
		// TODO Auto-generated method stub
		return ItemStackHelper.getAndRemove(this.containingItems, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		this.containingItems[index] = stack;
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 64;
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
		// TODO Auto-generated method stub
		return buffer;
	}

	@Override
	public void setEnergy(double energy) {
		// TODO Auto-generated method stub
		buffer = energy;
	}

	@Override
	public double getMaxEnergy() {
		// TODO Auto-generated method stub
		return 10000;
	}

	@Override
	public double transferEnergyToAcceptor(EnumFacing side, double amount) {
		// TODO Auto-generated method stub
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
		return null;
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
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.ENERGY_STORAGE_CAPABILITY
				|| capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY
				|| capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	public CapabilityWrapperManager manager = new CapabilityWrapperManager(IFluidHandlerWrapper.class, FluidHandlerWrapper.class);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
		{
			return (T)manager.getWrapper(this, side);
		}
		else if(capability == Capabilities.ENERGY_STORAGE_CAPABILITY || capability == Capabilities.ENERGY_ACCEPTOR_CAPABILITY)
			return (T)this;
		
		return super.getCapability(capability, side);
	}

}