package keegan.labstuff.tileentity;

import keegan.labstuff.blocks.*;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityRocket extends TileEntity implements ITickable, IFluidHandlerWrapper
{
	//YXZ because it's easier to initialize. Core MUST be the middle top block. Rocket MUST be odd length and odd width
	public Block[][][] rocket;
	private int speed;
	public boolean formed;
	private boolean launching;
	private int fuel;
	private FluidTank fuelBuffer;
	private String model;
	public double height;
	
	public TileEntityRocket(Block[][][] multi, int speed, int fuel, String model)
	{
		rocket = multi;
		this.speed = speed;
		this.fuel = fuel;
		fuelBuffer = new FluidTank(fuel);
		this.model = model;
	}
	
	//Fluid capability used for tools like The One Probe to view fuel levels
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
				|| super.hasCapability(capability, facing);
	}

	public CapabilityWrapperManager manager = new CapabilityWrapperManager(IFluidHandlerWrapper.class, FluidHandlerWrapper.class);

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T)manager.getWrapper(this, side);
		
		return super.getCapability(capability, side);
	}
	
	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setBoolean("launching", launching);
		tag.setDouble("height", height);
		return tag;
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(pos, 1, getUpdateTag());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		launching = pkt.getNbtCompound().getBoolean("launching");
		height = pkt.getNbtCompound().getDouble("height");
	}
	
	
	@Override
	public void update() {
		boolean tempFormed = true;
		for(int y = 0; y < rocket.length; y++)
		{
			for(int x = 0; x < rocket[y].length; x++)
			{
				for(int z = 0; z < rocket[y][x].length; z++)
				{
					int xB = x - ((rocket[y].length-1)/2);
					int yB = -y;
					int zB = z - ((rocket[y][x].length-1)/2);
					
					if(!Block.isEqualTo(worldObj.getBlockState(pos.add(xB, yB, zB)).getBlock(),rocket[y][x][z]))
					{
						tempFormed = false;
					}
				}
			}
		}
		formed = tempFormed;

		if(formed)
		{
			for(int y = 0; y < rocket.length; y++)
			{
				for(int x = 0; x < rocket[y].length; x++)
				{
					for(int z = 0; z < rocket[y][x].length; z++)
					{
						int xB = x - ((rocket[y].length-1)/2);
						int yB = -y;
						int zB = z - ((rocket[y][x].length-1)/2);
						
						if(worldObj.getBlockState(pos.add(xB,yB,zB)).getBlock() instanceof BlockRocketPart)
								worldObj.setBlockState(pos.add(xB, yB, zB), worldObj.getBlockState(pos.add(xB,yB,zB)).withProperty(BlockRocketPart.COMPLETE, true));
						else if(worldObj.getBlockState(getPos().add(xB,yB,zB)).getBlock() instanceof BlockComputerDevice)
						{
							worldObj.setBlockState(pos.add(xB, yB, zB), worldObj.getBlockState(pos.add(xB,yB,zB)).withProperty(BlockComputerDevice.VANISH, true));
						}
					}
				}
			}
			TileEntity pipe = worldObj.getTileEntity(pos.add(-((rocket[0].length+1)/2),0,0));
			if(pipe instanceof TEFuelingTower)
			{
				int empty = fuel - fuelBuffer.getFluidAmount();
				if(((TEFuelingTower)pipe).getFuelLevel() > 0 && empty > 0)
				{
					fuelBuffer.fill(((TEFuelingTower)pipe).drainFuel(((TEFuelingTower)pipe).getFuelLevel()), true);
				}
			}
			if(launching)
			{
				System.out.println("Height " + height);
				height += speed/20;
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, pos.getX(), pos.getY()-rocket.length+height, pos.getZ(), 5, .05, 5, 0);
				if((height + pos.getY() - rocket.length) >= 1000)
					epoch();
			}
		}
		else
		{
			resetRocket();
		}
	
	}
	
	public void resetRocket() {
		for(int y = 0; y < rocket.length; y++)
		{
			for(int x = 0; x < rocket[y].length; x++)
			{
				for(int z = 0; z < rocket[y][x].length; z++)
				{
					int xB = x - ((rocket[y].length-1)/2);
					int yB = -y;
					int zB = z - ((rocket[y][x].length-1)/2);
					
					if(worldObj.getBlockState(getPos().add(xB,yB,zB)).getBlock() instanceof BlockRocketPart)
					{
						worldObj.setBlockState(pos.add(xB, yB, zB), worldObj.getBlockState(pos.add(xB,yB,zB)).withProperty(BlockRocketPart.COMPLETE, false));
					}
					else if(worldObj.getBlockState(getPos().add(xB,yB,zB)).getBlock() instanceof BlockComputerDevice)
					{
						worldObj.setBlockState(pos.add(xB, yB, zB), worldObj.getBlockState(pos.add(xB,yB,zB)).withProperty(BlockComputerDevice.VANISH, false));
					}
				}
			}
		}
	}
	
	public void launch()
	{
		System.out.println("Attempting launch");
		if(fuelBuffer.getFluidAmount() == fuel)
		{
			System.out.println("Launching");
			launching = true;
			worldObj.notifyBlockUpdate(pos, worldObj.getBlockState(pos), worldObj.getBlockState(pos), 0);
		}
		else
			System.out.println("Insufficient Fuel");
	}
	
	public void epoch()
	{
		for(int y = 0; y < rocket.length; y++)
		{
			for(int x = 0; x < rocket[y].length; x++)
			{
				for(int z = 0; z < rocket[y][x].length; z++)
				{
					int xB = x - ((rocket[y].length-1)/2);
					int yB = -y;
					int zB = z - ((rocket[y][x].length-1)/2);
					
					worldObj.setBlockToAir(pos.add(xB,yB,zB));
				}
			}
		}
	}


	public String getModel() {
		// TODO Auto-generated method stub
		return model;
	}
	
	public boolean launching()
	{
		return launching;
	}

	@Override
	public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFill(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrain(EnumFacing from, Fluid fluid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FluidTankInfo[] getTankInfo(EnumFacing from) {
		// TODO Auto-generated method stub
		return new FluidTankInfo[]{fuelBuffer.getInfo()};
	}

}
