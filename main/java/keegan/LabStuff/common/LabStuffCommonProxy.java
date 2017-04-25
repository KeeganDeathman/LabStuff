package keegan.labstuff.common;

import keegan.labstuff.util.Vector3;
import net.minecraft.entity.player.*;
import net.minecraft.item.Item;
import net.minecraft.network.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LabStuffCommonProxy 
{
	
	public void registerRenders()
	{
		
	}
	
	public void initMod()
	{
		
	}

	public void preInit() {
		// TODO Auto-generated method stub
	}

	public void registerItemModel(Item item) {
		// TODO Auto-generated method stub
		
	}
	
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object[] otherInfo)
    {
    }
	
	public void registerFluidModels(){}
	
	public EntityPlayer getPlayer(MessageContext context)
	{
		return context.getServerHandler().playerEntity;
	}
	
	public void handlePacket(Runnable runnable, EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP)
		{
			((WorldServer)player.worldObj).addScheduledTask(runnable);
		}
	}

	public World getWorldForID(int dimensionID) 
	{
		MinecraftServer theServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (theServer == null)
		{
			return null;
		}
		return theServer.worldServerForDimension(dimensionID);
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler handler)
    {
        if (handler instanceof NetHandlerPlayServer)
        {
            return ((NetHandlerPlayServer) handler).playerEntity;
        }
        else
        {
            return null;
        }
    }


}
