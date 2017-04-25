package keegan.labstuff.items;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.config.ConfigManagerCore;
import keegan.labstuff.dimension.WorldProviderSpaceStation;
import keegan.labstuff.entities.EntityAstroMiner;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemAstroMiner extends Item implements IHoldableItem, ISortableItem
{
    public ItemAstroMiner(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setMaxStackSize(1);
        this.setRegistryName("labstuff",assetName);
        //this.setTextureName("arrow");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
    	return LabStuffClientProxy.labStuffItem;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public CreativeTabs getCreativeTab()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        TileEntity tile = null;

        if (worldIn.isRemote || playerIn == null)
        {
            return EnumActionResult.PASS;
        }
        else
        {
            final Block id = worldIn.getBlockState(pos).getBlock();

            if (id == LabStuffMain.fakeBlock)
            {
                tile = worldIn.getTileEntity(pos);

                if (tile instanceof TileEntityMulti)
                {
                    tile = ((TileEntityMulti) tile).getMainBlockTile();
                }
            }

            if (id == LabStuffMain.minerBaseFull)
            {
                tile = worldIn.getTileEntity(pos);
            }

            if (tile instanceof TileEntityMinerBase)
            {
                if (worldIn.provider instanceof WorldProviderSpaceStation)
                {
                    playerIn.addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.message.astro_miner7.fail")));
                    return EnumActionResult.FAIL;
                }

                if (((TileEntityMinerBase) tile).getLinkedMiner() != null)
                {
                    playerIn.addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.message.astro_miner.fail")));
                    return EnumActionResult.FAIL;
                }

                //Gives a chance for any loaded Astro Miner to link itself
                if (((TileEntityMinerBase) tile).ticks < 15L)
                {
                    return EnumActionResult.FAIL;
                }

                EntityPlayerMP playerMP = (EntityPlayerMP) playerIn;
                LSPlayerStats stats = LSPlayerStats.get(playerIn);

                int astroCount = stats.getAstroMinerCount();
                if (astroCount >= ConfigManagerCore.astroMinerMax && (!playerIn.capabilities.isCreativeMode))
                {
                    playerIn.addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.message.astro_miner2.fail")));
                    return EnumActionResult.FAIL;
                }

                if (!((TileEntityMinerBase) tile).spawnMiner(playerMP))
                {
                    playerIn.addChatMessage(new TextComponentString(LabStuffUtils.translate("gui.message.astro_miner1.fail") + " " + LabStuffUtils.translate(EntityAstroMiner.blockingBlock.toString())));
                    return EnumActionResult.FAIL;
                }

                if (!playerIn.capabilities.isCreativeMode)
                {
                    stats.setAstroMinerCount(stats.getAstroMinerCount() + 1);
                    --stack.stackSize;
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List<String> tooltip, boolean b)
    {
        //TODO
    }

    @Override
    public boolean shouldHoldLeftHandUp(EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean shouldHoldRightHandUp(EntityPlayer player)
    {
        return true;
    }

    @Override
    public boolean shouldCrouch(EntityPlayer player)
    {
        return true;
    }

    @Override
    public EnumSortCategoryItem getCategory(int meta)
    {
        return EnumSortCategoryItem.GENERAL;
    }
}