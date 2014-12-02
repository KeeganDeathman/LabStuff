package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.BlockPlasmaPipe;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlasma extends TileEntity 
{
	
	protected int plasmaInt = 0;
	
	public TileEntityPlasma()
	{
		super();
	}
	
	
	public void addPlasma(int addition, TileEntity issuer)
	{
		plasmaInt+=addition;
		if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
		{
				((TileEntityPlasma) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1)).addPlasmaNetworked(plasmaInt, this);
		}
	}
	
	public void addPlasmaNetworked(int addition, TileEntityPlasma issuer)
	{
		if(plasmaInt + addition == issuer.getPlasma())
		{
			plasmaInt+=addition;
			if (worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord + 1, yCoord, zCoord) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(xCoord + 1, yCoord, zCoord)).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord - 1, yCoord, zCoord) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(xCoord - 1, yCoord, zCoord)).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord + 1, zCoord) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord + 1, zCoord)).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord - 1, zCoord) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord - 1, zCoord)).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)  instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord + 1) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord, zCoord + 1)).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord - 1) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(xCoord, yCoord, zCoord - 1)).addPlasmaNetworked(plasmaInt, this);
			}
		}
		else
		{
			int diff = issuer.getPlasma() - plasmaInt;
			if(diff > 0)
			{
				this.addPlasmaNetworked(diff, issuer);
			}
		}
	}
	
	public void subtractPlasma(int subtraction, TileEntity issuer)
	{
		if(plasmaInt >= subtraction)
		{
			plasmaInt-=subtraction;
			if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null)
			{
				((TileEntityPlasma)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
			}
			if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null)
			{
				((TileEntityPlasma)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null)
			{
				((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null)
			{
				((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null)
			{
				((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).subtractPlasmaNetworked(plasmaInt, this);
			}
			if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null)
			{
				((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).subtractPlasmaNetworked(plasmaInt, this);
			}
		}
	}
	
	public void subtractPlasmaNetworked(int subtraction, TileEntityPlasma issuer)
	{
		if(plasmaInt >= subtraction)
		{
			if(plasmaInt - subtraction == issuer.getPlasma())
			{
				plasmaInt-=subtraction;
				if(worldObj.getTileEntity(xCoord+1, yCoord, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord+1, yCoord, zCoord) != null)
				{
					((TileEntityPlasma)worldObj.getTileEntity(xCoord+1, yCoord, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
				}
				if(worldObj.getTileEntity(xCoord-1, yCoord, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != issuer && worldObj.getTileEntity(xCoord-1, yCoord, zCoord) != null)
				{
					((TileEntityPlasma)worldObj.getTileEntity(xCoord-1, yCoord, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
				}
				if(worldObj.getTileEntity(xCoord, yCoord+1, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord+1, zCoord) != null)
				{
					((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord+1, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
				}
				if(worldObj.getTileEntity(xCoord, yCoord-1, zCoord) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != issuer && worldObj.getTileEntity(xCoord, yCoord-1, zCoord) != null)
				{
					((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord-1, zCoord)).subtractPlasmaNetworked(plasmaInt, this);
				}
				if(worldObj.getTileEntity(xCoord, yCoord, zCoord+1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord+1) != null)
				{
					((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord, zCoord+1)).subtractPlasmaNetworked(plasmaInt, this);
				}
				if(worldObj.getTileEntity(xCoord, yCoord, zCoord-1) instanceof TileEntityPlasma && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != issuer && worldObj.getTileEntity(xCoord, yCoord, zCoord-1) != null)
				{
					((TileEntityPlasma)worldObj.getTileEntity(xCoord, yCoord, zCoord-1)).subtractPlasmaNetworked(plasmaInt, this);
				}
			}
			else
			{
				int diff = plasmaInt - issuer.getPlasma();
				if(diff > 0)
				{
					this.subtractPlasmaNetworked(diff, issuer);
				}
			}
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("plasma", this.plasmaInt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		plasmaInt = tag.getInteger("plasma");
	}
	
	public int getPlasma()
	{
		return plasmaInt;
	}
}
