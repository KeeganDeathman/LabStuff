package keegan.labstuff.client;


import keegan.labstuff.client.gui.*;
import keegan.labstuff.common.capabilities.LSPlayerStats;
import keegan.labstuff.container.*;
import keegan.labstuff.entities.EntityTieredRocket;
import keegan.labstuff.tileentity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		LSPlayerStats stats = LSPlayerStats.get(player);

        if (ID == 36 && player.getRidingEntity() instanceof EntityTieredRocket)
        {
            return new ContainerRocketInventory(player.inventory, (EntityTieredRocket) player.getRidingEntity(), ((EntityTieredRocket) player.getRidingEntity()).getType(), player);
        }
        else if (ID == 37)
        {
            return new ContainerExtendedInventory(player, stats.getExtendedInventory());
        }
		
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x,y,z));
		
		if(tileEntity instanceof TileEntityCircuitDesignTable)
		{
			return new ContainerCircuitDesignTable(player.inventory, (TileEntityCircuitDesignTable) tileEntity);
		}
		if(tileEntity instanceof TileEntityCircuitMaker)
		{
			return new ContainerCircuitMaker(player.inventory, (TileEntityCircuitMaker) tileEntity);
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
		if(tileEntity instanceof TileEntityWorkbench)
		{
			return new ContainerWorkbench(player.inventory, (TileEntityWorkbench) tileEntity);
		}
		if(tileEntity instanceof Chloralakizer)
		{
			return new ContainerChloralakizer(player.inventory, (Chloralakizer) tileEntity);
		}
		if(tileEntity instanceof ReactionChamber)
		{
			return new ContainerReactionChamber(player.inventory, (ReactionChamber) tileEntity);
		}
		if(tileEntity instanceof AlloySmelter)
		{
			return new ContainerAlloySmelter(player.inventory, (AlloySmelter) tileEntity);
		}
		if(tileEntity instanceof Squeezer)
		{
			return new ContainerSqueezer(player.inventory, (Squeezer) tileEntity);
		}
		if(tileEntity instanceof Fermenter)
		{
			return new ContainerFermenter(player.inventory, (Fermenter) tileEntity);
		}
		if(tileEntity instanceof IBM650Punch)
		{
			return new ContainerIBM650Punch(player.inventory, (IBM650Punch) tileEntity);
		}
		if(tileEntity instanceof IBM650Console)
		{
			return new ContainerIBM650Console(player.inventory, (IBM650Console) tileEntity);
		}
		if(tileEntity instanceof KeyPunch)
			return new ContainerKeyPunch(player.inventory, (KeyPunch) tileEntity);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return getGui(ID, player, world, x, y, z);
	}
	
	public static Object getGui(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(tileEntity instanceof TileEntityCircuitDesignTable)
		{
			return new GuiCircuitDesignTable(player.inventory, (TileEntityCircuitDesignTable) tileEntity);
		}
		if(tileEntity instanceof TileEntityCircuitMaker)
		{
			return new GuiCircuitMaker(player.inventory, (TileEntityCircuitMaker) tileEntity);
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
		if(tileEntity instanceof TileEntityWorkbench)
		{
			return new GuiWorkbench(player.inventory, (TileEntityWorkbench) tileEntity);
		}
		if(tileEntity instanceof Chloralakizer)
		{
			return new GuiChloralakizer(player.inventory, (Chloralakizer) tileEntity);
		}
		if(tileEntity instanceof ReactionChamber)
		{
			return new GuiReactionChamber(player.inventory, (ReactionChamber) tileEntity);
		}
		if(tileEntity instanceof AlloySmelter)
		{
			return new GuiAlloySmelter(player.inventory, (AlloySmelter) tileEntity);
		}
		if(tileEntity instanceof Squeezer)
		{
			return new GuiSqueezer(player.inventory, (Squeezer) tileEntity);
		}
		if(tileEntity instanceof Fermenter)
		{
			return new GuiFermenter(player.inventory, (Fermenter) tileEntity);
		}
		if(tileEntity instanceof IBM650Punch)
		{
			return new GuiIBM650Punch(player.inventory, (IBM650Punch) tileEntity);
		}
		if(tileEntity instanceof KeyPunch && ID == 24)
			return new GuiKeyPunch(player.inventory, (KeyPunch)tileEntity);
		if(tileEntity instanceof KeyPunch && ID == 29)
			return new GuiKeyPunchManual(player.inventory, (KeyPunch)tileEntity);
		if(tileEntity instanceof KeyPunch && ID == 30)
			return new GuiKeyPunchRead(player.inventory, (KeyPunch)tileEntity);
		if(tileEntity instanceof IBM650Console)
		{
			return new GuiIBM650Console(player.inventory, (IBM650Console) tileEntity);
		}
		if(ID == 27)
			return new GuiPunchCard(player);
		if(ID == 28)
			return new GuiPunchDeck(player);
		return null;
	}

}