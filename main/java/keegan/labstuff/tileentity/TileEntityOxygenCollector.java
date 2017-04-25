package keegan.labstuff.tileentity;

import java.util.EnumSet;

import keegan.ditty.Annotations.NetworkedField;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.util.LabStuffUtils;
import keegan.labstuff.world.*;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityOxygenCollector extends TileEntityOxygen implements IInventory, ISidedInventory
{
    public boolean active;
    public static final int OUTPUT_PER_TICK = 100;
    @NetworkedField(targetSide = Side.CLIENT)
    public float lastOxygenCollected;
    private ItemStack[] containingItems = new ItemStack[1];
    private boolean noAtmosphericOxygen = true;
    private boolean isInitialised = false;
    private boolean producedLastTick = false;
    

    public TileEntityOxygenCollector()
    {
        super(6000, 0);
        this.noRedstoneControl = true;
    }

    @Override
    public void update()
    {
        super.update();

        if (!this.worldObj.isRemote)
        {
            producedLastTick = tank.getFluidAmount() < tank.getCapacity();
        	
            if (this.worldObj.rand.nextInt(10) == 0)
            {
                if (this.buffer >= 200)
                {
                    // The later calculations are more efficient if power is a float, so
                    // there are fewer casts
                    float nearbyLeaves = 0;

                    if (!this.isInitialised)
                    {
                        this.noAtmosphericOxygen = (this.worldObj.provider instanceof ILabstuffWorldProvider && !((ILabstuffWorldProvider) this.worldObj.provider).isGasPresent(IAtmosphericGas.OXYGEN));
                        this.isInitialised = true;
                    }

                    if (this.noAtmosphericOxygen)
                    {
                        // Pre-test to see if close to the map edges, so code
                        // doesn't have to continually test for map edges inside the
                        // loop
                        if (this.getPos().getX() > -29999995 && this.getPos().getY() < 2999995 && this.getPos().getZ() > -29999995 && this.getPos().getZ() < 29999995)
                        {
                            // Test the y coordinates, so code doesn't have to keep
                            // testing that either
                            int miny = this.getPos().getY() - 5;
                            int maxy = this.getPos().getY() + 5;
                            if (miny < 0)
                            {
                                miny = 0;
                            }
                            if (maxy >= this.worldObj.getHeight())
                            {
                                maxy = this.worldObj.getHeight() - 1;
                            }

                            // Loop the x and the z first, so the y loop will be at
                            // fixed (x,z) coordinates meaning fixed chunk
                            // coordinates
                            for (int x = this.getPos().getX() - 5; x <= this.getPos().getX() + 5; x++)
                            {
                                int chunkx = x >> 4;
                                int intrachunkx = x & 15;
                                // Preload the first chunk for the z loop - there
                                // can be a maximum of 2 chunks in the z loop
                                int chunkz = this.getPos().getZ() - 5 >> 4;
                                Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkx, chunkz);
                                for (int z = this.getPos().getZ() - 5; z <= this.getPos().getZ() + 5; z++)
                                {
                                    if (z >> 4 != chunkz)
                                    {
                                        // moved across z chunk boundary into a new
                                        // chunk, so load the new chunk
                                        chunkz = z >> 4;
                                        chunk = this.worldObj.getChunkFromChunkCoords(chunkx, chunkz);
                                    }
                                    for (int y = miny; y <= maxy; y++)
                                    {
                                        // chunk.getBlockID is like world.getBlock
                                        // but faster - needs to be given
                                        // intra-chunk coordinates though
                                        final IBlockState state = chunk.getBlockState(intrachunkx, y, z & 15);
                                        // Test for the two most common blocks (air
                                        // and breatheable air) without looking up
                                        // in the blocksList
                                        if (!(state.getBlock() instanceof BlockAir))
                                        {
                                            BlockPos pos = new BlockPos(x, y, z);
                                            if (state.getBlock().isLeaves(state, this.worldObj, pos) || state.getBlock() instanceof IPlantable && ((IPlantable) state.getBlock()).getPlantType(this.worldObj, pos) == EnumPlantType.Crop)
                                            {
                                                nearbyLeaves += 0.075F * 10F;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        nearbyLeaves = 9.3F * 10F;
                    }

                    nearbyLeaves = (float) Math.floor(nearbyLeaves);

                    this.lastOxygenCollected = nearbyLeaves / 10F;

                    this.tank.setFluid(new FluidStack(LabStuffMain.LO2, (int) Math.max(Math.min(tank.getFluidAmount() + nearbyLeaves, this.tank.getCapacity()), 0)));
                }
                else
                {
                    this.lastOxygenCollected = 0;
                }
            }
        }
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
        return LabStuffUtils.translate("container.oxygencollector.name");
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
    public void closeInventory(EntityPlayer player)
    {

    }

    @Override
    public void openInventory(EntityPlayer player)
    {

    }

    // ISidedInventory Implementation:

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return this.isItemValidForSlot(slotID, itemstack);
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return slotID == 0;
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

    public boolean shouldUseEnergy()
    {
        return this.tank.getCapacity() > 0F && producedLastTick;
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