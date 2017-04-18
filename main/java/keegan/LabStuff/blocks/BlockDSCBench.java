package keegan.labstuff.blocks;

import java.util.List;

import org.apache.http.util.LangUtils;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.EnumColor;
import keegan.labstuff.tileentity.DSCBench;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDSCBench extends Block implements ITileEntityProvider
{

	public BlockDSCBench(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new DSCBench();
	}
	
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
		if (!world.isRemote)
		{
			// System.out.println("Server");
			if (!player.isSneaking())
			{
				// System.out.println("All systems are go!");
				player.openGui(LabStuffMain.instance, 13, world, pos.getX(), pos.getY(), pos.getZ());
				// System.out.println("Gui opened");
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void addInformation(ItemStack itemstack, EntityPlayer entityplayer, List<String> list, boolean flag)
	{
		super.addInformation(itemstack, entityplayer, list, flag);

		list.add("Requires 10 RAM");
	}
	


	
}
