package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;

public class DSCPart extends TileEntity implements ITickable
{
	private String id;
	private TileEntityRibbonCable network;
	private int tickCount;
	
	
	@Override
	public void update()
	{
		if(id == null && !worldObj.isRemote)
			registerWithNetwork();
	}
	
	public void registerWithNetwork()
	{
		if(!worldObj.isRemote) {
			for(EnumFacing side : EnumFacing.VALUES)
			{
				if (worldObj.getBlockState(pos.offset(side)).getBlock() != null && id == null) {
					register(pos.offset(side));
				}
			}
		}
	}
	
	public void register(BlockPos pos)
	{
		int idPossible = 0;
		if(worldObj.getTileEntity(pos) instanceof TileEntityRibbonCable)
		{
			while(id == null)
			{
				network = (TileEntityRibbonCable)worldObj.getTileEntity(pos);
				if(network.getDeviceById("_" + idPossible) != null)
				{
					idPossible += 1;
				}
				else
				{
					this.id = "_" + idPossible;
					network.addDevice(this);
				}
			}
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void performAction(String command, DSCPart sender) {}
	
	public TileEntityRibbonCable getNetwork()
	{
		return network;
	}
}
