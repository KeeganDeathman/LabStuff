package keegan.labstuff.blocks;

import keegan.labstuff.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSolenoidAxel extends Block implements ITileEntityProvider
{

	public BlockSolenoidAxel(Material p_i45394_1_)
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_)
	{
		// TODO Auto-generated method stub
		return new TileEntitySolenoidAxel();
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
		
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
    {
    	if(!world.isRemote)
    	{
    		TileEntity tile = world.getTileEntity(x, y-2, z);
    		if(tile instanceof TileEntitySolenoidAxel)
    		{
    			if(((TileEntitySolenoidAxel)tile).isMultiBlock() && ((TileEntitySolenoidAxel)tile).getEnergy() >= 250000)
    				player.addChatMessage(new ChatComponentText("Spinning"));
    			else
    					player.addChatMessage(new ChatComponentText("Still"));
    			return true;
    		}
    		return false;
    	}
    	return false;
    }
	
	private IIcon side1;
	private IIcon side2;
	
	@Override
    // registerIcons
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        // blockIcon - blockIcon
        this.side1 = this.blockIcon = par1IconRegister.registerIcon("labstuff:blockSolenoidAxel");
        this.side2 = par1IconRegister.registerIcon("labstuff:blockSolenoidVert");
    }
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if(side == 0|| side == 1)
		{
			return this.side1;
		}
		return this.side2;
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
	{
		if(access.getBlockMetadata(x - ForgeDirection.getOrientation(side).offsetX, y - ForgeDirection.getOrientation(side).offsetY, z - ForgeDirection.getOrientation(side).offsetZ) == 1)
			return false;
		return true;
	}
	
	

}
