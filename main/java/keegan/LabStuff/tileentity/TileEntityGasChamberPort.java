package keegan.labstuff.tileentity;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.blocks.*;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileEntityGasChamberPort extends TileEntity implements IInventory, ITickable, IPlasmaHandler, ICableOutputter
{
	private ItemStack[] chestContents;
	
	public boolean input = false;
	public int testtubes;
	public int plasma;
	
	public TileEntityGasChamberPort()
	{
		chestContents = new ItemStack[1];
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
	    super.writeToNBT(nbt);

	    NBTTagList list = new NBTTagList();
	    for (int i = 0; i < this.getSizeInventory(); ++i) {
	        if (this.getStackInSlot(i) != null) {
	            NBTTagCompound stackTag = new NBTTagCompound();
	            stackTag.setByte("Slot", (byte) i);
	            this.getStackInSlot(i).writeToNBT(stackTag);
	            list.appendTag(stackTag);
	        }
	    }
	    nbt.setTag("Items", list);
	    
	    return nbt;
	}
	
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing side)
	{
		return capability == Capabilities.PLASMA_HANDLER_CAPABILITY || capability == Capabilities.CABLE_OUTPUTTER_CAPABILITY || super.hasCapability(capability, side);
	}
	

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing side)
	{
		if(capability == Capabilities.PLASMA_HANDLER_CAPABILITY
				|| capability == Capabilities.CABLE_OUTPUTTER_CAPABILITY)
		{
			return (T)this;
		}
		
		return super.getCapability(capability, side);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	    super.readFromNBT(nbt);

	    NBTTagList list = nbt.getTagList("Items", 10);
	    for (int i = 0; i < list.tagCount(); ++i) {
	        NBTTagCompound stackTag = list.getCompoundTagAt(i);
	        int slot = stackTag.getByte("Slot") & 255;
	        this.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(stackTag));
	    }

	}
	
	TileEntityGasChamberPort remoteTile;
	TileEntityGasChamberPort outputTile;
	
	@Override
	public void update()
	{
		//get input.output state
		BlockGasChamberPort me = (BlockGasChamberPort) worldObj.getBlockState(pos).getBlock();
		this.input = worldObj.getBlockState(pos.down(2)).getBlock() instanceof BlockGasChamberPort;
		if(input && worldObj.getBlockState(pos.down(2)).getBlock() instanceof BlockGasChamberPort)
		{
			//Top Port
			
			remoteTile = (TileEntityGasChamberPort)worldObj.getTileEntity(pos.down(2));
			if (this.getStackInSlot(0) != null) 
			{
				if(this.getStackInSlot(0).stackSize > 0)
				{
					remoteTile.testtubes = this.getStackInSlot(0).stackSize;
				}
			}
		}
		if(!input && worldObj.getTileEntity(pos.up(2)) instanceof TileEntityGasChamberPort)
		{
			remoteTile = (TileEntityGasChamberPort)worldObj.getTileEntity(pos.up(2));
			
			if(getLaser() != null)
			{
				if(worldObj.getBlockState(pos.west().up()) != null && worldObj.getBlockState(pos.west().up()).getBlock().equals(LabStuffMain.blockGasChamberPort))
					outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(pos.west().up());
				if(worldObj.getBlockState(pos.east().up()) != null && worldObj.getBlockState(pos.east().up()).getBlock().equals(LabStuffMain.blockGasChamberPort))
					outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(pos.east().up());
				if(worldObj.getBlockState(pos.south().up()) != null && worldObj.getBlockState(pos.south().up()).getBlock().equals(LabStuffMain.blockGasChamberPort))
					outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(pos.south().up());
				if(worldObj.getBlockState(pos.north().up()) != null && worldObj.getBlockState(pos.north().up()).getBlock().equals(LabStuffMain.blockGasChamberPort))
					outputTile = (TileEntityGasChamberPort) worldObj.getTileEntity(pos.north().up());

					
				if(testtubes > 0 && remoteTile.isMultiblock())
				{
					plasma += 500;
					
					if(worldObj.getTileEntity(pos.down()) != null)
					{
						TileEntity tileEntity = worldObj.getTileEntity(pos.down());
						if(CapabilityUtils.hasCapability(tileEntity, Capabilities.PLASMA_HANDLER_CAPABILITY, EnumFacing.UP))
						{
							
							IPlasmaHandler acceptor = CapabilityUtils.getCapability(tileEntity, Capabilities.PLASMA_HANDLER_CAPABILITY, EnumFacing.UP);
							if(acceptor.canConnectPlasma(EnumFacing.UP) && acceptor.canReceivePlasma(EnumFacing.UP))
							{
								int sent = acceptor.transferPlasma(500, EnumFacing.UP);
								plasma -= sent;
							}
						}
					}
						
					remoteTile.decrStackSize(0, 1);
					outputTile.setInventorySlotContents(0, new ItemStack(LabStuffMain.itemTestTube, outputTile.emptyTubes()+1));
					testtubes-=1;
				}
			}
		}
	}

	public Block[][][] multiblocks = new Block[3][3][3];
	private boolean multiblock = false;
	
	public boolean isMultiblock() {
		multiblock = false;
		int coreX = pos.getX();
		int coreY = pos.getY() - 1;
		int coreZ = pos.getZ();
		
		multiblocks[0][0][0] = getBlock(coreX + 1, coreY + 1, coreZ + 1);
		multiblocks[0][0][1] = getBlock(coreX, coreY + 1, coreZ + 1);
		multiblocks[0][0][2] = getBlock(coreX - 1, coreY + 1, coreZ + 1);
		multiblocks[0][1][0] = getBlock(coreX + 1, coreY + 1, coreZ);
		multiblocks[0][1][1] = getBlock(coreX, coreY + 1, coreZ);
		multiblocks[0][1][2] = getBlock(coreX - 1, coreY + 1, coreZ);
		multiblocks[0][2][0] = getBlock(coreX + 1, coreY + 1, coreZ - 1);
		multiblocks[0][2][1] = getBlock(coreX, coreY + 1, coreZ - 1);
		multiblocks[0][2][2] = getBlock(coreX - 1, coreY + 1, coreZ - 1);
		multiblocks[1][0][0] = getBlock(coreX + 1, coreY, coreZ + 1);
		multiblocks[1][0][1] = getBlock(coreX, coreY, coreZ + 1);
		multiblocks[1][0][2] = getBlock(coreX - 1, coreY, coreZ + 1);
		multiblocks[1][1][0] = getBlock(coreX + 1, coreY, coreZ);
		multiblocks[1][1][1] = getBlock(coreX, coreY, coreZ);
		multiblocks[1][1][2] = getBlock(coreX - 1, coreY, coreZ);
		multiblocks[1][2][0] = getBlock(coreX + 1, coreY, coreZ - 1);
		multiblocks[1][2][1] = getBlock(coreX, coreY, coreZ - 1);
		multiblocks[1][2][2] = getBlock(coreX - 1, coreY, coreZ - 1);
		multiblocks[2][0][0] = getBlock(coreX + 1, coreY - 1, coreZ + 1);
		multiblocks[2][0][1] = getBlock(coreX, coreY - 1, coreZ + 1);
		multiblocks[2][0][2] = getBlock(coreX - 1, coreY - 1, coreZ + 1);
		multiblocks[2][1][0] = getBlock(coreX + 1, coreY - 1, coreZ);
		multiblocks[2][1][1] = getBlock(coreX, coreY - 1, coreZ);
		multiblocks[2][1][2] = getBlock(coreX - 1, coreY - 1, coreZ);
		multiblocks[2][2][0] = getBlock(coreX + 1, coreY - 1, coreZ - 1);
		multiblocks[2][2][1] = getBlock(coreX, coreY - 1, coreZ - 1);
		multiblocks[2][2][2] = getBlock(coreX - 1, coreY - 1, coreZ - 1);
		if (multiblocks[0][0][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][0][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][0][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][1][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][1][1].equals(LabStuffMain.blockGasChamberPort)
				&& multiblocks[0][1][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][2][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][2][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[0][2][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[1][0][0].equals(LabStuffMain.blockGasChamberWall)
				&& (multiblocks[1][0][1].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][0][1].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][0][1].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][0][2].equals(LabStuffMain.blockGasChamberWall)
				&& (multiblocks[1][1][0].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][1][0].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][1][0].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][1][1].equals(Blocks.AIR)
				&& (multiblocks[1][1][2].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][1][2].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][1][2].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][2][0].equals(LabStuffMain.blockGasChamberWall)
				&& (multiblocks[1][2][1].equals(LabStuffMain.blockGasChamberWall)
						|| multiblocks[1][2][1].equals(LabStuffMain.blockElectronGrabber)
						|| multiblocks[1][2][1].equals(LabStuffMain.blockGasChamberPort))
				&& multiblocks[1][2][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][0][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][0][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][0][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][1][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][1][1].equals(LabStuffMain.blockGasChamberPort)
				&& multiblocks[2][1][2].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][2][0].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][2][1].equals(LabStuffMain.blockGasChamberWall)
				&& multiblocks[2][2][2].equals(LabStuffMain.blockGasChamberWall)) 
		{
			if (multiblocks[1][0][1].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
			if (multiblocks[1][1][0].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
			if (multiblocks[1][1][2].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
			if (multiblocks[1][2][1].equals(LabStuffMain.blockGasChamberPort))
				multiblock = true;
		}
		return multiblock;
	}

	private Block getBlock(int i, int j, int k) {
		// TODO Auto-generated method stub
		return worldObj.getBlockState(new BlockPos(i,j,k)).getBlock();
	}

	public TileEntityElectronGrabber getLaser()
	{
		TileEntityElectronGrabber laser = null;
		
		if(input)
		{
			if(worldObj.getBlockState(pos.west().down()) != null && worldObj.getBlockState(pos.west().down()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.west().down());
			if(worldObj.getBlockState(pos.east().down()) != null && worldObj.getBlockState(pos.east().down()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.east().down());
			if(worldObj.getBlockState(pos.south().down()) != null && worldObj.getBlockState(pos.south().down()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.south().down());
			if(worldObj.getBlockState(pos.north().down()) != null && worldObj.getBlockState(pos.north().down()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.north().down());
		}
		else if(worldObj.getBlockState(pos.down()).getBlock() instanceof BlockGasChamberWall && worldObj.getBlockState(pos.up()).getBlock() instanceof BlockGasChamberWall)
		{
			
			if(worldObj.getBlockState(pos.west(2)) != null && worldObj.getBlockState(pos.west(2)).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.west(2));
			if(worldObj.getBlockState(pos.east(2)) != null && worldObj.getBlockState(pos.east(2)).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.east(2));
			if(worldObj.getBlockState(pos.south(2)) != null && worldObj.getBlockState(pos.south(2)).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.south(2));
			if(worldObj.getBlockState(pos.north(2)) != null && worldObj.getBlockState(pos.north(2)).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.north(2));
			if(worldObj.getBlockState(pos.south().east()) != null && worldObj.getBlockState(pos.south().east()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.south().east());
			if(worldObj.getBlockState(pos.north().east()) != null && worldObj.getBlockState(pos.north().east()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.north().east());
			if(worldObj.getBlockState(pos.south().west()) != null && worldObj.getBlockState(pos.south().west()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.south().west());
			if(worldObj.getBlockState(pos.north().west()) != null && worldObj.getBlockState(pos.north().west()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.north().west());
		}
		else
		{
			if(worldObj.getBlockState(pos.west().up()) != null && worldObj.getBlockState(pos.west().up()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.west().up());
			if(worldObj.getBlockState(pos.east().up()) != null && worldObj.getBlockState(pos.east().up()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.east().up());
			if(worldObj.getBlockState(pos.south().up()) != null && worldObj.getBlockState(pos.south().up()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.south().up());
			if(worldObj.getBlockState(pos.north().up()) != null && worldObj.getBlockState(pos.north().up()).getBlock().equals(LabStuffMain.blockElectronGrabber))
				laser = (TileEntityElectronGrabber) worldObj.getTileEntity(pos.north().up());

			
		}
		return laser;
	}
	
	public int emptyTubes()
	{
		if(getStackInSlot(0) != null)
			return getStackInSlot(0).stackSize;
		return 0;
	}

	@Override
	public int getSizeInventory() 
	{
		// TODO Auto-generated method stub
		return chestContents.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		// TODO Auto-generated method stub
		return chestContents[slot];
	}

	@Override
	public ItemStack decrStackSize(int slot, int amt) 
	{
		// TODO Auto-generated method stub
		ItemStack stack = getStackInSlot(slot);
		if (stack != null)
		{
				if (stack.stackSize <= amt)
				{
					setInventorySlotContents(slot, null);
				}
				else
				{
					stack = stack.splitStack(amt);
					if (stack.stackSize == 0)
					{
						setInventorySlotContents(slot, null);
					}
				}
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack itemstack) 
	{
		chestContents[slot] = itemstack;
		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		
	}

	@Override
	public int getInventoryStackLimit() 
	{
		// TODO Auto-generated method stub
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) 
	{
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isItemValidForSlot(int arg0, ItemStack arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean testTubeSlot() {
		return input || (worldObj.getBlockState(pos.down()).getBlock().equals(LabStuffMain.blockGasChamberWall) && worldObj.getBlockState(pos.up()).getBlock().equals(LabStuffMain.blockGasChamberWall))
;	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Gas Chamber Port";
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
        return ItemStackHelper.getAndRemove(this.chestContents, index);
	}
	
	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {

	        for (int i = 0; i < this.chestContents.length; ++i)
	        {
	            this.chestContents[i] = null;
	        }		
	}

	@Override
	public int getPlasma(EnumFacing from) {
		// TODO Auto-generated method stub
		return plasma;
	}

	@Override
	public int drainPlasma(int amount, EnumFacing from) {
		// TODO Auto-generated method stub
		if(amount < plasma)
		{
			plasma -= amount;
			return 0;
		}
		else
		{
			int oldPlasma = plasma;
			plasma = 0;
			return amount - oldPlasma;
		}
	}


	@Override
	public boolean canReceivePlasma(EnumFacing side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canDrainPlasma(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int transferPlasma(int amount, EnumFacing from) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean canConnectPlasma(EnumFacing opposite) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setPlasma(int d, EnumFacing from) {
		plasma = d;
	}

	@Override
	public EnumSet<EnumFacing> getOutputtingSides() {
		// TODO Auto-generated method stub
		return EnumSet.of(EnumFacing.DOWN);
	}

	@Override
	public boolean canOutputTo(EnumFacing side) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
