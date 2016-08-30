package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockElectronGrabber;
import keegan.labstuff.blocks.BlockGasChamberPort;
import keegan.labstuff.blocks.BlockGasChamberWall;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public class TileEntityElectronGrabber extends TileEntity {

	public Block[][][] multiblocks = new Block[3][3][3];
	private boolean multiblock = false;
	private int[] coords = new int[3];

	public TileEntityElectronGrabber() {
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);
		tagCompound.setInteger("blockMeta", blockMetadata);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, tag.getInteger("blockMeta"), 2);
	}

	public boolean isMultiblock() {
		coords[0] = xCoord;
		coords[1] = yCoord;
		coords[2] = zCoord;
		int coreX = xCoord;
		int coreY = yCoord;
		int coreZ = zCoord;
		if (this.blockMetadata == 0) {
			coreX = xCoord;
			coreY = yCoord;
			coreZ = zCoord - 1;
		}

		if (blockMetadata == 1) {
			coreX = xCoord - 1;
			coreY = yCoord;
			coreZ = zCoord;
		}

		if (blockMetadata == 2) {
			coreX = xCoord;
			coreY = yCoord;
			coreZ = zCoord + 1;
		}

		if (blockMetadata == 3) {
			coreX = xCoord + 1;
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
		multiblocks[0][2][1] = worldObj.getBlock(coreX, coreY + 1, coreZ - 1);
		multiblocks[0][2][2] = worldObj.getBlock(coreX - 1, coreY + 1, coreZ - 1);
		multiblocks[1][0][0] = worldObj.getBlock(coreX + 1, coreY, coreZ + 1);
		multiblocks[1][0][1] = worldObj.getBlock(coreX, coreY, coreZ + 1);
		multiblocks[1][0][2] = worldObj.getBlock(coreX - 1, coreY, coreZ + 1);
		multiblocks[1][1][0] = worldObj.getBlock(coreX + 1, coreY, coreZ);
		multiblocks[1][1][1] = worldObj.getBlock(coreX, coreY, coreZ);
		multiblocks[1][1][2] = worldObj.getBlock(coreX - 1, coreY, coreZ);
		multiblocks[1][2][0] = worldObj.getBlock(coreX + 1, coreY, coreZ - 1);
		multiblocks[1][2][1] = worldObj.getBlock(coreX, coreY, coreZ - 1);
		multiblocks[1][2][2] = worldObj.getBlock(coreX - 1, coreY, coreZ - 1);
		multiblocks[2][0][0] = worldObj.getBlock(coreX + 1, coreY - 1, coreZ + 1);
		multiblocks[2][0][1] = worldObj.getBlock(coreX, coreY - 1, coreZ + 1);
		multiblocks[2][0][2] = worldObj.getBlock(coreX - 1, coreY - 1, coreZ + 1);
		multiblocks[2][1][0] = worldObj.getBlock(coreX + 1, coreY - 1, coreZ);
		multiblocks[2][1][1] = worldObj.getBlock(coreX, coreY - 1, coreZ);
		multiblocks[2][1][2] = worldObj.getBlock(coreX - 1, coreY - 1, coreZ);
		multiblocks[2][2][0] = worldObj.getBlock(coreX + 1, coreY - 1, coreZ - 1);
		multiblocks[2][2][1] = worldObj.getBlock(coreX, coreY - 1, coreZ - 1);
		multiblocks[2][2][2] = worldObj.getBlock(coreX - 1, coreY - 1, coreZ - 1);
		if (multiblocks[0][0][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][0][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][0][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][1][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][1][1].equals(LabStuffMain.blockGasChamberPort)
				&& multiblocks[0][1][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][2][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][2][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][2][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[1][0][0].equals(LabStuffMain.blockGasChamberWall)
				&& (multiblocks[1][0][1].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][0][1].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][0][1].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][0][2].equals(LabStuffMain.blockGasChamberWall)
				&& (multiblocks[1][1][0].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][1][0].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][1][0].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][1][1].equals(Blocks.air)
				&& (multiblocks[1][1][2].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][1][2].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][1][2].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][2][0].equals(LabStuffMain.blockGasChamberWall)
				&& (multiblocks[1][2][1].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][2][1].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][2][1].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][2][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][0][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][0][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][0][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][1][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][1][1].equals(LabStuffMain.blockGasChamberPort)
				&& multiblocks[2][1][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][2][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][2][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][2][2].equals(LabStuffMain.blockGasChamberWall)) 
		{
			if (multiblocks[1][0][1].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
			if (multiblocks[1][1][0].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
			if (multiblocks[1][1][2].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
			if (multiblocks[1][2][1].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
		}
		return multiblock;

	}

}
