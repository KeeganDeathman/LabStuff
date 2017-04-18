package keegan.labstuff;

import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.common.EnumGravity;
import keegan.labstuff.common.capabilities.LSPlayerStatsClient;
import keegan.labstuff.dimension.*;
import keegan.labstuff.entities.IAntiGrav;
import keegan.labstuff.items.IArmorGravity;
import keegan.labstuff.util.*;
import keegan.labstuff.world.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.relauncher.*;


/**
 * These methods are called from vanilla minecraft through bytecode injection done in MicdoodleCore
 *
 * See https://github.com/micdoodle8/MicdoodleCore
 */
public class TransformerHooks
{
    private static IWorldGenerator generatorGCGreg = null;
    private static IWorldGenerator generatorCoFH = null;
    private static IWorldGenerator generatorDenseOres = null;
    private static IWorldGenerator generatorTCAuraNodes = null;
    private static IWorldGenerator generatorAE2meteors = null;
    private static Method generateTCAuraNodes = null;
    private static boolean generatorsInitialised = false;

    public static double getGravityForEntity(Entity entity)
    {
        if (entity.worldObj.provider instanceof ILabstuffWorldProvider)
        {
            if (entity instanceof EntityChicken && !OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, entity.getEntityBoundingBox()))
            {
                return 0.08D;
            }

            final ILabstuffWorldProvider customProvider = (ILabstuffWorldProvider) entity.worldObj.provider;
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) entity;
                if (player.inventory != null)
                {
                    int armorModLowGrav = 100;
                    int armorModHighGrav = 100;
                    for (ItemStack armorPiece : player.getArmorInventoryList())
                    {
                        if (armorPiece != null && armorPiece.getItem() instanceof IArmorGravity)
                        {
                            armorModLowGrav -= ((IArmorGravity) armorPiece.getItem()).gravityOverrideIfLow(player);
                            armorModHighGrav -= ((IArmorGravity) armorPiece.getItem()).gravityOverrideIfHigh(player);
                        }
                    }
                    if (armorModLowGrav > 100)
                    {
                        armorModLowGrav = 100;
                    }
                    if (armorModHighGrav > 100)
                    {
                        armorModHighGrav = 100;
                    }
                    if (armorModLowGrav < 0)
                    {
                        armorModLowGrav = 0;
                    }
                    if (armorModHighGrav < 0)
                    {
                        armorModHighGrav = 0;
                    }
                    if (customProvider.getGravity() > 0)
                    {
                        return 0.08D - (customProvider.getGravity() * armorModLowGrav) / 100;
                    }
                    return 0.08D - (customProvider.getGravity() * armorModHighGrav) / 100;
                }
            }
            return 0.08D - customProvider.getGravity();
        }
        else if (entity instanceof IAntiGrav)
        {
            return 0;
        }
        else
        {
            return 0.08D;
        }
    }

    public static double getItemGravity(EntityItem e)
    {
        if (e.worldObj.provider instanceof ILabstuffWorldProvider)
        {
            final ILabstuffWorldProvider customProvider = (ILabstuffWorldProvider) e.worldObj.provider;
            return Math.max(0.002D, 0.03999999910593033D - (customProvider instanceof IOrbitDimension ? 0.05999999910593033D : customProvider.getGravity()) / 1.75D);
        }
        else
        {
            return 0.03999999910593033D;
        }
    }

    public static float getArrowGravity(EntityArrow e)
    {
        if (e.worldObj.provider instanceof ILabstuffWorldProvider)
        {
            return 0.005F;
        }
        else
        {
            return 0.05F;
        }
    }

    public static float getRainStrength(World world, float partialTicks)
    {
        if (world.isRemote)
        {
            if (world.provider.getSkyRenderer() instanceof SkyProviderOverworld)
            {
                return 0.0F;
            }
        }

        return world.prevRainingStrength + (world.rainingStrength - world.prevRainingStrength) * partialTicks;
    }

    public static boolean otherModPreventGenerate(int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (!(world.provider instanceof ILabstuffWorldProvider))
        {
            return false;
        }
        if (world.provider instanceof WorldProviderSpaceStation)
        {
            return true;
        }


        return true;
    }

    @SideOnly(Side.CLIENT)
    public static float getWorldBrightness(WorldClient world)
    {
        if (world.provider instanceof WorldProviderLuna)
        {
            float f1 = world.getCelestialAngle(1.0F);
            float f2 = 1.0F - (MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.2F);

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            f2 = 1.0F - f2;
            return f2 * 0.8F;
        }

        return world.getSunBrightness(1.0F);
    }

    @SideOnly(Side.CLIENT)
    public static float getColorRed(World world)
    {
        return (float) WorldUtil.getWorldColor(world).x;
    }

    @SideOnly(Side.CLIENT)
    public static float getColorGreen(World world)
    {
        return (float) WorldUtil.getWorldColor(world).y;
    }

    @SideOnly(Side.CLIENT)
    public static float getColorBlue(World world)
    {
        return (float) WorldUtil.getWorldColor(world).z;
    }

    @SideOnly(Side.CLIENT)
    public static Vec3d getFogColorHook(World world)
    {
        EntityPlayerSP player = FMLClientHandler.instance().getClient().thePlayer;
        if (world.provider.getSkyRenderer() instanceof SkyProviderOverworld)
        {
            float var20 = ((float) (player.posY) - 200) / 1000.0F;
            var20 = MathHelper.sqrt_float(var20);
            final float var21 = Math.max(1.0F - var20 * 40.0F, 0.0F);

            Vec3 vec = world.getFogColor(1.0F);

            return new Vec3d(vec.xCoord * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.yCoord * Math.max(1.0F - var20 * 1.29F, 0.0F), vec.zCoord * Math.max(1.0F - var20 * 1.29F, 0.0F));
        }

        return world.getFogColor(1.0F);
    }

    @SideOnly(Side.CLIENT)
    public static Vec3d getSkyColorHook(World world)
    {
        EntityPlayerSP player = FMLClientHandler.instance().getClient().thePlayer;
        if (world.provider.getSkyRenderer() instanceof SkyProviderOverworld || (player != null && player.posY > 130 && player.getRidingEntity() instanceof EntitySpaceshipBase))
        {
            float f1 = world.getCelestialAngle(1.0F);
            float f2 = MathHelper.cos(f1 * (float) Math.PI * 2.0F) * 2.0F + 0.5F;

            if (f2 < 0.0F)
            {
                f2 = 0.0F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            int i = MathHelper.floor_double(player.posX);
            int j = MathHelper.floor_double(player.posY);
            int k = MathHelper.floor_double(player.posZ);
            BlockPos pos = new BlockPos(i, j, k);
            int l = ForgeHooksClient.getSkyBlendColour(world, pos);
            float f4 = (float) (l >> 16 & 255) / 255.0F;
            float f5 = (float) (l >> 8 & 255) / 255.0F;
            float f6 = (float) (l & 255) / 255.0F;
            f4 *= f2;
            f5 *= f2;
            f6 *= f2;

            if (player.posY <= 200)
            {
                Vec3d vec = world.getSkyColor(FMLClientHandler.instance().getClient().getRenderViewEntity(), 1.0F);
                double blend = (player.posY - 130) / (200 - 130);
                double ablend = 1 - blend;
                return new Vec3d(f4 * blend + vec.xCoord * ablend, f5 * blend + vec.yCoord * ablend, f6 * blend + vec.zCoord * ablend);
            }
            else
            {
                double blend = Math.min(1.0D, (player.posY - 200) / 300.0D);
                double ablend = 1.0D - blend;
                blend /= 255.0D;
                return new Vec3d(f4 * ablend + blend * 31.0D, f5 * ablend + blend * 8.0D, f6 * ablend + blend * 99.0D);
            }
        }

        return world.getSkyColor(FMLClientHandler.instance().getClient().getRenderViewEntity(), 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public static double getRenderPosY(Entity viewEntity, double regular)
    {
        if (viewEntity.posY >= 256)
        {
            return 255.0F;
        }
        else
        {
            return regular + viewEntity.getEyeHeight();
        }
    }

    @SideOnly(Side.CLIENT)
    public static boolean shouldRenderFire(Entity entity)
    {
        if (entity.worldObj == null || !(entity.worldObj.provider instanceof ILabstuffWorldProvider))
        {
            return entity.isBurning();
        }

        if (!(entity instanceof EntityLivingBase) && !(entity instanceof EntityArrow))
        {
            return entity.isBurning();
        }

        if (entity.isBurning())
        {
            if (OxygenUtil.noAtmosphericCombustion(entity.worldObj.provider))
            {
                return OxygenUtil.isAABBInBreathableAirBlock(entity.worldObj, entity.getEntityBoundingBox());
            }
            else
            {
                return true;
            }
            //Disable fire on Labstuff worlds with no oxygen
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public static void orientCamera(float partialTicks)
    {
        EntityPlayerSP player = LabStuffClientProxy.mc.thePlayer;
        LSPlayerStatsClient stats = LSPlayerStatsClient.get(player);

        Entity viewEntity = LabStuffClientProxy.mc.getRenderViewEntity();


        if (viewEntity instanceof EntityLivingBase && viewEntity.worldObj.provider instanceof IZeroGDimension && !((EntityLivingBase)viewEntity).isPlayerSleeping())
        {
            float pitch = viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks;
            float yaw = viewEntity.prevRotationYaw + (viewEntity.rotationYaw - viewEntity.prevRotationYaw) * partialTicks + 180.0F;
            float eyeHeightChange = viewEntity.width / 2.0F;

//            GL11.glTranslatef(0.0F, -f1, 0.0F);
            GL11.glRotatef(-yaw, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-pitch, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.0F, 0.1F);

            EnumGravity gDir = stats.getGdir();
            GL11.glRotatef(180.0F * gDir.getThetaX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(180.0F * gDir.getThetaZ(), 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(pitch * gDir.getPitchGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(pitch * gDir.getPitchGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityX(), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityY(), 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(yaw * gDir.getYawGravityZ(), 0.0F, 0.0F, 1.0F);

//        	GL11.glTranslatef(sneakY * gDir.getSneakVecX(), sneakY * gDir.getSneakVecY(), sneakY * gDir.getSneakVecZ());

            GL11.glTranslatef(eyeHeightChange * gDir.getEyeVecX(), eyeHeightChange * gDir.getEyeVecY(), eyeHeightChange * gDir.getEyeVecZ());

            if (stats.getGravityTurnRate() < 1.0F)
            {
                GL11.glRotatef(90.0F * (stats.getGravityTurnRatePrev() + (stats.getGravityTurnRate() - stats.getGravityTurnRatePrev()) * partialTicks), stats.getGravityTurnVecX(), stats.getGravityTurnVecY(), stats.getGravityTurnVecZ());
            }
        }

        //omit this for interesting 3P views
//        GL11.glTranslatef(0.0F, 0.0F, -0.1F);
//        GL11.glRotatef(pitch, 1.0F, 0.0F, 0.0F);
//        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
//        GL11.glTranslatef(0.0F, f1, 0.0F);
    }

}