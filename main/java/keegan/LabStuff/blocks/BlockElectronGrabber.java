package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityElectronGrabber;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockElectronGrabber extends Block implements ITileEntityProvider
{

	public Block[][][] multiblocks;


	public BlockElectronGrabber(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) 
	{
		return new TileEntityElectronGrabber();
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
    	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	if (l == 0)
    		world.setBlockMetadataWithNotify(x, y, z, 0, 2);

    	if (l == 1)
    		world.setBlockMetadataWithNotify(x, y, z, 1, 2);

        if (l == 2)
        	world.setBlockMetadataWithNotify(x, y, z, 2, 2);

        if (l == 3)
        	world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        
    }
	
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
	{
		super.removedByPlayer(world, player, x, y, z, false);
		if (multiblocks[0][0][0] != null) 
		{
			for (int i = 0; i < 3; i++) 
			{
				for (int j = 0; j < 3; j++) 
				{
					for (int k = 0; k < 3; k++) 
					{
						if (multiblocks[i][j][k] != null && multiblocks[i][j][k] == LabStuffMain.blockGasChamberWall) 
						{
							((BlockGasChamberWall) multiblocks[i][j][k]).setMultiBlockState(false, null);
						}
						else if (multiblocks[i][j][k] != null && multiblocks[i][j][k] == LabStuffMain.blockGasChamberPort) 
						{
							((BlockGasChamberPort) multiblocks[i][j][k]).setInputState(false);
						}
					}
				}
			}
		}
		return true;
	}

}
