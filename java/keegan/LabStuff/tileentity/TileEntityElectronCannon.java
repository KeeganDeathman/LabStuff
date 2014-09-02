package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockElectronCannon;
import keegan.labstuff.blocks.BlockGasChamberPort;
import keegan.labstuff.blocks.BlockGasChamberWall;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

public class TileEntityElectronCannon extends TileEntity 
{
	
	public Block[][][] multiblocks = new Block[3][3][3];
	private int[] coords = new int[3];
	
	public TileEntityElectronCannon() 
	{
	}

	@Override
	public void updateEntity()
	{
		coords[0] = xCoord;
		coords[1] = yCoord;
		coords[2] = zCoord;
		int coreX = xCoord;
		int coreY = yCoord;
		int coreZ = zCoord;
		if(this.blockMetadata == 0)
		{
			coreX = xCoord;
			coreY = yCoord;
			coreZ = zCoord + 1;
		}
		
		if(blockMetadata == 2)
		{
			coreX = xCoord;
			coreY = yCoord;
			coreZ = zCoord - 1;
		}
		
		if(blockMetadata == 1)
		{
			coreX = xCoord + 1;
			coreY = yCoord;
			coreZ = zCoord;
		}
		
		if(blockMetadata == 1)
		{
			coreX = xCoord - 1;
			coreY = yCoord;
			coreZ = zCoord;
		}
			
		multiblocks[0][0][0] = worldObj.getBlock(coreX + 1, coreY + 1, coreZ + 1);
		multiblocks[0][0][1] = worldObj.getBlock(coreX, coreY + 1, coreZ + 1);
		multiblocks[0][0][2] = worldObj.getBlock(coreX - 1, coreY + 1, coreZ + 1);
		multiblocks[0][1][0] = worldObj.getBlock(coreX + 1, coreY + 1, coreZ);
		multiblocks[0][1][1] = worldObj.getBlock(coreX, coreY + 1, coreZ);
		multiblocks[0][1][2] = worldObj.getBlock(coreX - 1, coreY + 1, coreZ);
		multiblocks[0][2][0] = worldObj.getBlock(coreX + 1, coreY + 1, coreZ - 1);
		multiblocks[0][2][0] = worldObj.getBlock(coreX, coreY + 1, coreZ - 1);
		multiblocks[0][2][0] = worldObj.getBlock(coreX - 1, coreY + 1, coreZ - 1);
		multiblocks[1][0][0] = worldObj.getBlock(coreX + 1, coreY, coreZ + 1);
		multiblocks[1][0][1] = worldObj.getBlock(coreX, coreY, coreZ + 1);
		multiblocks[1][0][2] = worldObj.getBlock(coreX - 1, coreY, coreZ + 1);
		multiblocks[1][1][0] = worldObj.getBlock(coreX + 1, coreY, coreZ);
		multiblocks[1][1][1] = worldObj.getBlock(coreX, coreY, coreZ);
		multiblocks[1][1][2] = worldObj.getBlock(coreX - 1, coreY, coreZ);
		multiblocks[1][2][0] = worldObj.getBlock(coreX + 1, coreY, coreZ - 1);
		multiblocks[1][2][0] = worldObj.getBlock(coreX, coreY, coreZ - 1);
		multiblocks[1][2][0] = worldObj.getBlock(coreX - 1, coreY, coreZ - 1);
		multiblocks[2][0][0] = worldObj.getBlock(coreX + 1, coreY - 1, coreZ + 1);
		multiblocks[2][0][1] = worldObj.getBlock(coreX, coreY - 1, coreZ + 1);
		multiblocks[2][0][2] = worldObj.getBlock(coreX - 1, coreY - 1, coreZ + 1);
		multiblocks[2][1][0] = worldObj.getBlock(coreX + 1, coreY - 1, coreZ);
		multiblocks[2][1][1] = worldObj.getBlock(coreX, coreY - 1, coreZ);
		multiblocks[2][1][2] = worldObj.getBlock(coreX - 1, coreY - 1, coreZ);
		multiblocks[2][2][0] = worldObj.getBlock(coreX + 1, coreY - 1, coreZ - 1);
		multiblocks[2][2][1] = worldObj.getBlock(coreX, coreY - 1, coreZ - 1);
		multiblocks[2][2][2] = worldObj.getBlock(coreX - 1, coreY - 1, coreZ - 1);
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				for(int k = 0; k < 3; k++)
				{
					if (multiblocks[0][0][0] == LabStuffMain.blockGasChamberWall
					&& multiblocks[0][0][1] == LabStuffMain.blockGasChamberWall
					&& multiblocks[0][0][2] == LabStuffMain.blockGasChamberWall
					&& multiblocks[0][1][0] == LabStuffMain.blockGasChamberWall
					&& multiblocks[0][1][1] == LabStuffMain.blockGasChamberPort
					&& multiblocks[0][1][2] == LabStuffMain.blockGasChamberWall
					&& multiblocks[0][2][0] == LabStuffMain.blockGasChamberWall
					&& multiblocks[0][2][1] == LabStuffMain.blockGasChamberWall
					&& multiblocks[0][2][2] == LabStuffMain.blockGasChamberWall
					&& multiblocks[1][0][0] == LabStuffMain.blockGasChamberWall
					&& (multiblocks[1][0][1] == LabStuffMain.blockGasChamberWall || multiblocks[1][0][1] == LabStuffMain.blockElectronCannon)
					&& multiblocks[1][0][2] == LabStuffMain.blockGasChamberWall
					&& (multiblocks[1][1][0] == LabStuffMain.blockGasChamberWall || multiblocks[1][1][0] == LabStuffMain.blockElectronCannon)
					&& multiblocks[1][1][1] == Blocks.air
					&& (multiblocks[1][1][2] == LabStuffMain.blockGasChamberWall || multiblocks[1][1][2] == LabStuffMain.blockElectronCannon)
					&& multiblocks[1][2][0] == LabStuffMain.blockGasChamberWall
					&& (multiblocks[1][2][1] == LabStuffMain.blockGasChamberWall || multiblocks[1][2][1] == LabStuffMain.blockElectronCannon)
					&& multiblocks[1][2][2] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][0][0] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][0][1] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][0][2] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][1][0] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][1][1] == LabStuffMain.blockGasChamberPort
					&& multiblocks[2][1][2] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][2][0] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][2][1] == LabStuffMain.blockGasChamberWall
					&& multiblocks[2][2][2] == LabStuffMain.blockGasChamberWall) 
					{
						if (multiblocks[i][j][k] != null
								&& multiblocks[i][j][k] == LabStuffMain.blockGasChamberWall) {
							System.out.println("Lets a go!");
							((BlockGasChamberWall) multiblocks[i][j][k])
									.setMultiBlockState(true, multiblocks,
											coords, worldObj);
						} else if (multiblocks[i][j][k] != null
								&& multiblocks[i][j][k] == LabStuffMain.blockGasChamberPort
								&& i == 0) {
							((BlockGasChamberPort) multiblocks[i][j][k]).input = true;
						}
					}
				}
			}
		}
		
		if(worldObj.getBlock(xCoord, yCoord, zCoord) == LabStuffMain.blockElectronCannon)
		{
			((BlockElectronCannon) worldObj.getBlock(xCoord, yCoord, zCoord)).multiblocks = multiblocks;
		}
		
	}
	
	
}
