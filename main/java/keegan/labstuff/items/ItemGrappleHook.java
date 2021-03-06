package keegan.labstuff.items;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.entities.EntityGrapple;
import keegan.labstuff.util.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemGrappleHook extends ItemBow implements ISortableItem
{
    public ItemGrappleHook(String assetName)
    {
        super();
        this.setRegistryName("labstuff",assetName);
        this.setMaxStackSize(1);
        //this.setTextureName("arrow");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTab()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
    	return LabStuffClientProxy.labStuffItem;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entity, int timeLeft)
    {
        if (!(entity instanceof EntityPlayer))
        {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        boolean canShoot = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
        ItemStack string = null;

        for (ItemStack itemstack : player.inventory.mainInventory)
        {
            if (itemstack != null && itemstack.getItem() == Items.STRING)
            {
                string = itemstack;
                canShoot = true;
            }
        }

        if (canShoot)
        {
            if (string == null)
            {
                string = new ItemStack(Items.STRING, 1);
            }

            EntityGrapple grapple = new EntityGrapple(worldIn, player, 2.0F);

            worldIn.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (Item.itemRand.nextFloat() * 0.4F + 1.2F) + 0.5F);

            if (!worldIn.isRemote)
            {
                worldIn.spawnEntityInWorld(grapple);
            }

            stack.damageItem(1, player);
            grapple.canBePickedUp = player.capabilities.isCreativeMode ? 2 : 1;

            if (!player.capabilities.isCreativeMode)
            {
                --string.stackSize;

                if (string.stackSize == 0)
                {
                    player.inventory.deleteStack(string);
                }
            }
        }
        else if (worldIn.isRemote)
        {
            player.addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.message.grapple.fail")));
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        playerIn.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}