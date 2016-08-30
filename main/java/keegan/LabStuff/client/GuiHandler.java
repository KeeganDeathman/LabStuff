package keegan.labstuff.client;


import cpw.mods.fml.common.network.IGuiHandler;
import keegan.labstuff.client.gui.*;
import keegan.labstuff.container.*;
import keegan.labstuff.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x,y,z);
		
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
		if(tileEntity instanceof TileEntityGasChamberPort)
		{
			return new ContainerGasChamberPort(player.inventory, (TileEntityGasChamberPort) tileEntity);
		}
		if(tileEntity instanceof TileEntityPowerFurnace)
		{
			return new ContainerPowerFurnace(player.inventory, (TileEntityPowerFurnace) tileEntity);
		}
		if(tileEntity instanceof TileEntityCzo)
		{
			return new ContainerCzo(player.inventory, (TileEntityCzo) tileEntity);
		}
		if(tileEntity instanceof TileEntityVent)
		{
			return new ContainerVent(player.inventory, (TileEntityVent) tileEntity);
		}
		if(tileEntity instanceof TileEntityAcceleratorControlPanel)
		{
			return new ContainerACP();
		}
		if(tileEntity instanceof TileEntityAcceleratorInterface)
		{
			return new ContainerAcceleratorInterface(player.inventory, (TileEntityAcceleratorInterface) tileEntity);
		}
		if(tileEntity instanceof TileEntityCharger)
		{
			return new ContainerCharger(player.inventory, (TileEntityCharger) tileEntity);
		}
		if(tileEntity instanceof TileEntityDLLaptop)
		{
			return null;
		}
		if(tileEntity instanceof DSCBench)
		{
			return new ContainerDSCBench(player.inventory, (DSCBench) tileEntity);
		}
		if(tileEntity instanceof DSCDrive)
		{
			return new ContainerDSCDrive(player.inventory, (DSCDrive) tileEntity);
		}
		if(tileEntity instanceof TileEntityEnricher)
		{
			return new ContainerEnricher(player.inventory, (TileEntityEnricher) tileEntity);
		}
		if(tileEntity instanceof TileEntityGravityManipulater)
		{
			return new ContainerGravity(player.inventory, (TileEntityGravityManipulater) tileEntity);
		}
		if(tileEntity instanceof TileEntityMatterCollector)
		{
			return new ContainerMatterCollector(player.inventory, (TileEntityMatterCollector) tileEntity);
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
		if(tileEntity instanceof TileEntityGasChamberPort)
		{
			return new GuiGasChamberPort(player.inventory, (TileEntityGasChamberPort) tileEntity, world);
		}
		if(tileEntity instanceof TileEntityPowerFurnace)
		{
			return new GuiPowerFurnace(player.inventory, (TileEntityPowerFurnace) tileEntity);
		}
		if(tileEntity instanceof TileEntityCzo)
		{
			return new GuiCzo(player.inventory, (TileEntityCzo) tileEntity);
		}
		if(tileEntity instanceof TileEntityVent)
		{
			return new GuiVent(player.inventory, (TileEntityVent) tileEntity);
		}
		if(tileEntity instanceof TileEntityAcceleratorControlPanel)
		{
			return new GuiACP((TileEntityAcceleratorControlPanel) tileEntity);
		}
		if(tileEntity instanceof TileEntityAcceleratorInterface)
		{
			return new GuiAcceleratorInterface(player.inventory, (TileEntityAcceleratorInterface) tileEntity);
		}
		if(tileEntity instanceof TileEntityCharger)
		{
			return new GuiCharger(player.inventory, (TileEntityCharger) tileEntity);
		}
		if(tileEntity instanceof TileEntityDLLaptop)
		{
			return new GuiDLLaptop((TileEntityDLLaptop) tileEntity, player.inventory, player);
		}
		if(tileEntity instanceof DSCBench)
		{
			return new GuiDSCBench(player.inventory, (DSCBench) tileEntity);
		}
		if(tileEntity instanceof DSCDrive)
		{
			return new GuiDSCDrive(player.inventory, (DSCDrive) tileEntity);
		}
		if(tileEntity instanceof TileEntityEnricher)
		{
			return new GuiEnricher(player.inventory, (TileEntityEnricher) tileEntity);
		}
		if(tileEntity instanceof TileEntityGravityManipulater)
		{
			return new GuiGravityManipulater((TileEntityGravityManipulater) tileEntity, player.inventory);
		}
		if(tileEntity instanceof TileEntityCzo)
		{
			return new GuiCzo(player.inventory, (TileEntityCzo) tileEntity);
		}
		if(tileEntity instanceof TileEntityMatterCollector)
		{
			return new GuiMatterCollector((TileEntityMatterCollector) tileEntity, player.inventory);
		}
		return null;
	}

}