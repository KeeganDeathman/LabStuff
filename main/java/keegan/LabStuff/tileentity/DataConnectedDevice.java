package keegan.labstuff.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class DataConnectedDevice extends TileEntity implements ITickable
{
	private String id;
	private TileEntityDataCable network;
	private int tickCount;
	
	
	@Override
	public void update()
	{
		tickCount++;
		if(tickCount>=100)
		{
			tickCount=0;
			if(id == null && !worldObj.isRemote)
				registerWithNetwork();
		}
	}
	
	public void registerWithNetwork()
	{
		if(!worldObj.isRemote) {
			System.out.println("remote");
			if (worldObj.getBlockState(pos.east()).getBlock() != null && id == null) {
				register(pos.east());
			}if (worldObj.getBlockState(pos.west()).getBlock() != null && id == null) {
				register(pos.west());
			}if (worldObj.getBlockState(pos.up()).getBlock() != null && id == null) {
				register(pos.up());
			}if (worldObj.getBlockState(pos.down()).getBlock() != null && id == null) {
				register(pos.down());
			}if (worldObj.getBlockState(pos.south()).getBlock() != null && id == null) {
				register(pos.south());
			}if (worldObj.getBlockState(pos.north()).getBlock() != null && id == null) {
				register(pos.north());
			}
		}
		else
		{
			System.out.println("So remote");
		}
	}
	
	public void register(BlockPos pos)
	{
		int idPossible = 0;
		if(worldObj.getTileEntity(pos) instanceof TileEntityDataCable)
		while(id == null)
		{
			network = (TileEntityDataCable)worldObj.getTileEntity(pos);
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
	
	public String getId() {
		return id;
	}
	
	public void performAction(String command) {}
	
	public TileEntityDataCable getNetwork()
	{
		return network;
	}
}
