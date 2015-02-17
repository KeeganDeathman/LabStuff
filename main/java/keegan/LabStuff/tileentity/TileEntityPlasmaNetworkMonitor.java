package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.BlockPlasmaPipe;
import net.minecraft.world.World;

public class TileEntityPlasmaNetworkMonitor extends TileEntityPlasma 
{
	
	public TileEntityPlasmaNetworkMonitor(World world)
	{
		this.worldObj = world;
		equalize();
	}
	
	public void equalize()
	{
		if (getPlasma() == 0) {
			if (worldObj.getBlock(xCoord + 1, yCoord, zCoord) != null) {
				eqaulizeWith(xCoord + 1, yCoord, zCoord);
			} else if (worldObj.getBlock(xCoord - 1, yCoord, zCoord) != null) {
				eqaulizeWith(xCoord - 1, yCoord, zCoord);
			} else if (worldObj.getBlock(xCoord, yCoord + 1, zCoord) != null) {
				eqaulizeWith(xCoord, yCoord + 1, zCoord);
			} else if (worldObj.getBlock(xCoord, yCoord - 1, zCoord) != null) {
				eqaulizeWith(xCoord, yCoord - 1, zCoord);
			} else if (worldObj.getBlock(xCoord, yCoord, zCoord + 1) != null) {
				eqaulizeWith(xCoord, yCoord, zCoord + 1);
			} else if (worldObj.getBlock(xCoord, yCoord, zCoord - 1) != null) {
				eqaulizeWith(xCoord, yCoord, zCoord - 1);
			}
		}
	}
	
	private void eqaulizeWith(int x, int y, int z)
	{
		if(worldObj.getBlock(x,y,z) instanceof BlockPlasmaPipe)
		{
			TileEntityPlasmaPipe tile = (TileEntityPlasmaPipe)worldObj.getTileEntity(x,y,z);
			plasmaInt = tile.getPlasma();
		}
	}
}
