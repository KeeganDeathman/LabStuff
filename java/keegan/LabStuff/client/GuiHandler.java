package keegan.labstuff.client;

import keegan.labstuff.container.ContainerCircuitDesignTable;
import keegan.labstuff.container.ContainerCircuitMaker;
import keegan.labstuff.container.ContainerComputer;
import keegan.labstuff.container.ContainerElectrifier;
import keegan.labstuff.tileentity.TileEntityCircuitDesignTable;
import keegan.labstuff.tileentity.TileEntityCircuitMaker;
import keegan.labstuff.tileentity.TileEntityComputer;
import keegan.labstuff.tileentity.TileEntityElectrifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if(tileEntity instanceof TileEntityCircuitDesignTable)
		{
			return new ContainerCircuitDesignTable(player.inventory, (TileEntityCircuitDesignTable) tileEntity);
		}
		if(tileEntity instanceof TileEntityCircuitMaker)
		{
			return new ContainerCircuitMaker(player.inventory, (TileEntityCircuitMaker) tileEntity);
		}
		if(tileEntity instanceof TileEntityComputer)
		{
			return new ContainerComputer(player.inventory, (TileEntityComputer) tileEntity);
		}
		if(tileEntity instanceof TileEntityElectrifier)
		{
			return new ContainerElectrifier(player.inventory, (TileEntityElectrifier) tileEntity);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return getGui(ID, player, world, x, y, z);
	}
	
	public static Object getGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if(tileEntity instanceof TileEntityCircuitDesignTable)
		{
			return new GuiCircuitDesignTable(player.inventory, (TileEntityCircuitDesignTable) tileEntity);
		}
		if(tileEntity instanceof TileEntityCircuitMaker)
		{
			return new GuiCircuitMaker(player.inventory, (TileEntityCircuitMaker) tileEntity);
		}
		if(tileEntity instanceof TileEntityComputer)
		{
			return new GuiComputer(player.inventory, (TileEntityComputer) tileEntity, player);
		}
		if(tileEntity instanceof TileEntityElectrifier)
		{
			return new GuiElectrifier(player.inventory, (TileEntityElectrifier) tileEntity, world);
		}
		
		return null;
	}

}
