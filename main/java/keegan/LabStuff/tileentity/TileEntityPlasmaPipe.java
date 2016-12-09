package keegan.labstuff.tileentity;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.*;
import net.minecraft.util.math.BlockPos;
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
	public void update()
	{
		if(!networked)
			equalize();
		worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(BlockPlasmaPipe.PLASMA, (plasmaInt > 0)));
	}
	
	//ONLY call when the block is added!
	public void equalize()
	{
		if(!worldObj.isRemote) {
			if (worldObj.getBlockState(pos.east()) != null && getPlasma() == 0) {
				eqaulizeWith(pos.east());
			}if (worldObj.getBlockState(pos.west()) != null && getPlasma() == 0) {
				eqaulizeWith(pos.west());
			}if (worldObj.getBlockState(pos.up()) != null && getPlasma() == 0) {
					eqaulizeWith(pos.up());
			}if (worldObj.getBlockState(pos.down()) != null && getPlasma() == 0) {
				 eqaulizeWith(pos.down());
			}if (worldObj.getBlockState(pos.south()) != null && getPlasma() == 0) {
				 eqaulizeWith(pos.south());
			}if (worldObj.getBlockState(pos.north()) != null && getPlasma() == 0) {
				 eqaulizeWith(pos.north());
			}
		}
	}
	
	private void eqaulizeWith(BlockPos pos)
	{
		if(worldObj.getBlockState(pos).getBlock() == LabStuffMain.blockPlasmaPipe)
		{
			TileEntityPlasmaPipe tile = (TileEntityPlasmaPipe)worldObj.getTileEntity(pos);
			plasmaInt = tile.getPlasma();
			if(getPlasma() > 0)
				networked = true;
		}
	}
}
