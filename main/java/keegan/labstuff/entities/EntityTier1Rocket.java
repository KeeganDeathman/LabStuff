package keegan.labstuff.entities;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import keegan.labstuff.world.ILabstuffWorldProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

//import calclavia.api.icbm.IMissile;

public class EntityTier1Rocket extends EntityTieredRocket
{
    public EntityTier1Rocket(World par1World)
    {
        super(par1World);
        this.setSize(1.2F, 3.5F);
//        this.yOffset = 1.5F;
    }

    public EntityTier1Rocket(World par1World, double par2, double par4, double par6, EnumRocketType rocketType)
    {
        super(par1World, par2, par4, par6);
        this.rocketType = rocketType;
        this.cargoItems = new ItemStack[this.getSizeInventory()];
        this.setSize(1.2F, 3.5F);
//        this.yOffset = 1.5F;
    }

    @Override
    public double getMountedYOffset()
    {
        return 0.3D;
    }

    @Override
    public float getRotateOffset()
    {
        return 0.0F;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target)
    {
        return new ItemStack(LabStuffMain.eagle, 1, this.rocketType.getIndex());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void onUpdate()
    {
        super.onUpdate();

        int i;

        if (this.timeUntilLaunch >= 100)
        {
            i = Math.abs(this.timeUntilLaunch / 100);
        }
        else
        {
            i = 1;
        }

        if ((this.getLaunched() || this.launchPhase == EnumLaunchPhase.IGNITED.ordinal() && this.rand.nextInt(i) == 0) && !ConfigManagerCore.disableSpaceshipParticles && this.hasValidFuel())
        {
            if (this.worldObj.isRemote)
            {
                this.spawnParticles(this.getLaunched());
            }
        }

        if (this.launchPhase == EnumLaunchPhase.LAUNCHED.ordinal() && this.hasValidFuel())
        {
            if (!this.landing)
            {
                double d = this.timeSinceLaunch / 150;

                d = Math.min(d, 1);

                if (d != 0.0)
                {
                    this.motionY = -d * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);
                }
            }
            else
            {
                this.motionY -= 0.008D;
            }

            double multiplier = 1.0D;

            if (this.worldObj.provider instanceof ILabstuffWorldProvider)
            {
                multiplier = ((ILabstuffWorldProvider) this.worldObj.provider).getFuelUsageMultiplier();

                if (multiplier <= 0)
                {
                    multiplier = 1;
                }
            }

            if (this.timeSinceLaunch % MathHelper.floor_double(3 * (1 / multiplier)) == 0)
            {
                this.removeFuel(1);
                if (!this.hasValidFuel())
                {
                    this.stopRocketSound();
                }
            }
        }
        else if (!this.hasValidFuel() && this.getLaunched() && !this.worldObj.isRemote)
        {
            if (Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 10 != 0.0)
            {
                this.motionY -= Math.abs(Math.sin(this.timeSinceLaunch / 1000)) / 20;
            }
        }
    }

    @Override
    public void onTeleport(EntityPlayerMP player)
    {
        final EntityPlayerMP playerBase = PlayerUtil.getPlayerBaseServerFromPlayer(player, false);

        if (playerBase != null)
        {
            LSPlayerStats stats = LSPlayerStats.get(player);

            if (this.cargoItems == null || this.cargoItems.length == 0)
            {
                stats.setRocketStacks(new ItemStack[2]);
            }
            else
            {
                stats.setRocketStacks(this.cargoItems);
            }

            stats.setRocketType(this.rocketType.getIndex());
            stats.setRocketItem(LabStuffMain.eagle);
            stats.setFuelLevel(this.fuelTank.getFluidAmount());
        }
    }

    protected void spawnParticles(boolean launched)
    {
        if (!this.isDead)
        {
            double x1 = 2 * Math.cos(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
            double z1 = 2 * Math.sin(this.rotationYaw * Math.PI / 180.0D) * Math.sin(this.rotationPitch * Math.PI / 180.0D);
            double y1 = 2 * Math.cos((this.rotationPitch - 180) * Math.PI / 180.0D);

            if (this.landing && this.targetVec != null)
            {
                double modifier = this.posY - this.targetVec.getY();
                modifier = Math.max(modifier, 1.0);
                x1 *= modifier / 60.0D;
                y1 *= modifier / 60.0D;
                z1 *= modifier / 60.0D;
            }

            final double y = this.prevPosY + (this.posY - this.prevPosY) + y1;

            final double x2 = this.posX + x1;
            final double z2 = this.posZ + z1;

            EntityLivingBase riddenByEntity = !this.getPassengers().isEmpty() && this.getPassengers().get(0) instanceof EntityLivingBase ? (EntityLivingBase) this.getPassengers().get(0) : null;

            if (this.getLaunched())
            {
                Vector3 motionVec = new Vector3(x1, y1, z1);
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y, z2 + 0.4 - this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y, z2 + 0.4 - this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y, z2 - 0.4 + this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y, z2 - 0.4 + this.rand.nextDouble() / 10), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y, z2), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 + 0.4, y, z2), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2 - 0.4, y, z2), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y, z2 + 0.4D), motionVec, new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameLaunched", new Vector3(x2, y, z2 - 0.4D), motionVec, new Object[] { riddenByEntity });

            }
            else
            {
                LabStuffMain.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y + 0.9, z2 + 0.4 - this.rand.nextDouble() / 10), new Vector3(this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25), new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y + 0.9, z2 + 0.4 - this.rand.nextDouble() / 10), new Vector3(this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25), new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 - 0.4 + this.rand.nextDouble() / 10, y + 0.9, z2 - 0.4 + this.rand.nextDouble() / 10), new Vector3(this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25), new Object[] { riddenByEntity });
                LabStuffMain.proxy.spawnParticle("launchFlameIdle", new Vector3(x2 + 0.4 - this.rand.nextDouble() / 10, y + 0.9, z2 - 0.4 + this.rand.nextDouble() / 10), new Vector3(this.rand.nextDouble() / 2.0 - 0.25, 0.0, this.rand.nextDouble() / 2.0 - 0.25), new Object[] { riddenByEntity });
            }
        }
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
        return !this.isDead && par1EntityPlayer.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
    }

    @Override
    public int getPreLaunchWait()
    {
        return 400;
    }

    @Override
    public List<ItemStack> getItemsDropped(List<ItemStack> droppedItems)
    {
        super.getItemsDropped(droppedItems);
        ItemStack rocket = new ItemStack(LabStuffMain.eagle, 1, this.rocketType.getIndex());
        rocket.setTagCompound(new NBTTagCompound());
        rocket.getTagCompound().setInteger("RocketFuel", this.fuelTank.getFluidAmount());
        droppedItems.add(rocket);
        return droppedItems;
    }

    @Override
    public boolean hasCustomName()
    {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack)
    {
        return false;
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
    public boolean isDockValid(IFuelDock dock)
    {
        return dock instanceof TileEntityLandingPad;
    }

    @Override
    public int getRocketTier()
    {
        return 1;
    }

    @Override
    public int getFuelTankCapacity()
    {
        return 1000;
    }

    @Override
    public float getCameraZoom()
    {
        return 15.0F;
    }

    @Override
    public boolean defaultThirdPerson()
    {
        return true;
    }

    @Override
    public double getOnPadYOffset()
    {
        return 0.0D;
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
}