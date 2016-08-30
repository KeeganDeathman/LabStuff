package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.*;

public class BlockACPGag extends Block
{

	private int[] coreCoords;

	public BlockACPGag(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		coreCoords = new int[3];
	}

	public void setCore(int x, int y, int z, World world)
	{
		if(world.getBlock(x, y-1, z).equals(LabStuffMain.blockAcceleratorControlPanel))
			y -= 1;
		else if(world.getBlock(x, y, z-1).equals(LabStuffMain.blockAcceleratorControlPanel))
			z -= 1;
		else if(world.getBlock(x+1, y, z).equals(LabStuffMain.blockAcceleratorControlPanel))
			x += 1;
		else if(world.getBlock(x, y-1, z-1).equals(LabStuffMain.blockAcceleratorControlPanel))
		{
			y -= 1;
			z -= 1;
		}
		else if(world.getBlock(x+1, y-1, z).equals(LabStuffMain.blockAcceleratorControlPanel))
		{
			y -= 1;
			x += 1;
		}
		else if(world.getBlock(x, y, z-2).equals(LabStuffMain.blockAcceleratorControlPanel))
			z -= 2;
		else if(world.getBlock(x, y-1, z-2).equals(LabStuffMain.blockAcceleratorControlPanel))
		{
			y -= 1;
			z -= 2;
		}
		else if(world.getBlock(x+1, y, z).equals(LabStuffMain.blockACPGag))
		{
			if(world.getBlock(x, y-1, z).equals(LabStuffMain.blockACPGag))
			{
				y -= 1;
				if(world.getBlock(x+2, y, z).equals(LabStuffMain.blockAcceleratorControlPanel))
					x += 2;
				else if(world.getBlock(x+3, y, z).equals(LabStuffMain.blockAcceleratorControlPanel))
					x += 3;
			}
			else
			{
				if(world.getBlock(x+2, y, z).equals(LabStuffMain.blockAcceleratorControlPanel))
					x += 2;
				else if(world.getBlock(x+3, y, z).equals(LabStuffMain.blockAcceleratorControlPanel))
					x += 3;
			}
		}
		coreCoords[0] = x;
		coreCoords[1] = y;
		coreCoords[2] = z;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
	{
		if (!world.isRemote)
		{
			setCore(x, y, z, world);
			// System.out.println("Server");
			if (!player.isSneaking())
			{
				player.openGui(LabStuffMain.instance, 3, world, coreCoords[0], coreCoords[1], coreCoords[2]);
				return true;
			}
		}
		return false;
	}
	
	public boolean isOpaqueCube()
	{
		return false;
	}

	public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
		return false;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return false;
	}

}