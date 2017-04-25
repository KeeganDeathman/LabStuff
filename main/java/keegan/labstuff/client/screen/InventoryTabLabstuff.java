package keegan.labstuff.client.screen;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.client.tabs.AbstractTab;
import keegan.labstuff.util.LabStuffUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.FMLClientHandler;

public class InventoryTabLabstuff extends AbstractTab
{
    public InventoryTabLabstuff()
    {
        super(0, 0, 0, new ItemStack(LabStuffMain.oxMask));
    }

    @Override
    public void onTabClicked()
    {
    	LabStuffMain.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_OPEN_EXTENDED_INVENTORY, LabStuffUtils.getDimensionID(FMLClientHandler.instance().getClient().theWorld), new Object[] {}));
        LabStuffClientProxy.playerClientHandler.onBuild(0, FMLClientHandler.instance().getClientPlayerEntity());
    }

    @Override
    public boolean shouldAddToList()
    {
        return true;
    }
}