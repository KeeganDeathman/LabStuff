package keegan.labstuff.multipart;

import java.util.List;

import org.apache.http.util.LangUtils;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.EnumColor;
import keegan.labstuff.items.IMetaItem;
import keegan.labstuff.network.*;
import mcmultipart.item.ItemMultiPart;
import mcmultipart.multipart.IMultipart;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemPartTransmitter extends ItemMultiPart implements IMetaItem
{
	public ItemPartTransmitter()
	{
		super();
		setHasSubtypes(true);
		setCreativeTab(LabStuffMain.tabLabStuff);
	}
	
	@Override
	public IMultipart createPart(World world, BlockPos pos, EnumFacing dir, Vec3d hit, ItemStack stack, EntityPlayer player)
	{
		return PartTransmitter.getPartType(TransmitterType.values()[getDamage(stack)]);
	}

	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag)
	{}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> listToAddTo)
	{
		for(TransmitterType type : TransmitterType.values())
		{
			listToAddTo.add(new ItemStack(item, 1, type.ordinal()));
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return getUnlocalizedName() + "." + TransmitterType.values()[stack.getItemDamage()].getName();
	}

	@Override
	public String getTexture(int meta) 
	{
		return TransmitterType.values()[meta].name();
	}

	@Override
	public int getVariants() 
	{
		return TransmitterType.values().length;
	}
}