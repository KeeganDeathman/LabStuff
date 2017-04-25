package keegan.labstuff.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SlotArmorLS extends Slot
{
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    final int armorType;
    final EntityPlayer thePlayer;

    public SlotArmorLS(EntityPlayer thePlayer, IInventory par2IInventory, int par3, int par4, int par5, int par6)
    {
        super(par2IInventory, par3, par4, par5);
        this.thePlayer = thePlayer;
        this.armorType = par6;
    }

    @Override
    public int getSlotStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        final Item item = par1ItemStack == null ? null : par1ItemStack.getItem();
        return item != null && item.isValidArmor(par1ItemStack, VALID_EQUIPMENT_SLOTS[this.armorType], this.thePlayer);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getSlotTexture()
    {
        return ItemArmor.EMPTY_SLOT_NAMES[this.armorType];
    }
}