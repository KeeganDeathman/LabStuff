package keegan.labstuff.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import keegan.labstuff.container.*;
import keegan.labstuff.tileentity.*;
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
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
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
		
		return null;
	}

}
