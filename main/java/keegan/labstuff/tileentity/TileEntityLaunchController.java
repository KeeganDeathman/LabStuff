package keegan.labstuff.tileentity;

import java.io.IOException;
import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.blocks.*;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.entities.*;
import keegan.labstuff.network.IEnergyWrapper;
import keegan.labstuff.util.Annotations.NetworkedField;
import keegan.labstuff.util.LabStuffUtils;
import keegan.labstuff.world.ChunkLoadingCallback;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class TileEntityLaunchController extends TileEntity implements IChunkLoader, ISidedInventory, ILandingPadAttachable, IEnergyWrapper,ITickable
{
    public static final int LV_PER_TICK = 45;
    private ItemStack[] containingItems = new ItemStack[1];
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean launchPadRemovalDisabled = true;
    private Ticket chunkLoadTicket;
    private List<BlockPos> connectedPads = new ArrayList<BlockPos>();
    @NetworkedField(targetSide = Side.CLIENT)
    public int frequency = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public int destFrequency = -1;
    @NetworkedField(targetSide = Side.CLIENT)
    public String ownerName = "";
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean frequencyValid;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean destFrequencyValid;
    @NetworkedField(targetSide = Side.CLIENT)
    public int launchDropdownSelection;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean launchSchedulingEnabled;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean controlEnabled;
    public boolean hideTargetDestination = true;
    public boolean requiresClientUpdate;
    public Object attachedDock = null;
    private boolean frequencyCheckNeeded = false;
    private double buffer = 0;
    private boolean hasEnoughEnergyToRun = this.buffer >= LV_PER_TICK;
//    private static Map<Integer, Long> tickCounts = new HashMap();
//    private static Map<Integer, Integer> instanceCounts = new HashMap();

    public TileEntityLaunchController()
    {
    }
    
    int ticks = 0;

    @Override
    public void update()
    {

        if (!this.worldObj.isRemote)
        {
        	ticks++;
//            if (ConfigManagerCore.enableDebug)
//            {
//            	int dim = this.worldObj);
//            	Long tickCount = tickCounts.get(dim);
//            	if (tickCount == null)
//            	{
//            		tickCount = 0L;
//            		tickCounts.put(dim, tickCount);
//            		instanceCounts.put(dim, 0);
//            	}
//            	int instanceCount = instanceCounts.get(dim);
//	        	if (this.worldObj.getTotalWorldTime() > tickCount)
//	            {
//	            	tickCount = this.worldObj.getTotalWorldTime();
//	            	if (tickCount % 20L == 0L) GCLog.debug("Dim " + dim + ": Number of Launch Controllers updating each tick: " + instanceCount);
//	            	instanceCount = 1;
//	            }
//	            else
//	            	instanceCount++;
//	        	tickCounts.put(dim, tickCount);
//	        	instanceCounts.put(dim, instanceCount);
//            }
        	
        	this.hasEnoughEnergyToRun = this.buffer >= this.LV_PER_TICK;
        	
      		this.controlEnabled = this.launchSchedulingEnabled && this.hasEnoughEnergyToRun;
        	
        	if (this.frequencyCheckNeeded)
            {
                this.checkDestFrequencyValid();
                this.frequencyCheckNeeded = false;
            }

            if (this.requiresClientUpdate)
            {
                // PacketDispatcher.sendPacketToAllPlayers(this.getPacket());
                // TODO
                this.requiresClientUpdate = false;
            }

            if (this.ticks % 40 == 0)
            {
                this.setFrequency(this.frequency);
                this.setDestinationFrequency(this.destFrequency);
            }

            if (this.ticks % 20 == 0)
            {
                if (this.chunkLoadTicket != null)
                {
                    for (int i = 0; i < this.connectedPads.size(); i++)
                    {
                        BlockPos coords = this.connectedPads.get(i);
                        Block block = this.worldObj.getBlockState(coords).getBlock();

                        if (block != LabStuffMain.landingPadFull)
                        {
                            this.connectedPads.remove(i);
                            ForgeChunkManager.unforceChunk(this.chunkLoadTicket, new ChunkPos(coords.getX() >> 4, coords.getZ() >> 4));
                        }
                    }
                }
            }
        }
        else
        {
            if (this.frequency == -1 && this.destFrequency == -1)
            {
                LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_UPDATE_ADVANCED_GUI, LabStuffUtils.getDimensionID(this.worldObj), new Object[] { 5, this.getPos(), 0 }));
            }
        }
    }

    public String getOwnerName()
    {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName)
    {
        this.ownerName = ownerName;
    }

    @Override
    public void invalidate()
    {
        super.invalidate();

        if (this.chunkLoadTicket != null)
        {
            ForgeChunkManager.releaseTicket(this.chunkLoadTicket);
        }
    }

    public void onTicketLoaded(Ticket ticket, boolean placed)
    {
        if (!this.worldObj.isRemote && ConfigManagerCore.launchControllerChunkLoad)
        {
            if (ticket == null)
            {
                return;
            }

            if (this.chunkLoadTicket == null)
            {
                this.chunkLoadTicket = ticket;
            }

            NBTTagCompound nbt = this.chunkLoadTicket.getModData();
            nbt.setInteger("ChunkLoaderTileX", this.getPos().getX());
            nbt.setInteger("ChunkLoaderTileY", this.getPos().getY());
            nbt.setInteger("ChunkLoaderTileZ", this.getPos().getZ());

            for (int x = -2; x <= 2; x++)
            {
                for (int z = -2; z <= 2; z++)
                {
                    Block blockID = this.worldObj.getBlockState(this.getPos().add(x, 0, z)).getBlock();

                    if (blockID instanceof BlockLandingPadFull)
                    {
                        if (this.getPos().getX() + x >> 4 != this.getPos().getX() >> 4 || this.getPos().getZ() + z >> 4 != this.getPos().getZ() >> 4)
                        {
                            this.connectedPads.add(new BlockPos(this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z));

                            if (placed)
                            {
                                ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.worldObj, this.getPos().getX() + x, this.getPos().getY(), this.getPos().getZ() + z, this.getOwnerName());
                            }
                            else
                            {
                                ChunkLoadingCallback.addToList(this.worldObj, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getOwnerName());
                            }
                        }
                    }
                }
            }

            ChunkLoadingCallback.forceChunk(this.chunkLoadTicket, this.worldObj, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), this.getOwnerName());
        }
    }

    public Ticket getTicket()
    {
        return this.chunkLoadTicket;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.ownerName = nbt.getString("OwnerName");
        this.launchDropdownSelection = nbt.getInteger("LaunchSelection");
        this.frequency = nbt.getInteger("ControllerFrequency");
        this.destFrequency = nbt.getInteger("TargetFrequency");
        this.frequencyCheckNeeded = true;
        this.launchPadRemovalDisabled = nbt.getBoolean("LaunchPadRemovalDisabled");
        this.launchSchedulingEnabled = nbt.getBoolean("LaunchPadSchedulingEnabled");
        this.hideTargetDestination = nbt.getBoolean("HideTargetDestination");
        this.requiresClientUpdate = true;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setString("OwnerName", this.ownerName);
        nbt.setInteger("LaunchSelection", this.launchDropdownSelection);
        nbt.setInteger("ControllerFrequency", this.frequency);
        nbt.setInteger("TargetFrequency", this.destFrequency);
        nbt.setBoolean("LaunchPadRemovalDisabled", this.launchPadRemovalDisabled);
        nbt.setBoolean("LaunchPadSchedulingEnabled", this.launchSchedulingEnabled);
        nbt.setBoolean("HideTargetDestination", this.hideTargetDestination);
        return nbt;
    }

    @Override
    public String getName()
    {
        return LabStuffUtils.translate("container.launchcontroller.name");
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    public boolean canAttachToLandingPad(IBlockAccess world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);

        return tile instanceof TileEntityLandingPad;
    }

    public void setFrequency(int frequency)
    {
        this.frequency = frequency;

        if (this.frequency >= 0 && FMLCommonHandler.instance().getMinecraftServerInstance() != null)
        {
            this.frequencyValid = true;
            WorldServer[] servers = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

            worldLoop:
            for (int i = 0; i < servers.length; i++)
            {
                WorldServer world = servers[i];

                for (TileEntity tile2 : new ArrayList<TileEntity>(world.loadedTileEntityList))
                {
                    if (this != tile2)
                    {
                        tile2 = world.getTileEntity(tile2.getPos());
                        if (tile2 == null)
                        {
                            continue;
                        }

                        if (tile2 instanceof TileEntityLaunchController)
                        {
                            TileEntityLaunchController launchController2 = (TileEntityLaunchController) tile2;

                            if (launchController2.frequency == this.frequency)
                            {
                                this.frequencyValid = false;
                                break worldLoop;
                            }
                        }
                    }
                }
            }
        }
        else
        {
            this.frequencyValid = false;
        }
    }

    public void setDestinationFrequency(int frequency)
    {
        if (frequency != this.destFrequency)
        {
            this.destFrequency = frequency;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void checkDestFrequencyValid()
    {
        if (!this.worldObj.isRemote && FMLCommonHandler.instance().getMinecraftServerInstance() != null)
        {
            this.destFrequencyValid = false;
            if (this.destFrequency >= 0)
            {
                WorldServer[] servers = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;
                for (int i = 0; i < servers.length; i++)
                {
                    WorldServer world = servers[i];

                    for (TileEntity tile2 : new ArrayList<TileEntity>(world.loadedTileEntityList))
                    {
                        if (this != tile2)
                        {
                            tile2 = world.getTileEntity(tile2.getPos());
                            if (tile2 == null)
                            {
                                continue;
                            }

                            if (tile2 instanceof TileEntityLaunchController)
                            {
                                TileEntityLaunchController launchController2 = (TileEntityLaunchController) tile2;

                                if (launchController2.frequency == this.destFrequency)
                                {
                                    this.destFrequencyValid = true;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean validFrequency()
    {
        this.checkDestFrequencyValid();
        return this.hasEnoughEnergyToRun && this.frequencyValid && this.destFrequencyValid;
    }

    public void setLaunchDropdownSelection(int newvalue)
    {
        if (newvalue != this.launchDropdownSelection)
        {
            this.launchDropdownSelection = newvalue;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void setLaunchSchedulingEnabled(boolean newvalue)
    {
        if (newvalue != this.launchSchedulingEnabled)
        {
            this.launchSchedulingEnabled = newvalue;
            this.checkDestFrequencyValid();
            this.updateRocketOnDockSettings();
        }
    }

    public void updateRocketOnDockSettings()
    {
        if (this.attachedDock instanceof TileEntityLandingPad)
        {
            TileEntityLandingPad pad = ((TileEntityLandingPad) this.attachedDock);
            IDockable rocket = pad.getDockedEntity();
            if (rocket instanceof EntityAutoRocket)
            {
                ((EntityAutoRocket) rocket).updateControllerSettings(pad);
            }
        }
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return null;
    }

    public void setAttachedPad(IFuelDock pad)
    {
        this.attachedDock = pad;
    }

	@Override
	public int getSizeInventory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getInventoryStackLimit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		// TODO Auto-generated method stub
		return false;
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
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
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
		return buffer += amount;
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
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Chunk loadChunk(World worldIn, int x, int z) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveChunk(World worldIn, Chunk chunkIn) throws MinecraftException, IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveExtraChunkData(World worldIn, Chunk chunkIn) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chunkTick() 
	{
		if(getTicket() == null)
			this.chunkLoadTicket = ForgeChunkManager.requestTicket(LabStuffMain.instance, worldObj, Type.NORMAL);
		ForgeChunkManager.forceChunk(getTicket(), new ChunkPos(pos));
	}

	@Override
	public void saveExtraData() {
		// TODO Auto-generated method stub
		
	}

}