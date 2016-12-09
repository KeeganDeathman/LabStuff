package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.BlockPlasmaPipe;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityPlasma extends TileEntity implements ITickable
{
	
	protected int plasmaInt = 0;
	
	public TileEntityPlasma()
	{
		super();
	}
	
	
	public void addPlasma(int addition, TileEntity issuer)
	{
		plasmaInt+=addition;
		if (worldObj.getTileEntity(pos.add(1,0,0))  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.add(1,0,0)) != issuer && worldObj.getTileEntity(pos.add(1,0,0)) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(pos.add(1,0,0))).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(pos.west())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.west()) != issuer && worldObj.getTileEntity(pos.west()) != null) 
		{
				((TileEntityPlasma) worldObj.getTileEntity(pos.west())).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(pos.up())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.up()) != issuer && worldObj.getTileEntity(pos.up()) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(pos.up())).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(pos.down())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.down()) != issuer && worldObj.getTileEntity(pos.down()) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(pos.down())).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(pos.south())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.south()) != issuer && worldObj.getTileEntity(pos.south()) != null) 
		{
			((TileEntityPlasma) worldObj.getTileEntity(pos.south())).addPlasmaNetworked(plasmaInt, this);
		}
		if (worldObj.getTileEntity(pos.north()) instanceof TileEntityPlasma && worldObj.getTileEntity(pos.north()) != issuer && worldObj.getTileEntity(pos.north()) != null) {
				((TileEntityPlasma) worldObj.getTileEntity(pos.north())).addPlasmaNetworked(plasmaInt, this);
		}
	}
	
	public void addPlasmaNetworked(int addition, TileEntityPlasma issuer)
	{
		if(plasmaInt + addition == issuer.getPlasma())
		{
			plasmaInt+=addition;
			if (worldObj.getTileEntity(pos.add(1,0,0))  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.add(1,0,0)) != issuer && worldObj.getTileEntity(pos.add(1,0,0)) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.add(1,0,0))).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.west())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.west()) != issuer && worldObj.getTileEntity(pos.west()) != null) 
			{
					((TileEntityPlasma) worldObj.getTileEntity(pos.west())).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.up())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.up()) != issuer && worldObj.getTileEntity(pos.up()) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.up())).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.down())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.down()) != issuer && worldObj.getTileEntity(pos.down()) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.down())).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.south())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.south()) != issuer && worldObj.getTileEntity(pos.south()) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.south())).addPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.north()) instanceof TileEntityPlasma && worldObj.getTileEntity(pos.north()) != issuer && worldObj.getTileEntity(pos.north()) != null) {
					((TileEntityPlasma) worldObj.getTileEntity(pos.north())).addPlasmaNetworked(plasmaInt, this);
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
			if (worldObj.getTileEntity(pos.add(1,0,0))  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.add(1,0,0)) != issuer && worldObj.getTileEntity(pos.add(1,0,0)) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.add(1,0,0))).subtractPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.west())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.west()) != issuer && worldObj.getTileEntity(pos.west()) != null) 
			{
					((TileEntityPlasma) worldObj.getTileEntity(pos.west())).subtractPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.up())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.up()) != issuer && worldObj.getTileEntity(pos.up()) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.up())).subtractPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.down())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.down()) != issuer && worldObj.getTileEntity(pos.down()) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.down())).subtractPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.south())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.south()) != issuer && worldObj.getTileEntity(pos.south()) != null) 
			{
				((TileEntityPlasma) worldObj.getTileEntity(pos.south())).subtractPlasmaNetworked(plasmaInt, this);
			}
			if (worldObj.getTileEntity(pos.north()) instanceof TileEntityPlasma && worldObj.getTileEntity(pos.north()) != issuer && worldObj.getTileEntity(pos.north()) != null) {
					((TileEntityPlasma) worldObj.getTileEntity(pos.north())).subtractPlasmaNetworked(plasmaInt, this);
			}
		}
	}
	
	public void subtractPlasmaNetworked(int subtraction, TileEntityPlasma issuer)
	{
		if(plasmaInt >= subtraction)
		{
			if(plasmaInt - subtraction == issuer.getPlasma())
			{
				if (worldObj.getTileEntity(pos.add(1,0,0))  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.add(1,0,0)) != issuer && worldObj.getTileEntity(pos.add(1,0,0)) != null) 
				{
					((TileEntityPlasma) worldObj.getTileEntity(pos.add(1,0,0))).subtractPlasmaNetworked(plasmaInt, this);
				}
				if (worldObj.getTileEntity(pos.west())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.west()) != issuer && worldObj.getTileEntity(pos.west()) != null) 
				{
					((TileEntityPlasma) worldObj.getTileEntity(pos.west())).subtractPlasmaNetworked(plasmaInt, this);
				}
				if (worldObj.getTileEntity(pos.up())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.up()) != issuer && worldObj.getTileEntity(pos.up()) != null) 
				{
					((TileEntityPlasma) worldObj.getTileEntity(pos.up())).subtractPlasmaNetworked(plasmaInt, this);
				}
				if (worldObj.getTileEntity(pos.down())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.down()) != issuer && worldObj.getTileEntity(pos.down()) != null) 
				{
					((TileEntityPlasma) worldObj.getTileEntity(pos.down())).subtractPlasmaNetworked(plasmaInt, this);
				}
				if (worldObj.getTileEntity(pos.south())  instanceof TileEntityPlasma && worldObj.getTileEntity(pos.south()) != issuer && worldObj.getTileEntity(pos.south()) != null) 
				{
					((TileEntityPlasma) worldObj.getTileEntity(pos.south())).subtractPlasmaNetworked(plasmaInt, this);
				}
				if (worldObj.getTileEntity(pos.north()) instanceof TileEntityPlasma && worldObj.getTileEntity(pos.north()) != issuer && worldObj.getTileEntity(pos.north()) != null) {
					((TileEntityPlasma) worldObj.getTileEntity(pos.north())).subtractPlasmaNetworked(plasmaInt, this);
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
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setInteger("plasma", this.plasmaInt);
		
		return tag;
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


	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
