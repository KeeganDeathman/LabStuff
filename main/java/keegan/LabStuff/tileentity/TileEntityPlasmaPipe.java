package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.BlockPlasmaPipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;


public class TileEntityPlasmaPipe extends TileEntityPlasma	
{
	
	private boolean networked;
	
	public TileEntityPlasmaPipe()
	{
		super();
		networked = false;
	}
	
	public TileEntityPlasmaPipe(World world)
	{
		super();
		plasmaInt = this.getPlasma();
		this.worldObj = world;
	}
	
	@Override
	public void updateEntity()
	{
		if(!networked)
			equalize();
	}
	
	//ONLY call when the block is added!
	public void equalize()
	{
		if(!worldObj.isRemote) {
			if (worldObj.getBlock(xCoord + 1, yCoord, zCoord) != null && getPlasma() == 0) {
				eqaulizeWith(xCoord + 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord - 1, yCoord, zCoord) != null && getPlasma() == 0) {
				eqaulizeWith(xCoord - 1, yCoord, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) != null && getPlasma() == 0) {
					eqaulizeWith(xCoord, yCoord + 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) != null && getPlasma() == 0) {
				 eqaulizeWith(xCoord, yCoord - 1, zCoord);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord + 1) != null && getPlasma() == 0) {
				 eqaulizeWith(xCoord, yCoord, zCoord + 1);
			}if (worldObj.getBlock(xCoord, yCoord, zCoord - 1) != null && getPlasma() == 0) {
				 eqaulizeWith(xCoord, yCoord, zCoord - 1);
			}
		}
	}
	
	private void eqaulizeWith(int x, int y, int z)
	{
		if(worldObj.getBlock(x,y,z) == LabStuffMain.blockPlasmaPipe)
		{
			TileEntityPlasmaPipe tile = (TileEntityPlasmaPipe)worldObj.getTileEntity(x,y,z);
			plasmaInt = tile.getPlasma();
			if(getPlasma() > 0)
				networked = true;
		}
	}
}
