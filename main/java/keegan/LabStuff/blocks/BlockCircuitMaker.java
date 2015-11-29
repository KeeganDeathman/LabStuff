package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCircuitMaker extends Block implements ITileEntityProvider
{

	public BlockCircuitMaker(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
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
        this.side1 = this.blockIcon = par1IconRegister.registerIcon("labstuff:blockCircuitMaker");
        this.side2 = par1IconRegister.registerIcon("labstuff:circuitMakerSide");
        this.side3 = par1IconRegister.registerIcon("labstuff:blockCircuitMakerFront");
        this.side4 = par1IconRegister.registerIcon("labstuff:blockCircuitMakerBack");
    }
	
	@Override
	public IIcon getIcon(int side, int metadata)
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
			return this.side4;
		}
		//North
		else if(side == 3 && metadata == 0)
		{
			return this.side3;
		}
		else if(side == 2 && metadata == 0)
		{
			return this.side4;
		}
		//West
		else if(side == 5 && metadata == 3)
		{
			return this.side3;
		}
		else if(side == 4 && metadata == 3)
		{
			return this.side4;
		}
		//East
		else if(side == 4 && metadata == 1)
		{
			return this.side3;
		}
		else if(side == 5 && metadata == 1)
		{
			return this.side4;
		}
		return this.side1;
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
		System.out.println("Hello!");
		if(!world.isRemote)
		{
			player.openGui(LabStuffMain.instance, 6, world, x, y, z);
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int arg1)
	{
		return new TileEntityCircuitMaker();
	}



	   
}
