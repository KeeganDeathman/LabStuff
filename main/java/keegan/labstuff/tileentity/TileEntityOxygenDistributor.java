package keegan.labstuff.tileentity;

import java.util.*;

import io.netty.buffer.ByteBuf;
import keegan.ditty.Annotations.NetworkedField;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.IOxygenReliantBlock;
import keegan.labstuff.entities.IBubbleProvider;
import keegan.labstuff.items.IItemOxygenSupply;
import keegan.labstuff.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.*;

public class TileEntityOxygenDistributor extends TileEntityOxygen implements IInventory, ISidedInventory, IBubbleProvider
{
    public boolean active;
    public boolean lastActive;

    private ItemStack[] containingItems = new ItemStack[2];
    public static HashSet<BlockVec3Dim> loadedTiles = new HashSet();
    public float bubbleSize;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRenderBubble = true;
    private int ticks;
    private boolean hasEnoughEnergyToRun;

    public TileEntityOxygenDistributor()
    {
        super(6000, 8);
//        this.oxygenBubble = null;
    }

    @Override
    public void validate()
    {
        super.validate();
        if (!this.worldObj.isRemote) TileEntityOxygenDistributor.loadedTiles.add(new BlockVec3Dim(this));
    }

    @Override
    public void onChunkUnload()
    {
        TileEntityOxygenDistributor.loadedTiles.remove(new BlockVec3Dim(this));
        super.onChunkUnload();
    }

    @Override
    public void invalidate()
    {
        if (!this.worldObj.isRemote/* && this.oxygenBubble != null*/)
        {
            int bubbleR = MathHelper.ceiling_double_int(bubbleSize);
            int bubbleR2 = (int) (bubbleSize * bubbleSize);
            final int xMin = this.getPos().getX() - bubbleR;
            final int xMax = this.getPos().getX() + bubbleR;
            final int yMin = this.getPos().getY() - bubbleR;
            final int yMax = this.getPos().getY() + bubbleR;
            final int zMin = this.getPos().getZ() - bubbleR;
            final int zMax = this.getPos().getZ() + bubbleR;
            for (int x = xMin; x < xMax; x++)
            {
                for (int z = zMin; z < zMax; z++)
                {
                    //Doing y as the inner loop allows BlockVec3 to cache the chunks more efficiently
                    for (int y = yMin; y < yMax; y++)
                    {
                        IBlockState state = new BlockVec3(x, y, z).getBlockState(this.worldObj);

                        if (state.getBlock() instanceof IOxygenReliantBlock && this.getDistanceFromServer(x, y, z) <= bubbleR2)
                        {
                            this.worldObj.scheduleUpdate(new BlockPos(x, y, z), state.getBlock(), 0);
                        }
                    }
                }
            }
//        	this.oxygenBubble.setDead();
            TileEntityOxygenDistributor.loadedTiles.remove(new BlockVec3Dim(this));
        }

        super.invalidate();
    }

    @Override
    public void addExtraNetworkedData(List<Object> networkedList)
    {
        if (!this.worldObj.isRemote && !this.isInvalid())
        {
//            networkedList.add(this.oxygenBubble != null);
//            if (this.oxygenBubble != null)
//            {
//                networkedList.add(this.oxygenBubble.getEntityId());
//            }
            if (this.worldObj.getMinecraftServer().isDedicatedServer())
            {
                networkedList.add(loadedTiles.size());
                //TODO: Limit this to ones in the same dimension as this tile?
                for (BlockVec3Dim distributor : loadedTiles)
                {
                    if (distributor == null)
                    {
                        networkedList.add(-1);
                        networkedList.add(-1);
                        networkedList.add(-1);
                        networkedList.add(-1);
                    }
                    else
                    {
                        networkedList.add(distributor.x);
                        networkedList.add(distributor.y);
                        networkedList.add(distributor.z);
                        networkedList.add(distributor.dim);
                    }
                }
            }
            else
            //Signal integrated server, do not clear loadedTiles
            {
                networkedList.add(-1);
            }
            networkedList.add(this.bubbleSize);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB(this.getPos().getX() - this.bubbleSize, this.getPos().getY() - this.bubbleSize, this.getPos().getZ() - this.bubbleSize, this.getPos().getX() + this.bubbleSize, this.getPos().getY() + this.bubbleSize, this.getPos().getZ() + this.bubbleSize);
    }

    @Override
    public void readExtraNetworkedData(ByteBuf dataStream)
    {
        if (this.worldObj.isRemote)
        {
//            if (dataStream.readBoolean())
//            {
//                this.oxygenBubble = (EntityBubble) worldObj.getEntityByID(dataStream.readInt());
//            }
            int size = dataStream.readInt();
            if (size >= 0)
            {
                loadedTiles.clear();
                for (int i = 0; i < size; ++i)
                {
                    int i1 = dataStream.readInt();
                    int i2 = dataStream.readInt();
                    int i3 = dataStream.readInt();
                    int i4 = dataStream.readInt();
                    if (i1 == -1 && i2 == -1 && i3 == -1 && i4 == -1)
                    {
                        continue;
                    }
                    loadedTiles.add(new BlockVec3Dim(i1, i2, i3, i4));
                }
            }
            this.bubbleSize = dataStream.readFloat();
        }
    }

    public int getDistanceFromServer(int par1, int par3, int par5)
    {
        final int d3 = this.getPos().getX() - par1;
        final int d4 = this.getPos().getY() - par3;
        final int d5 = this.getPos().getZ() - par5;
        return d3 * d3 + d4 * d4 + d5 * d5;
    }

    
    @Override
    public void update()
    {
    	ticks++;
    	hasEnoughEnergyToRun = buffer >= 75;
        if (!this.worldObj.isRemote)
        {
            ItemStack oxygenItemStack = this.getStackInSlot(1);
            if (oxygenItemStack != null && oxygenItemStack.getItem() instanceof IItemOxygenSupply)
            {
                IItemOxygenSupply oxygenItem = (IItemOxygenSupply) oxygenItemStack.getItem();
                int oxygenDraw = (int) Math.floor(Math.min(this.oxygenPerTick * 2.5F, tank.getCapacity() - tank.getFluidAmount()));
                oxygenItem.discharge(oxygenItemStack, oxygenDraw);
                this.tank.fill(new FluidStack(LabStuffMain.LO2, oxygenDraw), true);
            }
        }

        super.update();

        if (!this.worldObj.isRemote)
        {
            if (this.buffer > 0.0F && this.hasEnoughEnergyToRun && this.tank.getFluidAmount() > this.oxygenPerTick)
            {
                this.bubbleSize += 0.01F;
            }
            else
            {
                this.bubbleSize -= 0.1F;
            }

            this.bubbleSize = Math.min(Math.max(this.bubbleSize, 0.0F), 10.0F);
        }

//        if (!hasValidBubble && !this.worldObj.isRemote && (this.oxygenBubble == null || this.ticks < 25))
//        {
//            //Check it's a world without a breathable atmosphere
//        	if (this.oxygenBubble == null && this.worldObj.provider instanceof IGalacticraftWorldProvider && !((IGalacticraftWorldProvider)this.worldObj.provider).hasBreathableAtmosphere())
//            {
//                this.oxygenBubble = new EntityBubble(this.worldObj, new Vector3(this), this);
//                this.hasValidBubble = true;
//                this.worldObj.spawnEntityInWorld(this.oxygenBubble);
//            }
//        }

        if (!this.worldObj.isRemote/* && this.oxygenBubble != null*/)
        {
            this.active = bubbleSize >= 1 && this.hasEnoughEnergyToRun && tank.getFluidAmount() > this.oxygenPerTick;

            if (this.ticks % (this.active ? 20 : 4) == 0)
            {
                double size = bubbleSize;
                int bubbleR = MathHelper.floor_double(size) + 4;
                int bubbleR2 = (int) (size * size);
                for (int x = this.getPos().getX() - bubbleR; x <= this.getPos().getX() + bubbleR; x++)
                {
                    for (int y = this.getPos().getY() - bubbleR; y <= this.getPos().getY() + bubbleR; y++)
                    {
                        for (int z = this.getPos().getZ() - bubbleR; z <= this.getPos().getZ() + bubbleR; z++)
                        {
                            BlockPos pos = new BlockPos(x, y, z);
                            Block block = this.worldObj.getBlockState(pos).getBlock();

                            if (block instanceof IOxygenReliantBlock)
                            {
                                if (this.getDistanceFromServer(x, y, z) <= bubbleR2)
                                {
                                    ((IOxygenReliantBlock) block).onOxygenAdded(this.worldObj, new BlockPos(x, y, z));
                                }
                                else
                                {
                                    //Do not necessarily extinguish it - it might be inside another oxygen system
                                    this.worldObj.scheduleUpdate(pos, block, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        this.lastActive = this.active;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("bubbleVisible"))
        {
            this.setBubbleVisible(nbt.getBoolean("bubbleVisible"));
        }

        if (nbt.hasKey("bubbleSize"))
        {
            this.bubbleSize = nbt.getFloat("bubbleSize");
        }
//        this.hasValidBubble = nbt.getBoolean("hasValidBubble");

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

        nbt.setBoolean("bubbleVisible", this.shouldRenderBubble);
        nbt.setFloat("bubbleSize", this.bubbleSize);
//        nbt.setBoolean("hasValidBubble", this.hasValidBubble);

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
        return LabStuffUtils.translate("container.oxygendistributor.name");
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
    public void openInventory(EntityPlayer player)
    {

    }

    @Override
    public void closeInventory(EntityPlayer player)
    {

    }

    public boolean inBubble(double pX, double pY, double pZ)
    {
        double r = bubbleSize;
        r *= r;
        double d3 = this.getPos().getX() + 0.5D - pX;
        d3 *= d3;
        if (d3 > r)
        {
            return false;
        }
        double d4 = this.getPos().getZ() + 0.5D - pZ;
        d4 *= d4;
        if (d3 + d4 > r)
        {
            return false;
        }
        double d5 = this.getPos().getY() + 0.5D - pY;
        return d3 + d4 + d5 * d5 < r;
    }

    @Override
    public void setBubbleVisible(boolean shouldRender)
    {
        this.shouldRenderBubble = shouldRender;
//        this.oxygenBubble.setShouldRender(shouldRender);
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