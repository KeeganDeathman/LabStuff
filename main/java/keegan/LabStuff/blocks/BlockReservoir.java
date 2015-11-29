package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityReservoir;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;

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
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer ep, int p6, float p7, float p8, float p9)
	{
		ItemStack stack = ep.inventory.getCurrentItem();
		if(FluidContainerRegistry.isBucket(stack))
		{
			if(FluidContainerRegistry.isEmptyContainer(stack))
			{
				TileEntityReservoir te = (TileEntityReservoir)world.getTileEntity(x, y, z);
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
				TileEntityReservoir te = (TileEntityReservoir)world.getTileEntity(x, y, z);
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

}
