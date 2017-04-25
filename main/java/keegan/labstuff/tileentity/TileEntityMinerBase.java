package keegan.labstuff.tileentity;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.Map.Entry;

import com.google.common.collect.Lists;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.dimension.WorldProviderAsteroids;
import keegan.labstuff.entities.EntityAstroMiner;
import keegan.labstuff.network.IEnergyWrapper;
import keegan.labstuff.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.model.b3d.B3DModel.Face;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.*;

public class TileEntityMinerBase extends TileEntityAdvanced implements ISidedInventory, IMultiBlock, ITileClientUpdates, IEnergyWrapper, IDisableableMachine
{
    public static final int HOLDSIZE = 72;
    private ItemStack[] containingItems = new ItemStack[HOLDSIZE + 1];
    private int[] slotArray;
    public boolean isMaster = false;
    public EnumFacing facing = EnumFacing.NORTH;
    private BlockPos mainBlockPosition;
    private LinkedList<BlockVec3> targetPoints = new LinkedList();
    private WeakReference<TileEntityMinerBase> masterTile = null;
    public boolean updateClientFlag;
    public boolean findTargetPointsFlag;
    public int linkCountDown = 0;
    public static Map<Integer, List<BlockPos>> newMinerBases = new HashMap<Integer, List<BlockPos>>();
    private AxisAlignedBB renderAABB;
    
    public static void checkNewMinerBases()
    {
        Iterator<Entry<Integer, List<BlockPos>>> entries = newMinerBases.entrySet().iterator();
        while (entries.hasNext())
        {
            Entry<Integer, List<BlockPos>> entry = entries.next();
            if (entry.getValue().isEmpty()) continue;
            
            World w = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(entry.getKey());
            if (w == null)
            {
                LSLog.severe("Astro Miner Base placement: Unable to find server world for dim " + entry.getKey());
                entries.remove();
                continue;
            }
            
            for (BlockPos posMain : entry.getValue())
            {
                BlockPos master = new BlockPos(posMain);
                for (int x = 0; x < 2; x++)
                {
                    for (int y = 0; y < 2; y++)
                    {
                        for (int z = 0; z < 2; z++)
                        {
                            BlockPos pos = posMain.add(x, y, z);
                            w.setBlockState(pos, LabStuffMain.minerBaseFull.getDefaultState(), 2);
                            final TileEntity tile = w.getTileEntity(pos);

                            if (tile instanceof TileEntityMinerBase)
                            {
                                ((TileEntityMinerBase) tile).setMainBlockPos(master);
                                ((TileEntityMinerBase) tile).updateClientFlag = true;
                            }
                        }
                    }
                }
            }
            
            entry.getValue().clear();
        }
    }
    
    public static void addNewMinerBase(int dimID, BlockPos blockPos)
    {
        if (newMinerBases.containsKey(dimID))
        {
            newMinerBases.get(dimID).add(blockPos);
        }
        else
        {
            List<BlockPos> blockPositions = Lists.newArrayList();
            newMinerBases.put(dimID, blockPositions);
            blockPositions.add(blockPos);
        }
    }
    

    public void setMainBlockPosition(BlockPos mainBlockPosition)
    {
        this.mainBlockPosition = mainBlockPosition;
    }

    /**
     * The number of players currently using this chest
     */
    public int numUsingPlayers;

    /**
     * Server sync counter (once per 20 ticks)
     */
    private int ticksSinceSync;

    private boolean spawnedMiner = false;

    public EntityAstroMiner linkedMiner = null;
    public UUID linkedMinerID = null;
    private boolean initialised;
    public boolean hasEnoughEnergyToRun;
    public double buffer = 0;

    public TileEntityMinerBase()
    {
        this.slotArray = new int[HOLDSIZE];
        for (int i = 0; i < HOLDSIZE; i++)
        {
            this.slotArray[i] = i + 1;
        }
    }

    @Override
    public void update()
    {
    	ticks++;
    	if(buffer >= 40)
    		hasEnoughEnergyToRun = true;
    	else
    		hasEnoughEnergyToRun = false;

        if (!this.initialised)
        {
            this.initialised = true;
            if (!this.worldObj.isRemote && !this.isMaster)
            {
                if (this.getMaster() == null)
                {
                    this.worldObj.setBlockState(this.getPos(), LabStuffMain.blockMinerBase.getDefaultState(), 2);
                }
            }
        }

        if (this.updateClientFlag)
        {
            assert(!this.worldObj.isRemote);  //Just checking: updateClientFlag should not be capable of being set on clients
            this.updateAllInDimension();
        	this.updateClientFlag = false;
        }

        if (this.findTargetPointsFlag)
        {
            if (this.isMaster && this.linkedMiner != null)
            {
                this.findTargetPoints();
            }
            this.findTargetPointsFlag = false;
        }

        //TODO: Find linkedminer by UUID and update it if not chunkloaded?

        if (!this.isMaster)
        {
            TileEntityMinerBase master = this.getMaster();

            if (master != null)
            {
                float energyLimit = (float) (master.getMaxEnergy() - master.getEnergy());
                if (energyLimit < 0F)
                {
                    energyLimit = 0F;
                }
                float hasEnergy = (float) buffer;
                if (hasEnergy > 0F)
                {
                    buffer -= energyLimit;
                    master.transferEnergyToAcceptor(LabStuffUtils.getDirectionOfConnection(master, this), energyLimit);
                }
            }
        }

        //Used for refreshing client with linked miner position data
        if (this.linkCountDown > 0)
        {
            this.linkCountDown--;
        }
    }

    //TODO - currently unused, the master position replaces this?
    protected void initialiseMultiTiles(BlockPos pos, World world)
    {
        List<BlockPos> positions = new ArrayList();
        this.getPositions(pos, positions);
        for (BlockPos vecToAdd : positions)
        {
            TileEntity tile = world.getTileEntity(vecToAdd);
            if (tile instanceof TileEntityMinerBase)
            {
                ((TileEntityMinerBase) tile).mainBlockPosition = pos;
            }
        }
    }

    public boolean spawnMiner(EntityPlayerMP player)
    {
        if (this.isMaster)
        {
            if (this.linkedMiner != null)
            {
                if (this.linkedMiner.isDead)
                {
                    this.unlinkMiner();
                }
            }
            if (this.linkedMinerID == null)
            {
//                System.err.println("" + this.facing);
                if (EntityAstroMiner.spawnMinerAtBase(this.worldObj, this.getPos().getX() + 1, this.getPos().getY() + 1, this.getPos().getZ() + 1, this.facing, new BlockVec3(this), player))
                {
                    this.findTargetPoints();
                    return true;
                }
            }
            return false;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.spawnMiner(player);
        }
        return false;
    }

    public TileEntityMinerBase getMaster()
    {
        if (this.mainBlockPosition == null)
        {
            return null;
        }

        if (masterTile == null)
        {
            TileEntity tileEntity = this.worldObj.getTileEntity(this.mainBlockPosition);

            if (tileEntity instanceof TileEntityMinerBase)
            {
                masterTile = new WeakReference<TileEntityMinerBase>(((TileEntityMinerBase) tileEntity));
            }

            if (masterTile == null)
            {
                return null;
            }
        }

        TileEntityMinerBase master = this.masterTile.get();

        if (master != null && master.isMaster)
        {
            return master;
        }

        return null;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.containingItems = this.readStandardItemsFromNBT(nbt);
        this.isMaster = nbt.getBoolean("isMaster");
        if (this.isMaster)
        {
            this.updateClientFlag = true;
        }
        else {
            NBTTagCompound tagCompound = nbt.getCompoundTag("masterpos");
            if (tagCompound.getKeySet().isEmpty())
                this.setMainBlockPosition(null);
            else
            {
                this.setMainBlockPosition(new BlockPos(tagCompound.getInteger("x"), tagCompound.getInteger("y"), tagCompound.getInteger("z")));
                this.updateClientFlag = true;
            }
        }
        this.facing = EnumFacing.getHorizontal(nbt.getInteger("facing"));
        if (nbt.hasKey("LinkedUUIDMost", 4) && nbt.hasKey("LinkedUUIDLeast", 4))
        {
            this.linkedMinerID = new UUID(nbt.getLong("LinkedUUIDMost"), nbt.getLong("LinkedUUIDLeast"));
        }
        else
        {
            this.linkedMinerID = null;
        }
        if (nbt.hasKey("TargetPoints"))
        {
            this.targetPoints.clear();
            final NBTTagList mpList = nbt.getTagList("TargetPoints", 10);
            for (int j = 0; j < mpList.tagCount(); j++)
            {
                NBTTagCompound bvTag = mpList.getCompoundTagAt(j);
                this.targetPoints.add(BlockVec3.readFromNBT(bvTag));
            }
        }
        else
        {
            this.findTargetPointsFlag = this.isMaster;
        }
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

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        this.writeStandardItemsToNBT(nbt);
        nbt.setBoolean("isMaster", this.isMaster);
        if (!this.isMaster && this.mainBlockPosition != null)
        {
            NBTTagCompound masterTag = new NBTTagCompound();
            masterTag.setInteger("x", this.mainBlockPosition.getX());
            masterTag.setInteger("y", this.mainBlockPosition.getY());
            masterTag.setInteger("z", this.mainBlockPosition.getZ());
            nbt.setTag("masterpos", masterTag);
        }
        nbt.setInteger("facing", this.facing.getHorizontalIndex());
        if (this.isMaster && this.linkedMinerID != null)
        {
            nbt.setLong("LinkedUUIDMost", this.linkedMinerID.getMostSignificantBits());
            nbt.setLong("LinkedUUIDLeast", this.linkedMinerID.getLeastSignificantBits());
        }
        NBTTagList mpList = new NBTTagList();
        for (int j = 0; j < this.targetPoints.size(); j++)
        {
            mpList.appendTag(this.targetPoints.get(j).writeToNBT(new NBTTagCompound()));
        }
        nbt.setTag("TargetPoints", mpList);
        return nbt;
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
    public boolean hasCustomName()
    {
        return true;
    }

    public boolean addToInventory(ItemStack itemstack)
    {
        //TODO - add test for is container open and if so use Container.mergeItemStack

        boolean flag1 = false;
        int k = 1;
        int invSize = this.getSizeInventory();

        ItemStack existingStack;

        if (itemstack.isStackable())
        {
            while (itemstack.stackSize > 0 && k < invSize)
            {
                existingStack = this.containingItems[k];

                if (existingStack != null && existingStack.getItem() == itemstack.getItem() && (!itemstack.getHasSubtypes() || itemstack.getItemDamage() == existingStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(itemstack, existingStack))
                {
                    int combined = existingStack.stackSize + itemstack.stackSize;

                    if (combined <= itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize = 0;
                        existingStack.stackSize = combined;
                        flag1 = true;
                    }
                    else if (existingStack.stackSize < itemstack.getMaxStackSize())
                    {
                        itemstack.stackSize -= itemstack.getMaxStackSize() - existingStack.stackSize;
                        existingStack.stackSize = itemstack.getMaxStackSize();
                        flag1 = true;
                    }
                }

                ++k;
            }
        }

        if (itemstack.stackSize > 0)
        {
            k = 1;

            while (k < invSize)
            {
                existingStack = this.containingItems[k];

                if (existingStack == null)
                {
                    this.containingItems[k] = itemstack.copy();
                    itemstack.stackSize = 0;
                    flag1 = true;
                    break;
                }

                ++k;
            }
        }

        this.markDirty();
        return flag1;
    }

    @Override
    public void validate()
    {
        super.validate();
        this.clientValidate();
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void invalidate()
    {
        super.invalidate();
        this.updateContainingBlockInfo();
    }

    @Override
    public String getName()
    {
        return LabStuffUtils.translate("tile.miner_base.name");
    }

    protected ItemStack[] getContainingItems()
    {
        if (this.isMaster)
        {
            return this.containingItems;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.getContainingItems();
        }

        return this.containingItems;
    }

    public void setMainBlockPos(BlockPos master)
    {
        this.masterTile = null;
        if (this.getPos().equals(master))
        {
            this.isMaster = true;
            this.setMainBlockPosition(null);
            return;
        }
        this.isMaster = false;
        this.setMainBlockPosition(master);
        this.markDirty();
    }

    public void onBlockRemoval()
    {
        if (this.isMaster)
        {
            this.invalidate();
            this.onDestroy(this);
            return;
        }

        TileEntityMinerBase master = this.getMaster();

        if (master != null && !master.isInvalid())
        {
            this.worldObj.destroyBlock(master.getPos(), false);
        }
    }

    @Override
    public boolean onActivated(EntityPlayer entityPlayer)
    {
        if (this.isMaster)
        {
            ItemStack holding = entityPlayer.getActiveItemStack();
            if (holding != null && holding.getItem() == LabStuffMain.astroMiner)
            {
                return false;
            }

            entityPlayer.openGui(LabStuffMain.instance, 34, this.worldObj, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
            return true;
        }
        else
        {
            TileEntityMinerBase master = this.getMaster();
            return master != null && master.onActivated(entityPlayer);
        }
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        for (int y = 0; y < 2; y++)
        {
            if (placedPosition.getY() + y >= this.worldObj.getHeight()) break;
            for (int x = 0; x < 2; x++)
            {
                for (int z = 0; z < 2; z++)
                {
                    if (x + y + z == 0) continue;
                    positions.add(new BlockPos(placedPosition.getX() + x, placedPosition.getY() + y, placedPosition.getZ() + z));
                }
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        final BlockPos thisBlock = getPos();
        List<BlockPos> positions = new ArrayList();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            IBlockState stateAt = this.worldObj.getBlockState(pos);

            if (stateAt.getBlock() == LabStuffMain.minerBaseFull)
            {
                if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1D)
                {
                    FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.worldObj.getBlockState(pos));
                }
                this.worldObj.destroyBlock(pos, false);
            }
        }
        this.worldObj.destroyBlock(thisBlock, true);
    }

    //TODO
    //maybe 2 electrical inputs are needed?
    //chest goes above (could be 2 chests or other mods storage)

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        if (this.renderAABB == null)
        {
            this.renderAABB = new AxisAlignedBB(pos, pos.add(2, 2, 2));
        }
        return this.renderAABB;
    }

    @Override
    public void buildDataPacket(int[] data)
    {
        int x, y, z;
        if (this.mainBlockPosition != null)
        {
            x = this.mainBlockPosition.getX();
            y = this.mainBlockPosition.getY();
            z = this.mainBlockPosition.getZ();
        }
        else
        {
            x = this.getPos().getX();
            y = this.getPos().getY();
            z = this.getPos().getZ();
        }
        int link = (this.linkedMinerID != null) ? 8 : 0;
        data[0] = link + this.facing.ordinal();
        data[1] = x;
        data[2] = y;
        data[3] = z;
    }

    public void linkMiner(EntityAstroMiner entityAstroMiner)
    {
        this.linkedMiner = entityAstroMiner;
        this.linkedMinerID = this.linkedMiner.getUniqueID();
        this.updateClientFlag = true;
        this.markDirty();
    }

    public void unlinkMiner()
    {
        this.linkedMiner = null;
        this.linkedMinerID = null;
        this.updateClientFlag = true;
        this.markDirty();
    }

    public UUID getLinkedMiner()
    {
        if (this.isMaster)
        {
            return this.linkedMinerID;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.linkedMinerID;
        }
        return null;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        if (this.isMaster)
        {
            return side != this.facing ? slotArray : new int[] {};
        }

        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.getSlotsForFace(side);
        }

        return new int[] {};
    }

    @Override
    public boolean canInsertItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side)
    {
    	return true;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemstack)
    {
    	return true;
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (this.isMaster)
        {
            return this.containingItems[par1];
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.getStackInSlot(par1);
        }

        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt)
    {
        if (this.isMaster)
        {
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
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.decrStackSize(slot, amt);
        }

        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int par1)
    {
        if (this.isMaster)
        {
        	return ItemStackHelper.getAndRemove(containingItems, par1);
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            return master.removeStackFromSlot(par1);
        }

        return null;
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
        if (this.isMaster)
        {
        	this.containingItems[par1] = par2ItemStack;
            this.markDirty();
            return;
        }
        TileEntityMinerBase master = this.getMaster();
        if (master != null)
        {
            master.setInventorySlotContents(par1, par2ItemStack);
        }

        return;
    }

    @Override
    public int getSizeInventory()
    {
        return HOLDSIZE + 1;
    }

    public BlockVec3 findNextTarget()
    {
        if (!this.targetPoints.isEmpty())
        {
            BlockVec3 pos = this.targetPoints.removeFirst();
            this.markDirty();
            if (pos != null)
            {
                return pos.clone();
            }
        }

        //No more mining targets, the whole area is mined
        return null;
    }

    private void findTargetPoints()
    {
        this.targetPoints.clear();
        BlockVec3 posnTarget = new BlockVec3(this);

        if (this.worldObj.provider instanceof WorldProviderAsteroids)
        {
            ArrayList<BlockVec3> roids = ((WorldProviderAsteroids) this.worldObj.provider).getClosestAsteroidsXZ(posnTarget.x, posnTarget.y, posnTarget.z, this.facing.getIndex(), 100);
            if (roids != null && roids.size() > 0)
            {
                this.targetPoints.addAll(roids);
                return;
            }
        }

        posnTarget.modifyPositionFromSide(this.facing, this.worldObj.rand.nextInt(16) + 32);
        int miny = Math.min(this.getPos().getY() * 2 - 90, this.getPos().getY() - 22);
        if (miny < 5)
        {
            miny = 5;
        }
        posnTarget.y = miny + 5 + this.worldObj.rand.nextInt(4);

        this.targetPoints.add(posnTarget);

        EnumFacing lateral = EnumFacing.NORTH;
        EnumFacing inLine = this.facing;
        if (inLine.getAxis() == Axis.Z)
        {
            lateral = EnumFacing.WEST;
        }

        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 13));
        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -13));
        if (posnTarget.y > 17)
        {
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 7).modifyPositionFromSide(EnumFacing.DOWN, 11));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -7).modifyPositionFromSide(EnumFacing.DOWN, 11));
        }
        else
        {
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 26));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -26));
        }
        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 7).modifyPositionFromSide(EnumFacing.UP, 11));
        this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -7).modifyPositionFromSide(EnumFacing.UP, 11));
        if (posnTarget.y < this.getPos().getY() - 38)
        {
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, 13).modifyPositionFromSide(EnumFacing.UP, 22));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(EnumFacing.UP, 22));
            this.targetPoints.add(posnTarget.clone().modifyPositionFromSide(lateral, -13).modifyPositionFromSide(EnumFacing.UP, 22));
        }

        int s = this.targetPoints.size();
        for (int i = 0; i < s; i++)
        {
            this.targetPoints.add(this.targetPoints.get(i).clone().modifyPositionFromSide(inLine, EntityAstroMiner.MINE_LENGTH + 6));
        }

        this.markDirty();
        return;
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        //Used to recall miner
        TileEntityMinerBase master;
        if (!this.isMaster)
        {
            master = this.getMaster();
            if (master == null)
            {
                return;
            }
        }
        else
        {
            master = this;
        }
        if (master.linkedMiner != null)
        {
            master.linkedMiner.recall();
        }
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    @Override
    public void updateClient(List<Object> data)
    {
        int data1 = (Integer) data.get(1);
        this.facing = EnumFacing.getFront(data1 & 7);
        this.setMainBlockPos(new BlockPos((Integer) data.get(2), (Integer) data.get(3), (Integer) data.get(4)));
        if (data1 > 7)
        {
            this.linkedMinerID = UUID.randomUUID();
        }
        else
        {
            this.linkedMinerID = null;
        }
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
		buffer = energy;
	}

	@Override
	public double getMaxEnergy() {
		return 1000000;
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
	public boolean getDisabled(int index) {
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