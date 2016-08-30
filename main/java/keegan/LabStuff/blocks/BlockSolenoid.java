package keegan.labstuff.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSolenoid extends Block {

	public BlockSolenoid(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return false;
		
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side)
	{
		if(access.getBlockMetadata(x - ForgeDirection.getOrientation(side).offsetX, y - ForgeDirection.getOrientation(side).offsetY, z - ForgeDirection.getOrientation(side).offsetZ) == 1)
			return false;
		return true;
	}
	
	private IIcon side1;
	private IIcon side2;
	private IIcon side3;
	
	@Override
    // registerIcons
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        // blockIcon - blockIcon
		this.side1 = this.blockIcon = par1IconRegister.registerIcon("labstuff:blockSolenoid");
        this.side2 = par1IconRegister.registerIcon("labstuff:blockSolenoidVert");
        this.side3 = par1IconRegister.registerIcon("labStuff:blockSolenoidHoriz");
	}
	
	@Override
	public IIcon getIcon(int side, int metadata)
	{
		if(getUnlocalizedName().endsWith("Arm"))
		{
			if(side == 0 || side == 1)
				return side2;
			if(side == 2 || side == 3)
				return side2;
			return side1;
		}
		if(side == 0 || side == 1)
			return side1;
		if(side == 2 || side == 3)
			return side3;
		return side3;
	}
	
	

}
