package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.TileEntityIndustrialMotorContact;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockIndustrialMotorContact extends Block implements ITileEntityProvider
{

	public BlockIndustrialMotorContact(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return new TileEntityIndustrialMotorContact();
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
		
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
	{
		if(access.getBlockMetadata(x - ForgeDirection.getOrientation(side).offsetX, y - ForgeDirection.getOrientation(side).offsetY, z - ForgeDirection.getOrientation(side).offsetZ) == 1)
			return false;
		return true;
	}

}
