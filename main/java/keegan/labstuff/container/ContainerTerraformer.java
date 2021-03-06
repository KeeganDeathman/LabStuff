package keegan.labstuff.container;

import java.util.*;

import keegan.labstuff.slot.SlotSpecific;
import keegan.labstuff.tileentity.TileEntityTerraformer;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.*;

public class ContainerTerraformer extends Container
{
    private final TileEntityTerraformer tileEntity;
    private static LinkedList<ItemStack> saplingList = null;

    public ContainerTerraformer(InventoryPlayer par1InventoryPlayer, TileEntityTerraformer tileEntity, EntityPlayer player)
    {
        this.tileEntity = tileEntity;

        this.addSlotToContainer(new SlotSpecific(tileEntity, 0, 25, 19, new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.BUCKET)));


        int var6;
        int var7;

        for (var6 = 0; var6 < 3; ++var6)
        {
            List<ItemStack> stacks = new ArrayList<ItemStack>();

            if (var6 == 0)
            {
                stacks.add(new ItemStack(Items.DYE, 1, 15));
            }
            else if (var6 == 1)
            {
                if (ContainerTerraformer.saplingList == null)
                {
                    initSaplingList();
                }

                stacks.addAll(ContainerTerraformer.saplingList);
            }
            else if (var6 == 2)
            {
                stacks.add(new ItemStack(Items.WHEAT_SEEDS));
            }

            for (var7 = 0; var7 < 4; ++var7)
            {
                this.addSlotToContainer(new SlotSpecific(tileEntity, var7 + var6 * 4 + 2, 25 + var7 * 18, 63 + var6 * 24, stacks.toArray(new ItemStack[stacks.size()])));
            }
        }

        for (var6 = 0; var6 < 3; ++var6)
        {
            for (var7 = 0; var7 < 9; ++var7)
            {
                this.addSlotToContainer(new Slot(par1InventoryPlayer, var7 + var6 * 9 + 9, 8 + var7 * 18, 155 + var6 * 18));
            }
        }

        for (var6 = 0; var6 < 9; ++var6)
        {
            this.addSlotToContainer(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 213));
        }

        tileEntity.openInventory(player);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityplayer)
    {
        super.onContainerClosed(entityplayer);
        this.tileEntity.closeInventory(entityplayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.isUseableByPlayer(par1EntityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        final Slot slot = (Slot) this.inventorySlots.get(par1);
        final int b = this.inventorySlots.size();

        if (slot != null && slot.getHasStack())
        {
            final ItemStack var4 = slot.getStack();
            var2 = var4.copy();

            if (par1 < b - 36)
            {
                if (!this.mergeItemStack(var4, b - 36, b, true))
                {
                    return null;
                }
            }
            else
            {
                if (var4.getItem() == Items.WATER_BUCKET)
                {
                    if (!this.mergeItemStack(var4, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (var4.getItem() == Items.DYE && var4.getItemDamage() == 15)
                {
                    if (!this.mergeItemStack(var4, 2, 6, false))
                    {
                        return null;
                    }
                }
                else if (this.getSlot(6).isItemValid(var4))
                {
                    if (!this.mergeItemStack(var4, 6, 10, false))
                    {
                        return null;
                    }
                }
                else if (var4.getItem() == Items.WHEAT_SEEDS)
                {
                    if (!this.mergeItemStack(var4, 10, 14, false))
                    {
                        return null;
                    }
                }
                else if (par1 < b - 9)
                {
                    if (!this.mergeItemStack(var4, b - 9, b, false))
                    {
                        return null;
                    }
                }
                else if (!this.mergeItemStack(var4, b - 36, b - 9, false))
                {
                    return null;
                }
            }

            if (var4.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (var4.stackSize == var2.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, var4);
        }

        return var2;
    }

    public static boolean isOnSaplingList(ItemStack stack)
    {
        if (saplingList == null)
        {
            initSaplingList();
        }

        for (ItemStack sapling : saplingList)
        {
            if (sapling.isItemEqual(stack))
            {
                return true;
            }
        }

        return false;
    }

    private static void initSaplingList()
    {
        ContainerTerraformer.saplingList = new LinkedList();
        Iterator iterator = Block.REGISTRY.iterator();

        while (iterator.hasNext())
        {
            Block b = (Block) iterator.next();

            if (b instanceof BlockBush)
            {
                try
                {
                    Item item = Item.getItemFromBlock(b);
                    if (item != null)
                    {
                        //item.getSubItems(item, null, subItemsList); - can't use because clientside only
                        ContainerTerraformer.saplingList.add(new ItemStack(item, 1, 0));
                        String basicName = item.getUnlocalizedName(new ItemStack(item, 1, 0));
                        for (int i = 1; i < 16; i++)
                        {
                            ItemStack testStack = new ItemStack(item, 1, i);
                            String testName = item.getUnlocalizedName(testStack);
                            if (testName == null || testName.equals("") || testName.equals(basicName))
                            {
                                break;
                            }
                            ContainerTerraformer.saplingList.add(testStack);
                        }
                    }
                }
                catch (Exception e)
                {
                }
            }
        }
    }
}