package keegan.labstuff.blocks;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class BlockGasChamberWall extends Block {

	public BlockGasChamberWall(Material p_i45394_1_) {
		super(p_i45394_1_);
		// TODO Auto-generated constructor stub
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
	public void registerBlockIcons(IIconRegister par1IconRegister) {
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
	public IIcon getIcon(IBlockAccess access, int x, int y, int z, int side) {
		if (isMultiblock(access, x, y, z)) {
			if (access.getBlock(x, y - 1, z).equals(Blocks.air)
					&& (access.getBlock(x, y + 1, z).equals(LabStuffMain.blockGasChamberPort)
							|| access.getBlock(x, y + 1, z).equals(LabStuffMain.blockGasChamberWall)
							|| access.getBlock(x, y + 1, z).equals(LabStuffMain.blockElectronGrabber))) {
				// Bottom Layer
				if (side == 0) {
					if (access.getBlock(x - 1, y, z).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][0];
					if (access.getBlock(x + 1, y, z).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][2];
					if (access.getBlock(x, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][1];
					if (access.getBlock(x, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][1];
					if (access.getBlock(x - 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][0];
					if (access.getBlock(x + 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][2];
					if (access.getBlock(x - 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][0];
					if (access.getBlock(x + 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][2];
				}
				if (side == 2 || side == 3) {
					if (access.getBlock(x - 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][0];
					if (access.getBlock(x + 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][2];
					if (access.getBlock(x - 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][2];
					if (access.getBlock(x + 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][0];
					return icons[0][1];
				}
				if (side == 4 || side == 5) {
					if (access.getBlock(x - 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][2];
					if (access.getBlock(x + 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][0];
					if (access.getBlock(x - 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][0];
					if (access.getBlock(x + 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][2];
					return icons[0][1];
				}
			} else if (access.getBlock(x, y - 1, z).equals(LabStuffMain.blockGasChamberWall)
					&& access.getBlock(x, y + 1, z).equals(LabStuffMain.blockGasChamberWall)) {
				// Middle Layer
				if (side == 2 || side == 3) {
					if (access.getBlock(x - 1, y + 1, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][0];
					if (access.getBlock(x + 1, y + 1, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][2];
					if (access.getBlock(x - 1, y + 1, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][2];
					if (access.getBlock(x + 1, y + 1, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][0];
					return icons[1][1];
				}
				if (side == 4 || side == 5) {
					if (access.getBlock(x - 1, y + 1, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][2];
					if (access.getBlock(x + 1, y + 1, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][0];
					if (access.getBlock(x - 1, y + 1, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][0];
					if (access.getBlock(x + 1, y + 1, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][2];
					return icons[1][1];
				}
			} else if (access.getBlock(x, y + 1, z).equals(Blocks.air)
					&& (access.getBlock(x, y - 1, z).equals(LabStuffMain.blockGasChamberPort)
							|| access.getBlock(x, y - 1, z).equals(LabStuffMain.blockGasChamberWall)
							|| access.getBlock(x, y - 1, z).equals(LabStuffMain.blockElectronGrabber))) {
				// Top layer
				if (side == 1) {
					if (access.getBlock(x - 1, y, z).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][0];
					if (access.getBlock(x + 1, y, z).equals(LabStuffMain.blockGasChamberPort))
						return icons[1][2];
					if (access.getBlock(x, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][1];
					if (access.getBlock(x, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][1];
					if (access.getBlock(x - 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][0];
					if (access.getBlock(x + 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[0][2];
					if (access.getBlock(x - 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][0];
					if (access.getBlock(x + 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][2];
				}
				if (side == 2 || side == 3) {
					if (access.getBlock(x - 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][0];
					if (access.getBlock(x + 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][2];
					if (access.getBlock(x - 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][2];
					if (access.getBlock(x + 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][0];
					return icons[2][1];
				}
				if (side == 4 || side == 5) {
					if (access.getBlock(x - 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][2];
					if (access.getBlock(x + 1, y, z - 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][0];
					if (access.getBlock(x - 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][0];
					if (access.getBlock(x + 1, y, z + 1).equals(LabStuffMain.blockGasChamberPort))
						return icons[2][2];
					return icons[2][1];
				}
			}
		}
		return this.blank;
	}

	private boolean isMultiblock(IBlockAccess access, int x, int y, int z) {
		for (int xO = -2; xO < 3; xO++) {
			for (int zO = -2; zO < 3; zO++) {
				if (access.getBlock(x + xO, y, z + zO) != null) {
					if (access.getBlock(x + xO, y, z + zO).equals(LabStuffMain.blockElectronGrabber))
						return ((TileEntityElectronGrabber) access.getTileEntity(x + xO, y, z + zO)).isMultiblock();
					if (access.getBlock(x + xO, y, z + zO).equals(LabStuffMain.blockGasChamberPort)) {
						if (((TileEntityGasChamberPort) access.getTileEntity(x + xO, y, z + zO)).getLaser() != null)
							return ((TileEntityGasChamberPort) access.getTileEntity(x + xO, y, z + zO)).getLaser()
									.isMultiblock();
					}
				}
			}
		}
		return false;
	}

}
