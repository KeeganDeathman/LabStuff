package keegan.labstuff.blocks;

import java.lang.reflect.Method;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.capabilities.IWrenchable;
import keegan.labstuff.util.CompatibilityManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * An advanced block class that is to be extended for wrenching capabilities.
 */
public abstract class BlockAdvanced extends Block
{
    public BlockAdvanced(Material material)
    {
        super(material);
        this.setHardness(0.6f);
        this.setResistance(2.5F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (hand != EnumHand.MAIN_HAND)
        {
            return false;
        }

        /**
         * Check if the player is holding a wrench or an electric item. If so,
         * call the wrench event.
         */
        if (this.isUsableWrench(playerIn, heldItem, pos))
        {
            this.damageWrench(playerIn, heldItem, pos);

            if (playerIn.isSneaking())
            {
                if (this.onSneakUseWrench(worldIn, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ))
                {
                    playerIn.swingArm(hand);
                    return !worldIn.isRemote;
                }
            }

            if (this.onUseWrench(worldIn, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ))
            {
                playerIn.swingArm(hand);
                return !worldIn.isRemote;
            }
        }

        if (playerIn.isSneaking())
        {
            if (this.onSneakMachineActivated(worldIn, pos, playerIn, hand, heldItem, side, hitX, hitY, hitZ))
            {
                return true;
            }
        }

        return this.onMachineActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    /**
     * A function that denotes if an itemStack is a wrench that can be used.
     * Override this for more wrench compatibility. Compatible with Buildcraft
     * and IC2 wrench API via reflection.
     *
     * @return True if it is a wrench.
     */
    public boolean isUsableWrench(EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (entityPlayer != null && itemStack != null)
        {
            Item item = itemStack.getItem();
            if (item == LabStuffMain.itemWrench) return true;
            
            Class<? extends Item> wrenchClass = item.getClass();

            /**
             * UE and Buildcraft
             */
            try
            {
                Method methodCanWrench = wrenchClass.getMethod("canWrench", EntityPlayer.class, BlockPos.class);
                return (Boolean) methodCanWrench.invoke(item, entityPlayer, pos);
            }
            catch (NoClassDefFoundError e)
            {
            }
            catch (Exception e)
            {
            }

            if (CompatibilityManager.isIc2Loaded())
            {
            /**
             * Industrialcraft
             */
            try
            {
                if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                {
                    return itemStack.getItemDamage() < itemStack.getMaxDamage();
                }
            }
            catch (Exception e)
            {
            }
            }
        }

        return false;
    }

    /**
     * This function damages a wrench. Works with Buildcraft and Industrialcraft
     * wrenches.
     *
     * @return True if damage was successfull.
     */
    public boolean damageWrench(EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos)
    {
        if (this.isUsableWrench(entityPlayer, itemStack, pos))
        {
            Class<? extends Item> wrenchClass = itemStack.getItem().getClass();

            /**
             * UE and Buildcraft
             */
            try
            {
                Method methodWrenchUsed = wrenchClass.getMethod("wrenchUsed", EntityPlayer.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                methodWrenchUsed.invoke(itemStack.getItem(), entityPlayer, pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
            catch (Exception e)
            {
            }

            /**
             * Industrialcraft
             */
            try
            {
                if (wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrench") || wrenchClass == Class.forName("ic2.core.item.tool.ItemToolWrenchElectric"))
                {
                    Method methodWrenchDamage = wrenchClass.getMethod("damage", ItemStack.class, Integer.TYPE, EntityPlayer.class);
                    methodWrenchDamage.invoke(itemStack.getItem(), itemStack, 1, entityPlayer);
                    return true;
                }
            }
            catch (Exception e)
            {
            }
        }

        return false;
    }

    /**
     * Called when the machine is right clicked by the player
     *
     * @return True if something happens
     */
    public boolean onMachineActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called when the machine is being wrenched by a player while sneaking.
     *
     * @return True if something happens
     */
    public boolean onSneakMachineActivated(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called when a player uses a wrench on the machine
     *
     * @return True if some happens
     */
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return false;
    }

    /**
     * Called when a player uses a wrench on the machine while sneaking. Only
     * works with the UE wrench.
     *
     * @return True if some happens
     */
    public boolean onSneakUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return this.onUseWrench(world, pos, entityPlayer, hand, heldItem, side, hitX, hitY, hitZ);
    }

}