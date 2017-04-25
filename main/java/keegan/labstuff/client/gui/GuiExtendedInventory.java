package keegan.labstuff.client.gui;

import keegan.labstuff.client.screen.InventoryTabLabstuff;
import keegan.labstuff.client.tabs.*;
import keegan.labstuff.container.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;

public class GuiExtendedInventory extends InventoryEffectRenderer
{
    private static final ResourceLocation inventoryTexture = new ResourceLocation("labstuff:textures/gui/inventory.png");

    private float xSize_lo_2;
    private float ySize_lo_2;

    private int potionOffsetLast;
    private static float rotation = 0.0F;

    private boolean initWithPotion;

    public GuiExtendedInventory(EntityPlayer entityPlayer, InventoryExtended inventory)
    {
        super(new ContainerExtendedInventory(entityPlayer, inventory));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        GuiExtendedInventory.drawPlayerOnGui(this.mc, 33, 60, 29, 51 - this.xSize_lo_2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        super.initGui();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiLeft += this.getPotionOffset();
        this.potionOffsetLast = getPotionOffsetNEI();

        int cornerX = this.guiLeft;
        int cornerY = this.guiTop;

        TabRegistry.updateTabValues(cornerX, cornerY, InventoryTabLabstuff.class);
        TabRegistry.addTabsToList(this.buttonList);

        this.buttonList.add(new GuiButton(0, this.guiLeft + 10, this.guiTop + 71, 7, 7, ""));
        this.buttonList.add(new GuiButton(1, this.guiLeft + 51, this.guiTop + 71, 7, 7, ""));
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
        case 0:
            GuiExtendedInventory.rotation += 10.0F;
            break;
        case 1:
            GuiExtendedInventory.rotation -= 10.0F;
            break;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiExtendedInventory.inventoryTexture);
        final int k = this.guiLeft;
        final int l = this.guiTop;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        int newPotionOffset = this.getPotionOffsetNEI();
        if (newPotionOffset < this.potionOffsetLast)
        {
            int diff = newPotionOffset - this.potionOffsetLast;
            this.potionOffsetLast = newPotionOffset;
            this.guiLeft += diff;
            for (int k = 0; k < this.buttonList.size(); ++k)
            {
                GuiButton b = (GuiButton) this.buttonList.get(k);
                if (!(b instanceof AbstractTab))
                {
                    b.xPosition += diff;
                }
            }
        }
        super.drawScreen(par1, par2, par3);
        this.xSize_lo_2 = par1;
        this.ySize_lo_2 = par2;
    }

    public static void drawPlayerOnGui(Minecraft mc, int x, int y, int scale, float mouseX)
    {
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = mc.thePlayer.renderYawOffset;
        float f3 = mc.thePlayer.rotationYaw;
        float f4 = mc.thePlayer.rotationPitch;
        float f5 = mc.thePlayer.rotationYawHead;
        mouseX -= 19;
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        mc.thePlayer.renderYawOffset = GuiExtendedInventory.rotation;
        mc.thePlayer.rotationYaw = (float) Math.atan(mouseX / 40.0F) * 40.0F;
        mc.thePlayer.rotationYaw = GuiExtendedInventory.rotation;
        mc.thePlayer.rotationYawHead = mc.thePlayer.rotationYaw;
        mc.thePlayer.rotationPitch = (float) Math.sin(mc.getSystemTime() / 500.0F) * 3.0F;
        GlStateManager.translate(0.0F, (float) mc.thePlayer.getYOffset(), 0.0F);
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        mc.thePlayer.renderYawOffset = f2;
        mc.thePlayer.rotationYaw = f3;
        mc.thePlayer.rotationPitch = f4;
        mc.thePlayer.rotationYawHead = f5;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public int getPotionOffset()
    {
        // If at least one potion is active...
        if (!Minecraft.getMinecraft().thePlayer.getActivePotionEffects().isEmpty())
        {
            this.initWithPotion = true;
            return 60 + getPotionOffsetNEI();
        }

        // No potions, no offset needed
        this.initWithPotion = false;
        return 0;
    }

    public int getPotionOffsetNEI()
    {
        if (this.initWithPotion && Loader.isModLoaded("NotEnoughItems"))
        {
            try
            {
                // Check whether NEI is hidden and enabled
                Class<?> c = Class.forName("codechicken.nei.NEIClientConfig");
                Object hidden = c.getMethod("isHidden").invoke(null);
                Object enabled = c.getMethod("isEnabled").invoke(null);
                if (hidden instanceof Boolean && enabled instanceof Boolean)
                {
                    if ((Boolean) hidden || !((Boolean) enabled))
                    {
                        // If NEI is disabled or hidden, offset the tabs by the standard 60
                        return 0;
                    }
                    //Active NEI undoes the standard potion offset
                    return -60;
                }
            }
            catch (Exception e)
            {
            }
        }
        //No NEI, no change
        return 0;
    }
}