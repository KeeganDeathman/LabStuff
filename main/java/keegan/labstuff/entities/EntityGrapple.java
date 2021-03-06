package keegan.labstuff.entities;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.common.capabilities.LSPlayerStatsClient;
import keegan.labstuff.util.*;
import keegan.labstuff.world.IZeroGDimension;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.*;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.*;

public class EntityGrapple extends Entity implements IProjectile
{
    private static final DataParameter<Integer> PULLING_ENTITY_ID = EntityDataManager.createKey(EntityGrapple.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IS_PULLING = EntityDataManager.createKey(EntityGrapple.class, DataSerializers.BOOLEAN);
    private BlockPos hitVec;
    private Block hitBlock;
    private int inData;
    private boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public EntityPlayer shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    public float rotationRoll;
    public float prevRotationRoll;
    public boolean pullingPlayer;

    public EntityGrapple(World par1World)
    {
        super(par1World);
        this.ignoreFrustumCheck = false;
//        this.yOffset = -1.5F;
        this.setSize(0.75F, 0.75F);
    }

    public EntityGrapple(World par1World, EntityPlayer shootingEntity, float par3)
    {
        super(par1World);
        this.shootingEntity = shootingEntity;
        this.setSize(0.75F, 0.75F);

        if (shootingEntity != null)
        {
            this.canBePickedUp = 1;
            this.setLocationAndAngles(shootingEntity.posX, shootingEntity.posY + shootingEntity.getEyeHeight(), shootingEntity.posZ, shootingEntity.rotationYaw, shootingEntity.rotationPitch);
        }

        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
//        this.yOffset = -1.5F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

        if (Double.isNaN(d0))
        {
            d0 = 1.0D;
        }

        d0 = d0 * 64.0D * 10.0;
        return distance < d0 * d0;
    }

    @Override
    public double getYOffset()
    {
        return -1.5F;
    }

    @Override
    protected void entityInit()
    {
        this.dataManager.register(PULLING_ENTITY_ID, 0);
        this.dataManager.register(IS_PULLING, false);
    }

    @Override
    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= f2;
        par3 /= f2;
        par5 /= f2;
        par1 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
        par3 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
        par5 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * par8;
        par1 *= par7;
        par3 *= par7;
        par5 *= par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, f3) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean b)
    {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }

    @Override
    public void setPosition(double x, double y, double z)
    {
        super.setPosition(x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(par3, f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        this.prevRotationRoll = this.rotationRoll;

        if (!this.worldObj.isRemote)
        {
            this.updateShootingEntity();

            if (this.getPullingEntity())
            {
                EntityPlayer shootingEntity = this.getShootingEntity();
                if (shootingEntity != null)
                {
                    double deltaPosition = this.getDistanceSqToEntity(shootingEntity);

                    Vector3 mot = new Vector3(shootingEntity.motionX, shootingEntity.motionY, shootingEntity.motionZ);

                    if (mot.getMagnitudeSquared() < 0.01 && this.pullingPlayer)
                    {
                        if (deltaPosition < 10)
                        {
                            this.onCollideWithPlayer(shootingEntity);
                        }
                        this.updatePullingEntity(false);
                        this.setDead();
                    }

                    this.pullingPlayer = true;
                }
            }
        }
        else
        {
            if (this.getPullingEntity())
            {
                EntityPlayer shootingEntity = this.getShootingEntity();
                if (shootingEntity != null)
                {
                    shootingEntity.setVelocity((this.posX - shootingEntity.posX) / 12.0F, (this.posY - shootingEntity.posY) / 12.0F, (this.posZ - shootingEntity.posZ) / 12.0F);
                    if (shootingEntity.worldObj.isRemote && shootingEntity.worldObj.provider instanceof IZeroGDimension)
                    {
                        LSPlayerStatsClient stats = LSPlayerStatsClient.get(shootingEntity);
                        stats.getFreefallHandler().updateFreefall(shootingEntity);
                    }
                }
            }
        }

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, f) * 180.0D / Math.PI);
        }

        if (this.hitVec != null)
        {
            IBlockState state = this.worldObj.getBlockState(this.hitVec);

            if (state.getBlock().getMaterial(state) != Material.AIR)
            {
                AxisAlignedBB axisalignedbb = state.getBlock().getCollisionBoundingBox(state, this.worldObj, this.hitVec);

                if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3d(this.posX, this.posY, this.posZ)))
                {
                    this.inGround = true;
                }
            }
        }

        if (this.arrowShake > 0)
        {
            --this.arrowShake;
        }

        if (this.inGround)
        {
            if (this.hitVec != null)
            {
                IBlockState state = this.worldObj.getBlockState(this.hitVec);
                Block block = state.getBlock();
                int j = block.getMetaFromState(state);

                if (block == this.hitBlock && j == this.inData)
                {
                    if (this.shootingEntity != null)
                    {
                        this.shootingEntity.motionX = (this.posX - this.shootingEntity.posX) / 16.0F;
                        this.shootingEntity.motionY = (this.posY - this.shootingEntity.posY) / 16.0F;
                        this.shootingEntity.motionZ = (this.posZ - this.shootingEntity.posZ) / 16.0F;
                        if (this.shootingEntity instanceof EntityPlayerMP)
                        	LabStuffMain.handler.preventFlyingKicks((EntityPlayerMP) this.shootingEntity);
                    }

                    if (!this.worldObj.isRemote && this.ticksInGround < 5)
                    {
                        this.updatePullingEntity(true);
                    }

                    ++this.ticksInGround;

                    if (this.ticksInGround == 1200)
                    {
                        this.setDead();
                    }
                }
                else
                {
                    this.inGround = false;
                    this.motionX *= this.rand.nextFloat() * 0.2F;
                    this.motionY *= this.rand.nextFloat() * 0.2F;
                    this.motionZ *= this.rand.nextFloat() * 0.2F;
                    this.ticksInGround = 0;
                    this.ticksInAir = 0;
                }
            }
        }
        else
        {
            this.rotationRoll += 5;
            ++this.ticksInAir;

            if (!this.worldObj.isRemote)
            {
                this.updatePullingEntity(false);
            }

            if (this.shootingEntity != null && this.getDistanceSqToEntity(this.shootingEntity) >= 40 * 40)
            {
                this.setDead();
            }

            Vec3d vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            Vec3d vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            RayTraceResult movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
            vec31 = new Vec3d(this.posX, this.posY, this.posZ);
            vec3 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                vec3 = new Vec3d(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int i;
            float f1;

            for (i = 0; i < list.size(); ++i)
            {
                Entity entity1 = (Entity) list.get(i);

                if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5))
                {
                    f1 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                    RayTraceResult movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec31.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new RayTraceResult(entity);
            }

            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer) movingobjectposition.entityHit;

                if (entityplayer.capabilities.disableDamage || this.shootingEntity != null && !this.shootingEntity.canAttackPlayer(entityplayer))
                {
                    movingobjectposition = null;
                }
            }

            float motion;

            if (movingobjectposition != null)
            {
                if (movingobjectposition.entityHit == null)
                {
                    this.hitVec = movingobjectposition.getBlockPos();
                    IBlockState state = this.worldObj.getBlockState(this.hitVec);
                    this.hitBlock = state.getBlock();
                    this.inData = state.getBlock().getMetaFromState(state);
                    this.motionX = (float) (movingobjectposition.hitVec.xCoord - this.posX);
                    this.motionY = (float) (movingobjectposition.hitVec.yCoord - this.posY);
                    this.motionZ = (float) (movingobjectposition.hitVec.zCoord - this.posZ);
                    motion = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / motion * 0.05000000074505806D;
                    this.posY -= this.motionY / motion * 0.05000000074505806D;
                    this.posZ -= this.motionZ / motion * 0.05000000074505806D;
                    this.playSound(SoundEvents.ENTITY_ARROW_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;

                    if (this.hitBlock.getMaterial(state) != Material.AIR)
                    {
                        this.hitBlock.onEntityCollidedWithBlock(this.worldObj, this.hitVec, state, this);
                    }
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            motion = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.rotationPitch = (float) (Math.atan2(this.motionY, motion) * 180.0D / Math.PI);

            while (this.rotationPitch - this.prevRotationPitch < -180.0F)
            {
                this.prevRotationPitch -= 360.0F;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f3 = 0.99F;
            f1 = 0.05F;

            if (this.isInWater())
            {
                float f4 = 0.25F;
                for (int l = 0; l < 4; ++l)
                {
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ);
                }

                f3 = 0.8F;
            }

            if (this.isWet())
            {
                this.extinguish();
            }

            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }

        if (!this.worldObj.isRemote && (this.ticksInGround - 1) % 10 == 0)
        {
            LabStuffMain.packetPipeline.sendToAllAround(new PacketSimple(PacketSimple.EnumSimplePacket.C_UPDATE_GRAPPLE_POS, LabStuffUtils.getDimensionID(this.worldObj), new Object[] { this.getEntityId(), new Vector3(this) }), new NetworkRegistry.TargetPoint(LabStuffUtils.getDimensionID(this.worldObj), this.posX, this.posY, this.posZ, 150));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (this.hitVec != null)
        {
            par1NBTTagCompound.setShort("xTile", (short) this.hitVec.getX());
            par1NBTTagCompound.setShort("yTile", (short) this.hitVec.getY());
            par1NBTTagCompound.setShort("zTile", (short) this.hitVec.getZ());
        }
        par1NBTTagCompound.setShort("life", (short) this.ticksInGround);
        par1NBTTagCompound.setByte("inTile", (byte) Block.getIdFromBlock(this.hitBlock));
        par1NBTTagCompound.setByte("inData", (byte) this.inData);
        par1NBTTagCompound.setByte("shake", (byte) this.arrowShake);
        par1NBTTagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        par1NBTTagCompound.setByte("pickup", (byte) this.canBePickedUp);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (par1NBTTagCompound.hasKey("xTile"))
        {
            this.hitVec = new BlockPos(par1NBTTagCompound.getShort("xTile"), par1NBTTagCompound.getShort("yTile"), par1NBTTagCompound.getShort("zTile"));
        }

        this.ticksInGround = par1NBTTagCompound.getShort("life");
        this.hitBlock = Block.getBlockById(par1NBTTagCompound.getByte("inTile") & 255);
        this.inData = par1NBTTagCompound.getByte("inData") & 255;
        this.arrowShake = par1NBTTagCompound.getByte("shake") & 255;
        this.inGround = par1NBTTagCompound.getByte("inGround") == 1;

        if (par1NBTTagCompound.hasKey("pickup", 99))
        {
            this.canBePickedUp = par1NBTTagCompound.getByte("pickup");
        }
        else if (par1NBTTagCompound.hasKey("player", 99))
        {
            this.canBePickedUp = par1NBTTagCompound.getBoolean("player") ? 1 : 0;
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
        {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !par1EntityPlayer.inventory.addItemStackToInventory(new ItemStack(Items.STRING, 1)))
            {
                flag = false;
            }

            if (flag)
            {
                this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                par1EntityPlayer.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

//    @Override
//    @SideOnly(Side.CLIENT)
//    public float getShadowSize()
//    {
//        return 0.0F;
//    }


    @Override
    public boolean canBeAttackedWithItem()
    {
        return false;
    }

    private void updateShootingEntity()
    {
        if (this.shootingEntity != null)
        {
            this.dataManager.set(PULLING_ENTITY_ID, this.shootingEntity.getEntityId());
        }
    }

    public EntityPlayer getShootingEntity()
    {
        Entity entity = this.worldObj.getEntityByID(this.dataManager.get(PULLING_ENTITY_ID));

        if (entity instanceof EntityPlayer)
        {
            return (EntityPlayer) entity;
        }

        return null;
    }

    public void updatePullingEntity(boolean pulling)
    {
        this.dataManager.set(IS_PULLING, pulling);
    }

    public boolean getPullingEntity()
    {
        return this.dataManager.get(IS_PULLING);
    }
}