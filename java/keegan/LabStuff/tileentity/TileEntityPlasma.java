package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.BlockPlasmaPipe;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlasma extends TileEntity 
{
	
	protected int plasmaInt = 0;
	
	public void addPlasma(int addition, TileEntity issuer)
	{
		plasmaInt+=addition;
			if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityPlasma
					&& worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != issuer
					&& worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(xCoord + 1,
						yCoord, zCoord)).addPlasma(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityPlasma
					&& worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != issuer
					&& worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(xCoord - 1,
						yCoord, zCoord)).addPlasma(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityPlasma
					&& worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != issuer
					&& worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(xCoord,
						yCoord + 1, zCoord)).addPlasma(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityPlasma
					&& worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != issuer
					&& worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(xCoord,
						yCoord - 1, zCoord)).addPlasma(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityPlasma
					&& worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != issuer
					&& worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord,
						zCoord + 1)).addPlasma(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityPlasma
					&& worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != issuer
					&& worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord,
						zCoord - 1)).addPlasma(plasmaInt, this);
			}
	}
	
	public void subtractPlasma(int subtraction, TileEntity issuer)
	{
		plasmaInt-=subtraction;
		if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null)
		{
			((TileEntityPlasma)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).subtractPlasma(plasmaInt, this);
		}
		if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null)
		{
			((TileEntityPlasma)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).subtractPlasma(plasmaInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null)
		{
			((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).subtractPlasma(plasmaInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null)
		{
			((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).subtractPlasma(plasmaInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null)
		{
			((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).subtractPlasma(plasmaInt, this);
		}
		if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null)
		{
			((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).subtractPlasma(plasmaInt, this);
		}
	}
	//ONLY call when the block is added!
	public void equalize()
	{
		if(worldObj.getBlock(xCoord + 1, yCoord, zCoord) instanceof BlockPlasmaPipe)
		{
			plasmaInt = ((TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)).getPlasma();
		}
		else if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) instanceof BlockPlasmaPipe)
		{
			plasmaInt = ((TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)).getPlasma();
		}
		else if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) instanceof BlockPlasmaPipe)
		{
			plasmaInt = ((TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).getPlasma();
		}
		else if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) instanceof BlockPlasmaPipe)
		{
			plasmaInt = ((TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).getPlasma();
		}
		else if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) instanceof BlockPlasmaPipe)
		{
			plasmaInt = ((TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)).getPlasma();
		}
		else if(worldObj.getBlock(xCoord - 1, yCoord, zCoord) instanceof BlockPlasmaPipe)
		{
			plasmaInt = ((TileEntityPlasmaPipe)worldObj.getTileEntity(xCoord, yCoord, zCoord - 1)).getPlasma();
		}
	}
	
	public int getPlasma()
	{
		return plasmaInt;
	}
}
