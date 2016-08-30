package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.*;

public class BlockTurbine extends Block implements ITileEntityProvider
{

	public BlockTurbine(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		// TODO Auto-generated method stub
		if(getUnlocalizedName().equals("tile.blockTurbineRotor"))
			return new TileEntityTurbine();
		if(getUnlocalizedName().equals("tile.blockTurbineValve"))
			return new TileEntityTurbineValve();
		if(getUnlocalizedName().equals("tile.blockTurbineVent"))
			return new TileEntityTurbineVent();
		return null;
	}
	
	public boolean validCasing()
	{
		return (getUnlocalizedName().contains("Case") || getUnlocalizedName().contains("Glass") || getUnlocalizedName().contains("Valve") || getUnlocalizedName().contains("Vent"));
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par5, float par6, float par7, float par8)
	{
		System.out.println("Hello!");
		if(!world.isRemote)
		{
			if(player.getHeldItem().getItem().equals(LabStuffMain.itemTurbineBlades) && getUnlocalizedName().equals("tile.blockTurbineRotor"))
			{
				player.getHeldItem().stackSize -=1;
				world.setBlockMetadataWithNotify(x, y, z, world.getBlockMetadata(x, y, z)+1, 1+2);
				if(world.getBlockMetadata(x, y, z) == 3)
					world.setBlockMetadataWithNotify(x, y, z, 2, 1+2);
				return true;
			}
		}
		return false;
	}
	private IIcon glass;	
	private IIcon glass1;
	private IIcon glass2;
	private IIcon glass3;
	private IIcon glass4;
	private IIcon glass5;
	private IIcon glass6;
	private IIcon glass7;
	private IIcon glass8;
	private IIcon glass9;
	private IIcon glass1cd;
	private IIcon glass1cl;
	private IIcon glass1cu;
	private IIcon glass1cr;
	private IIcon glass2cv;
	private IIcon glass2ch;
	
	private IIcon case0;	
	private IIcon case1;
	private IIcon case2;
	private IIcon case3;
	private IIcon case4;
	private IIcon case5;
	private IIcon case6;
	private IIcon case7;
	private IIcon case8;
	private IIcon case9;
	private IIcon case1cd;
	private IIcon case1cl;
	private IIcon case1cu;
	private IIcon case1cr;
	private IIcon case2cv;
	private IIcon case2ch;
	
	private IIcon coil0;	
	private IIcon coil1;
	private IIcon coil2;
	private IIcon coil3;
	private IIcon coil4;
	private IIcon coil5;
	private IIcon coil6;
	private IIcon coil7;
	private IIcon coil8;
	private IIcon coil9;
	private IIcon coil1cd;
	private IIcon coil1cl;
	private IIcon coil1cu;
	private IIcon coil1cr;
	private IIcon coil2cv;
	private IIcon coil2ch;
	
	@Override
	public void registerBlockIcons(IIconRegister reg)
	{
		
		glass = reg.registerIcon("labstuff:turbineglass");
		glass1 = reg.registerIcon("labstuff:turbineglass1");
		glass2 = reg.registerIcon("labstuff:turbineglass2");
		glass3 = reg.registerIcon("labstuff:turbineglass3");
		glass4 = reg.registerIcon("labstuff:turbineglass4");
		glass5 = reg.registerIcon("labstuff:turbineglass5");
		glass6 = reg.registerIcon("labstuff:turbineglass6");
		glass7 = reg.registerIcon("labstuff:turbineglass7");
		glass8 = reg.registerIcon("labstuff:turbineglass8");
		glass9 = reg.registerIcon("labstuff:blockGag");
		glass1cd = reg.registerIcon("labstuff:turbineglass1cd");
		glass1cl = reg.registerIcon("labstuff:turbineglass1cl");
		glass1cu = reg.registerIcon("labstuff:turbineglass1cu");
		glass1cr = reg.registerIcon("labstuff:turbineglass1cr");
		glass2cv = reg.registerIcon("labstuff:turbineglass2cv");
		glass2ch = reg.registerIcon("labstuff:turbineglass2ch");
		
		case0 = reg.registerIcon("labstuff:turbinecasing0");
		case1 = reg.registerIcon("labstuff:turbinecasing1");
		case2 = reg.registerIcon("labstuff:turbinecasing2");
		case3 = reg.registerIcon("labstuff:turbinecasing3");
		case4 = reg.registerIcon("labstuff:turbinecasing4");
		case5 = reg.registerIcon("labstuff:turbinecasing5");
		case6 = reg.registerIcon("labstuff:turbinecasing6");
		case7 = reg.registerIcon("labstuff:turbinecasing7");
		case8 = reg.registerIcon("labstuff:turbinecasing8");
		case9 = reg.registerIcon("labstuff:turbinecasing9");
		case1cd = reg.registerIcon("labstuff:turbinecasing1cd");
		case1cl = reg.registerIcon("labstuff:turbinecasing1cl");
		case1cu = reg.registerIcon("labstuff:turbinecasing1cu");
		case1cr = reg.registerIcon("labstuff:turbinecasing1cr");
		case2cv = reg.registerIcon("labstuff:turbinecasing2cv");
		case2ch = reg.registerIcon("labstuff:turbinecasing2ch");
		
		coil0 = reg.registerIcon("labstuff:emcoil0");
		coil1 = reg.registerIcon("labstuff:emcoil1");
		coil2 = reg.registerIcon("labstuff:emcoil2");
		coil3 = reg.registerIcon("labstuff:emcoil3");
		coil4 = reg.registerIcon("labstuff:emcoil4");
		coil5 = reg.registerIcon("labstuff:emcoil5");
		coil6 = reg.registerIcon("labstuff:emcoil6");
		coil7 = reg.registerIcon("labstuff:emcoil7");
		coil8 = reg.registerIcon("labstuff:emcoil8");
		coil9 = reg.registerIcon("labstuff:emcoil9");
		coil1cd = reg.registerIcon("labstuff:emcoil1cd");
		coil1cl = reg.registerIcon("labstuff:emcoil1cl");
		coil1cu = reg.registerIcon("labstuff:emcoil1cu");
		coil1cr = reg.registerIcon("labstuff:emcoil1cr");
		coil2cv = reg.registerIcon("labstuff:emcoil2cv");
		coil2ch = reg.registerIcon("labstuff:emcoil2ch");
		
		if(getUnlocalizedName().contains("Glass"))
			this.blockIcon = glass;
		if(getUnlocalizedName().contains("Case"))
			this.blockIcon = case0;
		if(getUnlocalizedName().contains("Coil"))
			this.blockIcon = coil0;
		if(getUnlocalizedName().contains("Vent"))
			this.blockIcon = reg.registerIcon("labstuff:turbinevent");
		if(getUnlocalizedName().contains("Valve"))
			this.blockIcon = reg.registerIcon("labstuff:turbinevalve");
	}

	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
	{
		if(getUnlocalizedName().contains("Glass"))
		{
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			
			if(side == 0 || side == 1)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockTurbineGlass))
					right = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockTurbineGlass))
					left = true;
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockTurbineGlass))
					down = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockTurbineGlass))
					up = true;
			}
			if(side == 2)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockTurbineGlass))
					left = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockTurbineGlass))
					right = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineGlass))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineGlass))
					down = true;
			}
			if(side == 3)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockTurbineGlass))
					right = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockTurbineGlass))
					left = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineGlass))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineGlass))
					down = true;
			}
			if(side == 5)
			{
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockTurbineGlass))
					left = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockTurbineGlass))
					right = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineGlass))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineGlass))
					down = true;
			}
			if(side == 4)
			{
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockTurbineGlass))
					right = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockTurbineGlass))
					left = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineGlass))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineGlass))
					down = true;
			}
			
			if(up && down && left && right)
				return glass9;
			if(up && down && left)
				return glass4;
			if(up && down && right)
				return glass8;
			if(up && left && right)
				return glass6;
			if(down && left && right)
				return glass2;
			if(up && down)
				return glass2cv;
			if(right && left)
				return glass2ch;
			if(up && left)
				return glass5;
			if(up && right)
				return glass7;
			if(down && left)
				return glass3;
			if(down && right)
				return glass1;
			if(up)
				return glass1cu;
			if(down)
				return glass1cd;
			if(left)
				return glass1cl;
			if(right)
				return glass1cr;
		}
		
		if(getUnlocalizedName().contains("Case"))
		{
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			
			if(side == 0 || side == 1)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockTurbineCasing))
					right = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockTurbineCasing))
					left = true;
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockTurbineCasing))
					down = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockTurbineCasing))
					up = true;
			}
			if(side == 2)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockTurbineCasing))
					left = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockTurbineCasing))
					right = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineCasing))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineCasing))
					down = true;
			}
			if(side == 3)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockTurbineCasing))
					right = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockTurbineCasing))
					left = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineCasing))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineCasing))
					down = true;
			}
			if(side == 5)
			{
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockTurbineCasing))
					left = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockTurbineCasing))
					right = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineCasing))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineCasing))
					down = true;
			}
			if(side == 4)
			{
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockTurbineCasing))
					right = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockTurbineCasing))
					left = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockTurbineCasing))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockTurbineCasing))
					down = true;
			}
			
			if(up && down && left && right)
				return case9;
			if(up && down && left)
				return case4;
			if(up && down && right)
				return case8;
			if(up && left && right)
				return case6;
			if(down && left && right)
				return case2;
			if(up && down)
				return case2cv;
			if(right && left)
				return case2ch;
			if(up && left)
				return case5;
			if(up && right)
				return case7;
			if(down && left)
				return case3;
			if(down && right)
				return case1;
			if(up)
				return case1cu;
			if(down)
				return case1cd;
			if(left)
				return case1cl;
			if(right)
				return case1cr;
		}
		
		if(getUnlocalizedName().contains("Coil"))
		{
			boolean up = false;
			boolean down = false;
			boolean left = false;
			boolean right = false;
			
			if(side == 0 || side == 1)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockElectromagneticCoil))
					right = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockElectromagneticCoil))
					left = true;
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockElectromagneticCoil))
					down = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockElectromagneticCoil))
					up = true;
			}
			if(side == 2)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockElectromagneticCoil))
					left = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockElectromagneticCoil))
					right = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockElectromagneticCoil))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockElectromagneticCoil))
					down = true;
			}
			if(side == 3)
			{
				if(access.getBlock(x+1, y, z).equals(LabStuffMain.blockElectromagneticCoil))
					right = true;
				if(access.getBlock(x-1, y, z).equals(LabStuffMain.blockElectromagneticCoil))
					left = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockElectromagneticCoil))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockElectromagneticCoil))
					down = true;
			}
			if(side == 5)
			{
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockElectromagneticCoil))
					left = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockElectromagneticCoil))
					right = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockElectromagneticCoil))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockElectromagneticCoil))
					down = true;
			}
			if(side == 4)
			{
				if(access.getBlock(x, y, z+1).equals(LabStuffMain.blockElectromagneticCoil))
					right = true;
				if(access.getBlock(x, y, z-1).equals(LabStuffMain.blockElectromagneticCoil))
					left = true;
				if(access.getBlock(x, y+1, z).equals(LabStuffMain.blockElectromagneticCoil))
					up = true;
				if(access.getBlock(x, y-1, z).equals(LabStuffMain.blockElectromagneticCoil))
					down = true;
			}
			
			if(up && down && left && right)
				return coil9;
			if(up && down && left)
				return coil4;
			if(up && down && right)
				return coil8;
			if(up && left && right)
				return coil6;
			if(down && left && right)
				return coil2;
			if(up && down)
				return coil2cv;
			if(right && left)
				return coil2ch;
			if(up && left)
				return coil5;
			if(up && right)
				return coil7;
			if(down && left)
				return coil3;
			if(down && right)
				return coil1;
			if(up)
				return coil1cu;
			if(down)
				return coil1cd;
			if(left)
				return coil1cl;
			if(right)
				return coil1cr;
		}
		
		return this.blockIcon;
	}
	
	
	@Override
	public boolean isOpaqueCube()
	{
		if(getUnlocalizedName().contains("Glass") || getUnlocalizedName().contains("Rotor"))
			return false;
		return true;
	}
	
	 public boolean renderAsNormalBlock()
	 {
		 return false;
	 }
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		if(getUnlocalizedName().contains("Rotor"))
			return false;
		if(world.getBlock(x, y, z).equals(LabStuffMain.blockTurbineGlass) && getUnlocalizedName().contains("Glass"))
			return false;
		return true;
	}

}
