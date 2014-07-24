package keegan.labstuff.blocks;

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
	
	public void onBlockPlacedBy(World par1World, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
	{
		int whichDirectionFacing = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
	    par1World.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
	    System.out.println(whichDirectionFacing);
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
	{
		System.out.println("Hello!");
		if(world.isRemote == true)
		{
			player.addChatMessage(new ChatComponentTranslation("text.broken.idiot"));
			return true;
		}
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int arg1)
	{
		return new TileEntityCircuitMaker(world);
	}



	   
}
