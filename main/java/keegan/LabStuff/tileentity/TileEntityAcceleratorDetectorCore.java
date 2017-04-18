package keegan.labstuff.tileentity;

import java.util.ArrayList;

import keegan.labstuff.LabStuffMain;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileEntityAcceleratorDetectorCore extends TileEntity {

	private boolean isMultiblockComplete = false;
	private boolean isPowered = false;

	private Block getBlock(int xCoord, int yCoord, int zCoord) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(new BlockPos(xCoord, yCoord, zCoord)).getBlock();
	}

	public boolean isMultiblockComplete() {

		Block[][][] multiblocks = new Block[11][11][3];
		int xCoord = pos.getX();
		int yCoord = pos.getY();
		int zCoord = pos.getZ();

		if(getBlock(xCoord+1,yCoord,zCoord).equals(LabStuffMain.blockAcceleratorTrackingDetector))
		{
			multiblocks[0][0][0] = this.getBlock(xCoord - 5, yCoord - 5, zCoord - 1);
			multiblocks[1][0][0] = this.getBlock(xCoord - 4, yCoord - 5, zCoord - 1);
			multiblocks[2][0][0] = this.getBlock(xCoord - 3, yCoord - 5, zCoord - 1);
			multiblocks[3][0][0] = this.getBlock(xCoord - 2, yCoord - 5, zCoord - 1);
			multiblocks[4][0][0] = this.getBlock(xCoord - 1, yCoord - 5, zCoord - 1);
			multiblocks[5][0][0] = this.getBlock(xCoord, yCoord - 5, zCoord - 1);
			multiblocks[6][0][0] = this.getBlock(xCoord + 1, yCoord - 5, zCoord - 1);
			multiblocks[7][0][0] = this.getBlock(xCoord + 2, yCoord - 5, zCoord - 1);
			multiblocks[8][0][0] = this.getBlock(xCoord + 3, yCoord - 5, zCoord - 1);
			multiblocks[9][0][0] = this.getBlock(xCoord + 4, yCoord - 5, zCoord - 1);
			multiblocks[10][0][0] = this.getBlock(xCoord + 5, yCoord - 5, zCoord - 1);
			multiblocks[0][1][0] = this.getBlock(xCoord - 5, yCoord - 4, zCoord - 1);
			multiblocks[1][1][0] = this.getBlock(xCoord - 4, yCoord - 4, zCoord - 1);
			multiblocks[2][1][0] = this.getBlock(xCoord - 3, yCoord - 4, zCoord - 1);
			multiblocks[3][1][0] = this.getBlock(xCoord - 2, yCoord - 4, zCoord - 1);
			multiblocks[4][1][0] = this.getBlock(xCoord - 1, yCoord - 4, zCoord - 1);
			multiblocks[5][1][0] = this.getBlock(xCoord, yCoord - 4, zCoord - 1);
			multiblocks[6][1][0] = this.getBlock(xCoord + 1, yCoord - 4, zCoord - 1);
			multiblocks[7][1][0] = this.getBlock(xCoord + 2, yCoord - 4, zCoord - 1);
			multiblocks[8][1][0] = this.getBlock(xCoord + 3, yCoord - 4, zCoord - 1);
			multiblocks[9][1][0] = this.getBlock(xCoord + 4, yCoord - 4, zCoord - 1);
			multiblocks[10][1][0] = this.getBlock(xCoord + 5, yCoord - 4, zCoord - 1);
			multiblocks[0][2][0] = this.getBlock(xCoord - 5, yCoord - 3, zCoord - 1);
			multiblocks[1][2][0] = this.getBlock(xCoord - 4, yCoord - 3, zCoord - 1);
			multiblocks[2][2][0] = this.getBlock(xCoord - 3, yCoord - 3, zCoord - 1);
			multiblocks[3][2][0] = this.getBlock(xCoord - 2, yCoord - 3, zCoord - 1);
			multiblocks[4][2][0] = this.getBlock(xCoord - 1, yCoord - 3, zCoord - 1);
			multiblocks[5][2][0] = this.getBlock(xCoord, yCoord - 3, zCoord - 1);
			multiblocks[6][2][0] = this.getBlock(xCoord + 1, yCoord - 3, zCoord - 1);
			multiblocks[7][2][0] = this.getBlock(xCoord + 2, yCoord - 3, zCoord - 1);
			multiblocks[8][2][0] = this.getBlock(xCoord + 3, yCoord - 3, zCoord - 1);
			multiblocks[9][2][0] = this.getBlock(xCoord + 4, yCoord - 3, zCoord - 1);
			multiblocks[10][2][0] = this.getBlock(xCoord + 5, yCoord - 3, zCoord - 1);
			multiblocks[0][3][0] = this.getBlock(xCoord - 5, yCoord - 2, zCoord - 1);
			multiblocks[1][3][0] = this.getBlock(xCoord - 4, yCoord - 2, zCoord - 1);
			multiblocks[2][3][0] = this.getBlock(xCoord - 3, yCoord - 2, zCoord - 1);
			multiblocks[3][3][0] = this.getBlock(xCoord - 2, yCoord - 2, zCoord - 1);
			multiblocks[4][3][0] = this.getBlock(xCoord - 1, yCoord - 2, zCoord - 1);
			multiblocks[5][3][0] = this.getBlock(xCoord, yCoord - 2, zCoord - 1);
			multiblocks[6][3][0] = this.getBlock(xCoord + 1, yCoord - 2, zCoord - 1);
			multiblocks[7][3][0] = this.getBlock(xCoord + 2, yCoord - 2, zCoord - 1);
			multiblocks[8][3][0] = this.getBlock(xCoord + 3, yCoord - 2, zCoord - 1);
			multiblocks[9][3][0] = this.getBlock(xCoord + 4, yCoord - 2, zCoord - 1);
			multiblocks[10][3][0] = this.getBlock(xCoord + 5, yCoord - 2, zCoord - 1);
			multiblocks[0][4][0] = this.getBlock(xCoord - 5, yCoord - 1, zCoord - 1);
			multiblocks[1][4][0] = this.getBlock(xCoord - 4, yCoord - 1, zCoord - 1);
			multiblocks[2][4][0] = this.getBlock(xCoord - 3, yCoord - 1, zCoord - 1);
			multiblocks[3][4][0] = this.getBlock(xCoord - 2, yCoord - 1, zCoord - 1);
			multiblocks[4][4][0] = this.getBlock(xCoord - 1, yCoord - 1, zCoord - 1);
			multiblocks[5][4][0] = this.getBlock(xCoord, yCoord - 1, zCoord - 1);
			multiblocks[6][4][0] = this.getBlock(xCoord + 1, yCoord - 1, zCoord - 1);
			multiblocks[7][4][0] = this.getBlock(xCoord + 2, yCoord - 1, zCoord - 1);
			multiblocks[8][4][0] = this.getBlock(xCoord + 3, yCoord - 1, zCoord - 1);
			multiblocks[9][4][0] = this.getBlock(xCoord + 4, yCoord - 1, zCoord - 1);
			multiblocks[10][4][0] = this.getBlock(xCoord + 5, yCoord - 1, zCoord - 1);
			multiblocks[0][5][0] = this.getBlock(xCoord - 5, yCoord, zCoord - 1);
			multiblocks[1][5][0] = this.getBlock(xCoord - 4, yCoord, zCoord - 1);
			multiblocks[2][5][0] = this.getBlock(xCoord - 3, yCoord, zCoord - 1);
			multiblocks[3][5][0] = this.getBlock(xCoord - 2, yCoord, zCoord - 1);
			multiblocks[4][5][0] = this.getBlock(xCoord - 1, yCoord, zCoord - 1);
			multiblocks[5][5][0] = this.getBlock(xCoord, yCoord, zCoord - 1);
			multiblocks[6][5][0] = this.getBlock(xCoord + 1, yCoord, zCoord - 1);
			multiblocks[7][5][0] = this.getBlock(xCoord + 2, yCoord, zCoord - 1);
			multiblocks[8][5][0] = this.getBlock(xCoord + 3, yCoord, zCoord - 1);
			multiblocks[9][5][0] = this.getBlock(xCoord + 4, yCoord, zCoord - 1);
			multiblocks[10][5][0] = this.getBlock(xCoord + 5, yCoord, zCoord - 1);
			multiblocks[0][6][0] = this.getBlock(xCoord - 5, yCoord + 1, zCoord - 1);
			multiblocks[1][6][0] = this.getBlock(xCoord - 4, yCoord + 1, zCoord - 1);
			multiblocks[2][6][0] = this.getBlock(xCoord - 3, yCoord + 1, zCoord - 1);
			multiblocks[3][6][0] = this.getBlock(xCoord - 2, yCoord + 1, zCoord - 1);
			multiblocks[4][6][0] = this.getBlock(xCoord - 1, yCoord + 1, zCoord - 1);
			multiblocks[5][6][0] = this.getBlock(xCoord, yCoord + 1, zCoord - 1);
			multiblocks[6][6][0] = this.getBlock(xCoord + 1, yCoord + 1, zCoord - 1);
			multiblocks[7][6][0] = this.getBlock(xCoord + 2, yCoord + 1, zCoord - 1);
			multiblocks[8][6][0] = this.getBlock(xCoord + 3, yCoord + 1, zCoord - 1);
			multiblocks[9][6][0] = this.getBlock(xCoord + 4, yCoord + 1, zCoord - 1);
			multiblocks[10][6][0] = this.getBlock(xCoord + 5, yCoord + 1, zCoord - 1);
			multiblocks[0][7][0] = this.getBlock(xCoord - 5, yCoord + 2, zCoord - 1);
			multiblocks[1][7][0] = this.getBlock(xCoord - 4, yCoord + 2, zCoord - 1);
			multiblocks[2][7][0] = this.getBlock(xCoord - 3, yCoord + 2, zCoord - 1);
			multiblocks[3][7][0] = this.getBlock(xCoord - 2, yCoord + 2, zCoord - 1);
			multiblocks[4][7][0] = this.getBlock(xCoord - 1, yCoord + 2, zCoord - 1);
			multiblocks[5][7][0] = this.getBlock(xCoord, yCoord + 2, zCoord - 1);
			multiblocks[6][7][0] = this.getBlock(xCoord + 1, yCoord + 2, zCoord - 1);
			multiblocks[7][7][0] = this.getBlock(xCoord + 2, yCoord + 2, zCoord - 1);
			multiblocks[8][7][0] = this.getBlock(xCoord + 3, yCoord + 2, zCoord - 1);
			multiblocks[9][7][0] = this.getBlock(xCoord + 4, yCoord + 2, zCoord - 1);
			multiblocks[10][7][0] = this.getBlock(xCoord + 5, yCoord + 2, zCoord - 1);
			multiblocks[0][8][0] = this.getBlock(xCoord - 5, yCoord + 3, zCoord - 1);
			multiblocks[1][8][0] = this.getBlock(xCoord - 4, yCoord + 3, zCoord - 1);
			multiblocks[2][8][0] = this.getBlock(xCoord - 3, yCoord + 3, zCoord - 1);
			multiblocks[3][8][0] = this.getBlock(xCoord - 2, yCoord + 3, zCoord - 1);
			multiblocks[4][8][0] = this.getBlock(xCoord - 1, yCoord + 3, zCoord - 1);
			multiblocks[5][8][0] = this.getBlock(xCoord, yCoord + 3, zCoord - 1);
			multiblocks[6][8][0] = this.getBlock(xCoord + 1, yCoord + 3, zCoord - 1);
			multiblocks[7][8][0] = this.getBlock(xCoord + 2, yCoord + 3, zCoord - 1);
			multiblocks[8][8][0] = this.getBlock(xCoord + 3, yCoord + 3, zCoord - 1);
			multiblocks[9][8][0] = this.getBlock(xCoord + 4, yCoord + 3, zCoord - 1);
			multiblocks[10][8][0] = this.getBlock(xCoord + 5, yCoord + 3, zCoord - 1);
			multiblocks[0][9][0] = this.getBlock(xCoord - 5, yCoord + 4, zCoord - 1);
			multiblocks[1][9][0] = this.getBlock(xCoord - 4, yCoord + 4, zCoord - 1);
			multiblocks[2][9][0] = this.getBlock(xCoord - 3, yCoord + 4, zCoord - 1);
			multiblocks[3][9][0] = this.getBlock(xCoord - 2, yCoord + 4, zCoord - 1);
			multiblocks[4][9][0] = this.getBlock(xCoord - 1, yCoord + 4, zCoord - 1);
			multiblocks[5][9][0] = this.getBlock(xCoord, yCoord + 4, zCoord - 1);
			multiblocks[6][9][0] = this.getBlock(xCoord + 1, yCoord + 4, zCoord - 1);
			multiblocks[7][9][0] = this.getBlock(xCoord + 2, yCoord + 4, zCoord - 1);
			multiblocks[8][9][0] = this.getBlock(xCoord + 3, yCoord + 4, zCoord - 1);
			multiblocks[9][9][0] = this.getBlock(xCoord + 4, yCoord + 4, zCoord - 1);
			multiblocks[10][9][0] = this.getBlock(xCoord + 5, yCoord + 4, zCoord - 1);
			multiblocks[0][10][0] = this.getBlock(xCoord - 5, yCoord + 5, zCoord - 1);
			multiblocks[1][10][0] = this.getBlock(xCoord - 4, yCoord + 5, zCoord - 1);
			multiblocks[2][10][0] = this.getBlock(xCoord - 3, yCoord + 5, zCoord - 1);
			multiblocks[3][10][0] = this.getBlock(xCoord - 2, yCoord + 5, zCoord - 1);
			multiblocks[4][10][0] = this.getBlock(xCoord - 1, yCoord + 5, zCoord - 1);
			multiblocks[5][10][0] = this.getBlock(xCoord, yCoord + 5, zCoord - 1);
			multiblocks[6][10][0] = this.getBlock(xCoord + 1, yCoord + 5, zCoord - 1);
			multiblocks[7][10][0] = this.getBlock(xCoord + 2, yCoord + 5, zCoord - 1);
			multiblocks[8][10][0] = this.getBlock(xCoord + 3, yCoord + 5, zCoord - 1);
			multiblocks[9][10][0] = this.getBlock(xCoord + 4, yCoord + 5, zCoord - 1);
			multiblocks[10][10][0] = this.getBlock(xCoord + 5, yCoord + 5, zCoord - 1);

			// Middle slice [x][y][1]
			multiblocks[0][0][1] = this.getBlock(xCoord - 5, yCoord - 5, zCoord);
			multiblocks[1][0][1] = this.getBlock(xCoord - 4, yCoord - 5, zCoord);
			multiblocks[2][0][1] = this.getBlock(xCoord - 3, yCoord - 5, zCoord);
			multiblocks[3][0][1] = this.getBlock(xCoord - 2, yCoord - 5, zCoord);
			multiblocks[4][0][1] = this.getBlock(xCoord - 1, yCoord - 5, zCoord);
			multiblocks[5][0][1] = this.getBlock(xCoord, yCoord - 5, zCoord);
			multiblocks[6][0][1] = this.getBlock(xCoord + 1, yCoord - 5, zCoord);
			multiblocks[7][0][1] = this.getBlock(xCoord + 2, yCoord - 5, zCoord);
			multiblocks[8][0][1] = this.getBlock(xCoord + 3, yCoord - 5, zCoord);
			multiblocks[9][0][1] = this.getBlock(xCoord + 4, yCoord - 5, zCoord);
			multiblocks[10][0][1] = this.getBlock(xCoord + 5, yCoord - 5, zCoord);
			multiblocks[0][1][1] = this.getBlock(xCoord - 5, yCoord - 4, zCoord);
			multiblocks[1][1][1] = this.getBlock(xCoord - 4, yCoord - 4, zCoord);
			multiblocks[2][1][1] = this.getBlock(xCoord - 3, yCoord - 4, zCoord);
			multiblocks[3][1][1] = this.getBlock(xCoord - 2, yCoord - 4, zCoord);
			multiblocks[4][1][1] = this.getBlock(xCoord - 1, yCoord - 4, zCoord);
			multiblocks[5][1][1] = this.getBlock(xCoord, yCoord - 4, zCoord);
			multiblocks[6][1][1] = this.getBlock(xCoord + 1, yCoord - 4, zCoord);
			multiblocks[7][1][1] = this.getBlock(xCoord + 2, yCoord - 4, zCoord);
			multiblocks[8][1][1] = this.getBlock(xCoord + 3, yCoord - 4, zCoord);
			multiblocks[9][1][1] = this.getBlock(xCoord + 4, yCoord - 4, zCoord);
			multiblocks[10][1][1] = this.getBlock(xCoord + 5, yCoord - 4, zCoord);
			multiblocks[0][2][1] = this.getBlock(xCoord - 5, yCoord - 3, zCoord);
			multiblocks[1][2][1] = this.getBlock(xCoord - 4, yCoord - 3, zCoord);
			multiblocks[2][2][1] = this.getBlock(xCoord - 3, yCoord - 3, zCoord);
			multiblocks[3][2][1] = this.getBlock(xCoord - 2, yCoord - 3, zCoord);
			multiblocks[4][2][1] = this.getBlock(xCoord - 1, yCoord - 3, zCoord);
			multiblocks[5][2][1] = this.getBlock(xCoord, yCoord - 3, zCoord);
			multiblocks[6][2][1] = this.getBlock(xCoord + 1, yCoord - 3, zCoord);
			multiblocks[7][2][1] = this.getBlock(xCoord + 2, yCoord - 3, zCoord);
			multiblocks[8][2][1] = this.getBlock(xCoord + 3, yCoord - 3, zCoord);
			multiblocks[9][2][1] = this.getBlock(xCoord + 4, yCoord - 3, zCoord);
			multiblocks[10][2][1] = this.getBlock(xCoord + 5, yCoord - 3, zCoord);
			multiblocks[0][3][1] = this.getBlock(xCoord - 5, yCoord - 2, zCoord);
			multiblocks[1][3][1] = this.getBlock(xCoord - 4, yCoord - 2, zCoord);
			multiblocks[2][3][1] = this.getBlock(xCoord - 3, yCoord - 2, zCoord);
			multiblocks[3][3][1] = this.getBlock(xCoord - 2, yCoord - 2, zCoord);
			multiblocks[4][3][1] = this.getBlock(xCoord - 1, yCoord - 2, zCoord);
			multiblocks[5][3][1] = this.getBlock(xCoord, yCoord - 2, zCoord);
			multiblocks[6][3][1] = this.getBlock(xCoord + 1, yCoord - 2, zCoord);
			multiblocks[7][3][1] = this.getBlock(xCoord + 2, yCoord - 2, zCoord);
			multiblocks[8][3][1] = this.getBlock(xCoord + 3, yCoord - 2, zCoord);
			multiblocks[9][3][1] = this.getBlock(xCoord + 4, yCoord - 2, zCoord);
			multiblocks[10][3][1] = this.getBlock(xCoord + 5, yCoord - 2, zCoord);
			multiblocks[0][4][1] = this.getBlock(xCoord - 5, yCoord - 1, zCoord);
			multiblocks[1][4][1] = this.getBlock(xCoord - 4, yCoord - 1, zCoord);
			multiblocks[2][4][1] = this.getBlock(xCoord - 3, yCoord - 1, zCoord);
			multiblocks[3][4][1] = this.getBlock(xCoord - 2, yCoord - 1, zCoord);
			multiblocks[4][4][1] = this.getBlock(xCoord - 1, yCoord - 1, zCoord);
			multiblocks[5][4][1] = this.getBlock(xCoord, yCoord - 1, zCoord);
			multiblocks[6][4][1] = this.getBlock(xCoord + 1, yCoord - 1, zCoord);
			multiblocks[7][4][1] = this.getBlock(xCoord + 2, yCoord - 1, zCoord);
			multiblocks[8][4][1] = this.getBlock(xCoord + 3, yCoord - 1, zCoord);
			multiblocks[9][4][1] = this.getBlock(xCoord + 4, yCoord - 1, zCoord);
			multiblocks[10][4][1] = this.getBlock(xCoord + 5, yCoord - 1, zCoord);
			multiblocks[0][5][1] = this.getBlock(xCoord - 5, yCoord, zCoord);
			multiblocks[1][5][1] = this.getBlock(xCoord - 4, yCoord, zCoord);
			multiblocks[2][5][1] = this.getBlock(xCoord - 3, yCoord, zCoord);
			multiblocks[3][5][1] = this.getBlock(xCoord - 2, yCoord, zCoord);
			multiblocks[4][5][1] = this.getBlock(xCoord - 1, yCoord, zCoord);
			multiblocks[5][5][1] = this.getBlock(xCoord, yCoord, zCoord);
			multiblocks[6][5][1] = this.getBlock(xCoord + 1, yCoord, zCoord);
			multiblocks[7][5][1] = this.getBlock(xCoord + 2, yCoord, zCoord);
			multiblocks[8][5][1] = this.getBlock(xCoord + 3, yCoord, zCoord);
			multiblocks[9][5][1] = this.getBlock(xCoord + 4, yCoord, zCoord);
			multiblocks[10][5][1] = this.getBlock(xCoord + 5, yCoord, zCoord);
			multiblocks[0][6][1] = this.getBlock(xCoord - 5, yCoord + 1, zCoord);
			multiblocks[1][6][1] = this.getBlock(xCoord - 4, yCoord + 1, zCoord);
			multiblocks[2][6][1] = this.getBlock(xCoord - 3, yCoord + 1, zCoord);
			multiblocks[3][6][1] = this.getBlock(xCoord - 2, yCoord + 1, zCoord);
			multiblocks[4][6][1] = this.getBlock(xCoord - 1, yCoord + 1, zCoord);
			multiblocks[5][6][1] = this.getBlock(xCoord, yCoord + 1, zCoord);
			multiblocks[6][6][1] = this.getBlock(xCoord + 1, yCoord + 1, zCoord);
			multiblocks[7][6][1] = this.getBlock(xCoord + 2, yCoord + 1, zCoord);
			multiblocks[8][6][1] = this.getBlock(xCoord + 3, yCoord + 1, zCoord);
			multiblocks[9][6][1] = this.getBlock(xCoord + 4, yCoord + 1, zCoord);
			multiblocks[10][6][1] = this.getBlock(xCoord + 5, yCoord + 1, zCoord);
			multiblocks[0][7][1] = this.getBlock(xCoord - 5, yCoord + 2, zCoord);
			multiblocks[1][7][1] = this.getBlock(xCoord - 4, yCoord + 2, zCoord);
			multiblocks[2][7][1] = this.getBlock(xCoord - 3, yCoord + 2, zCoord);
			multiblocks[3][7][1] = this.getBlock(xCoord - 2, yCoord + 2, zCoord);
			multiblocks[4][7][1] = this.getBlock(xCoord - 1, yCoord + 2, zCoord);
			multiblocks[5][7][1] = this.getBlock(xCoord, yCoord + 2, zCoord);
			multiblocks[6][7][1] = this.getBlock(xCoord + 1, yCoord + 2, zCoord);
			multiblocks[7][7][1] = this.getBlock(xCoord + 2, yCoord + 2, zCoord);
			multiblocks[8][7][1] = this.getBlock(xCoord + 3, yCoord + 2, zCoord);
			multiblocks[9][7][1] = this.getBlock(xCoord + 4, yCoord + 2, zCoord);
			multiblocks[10][7][1] = this.getBlock(xCoord + 5, yCoord + 2, zCoord);
			multiblocks[0][8][1] = this.getBlock(xCoord - 5, yCoord + 3, zCoord);
			multiblocks[1][8][1] = this.getBlock(xCoord - 4, yCoord + 3, zCoord);
			multiblocks[2][8][1] = this.getBlock(xCoord - 3, yCoord + 3, zCoord);
			multiblocks[3][8][1] = this.getBlock(xCoord - 2, yCoord + 3, zCoord);
			multiblocks[4][8][1] = this.getBlock(xCoord - 1, yCoord + 3, zCoord);
			multiblocks[5][8][1] = this.getBlock(xCoord, yCoord + 3, zCoord);
			multiblocks[6][8][1] = this.getBlock(xCoord + 1, yCoord + 3, zCoord);
			multiblocks[7][8][1] = this.getBlock(xCoord + 2, yCoord + 3, zCoord);
			multiblocks[8][8][1] = this.getBlock(xCoord + 3, yCoord + 3, zCoord);
			multiblocks[9][8][1] = this.getBlock(xCoord + 4, yCoord + 3, zCoord);
			multiblocks[10][8][1] = this.getBlock(xCoord + 5, yCoord + 3, zCoord);
			multiblocks[0][9][1] = this.getBlock(xCoord - 5, yCoord + 4, zCoord);
			multiblocks[1][9][1] = this.getBlock(xCoord - 4, yCoord + 4, zCoord);
			multiblocks[2][9][1] = this.getBlock(xCoord - 3, yCoord + 4, zCoord);
			multiblocks[3][9][1] = this.getBlock(xCoord - 2, yCoord + 4, zCoord);
			multiblocks[4][9][1] = this.getBlock(xCoord - 1, yCoord + 4, zCoord);
			multiblocks[5][9][1] = this.getBlock(xCoord, yCoord + 4, zCoord);
			multiblocks[6][9][1] = this.getBlock(xCoord + 1, yCoord + 4, zCoord);
			multiblocks[7][9][1] = this.getBlock(xCoord + 2, yCoord + 4, zCoord);
			multiblocks[8][9][1] = this.getBlock(xCoord + 3, yCoord + 4, zCoord);
			multiblocks[9][9][1] = this.getBlock(xCoord + 4, yCoord + 4, zCoord);
			multiblocks[10][9][1] = this.getBlock(xCoord + 5, yCoord + 4, zCoord);
			multiblocks[0][10][1] = this.getBlock(xCoord - 5, yCoord + 5, zCoord);
			multiblocks[1][10][1] = this.getBlock(xCoord - 4, yCoord + 5, zCoord);
			multiblocks[2][10][1] = this.getBlock(xCoord - 3, yCoord + 5, zCoord);
			multiblocks[3][10][1] = this.getBlock(xCoord - 2, yCoord + 5, zCoord);
			multiblocks[4][10][1] = this.getBlock(xCoord - 1, yCoord + 5, zCoord);
			multiblocks[5][10][1] = this.getBlock(xCoord, yCoord + 5, zCoord);
			multiblocks[6][10][1] = this.getBlock(xCoord + 1, yCoord + 5, zCoord);
			multiblocks[7][10][1] = this.getBlock(xCoord + 2, yCoord + 5, zCoord);
			multiblocks[8][10][1] = this.getBlock(xCoord + 3, yCoord + 5, zCoord);
			multiblocks[9][10][1] = this.getBlock(xCoord + 4, yCoord + 5, zCoord);
			multiblocks[10][10][1] = this.getBlock(xCoord + 5, yCoord + 5, zCoord);

			// Third slice [x][y][2]
			multiblocks[0][0][2] = this.getBlock(xCoord - 5, yCoord - 5, zCoord + 1);
			multiblocks[1][0][2] = this.getBlock(xCoord - 4, yCoord - 5, zCoord + 1);
			multiblocks[2][0][2] = this.getBlock(xCoord - 3, yCoord - 5, zCoord + 1);
			multiblocks[3][0][2] = this.getBlock(xCoord - 2, yCoord - 5, zCoord + 1);
			multiblocks[4][0][2] = this.getBlock(xCoord - 1, yCoord - 5, zCoord + 1);
			multiblocks[5][0][2] = this.getBlock(xCoord, yCoord - 5, zCoord + 1);
			multiblocks[6][0][2] = this.getBlock(xCoord + 1, yCoord - 5, zCoord + 1);
			multiblocks[7][0][2] = this.getBlock(xCoord + 2, yCoord - 5, zCoord + 1);
			multiblocks[8][0][2] = this.getBlock(xCoord + 3, yCoord - 5, zCoord + 1);
			multiblocks[9][0][2] = this.getBlock(xCoord + 4, yCoord - 5, zCoord + 1);
			multiblocks[10][0][2] = this.getBlock(xCoord + 5, yCoord - 5, zCoord + 1);
			multiblocks[0][1][2] = this.getBlock(xCoord - 5, yCoord - 4, zCoord + 1);
			multiblocks[1][1][2] = this.getBlock(xCoord - 4, yCoord - 4, zCoord + 1);
			multiblocks[2][1][2] = this.getBlock(xCoord - 3, yCoord - 4, zCoord + 1);
			multiblocks[3][1][2] = this.getBlock(xCoord - 2, yCoord - 4, zCoord + 1);
			multiblocks[4][1][2] = this.getBlock(xCoord - 1, yCoord - 4, zCoord + 1);
			multiblocks[5][1][2] = this.getBlock(xCoord, yCoord - 4, zCoord + 1);
			multiblocks[6][1][2] = this.getBlock(xCoord + 1, yCoord - 4, zCoord + 1);
			multiblocks[7][1][2] = this.getBlock(xCoord + 2, yCoord - 4, zCoord + 1);
			multiblocks[8][1][2] = this.getBlock(xCoord + 3, yCoord - 4, zCoord + 1);
			multiblocks[9][1][2] = this.getBlock(xCoord + 4, yCoord - 4, zCoord + 1);
			multiblocks[10][1][2] = this.getBlock(xCoord + 5, yCoord - 4, zCoord + 1);
			multiblocks[0][2][2] = this.getBlock(xCoord - 5, yCoord - 3, zCoord + 1);
			multiblocks[1][2][2] = this.getBlock(xCoord - 4, yCoord - 3, zCoord + 1);
			multiblocks[2][2][2] = this.getBlock(xCoord - 3, yCoord - 3, zCoord + 1);
			multiblocks[3][2][2] = this.getBlock(xCoord - 2, yCoord - 3, zCoord + 1);
			multiblocks[4][2][2] = this.getBlock(xCoord - 1, yCoord - 3, zCoord + 1);
			multiblocks[5][2][2] = this.getBlock(xCoord, yCoord - 3, zCoord + 1);
			multiblocks[6][2][2] = this.getBlock(xCoord + 1, yCoord - 3, zCoord + 1);
			multiblocks[7][2][2] = this.getBlock(xCoord + 2, yCoord - 3, zCoord + 1);
			multiblocks[8][2][2] = this.getBlock(xCoord + 3, yCoord - 3, zCoord + 1);
			multiblocks[9][2][2] = this.getBlock(xCoord + 4, yCoord - 3, zCoord + 1);
			multiblocks[10][2][2] = this.getBlock(xCoord + 5, yCoord - 3, zCoord + 1);
			multiblocks[0][3][2] = this.getBlock(xCoord - 5, yCoord - 2, zCoord + 1);
			multiblocks[1][3][2] = this.getBlock(xCoord - 4, yCoord - 2, zCoord + 1);
			multiblocks[2][3][2] = this.getBlock(xCoord - 3, yCoord - 2, zCoord + 1);
			multiblocks[3][3][2] = this.getBlock(xCoord - 2, yCoord - 2, zCoord + 1);
			multiblocks[4][3][2] = this.getBlock(xCoord - 1, yCoord - 2, zCoord + 1);
			multiblocks[5][3][2] = this.getBlock(xCoord, yCoord - 2, zCoord + 1);
			multiblocks[6][3][2] = this.getBlock(xCoord + 1, yCoord - 2, zCoord + 1);
			multiblocks[7][3][2] = this.getBlock(xCoord + 2, yCoord - 2, zCoord + 1);
			multiblocks[8][3][2] = this.getBlock(xCoord + 3, yCoord - 2, zCoord + 1);
			multiblocks[9][3][2] = this.getBlock(xCoord + 4, yCoord - 2, zCoord + 1);
			multiblocks[10][3][2] = this.getBlock(xCoord + 5, yCoord - 2, zCoord + 1);
			multiblocks[0][4][2] = this.getBlock(xCoord - 5, yCoord - 1, zCoord + 1);
			multiblocks[1][4][2] = this.getBlock(xCoord - 4, yCoord - 1, zCoord + 1);
			multiblocks[2][4][2] = this.getBlock(xCoord - 3, yCoord - 1, zCoord + 1);
			multiblocks[3][4][2] = this.getBlock(xCoord - 2, yCoord - 1, zCoord + 1);
			multiblocks[4][4][2] = this.getBlock(xCoord - 1, yCoord - 1, zCoord + 1);
			multiblocks[5][4][2] = this.getBlock(xCoord, yCoord - 1, zCoord + 1);
			multiblocks[6][4][2] = this.getBlock(xCoord + 1, yCoord - 1, zCoord + 1);
			multiblocks[7][4][2] = this.getBlock(xCoord + 2, yCoord - 1, zCoord + 1);
			multiblocks[8][4][2] = this.getBlock(xCoord + 3, yCoord - 1, zCoord + 1);
			multiblocks[9][4][2] = this.getBlock(xCoord + 4, yCoord - 1, zCoord + 1);
			multiblocks[10][4][2] = this.getBlock(xCoord + 5, yCoord - 1, zCoord + 1);
			multiblocks[0][5][2] = this.getBlock(xCoord - 5, yCoord, zCoord + 1);
			multiblocks[1][5][2] = this.getBlock(xCoord - 4, yCoord, zCoord + 1);
			multiblocks[2][5][2] = this.getBlock(xCoord - 3, yCoord, zCoord + 1);
			multiblocks[3][5][2] = this.getBlock(xCoord - 2, yCoord, zCoord + 1);
			multiblocks[4][5][2] = this.getBlock(xCoord - 1, yCoord, zCoord + 1);
			multiblocks[5][5][2] = this.getBlock(xCoord, yCoord, zCoord + 1);
			multiblocks[6][5][2] = this.getBlock(xCoord + 1, yCoord, zCoord + 1);
			multiblocks[7][5][2] = this.getBlock(xCoord + 2, yCoord, zCoord + 1);
			multiblocks[8][5][2] = this.getBlock(xCoord + 3, yCoord, zCoord + 1);
			multiblocks[9][5][2] = this.getBlock(xCoord + 4, yCoord, zCoord + 1);
			multiblocks[10][5][2] = this.getBlock(xCoord + 5, yCoord, zCoord + 1);
			multiblocks[0][6][2] = this.getBlock(xCoord - 5, yCoord + 1, zCoord + 1);
			multiblocks[1][6][2] = this.getBlock(xCoord - 4, yCoord + 1, zCoord + 1);
			multiblocks[2][6][2] = this.getBlock(xCoord - 3, yCoord + 1, zCoord + 1);
			multiblocks[3][6][2] = this.getBlock(xCoord - 2, yCoord + 1, zCoord + 1);
			multiblocks[4][6][2] = this.getBlock(xCoord - 1, yCoord + 1, zCoord + 1);
			multiblocks[5][6][2] = this.getBlock(xCoord, yCoord + 1, zCoord + 1);
			multiblocks[6][6][2] = this.getBlock(xCoord + 1, yCoord + 1, zCoord + 1);
			multiblocks[7][6][2] = this.getBlock(xCoord + 2, yCoord + 1, zCoord + 1);
			multiblocks[8][6][2] = this.getBlock(xCoord + 3, yCoord + 1, zCoord + 1);
			multiblocks[9][6][2] = this.getBlock(xCoord + 4, yCoord + 1, zCoord + 1);
			multiblocks[10][6][2] = this.getBlock(xCoord + 5, yCoord + 1, zCoord + 1);
			multiblocks[0][7][2] = this.getBlock(xCoord - 5, yCoord + 2, zCoord + 1);
			multiblocks[1][7][2] = this.getBlock(xCoord - 4, yCoord + 2, zCoord + 1);
			multiblocks[2][7][2] = this.getBlock(xCoord - 3, yCoord + 2, zCoord + 1);
			multiblocks[3][7][2] = this.getBlock(xCoord - 2, yCoord + 2, zCoord + 1);
			multiblocks[4][7][2] = this.getBlock(xCoord - 1, yCoord + 2, zCoord + 1);
			multiblocks[5][7][2] = this.getBlock(xCoord, yCoord + 2, zCoord + 1);
			multiblocks[6][7][2] = this.getBlock(xCoord + 1, yCoord + 2, zCoord + 1);
			multiblocks[7][7][2] = this.getBlock(xCoord + 2, yCoord + 2, zCoord + 1);
			multiblocks[8][7][2] = this.getBlock(xCoord + 3, yCoord + 2, zCoord + 1);
			multiblocks[9][7][2] = this.getBlock(xCoord + 4, yCoord + 2, zCoord + 1);
			multiblocks[10][7][2] = this.getBlock(xCoord + 5, yCoord + 2, zCoord + 1);
			multiblocks[0][8][2] = this.getBlock(xCoord - 5, yCoord + 3, zCoord + 1);
			multiblocks[1][8][2] = this.getBlock(xCoord - 4, yCoord + 3, zCoord + 1);
			multiblocks[2][8][2] = this.getBlock(xCoord - 3, yCoord + 3, zCoord + 1);
			multiblocks[3][8][2] = this.getBlock(xCoord - 2, yCoord + 3, zCoord + 1);
			multiblocks[4][8][2] = this.getBlock(xCoord - 1, yCoord + 3, zCoord + 1);
			multiblocks[5][8][2] = this.getBlock(xCoord, yCoord + 3, zCoord + 1);
			multiblocks[6][8][2] = this.getBlock(xCoord + 1, yCoord + 3, zCoord + 1);
			multiblocks[7][8][2] = this.getBlock(xCoord + 2, yCoord + 3, zCoord + 1);
			multiblocks[8][8][2] = this.getBlock(xCoord + 3, yCoord + 3, zCoord + 1);
			multiblocks[9][8][2] = this.getBlock(xCoord + 4, yCoord + 3, zCoord + 1);
			multiblocks[10][8][2] = this.getBlock(xCoord + 5, yCoord + 3, zCoord + 1);
			multiblocks[0][9][2] = this.getBlock(xCoord - 5, yCoord + 4, zCoord + 1);
			multiblocks[1][9][2] = this.getBlock(xCoord - 4, yCoord + 4, zCoord + 1);
			multiblocks[2][9][2] = this.getBlock(xCoord - 3, yCoord + 4, zCoord + 1);
			multiblocks[3][9][2] = this.getBlock(xCoord - 2, yCoord + 4, zCoord + 1);
			multiblocks[4][9][2] = this.getBlock(xCoord - 1, yCoord + 4, zCoord + 1);
			multiblocks[5][9][2] = this.getBlock(xCoord, yCoord + 4, zCoord + 1);
			multiblocks[6][9][2] = this.getBlock(xCoord + 1, yCoord + 4, zCoord + 1);
			multiblocks[7][9][2] = this.getBlock(xCoord + 2, yCoord + 4, zCoord + 1);
			multiblocks[8][9][2] = this.getBlock(xCoord + 3, yCoord + 4, zCoord + 1);
			multiblocks[9][9][2] = this.getBlock(xCoord + 4, yCoord + 4, zCoord + 1);
			multiblocks[10][9][2] = this.getBlock(xCoord + 5, yCoord + 4, zCoord + 1);
			multiblocks[0][10][2] = this.getBlock(xCoord - 5, yCoord + 5, zCoord + 1);
			multiblocks[1][10][2] = this.getBlock(xCoord - 4, yCoord + 5, zCoord + 1);
			multiblocks[2][10][2] = this.getBlock(xCoord - 3, yCoord + 5, zCoord + 1);
			multiblocks[3][10][2] = this.getBlock(xCoord - 2, yCoord + 5, zCoord + 1);
			multiblocks[4][10][2] = this.getBlock(xCoord - 1, yCoord + 5, zCoord + 1);
			multiblocks[5][10][2] = this.getBlock(xCoord, yCoord + 5, zCoord + 1);
			multiblocks[6][10][2] = this.getBlock(xCoord + 1, yCoord + 5, zCoord + 1);
			multiblocks[7][10][2] = this.getBlock(xCoord + 2, yCoord + 5, zCoord + 1);
			multiblocks[8][10][2] = this.getBlock(xCoord + 3, yCoord + 5, zCoord + 1);
			multiblocks[9][10][2] = this.getBlock(xCoord + 4, yCoord + 5, zCoord + 1);
			multiblocks[10][10][2] = this.getBlock(xCoord + 5, yCoord + 5, zCoord + 1);
		}
		else
		{
			multiblocks[0][0][0] = this.getBlockZYX(zCoord - 5, yCoord - 5, xCoord - 1);
			multiblocks[1][0][0] = this.getBlockZYX(zCoord - 4, yCoord - 5, xCoord - 1);
			multiblocks[2][0][0] = this.getBlockZYX(zCoord - 3, yCoord - 5, xCoord - 1);
			multiblocks[3][0][0] = this.getBlockZYX(zCoord - 2, yCoord - 5, xCoord - 1);
			multiblocks[4][0][0] = this.getBlockZYX(zCoord - 1, yCoord - 5, xCoord - 1);
			multiblocks[5][0][0] = this.getBlockZYX(zCoord, yCoord - 5, xCoord - 1);
			multiblocks[6][0][0] = this.getBlockZYX(zCoord + 1, yCoord - 5, xCoord - 1);
			multiblocks[7][0][0] = this.getBlockZYX(zCoord + 2, yCoord - 5, xCoord - 1);
			multiblocks[8][0][0] = this.getBlockZYX(zCoord + 3, yCoord - 5, xCoord - 1);
			multiblocks[9][0][0] = this.getBlockZYX(zCoord + 4, yCoord - 5, xCoord - 1);
			multiblocks[10][0][0] = this.getBlockZYX(zCoord + 5, yCoord - 5, xCoord - 1);
			multiblocks[0][1][0] = this.getBlockZYX(zCoord - 5, yCoord - 4, xCoord - 1);
			multiblocks[1][1][0] = this.getBlockZYX(zCoord - 4, yCoord - 4, xCoord - 1);
			multiblocks[2][1][0] = this.getBlockZYX(zCoord - 3, yCoord - 4, xCoord - 1);
			multiblocks[3][1][0] = this.getBlockZYX(zCoord - 2, yCoord - 4, xCoord - 1);
			multiblocks[4][1][0] = this.getBlockZYX(zCoord - 1, yCoord - 4, xCoord - 1);
			multiblocks[5][1][0] = this.getBlockZYX(zCoord, yCoord - 4, xCoord - 1);
			multiblocks[6][1][0] = this.getBlockZYX(zCoord + 1, yCoord - 4, xCoord - 1);
			multiblocks[7][1][0] = this.getBlockZYX(zCoord + 2, yCoord - 4, xCoord - 1);
			multiblocks[8][1][0] = this.getBlockZYX(zCoord + 3, yCoord - 4, xCoord - 1);
			multiblocks[9][1][0] = this.getBlockZYX(zCoord + 4, yCoord - 4, xCoord - 1);
			multiblocks[10][1][0] = this.getBlockZYX(zCoord + 5, yCoord - 4, xCoord - 1);
			multiblocks[0][2][0] = this.getBlockZYX(zCoord - 5, yCoord - 3, xCoord - 1);
			multiblocks[1][2][0] = this.getBlockZYX(zCoord - 4, yCoord - 3, xCoord - 1);
			multiblocks[2][2][0] = this.getBlockZYX(zCoord - 3, yCoord - 3, xCoord - 1);
			multiblocks[3][2][0] = this.getBlockZYX(zCoord - 2, yCoord - 3, xCoord - 1);
			multiblocks[4][2][0] = this.getBlockZYX(zCoord - 1, yCoord - 3, xCoord - 1);
			multiblocks[5][2][0] = this.getBlockZYX(zCoord, yCoord - 3, xCoord - 1);
			multiblocks[6][2][0] = this.getBlockZYX(zCoord + 1, yCoord - 3, xCoord - 1);
			multiblocks[7][2][0] = this.getBlockZYX(zCoord + 2, yCoord - 3, xCoord - 1);
			multiblocks[8][2][0] = this.getBlockZYX(zCoord + 3, yCoord - 3, xCoord - 1);
			multiblocks[9][2][0] = this.getBlockZYX(zCoord + 4, yCoord - 3, xCoord - 1);
			multiblocks[10][2][0] = this.getBlockZYX(zCoord + 5, yCoord - 3, xCoord - 1);
			multiblocks[0][3][0] = this.getBlockZYX(zCoord - 5, yCoord - 2, xCoord - 1);
			multiblocks[1][3][0] = this.getBlockZYX(zCoord - 4, yCoord - 2, xCoord - 1);
			multiblocks[2][3][0] = this.getBlockZYX(zCoord - 3, yCoord - 2, xCoord - 1);
			multiblocks[3][3][0] = this.getBlockZYX(zCoord - 2, yCoord - 2, xCoord - 1);
			multiblocks[4][3][0] = this.getBlockZYX(zCoord - 1, yCoord - 2, xCoord - 1);
			multiblocks[5][3][0] = this.getBlockZYX(zCoord, yCoord - 2, xCoord - 1);
			multiblocks[6][3][0] = this.getBlockZYX(zCoord + 1, yCoord - 2, xCoord - 1);
			multiblocks[7][3][0] = this.getBlockZYX(zCoord + 2, yCoord - 2, xCoord - 1);
			multiblocks[8][3][0] = this.getBlockZYX(zCoord + 3, yCoord - 2, xCoord - 1);
			multiblocks[9][3][0] = this.getBlockZYX(zCoord + 4, yCoord - 2, xCoord - 1);
			multiblocks[10][3][0] = this.getBlockZYX(zCoord + 5, yCoord - 2, xCoord - 1);
			multiblocks[0][4][0] = this.getBlockZYX(zCoord - 5, yCoord - 1, xCoord - 1);
			multiblocks[1][4][0] = this.getBlockZYX(zCoord - 4, yCoord - 1, xCoord - 1);
			multiblocks[2][4][0] = this.getBlockZYX(zCoord - 3, yCoord - 1, xCoord - 1);
			multiblocks[3][4][0] = this.getBlockZYX(zCoord - 2, yCoord - 1, xCoord - 1);
			multiblocks[4][4][0] = this.getBlockZYX(zCoord - 1, yCoord - 1, xCoord - 1);
			multiblocks[5][4][0] = this.getBlockZYX(zCoord, yCoord - 1, xCoord - 1);
			multiblocks[6][4][0] = this.getBlockZYX(zCoord + 1, yCoord - 1, xCoord - 1);
			multiblocks[7][4][0] = this.getBlockZYX(zCoord + 2, yCoord - 1, xCoord - 1);
			multiblocks[8][4][0] = this.getBlockZYX(zCoord + 3, yCoord - 1, xCoord - 1);
			multiblocks[9][4][0] = this.getBlockZYX(zCoord + 4, yCoord - 1, xCoord - 1);
			multiblocks[10][4][0] = this.getBlockZYX(zCoord + 5, yCoord - 1, xCoord - 1);
			multiblocks[0][5][0] = this.getBlockZYX(zCoord - 5, yCoord, xCoord - 1);
			multiblocks[1][5][0] = this.getBlockZYX(zCoord - 4, yCoord, xCoord - 1);
			multiblocks[2][5][0] = this.getBlockZYX(zCoord - 3, yCoord, xCoord - 1);
			multiblocks[3][5][0] = this.getBlockZYX(zCoord - 2, yCoord, xCoord - 1);
			multiblocks[4][5][0] = this.getBlockZYX(zCoord - 1, yCoord, xCoord - 1);
			multiblocks[5][5][0] = this.getBlockZYX(zCoord, yCoord, xCoord - 1);
			multiblocks[6][5][0] = this.getBlockZYX(zCoord + 1, yCoord, xCoord - 1);
			multiblocks[7][5][0] = this.getBlockZYX(zCoord + 2, yCoord, xCoord - 1);
			multiblocks[8][5][0] = this.getBlockZYX(zCoord + 3, yCoord, xCoord - 1);
			multiblocks[9][5][0] = this.getBlockZYX(zCoord + 4, yCoord, xCoord - 1);
			multiblocks[10][5][0] = this.getBlockZYX(zCoord + 5, yCoord, xCoord - 1);
			multiblocks[0][6][0] = this.getBlockZYX(zCoord - 5, yCoord + 1, xCoord - 1);
			multiblocks[1][6][0] = this.getBlockZYX(zCoord - 4, yCoord + 1, xCoord - 1);
			multiblocks[2][6][0] = this.getBlockZYX(zCoord - 3, yCoord + 1, xCoord - 1);
			multiblocks[3][6][0] = this.getBlockZYX(zCoord - 2, yCoord + 1, xCoord - 1);
			multiblocks[4][6][0] = this.getBlockZYX(zCoord - 1, yCoord + 1, xCoord - 1);
			multiblocks[5][6][0] = this.getBlockZYX(zCoord, yCoord + 1, xCoord - 1);
			multiblocks[6][6][0] = this.getBlockZYX(zCoord + 1, yCoord + 1, xCoord - 1);
			multiblocks[7][6][0] = this.getBlockZYX(zCoord + 2, yCoord + 1, xCoord - 1);
			multiblocks[8][6][0] = this.getBlockZYX(zCoord + 3, yCoord + 1, xCoord - 1);
			multiblocks[9][6][0] = this.getBlockZYX(zCoord + 4, yCoord + 1, xCoord - 1);
			multiblocks[10][6][0] = this.getBlockZYX(zCoord + 5, yCoord + 1, xCoord - 1);
			multiblocks[0][7][0] = this.getBlockZYX(zCoord - 5, yCoord + 2, xCoord - 1);
			multiblocks[1][7][0] = this.getBlockZYX(zCoord - 4, yCoord + 2, xCoord - 1);
			multiblocks[2][7][0] = this.getBlockZYX(zCoord - 3, yCoord + 2, xCoord - 1);
			multiblocks[3][7][0] = this.getBlockZYX(zCoord - 2, yCoord + 2, xCoord - 1);
			multiblocks[4][7][0] = this.getBlockZYX(zCoord - 1, yCoord + 2, xCoord - 1);
			multiblocks[5][7][0] = this.getBlockZYX(zCoord, yCoord + 2, xCoord - 1);
			multiblocks[6][7][0] = this.getBlockZYX(zCoord + 1, yCoord + 2, xCoord - 1);
			multiblocks[7][7][0] = this.getBlockZYX(zCoord + 2, yCoord + 2, xCoord - 1);
			multiblocks[8][7][0] = this.getBlockZYX(zCoord + 3, yCoord + 2, xCoord - 1);
			multiblocks[9][7][0] = this.getBlockZYX(zCoord + 4, yCoord + 2, xCoord - 1);
			multiblocks[10][7][0] = this.getBlockZYX(zCoord + 5, yCoord + 2, xCoord - 1);
			multiblocks[0][8][0] = this.getBlockZYX(zCoord - 5, yCoord + 3, xCoord - 1);
			multiblocks[1][8][0] = this.getBlockZYX(zCoord - 4, yCoord + 3, xCoord - 1);
			multiblocks[2][8][0] = this.getBlockZYX(zCoord - 3, yCoord + 3, xCoord - 1);
			multiblocks[3][8][0] = this.getBlockZYX(zCoord - 2, yCoord + 3, xCoord - 1);
			multiblocks[4][8][0] = this.getBlockZYX(zCoord - 1, yCoord + 3, xCoord - 1);
			multiblocks[5][8][0] = this.getBlockZYX(zCoord, yCoord + 3, xCoord - 1);
			multiblocks[6][8][0] = this.getBlockZYX(zCoord + 1, yCoord + 3, xCoord - 1);
			multiblocks[7][8][0] = this.getBlockZYX(zCoord + 2, yCoord + 3, xCoord - 1);
			multiblocks[8][8][0] = this.getBlockZYX(zCoord + 3, yCoord + 3, xCoord - 1);
			multiblocks[9][8][0] = this.getBlockZYX(zCoord + 4, yCoord + 3, xCoord - 1);
			multiblocks[10][8][0] = this.getBlockZYX(zCoord + 5, yCoord + 3, xCoord - 1);
			multiblocks[0][9][0] = this.getBlockZYX(zCoord - 5, yCoord + 4, xCoord - 1);
			multiblocks[1][9][0] = this.getBlockZYX(zCoord - 4, yCoord + 4, xCoord - 1);
			multiblocks[2][9][0] = this.getBlockZYX(zCoord - 3, yCoord + 4, xCoord - 1);
			multiblocks[3][9][0] = this.getBlockZYX(zCoord - 2, yCoord + 4, xCoord - 1);
			multiblocks[4][9][0] = this.getBlockZYX(zCoord - 1, yCoord + 4, xCoord - 1);
			multiblocks[5][9][0] = this.getBlockZYX(zCoord, yCoord + 4, xCoord - 1);
			multiblocks[6][9][0] = this.getBlockZYX(zCoord + 1, yCoord + 4, xCoord - 1);
			multiblocks[7][9][0] = this.getBlockZYX(zCoord + 2, yCoord + 4, xCoord - 1);
			multiblocks[8][9][0] = this.getBlockZYX(zCoord + 3, yCoord + 4, xCoord - 1);
			multiblocks[9][9][0] = this.getBlockZYX(zCoord + 4, yCoord + 4, xCoord - 1);
			multiblocks[10][9][0] = this.getBlockZYX(zCoord + 5, yCoord + 4, xCoord - 1);
			multiblocks[0][10][0] = this.getBlockZYX(zCoord - 5, yCoord + 5, xCoord - 1);
			multiblocks[1][10][0] = this.getBlockZYX(zCoord - 4, yCoord + 5, xCoord - 1);
			multiblocks[2][10][0] = this.getBlockZYX(zCoord - 3, yCoord + 5, xCoord - 1);
			multiblocks[3][10][0] = this.getBlockZYX(zCoord - 2, yCoord + 5, xCoord - 1);
			multiblocks[4][10][0] = this.getBlockZYX(zCoord - 1, yCoord + 5, xCoord - 1);
			multiblocks[5][10][0] = this.getBlockZYX(zCoord, yCoord + 5, xCoord - 1);
			multiblocks[6][10][0] = this.getBlockZYX(zCoord + 1, yCoord + 5, xCoord - 1);
			multiblocks[7][10][0] = this.getBlockZYX(zCoord + 2, yCoord + 5, xCoord - 1);
			multiblocks[8][10][0] = this.getBlockZYX(zCoord + 3, yCoord + 5, xCoord - 1);
			multiblocks[9][10][0] = this.getBlockZYX(zCoord + 4, yCoord + 5, xCoord - 1);
			multiblocks[10][10][0] = this.getBlockZYX(zCoord + 5, yCoord + 5, xCoord - 1);

			// Middle slice [x][y][1]
			multiblocks[0][0][1] = this.getBlockZYX(zCoord - 5, yCoord - 5, xCoord);
			multiblocks[1][0][1] = this.getBlockZYX(zCoord - 4, yCoord - 5, xCoord);
			multiblocks[2][0][1] = this.getBlockZYX(zCoord - 3, yCoord - 5, xCoord);
			multiblocks[3][0][1] = this.getBlockZYX(zCoord - 2, yCoord - 5, xCoord);
			multiblocks[4][0][1] = this.getBlockZYX(zCoord - 1, yCoord - 5, xCoord);
			multiblocks[5][0][1] = this.getBlockZYX(zCoord, yCoord - 5, xCoord);
			multiblocks[6][0][1] = this.getBlockZYX(zCoord + 1, yCoord - 5, xCoord);
			multiblocks[7][0][1] = this.getBlockZYX(zCoord + 2, yCoord - 5, xCoord);
			multiblocks[8][0][1] = this.getBlockZYX(zCoord + 3, yCoord - 5, xCoord);
			multiblocks[9][0][1] = this.getBlockZYX(zCoord + 4, yCoord - 5, xCoord);
			multiblocks[10][0][1] = this.getBlockZYX(zCoord + 5, yCoord - 5, xCoord);
			multiblocks[0][1][1] = this.getBlockZYX(zCoord - 5, yCoord - 4, xCoord);
			multiblocks[1][1][1] = this.getBlockZYX(zCoord - 4, yCoord - 4, xCoord);
			multiblocks[2][1][1] = this.getBlockZYX(zCoord - 3, yCoord - 4, xCoord);
			multiblocks[3][1][1] = this.getBlockZYX(zCoord - 2, yCoord - 4, xCoord);
			multiblocks[4][1][1] = this.getBlockZYX(zCoord - 1, yCoord - 4, xCoord);
			multiblocks[5][1][1] = this.getBlockZYX(zCoord, yCoord - 4, xCoord);
			multiblocks[6][1][1] = this.getBlockZYX(zCoord + 1, yCoord - 4, xCoord);
			multiblocks[7][1][1] = this.getBlockZYX(zCoord + 2, yCoord - 4, xCoord);
			multiblocks[8][1][1] = this.getBlockZYX(zCoord + 3, yCoord - 4, xCoord);
			multiblocks[9][1][1] = this.getBlockZYX(zCoord + 4, yCoord - 4, xCoord);
			multiblocks[10][1][1] = this.getBlockZYX(zCoord + 5, yCoord - 4, xCoord);
			multiblocks[0][2][1] = this.getBlockZYX(zCoord - 5, yCoord - 3, xCoord);
			multiblocks[1][2][1] = this.getBlockZYX(zCoord - 4, yCoord - 3, xCoord);
			multiblocks[2][2][1] = this.getBlockZYX(zCoord - 3, yCoord - 3, xCoord);
			multiblocks[3][2][1] = this.getBlockZYX(zCoord - 2, yCoord - 3, xCoord);
			multiblocks[4][2][1] = this.getBlockZYX(zCoord - 1, yCoord - 3, xCoord);
			multiblocks[5][2][1] = this.getBlockZYX(zCoord, yCoord - 3, xCoord);
			multiblocks[6][2][1] = this.getBlockZYX(zCoord + 1, yCoord - 3, xCoord);
			multiblocks[7][2][1] = this.getBlockZYX(zCoord + 2, yCoord - 3, xCoord);
			multiblocks[8][2][1] = this.getBlockZYX(zCoord + 3, yCoord - 3, xCoord);
			multiblocks[9][2][1] = this.getBlockZYX(zCoord + 4, yCoord - 3, xCoord);
			multiblocks[10][2][1] = this.getBlockZYX(zCoord + 5, yCoord - 3, xCoord);
			multiblocks[0][3][1] = this.getBlockZYX(zCoord - 5, yCoord - 2, xCoord);
			multiblocks[1][3][1] = this.getBlockZYX(zCoord - 4, yCoord - 2, xCoord);
			multiblocks[2][3][1] = this.getBlockZYX(zCoord - 3, yCoord - 2, xCoord);
			multiblocks[3][3][1] = this.getBlockZYX(zCoord - 2, yCoord - 2, xCoord);
			multiblocks[4][3][1] = this.getBlockZYX(zCoord - 1, yCoord - 2, xCoord);
			multiblocks[5][3][1] = this.getBlockZYX(zCoord, yCoord - 2, xCoord);
			multiblocks[6][3][1] = this.getBlockZYX(zCoord + 1, yCoord - 2, xCoord);
			multiblocks[7][3][1] = this.getBlockZYX(zCoord + 2, yCoord - 2, xCoord);
			multiblocks[8][3][1] = this.getBlockZYX(zCoord + 3, yCoord - 2, xCoord);
			multiblocks[9][3][1] = this.getBlockZYX(zCoord + 4, yCoord - 2, xCoord);
			multiblocks[10][3][1] = this.getBlockZYX(zCoord + 5, yCoord - 2, xCoord);
			multiblocks[0][4][1] = this.getBlockZYX(zCoord - 5, yCoord - 1, xCoord);
			multiblocks[1][4][1] = this.getBlockZYX(zCoord - 4, yCoord - 1, xCoord);
			multiblocks[2][4][1] = this.getBlockZYX(zCoord - 3, yCoord - 1, xCoord);
			multiblocks[3][4][1] = this.getBlockZYX(zCoord - 2, yCoord - 1, xCoord);
			multiblocks[4][4][1] = this.getBlockZYX(zCoord - 1, yCoord - 1, xCoord);
			multiblocks[5][4][1] = this.getBlockZYX(zCoord, yCoord - 1, xCoord);
			multiblocks[6][4][1] = this.getBlockZYX(zCoord + 1, yCoord - 1, xCoord);
			multiblocks[7][4][1] = this.getBlockZYX(zCoord + 2, yCoord - 1, xCoord);
			multiblocks[8][4][1] = this.getBlockZYX(zCoord + 3, yCoord - 1, xCoord);
			multiblocks[9][4][1] = this.getBlockZYX(zCoord + 4, yCoord - 1, xCoord);
			multiblocks[10][4][1] = this.getBlockZYX(zCoord + 5, yCoord - 1, xCoord);
			multiblocks[0][5][1] = this.getBlockZYX(zCoord - 5, yCoord, xCoord);
			multiblocks[1][5][1] = this.getBlockZYX(zCoord - 4, yCoord, xCoord);
			multiblocks[2][5][1] = this.getBlockZYX(zCoord - 3, yCoord, xCoord);
			multiblocks[3][5][1] = this.getBlockZYX(zCoord - 2, yCoord, xCoord);
			multiblocks[4][5][1] = this.getBlockZYX(zCoord - 1, yCoord, xCoord);
			multiblocks[5][5][1] = this.getBlockZYX(zCoord, yCoord, xCoord);
			multiblocks[6][5][1] = this.getBlockZYX(zCoord + 1, yCoord, xCoord);
			multiblocks[7][5][1] = this.getBlockZYX(zCoord + 2, yCoord, xCoord);
			multiblocks[8][5][1] = this.getBlockZYX(zCoord + 3, yCoord, xCoord);
			multiblocks[9][5][1] = this.getBlockZYX(zCoord + 4, yCoord, xCoord);
			multiblocks[10][5][1] = this.getBlockZYX(zCoord + 5, yCoord, xCoord);
			multiblocks[0][6][1] = this.getBlockZYX(zCoord - 5, yCoord + 1, xCoord);
			multiblocks[1][6][1] = this.getBlockZYX(zCoord - 4, yCoord + 1, xCoord);
			multiblocks[2][6][1] = this.getBlockZYX(zCoord - 3, yCoord + 1, xCoord);
			multiblocks[3][6][1] = this.getBlockZYX(zCoord - 2, yCoord + 1, xCoord);
			multiblocks[4][6][1] = this.getBlockZYX(zCoord - 1, yCoord + 1, xCoord);
			multiblocks[5][6][1] = this.getBlockZYX(zCoord, yCoord + 1, xCoord);
			multiblocks[6][6][1] = this.getBlockZYX(zCoord + 1, yCoord + 1, xCoord);
			multiblocks[7][6][1] = this.getBlockZYX(zCoord + 2, yCoord + 1, xCoord);
			multiblocks[8][6][1] = this.getBlockZYX(zCoord + 3, yCoord + 1, xCoord);
			multiblocks[9][6][1] = this.getBlockZYX(zCoord + 4, yCoord + 1, xCoord);
			multiblocks[10][6][1] = this.getBlockZYX(zCoord + 5, yCoord + 1, xCoord);
			multiblocks[0][7][1] = this.getBlockZYX(zCoord - 5, yCoord + 2, xCoord);
			multiblocks[1][7][1] = this.getBlockZYX(zCoord - 4, yCoord + 2, xCoord);
			multiblocks[2][7][1] = this.getBlockZYX(zCoord - 3, yCoord + 2, xCoord);
			multiblocks[3][7][1] = this.getBlockZYX(zCoord - 2, yCoord + 2, xCoord);
			multiblocks[4][7][1] = this.getBlockZYX(zCoord - 1, yCoord + 2, xCoord);
			multiblocks[5][7][1] = this.getBlockZYX(zCoord, yCoord + 2, xCoord);
			multiblocks[6][7][1] = this.getBlockZYX(zCoord + 1, yCoord + 2, xCoord);
			multiblocks[7][7][1] = this.getBlockZYX(zCoord + 2, yCoord + 2, xCoord);
			multiblocks[8][7][1] = this.getBlockZYX(zCoord + 3, yCoord + 2, xCoord);
			multiblocks[9][7][1] = this.getBlockZYX(zCoord + 4, yCoord + 2, xCoord);
			multiblocks[10][7][1] = this.getBlockZYX(zCoord + 5, yCoord + 2, xCoord);
			multiblocks[0][8][1] = this.getBlockZYX(zCoord - 5, yCoord + 3, xCoord);
			multiblocks[1][8][1] = this.getBlockZYX(zCoord - 4, yCoord + 3, xCoord);
			multiblocks[2][8][1] = this.getBlockZYX(zCoord - 3, yCoord + 3, xCoord);
			multiblocks[3][8][1] = this.getBlockZYX(zCoord - 2, yCoord + 3, xCoord);
			multiblocks[4][8][1] = this.getBlockZYX(zCoord - 1, yCoord + 3, xCoord);
			multiblocks[5][8][1] = this.getBlockZYX(zCoord, yCoord + 3, xCoord);
			multiblocks[6][8][1] = this.getBlockZYX(zCoord + 1, yCoord + 3, xCoord);
			multiblocks[7][8][1] = this.getBlockZYX(zCoord + 2, yCoord + 3, xCoord);
			multiblocks[8][8][1] = this.getBlockZYX(zCoord + 3, yCoord + 3, xCoord);
			multiblocks[9][8][1] = this.getBlockZYX(zCoord + 4, yCoord + 3, xCoord);
			multiblocks[10][8][1] = this.getBlockZYX(zCoord + 5, yCoord + 3, xCoord);
			multiblocks[0][9][1] = this.getBlockZYX(zCoord - 5, yCoord + 4, xCoord);
			multiblocks[1][9][1] = this.getBlockZYX(zCoord - 4, yCoord + 4, xCoord);
			multiblocks[2][9][1] = this.getBlockZYX(zCoord - 3, yCoord + 4, xCoord);
			multiblocks[3][9][1] = this.getBlockZYX(zCoord - 2, yCoord + 4, xCoord);
			multiblocks[4][9][1] = this.getBlockZYX(zCoord - 1, yCoord + 4, xCoord);
			multiblocks[5][9][1] = this.getBlockZYX(zCoord, yCoord + 4, xCoord);
			multiblocks[6][9][1] = this.getBlockZYX(zCoord + 1, yCoord + 4, xCoord);
			multiblocks[7][9][1] = this.getBlockZYX(zCoord + 2, yCoord + 4, xCoord);
			multiblocks[8][9][1] = this.getBlockZYX(zCoord + 3, yCoord + 4, xCoord);
			multiblocks[9][9][1] = this.getBlockZYX(zCoord + 4, yCoord + 4, xCoord);
			multiblocks[10][9][1] = this.getBlockZYX(zCoord + 5, yCoord + 4, xCoord);
			multiblocks[0][10][1] = this.getBlockZYX(zCoord - 5, yCoord + 5, xCoord);
			multiblocks[1][10][1] = this.getBlockZYX(zCoord - 4, yCoord + 5, xCoord);
			multiblocks[2][10][1] = this.getBlockZYX(zCoord - 3, yCoord + 5, xCoord);
			multiblocks[3][10][1] = this.getBlockZYX(zCoord - 2, yCoord + 5, xCoord);
			multiblocks[4][10][1] = this.getBlockZYX(zCoord - 1, yCoord + 5, xCoord);
			multiblocks[5][10][1] = this.getBlockZYX(zCoord, yCoord + 5, xCoord);
			multiblocks[6][10][1] = this.getBlockZYX(zCoord + 1, yCoord + 5, xCoord);
			multiblocks[7][10][1] = this.getBlockZYX(zCoord + 2, yCoord + 5, xCoord);
			multiblocks[8][10][1] = this.getBlockZYX(zCoord + 3, yCoord + 5, xCoord);
			multiblocks[9][10][1] = this.getBlockZYX(zCoord + 4, yCoord + 5, xCoord);
			multiblocks[10][10][1] = this.getBlockZYX(zCoord + 5, yCoord + 5, xCoord);

			// Third slice [x][y][2]
			multiblocks[0][0][2] = this.getBlockZYX(zCoord - 5, yCoord - 5, xCoord + 1);
			multiblocks[1][0][2] = this.getBlockZYX(zCoord - 4, yCoord - 5, xCoord + 1);
			multiblocks[2][0][2] = this.getBlockZYX(zCoord - 3, yCoord - 5, xCoord + 1);
			multiblocks[3][0][2] = this.getBlockZYX(zCoord - 2, yCoord - 5, xCoord + 1);
			multiblocks[4][0][2] = this.getBlockZYX(zCoord - 1, yCoord - 5, xCoord + 1);
			multiblocks[5][0][2] = this.getBlockZYX(zCoord, yCoord - 5, xCoord + 1);
			multiblocks[6][0][2] = this.getBlockZYX(zCoord + 1, yCoord - 5, xCoord + 1);
			multiblocks[7][0][2] = this.getBlockZYX(zCoord + 2, yCoord - 5, xCoord + 1);
			multiblocks[8][0][2] = this.getBlockZYX(zCoord + 3, yCoord - 5, xCoord + 1);
			multiblocks[9][0][2] = this.getBlockZYX(zCoord + 4, yCoord - 5, xCoord + 1);
			multiblocks[10][0][2] = this.getBlockZYX(zCoord + 5, yCoord - 5, xCoord + 1);
			multiblocks[0][1][2] = this.getBlockZYX(zCoord - 5, yCoord - 4, xCoord + 1);
			multiblocks[1][1][2] = this.getBlockZYX(zCoord - 4, yCoord - 4, xCoord + 1);
			multiblocks[2][1][2] = this.getBlockZYX(zCoord - 3, yCoord - 4, xCoord + 1);
			multiblocks[3][1][2] = this.getBlockZYX(zCoord - 2, yCoord - 4, xCoord + 1);
			multiblocks[4][1][2] = this.getBlockZYX(zCoord - 1, yCoord - 4, xCoord + 1);
			multiblocks[5][1][2] = this.getBlockZYX(zCoord, yCoord - 4, xCoord + 1);
			multiblocks[6][1][2] = this.getBlockZYX(zCoord + 1, yCoord - 4, xCoord + 1);
			multiblocks[7][1][2] = this.getBlockZYX(zCoord + 2, yCoord - 4, xCoord + 1);
			multiblocks[8][1][2] = this.getBlockZYX(zCoord + 3, yCoord - 4, xCoord + 1);
			multiblocks[9][1][2] = this.getBlockZYX(zCoord + 4, yCoord - 4, xCoord + 1);
			multiblocks[10][1][2] = this.getBlockZYX(zCoord + 5, yCoord - 4, xCoord + 1);
			multiblocks[0][2][2] = this.getBlockZYX(zCoord - 5, yCoord - 3, xCoord + 1);
			multiblocks[1][2][2] = this.getBlockZYX(zCoord - 4, yCoord - 3, xCoord + 1);
			multiblocks[2][2][2] = this.getBlockZYX(zCoord - 3, yCoord - 3, xCoord + 1);
			multiblocks[3][2][2] = this.getBlockZYX(zCoord - 2, yCoord - 3, xCoord + 1);
			multiblocks[4][2][2] = this.getBlockZYX(zCoord - 1, yCoord - 3, xCoord + 1);
			multiblocks[5][2][2] = this.getBlockZYX(zCoord, yCoord - 3, xCoord + 1);
			multiblocks[6][2][2] = this.getBlockZYX(zCoord + 1, yCoord - 3, xCoord + 1);
			multiblocks[7][2][2] = this.getBlockZYX(zCoord + 2, yCoord - 3, xCoord + 1);
			multiblocks[8][2][2] = this.getBlockZYX(zCoord + 3, yCoord - 3, xCoord + 1);
			multiblocks[9][2][2] = this.getBlockZYX(zCoord + 4, yCoord - 3, xCoord + 1);
			multiblocks[10][2][2] = this.getBlockZYX(zCoord + 5, yCoord - 3, xCoord + 1);
			multiblocks[0][3][2] = this.getBlockZYX(zCoord - 5, yCoord - 2, xCoord + 1);
			multiblocks[1][3][2] = this.getBlockZYX(zCoord - 4, yCoord - 2, xCoord + 1);
			multiblocks[2][3][2] = this.getBlockZYX(zCoord - 3, yCoord - 2, xCoord + 1);
			multiblocks[3][3][2] = this.getBlockZYX(zCoord - 2, yCoord - 2, xCoord + 1);
			multiblocks[4][3][2] = this.getBlockZYX(zCoord - 1, yCoord - 2, xCoord + 1);
			multiblocks[5][3][2] = this.getBlockZYX(zCoord, yCoord - 2, xCoord + 1);
			multiblocks[6][3][2] = this.getBlockZYX(zCoord + 1, yCoord - 2, xCoord + 1);
			multiblocks[7][3][2] = this.getBlockZYX(zCoord + 2, yCoord - 2, xCoord + 1);
			multiblocks[8][3][2] = this.getBlockZYX(zCoord + 3, yCoord - 2, xCoord + 1);
			multiblocks[9][3][2] = this.getBlockZYX(zCoord + 4, yCoord - 2, xCoord + 1);
			multiblocks[10][3][2] = this.getBlockZYX(zCoord + 5, yCoord - 2, xCoord + 1);
			multiblocks[0][4][2] = this.getBlockZYX(zCoord - 5, yCoord - 1, xCoord + 1);
			multiblocks[1][4][2] = this.getBlockZYX(zCoord - 4, yCoord - 1, xCoord + 1);
			multiblocks[2][4][2] = this.getBlockZYX(zCoord - 3, yCoord - 1, xCoord + 1);
			multiblocks[3][4][2] = this.getBlockZYX(zCoord - 2, yCoord - 1, xCoord + 1);
			multiblocks[4][4][2] = this.getBlockZYX(zCoord - 1, yCoord - 1, xCoord + 1);
			multiblocks[5][4][2] = this.getBlockZYX(zCoord, yCoord - 1, xCoord + 1);
			multiblocks[6][4][2] = this.getBlockZYX(zCoord + 1, yCoord - 1, xCoord + 1);
			multiblocks[7][4][2] = this.getBlockZYX(zCoord + 2, yCoord - 1, xCoord + 1);
			multiblocks[8][4][2] = this.getBlockZYX(zCoord + 3, yCoord - 1, xCoord + 1);
			multiblocks[9][4][2] = this.getBlockZYX(zCoord + 4, yCoord - 1, xCoord + 1);
			multiblocks[10][4][2] = this.getBlockZYX(zCoord + 5, yCoord - 1, xCoord + 1);
			multiblocks[0][5][2] = this.getBlockZYX(zCoord - 5, yCoord, xCoord + 1);
			multiblocks[1][5][2] = this.getBlockZYX(zCoord - 4, yCoord, xCoord + 1);
			multiblocks[2][5][2] = this.getBlockZYX(zCoord - 3, yCoord, xCoord + 1);
			multiblocks[3][5][2] = this.getBlockZYX(zCoord - 2, yCoord, xCoord + 1);
			multiblocks[4][5][2] = this.getBlockZYX(zCoord - 1, yCoord, xCoord + 1);
			multiblocks[5][5][2] = this.getBlockZYX(zCoord, yCoord, xCoord + 1);
			multiblocks[6][5][2] = this.getBlockZYX(zCoord + 1, yCoord, xCoord + 1);
			multiblocks[7][5][2] = this.getBlockZYX(zCoord + 2, yCoord, xCoord + 1);
			multiblocks[8][5][2] = this.getBlockZYX(zCoord + 3, yCoord, xCoord + 1);
			multiblocks[9][5][2] = this.getBlockZYX(zCoord + 4, yCoord, xCoord + 1);
			multiblocks[10][5][2] = this.getBlockZYX(zCoord + 5, yCoord, xCoord + 1);
			multiblocks[0][6][2] = this.getBlockZYX(zCoord - 5, yCoord + 1, xCoord + 1);
			multiblocks[1][6][2] = this.getBlockZYX(zCoord - 4, yCoord + 1, xCoord + 1);
			multiblocks[2][6][2] = this.getBlockZYX(zCoord - 3, yCoord + 1, xCoord + 1);
			multiblocks[3][6][2] = this.getBlockZYX(zCoord - 2, yCoord + 1, xCoord + 1);
			multiblocks[4][6][2] = this.getBlockZYX(zCoord - 1, yCoord + 1, xCoord + 1);
			multiblocks[5][6][2] = this.getBlockZYX(zCoord, yCoord + 1, xCoord + 1);
			multiblocks[6][6][2] = this.getBlockZYX(zCoord + 1, yCoord + 1, xCoord + 1);
			multiblocks[7][6][2] = this.getBlockZYX(zCoord + 2, yCoord + 1, xCoord + 1);
			multiblocks[8][6][2] = this.getBlockZYX(zCoord + 3, yCoord + 1, xCoord + 1);
			multiblocks[9][6][2] = this.getBlockZYX(zCoord + 4, yCoord + 1, xCoord + 1);
			multiblocks[10][6][2] = this.getBlockZYX(zCoord + 5, yCoord + 1, xCoord + 1);
			multiblocks[0][7][2] = this.getBlockZYX(zCoord - 5, yCoord + 2, xCoord + 1);
			multiblocks[1][7][2] = this.getBlockZYX(zCoord - 4, yCoord + 2, xCoord + 1);
			multiblocks[2][7][2] = this.getBlockZYX(zCoord - 3, yCoord + 2, xCoord + 1);
			multiblocks[3][7][2] = this.getBlockZYX(zCoord - 2, yCoord + 2, xCoord + 1);
			multiblocks[4][7][2] = this.getBlockZYX(zCoord - 1, yCoord + 2, xCoord + 1);
			multiblocks[5][7][2] = this.getBlockZYX(zCoord, yCoord + 2, xCoord + 1);
			multiblocks[6][7][2] = this.getBlockZYX(zCoord + 1, yCoord + 2, xCoord + 1);
			multiblocks[7][7][2] = this.getBlockZYX(zCoord + 2, yCoord + 2, xCoord + 1);
			multiblocks[8][7][2] = this.getBlockZYX(zCoord + 3, yCoord + 2, xCoord + 1);
			multiblocks[9][7][2] = this.getBlockZYX(zCoord + 4, yCoord + 2, xCoord + 1);
			multiblocks[10][7][2] = this.getBlockZYX(zCoord + 5, yCoord + 2, xCoord + 1);
			multiblocks[0][8][2] = this.getBlockZYX(zCoord - 5, yCoord + 3, xCoord + 1);
			multiblocks[1][8][2] = this.getBlockZYX(zCoord - 4, yCoord + 3, xCoord + 1);
			multiblocks[2][8][2] = this.getBlockZYX(zCoord - 3, yCoord + 3, xCoord + 1);
			multiblocks[3][8][2] = this.getBlockZYX(zCoord - 2, yCoord + 3, xCoord + 1);
			multiblocks[4][8][2] = this.getBlockZYX(zCoord - 1, yCoord + 3, xCoord + 1);
			multiblocks[5][8][2] = this.getBlockZYX(zCoord, yCoord + 3, xCoord + 1);
			multiblocks[6][8][2] = this.getBlockZYX(zCoord + 1, yCoord + 3, xCoord + 1);
			multiblocks[7][8][2] = this.getBlockZYX(zCoord + 2, yCoord + 3, xCoord + 1);
			multiblocks[8][8][2] = this.getBlockZYX(zCoord + 3, yCoord + 3, xCoord + 1);
			multiblocks[9][8][2] = this.getBlockZYX(zCoord + 4, yCoord + 3, xCoord + 1);
			multiblocks[10][8][2] = this.getBlockZYX(zCoord + 5, yCoord + 3, xCoord + 1);
			multiblocks[0][9][2] = this.getBlockZYX(zCoord - 5, yCoord + 4, xCoord + 1);
			multiblocks[1][9][2] = this.getBlockZYX(zCoord - 4, yCoord + 4, xCoord + 1);
			multiblocks[2][9][2] = this.getBlockZYX(zCoord - 3, yCoord + 4, xCoord + 1);
			multiblocks[3][9][2] = this.getBlockZYX(zCoord - 2, yCoord + 4, xCoord + 1);
			multiblocks[4][9][2] = this.getBlockZYX(zCoord - 1, yCoord + 4, xCoord + 1);
			multiblocks[5][9][2] = this.getBlockZYX(zCoord, yCoord + 4, xCoord + 1);
			multiblocks[6][9][2] = this.getBlockZYX(zCoord + 1, yCoord + 4, xCoord + 1);
			multiblocks[7][9][2] = this.getBlockZYX(zCoord + 2, yCoord + 4, xCoord + 1);
			multiblocks[8][9][2] = this.getBlockZYX(zCoord + 3, yCoord + 4, xCoord + 1);
			multiblocks[9][9][2] = this.getBlockZYX(zCoord + 4, yCoord + 4, xCoord + 1);
			multiblocks[10][9][2] = this.getBlockZYX(zCoord + 5, yCoord + 4, xCoord + 1);
			multiblocks[0][10][2] = this.getBlockZYX(zCoord - 5, yCoord + 5, xCoord + 1);
			multiblocks[1][10][2] = this.getBlockZYX(zCoord - 4, yCoord + 5, xCoord + 1);
			multiblocks[2][10][2] = this.getBlockZYX(zCoord - 3, yCoord + 5, xCoord + 1);
			multiblocks[3][10][2] = this.getBlockZYX(zCoord - 2, yCoord + 5, xCoord + 1);
			multiblocks[4][10][2] = this.getBlockZYX(zCoord - 1, yCoord + 5, xCoord + 1);
			multiblocks[5][10][2] = this.getBlockZYX(zCoord, yCoord + 5, xCoord + 1);
			multiblocks[6][10][2] = this.getBlockZYX(zCoord + 1, yCoord + 5, xCoord + 1);
			multiblocks[7][10][2] = this.getBlockZYX(zCoord + 2, yCoord + 5, xCoord + 1);
			multiblocks[8][10][2] = this.getBlockZYX(zCoord + 3, yCoord + 5, xCoord + 1);
			multiblocks[9][10][2] = this.getBlockZYX(zCoord + 4, yCoord + 5, xCoord + 1);
			multiblocks[10][10][2] = this.getBlockZYX(zCoord + 5, yCoord + 5, xCoord + 1);
		}
		
		if (multiblocks[3][0][0] != null && multiblocks[4][0][0] != null && multiblocks[5][0][0] != null && multiblocks[6][0][0] != null && multiblocks[7][0][0] != null && multiblocks[2][1][0] != null && multiblocks[3][1][0] != null && multiblocks[4][1][0] != null && multiblocks[5][1][0] != null && multiblocks[6][1][0] != null && multiblocks[7][1][0] != null && multiblocks[8][1][0] != null && multiblocks[1][2][0] != null && multiblocks[2][2][0] != null && multiblocks[3][2][0] != null && multiblocks[4][2][0] != null && multiblocks[5][2][0] != null && multiblocks[6][2][0] != null && multiblocks[7][2][0] != null && multiblocks[8][2][0] != null && multiblocks[9][2][0] != null && multiblocks[0][3][0] != null && multiblocks[1][3][0] != null && multiblocks[2][3][0] != null && multiblocks[3][3][0] != null && multiblocks[4][3][0] != null && multiblocks[5][3][0] != null && multiblocks[6][3][0] != null && multiblocks[7][3][0] != null && multiblocks[8][3][0] != null
				&& multiblocks[9][3][0] != null && multiblocks[10][3][0] != null && multiblocks[0][4][0] != null && multiblocks[1][4][0] != null && multiblocks[2][4][0] != null && multiblocks[3][4][0] != null && multiblocks[4][4][0] != null && multiblocks[5][4][0] != null && multiblocks[6][4][0] != null && multiblocks[7][4][0] != null && multiblocks[8][4][0] != null && multiblocks[9][4][0] != null && multiblocks[10][4][0] != null && multiblocks[0][5][0] != null && multiblocks[1][5][0] != null && multiblocks[2][5][0] != null && multiblocks[3][5][0] != null && multiblocks[4][5][0] != null && multiblocks[5][5][0] != null && multiblocks[6][5][0] != null && multiblocks[7][5][0] != null && multiblocks[8][5][0] != null && multiblocks[9][5][0] != null && multiblocks[10][5][0] != null && multiblocks[0][6][0] != null && multiblocks[1][6][0] != null && multiblocks[2][6][0] != null && multiblocks[3][6][0] != null && multiblocks[4][6][0] != null && multiblocks[5][6][0] != null
				&& multiblocks[6][6][0] != null && multiblocks[7][6][0] != null && multiblocks[8][6][0] != null && multiblocks[9][6][0] != null && multiblocks[10][6][0] != null && multiblocks[0][7][0] != null && multiblocks[1][7][0] != null && multiblocks[2][7][0] != null && multiblocks[3][7][0] != null && multiblocks[4][7][0] != null && multiblocks[5][7][0] != null && multiblocks[6][7][0] != null && multiblocks[7][7][0] != null && multiblocks[8][7][0] != null && multiblocks[9][7][0] != null && multiblocks[10][7][0] != null && multiblocks[1][8][0] != null && multiblocks[2][8][0] != null && multiblocks[3][8][0] != null && multiblocks[4][8][0] != null && multiblocks[5][8][0] != null && multiblocks[6][8][0] != null && multiblocks[7][8][0] != null && multiblocks[8][8][0] != null && multiblocks[9][8][0] != null && multiblocks[2][9][0] != null && multiblocks[3][9][0] != null && multiblocks[4][9][0] != null && multiblocks[5][9][0] != null && multiblocks[6][9][0] != null
				&& multiblocks[7][9][0] != null && multiblocks[8][9][0] != null && multiblocks[3][10][0] != null && multiblocks[4][10][0] != null && multiblocks[5][10][0] != null && multiblocks[6][10][0] != null && multiblocks[7][10][0] != null && multiblocks[3][0][1] != null && multiblocks[4][0][1] != null && multiblocks[5][0][1] != null && multiblocks[6][0][1] != null && multiblocks[7][0][1] != null && multiblocks[2][1][1] != null && multiblocks[3][1][1] != null && multiblocks[4][1][1] != null && multiblocks[5][1][1] != null && multiblocks[6][1][1] != null && multiblocks[7][1][1] != null && multiblocks[8][1][1] != null && multiblocks[1][2][1] != null && multiblocks[2][2][1] != null && multiblocks[3][2][1] != null && multiblocks[4][2][1] != null && multiblocks[5][2][1] != null && multiblocks[6][2][1] != null && multiblocks[7][2][1] != null && multiblocks[8][2][1] != null && multiblocks[9][2][1] != null && multiblocks[0][3][1] != null && multiblocks[1][3][1] != null
				&& multiblocks[2][3][1] != null && multiblocks[3][3][1] != null && multiblocks[4][3][1] != null && multiblocks[5][3][1] != null && multiblocks[6][3][1] != null && multiblocks[7][3][1] != null && multiblocks[8][3][1] != null && multiblocks[9][3][1] != null && multiblocks[10][3][1] != null && multiblocks[0][4][1] != null && multiblocks[1][4][1] != null && multiblocks[2][4][1] != null && multiblocks[3][4][1] != null && multiblocks[4][4][1] != null && multiblocks[5][4][1] != null && multiblocks[6][4][1] != null && multiblocks[7][4][1] != null && multiblocks[8][4][1] != null && multiblocks[9][4][1] != null && multiblocks[10][4][1] != null && multiblocks[0][5][1] != null && multiblocks[1][5][1] != null && multiblocks[2][5][1] != null && multiblocks[3][5][1] != null && multiblocks[4][5][1] != null && multiblocks[5][5][1] != null && multiblocks[6][5][1] != null && multiblocks[7][5][1] != null && multiblocks[8][5][1] != null && multiblocks[9][5][1] != null
				&& multiblocks[10][5][1] != null && multiblocks[0][6][1] != null && multiblocks[1][6][1] != null && multiblocks[2][6][1] != null && multiblocks[3][6][1] != null && multiblocks[4][6][1] != null && multiblocks[5][6][1] != null && multiblocks[6][6][1] != null && multiblocks[7][6][1] != null && multiblocks[8][6][1] != null && multiblocks[9][6][1] != null && multiblocks[10][6][1] != null && multiblocks[0][7][1] != null && multiblocks[1][7][1] != null && multiblocks[2][7][1] != null && multiblocks[3][7][1] != null && multiblocks[4][7][1] != null && multiblocks[5][7][1] != null && multiblocks[6][7][1] != null && multiblocks[7][7][1] != null && multiblocks[8][7][1] != null && multiblocks[9][7][1] != null && multiblocks[10][7][1] != null && multiblocks[1][8][1] != null && multiblocks[2][8][1] != null && multiblocks[3][8][1] != null && multiblocks[4][8][1] != null && multiblocks[5][8][1] != null && multiblocks[6][8][1] != null && multiblocks[7][8][1] != null
				&& multiblocks[8][8][1] != null && multiblocks[9][8][1] != null && multiblocks[2][9][1] != null && multiblocks[3][9][1] != null && multiblocks[4][9][1] != null && multiblocks[5][9][1] != null && multiblocks[6][9][1] != null && multiblocks[7][9][1] != null && multiblocks[8][9][1] != null && multiblocks[3][10][1] != null && multiblocks[4][10][1] != null && multiblocks[5][10][1] != null && multiblocks[6][10][1] != null && multiblocks[7][10][1] != null && multiblocks[3][0][2] != null && multiblocks[4][0][2] != null && multiblocks[5][0][2] != null && multiblocks[6][0][2] != null && multiblocks[7][0][2] != null && multiblocks[2][1][2] != null && multiblocks[3][1][2] != null && multiblocks[4][1][2] != null && multiblocks[5][1][2] != null && multiblocks[6][1][2] != null && multiblocks[7][1][2] != null && multiblocks[8][1][2] != null && multiblocks[1][2][2] != null && multiblocks[2][2][2] != null && multiblocks[3][2][2] != null && multiblocks[4][2][2] != null
				&& multiblocks[5][2][2] != null && multiblocks[6][2][2] != null && multiblocks[7][2][2] != null && multiblocks[8][2][2] != null && multiblocks[9][2][2] != null && multiblocks[0][3][2] != null && multiblocks[1][3][2] != null && multiblocks[2][3][2] != null && multiblocks[3][3][2] != null && multiblocks[4][3][2] != null && multiblocks[5][3][2] != null && multiblocks[6][3][2] != null && multiblocks[7][3][2] != null && multiblocks[8][3][2] != null && multiblocks[9][3][2] != null && multiblocks[10][3][2] != null && multiblocks[0][4][2] != null && multiblocks[1][4][2] != null && multiblocks[2][4][2] != null && multiblocks[3][4][2] != null && multiblocks[4][4][2] != null && multiblocks[5][4][2] != null && multiblocks[6][4][2] != null && multiblocks[7][4][2] != null && multiblocks[8][4][2] != null && multiblocks[9][4][2] != null && multiblocks[10][4][2] != null && multiblocks[0][5][2] != null && multiblocks[1][5][2] != null && multiblocks[2][5][2] != null
				&& multiblocks[3][5][2] != null && multiblocks[4][5][2] != null && multiblocks[5][5][2] != null && multiblocks[6][5][2] != null && multiblocks[7][5][2] != null && multiblocks[8][5][2] != null && multiblocks[9][5][2] != null && multiblocks[10][5][2] != null && multiblocks[0][6][2] != null && multiblocks[1][6][2] != null && multiblocks[2][6][2] != null && multiblocks[3][6][2] != null && multiblocks[4][6][2] != null && multiblocks[5][6][2] != null && multiblocks[6][6][2] != null && multiblocks[7][6][2] != null && multiblocks[8][6][2] != null && multiblocks[9][6][2] != null && multiblocks[10][6][2] != null && multiblocks[0][7][2] != null && multiblocks[1][7][2] != null && multiblocks[2][7][2] != null && multiblocks[3][7][2] != null && multiblocks[4][7][2] != null && multiblocks[5][7][2] != null && multiblocks[6][7][2] != null && multiblocks[7][7][2] != null && multiblocks[8][7][2] != null && multiblocks[9][7][2] != null && multiblocks[10][7][2] != null
				&& multiblocks[1][8][2] != null && multiblocks[2][8][2] != null && multiblocks[3][8][2] != null && multiblocks[4][8][2] != null && multiblocks[5][8][2] != null && multiblocks[6][8][2] != null && multiblocks[7][8][2] != null && multiblocks[8][8][2] != null && multiblocks[9][8][2] != null && multiblocks[2][9][2] != null && multiblocks[3][9][2] != null && multiblocks[4][9][2] != null && multiblocks[5][9][2] != null && multiblocks[6][9][2] != null && multiblocks[7][9][2] != null && multiblocks[8][9][2] != null && multiblocks[3][10][2] != null && multiblocks[4][10][2] != null && multiblocks[5][10][2] != null && multiblocks[6][10][2] != null && multiblocks[7][10][2] != null)
		{
			if (multiblocks[5][5][1].equals(LabStuffMain.blockAcceleratorDetectorCore)) {

				if (multiblocks[5][4][1].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][4][1].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][5][1].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][5][1].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][6][1].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[5][6][1].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][6][1].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][4][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[5][4][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][4][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][5][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][5][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][6][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[5][6][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][6][0].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][4][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[5][4][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][4][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][5][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][5][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[4][6][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[5][6][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)
						&& multiblocks[6][6][2].equals(LabStuffMain.blockAcceleratorTrackingDetector)) {
					if (multiblocks[3][3][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[4][3][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[5][3][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[6][3][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][3][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][4][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][4][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][5][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][5][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][6][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][6][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][7][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[4][7][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[5][7][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[6][7][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][7][1].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][3][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[4][3][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[5][3][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[6][3][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][3][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][4][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][4][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][5][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][5][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][6][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][6][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][7][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[4][7][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[5][7][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[6][7][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][7][0].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][3][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[4][3][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[5][3][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[6][3][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][3][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][4][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][4][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][5][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][5][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][6][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][6][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[3][7][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[4][7][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[5][7][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[6][7][2].equals(LabStuffMain.blockAcceleratorSolenoid)
							&& multiblocks[7][7][2].equals(LabStuffMain.blockAcceleratorSolenoid)) {
						if (multiblocks[3][2][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[4][2][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[5][2][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[6][2][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[7][2][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][3][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][3][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][4][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][4][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][5][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][5][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][6][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][6][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][7][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][7][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[3][8][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[4][8][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[5][8][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[6][8][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[7][8][1].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[3][2][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[4][2][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[5][2][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[6][2][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[7][2][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][3][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][3][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][4][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][4][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][5][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][5][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][6][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][6][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][7][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][7][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[3][8][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[4][8][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[5][8][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[6][8][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[7][8][0].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[3][2][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[4][2][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[5][2][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[6][2][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[7][2][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][3][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][3][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][4][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][4][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][5][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][5][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][6][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][6][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[2][7][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[8][7][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[3][8][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[4][8][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[5][8][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[6][8][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)
								&& multiblocks[7][8][2].equals(LabStuffMain.blockAcceleratorElectromagneticCalorimeter)) {
							if (multiblocks[3][1][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[4][1][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[5][1][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[6][1][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[7][1][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][3][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][3][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][4][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][4][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][5][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][5][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][6][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][6][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][7][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][7][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[3][9][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[4][9][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[5][9][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[6][9][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[7][9][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[2][2][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[8][2][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[2][8][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[8][8][1].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[3][1][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[4][1][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[5][1][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[6][1][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[7][1][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][3][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][3][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][4][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][4][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][5][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][5][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][6][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][6][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][7][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][7][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[3][9][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[4][9][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[5][9][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[6][9][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[7][9][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[2][2][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[8][2][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[2][8][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[8][8][0].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[3][1][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[4][1][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[5][1][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[6][1][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[7][1][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][3][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][3][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][4][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][4][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][5][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][5][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][6][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][6][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[1][7][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[9][7][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[3][9][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[4][9][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[5][9][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[6][9][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[7][9][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[2][2][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[8][2][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[2][8][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)
									&& multiblocks[8][8][2].equals(LabStuffMain.blockAcceleratorHadronCalorimeter)) {
								if (multiblocks[5][5][2].equals(LabStuffMain.blockAcceleratorTube)
										&& multiblocks[5][5][0].equals(LabStuffMain.blockAcceleratorTube)) {
									if (multiblocks[3][0][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[4][0][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[5][0][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[6][0][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[7][0][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][3][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][3][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][4][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][4][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][5][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][5][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][6][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][6][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][7][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][7][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[3][10][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[4][10][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[5][10][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[6][10][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[7][10][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[2][1][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[1][2][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[8][1][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[9][2][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[1][8][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[2][9][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[8][9][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[9][8][1].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[3][0][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[4][0][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[5][0][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[6][0][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[7][0][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][3][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][3][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][4][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][4][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][5][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][5][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][6][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][6][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][7][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][7][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[3][10][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[4][10][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[5][10][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[6][10][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[7][10][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[2][1][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[1][2][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[8][1][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[9][2][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[1][8][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[2][9][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[8][9][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[9][8][0].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[3][0][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[4][0][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[5][0][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[6][0][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[7][0][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][3][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][3][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][4][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][4][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][5][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][5][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][6][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][6][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[0][7][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[10][7][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[3][10][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[4][10][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[5][10][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[6][10][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[7][10][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[2][1][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[1][2][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[8][1][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[9][2][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[1][8][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[2][9][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[8][9][2].equals(LabStuffMain.blockAcceleratorMuonDetector)
											&& multiblocks[9][8][2].equals(LabStuffMain.blockAcceleratorMuonDetector)) {
										// ((TileEntityAcceleratorTube)
										// this.getTileEntity(xCoord, yCoord,
										// zCoord +
										// 1)).addDetector(((TileEntityAcceleratorTube)
										// this.getTileEntity(xCoord, yCoord,
										// zCoord - 1)));
										isMultiblockComplete = true;
									} else {
										isMultiblockComplete = false;
									}
								} else
								{
									isMultiblockComplete = false;
								}
							} else {
								isMultiblockComplete = false;
							}
						} else {
							isMultiblockComplete = false;
						}
					} else {
						isMultiblockComplete = false;
					}
				} else {
					isMultiblockComplete = false;
				}
			}
		} else {
			isMultiblockComplete = false;
		}
	
		return isMultiblockComplete;
	}

	
	//I made this because I'm lazy
	private Block getBlockZYX(int i, int j, int k) {
		// TODO Auto-generated method stub
		return getBlock(k, j, i);
	}

	public boolean isPowered() {
		return isPowered;
	}

	public boolean isGoodForLaunch() {
		if (worldObj.getTileEntity(pos.south()) != null)
			return isAcceleratorComplete() > 0 && isPowered;
		return false;
	}

	public int isAcceleratorComplete() 
	{
		if(isMultiblockComplete())
		{
			if(worldObj.getBlockState(pos.add(0,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(0,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) && worldObj.getBlockState(pos.add(0,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(0,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
			{
				//Z Axis
				if(worldObj.getBlockState(pos.add(1,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(1,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) && worldObj.getBlockState(pos.add(1,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(1,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
				{
					//x+
					if(worldObj.getBlockState(pos.add(2,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) 
							&& worldObj.getBlockState(pos.add(3,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(3,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(4,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(4,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(5,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(5,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(6,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(6,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(7,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(7,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(8,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(8,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(9,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(9,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(10,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(10,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(11,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(11,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(12,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(12,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(13,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(13,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(14,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(14,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(15,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(15,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(16,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(16,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(3,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(3,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(4,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(4,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(5,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(5,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(6,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(6,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(7,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(7,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(8,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(8,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(9,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(9,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(10,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(10,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(11,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(11,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(12,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(12,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(13,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(13,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(14,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(14,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(15,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(15,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(16,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(16,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(16,0,1)).getBlock() != null && worldObj.getBlockState(pos.add(16,0,1)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(16,0,0)).getBlock() != null && worldObj.getBlockState(pos.add(16,0,0)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(16,0,-1)).getBlock() != null && worldObj.getBlockState(pos.add(16,0,-1)).getBlock().equals(LabStuffMain.blockAcceleratorTube))

					{
						return 1;
					}
				}
				else if(worldObj.getBlockState(pos.add(-1,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-1,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) && worldObj.getBlockState(pos.add(-1,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-1,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
				{
					//x-
					if(worldObj.getBlockState(pos.add(-2,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) 
							&& worldObj.getBlockState(pos.add(-3,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-3,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-4,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-4,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-5,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-5,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-6,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-6,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-7,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-7,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-8,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-8,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-9,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-9,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-10,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-10,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-11,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-11,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-12,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-12,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-13,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-13,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-14,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-14,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-15,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-15,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-16,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-16,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-3,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-3,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-4,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-4,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-5,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-5,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-6,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-6,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-7,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-7,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-8,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-8,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-9,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-9,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-10,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-10,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-11,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-11,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-12,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-12,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-13,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-13,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-14,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-14,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-15,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-15,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-16,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-16,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-16,0,1)).getBlock() != null && worldObj.getBlockState(pos.add(-16,0,1)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-16,0,0)).getBlock() != null && worldObj.getBlockState(pos.add(-16,0,0)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-16,0,-1)).getBlock() != null && worldObj.getBlockState(pos.add(-16,0,-1)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
					{
						return 1;
					}
				}
			}
			else if(worldObj.getBlockState(pos.add(2,0,0)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,0)).getBlock().equals(LabStuffMain.blockAcceleratorTube) && worldObj.getBlockState(pos.add(-2,0,0)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,0)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
			{
				//X axis
				if(worldObj.getBlockState(pos.add(2,0,1)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,1)).getBlock().equals(LabStuffMain.blockAcceleratorTube) && worldObj.getBlockState(pos.add(-2,0,1)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,1)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
				{
					//z+
					if(worldObj.getBlockState(pos.add(2,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) 
							&& worldObj.getBlockState(pos.add(-2,0,3)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,3)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,4)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,4)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,5)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,5)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,6)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,6)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,7)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,7)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,8)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,8)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,9)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,9)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,10)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,10)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,11)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,11)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,12)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,12)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,13)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,13)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,14)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,14)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,15)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,15)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,16)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,3)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,3)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,4)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,4)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,5)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,5)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,6)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,6)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,7)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,7)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,8)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,8)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,9)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,9)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,10)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,10)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,11)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,11)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,12)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,12)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,13)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,13)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,14)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,14)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,15)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,15)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,16)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(1,0,16)).getBlock() != null && worldObj.getBlockState(pos.add(1,0,16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(0,0,16)).getBlock() != null && worldObj.getBlockState(pos.add(0,0,16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-1,0,16)).getBlock() != null && worldObj.getBlockState(pos.add(-1,0,16)).getBlock().equals(LabStuffMain.blockAcceleratorTube))

					{
						return 2;
					}
				}
				else if(worldObj.getBlockState(pos.add(-1,0,2)).getBlock() != null && worldObj.getBlockState(pos.add(-1,0,2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) && worldObj.getBlockState(pos.add(-1,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(-1,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
				{
					//z-
					if(worldObj.getBlockState(pos.add(2,0,-2)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-2)).getBlock().equals(LabStuffMain.blockAcceleratorTube) 
							&& worldObj.getBlockState(pos.add(-2,0,-3)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-3)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-4)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-4)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-5)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-5)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-6)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-6)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-7)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-7)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-8)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-8)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-9)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-9)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-10)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-10)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-11)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-11)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-12)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-12)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-13)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-13)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-14)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-14)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-15)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-15)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-2,0,-16)).getBlock() != null && worldObj.getBlockState(pos.add(-2,0,-16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-3)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-3)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-4)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-4)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-5)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-5)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-6)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-6)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-7)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-7)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-8)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-8)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-9)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-9)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-10)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-10)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-11)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-11)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-12)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-12)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-13)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-13)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-14)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-14)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-15)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-15)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(2,0,-16)).getBlock() != null && worldObj.getBlockState(pos.add(2,0,-16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(1,0,-16)).getBlock() != null && worldObj.getBlockState(pos.add(1,0,-16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(0,0,-16)).getBlock() != null && worldObj.getBlockState(pos.add(0,0,-16)).getBlock().equals(LabStuffMain.blockAcceleratorTube)
							&& worldObj.getBlockState(pos.add(-1,0,-16)).getBlock() != null && worldObj.getBlockState(pos.add(-1,0,-16)).getBlock().equals(LabStuffMain.blockAcceleratorTube))
					{
						return 2;
					}
				}
			
			}
		}
		return 0;
	}

	private TileEntity getTileEntity(int xCoord, int yCoord, int zCoord) {
		// TODO Auto-generated method stub
		return worldObj.getTileEntity(new BlockPos(xCoord, yCoord, zCoord));
	}

	public void setPowered(boolean isPowered) {
		this.isPowered = isPowered;
	}
}
