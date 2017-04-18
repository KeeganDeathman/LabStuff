package keegan.labstuff.entities;

import java.util.*;

import io.netty.buffer.ByteBuf;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.IPacketReceiver;
import keegan.labstuff.blocks.BlockLandingPadFull;
import keegan.labstuff.client.sounds.SoundUpdaterRocket;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.event.EventLandingPadRemoval;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import keegan.labstuff.util.FluidUtil;
import keegan.labstuff.world.*;
import net.minecraft.block.Block;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.*;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.*;

/**
 * Do not include this prefab class in your released mod download.
 */
public abstract class EntityAutoRocket extends EntitySpaceshipBase implements ILandable, IInventory, IPacketReceiver, IEntityNoisy
{
    public FluidTank fuelTank = new FluidTank(this.getFuelTankCapacity() * ConfigManagerCore.rocketFuelFactor);
    public int destinationFrequency = -1;
    public BlockPos targetVec;
    public int targetDimension;
    protected ItemStack[] cargoItems;
    private IFuelDock landingPad;
    public boolean landing;
    public EnumAutoLaunch autoLaunchSetting;
    public int autoLaunchCountdown;
    private BlockVec3 activeLaunchController;

    public String statusMessage;
    public String statusColour;
    public int statusMessageCooldown;
    public int lastStatusMessageCooldown;
    public boolean statusValid;
    protected double lastMotionY;
    protected double lastLastMotionY;
    private boolean waitForPlayer;
    protected ITickable rocketSoundUpdater;
    private boolean rocketSoundToStop = false;
    private static Class<?> controllerClass = null;

    static
    {
        try
        {
            controllerClass = Class.forName("keegan.labstuff.tileentity.TileEntityLaunchController");
        } catch (ClassNotFoundException e) 
        {
            LSLog.info("LabStuff' LaunchController not present, rockets will not be launch controlled.");
        }
    }

    public EntityAutoRocket(World world)
    {
        super(world);
    }

    public EntityAutoRocket(World world, double posX, double posY, double posZ)
    {
        this(world);
        this.setSize(0.98F, 2F);
        this.setPosition(posX, posY, posZ);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = posX;
        this.prevPosY = posY;
        this.prevPosZ = posZ;
    }

    public abstract int getFuelTankCapacity();

    //Used for Cargo Rockets, client is asking the server what is the status
    public boolean checkLaunchValidity()
    {
        this.statusMessageCooldown = 40;

        if (this.hasValidFuel())
        {
            if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal() && !this.worldObj.isRemote)
            {
                if (!this.setFrequency())
                {
                    this.destinationFrequency = -1;
                    this.statusMessage = I18n.translateToLocal("gui.message.frequency.name") + "#" + I18n.translateToLocal("gui.message.not_set.name");
                    this.statusColour = "\u00a7c";
                    return false;
                }
                else
                {
                    this.statusMessage = I18n.translateToLocal("gui.message.success.name");
                    this.statusColour = "\u00a7a";
                    return true;
                }
            }
        }
        else
        {
            this.destinationFrequency = -1;
            this.statusMessage = I18n.translateToLocal("gui.message.not_enough.name") + "#" + I18n.translateToLocal("gui.message.fuel.name");
            this.statusColour = "\u00a7c";
            return false;
        }

        this.destinationFrequency = -1;
        return false;
    }

    public boolean setFrequency()
    {
        if (controllerClass == null)
        {
            return false;
        }

        if (this.activeLaunchController != null)
                                {
            TileEntity launchController = this.activeLaunchController.getTileEntity(this.worldObj);
            if (controllerClass.isInstance(launchController))
                                {
                                    try
                                    {
                                Boolean b = (Boolean) controllerClass.getMethod("validFrequency").invoke(launchController);

                                if (b != null && b)
                                {
                                    int controllerFrequency = controllerClass.getField("destFrequency").getInt(launchController);
                                    boolean foundPad = this.setTarget(false, controllerFrequency);

                                    if (foundPad)
                                    {
                                        this.destinationFrequency = controllerFrequency;
                                        LSLog.debug("Rocket under launch control: going to target frequency " + controllerFrequency);
                                        return true;
                                    }
                                }
                            }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }

        this.destinationFrequency = -1;
        return false;
    }

    protected boolean setTarget(boolean doSet, int destFreq)
    {
        //Server instance can sometimes be null on a single player game switched to LAN mode
        if (FMLCommonHandler.instance().getMinecraftServerInstance() == null || FMLCommonHandler.instance().getMinecraftServerInstance().worldServers == null || controllerClass == null)
        {
            return false;
        }

        WorldServer[] servers = FMLCommonHandler.instance().getMinecraftServerInstance().worldServers;

        for (int i = 0; i < servers.length; i++)
        {
            WorldServer world = servers[i];

            try
            {
                for (TileEntity tile : new ArrayList<TileEntity>(world.loadedTileEntityList))
                {
                    if (!controllerClass.isInstance(tile))
                        continue;

                    tile = world.getTileEntity(tile.getPos());
                    if (!controllerClass.isInstance(tile))
                			continue;

                		int controllerFrequency = controllerClass.getField("frequency").getInt(tile);

                		if (destFreq == controllerFrequency)
                		{
                			boolean targetSet = false;

                			blockLoop:
                				for (int x = -2; x <= 2; x++)
                				{
                					for (int z = -2; z <= 2; z++)
                					{
                                        BlockPos pos = new BlockPos(tile.getPos().add(x, 0, z));
                						Block block = world.getBlockState(pos).getBlock();

                						if (block instanceof BlockLandingPadFull)
                						{
                							if (doSet)
                							{
                								this.targetVec = pos;
                							}

                							targetSet = true;
                							break blockLoop;
                						}
                					}
                				}

                			if (doSet)
                			{
                				this.targetDimension = tile.getWorld().provider.getDimension();
                			}

                			if (!targetSet)
                			{
                				if (doSet)
                				{
                					this.targetVec = null;
                				}

                				return false;
                			}
                			else
                			{
                				return true;
                			}
                		}
                	}
                }
            catch (Exception e)
            {
            	e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public int getScaledFuelLevel(int scale)
    {
        if (this.getFuelTankCapacity() <= 0)
        {
            return 0;
        }

        return this.fuelTank.getFluidAmount() * scale / this.getFuelTankCapacity() / ConfigManagerCore.rocketFuelFactor;
    }

    @Override
    public void onUpdate()
    {
        if (this.landing && this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
        {
            if (this.targetVec != null)
            {
                double yDiff = this.posY - this.getOnPadYOffset() - this.targetVec.getY();
                this.motionY = Math.max(-2.0F, (yDiff - 0.05D) / -70.0D);

                if (yDiff > 1F && yDiff < 4F)
                {
                    for (Object o : this.worldObj.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox().offset(0D, -3D, 0D), EntitySpaceshipBase.rocketSelector))
                    {
                        if (o instanceof EntitySpaceshipBase)
                        {
                            ((EntitySpaceshipBase)o).dropShipAsItem();
                            ((EntitySpaceshipBase)o).setDead();
                        }
                    }
                }
                if (yDiff < 0.06F)
                {
                    int yMin = MathHelper.floor_double(this.getEntityBoundingBox().minY - this.getOnPadYOffset() - 0.45D) - 2;
                    int yMax = MathHelper.floor_double(this.getEntityBoundingBox().maxY) + 1;
                    int zMin = MathHelper.floor_double(this.posZ) - 1;
                    int zMax = MathHelper.floor_double(this.posZ) + 1;
                    for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                    {
                        for (int z = zMin; z <= zMax; z++)
                        {
                            //Doing y as the inner loop may help with cacheing of chunks
                            for (int y = yMin; y <= yMax; y++)
                            {
                                if (this.worldObj.getTileEntity(new BlockPos(x, y, z)) instanceof IFuelDock)
                                {
                                    //Land the rocket on the pad found
                                    this.failRocket();
                                }
                            }
                        }
                    }
                }
            }
        }

        super.onUpdate();

        if (!this.worldObj.isRemote)
        {
            if (this.statusMessageCooldown > 0)
            {
                this.statusMessageCooldown--;
            }

            if (this.statusMessageCooldown == 0 && this.lastStatusMessageCooldown > 0 && this.statusValid)
            {
                this.autoLaunch();
            }

            if (this.autoLaunchCountdown > 0 && !this.getPassengers().isEmpty())
            {
                if (--this.autoLaunchCountdown <= 0)
                {
                    this.autoLaunch();
                }
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.ROCKET_IS_FUELED && this.fuelTank.getFluidAmount() == this.fuelTank.getCapacity()  && !this.getPassengers().isEmpty())
            {
                this.autoLaunch();
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.INSTANT)
            {
                if (this.autoLaunchCountdown == 0 && !this.getPassengers().isEmpty())
                {
                    this.autoLaunch();
                }
            }

            if (this.autoLaunchSetting == EnumAutoLaunch.REDSTONE_SIGNAL)
            {
                if (this.ticks % 11 == 0 && this.activeLaunchController != null)
                                    {
                    if (RedstoneUtil.isBlockReceivingRedstone(this.worldObj, this.activeLaunchController.toBlockPos()))
                                    {
                                        this.autoLaunch();
                                    }
                                }
            }

            if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
            {
                this.setPad(null);
            }
            else
            {
                if (this.launchPhase == EnumLaunchPhase.UNIGNITED.ordinal() && this.landingPad != null && this.ticks % 17 == 0)
                {
                    this.updateControllerSettings(this.landingPad);
                }
            }

            this.lastStatusMessageCooldown = this.statusMessageCooldown;          
        }
        
        if (this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() || this.getLaunched())
        {
	        if (this.rocketSoundUpdater != null)
	        {
	            this.rocketSoundUpdater.update();
	            this.rocketSoundToStop = true;
	        }
        }
        else
        {
        	//Not ignited - either because not yet launched, or because it has landed
        	if (this.rocketSoundToStop)
        		this.stopRocketSound();
        }
    }

    @Override
    protected boolean shouldMoveClientSide()
    {
        return false;
    }

    //Server side only - this is a Launch Controlled ignition attempt
    private void autoLaunch()
    {
        if (this.autoLaunchSetting != null)
        {
            if (this.activeLaunchController != null)
            {
                TileEntity tile = this.activeLaunchController.getTileEntity(this.worldObj);

                if (controllerClass.isInstance(tile))
                {
                    Boolean autoLaunchEnabled = null;
                    try
                    {
                        autoLaunchEnabled = controllerClass.getField("controlEnabled").getBoolean(tile);
                    } catch (Exception e) { }

                    if (autoLaunchEnabled != null && autoLaunchEnabled)
                    {
                        if (this.fuelTank.getFluidAmount() > this.fuelTank.getCapacity() * 2 / 5)
        this.ignite();
                        else
                            this.failMessageInsufficientFuel();
                    }
                    else
                    {
                        this.failMessageLaunchController();
                    }
                }            
            }
            this.autoLaunchSetting = null;

            return;
        }
        else
        {
            this.ignite();
        }
    }

    public boolean igniteWithResult()
    {
        if (this.setFrequency())
        {
            super.ignite();
            this.activeLaunchController = null;
            return true;
        }
        else
        {
            if (this.isPlayerRocket())
            {
                super.ignite();
                this.activeLaunchController = null;
                return true;
            }

            this.activeLaunchController = null;
            return false;
        }
    }

    @Override
    public void ignite()
    {
        this.igniteWithResult();
    }

    public abstract boolean isPlayerRocket();

    @Override
    public void landEntity(BlockPos pos)
    {
        TileEntity tile = this.worldObj.getTileEntity(pos);

        if (tile instanceof IFuelDock)
        {
            IFuelDock dock = (IFuelDock) tile;

            if (this.isDockValid(dock))
            {
                if (!this.worldObj.isRemote)
                {
                    //Drop any existing rocket on the landing pad
                	if (dock.getDockedEntity() instanceof EntitySpaceshipBase && dock.getDockedEntity() != this)
                    {
                    	((EntitySpaceshipBase)dock.getDockedEntity()).dropShipAsItem();
                    	((EntitySpaceshipBase)dock.getDockedEntity()).setDead();
                    }
                	
                    this.setPad(dock);
                }

                this.onRocketLand(pos);
            }
        }
    }

    public void updateControllerSettings(IFuelDock dock)
    {
        HashSet<ILandingPadAttachable> connectedTiles = dock.getConnectedTiles();

        try
        {
            for (ILandingPadAttachable updatedTile : connectedTiles)
            {
                if (controllerClass.isInstance(updatedTile))
                {
                    //This includes a check for whether it has enough energy to run (if it doesn't, then a launch would not go to the destination frequency and the rocket would be lost!)
                    Boolean autoLaunchEnabled = controllerClass.getField("controlEnabled").getBoolean(updatedTile);

                    this.activeLaunchController = new BlockVec3((TileEntity) updatedTile);
                    
                    if (autoLaunchEnabled)
                    {
                        this.autoLaunchSetting = EnumAutoLaunch.values()[controllerClass.getField("launchDropdownSelection").getInt(updatedTile)];

                        switch (this.autoLaunchSetting)
                        {
                        case INSTANT:
                            //Small countdown to give player a moment to exit the Launch Controller GUI
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 12) this.autoLaunchCountdown = 12;
                            break;
                            //The other settings set time to count down AFTER player mounts rocket but BEFORE engine ignition
                            //TODO: if autoLaunchCountdown > 0 add some smoke (but not flame) particle effects or other pre-flight test feedback so the player knows something is happening
                        case TIME_10_SECONDS:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 200)
                            this.autoLaunchCountdown = 200;
                            break;
                        case TIME_30_SECONDS:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 600) this.autoLaunchCountdown = 600;
                            break;
                        case TIME_1_MINUTE:
                            if (this.autoLaunchCountdown <= 0 || this.autoLaunchCountdown > 1200) this.autoLaunchCountdown = 1200;
                            break;
                        default:
                            break;
                        }
                    }
                    else
                    {
                        //This LaunchController is out of power, disabled, invalid target or set not to launch
                        //No auto launch - but maybe another connectedTile will have some launch settings?
                        this.autoLaunchSetting = null;
                        this.autoLaunchCountdown = 0;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    protected void onRocketLand(BlockPos pos)
    {
        this.setPositionAndRotation(pos.getX() + 0.5, pos.getY() + 0.4D + this.getOnPadYOffset(), pos.getZ() + 0.5, this.rotationYaw, 0.0F);
        this.stopRocketSound();
    }
    
    public void stopRocketSound()
    {
        if (this.rocketSoundUpdater != null)
        {
        	((SoundUpdaterRocket) this.rocketSoundUpdater).stopRocketSound();
        }
        this.rocketSoundToStop = false;
    }

    @Override
    public void setDead()
    {
        super.setDead();

        if (this.rocketSoundUpdater != null)
        {
            this.rocketSoundUpdater.update();
        }
    }
    
    @Override
    public void decodePacketdata(ByteBuf buffer)
    {
        super.decodePacketdata(buffer);
        this.fuelTank.setFluid(new FluidStack(LabStuffMain.kerosene, buffer.readInt()));
        this.landing = buffer.readBoolean();
        this.destinationFrequency = buffer.readInt();

        if (buffer.readBoolean())
        {
            this.targetVec = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
        }

        this.motionX = buffer.readDouble() / 8000.0D;
        this.motionY = buffer.readDouble() / 8000.0D;
        this.motionZ = buffer.readDouble() / 8000.0D;
        this.lastMotionY = buffer.readDouble() / 8000.0D;
        this.lastLastMotionY = buffer.readDouble() / 8000.0D;

        if (this.cargoItems == null)
        {
            this.cargoItems = new ItemStack[this.getSizeInventory()];
        }

        this.setWaitForPlayer(buffer.readBoolean());

        this.statusMessage = ByteBufUtils.readUTF8String(buffer);
        this.statusMessage = this.statusMessage.equals("") ? null : this.statusMessage;
        this.statusMessageCooldown = buffer.readInt();
        this.lastStatusMessageCooldown = buffer.readInt();
        this.statusValid = buffer.readBoolean();

        //Update client with correct rider if needed
        if (this.worldObj.isRemote)
        {
	        int shouldBeMountedId = buffer.readInt();
	        if (this.getPassengers().isEmpty())
	        {
	        	 if (shouldBeMountedId > -1)
	        	 {
	        		 Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
	        		 if (e != null)
	        		 {
	        			 if (e.dimension != this.dimension)
	        			 {
	        				 if (e instanceof EntityPlayer)
	        				 {
	        					 e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.getDifficulty().getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP)e).interactionManager.getGameType().getID());
	        					 e.startRiding(this);
	        				 }
	        			 }
	        			 else
	        				 e.startRiding(this);
	        		 }
	        	 }
	        }
            else if (this.getPassengers().get(0).getEntityId() != shouldBeMountedId)
	        {
	        	if (shouldBeMountedId == -1)
	        	{
	        	    this.removePassengers();
	        	}
	        	else
	        	{
	        		Entity e = FMLClientHandler.instance().getWorldClient().getEntityByID(shouldBeMountedId);
	       		 	if (e != null)
	       		 	{
	       		 		if (e.dimension != this.dimension)
	       		 		{
	       		 			if (e instanceof EntityPlayer)
	       		 			{
	       		 				e = WorldUtil.forceRespawnClient(this.dimension, e.worldObj.getDifficulty().getDifficultyId(), e.worldObj.getWorldInfo().getTerrainType().getWorldTypeName(), ((EntityPlayerMP)e).interactionManager.getGameType().getID());
	       		 				e.startRiding(this);
	       		 			}
	       		 		}
	       		 		else
	       		 			e.startRiding(this);
	       		 	}
	        	}
	        }
        }
        this.statusColour = ByteBufUtils.readUTF8String(buffer);
        if (this.statusColour.equals("")) this.statusColour = null;
    }

    @Override
    public void getNetworkedData(ArrayList<Object> list)
    {
        super.getNetworkedData(list);

        list.add(this.fuelTank.getFluidAmount());
        list.add(this.landing);
        list.add(this.destinationFrequency);
        list.add(this.targetVec != null);

        if (this.targetVec != null)
        {
            list.add(this.targetVec.getX());
            list.add(this.targetVec.getY());
            list.add(this.targetVec.getZ());
        }

        list.add(this.motionX * 8000.0D);
        list.add(this.motionY * 8000.0D);
        list.add(this.motionZ * 8000.0D);
        list.add(this.lastMotionY * 8000.0D);
        list.add(this.lastLastMotionY * 8000.0D);

        list.add(this.getWaitForPlayer());

        list.add(this.statusMessage != null ? this.statusMessage : "");
        list.add(this.statusMessageCooldown);
        list.add(this.lastStatusMessageCooldown);
        list.add(this.statusValid);
        
        if (!this.worldObj.isRemote)
        {
        	list.add(this.getPassengers().isEmpty() ? -1 : this.getPassengers().get(0).getEntityId());
        }
        list.add(this.statusColour != null ? this.statusColour : "");
    }

    @Override
    protected void failRocket()
    {
        if (this.shouldCancelExplosion())
        {
            //TODO: why looking around when already know the target?
            //TODO: it would be good to land on an alternative neighbouring pad if there is already a rocket on the target pad
            for (int i = -3; i <= 3; i++)
            {
                BlockPos pos = new BlockPos((int) Math.floor(this.posX), (int) Math.floor(this.posY + i), (int) Math.floor(this.posZ));
                if (this.landing && this.targetVec != null && this.worldObj.getTileEntity(pos) instanceof IFuelDock && this.posY - this.targetVec.getY() < 5)
                {
                    for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
                    {
                        for (int y = MathHelper.floor_double(this.posY - 3.0D); y <= MathHelper.floor_double(this.posY) + 1; y++)
                        {
                            for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                            {
                                BlockPos pos1 = new BlockPos(x, y, z);
                                TileEntity tile = this.worldObj.getTileEntity(pos1);

                                if (tile instanceof IFuelDock)
                                {
                                    this.landEntity(pos1);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal())
        {
            super.failRocket();
        }
    }

    protected boolean shouldCancelExplosion()
    {
        return this.hasValidFuel();
    }

    public boolean hasValidFuel()
    {
        return this.fuelTank.getFluidAmount() > 0;
    }

    public void cancelLaunch()
    {
        this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
        this.timeUntilLaunch = 0;
        if (!this.worldObj.isRemote && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof EntityPlayerMP)
        {
            this.getPassengers().get(0).addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.rocket.warning.nogyroscope")));
        }
    }
    
    public void failMessageLaunchController()
    {
        if (!this.worldObj.isRemote && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) this.getPassengers().get(0)).addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.rocket.warning.launchcontroller")));
        }
    }

    public void failMessageInsufficientFuel()
    {
        if (!this.worldObj.isRemote && !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP) this.getPassengers().get(0)).addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.rocket.warning.fuelinsufficient")));
        }
    }

    @Override
    public void onLaunch()
    {
        if (!(this.worldObj.provider.getDimension() == LabStuffMain.planetOverworld.getDimensionID() || this.worldObj.provider instanceof ILabstuffWorldProvider))
        {
        	
            //No rocket flight in the Nether, the End etc
        	for (int i = ConfigManagerCore.disableRocketLaunchDimensions.length - 1; i >= 0; i--)
            {
                if (ConfigManagerCore.disableRocketLaunchDimensions[i] == this.worldObj.provider.getDimension())
                {
                	this.cancelLaunch();
                    return;
                }
            }

        }

        super.onLaunch();

        if (!this.worldObj.isRemote)
        {
        	LSPlayerStats stats = null;
        	
        	if (!this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) this.getPassengers().get(0);
                stats = LSPlayerStats.get(player);

                if (!(this.worldObj.provider instanceof IOrbitDimension))
                {
	                stats.setCoordsTeleportedFromX(player.posX);
	                stats.setCoordsTeleportedFromZ(player.posZ);
                }
            }

            int amountRemoved = 0;

            PADSEARCH:
            for (int x = MathHelper.floor_double(this.posX) - 1; x <= MathHelper.floor_double(this.posX) + 1; x++)
            {
                for (int y = MathHelper.floor_double(this.posY) - 3; y <= MathHelper.floor_double(this.posY) + 1; y++)
                {
                    for (int z = MathHelper.floor_double(this.posZ) - 1; z <= MathHelper.floor_double(this.posZ) + 1; z++)
                    {
                        BlockPos pos = new BlockPos(x, y, z);
                        final Block block = this.worldObj.getBlockState(pos).getBlock();

                        if (block != null && block instanceof BlockLandingPadFull)
                        {
                            if (amountRemoved < 9)
                            {
                                EventLandingPadRemoval event = new EventLandingPadRemoval(this.worldObj, pos);
                                MinecraftForge.EVENT_BUS.post(event);

                                if (event.allow)
                                {
                                    this.worldObj.setBlockToAir(pos);
                                    amountRemoved = 9;
                                }
                                break PADSEARCH;
                            }
                        }
                    }
                }
            }

            //Set the player's launchpad item for return on landing - or null if launchpads not removed
            if (stats != null)
            {
                stats.setLaunchpadStack(amountRemoved == 9 ? new ItemStack(LabStuffMain.landingPad, 9, 0) : null);
            }

            this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);

        if (this.fuelTank.getFluid() != null)
        {
            nbt.setTag("fuelTank", this.fuelTank.writeToNBT(new NBTTagCompound()));
        }

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = new NBTTagList();

            for (int var3 = 0; var3 < this.cargoItems.length; ++var3)
            {
                if (this.cargoItems[var3] != null)
                {
                    final NBTTagCompound var4 = new NBTTagCompound();
                    var4.setByte("Slot", (byte) var3);
                    this.cargoItems[var3].writeToNBT(var4);
                    var2.appendTag(var4);
                }
            }

            nbt.setTag("Items", var2);
        }

        nbt.setBoolean("TargetValid", this.targetVec != null);

        if (this.targetVec != null)
        {
            nbt.setDouble("targetTileX", this.targetVec.getX());
            nbt.setDouble("targetTileY", this.targetVec.getY());
            nbt.setDouble("targetTileZ", this.targetVec.getZ());
        }

        nbt.setBoolean("WaitingForPlayer", this.getWaitForPlayer());
        nbt.setBoolean("Landing", this.landing);
        nbt.setInteger("AutoLaunchSetting", this.autoLaunchSetting != null ? this.autoLaunchSetting.getIndex() : -1);
        nbt.setInteger("TimeUntilAutoLaunch", this.autoLaunchCountdown);
        nbt.setInteger("DestinationFrequency", this.destinationFrequency);
        if (this.activeLaunchController != null) this.activeLaunchController.writeToNBT(nbt,"ALCat");
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        if (nbt.hasKey("fuelTank"))
        {
            this.fuelTank.readFromNBT(nbt.getCompoundTag("fuelTank"));
        }

        if (this.getSizeInventory() > 0)
        {
            final NBTTagList var2 = nbt.getTagList("Items", 10);
            this.cargoItems = new ItemStack[this.getSizeInventory()];

            for (int var3 = 0; var3 < var2.tagCount(); ++var3)
            {
                final NBTTagCompound var4 = var2.getCompoundTagAt(var3);
                final int var5 = var4.getByte("Slot") & 255;

                if (var5 < this.cargoItems.length)
                {
                    this.cargoItems[var5] = ItemStack.loadItemStackFromNBT(var4);
                }
            }
        }

        if (nbt.getBoolean("TargetValid") && nbt.hasKey("targetTileX"))
        {
            this.targetVec = new BlockPos(MathHelper.floor_double(nbt.getDouble("targetTileX")), MathHelper.floor_double(nbt.getDouble("targetTileY")), MathHelper.floor_double(nbt.getDouble("targetTileZ")));
        }

        this.setWaitForPlayer(nbt.getBoolean("WaitingForPlayer"));
        this.landing = nbt.getBoolean("Landing");
        int autoLaunchValue = nbt.getInteger("AutoLaunchSetting");
        this.autoLaunchSetting = autoLaunchValue == -1 ? null : EnumAutoLaunch.values()[autoLaunchValue];
        this.autoLaunchCountdown = nbt.getInteger("TimeUntilAutoLaunch");
        this.destinationFrequency = nbt.getInteger("DestinationFrequency");
        this.activeLaunchController = BlockVec3.readFromNBT(nbt, "ALCat");
    }

    @Override
    public int addFuel(FluidStack liquid, boolean doFill)
    {
    	return FluidUtil.fillWithLSFuel(this.fuelTank, liquid, doFill);
    }

    @Override
    public FluidStack removeFuel(int amount)
    {
        return this.fuelTank.drain(amount * ConfigManagerCore.rocketFuelFactor, true);
    }

    @Override
    public void setPad(IFuelDock pad)
    {
        //Called either when a rocket lands or when one is placed
    	//Can also be called with null param when rocket leaves a pad
        this.landingPad = pad;
        if (pad != null)
        {
            pad.dockEntity(this);
            //NOTE: setPad() is called also when a world or chunk is loaded - if the rocket is Ignited (from NBT save data) do not change those settings
            if (!(this.launchPhase == EnumLaunchPhase.IGNITED.ordinal()))
            {
                this.setLaunchPhase(EnumLaunchPhase.UNIGNITED);
                this.targetVec = null;
                this.updateControllerSettings(pad);
            }
	        this.landing = false;
        }
    }

    @Override
    public IFuelDock getLandingPad()
    {
        return this.landingPad;
    }

    @Override
    public int getMaxFuel()
    {
        return this.fuelTank.getCapacity();
    }

    @Override
    public boolean isDockValid(IFuelDock dock)
    {
        return (dock instanceof TileEntityLandingPad);
    }

    @Override
    public EnumCargoLoadingState addCargo(ItemStack stack, boolean doAdd)
    {
        if (this.getSizeInventory() <= 3)
        {
            if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
            {
                this.autoLaunch();
            }

            return EnumCargoLoadingState.NOINVENTORY;
        }

        int count = 0;

        for (count = 0; count < this.cargoItems.length - 2; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt != null && stackAt.getItem() == stack.getItem() && stackAt.getItemDamage() == stack.getItemDamage() && stackAt.stackSize < stackAt.getMaxStackSize())
            {
                if (stackAt.stackSize + stack.stackSize <= stackAt.getMaxStackSize())
                {
                    if (doAdd)
                    {
                        this.cargoItems[count].stackSize += stack.stackSize;
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
                        this.cargoItems[count].stackSize = stackAt.getMaxStackSize();
                        this.markDirty();
                    }

                    stack.stackSize = surplus;
                    if (this.addCargo(stack, doAdd) == EnumCargoLoadingState.SUCCESS)
                    {
                        return EnumCargoLoadingState.SUCCESS;
                    }

                    this.cargoItems[count].stackSize = origSize;
                    if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
                    {
                        this.autoLaunch();
                    }
                    return EnumCargoLoadingState.FULL;
                }
            }
        }

        for (count = 0; count < this.cargoItems.length - 2; count++)
        {
            ItemStack stackAt = this.cargoItems[count];

            if (stackAt == null)
            {
                if (doAdd)
                {
                    this.cargoItems[count] = stack;
                    this.markDirty();
                }

                return EnumCargoLoadingState.SUCCESS;
            }
        }

        if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_FULL)
        {
            this.autoLaunch();
        }

        return EnumCargoLoadingState.FULL;
    }

    @Override
    public RemovalResult removeCargo(boolean doRemove)
    {
        for (int i = 0; i < this.cargoItems.length - 2; i++)
        {
            ItemStack stackAt = this.cargoItems[i];

            if (stackAt != null)
            {
                ItemStack resultStack = stackAt.copy();
                resultStack.stackSize = 1;

            	if (doRemove && --stackAt.stackSize <= 0)
                {
                    this.cargoItems[i] = null;
                }

                if (doRemove)
                {
                    this.markDirty();
                }
                return new RemovalResult(EnumCargoLoadingState.SUCCESS, resultStack);
            }
        }

        if (this.autoLaunchSetting == EnumAutoLaunch.CARGO_IS_UNLOADED)
        {
            this.autoLaunch();
        }

        return new RemovalResult(EnumCargoLoadingState.EMPTY, null);
    }

    @Override
    public ItemStack getStackInSlot(int par1)
    {
        if (this.cargoItems == null) return null; 
        
        return this.cargoItems[par1];
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
        if (this.cargoItems[par1] != null)
        {
            ItemStack var3;

            if (this.cargoItems[par1].stackSize <= par2)
            {
                var3 = this.cargoItems[par1];
                this.cargoItems[par1] = null;
                return var3;
            }
            else
            {
                var3 = this.cargoItems[par1].splitStack(par2);

                if (this.cargoItems[par1].stackSize == 0)
                {
                    this.cargoItems[par1] = null;
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
        if (this.cargoItems[par1] != null)
        {
            final ItemStack var2 = this.cargoItems[par1];
            this.cargoItems[par1] = null;
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
        this.cargoItems[par1] = par2ItemStack;

        if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
        {
            par2ItemStack.stackSize = this.getInventoryStackLimit();
        }
    }

    @Override
    public String getName()
    {
        return LabStuffUtils.translate("container.spaceship.name");
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {
    }

    @Override
    public void closeInventory(EntityPlayer player)
    {
    }

    @Override
    public boolean hasCustomName()
    {
        return true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return !this.isDead && entityplayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void markDirty()
    {
    }

    @Override
    public void onPadDestroyed()
    {
        if (!this.isDead && this.launchPhase != EnumLaunchPhase.LAUNCHED.ordinal())
        {
            this.dropShipAsItem();
            this.setDead();
        }
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItemList)
    {
        if (this.cargoItems != null)
        {
            for (final ItemStack item : this.cargoItems)
            {
                if (item != null)
                {
                    droppedItemList.add(item);
                }
            }
        }

        return droppedItemList;
    }

    public boolean getWaitForPlayer()
    {
        return this.waitForPlayer;
    }

    public void setWaitForPlayer(boolean waitForPlayer)
    {
        this.waitForPlayer = waitForPlayer;
    }

    public static enum EnumAutoLaunch
    {
        CARGO_IS_UNLOADED(0, "cargo_unloaded"),
        CARGO_IS_FULL(1, "cargo_full"),
        ROCKET_IS_FUELED(2, "fully_fueled"),
        INSTANT(3, "instant"),
        TIME_10_SECONDS(4, "ten_sec"),
        TIME_30_SECONDS(5, "thirty_sec"),
        TIME_1_MINUTE(6, "one_min"),
        REDSTONE_SIGNAL(7, "redstone_sig");

        private final int index;
        private String title;

        private EnumAutoLaunch(int index, String title)
        {
            this.index = index;
            this.title = title;
        }

        public int getIndex()
        {
            return this.index;
        }

        public String getTitle()
        {
            return LabStuffUtils.translate("gui.message." + this.title + ".name");
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ITickable getSoundUpdater()
    {
    	return this.rocketSoundUpdater;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ISound setSoundUpdater(EntityPlayerSP player)
    {
    	this.rocketSoundUpdater = new SoundUpdaterRocket(player, this);
    	return (ISound) this.rocketSoundUpdater;
    }
}