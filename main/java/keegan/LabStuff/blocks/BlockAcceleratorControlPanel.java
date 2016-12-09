package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityAcceleratorControlPanel;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockAcceleratorControlPanel extends Block implements ITileEntityProvider {

	public BlockAcceleratorControlPanel() {
		super(Material.IRON);
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityAcceleratorControlPanel();
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
		if (!world.isRemote) {
			// System.out.println("Server");
			if (!player.isSneaking()) {
				player.openGui(LabStuffMain.instance, 11, world, pos.getX(), pos.getY(), pos.getZ());
				return true;
			}
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, player, stack);
		world.setBlockState(pos.subtract(new Vec3i(3, 0, 0)), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.subtract(new Vec3i(2,0,0)), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.subtract(new Vec3i(1,0,0)), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.subtract(new Vec3i(3, 0, 0)).add(0, 1, 0), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.subtract(new Vec3i(2, 0, 0)).add(0, 1, 0), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.subtract(new Vec3i(1, 0, 0)).add(0, 1, 0), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.add(0, 0, 2), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.add(0, 0, 1), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.add(0, 1, 2), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.add(0, 1, 1), LabStuffMain.blockACPGag.getDefaultState());
		world.setBlockState(pos.add(0, 1, 0), LabStuffMain.blockACPGag.getDefaultState());
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side) {
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess access, BlockPos pos) {
		return false;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		((TileEntityAcceleratorControlPanel) world.getTileEntity(pos)).collision();
	}

}
