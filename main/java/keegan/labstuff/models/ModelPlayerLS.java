package keegan.labstuff.models;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.util.LabStuffUtils;
import keegan.labstuff.wrappers.PlayerGearData;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class ModelPlayerLS extends ModelPlayer
{
    public static final ResourceLocation oxygenMaskTexture = new ResourceLocation("labstuff:textures/model/oxygen.png");
    public static final ResourceLocation playerTexture = new ResourceLocation("labstuff:textures/model/player.png");

    public ModelPlayerLS(float var1, boolean smallArms)
    {
        super(var1, smallArms);
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