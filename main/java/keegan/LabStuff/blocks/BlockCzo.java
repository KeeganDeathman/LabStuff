package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityCzo;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCzo extends Block implements ITileEntityProvider
{

	public BlockCzo(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
	}

	
	@Override
    public boolean isOpaqueCube() 
    {
            return false;
    }
    
    public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityCzo();
	}
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		((TileEntityCzo)world.getTileEntity(x, y, z)).completeGrowth();
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
	{
		System.out.println("Hello!");
		if(!world.isRemote)
		{
			player.openGui(LabStuffMain.instance, 8, world, x, y, z);
			return true;
		}
		return false;
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

}
