package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityReservoir;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BlockReservoir extends Block implements ITileEntityProvider
{

	public BlockReservoir(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{ 
		return new TileEntityReservoir();
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer ep, EnumHand hand, ItemStack held, EnumFacing facing, float fx, float par8, float par9) 
	{
		ItemStack stack = ep.inventory.getCurrentItem();
		if(FluidContainerRegistry.isBucket(stack))
		{
			if(FluidContainerRegistry.isEmptyContainer(stack))
			{
				TileEntityReservoir te = (TileEntityReservoir)world.getTileEntity(pos);
				if(te != null)
				{
					if(te.tank.getFluidAmount() >= FluidContainerRegistry.BUCKET_VOLUME)
					{
						ep.inventory.decrStackSize(ep.inventory.currentItem, 1);
						ep.inventory.addItemStackToInventory(FluidContainerRegistry.fillFluidContainer(te.tank.getFluid(), stack));
						te.drain(null, new FluidStack(te.tank.getFluid(), FluidContainerRegistry.BUCKET_VOLUME), true);
					}
				}
			}
			else
			{
				TileEntityReservoir te = (TileEntityReservoir)world.getTileEntity(pos);
				if(te != null)
				{
					if(te.tank.getFluidAmount() <= te.tank.getCapacity() - FluidContainerRegistry.BUCKET_VOLUME)
					{
						te.fill(null, FluidContainerRegistry.getFluidForFilledItem(stack), true);
						ep.inventory.decrStackSize(ep.inventory.currentItem, 1);
						ep.inventory.addItemStackToInventory(FluidContainerRegistry.drainFluidContainer(stack));
						
					}
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

}
