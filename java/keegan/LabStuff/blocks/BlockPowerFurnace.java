package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityPowerFurnace;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockPowerFurnace extends Block implements ITileEntityProvider
{
	
	public BlockPowerFurnace(Material mat)
	{
		super(mat);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		player.openGui(LabStuffMain.instance, 7, world, x, y, z);
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
	
	private IIcon side1;
	private IIcon side2;
	private IIcon side3;
	private IIcon side3On;
	
	@Override
    // registerIcons
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        // blockIcon - blockIcon
        this.side1 = this.blockIcon = par1IconRegister.registerIcon("labstuff:blockCircuitMaker");
        this.side2 = par1IconRegister.registerIcon("labstuff:furnace-generator");
        this.side3 = par1IconRegister.registerIcon("labstuff:furnace-generator-front");
        this.side3On = par1IconRegister.registerIcon("labstuff:furnace-generator-front-on");
    }
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if(isOn(metadata))
		{
			return getIconOn(side, metadata);
		}
		else
		{
			return getIconOff(side, metadata);
		}
	}
	
	private IIcon getIconOff(int side, int metadata)
	{
		switch(side)
		{
			case 0: case 1:
				return this.side1;
			case 2:
				switch(metadata)
				{
					case(2):
						return this.side3;
					default:
						return this.side2;
				}
			case 3:
				switch(metadata)
				{
					case(0):
						return this.side3;
					default:
						return this.side2;
				}
			case 4:
				switch(metadata)
				{
					case 1:
						return this.side3;
					default:
						return this.side2;
				}
			case 5:
				switch(metadata)
				{
					case 3:
						return this.side3;
					default:
						return this.side2;
				}
			default:
				return this.side1;
		}
	}

	private IIcon getIconOn(int side, int metadata)
	{
		switch(side)
		{
			case 0: case 1:
				return this.side1;
			case 2:
				switch(metadata)
				{
					case 6:
						return this.side3;
					default:
						return this.side2;
				}
			case 3:
				switch(metadata)
				{
					case 4:
						return this.side3;
					default:
						return this.side2;
				}
			case 4:
				switch(metadata)
				{
					case 5:
						return this.side3;
					default:
						return this.side2;
				}
			case 5:
				switch(metadata)
				{
					case 7:
						return this.side3;
					default:
						return this.side2;
				}
			default:
				return this.side1;
		}
	}

	
	
	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) 
	{
		// TODO Auto-generated method stub
		return new TileEntityPowerFurnace();
	}

	public boolean isOn(World world, int x, int y, int z) 
	{
		return (world.getBlockMetadata(x, y, z) > 3);
	}
	public boolean isOn(int metadata) 
	{
		return (metadata > 3);
	}

	public void setOn(World world, int x, int y, int z, boolean on) {
		if(on)
		{
			if(isOn(world, x, y, x))
				return;
			else
				world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x,y,z) + 4, 2);
		}
		else
		{
			if(isOn(world, x, y, z))
			{
				world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z) - 4, 2);
			}
		}
	}
	
	
}
