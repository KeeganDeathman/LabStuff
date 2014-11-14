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
			System.out.println("Furnace is on, metadata " + metadata);
			return getIconOn(side, metadata);
		}
		else
		{
			System.out.println("Furnace is off, metadata " + metadata);
			return getIconOff(side, metadata);
		}
	}
	
	private IIcon getIconOff(int side, int metadata)
	{
		if(side == 0|| side == 1)
		{
			return this.side1;
		}
		else if(side == 4 && (metadata != 1 && metadata != 3))
		{
			return this.side2;
		}
		else if(side == 5 && (metadata != 1 && metadata !=3))
		{
			return this.side2;
		}
		else if(side == 2 && (metadata != 2 && metadata !=0))
		{
			return this.side2;
		}
		else if(side == 3 && (metadata != 0 && metadata !=2))
		{
			return this.side2;
		}
		//South
		else if(side == 2 && metadata == 2)
		{
			return this.side3;
		}
		else if(side == 3 && metadata == 2)
		{
			return this.side2;
		}
		//North
		else if(side == 3 && metadata == 0)
		{
			return this.side3;
		}
		else if(side == 2 && metadata == 0)
		{
			return this.side2;
		}
		//West
		else if(side == 5 && metadata == 3)
		{
			return this.side3;
		}
		else if(side == 4 && metadata == 3)
		{
			return this.side2;
		}
		//East
		else if(side == 4 && metadata == 1)
		{
			return this.side3;
		}
		else if(side == 5 && metadata == 1)
		{
			return this.side2;
		}
		return this.side1;
	}

	private IIcon getIconOn(int side, int metadata)
	{
		if(side == 4|| side == 5)
		{
			return this.side1;
		}
		else if(side == 4 && (metadata != 5 && metadata != 7))
		{
			return this.side2;
		}
		else if(side == 5 && (metadata != 5 && metadata !=7))
		{
			return this.side2;
		}
		else if(side == 2 && (metadata != 6 && metadata !=4))
		{
			return this.side2;
		}
		else if(side == 3 && (metadata != 4 && metadata !=6))
		{
			return this.side2;
		}
		//South
		else if(side == 2 && metadata == 6)
		{
			return this.side3On;
		}
		else if(side == 3 && metadata == 6)
		{
			return this.side2;
		}
		//North
		else if(side == 3 && metadata == 8)
		{
			return this.side3On;
		}
		else if(side == 2 && metadata == 8)
		{
			return this.side2;
		}
		//West
		else if(side == 5 && metadata == 7)
		{
			return this.side3On;
		}
		else if(side == 4 && metadata == 7)
		{
			return this.side2;
		}
		//East
		else if(side == 4 && metadata == 5)
		{
			return this.side3On;
		}
		else if(side == 5 && metadata == 5)
		{
			return this.side2;
		}
		return this.side1;
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
