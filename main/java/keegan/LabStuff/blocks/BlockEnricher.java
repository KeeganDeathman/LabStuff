package keegan.labstuff.blocks;

import java.util.Random;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class BlockEnricher extends Block implements ITileEntityProvider {

	public BlockEnricher(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		return new TileEntityEnricher();
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		player.openGui(LabStuffMain.instance, 17, world, x, y, z);
    		return true;
    	}
    	return false;
    }
	
	@Override
	public void updateTick(World world, int x, int y, int z, Random rand)
	{
		((TileEntityEnricher)world.getTileEntity(x, y, z)).completeEnrichment();
	}
	
	private IIcon side1;
	private IIcon side2;
	private IIcon side3;
	private IIcon side4;
	
	@Override
    // registerIcons
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        // blockIcon - blockIcon
        this.side1 = this.blockIcon = par1IconRegister.registerIcon("labstuff:blockenrichertop");
        this.side2 = par1IconRegister.registerIcon("labstuff:furnace-generator");
        this.side3 = par1IconRegister.registerIcon("labstuff:enricher");
        this.side4 = par1IconRegister.registerIcon("labstuff:enricher-gear");
    }
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		switch(side)
		{
			case 0: case 1:
				return this.side1;
			case 2:
				if(metadata == 2)
					return side3;
				else if(metadata == 1)
					return side4;
				else
					return side2;
			case 3:
				if(metadata == 0)
					return side3;
				else if(metadata == 3)
					return side4;
				else
					return side2;
			case 4:
				if(metadata == 1)
					return side3;
				else if(metadata == 0)
					return side4;
				else
					return side2;
			case 5:
				if(metadata == 3)
					return side3;
				else if(metadata == 2)
					return side4;
				else
					return side2;
			default:
				return this.side1;
		}
	}

}
