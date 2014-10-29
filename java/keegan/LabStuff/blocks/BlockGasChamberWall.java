package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.TileEntityElectronCannon;
import keegan.labstuff.tileentity.TileEntityElectronGrabber;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGasChamberWall extends Block 
{
	
	public boolean multiblock = false;
	public boolean visibility = true;
	private World world;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int tileX;
	private int tileY;
	private int tileZ;
	private boolean coordsGiven = false;
	private Block[][][] multiblocks;
	
	public BlockGasChamberWall(Material p_i45394_1_) 
	{
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
	}
	
	
	public boolean isMultiBlock()
	{
		boolean tileEntityPresent = false;
		if (world != null) {
			world.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		if(multiblocks != null && coordsGiven)
		{
			if(world.getTileEntity(tileX, tileY, tileZ) != null)
			{
				TileEntity tile = world.getTileEntity(tileX, tileY, tileZ);
				if(tile != null && (tile instanceof TileEntityElectronGrabber || tile instanceof TileEntityElectronCannon))
				{
					tileEntityPresent = true;
				}
				else
				{
					tileEntityPresent = false;
					System.out.println("Tile entity not found");
				}
			}
		}
		return multiblock && tileEntityPresent;
	}
	
	public void onBlockAdded(World world, int x, int y, int z)
	{
		System.out.println("X " + xCoord);
		System.out.println("Y " + yCoord);
		System.out.println("Z " + zCoord);
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}
	
	
	public void setMultiBlockState(boolean state, Block[][][] array)
	{
		multiblock = state;
		multiblocks = array;
		coordsGiven = false;
		System.out.println("Multiblock? " + isMultiBlock());
	}
	
	public void setMultiBlockState(boolean state, Block[][][] array, int[] tileCoords, World worldObj)
	{
		multiblock = state;
		multiblocks = array;
		tileX = tileCoords[0];
		tileY = tileCoords[1];
		tileZ = tileCoords[2];
		this.world = worldObj;
		coordsGiven = true;
		System.out.println("Multiblock? " + isMultiBlock());
	}
	
	private IIcon blank;
	private IIcon ul;
	private IIcon uc;
	private IIcon ur;
	private IIcon cl;
	private IIcon cc;
	private IIcon cr;
	private IIcon ll;
	private IIcon lc;
	private IIcon lr;
	private IIcon[][] icons = new IIcon[3][3];
	
	@Override
    // registerIcons
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        // blockIcon - blockIcon
		this.blank = this.blockIcon = par1IconRegister.registerIcon("labstuff:gaschamber0");
		this.ul = par1IconRegister.registerIcon("labstuff:gaschamber5");
		this.uc = par1IconRegister.registerIcon("labstuff:gaschamber6");
		this.ur = par1IconRegister.registerIcon("labstuff:gaschamber7");
		this.cl = par1IconRegister.registerIcon("labstuff:gaschamber4");
		this.cc = par1IconRegister.registerIcon("labstuff:gaschamber9");
		this.cr = par1IconRegister.registerIcon("labstuff:gaschamber8");
		this.ll = par1IconRegister.registerIcon("labstuff:gaschamber3");
		this.lc = par1IconRegister.registerIcon("labstuff:gaschamber2");
		this.lr = par1IconRegister.registerIcon("labstuff:gaschamber1");
		this.icons[0][0] = this.ul;
	    this.icons[0][1] = this.uc;
	    this.icons[0][2] = this.ur;
	    this.icons[1][0] = this.cl;
	    this.icons[1][1] = this.cc;
	    this.icons[1][2] = this.cr;
	    this.icons[2][0] = this.ll;
	    this.icons[2][1] = this.lc;
	    this.icons[2][2] = this.lr;
    }
	
	@Override
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side)
    {
		if (isMultiBlock()) 
		{
			if (side == 1 || side == 0) {
				if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort) {
					return icons[2][2];
				}
				if (access.getBlock(x, y, z + 1) == LabStuffMain.blockGasChamberPort) {
					return icons[2][1];
				}
				if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort) {
					return icons[2][0];
				}
				if (access.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberPort) {
					return icons[1][2];
				}
				if (access.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberPort) {
					return icons[1][0];
				}
				if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort) {
					return icons[0][0];
				}
				if (access.getBlock(x, y, z - 1) == LabStuffMain.blockGasChamberPort) {
					return icons[0][1];
				}
				if (access.getBlock(x + 1, y, z - 1) == LabStuffMain.blockGasChamberPort) {
					return icons[0][2];
				}

				return this.blank;
			}
			if (access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
					|| access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberPort
					|| access.getBlock(x, y - 1, z) == LabStuffMain.blockElectronCannon
					|| access.getBlock(x, y - 1, z) == LabStuffMain.blockElectronGrabber) 
			{
				if (side == 2 || side == 3) {
					if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 3) {
						return icons[2][2];
					}
					if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 2) {
						return icons[2][0];
					}
					if (access.getBlock(x, y, z + 1) == LabStuffMain.blockGasChamberPort) {
						return icons[2][1];
					}
					if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 3) {
						return icons[2][0];
					}
					if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 2) {
						return icons[2][2];
					}
					if (access.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberPort && side == 3) {
						return icons[2][2];
					}
					if (access.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberPort && side == 2) {
						return icons[2][0];
					}
					if (access.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberPort && side == 3) {
						return icons[2][0];
					}
					if (access.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberPort && side == 2) {
						return icons[2][2];
					}
					if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 3) {
						return icons[2][0];
					}
					if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 2) {
						return icons[2][2];
					}
					if (access.getBlock(x, y, z - 1) == LabStuffMain.blockGasChamberPort) {
						return icons[2][1];
					}
					if (access.getBlock(x + 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 3) {
						return icons[2][2];
					}
					if (access.getBlock(x + 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 2) {
						return icons[2][0];
					}
				}
				if (side == 4 || side == 5) {
					if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 5) {
						return icons[2][0];
					}
					if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 4) {
						return icons[2][2];
					}
					if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 5) {
						return icons[2][0];
					}
					if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 4) {
						return icons[2][2];
					}
					if (access.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberPort) {
						return icons[2][1];
					}
					if (access.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberPort) {
						return icons[2][1];
					}
					if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 5) {
						return icons[2][2];
					}
					if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 4) {
						return icons[2][0];
					}
				}
				if (side == 2 || side == 3) {
					if ((access.getBlock(x + 1, y, z) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x + 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x - 1, y, z) != LabStuffMain.blockGasChamberWall
							&& side == 3) {
						return icons[1][2];
					}
					if ((access.getBlock(x - 1, y, z) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x - 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x + 1, y, z) != LabStuffMain.blockGasChamberWall
							&& side == 2) {
						return icons[1][2];
					}
					if ((access.getBlock(x + 1, y, z) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x + 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x - 1, y, z) != LabStuffMain.blockGasChamberWall
							&& side == 2) {
						return icons[1][0];
					}
					if ((access.getBlock(x - 1, y, z) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x - 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x + 1, y, z) != LabStuffMain.blockGasChamberWall
							&& side == 3) {
						return icons[1][0];
					}
					if (access.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall) {
						return icons[1][1];
					}
				}
				if (side == 4 || side == 5) {
					if ((access.getBlock(x, y, z + 1) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x + 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x, y, z + 1) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y, z - 1) != LabStuffMain.blockGasChamberWall
							&& side == 4) {
						return icons[1][2];
					}
					if ((access.getBlock(x, y, z + 1) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x + 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x, y, z + 1) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y, z - 1) != LabStuffMain.blockGasChamberWall
							&& side == 5) {
						return icons[1][0];
					}
					if ((access.getBlock(x, y, z - 1) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x - 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x, y, z - 1) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y, z + 1) != LabStuffMain.blockGasChamberWall
							&& side == 4) {
						return icons[1][0];
					}
					if ((access.getBlock(x, y, z - 1) == LabStuffMain.blockElectronCannon
							|| access.getBlock(x - 1, y, z) == LabStuffMain.blockElectronGrabber || access
							.getBlock(x, y, z - 1) == LabStuffMain.blockGasChamberWall)
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y, z + 1) != LabStuffMain.blockGasChamberWall
							&& side == 5) {
						return icons[1][2];
					}
					if (access.getBlock(x, y, z - 1) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y + 1, z) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y, z + 1) == LabStuffMain.blockGasChamberWall
							&& access.getBlock(x, y - 1, z) == LabStuffMain.blockGasChamberWall) {
						return icons[1][1];
					}
				}
				return this.blank;
			}
			if (side == 2 || side == 3) {
				if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 3) {
					return icons[0][2];
				}
				if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 2) {
					return icons[0][0];
				}
				if (access.getBlock(x, y, z + 1) == LabStuffMain.blockGasChamberPort) {
					return icons[0][1];
				}
				if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 3) {
					return icons[0][0];
				}
				if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 2) {
					return icons[0][2];
				}
				if (access.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberPort) {
					return icons[0][2];
				}
				if (access.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberPort) {
					return icons[0][0];
				}
				if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort) {
					return icons[0][0];
				}
				if (access.getBlock(x, y, z - 1) == LabStuffMain.blockGasChamberPort) {
					return icons[0][1];
				}
				if (access.getBlock(x + 1, y, z - 1) == LabStuffMain.blockGasChamberPort) {
					return icons[0][2];
				}
			}
			if (side == 4 || side == 5) {
				if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 4) {
					return icons[0][2];
				}
				if (access.getBlock(x + 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 5) {
					return icons[0][0];
				}
				if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 4) {
					return icons[0][2];
				}
				if (access.getBlock(x - 1, y, z + 1) == LabStuffMain.blockGasChamberPort && side == 5) {
					return icons[0][0];
				}
				if (access.getBlock(x + 1, y, z) == LabStuffMain.blockGasChamberPort) {
					return icons[0][1];
				}
				if (access.getBlock(x - 1, y, z) == LabStuffMain.blockGasChamberPort) {
					return icons[0][1];
				}
				if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 4) {
					return icons[0][0];
				}
				if (access.getBlock(x - 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 5) {
					return icons[0][2];
				}
				if (access.getBlock(x + 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 4) {
					return icons[0][0];
				}
				if (access.getBlock(x + 1, y, z - 1) == LabStuffMain.blockGasChamberPort && side == 5) {
					return icons[0][2];
				}
			}
			/*
			
			if(multiblocks != null)
			{
				for(int i = 0; i < 3; i++)
				{
					for(int j = 0; j < 3; j++)
					{
						for(int k = 0; k < 3; k++)
						{
							if(multiblocks[i][j][k] == this)
							{
								if(side == 1 || side == 0)
								{
									System.out.println(i + "," + j + "," + k);
									if (i == 0 || i == 2) 
									{
										if (j == 0) 
										{
											return icons[0][k];
										} 
										else if (j == 1) 
										{
											return icons[1][k];
										} 
										else if (j == 2) 
										{
											return icons[2][k];
										}
									}
								}
							}
						}
					}
				}
			}
			else
			{
				System.out.println("Multiblocks array not defined");
			}
			*/
		}
		return this.blank;
    }
	
	
	
	
	
}
