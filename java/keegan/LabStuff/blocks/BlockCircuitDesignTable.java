package keegan.LabStuff.blocks;

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
import keegan.LabStuff.LabStuffMain;
import keegan.LabStuff.tileentity.TileEntityCircuitDesignTable;

public class BlockCircuitDesignTable extends Block implements ITileEntityProvider{

	public BlockCircuitDesignTable(int par1, Material par2Material) 
	{
		super(par2Material);
	}
	
   
	//It's not an opaque cube, so you need this.
    //@Override
    //public boolean isOpaqueCube() {
    //        return false;
    //}
    
    public boolean shouldSideBeRendered(IBlockAccess access, int i, int j, int k, int l)
	{
		return false;
	}
    
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, l, 2);
    }
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
    	player.openGui(LabStuffMain.instance, 4, world, x, y, z);
		return true;
    }

	@Override
	public TileEntity func_149915_a(World var1, int var2) {
		// TODO Auto-generated method stub
		return new TileEntityCircuitDesignTable();
	}

}
