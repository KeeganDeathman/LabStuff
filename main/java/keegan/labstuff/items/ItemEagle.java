package keegan.labstuff.items;

import java.util.List;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.LabStuffClientProxy;
import keegan.labstuff.common.EnumColor;
import keegan.labstuff.entities.EntityTier1Rocket;
import keegan.labstuff.entities.IRocketType.EnumRocketType;
import keegan.labstuff.tileentity.TileEntityLandingPad;
import keegan.labstuff.util.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.*;

public class ItemEagle extends Item implements IHoldableItem, ISortableItem
{
    public ItemEagle(String assetName)
    {
        super();
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setRegistryName("labstuff",assetName);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
    	return LabStuffMain.tabLabStuffSpace;
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        boolean padFound = false;
        TileEntity tile = null;

        if (worldIn.isRemote && playerIn instanceof EntityPlayerSP)
        {
            LabStuffClientProxy.playerClientHandler.onBuild(8, (EntityPlayerSP) playerIn);
            return EnumActionResult.FAIL;
        }
        else
        {
            float centerX = -1;
            float centerY = -1;
            float centerZ = -1;

            for (int i = -1; i < 2; i++)
            {
                for (int j = -1; j < 2; j++)
                {
                    BlockPos pos1 = pos.add(i, 0, j);
                    IBlockState state = worldIn.getBlockState(pos1);
                    final Block id = state.getBlock();
                    int meta = id.getMetaFromState(state);

                    if (id == LabStuffMain.landingPadFull && meta == 0)
                    {
                        padFound = true;
                        tile = worldIn.getTileEntity(pos.add(i, 0, j));

                        centerX = pos.getX() + i + 0.5F;
                        centerY = pos.getY() + 0.4F;
                        centerZ = pos.getZ() + j + 0.5F;

                        break;
                    }
                }

                if (padFound)
                {
                    break;
                }
            }

            if (padFound)
            {
                //Check whether there is already a rocket on the pad
                if (tile instanceof TileEntityLandingPad)
                {
                    if (((TileEntityLandingPad) tile).getDockedEntity() != null)
                    {
                        return EnumActionResult.FAIL;
                    }
                }
                else
                {
                    return EnumActionResult.FAIL;
                }

                final EntityTier1Rocket spaceship = new EntityTier1Rocket(worldIn, centerX, centerY, centerZ, EnumRocketType.values()[stack.getItemDamage()]);

                spaceship.setPosition(spaceship.posX, spaceship.posY + spaceship.getOnPadYOffset(), spaceship.posZ);
                worldIn.spawnEntityInWorld(spaceship);

                if (stack.hasTagCompound() && stack.getTagCompound().hasKey("RocketFuel"))
                {
                    spaceship.fuelTank.fill(new FluidStack(LabStuffMain.kerosene, stack.getTagCompound().getInteger("RocketFuel")), true);
                }

                if (!playerIn.capabilities.isCreativeMode)
                {
                    stack.stackSize--;

                    if (stack.stackSize <= 0)
                    {
                        stack = null;
                    }
                }

                if (spaceship.rocketType.getPreFueled())
                {
                    spaceship.fuelTank.fill(new FluidStack(LabStuffMain.kerosene, spaceship.getMaxFuel()), true);
                }
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
        return EnumActionResult.PASS;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 0; i < EnumRocketType.values().length; i++)
        {
            par3List.add(new ItemStack(par1, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        return LabStuffClientProxy.labStuffItem;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer player, List<String> tooltip, boolean b)
    {
        EnumRocketType type = EnumRocketType.values()[par1ItemStack.getItemDamage()];

        if (!type.getTooltip().isEmpty())
        {
            tooltip.add(type.getTooltip());
        }

        if (type.getPreFueled())
        {
            tooltip.add(EnumColor.RED + "\u00a7o" + LabStuffUtils.translate("gui.creative_only.desc"));
        }

        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey("RocketFuel"))
        {
            EntityTier1Rocket rocket = new EntityTier1Rocket(FMLClientHandler.instance().getWorldClient(), 0, 0, 0, EnumRocketType.values()[par1ItemStack.getItemDamage()]);
            tooltip.add(LabStuffUtils.translate("gui.message.fuel.name") + ": " + par1ItemStack.getTagCompound().getInteger("RocketFuel") + " / " + rocket.fuelTank.getCapacity());
        }
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
        return EnumSortCategoryItem.ROCKET;
    }
}