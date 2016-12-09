package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityGravityManipulater;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockGravityManipulater extends Block implements ITileEntityProvider
{

	public BlockGravityManipulater(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntityGravityManipulater();
	}

	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
		if (!world.isRemote)
		{
			// System.out.println("Server");
			TileEntityGravityManipulater te = (TileEntityGravityManipulater) world.getTileEntity(pos);
			if (!player.isSneaking() && te != null)
			{
				// System.out.println("All systems are go!");
				player.openGui(LabStuffMain.instance, 16, world, pos.getX(), pos.getY(), pos.getZ());
				// System.out.println("Gui opened");
				return true;
			}
		}
		return false;
	}

}
