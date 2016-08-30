package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityCircuitDesignTable;
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

public class BlockCircuitDesignTable extends Block implements ITileEntityProvider{

	public static Block instance;
	
	public BlockCircuitDesignTable(Material par2Material) 
	{
		super(par2Material);
	}
	
   
	//It's not an opaque cube, so you need this.
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
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
    	int l = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
    	if (l == 0)
    		onBlockPlacedBy(world, x, y, z, 0);

    	if (l == 1)
    		onBlockPlacedBy(world, x, y, z, 1);

        if (l == 2)
        	onBlockPlacedBy(world, x, y, z, 2);

        if (l == 3)
        	onBlockPlacedBy(world, x, y, z, 3);
    }
    
    public void onBlockPlacedBy(World world, int x, int y, int z, int meta)
    {
    	world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	System.out.println("Im awake!");
    	if(world.isRemote == false)
    	{
    		player.openGui(LabStuffMain.instance, 0, world, x, y, z);
    		return true;
    	}
    	return false;
    }


	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		// TODO Auto-generated method stub
		return new TileEntityCircuitDesignTable();
	}

}
