package keegan.labstuff.client.fx;

import java.util.List;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.entity.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class ParticleLanderFlame extends Particle
{
    private float smokeParticleScale;
    private EntityLivingBase ridingEntity;

    public ParticleLanderFlame(World world, double x, double y, double z, double mX, double mY, double mZ, EntityLivingBase ridingEntity)
    {
        super(world, x, y, z, mX, mY, mZ);
        this.motionX *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += mX;
        this.motionY = mY;
        this.motionZ += mZ;
        this.particleRed = 200F / 255F;
        this.particleGreen = 200F / 255F;
        this.particleBlue = 200F / 255F + this.rand.nextFloat() / 3;
        this.particleScale *= 8F * 1.0F;
        this.smokeParticleScale = this.particleScale;
        this.particleMaxAge = (int) 5.0D;
        this.field_190017_n = true;
        this.ridingEntity = ridingEntity;
    }

    @Override
    public void renderParticle(VertexBuffer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
    {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        float var8 = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;

        if (var8 < 0.0F)
        {
            var8 = 0.0F;
        }

        if (var8 > 1.0F)
        {
            var8 = 1.0F;
        }

        this.particleScale = this.smokeParticleScale * var8;
        super.renderParticle(worldRendererIn, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        this.particleGreen -= 0.09F;
        this.particleRed -= 0.09F;

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.particleScale *= 0.9599999785423279D;

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        final List<?> var3 = this.worldObj.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D));

        if (var3 != null)
        {
            for (int var4 = 0; var4 < var3.size(); ++var4)
            {
                final Entity var5 = (Entity) var3.get(var4);

                if (var5 instanceof EntityLivingBase)
                {
                    if (!var5.isDead && !var5.isBurning() && !var5.equals(this.ridingEntity))
                    {
                        var5.setFire(3);
                        LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_SET_ENTITY_FIRE, LabStuffUtils.getDimensionID(var5.worldObj), new Object[] { var5.getEntityId() }));
                    }
                }
            }
        }
    }

    @Override
    public int getBrightnessForRender(float par1)
    {
        return 15728880;
    }

//    @Override
//    public float getBrightness(float par1)
//    {
//        return 1.0F;
//    }
}