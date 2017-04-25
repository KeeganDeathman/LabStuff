package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockACPGag extends Block
{


	public BlockACPGag(Material p_i45394_1_)
	{
		super(p_i45394_1_);
	}

	public BlockPos setCore(BlockPos pos, World world)
	{
		BlockPos coreCoords = pos;
		
		if(world.getBlockState(pos.subtract(new Vec3i(0,1,0))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
			coreCoords = coreCoords.subtract(new Vec3i(0,1,0));
		else if(world.getBlockState(pos.subtract(new Vec3i(0,0,1))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
			coreCoords = coreCoords.subtract(new Vec3i(0,0,1));
		else if(world.getBlockState(pos.add(1, 0, 1)).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
			coreCoords = coreCoords.add(1,0,1);
		else if(world.getBlockState(pos.add(1, 0, 0)).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
			coreCoords = coreCoords.add(1,0,0);
		else if(world.getBlockState(pos.subtract(new Vec3i(0,1,1))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
			coreCoords = coreCoords.subtract(new Vec3i(0,1,1));
		else if(world.getBlockState(pos.subtract(new Vec3i(0,1,0)).add(1,0,0)).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
		{
			coreCoords = coreCoords.subtract(new Vec3i(0,1,0)).add(1,0,0);
		}
		else if(world.getBlockState(pos.subtract(new Vec3i(0,0,2))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
			coreCoords = coreCoords.subtract(new Vec3i(0,0,2));
		else if(world.getBlockState(pos.subtract(new Vec3i(0,1,2))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
		{
			coreCoords = coreCoords.subtract(new Vec3i(0,1,2));
		}
		else if(world.getBlockState(pos.add(1,0,0)).getBlock().equals(LabStuffMain.blockACPGag))
		{
			if(world.getBlockState(pos.subtract(new Vec3i(0,1,0))).getBlock().equals(LabStuffMain.blockACPGag))
			{
				coreCoords = coreCoords.subtract(new Vec3i(0,1,0));
				if(world.getBlockState(pos.add(2,0,0).subtract(new Vec3i(0,1,0))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
					coreCoords = coreCoords.add(2,0,0);
				else if(world.getBlockState(pos.add(3,0,0).subtract(new Vec3i(0,1,0))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
					coreCoords = coreCoords.add(3,0,0);
				else if(world.getBlockState(pos.add(1,0,0).subtract(new Vec3i(0,1,0))).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
					coreCoords = coreCoords.add(1,0,0);
				
			}
			else
			{
				if(world.getBlockState(pos.add(2,0,0)).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
					coreCoords = coreCoords.add(2,0,0);
				else if(world.getBlockState(pos.add(3,0,0)).getBlock().equals(LabStuffMain.blockAcceleratorControlPanel))
					coreCoords = coreCoords.add(3,0,0);
			}
		}
		
		return coreCoords;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) {
		
		if (!world.isRemote)
		{
			BlockPos coreCoords = setCore(pos, world);
			// System.out.println("Server");
			if (!player.isSneaking())
			{
				player.openGui(LabStuffMain.instance, 3, world, coreCoords.getX(), coreCoords.getY(), coreCoords.getZ());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.INVISIBLE;
	}
	
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
		return false;
	}

	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess access, BlockPos pos) {
		return false;
	}

}