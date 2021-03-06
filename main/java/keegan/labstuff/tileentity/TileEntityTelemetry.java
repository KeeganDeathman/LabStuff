package keegan.labstuff.tileentity;

import java.util.*;

import com.mojang.authlib.GameProfile;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.entities.*;
import keegan.labstuff.util.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.*;

public class TileEntityTelemetry extends TileEntity implements ITickable
{
    public Class clientClass;
    public int[] clientData = { -1 };
    public String clientName;
    public GameProfile clientGameProfile = null;

    public static HashSet<BlockVec3Dim> loadedList = new HashSet<BlockVec3Dim>();
    public Entity linkedEntity;
    private UUID toUpdate = null;
    private int pulseRate = 400;
    private int lastHurttime = 0;
    private int ticks = 0;

    @Override
    public void validate()
    {
        super.validate();
        if (this.worldObj.isRemote)
        {
            loadedList.add(new BlockVec3Dim(this));
        }
    }

    @Override
    public void invalidate()
    {
        super.invalidate();
        if (this.worldObj.isRemote)
        {
            loadedList.remove(new BlockVec3Dim(this));
        }
    }

    @Override
    public void update()
    {
        if (!this.worldObj.isRemote && ++this.ticks % 2 == 0)
        {
            if (this.toUpdate != null)
            {
                this.addTrackedEntity(this.toUpdate);
                this.toUpdate = null;
            }

            String name;
            int[] data = { -1, -1, -1, -1, -1 };
            String strUUID = "";

            if (linkedEntity != null)
            {
                //Help the Garbage Collector
                if (linkedEntity.isDead)
                {
                    linkedEntity = null;
                    name = "";
                    //TODO: track players after death and respawn? or not?
                }
                else
                {
                    if (linkedEntity instanceof EntityPlayerMP)
                    {
                        name = "$" + linkedEntity.getName();
                    }
                    else
                    {
                        name = EntityList.CLASS_TO_NAME.get(linkedEntity.getClass());
                    }

                    if (name == null)
                    {
                        LSLog.info("Telemetry Unit: Error finding name for " + linkedEntity.getClass().getSimpleName());
                        name = "";
                    }

                    double xmotion = linkedEntity.motionX;
                    double ymotion = linkedEntity instanceof EntityLivingBase ? linkedEntity.motionY + 0.078D : linkedEntity.motionY;
                    double zmotion = linkedEntity.motionZ;
                    data[2] = (int) (MathHelper.sqrt_double(xmotion * xmotion + ymotion * ymotion + zmotion * zmotion) * 2000D);

                    if (linkedEntity instanceof ITelemetry)
                    {
                        ((ITelemetry) linkedEntity).transmitData(data);
                    }
                    else if (linkedEntity instanceof EntityLivingBase)
                    {
                        EntityLivingBase eLiving = (EntityLivingBase) linkedEntity;
                        data[0] = eLiving.hurtTime;

                        //Calculate a "pulse rate" based on motion and taking damage
                        this.pulseRate--;
                        if (eLiving.hurtTime > this.lastHurttime)
                        {
                            this.pulseRate += 100;
                        }
                        this.lastHurttime = eLiving.hurtTime;
                        if (eLiving.getRidingEntity() != null)
                        {
                            data[2] /= 4;  //reduced pulse effect if riding a vehicle
                        }
                        else if (data[2] > 1)
                        {
                            this.pulseRate += 2;
                        }
                        this.pulseRate += Math.max(data[2] - pulseRate, 0) / 4;
                        if (this.pulseRate > 2000)
                        {
                            this.pulseRate = 2000;
                        }
                        if (this.pulseRate < 400)
                        {
                            this.pulseRate = 400;
                        }
                        data[2] = this.pulseRate / 10;

                        data[1] = (int) (eLiving.getHealth() * 100 / eLiving.getMaxHealth());
                        if (eLiving instanceof EntityPlayerMP)
                        {
                            data[3] = ((EntityPlayerMP) eLiving).getFoodStats().getFoodLevel() * 5;
                            LSPlayerStats stats = LSPlayerStats.get(eLiving);
                            data[4] = stats.getAirRemaining() * 4096 + stats.getAirRemaining2();
                            UUID uuid = ((EntityPlayerMP) eLiving).getUniqueID();
                            if (uuid != null)
                            {
                                strUUID = uuid.toString();
                            }
                        }
                        else if (eLiving instanceof EntityHorse)
                        {
                            data[3] = ((EntityHorse) eLiving).getType().ordinal();
                            data[4] = ((EntityHorse) eLiving).getHorseVariant();
                        }
                        else if (eLiving instanceof EntityVillager)
                        {
                            data[3] = ((EntityVillager) eLiving).getProfession();
                            data[4] = ((EntityVillager) eLiving).getGrowingAge();
                        }
                        else if (eLiving instanceof EntityWolf)
                        {
                            data[3] = ((EntityWolf) eLiving).getCollarColor().getDyeDamage();
                            data[4] = ((EntityWolf) eLiving).isBegging() ? 1 : 0;
                        }
                        else if (eLiving instanceof EntitySheep)
                        {
                            data[3] = ((EntitySheep) eLiving).getFleeceColor().getDyeDamage();
                            data[4] = ((EntitySheep) eLiving).getSheared() ? 1 : 0;
                        }
                        else if (eLiving instanceof EntityOcelot)
                        {
                            data[3] = ((EntityOcelot) eLiving).getTameSkin();
                        }
                        else if (eLiving instanceof EntitySkeleton)
                        {
                            data[3] = ((EntitySkeleton) eLiving).func_189771_df().ordinal();
                        }
                        else if (eLiving instanceof EntityZombie)
                        {
//                            data[3] = ((EntityZombie) eLiving).isVillager() ? 1 : 0; TODO Fix for MC 1.10
                            data[4] = ((EntityZombie) eLiving).isChild() ? 1 : 0;
                        }
                    }
                }
            }
            else
            {
                name = "";
            }
            LabStuffMain.packetPipeline.sendToAllAround(new PacketSimple(EnumSimplePacket.C_UPDATE_TELEMETRY, this.worldObj.provider.getDimension(), new Object[] { this.getPos(), name, data[0], data[1], data[2], data[3], data[4], strUUID }), new TargetPoint(this.worldObj.provider.getDimension(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 320D));
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void receiveUpdate(List <Object> data, int dimID)
    {
        String name = (String) data.get(1);
        if (name.startsWith("$"))
        {
            //It's a player name
            this.clientClass = EntityPlayerMP.class;
            String strName = name.substring(1);
            this.clientName = strName;
            this.clientGameProfile = PlayerUtil.getSkinForName(strName, (String) data.get(7), dimID);
        }
        else
        {
            this.clientClass = EntityList.NAME_TO_CLASS.get(name);
        }
        this.clientData = new int[5];
        for (int i = 2; i < 7; i++)
        {
            this.clientData[i - 2] = (Integer) data.get(i);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        Long msb = nbt.getLong("entityUUIDMost");
        Long lsb = nbt.getLong("entityUUIDLeast");
        this.toUpdate = new UUID(msb, lsb);
    }



    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        if (this.linkedEntity != null && !this.linkedEntity.isDead)
        {
            nbt.setLong("entityUUIDMost", this.linkedEntity.getUniqueID().getMostSignificantBits());
            nbt.setLong("entityUUIDLeast", this.linkedEntity.getUniqueID().getLeastSignificantBits());
        }
        return nbt;
    }

    public void addTrackedEntity(UUID uuid)
    {
        this.pulseRate = 400;
        this.lastHurttime = 0;
        List<Entity> eList = this.worldObj.loadedEntityList;
        for (Entity e : eList)
        {
            if (e.getUniqueID().equals(uuid))
            {
                this.linkedEntity = e;
                if (e instanceof EntitySpaceshipBase)
                {
                    ((EntitySpaceshipBase) e).addTelemetry(this);
                }
                return;
            }
        }
        //TODO Add some kind of watcher to add the entity when next loaded
        this.linkedEntity = null;
    }

    public void addTrackedEntity(Entity e)
    {
        this.pulseRate = 400;
        this.lastHurttime = 0;
        this.linkedEntity = e;
        if (e instanceof EntitySpaceshipBase)
        {
            ((EntitySpaceshipBase) e).addTelemetry(this);
        }
    }

    public void removeTrackedEntity()
    {
        this.pulseRate = 400;
        this.linkedEntity = null;
    }

    public static TileEntityTelemetry getNearest(TileEntity te)
    {
        if (te == null)
        {
            return null;
        }
        BlockVec3 target = new BlockVec3(te);

        int distSq = 1025;
        BlockVec3Dim nearest = null;
        int dim = LabStuffUtils.getDimensionID(te.getWorld());
        for (BlockVec3Dim telemeter : loadedList)
        {
            if (telemeter.dim != dim)
            {
                continue;
            }
            int dist = telemeter.distanceSquared(target);
            if (dist < distSq)
            {
                distSq = dist;
                nearest = telemeter;
            }
        }

        if (nearest == null)
        {
            return null;
        }
        TileEntity result = te.getWorld().getTileEntity(new BlockPos(nearest.x, nearest.y, nearest.z));
        if (result instanceof TileEntityTelemetry)
        {
            return (TileEntityTelemetry) result;
        }
        return null;
    }

    /**
     * Call this when a player wears a frequency module to check
     * whether it has been linked with a Telemetry Unit.
     *
     * @param held   The frequency module
     * @param player
     */
    public static void frequencyModulePlayer(ItemStack held, EntityPlayerMP player)
    {
        if (held == null)
        {
            return;
        }
        NBTTagCompound fmData = held.getTagCompound();
        if (fmData != null && fmData.hasKey("teDim"))
        {
            int dim = fmData.getInteger("teDim");
            int x = fmData.getInteger("teCoordX");
            int y = fmData.getInteger("teCoordY");
            int z = fmData.getInteger("teCoordZ");
            WorldProvider wp = WorldUtil.getProviderForDimensionServer(dim);
            //TODO
            if (wp == null || player.worldObj == null)
            {
                LSLog.debug("Frequency module worn: world provider is null.  This is a bug. " + dim);
            }
            else
            {
                TileEntity te = player.worldObj.getTileEntity(new BlockPos(x, y, z));
                if (te instanceof TileEntityTelemetry)
                {
                    if (player == null)
                    {
                        ((TileEntityTelemetry) te).removeTrackedEntity();
                    }
                    else
                    {
                        ((TileEntityTelemetry) te).addTrackedEntity(player.getUniqueID());
                    }
                }
            }
        }
    }

    public static void updateLinkedPlayer(EntityPlayerMP playerOld, EntityPlayerMP playerNew)
    {
        for (BlockVec3Dim telemeter : loadedList)
        {
			TileEntity te = telemeter.getTileEntityNoLoad();
            if (te instanceof TileEntityTelemetry)
            {
                if (((TileEntityTelemetry) te).linkedEntity == playerOld)
                {
                    ((TileEntityTelemetry) te).linkedEntity = playerNew;
                }
            }
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
}