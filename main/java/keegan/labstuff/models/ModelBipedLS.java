package keegan.labstuff.models;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.entities.EntitySpaceshipBase;
import keegan.labstuff.items.*;
import keegan.labstuff.util.*;
import keegan.labstuff.world.ILabstuffWorldProvider;
import keegan.labstuff.wrappers.PlayerGearData;
import net.minecraft.client.model.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.math.*;

public class ModelBipedLS extends ModelBiped
{
    public ModelBipedLS(float var1)
    {
        super(var1);
    }

    public static void setRotationAngles(ModelBiped biped, float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        final EntityPlayer player = (EntityPlayer) par7Entity;
        final ItemStack currentItemStack = player.inventory.getCurrentItem();

        if (!par7Entity.onGround && par7Entity.worldObj.provider instanceof ILabstuffWorldProvider && par7Entity.getRidingEntity() == null && !(currentItemStack != null && currentItemStack.getItem() instanceof IHoldableItem))
        {
            float speedModifier = 0.1162F * 2;

            float angularSwingArm = MathHelper.cos(par1 * (speedModifier / 2));
            float rightMod = biped.rightArmPose == ArmPose.ITEM ? 1 : 2;
            biped.bipedRightArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * rightMod * par2 * 0.5F;
            biped.bipedLeftArm.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 2.0F * par2 * 0.5F;
            biped.bipedRightArm.rotateAngleX += -angularSwingArm * 4.0F * par2 * 0.5F;
            biped.bipedLeftArm.rotateAngleX += angularSwingArm * 4.0F * par2 * 0.5F;
            biped.bipedLeftLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F + (float) Math.PI) * 1.4F * par2;
            biped.bipedLeftLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2 + (float) Math.PI) * 1.4F * par2;
            biped.bipedRightLeg.rotateAngleX -= MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
            biped.bipedRightLeg.rotateAngleX += MathHelper.cos(par1 * 0.1162F * 2) * 1.4F * par2;
        }

        PlayerGearData gearData = ModelBipedLS.getGearData(player);

        if (gearData != null)
        {
            if (gearData.getParachute() != null)
            {
                // Parachute is equipped
                biped.bipedLeftArm.rotateAngleX += (float) Math.PI;
                biped.bipedLeftArm.rotateAngleZ += (float) Math.PI / 10;
                biped.bipedRightArm.rotateAngleX += (float) Math.PI;
                biped.bipedRightArm.rotateAngleZ -= (float) Math.PI / 10;
            }
        }

        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof IHoldableItem)
        {
            Item heldItem = player.inventory.getCurrentItem().getItem();
            IHoldableItem holdableItem = (IHoldableItem) heldItem;
            IHoldableItemCustom holdableItemCustom = heldItem instanceof IHoldableItemCustom ? (IHoldableItemCustom) heldItem : null;

            if (holdableItem.shouldHoldLeftHandUp(player))
            {
                Vector3 angle = null;

                if (holdableItemCustom != null)
                {
                    angle = holdableItemCustom.getLeftHandRotation(player);
                }

                if (angle == null)
                {
                    angle = new Vector3((float) Math.PI + 0.3F, 0.0F, (float) Math.PI / 10.0F);
                }

                biped.bipedLeftArm.rotateAngleX = angle.floatX();
                biped.bipedLeftArm.rotateAngleY = angle.floatY();
                biped.bipedLeftArm.rotateAngleZ = angle.floatZ();
            }

            if (holdableItem.shouldHoldRightHandUp(player))
            {
                Vector3 angle = null;

                if (holdableItemCustom != null)
                {
                    angle = holdableItemCustom.getRightHandRotation(player);
                }

                if (angle == null)
                {
                    angle = new Vector3((float) Math.PI + 0.3F, 0.0F, (float) -Math.PI / 10.0F);
                }

                biped.bipedRightArm.rotateAngleX = angle.floatX();
                biped.bipedRightArm.rotateAngleY = angle.floatY();
                biped.bipedRightArm.rotateAngleZ = angle.floatZ();
            }

            if (player.onGround && holdableItem.shouldCrouch(player))
            {
                GlStateManager.translate(0.0F, player.isSneaking() ? 0.0F : 0.125F, 0.0F);
                biped.bipedBody.rotateAngleX = 0.5F;
                biped.bipedRightLeg.rotationPointZ = 4.0F;
                biped.bipedLeftLeg.rotationPointZ = 4.0F;
                biped.bipedRightLeg.rotationPointY = 9.0F;
                biped.bipedLeftLeg.rotationPointY = 9.0F;
                biped.bipedHead.rotationPointY = 1.0F;
                biped.bipedHeadwear.rotationPointY = 1.0F;
            }
        }

        final List<?> l = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(player.posX - 20, 0, player.posZ - 20, player.posX + 20, 200, player.posZ + 20));

        for (int i = 0; i < l.size(); i++)
        {
            final Entity e = (Entity) l.get(i);

            if (e instanceof EntitySpaceshipBase)
            {
                final EntitySpaceshipBase ship = (EntitySpaceshipBase) e;

                if (!ship.getPassengers().isEmpty() && !ship.getPassengers().contains(player) && (ship.getLaunched() || ship.timeUntilLaunch < 390))
                {
                    biped.bipedRightArm.rotateAngleZ -= (float) (Math.PI / 8) + MathHelper.sin(par3 * 0.9F) * 0.2F;
                    biped.bipedRightArm.rotateAngleX = (float) Math.PI;
                    break;
                }
            }
        }

        if (biped instanceof ModelPlayer)
        {
            copyModelAngles(biped.bipedLeftLeg, ((ModelPlayer) biped).bipedLeftLegwear);
            copyModelAngles(biped.bipedRightLeg, ((ModelPlayer) biped).bipedRightLegwear);
            copyModelAngles(biped.bipedLeftArm, ((ModelPlayer) biped).bipedLeftArmwear);
            copyModelAngles(biped.bipedRightArm, ((ModelPlayer) biped).bipedRightArmwear);
            copyModelAngles(biped.bipedBody, ((ModelPlayer) biped).bipedBodyWear);
        }
    }

    @Override
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        ModelBipedLS.setRotationAngles(this, par1, par2, par3, par4, par5, par6, par7Entity);
    }

    public static PlayerGearData getGearData(EntityPlayer player)
    {
        PlayerGearData gearData = LabStuffClientProxy.playerItemData.get(player.getName());

        if (gearData == null)
        {
            String id = player.getGameProfile().getName();

            if (!LabStuffClientProxy.gearDataRequests.contains(id))
            {
                LabStuffMain.packetPipeline.sendToServer(new PacketSimple(PacketSimple.EnumSimplePacket.S_REQUEST_GEAR_DATA, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { id }));
                LabStuffClientProxy.gearDataRequests.add(id);
            }
        }

        return gearData;
    }
}