package keegan.labstuff.dimension;

import java.util.*;

import com.google.common.collect.Lists;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.blocks.BlockSpinThruster;
import keegan.labstuff.client.SkyProviderOrbit;
import keegan.labstuff.common.capabilities.FreefallHandler;
import keegan.labstuff.entities.ITumblable;
import keegan.labstuff.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.*;

public class SpinManager
{
    private static final float GFORCE = 9.81F / 400F; //gravity in metres per tick squared
    public boolean doSpinning = true;
    public float angularVelocityRadians = 0F;
    public float skyAngularVelocity = (float) (this.angularVelocityRadians * 180 / Math.PI);
    public float angularVelocityTarget = 0F;
    public float angularVelocityAccel = 0F;
    public double spinCentreX;
    public double spinCentreZ;
    private float momentOfInertia;
    private float massCentreX;
    private float massCentreZ;
    public int ssBoundsMaxX;
    public int ssBoundsMinX;
    public int ssBoundsMaxY;
    public int ssBoundsMinY;
    public int ssBoundsMaxZ;
    public int ssBoundsMinZ;

    private OrbitSpinSaveData savefile;

    private LinkedList<BlockPos> thrustersPlus = Lists.newLinkedList();
    private LinkedList<BlockPos> thrustersMinus = Lists.newLinkedList();
    private BlockPos oneSSBlock;
    //private HashSet<BlockPos> stationBlocks = new HashSet();

    private HashSet<BlockVec3> checked = new HashSet<BlockVec3>();

    private float artificialG;
    //Used to make continuous particles + thrust sounds at the spin thrusters in this dimension
    //If false, make particles + sounds occasionally in small bursts, just for fun (micro attitude changes)
    //see: BlockSpinThruster.randomDisplayTick()
    public boolean thrustersFiring = false;
    private boolean dataNotLoaded = true;
    private List<Entity> loadedEntities = Lists.newLinkedList();

    private WorldProviderSpaceStation worldProvider;
    private World world;
    private boolean clientSide = true;

    public SpinManager(WorldProviderSpaceStation provider, World world)
    {
        this.worldProvider = provider;
        this.world = world;
    }

    /**
     * Called from WorldProviderOrbit when registering the worldObj
     */
    public void registerServerSide()
    {
        if (!this.world.isRemote)
        {
            this.clientSide = false;
        }
    }
    
    public float getSpinRate()
    {
        return this.skyAngularVelocity;
    }

    /**
     * Sets the spin rate for the dimension in radians per tick For example,
     * 0.031415 would be 1/200 revolution per tick So that would be 1 revolution
     * every 10 seconds
     */
    public void setSpinRate(float angle)
    {
        this.angularVelocityRadians = angle;
        this.skyAngularVelocity = angle * 180F / 3.1415927F;

        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            this.updateSkyProviderSpinRate();
        }
    }

    @SideOnly(Side.CLIENT)
    private void updateSkyProviderSpinRate()
    {
        IRenderHandler sky = this.worldProvider.getSkyRenderer();
        if (sky instanceof SkyProviderOrbit)
        {
            ((SkyProviderOrbit) sky).spinDeltaPerTick = this.skyAngularVelocity;
        }
    }

    public void setSpinRate(float angle, boolean firing)
    {
        this.angularVelocityRadians = angle;
        this.skyAngularVelocity = angle * 180F / 3.1415927F;
        IRenderHandler sky = this.worldProvider.getSkyRenderer();
        if (sky instanceof SkyProviderOrbit)
        {
            ((SkyProviderOrbit) sky).spinDeltaPerTick = this.skyAngularVelocity;
        }
        this.thrustersFiring = firing;
    }

    public void setSpinCentre(double x, double z)
    {
        this.spinCentreX = x;
        this.spinCentreZ = z;
    }

    public void setSpinBox(int mx, int xx, int my, int yy, int mz, int zz)
    {
        this.ssBoundsMinX = mx;
        this.ssBoundsMaxX = xx;
        this.ssBoundsMinY = my;
        this.ssBoundsMaxY = yy;
        this.ssBoundsMinZ = mz;
        this.ssBoundsMaxZ = zz;
    }

    public void removeThruster(BlockPos thruster, boolean positive)
    {
        if (positive)
        {
            this.thrustersPlus.remove(thruster);
        }
        else
        {
            this.thrustersMinus.remove(thruster);
        }
    }

    /**
     * This will check all blocks which are in contact with each other to find
     * the shape of the spacestation. It also finds the centre of mass (to
     * rotate around) and the moment of inertia (how easy/hard this is to
     * rotate).
     * <p/>
     * If placingThruster is true, it will return false if the thruster (at
     * baseBlock) is not in contact with the "existing" spacestation - so the
     * player cannot place thrusters on outlying disconnected blocks and expect
     * them to have an effect.
     * <p/>
     * Note: this check will briefly load, server-side, all chunks which have
     * spacestation blocks in them or 1 block adjacent to those.
     *
     * @param baseBlock
     * @return
     */
    public boolean refresh(BlockPos baseBlock, boolean placingThruster)
    {
        if (this.oneSSBlock == null || this.world.getBlockState(this.oneSSBlock).getBlock().isAir(this.world.getBlockState(this.oneSSBlock), this.world, this.oneSSBlock))
        {
            if (baseBlock != null)
            {
                this.oneSSBlock = baseBlock;
            }
            else
            {
                this.oneSSBlock = new BlockPos(0, 64, 0);
            }
        }

        // Find contiguous blocks using an algorithm like the oxygen sealer one
        List<BlockVec3> currentLayer = new LinkedList<BlockVec3>();
        List<BlockVec3> nextLayer = new LinkedList<BlockVec3>();
        final List<BlockPos> foundThrusters = new LinkedList<BlockPos>();

        this.checked.clear();
        currentLayer.add(new BlockVec3(this.oneSSBlock));
        this.checked.add(new BlockVec3(this.oneSSBlock));
        Block bStart = this.world.getBlockState(this.oneSSBlock).getBlock();
        if (bStart instanceof BlockSpinThruster)
        {
            foundThrusters.add(this.oneSSBlock);
        }

        float thismass = 0.1F; //Mass of a thruster
        float thismassCentreX = 0.1F * this.oneSSBlock.getX();
        float thismassCentreY = 0.1F * this.oneSSBlock.getY();
        float thismassCentreZ = 0.1F * this.oneSSBlock.getZ();
        float thismoment = 0F;
        int thisssBoundsMaxX = this.oneSSBlock.getX();
        int thisssBoundsMinX = this.oneSSBlock.getX();
        int thisssBoundsMaxY = this.oneSSBlock.getY();
        int thisssBoundsMinY = this.oneSSBlock.getY();
        int thisssBoundsMaxZ = this.oneSSBlock.getZ();
        int thisssBoundsMinZ = this.oneSSBlock.getZ();

        while (currentLayer.size() > 0)
        {
            int bits;
            for (BlockVec3 vec : currentLayer)
            {
                bits = vec.sideDoneBits;
                if (vec.x < thisssBoundsMinX)
                {
                    thisssBoundsMinX = vec.x;
                }
                if (vec.y < thisssBoundsMinY)
                {
                    thisssBoundsMinY = vec.y;
                }
                if (vec.z < thisssBoundsMinZ)
                {
                    thisssBoundsMinZ = vec.z;
                }
                if (vec.x > thisssBoundsMaxX)
                {
                    thisssBoundsMaxX = vec.x;
                }
                if (vec.y > thisssBoundsMaxY)
                {
                    thisssBoundsMaxY = vec.y;
                }
                if (vec.z > thisssBoundsMaxZ)
                {
                    thisssBoundsMaxZ = vec.z;
                }

                for (int side = 0; side < 6; side++)
                {
                    if ((bits & (1 << side)) == 1)
                    {
                        continue;
                    }
                    BlockVec3 sideVec = vec.newVecSide(side);

                    if (!this.checked.contains(sideVec))
                    {
                        this.checked.add(sideVec);
                        IBlockState state = sideVec.getBlockState(this.world);
                        Block b = state.getBlock();
                        if (b != null && !b.isAir(this.world.getBlockState(sideVec.toBlockPos()), this.world, sideVec.toBlockPos()))
                        {
                            nextLayer.add(sideVec);
                            if (bStart.isAir(this.world.getBlockState(this.oneSSBlock), this.world, this.oneSSBlock))
                            {
                                this.oneSSBlock = sideVec.toBlockPos();
                                bStart = b;
                            }
                            float m = 1.0F;
                            //Liquids have a mass of 1, stone, metal blocks etc will be heavier
                            if (!(b instanceof BlockLiquid))
                            {
                                //For most blocks, hardness gives a good idea of mass
                                m = b.getBlockHardness(this.world.getBlockState(sideVec.toBlockPos()), this.world, sideVec.toBlockPos());
                                if (m < 0.1F)
                                {
                                    m = 0.1F;
                                }
                                else if (m > 30F)
                                {
                                    m = 30F;
                                }
                                //Wood items have a high hardness compared with their presumed mass
                                if (b.getMaterial(state) == Material.WOOD)
                                {
                                    m /= 4;
                                }

                                //TODO: higher mass for future Galacticraft hi-density item like neutronium
                                //Maybe also check for things in other mods by name: lead, uranium blocks?
                            }
                            thismassCentreX += m * sideVec.x;
                            thismassCentreY += m * sideVec.y;
                            thismassCentreZ += m * sideVec.z;
                            thismass += m;
                            thismoment += m * (sideVec.x * sideVec.x + sideVec.z * sideVec.z);
                            if (b instanceof BlockSpinThruster && !RedstoneUtil.isBlockReceivingRedstone(this.world, sideVec.toBlockPos()))
                            {
                                foundThrusters.add(sideVec.toBlockPos());
                            }
                        }
                    }
                }
            }

            currentLayer = nextLayer;
            nextLayer = new LinkedList<BlockVec3>();
        }

        if (placingThruster && !this.checked.contains(new BlockVec3(baseBlock)))
        {
            if (foundThrusters.size() > 0)
            {
                //The thruster was not placed on the existing contiguous space station: it must be.
                return false;
            }

            //No thruster on the original space station - so assume the player made new station and start check again
            //This offers players a reset option: just remove all thrusters from original station then starting adding to new one
            //(This first check prevents an infinite loop)
            if (!this.oneSSBlock.equals(baseBlock))
            {
                this.oneSSBlock = baseBlock;
                IBlockState state = this.world.getBlockState(this.oneSSBlock);
                if (state.getBlock().getMaterial(state) != Material.AIR)
                {
                    return this.refresh(baseBlock, true);
                }
            }

            return false;

        }

        // Update thruster lists based on what was found
        this.thrustersPlus.clear();
        this.thrustersMinus.clear();
        for (BlockPos thruster : foundThrusters)
        {
            IBlockState state = this.world.getBlockState(thruster);
            int facing = state.getBlock().getMetaFromState(state) & 8;
            if (facing == 0)
            {
                this.thrustersPlus.add(thruster);
            }
            else
            {
                this.thrustersMinus.add(thruster);
            }
        }

        // Calculate centre of mass
        float mass = thismass;

        this.massCentreX = thismassCentreX / thismass + 0.5F;
        float massCentreY = thismassCentreY / thismass + 0.5F;
        this.massCentreZ = thismassCentreZ / thismass + 0.5F;
        //System.out.println("(X,Z) = "+this.massCentreX+","+this.massCentreZ);

        this.setSpinCentre(this.massCentreX, this.massCentreZ);

        //The boundary is at the outer edges of the blocks
        this.ssBoundsMaxX = thisssBoundsMaxX + 1;
        this.ssBoundsMinX = thisssBoundsMinX;
        this.ssBoundsMaxY = thisssBoundsMaxY + 1;
        this.ssBoundsMinY = thisssBoundsMinY;
        this.ssBoundsMaxZ = thisssBoundsMaxZ + 1;
        this.ssBoundsMinZ = thisssBoundsMinZ;

        // Calculate momentOfInertia
        thismoment -= this.massCentreX * this.massCentreX * mass;
        thismoment -= this.massCentreZ * this.massCentreZ * mass;
        this.momentOfInertia = thismoment;

        //TODO
        // TODO defy gravity
        // TODO break blocks which are outside SS (not in checked)
        // TODO prevent spin if there is a huge number of blocks outside SS

        LSLog.debug("MoI = " + this.momentOfInertia + " CoMx = " + this.massCentreX + " CoMz = " + this.massCentreZ);

        //Send packets to clients in this dimension
        List<Object> objList = new ArrayList<Object>();
        objList.add(Double.valueOf(this.spinCentreX));
        objList.add(Double.valueOf(this.spinCentreZ));
        LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_DATA, LabStuffUtils.getDimensionID(this.worldProvider), objList), LabStuffUtils.getDimensionID(this.worldProvider));

        objList = new ArrayList<Object>();
        objList.add(Integer.valueOf(this.ssBoundsMinX));
        objList.add(Integer.valueOf(this.ssBoundsMaxX));
        objList.add(Integer.valueOf(this.ssBoundsMinY));
        objList.add(Integer.valueOf(this.ssBoundsMaxY));
        objList.add(Integer.valueOf(this.ssBoundsMinZ));
        objList.add(Integer.valueOf(this.ssBoundsMaxZ));
        LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_BOX, LabStuffUtils.getDimensionID(this.worldProvider), objList), LabStuffUtils.getDimensionID(this.worldProvider));

        this.updateSpinSpeed();

        return true;
    }

    public void updateSpinSpeed()
    {
        if (this.momentOfInertia > 0F)
        {
            float netTorque = 0F;
            int countThrusters = 0;
            int countThrustersReverse = 0;

            for (BlockPos thruster : this.thrustersPlus)
            {
                float xx = thruster.getX() - this.massCentreX;
                float zz = thruster.getZ() - this.massCentreZ;
                netTorque += MathHelper.sqrt_float(xx * xx + zz * zz);
                countThrusters++;
            }
            for (BlockPos thruster : this.thrustersMinus)
            {
                float xx = thruster.getX() - this.massCentreX;
                float zz = thruster.getZ() - this.massCentreZ;
                netTorque -= MathHelper.sqrt_float(xx * xx + zz * zz);
                countThrustersReverse++;
            }

            if (countThrusters == countThrustersReverse)
            {
                this.angularVelocityAccel = 0.000004F;
                this.angularVelocityTarget = 0F;
            }
            else
            {
                countThrusters += countThrustersReverse;
                if (countThrusters > 4)
                {
                    countThrusters = 4;
                }

                float maxRx = Math.max(this.ssBoundsMaxX - this.massCentreX, this.massCentreX - this.ssBoundsMinX);
                float maxRz = Math.max(this.ssBoundsMaxZ - this.massCentreZ, this.massCentreZ - this.ssBoundsMinZ);
                float maxR = Math.max(maxRx, maxRz);
                this.angularVelocityTarget = MathHelper.sqrt_float(GFORCE / maxR) / 2;
                //The divide by 2 is not scientific but is a Minecraft factor as everything happens more quickly
                float spinCap = 0.00125F * countThrusters;

                //TODO: increase this above 20F in release versions so everything happens more slowly
                this.angularVelocityAccel = netTorque / this.momentOfInertia / 20F;
                if (this.angularVelocityAccel < 0)
                {
                    this.angularVelocityAccel = -this.angularVelocityAccel;
                    this.angularVelocityTarget = -this.angularVelocityTarget;
                    if (this.angularVelocityTarget < -spinCap)
                    {
                        this.angularVelocityTarget = -spinCap;
                    }
                }
                else
                    //Do not make it spin too fast or players might get dizzy
                    //Also make it so players need minimum 4 thrusters for best spin
                    if (this.angularVelocityTarget > spinCap)
                    {
                        this.angularVelocityTarget = spinCap;
                    }

            }
        }

        if (!this.clientSide)
        {
            //Save the updated data for the world
            this.save();
        }
    }

    public void updateSpin()
    {
        if (!this.clientSide)
        {
            if (this.dataNotLoaded)
            {
                this.savefile = OrbitSpinSaveData.initWorldData(this.world);
                this.readFromNBT(this.savefile.datacompound);
                this.dataNotLoaded = false;
            }

            if (this.doSpinning)
            {
                boolean updateNeeded = true;
                if (this.angularVelocityTarget < this.angularVelocityRadians)
                {
                    float newAngle = this.angularVelocityRadians - this.angularVelocityAccel;
                    if (newAngle < this.angularVelocityTarget)
                    {
                        newAngle = this.angularVelocityTarget;
                    }
                    this.setSpinRate(newAngle);
                    this.thrustersFiring = true;
                }
                else if (this.angularVelocityTarget > this.angularVelocityRadians)
                {
                    float newAngle = this.angularVelocityRadians + this.angularVelocityAccel;
                    if (newAngle > this.angularVelocityTarget)
                    {
                        newAngle = this.angularVelocityTarget;
                    }
                    this.setSpinRate(newAngle);
                    this.thrustersFiring = true;
                }
                else if (this.thrustersFiring)
                {
                    this.thrustersFiring = false;
                }
                else
                {
                    updateNeeded = false;
                }

                if (updateNeeded)
                {
                    this.writeToNBT(this.savefile.datacompound);
                    this.savefile.markDirty();
                    List<Object> objList = new ArrayList<Object>();
                    objList.add(Float.valueOf(this.angularVelocityRadians));
                    objList.add(Boolean.valueOf(this.thrustersFiring));
                    LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_SPIN, LabStuffUtils.getDimensionID(this.worldProvider), objList), LabStuffUtils.getDimensionID(this.worldProvider));
                }
            }

            //Update entity positions if in freefall
            this.loadedEntities.clear();
            this.loadedEntities.addAll(this.world.loadedEntityList);
            for (Entity e : this.loadedEntities)
            {
                //TODO: What about vehicles from GC (buggies) and other mods?
                if ((e instanceof EntityItem || e instanceof EntityLivingBase && !(e instanceof EntityPlayer) || e instanceof EntityTNTPrimed || e instanceof EntityFallingBlock) && !e.onGround)
                {
                    AxisAlignedBB entityBoundingBox = e.getEntityBoundingBox();
                    boolean outsideStation = entityBoundingBox.maxX < this.ssBoundsMinX || entityBoundingBox.minX > this.ssBoundsMaxX || entityBoundingBox.maxY < this.ssBoundsMinY ||
                            entityBoundingBox.minY > this.ssBoundsMaxY || entityBoundingBox.maxZ < this.ssBoundsMinZ || entityBoundingBox.minZ > this.ssBoundsMaxZ;

                    if (outsideStation || FreefallHandler.testEntityFreefall(this.world, entityBoundingBox))
                    {
                        if (this.doSpinning)
                        {
                            this.moveRotatedEntity(e, this.spinCentreX, this.spinCentreZ, this.angularVelocityRadians);
                        }
                        FreefallHandler.tickFreefallEntity(e);
                        if (e instanceof ITumblable)
                        {
                            ((ITumblable) e).setTumbling(3F);
                        }
                    }
                    else
                    {
                        if (e instanceof ITumblable)
                        {
                            ((ITumblable) e).setTumbling(0F);
                        }
                    }
                }
            }
        }
    }

    private void moveRotatedEntity(Entity e, double rotationCentreX, double rotationCentreZ, float deltaTheta)
    {
        //Do the rotation
        if (deltaTheta != 0F)
        {
            float angle;
            final double xx = e.posX - rotationCentreX;
            final double zz = e.posZ - rotationCentreZ;
            double arc = Math.sqrt(xx * xx + zz * zz);
            if (xx == 0D)
            {
                angle = zz > 0 ? 3.1415926535F / 2 : -3.1415926535F / 2;
            }
            else
            {
                angle = (float) Math.atan(zz / xx);
            }
            if (xx < 0D)
            {
                angle += 3.1415926535F;
            }
            angle += deltaTheta / 3F;
            arc = arc * deltaTheta;
            final double offsetX = -arc * MathHelper.sin(angle);
            final double offsetZ = arc * MathHelper.cos(angle);
            e.posX += offsetX;
            e.posZ += offsetZ;
            e.lastTickPosX += offsetX;
            e.lastTickPosZ += offsetZ;

            //Rotated into an unloaded chunk (probably also drifted out to there): byebye
            if (!e.worldObj.isBlockLoaded(new BlockPos(MathHelper.floor_double(e.posX), 64, MathHelper.floor_double(e.posZ))))
            {
                e.setDead();
                return;
            }

            e.setEntityBoundingBox(e.getEntityBoundingBox().offset(offsetX, 0.0D, offsetZ));
            //TODO check for block collisions here - if so move the entity appropriately and apply fall damage
            //Moving the entity = slide along / down
            e.rotationYaw += this.skyAngularVelocity;
            while (e.rotationYaw > 360F)
            {
                e.rotationYaw -= 360F;
            }

            //Rotate the motion vector (motionX, motionZ) by the angular rotation
            double velocity = Math.sqrt(e.motionX * e.motionX + e.motionZ * e.motionZ);
            if (e.motionX == 0D)
            {
                angle = e.motionZ > 0 ? 3.1415926535F / 2 : -3.1415926535F / 2;
            }
            else
            {
                angle = (float) Math.atan(e.motionZ / e.motionX);
            }
            if (e.motionX < 0D)
            {
                angle += 3.1415926535F;
            }
            angle += deltaTheta;
            e.motionX = velocity * MathHelper.cos(angle);
            e.motionZ = velocity * MathHelper.sin(angle);
        }
    }
    
    /**
     * Call this when player first login/transfer to this dimension
     * <p/>
     * TODO how can this code be called by other mods / plugins with teleports
     * (e.g. Bukkit)? See WorldUtil.teleportEntity()
     *
     * @param player
     */
    public void sendPackets(EntityPlayerMP player)
    {
        List<Object> objList = new ArrayList<Object>();
        objList.add(this.angularVelocityRadians);
        objList.add(this.thrustersFiring);
        if (player == null)
        {
            LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_SPIN, LabStuffUtils.getDimensionID(this.worldProvider), objList), LabStuffUtils.getDimensionID(this.worldProvider));
        }
        else
        {
            LabStuffMain.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_SPIN, LabStuffUtils.getDimensionID(player.worldObj), objList), player);
        }

        objList = new ArrayList<>();
        objList.add(this.spinCentreX);
        objList.add(this.spinCentreZ);
        if (player == null)
        {
            LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_DATA, LabStuffUtils.getDimensionID(this.worldProvider), objList), LabStuffUtils.getDimensionID(this.worldProvider));
        }
        else
        {
            LabStuffMain.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_DATA, LabStuffUtils.getDimensionID(player.worldObj), objList), player);
        }

        objList = new ArrayList<>();
        objList.add(this.ssBoundsMinX);
        objList.add(this.ssBoundsMaxX);
        objList.add(this.ssBoundsMinY);
        objList.add(this.ssBoundsMaxY);
        objList.add(this.ssBoundsMinZ);
        objList.add(this.ssBoundsMaxZ);
        if (player == null)
        {
            LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_BOX, LabStuffUtils.getDimensionID(this.worldProvider), objList), LabStuffUtils.getDimensionID(this.worldProvider));
        }
        else
        {
            LabStuffMain.packetPipeline.sendTo(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_STATION_BOX, LabStuffUtils.getDimensionID(player.worldObj), objList), player);
        }
    }

    private void save()
    {
        if (this.savefile == null)
        {
            this.savefile = OrbitSpinSaveData.initWorldData(this.world);
            this.dataNotLoaded = false;
        }
        else
        {
            this.writeToNBT(this.savefile.datacompound);
            this.savefile.markDirty();
        }
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        this.doSpinning = true;//nbt.getBoolean("doSpinning");
        this.angularVelocityRadians = nbt.getFloat("omegaRad");
        this.skyAngularVelocity = nbt.getFloat("omegaSky");
        this.angularVelocityTarget = nbt.getFloat("omegaTarget");
        this.angularVelocityAccel = nbt.getFloat("omegaAcc");

        NBTTagCompound oneBlock = (NBTTagCompound) nbt.getTag("oneBlock");
        if (oneBlock != null)
        {
//            this.oneSSBlock = BlockVec3.readFromNBT(oneBlock);
            this.oneSSBlock = new BlockPos(oneBlock.getInteger("x"), oneBlock.getInteger("y"), oneBlock.getInteger("z"));
        }
        else
        {
            this.oneSSBlock = null;
        }

        //A lot of the data can be refreshed by refresh
        this.refresh(this.oneSSBlock, false);

        this.sendPackets(null);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt.setBoolean("doSpinning", this.doSpinning);
        nbt.setFloat("omegaRad", this.angularVelocityRadians);
        nbt.setFloat("omegaSky", this.skyAngularVelocity);
        nbt.setFloat("omegaTarget", this.angularVelocityTarget);
        nbt.setFloat("omegaAcc", this.angularVelocityAccel);
        if (this.oneSSBlock != null)
        {
            NBTTagCompound oneBlock = new NBTTagCompound();
            oneBlock.setInteger("x", this.oneSSBlock.getX());
            oneBlock.setInteger("y", this.oneSSBlock.getY());
            oneBlock.setInteger("z", this.oneSSBlock.getZ());
            nbt.setTag("oneBlock", oneBlock);
        }
        return nbt;
    }
}