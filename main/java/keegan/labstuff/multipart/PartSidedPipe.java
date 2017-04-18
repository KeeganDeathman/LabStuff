package keegan.labstuff.multipart;

import java.util.*;

import io.netty.buffer.ByteBuf;
import keegan.labstuff.LabStuffMain;
import keegan.labstuff.common.*;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.network.*;
import keegan.labstuff.network.TransmitterType.Size;
import keegan.labstuff.util.LabStuffUtils;
import mcmultipart.MCMultiPartMod;
import mcmultipart.block.TileMultipartContainer;
import mcmultipart.multipart.*;
import mcmultipart.raytrace.*;
import mcmultipart.raytrace.RayTraceUtils.AdvancedRayTraceResultPart;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel.*;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.property.*;

public abstract class PartSidedPipe extends Multipart implements INormallyOccludingPart, /*ISlotOccludingPart, ISidedHollowConnect, JIconHitEffects, INeighborTileChange,*/ ITileNetwork, IBlockableConnection, IWrenchable, ITransmitter, ITickable
{
	public static AxisAlignedBB[] smallSides = new AxisAlignedBB[7];
	public static AxisAlignedBB[] largeSides = new AxisAlignedBB[7];

	public int delayTicks;

	public EnumFacing testingSide = null;

	public byte currentAcceptorConnections = 0x00;
	public byte currentTransmitterConnections = 0x00;

	public boolean sendDesc = false;
	public boolean redstonePowered = false;

	public boolean redstoneReactive = false;
	
	public boolean forceUpdate = true;
	
	public boolean redstoneSet = false;

	public ConnectionType[] connectionTypes = {ConnectionType.NORMAL, ConnectionType.NORMAL, ConnectionType.NORMAL, ConnectionType.NORMAL, ConnectionType.NORMAL, ConnectionType.NORMAL};
	public TileEntity[] cachedAcceptors = new TileEntity[6];

	static
	{
		smallSides[0] = new AxisAlignedBB(0.3, 0.0, 0.3, 0.7, 0.3, 0.7);
		smallSides[1] = new AxisAlignedBB(0.3, 0.7, 0.3, 0.7, 1.0, 0.7);
		smallSides[2] = new AxisAlignedBB(0.3, 0.3, 0.0, 0.7, 0.7, 0.3);
		smallSides[3] = new AxisAlignedBB(0.3, 0.3, 0.7, 0.7, 0.7, 1.0);
		smallSides[4] = new AxisAlignedBB(0.0, 0.3, 0.3, 0.3, 0.7, 0.7);
		smallSides[5] = new AxisAlignedBB(0.7, 0.3, 0.3, 1.0, 0.7, 0.7);
		smallSides[6] = new AxisAlignedBB(0.3, 0.3, 0.3, 0.7, 0.7, 0.7);

		largeSides[0] = new AxisAlignedBB(0.25, 0.0, 0.25, 0.75, 0.25, 0.75);
		largeSides[1] = new AxisAlignedBB(0.25, 0.75, 0.25, 0.75, 1.0, 0.75);
		largeSides[2] = new AxisAlignedBB(0.25, 0.25, 0.0, 0.75, 0.75, 0.25);
		largeSides[3] = new AxisAlignedBB(0.25, 0.25, 0.75, 0.75, 0.75, 1.0);
		largeSides[4] = new AxisAlignedBB(0.0, 0.25, 0.25, 0.25, 0.75, 0.75);
		largeSides[5] = new AxisAlignedBB(0.75, 0.25, 0.25, 1.0, 0.75, 0.75);
		largeSides[6] = new AxisAlignedBB(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
	}

	public static IMultipart getPartType(TransmitterType type)
	{
		switch(type)
		{
			case POWERCABLE:
				return new PartPowerCable();
			case LIQUIDPIPE:
				return new PartLiquidPipe();
			case PLASMAPIPE:
				return new PartPlasmaPipe();
			case DATACABLE:
				return new PartDataCable();
			default:
				return null;
		}
	}

	public static boolean connectionMapContainsSide(byte connections, EnumFacing side)
	{
		byte tester = (byte)(1 << side.ordinal());
		return (connections & tester) > 0;
	}

	public static byte setConnectionBit(byte connections, boolean toSet, EnumFacing side)
	{
		return (byte)((connections & ~(byte)(1 << side.ordinal())) | (byte)((toSet ? 1 : 0) << side.ordinal()));
	}

	@Override
	public void update()
	{
		if(getWorld().isRemote)
		{
			if(delayTicks == 5)
			{
				delayTicks = 6; /* don't refresh again */
				refreshConnections();
			}
			else if(delayTicks < 5)
			{
				delayTicks++;
			}
		}

		if(!getWorld().isRemote)
		{
			if(forceUpdate)
			{
				refreshConnections();
				forceUpdate = false;
			}
			
			if(sendDesc)
			{
				sendUpdatePacket();
				sendDesc = false;
			}
		}
	}
	
	public boolean handlesRedstone()
	{
		return true;
	}
	
	public boolean renderCenter()
	{
		return false;
	}
	
	public boolean transparencyRender()
	{
		return false;
	}

	public byte getPossibleTransmitterConnections()
	{
		byte connections = 0x00;

		if(handlesRedstone() && redstoneReactive && redstonePowered)
		{
			return connections;
		}

		for(EnumFacing side : EnumFacing.values())
		{
			if(canConnectMutual(side))
			{
				TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));

				if(tileEntity != null && CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite())
						&& TransmissionType.checkTransmissionType(CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite()), getTransmitterType().getTransmission())
						&& isValidTransmitter(tileEntity))
				{
					connections |= 1 << side.ordinal();
				}
			}
		}

		return connections;
	}

	public boolean getPossibleAcceptorConnection(EnumFacing side)
	{
		if(handlesRedstone() && redstoneReactive && redstonePowered)
		{
			return false;
		}

		if(canConnectMutual(side))
		{
			TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));

			if(isValidAcceptor(tileEntity, side))
			{
				if(cachedAcceptors[side.ordinal()] != tileEntity)
				{
					cachedAcceptors[side.ordinal()] = tileEntity;
					markDirtyAcceptor(side);
				}
				
				return true;
			}
		}
		
		if(cachedAcceptors[side.ordinal()] != null)
		{
			cachedAcceptors[side.ordinal()] = null;
			markDirtyAcceptor(side);
		}

		return false;
	}

	public boolean getPossibleTransmitterConnection(EnumFacing side)
	{
		if(handlesRedstone() && redstoneReactive && redstonePowered)
		{
			return false;
		}

		if(canConnectMutual(side))
		{
			TileEntity tileEntity = getWorld().getTileEntity(getPos().offset(side));
			
			if(CapabilityUtils.hasCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite())
					&& TransmissionType.checkTransmissionType(CapabilityUtils.getCapability(tileEntity, Capabilities.GRID_TRANSMITTER_CAPABILITY, side.getOpposite()), getTransmitterType().getTransmission())
					&& isValidTransmitter(tileEntity))
			{
				return true;
			}
		}

		return false;
	}

	public byte getPossibleAcceptorConnections()
	{
		byte connections = 0x00;

		if(handlesRedstone() && redstoneReactive && redstonePowered)
		{
			return connections;
		}

		for(EnumFacing side : EnumFacing.values())
		{
			if(canConnectMutual(side))
			{
				Coord4D coord = new Coord4D(getPos(), getWorld()).offset(side);
				
				if(!getWorld().isRemote && !coord.exists(getWorld()))
				{
					forceUpdate = true;
					continue;
				}
				
				TileEntity tileEntity = coord.getTileEntity(getWorld());

				if(isValidAcceptor(tileEntity, side))
				{
					if(cachedAcceptors[side.ordinal()] != tileEntity)
					{
						cachedAcceptors[side.ordinal()] = tileEntity;
						markDirtyAcceptor(side);
					}
					
					connections |= 1 << side.ordinal();
					continue;
				}
			}
			
			if(cachedAcceptors[side.ordinal()] != null)
			{
				cachedAcceptors[side.ordinal()] = null;
				markDirtyAcceptor(side);
			}
		}

		return connections;
	}

	public byte getAllCurrentConnections()
	{
		return (byte)(currentTransmitterConnections | currentAcceptorConnections);
	}
	
	protected boolean isValidTransmitter(TileEntity tileEntity)
	{
		return true;
	}
	
	@Override
	public float getHardness(PartMOP partHit)
	{
		return 3.5F;
	}

/*
	@Override
	public boolean occlusionTest(IMultipart other)
	{
		return NormalOcclusionTest.apply(this, other);
	}
*/

	@Override
	public void addSelectionBoxes(List<AxisAlignedBB> list)
	{
		if(getContainer() != null)
		{
			for(EnumFacing side : EnumFacing.values())
			{
				int ord = side.ordinal();
				byte connections = getAllCurrentConnections();

				if(connectionMapContainsSide(connections, side) || side == testingSide)
				{
					list.add(getTransmitterType().getSize() == Size.SMALL ? smallSides[ord] : largeSides[ord]);
				}
			}
		}

		list.add(getTransmitterType().getSize() == Size.SMALL ? smallSides[6] : largeSides[6]);
	}

	public abstract TransmitterType getTransmitterType();

	@Override
	public void addCollisionBoxes(AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
	{
		if(getContainer() != null)
		{
			for(EnumFacing side : EnumFacing.values())
			{
				int ord = side.ordinal();
				byte connections = getAllCurrentConnections();

				if(connectionMapContainsSide(connections, side) || side == testingSide)
				{
					AxisAlignedBB box = getTransmitterType().getSize() == Size.SMALL ? smallSides[ord] : largeSides[ord];
					if(box.intersectsWith(mask)) list.add(box);
				}
			}
		}

		AxisAlignedBB box = getTransmitterType().getSize() == Size.SMALL ? smallSides[6] : largeSides[6];
		if(box.intersectsWith(mask)) list.add(box);
	}

	@Override
	public void addOcclusionBoxes(List<AxisAlignedBB> list)
	{
		addSelectionBoxes(list);
	}
/*
	@Override
	public EnumSet<PartSlot> getSlotMask()
	{
		return EnumSet.of(PartSlot.CENTER);
	}

	@Override
	public EnumSet<PartSlot> getOccludedSlots()
	{
		return EnumSet.of(PartSlot.CENTER); //TODO implement properly
	}

	@Override
	public TextureAtlasSprite getBreakingIcon(Object subPart, EnumFacing side)
	{
		return getCenterIcon(true);
	}

	@Override
	public TextureAtlasSprite getBrokenIcon(EnumFacing side)
	{
		return getCenterIcon(true);
	}

	@Override
	public Cuboid6 getBounds()
	{
		return getTransmitterType().getSize() == Size.SMALL ? smallSides[6] : largeSides[6];
	}

	@Override
	public int getHollowSize(EnumFacing side)
	{
		EnumFacing direction = EnumFacing.getOrientation(side);

		if(connectionMapContainsSide(getAllCurrentConnections(), direction) || direction == testingSide)
		{
			return getTransmitterType().getSize().centerSize+1;
		}
		
		return 0;
	}
*/

	public abstract boolean isValidAcceptor(TileEntity tile, EnumFacing side);

	@Override
	public boolean canConnectMutual(EnumFacing side)
	{
		if(!canConnect(side)) return false;

		TileEntity tile = getWorld().getTileEntity(getPos().offset(side));
		
		if(!CapabilityUtils.hasCapability(tile, Capabilities.BLOCKABLE_CONNECTION_CAPABILITY, side.getOpposite()))
		{
			return true;
		}
		
		return CapabilityUtils.getCapability(tile, Capabilities.BLOCKABLE_CONNECTION_CAPABILITY, side.getOpposite()).canConnect(side.getOpposite());
	}

	@Override
	public boolean canConnect(EnumFacing side)
	{
		if(!redstoneSet)
		{
			if(redstoneReactive)
			{
				redstonePowered = getWorld().isBlockIndirectlyGettingPowered(getPos()) > 0;
			}
			else {
				redstonePowered = false;
			}
			
			redstoneSet = true;
		}
		
		if(handlesRedstone() && redstoneReactive && redstonePowered)
		{
			return false;
		}

		testingSide = side;
		IMultipart testPart = new OcclusionHelper.NormallyOccludingPart(getTransmitterType().getSize() == Size.SMALL ? smallSides[side.ordinal()] : largeSides[side.ordinal()]);
		boolean unblocked = OcclusionHelper.occlusionTest(testPart, (part) -> part==this, getContainer().getParts());//getContainer().canReplacePart(this, this);
		testingSide = null;
		
		return unblocked;
	}

	@Override
	public void readUpdatePacket(PacketBuffer packet)
	{
		currentTransmitterConnections = packet.readByte();
		currentAcceptorConnections = packet.readByte();

		for(int i = 0; i < 6; i++)
		{
			connectionTypes[i] = ConnectionType.values()[packet.readInt()];
		}

		notifyPartUpdate();
		markRenderUpdate();
	}

	@Override
	public void writeUpdatePacket(PacketBuffer packet)
	{
		packet.writeByte(currentTransmitterConnections);
		packet.writeByte(currentAcceptorConnections);

		for(int i = 0; i < 6; i++)
		{
			packet.writeInt(connectionTypes[i].ordinal());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbtTags)
	{
		super.readFromNBT(nbtTags);

		redstoneReactive = nbtTags.getBoolean("redstoneReactive");

		for(int i = 0; i < 6; i++)
		{
			connectionTypes[i] = ConnectionType.values()[nbtTags.getInteger("connection" + i)];
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbtTags)
	{
		super.writeToNBT(nbtTags);

		nbtTags.setBoolean("redstoneReactive", redstoneReactive);

		for(int i = 0; i < 6; i++)
		{
			nbtTags.setInteger("connection" + i, connectionTypes[i].ordinal());
		}
		
		return nbtTags;
	}

	@Override
	public boolean onActivated(EntityPlayer player, EnumHand hand, ItemStack stack, PartMOP hit)
	{
		if(stack == null)
		{
			return false;
		}

		if(LabStuffUtils.hasUsableWrench(player, getPos()) && player.isSneaking())
		{
			if(!getWorld().isRemote)
			{
				MultipartLabStuff.dropItems(this);
				getContainer().removePart(this);
			}

			return true;
		}

		return false;
	}

	@Override
	public List<ItemStack> getDrops()
	{
		return Collections.singletonList(getPickBlock(null, null));
	}

	@Override
	public ItemStack getPickBlock(EntityPlayer player, PartMOP hit)
	{
		return new ItemStack(LabStuffMain.itemPartTransmitter, 1, getTransmitterType().ordinal());
	}
	
	protected void onRefresh() {}

	public void refreshConnections()
	{
		if(redstoneReactive)
		{
			redstonePowered = getWorld().isBlockIndirectlyGettingPowered(getPos()) > 0;
		}
		else {
			redstonePowered = false;
		}
		
		redstoneSet = true;

		if(!getWorld().isRemote)
		{
			byte possibleTransmitters = getPossibleTransmitterConnections();
			byte possibleAcceptors = getPossibleAcceptorConnections();
			
			if((possibleTransmitters | possibleAcceptors) != getAllCurrentConnections())
			{
				sendDesc = true;
			}

			currentTransmitterConnections = possibleTransmitters;
			currentAcceptorConnections = possibleAcceptors;
		}
	}

	public void refreshConnections(EnumFacing side)
	{
		if(!getWorld().isRemote)
		{
			boolean possibleTransmitter = getPossibleTransmitterConnection(side);
			boolean possibleAcceptor = getPossibleAcceptorConnection(side);
			
			if((possibleTransmitter || possibleAcceptor) != connectionMapContainsSide(getAllCurrentConnections(), side))
			{
				sendDesc = true;
			}
			
			currentTransmitterConnections = setConnectionBit(currentTransmitterConnections, possibleTransmitter, side);
			currentAcceptorConnections = setConnectionBit(currentAcceptorConnections, possibleAcceptor, side);
		}
	}

	protected void onModeChange(EnumFacing side)
	{
		markDirtyAcceptor(side);
	}

	protected void markDirtyTransmitters()
	{
		notifyTileChange();
	}

	protected void markDirtyAcceptor(EnumFacing side) {}

	public abstract void onWorldJoin();
	
	public abstract void onWorldSeparate();
	
	@Override
	public void onRemoved()
	{
		onWorldSeparate();
		super.onRemoved();
	}
	
	@Override
	public void onUnloaded()
	{
		onWorldSeparate();
		super.onRemoved();
	}

	@Override
	public void onAdded()
	{
		onWorldJoin();
		super.onAdded();
		
		refreshConnections();
	}

	@Override
	public void onLoaded()
	{
		onWorldJoin();
		super.onLoaded();
	}
	
	@Override
	public void onNeighborTileChange(EnumFacing side) 
	{
		refreshConnections(side);
	}

	@Override
	public void onNeighborBlockChange(Block block)
	{
		if(handlesRedstone())
		{
			boolean prevPowered = redstonePowered;
			refreshConnections();
			
			if(prevPowered != redstonePowered)
			{
				markDirtyTransmitters();
			}
		}
		else {
			refreshConnections();
		}
	}

	@Override
	public void onPartChanged(IMultipart part)
	{
		super.onPartChanged(part);
		
		byte transmittersBefore = currentTransmitterConnections;
		refreshConnections();
		
		if(transmittersBefore != currentTransmitterConnections)
		{
			markDirtyTransmitters();
		}
	}

	@Override
	public void handlePacketData(ByteBuf dataStream) throws Exception {}

	@Override
	public ArrayList<Object> getNetworkedData(ArrayList<Object> data)
	{
		return data;
	}

	public ConnectionType getConnectionType(EnumFacing side)
	{
		return getConnectionType(side, getAllCurrentConnections(), currentTransmitterConnections, connectionTypes);
	}
	
	public static ConnectionType getConnectionType(EnumFacing side, byte allConnections, byte transmitterConnections, ConnectionType[] types)
	{
		if(!connectionMapContainsSide(allConnections, side))
		{
			return ConnectionType.NONE;
		}
		else if(connectionMapContainsSide(transmitterConnections, side))
		{
			return ConnectionType.NORMAL;
		}

		return types[side.ordinal()];
	}

	public List<EnumFacing> getConnections(ConnectionType type)
	{
		List<EnumFacing> sides = new ArrayList<EnumFacing>();

		for(EnumFacing side : EnumFacing.values())
		{
			if(getConnectionType(side) == type)
			{
				sides.add(side);
			}
		}

		return sides;
	}

	@Override
	public EnumActionResult onSneakRightClick(EntityPlayer player, EnumFacing side)
	{
		if(!getWorld().isRemote)
		{
			PartMOP hit = reTrace(getWorld(), getPos(), player);
	
			if(hit == null)
			{
				return EnumActionResult.PASS;
			}
			else {
				EnumFacing hitSide = sideHit(hit.subHit + 1);
				
				if(hitSide != null)
				{
					connectionTypes[hitSide.ordinal()] = connectionTypes[hitSide.ordinal()].next();
					sendDesc = true;

					onModeChange(EnumFacing.getFront(hitSide.ordinal()));
					player.addChatMessage(new TextComponentString("Connection type changed to " + connectionTypes[hitSide.ordinal()].toString()));

					return EnumActionResult.SUCCESS;
				}
				else {
					return onConfigure(player, 6, side);
				}
			}
		}
		
		return EnumActionResult.SUCCESS;
	}

	private PartMOP reTrace(World world, BlockPos pos, EntityPlayer player) 
	{
		Vec3d start = RayTraceUtils.getStart(player);
		Vec3d end = RayTraceUtils.getEnd(player);
		AdvancedRayTraceResultPart result = ((TileMultipartContainer)world.getTileEntity(pos)).getPartContainer().collisionRayTrace(start, end);
		
		return result == null ? null : result.hit;
	}

	protected EnumFacing sideHit(int boxIndex)
	{
		List<EnumFacing> list = new ArrayList<>();
		
		if(getContainer() != null)
		{
			for(EnumFacing side : EnumFacing.values())
			{
				int ord = side.ordinal();
				byte connections = getAllCurrentConnections();

				if(connectionMapContainsSide(connections, side))
				{
					list.add(side);
				}
			}
		}
		
		if(boxIndex < list.size()) return list.get(boxIndex);
		
		return null;
	}

	protected EnumActionResult onConfigure(EntityPlayer player, int part, EnumFacing side)
	{
		return EnumActionResult.PASS;
	}

	public EnumColor getRenderColor()
	{
		return null;
	}

	@Override
	public EnumActionResult onRightClick(EntityPlayer player, EnumFacing side)
	{
		if(!getWorld().isRemote && handlesRedstone())
		{
			redstoneReactive ^= true;
			refreshConnections();
			notifyTileChange();

			player.addChatMessage(new TextComponentString(EnumColor.DARK_BLUE + "[LabStuff]" + EnumColor.GREY + " Redstone sensitivity turned " + EnumColor.INDIGO + (redstoneReactive ? "on." : "off.")));
		}
		
		return EnumActionResult.SUCCESS;
	}

	@Override
	public ResourceLocation getModelPath()
	{
		return getType();
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		return new ExtendedBlockState(MCMultiPartMod.multipart, new IProperty[0], new IUnlistedProperty[] {OBJProperty.INSTANCE, ConnectionProperty.INSTANCE});
	}

	public List<String> getVisibleGroups()
	{
		List<String> visible = new ArrayList<>();
		
		for(EnumFacing side : EnumFacing.values())
		{
			visible.add(side.getName() + getConnectionType(side).getName().toUpperCase());
		}
		
		return visible;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state)
	{
		ConnectionProperty connectionProp = new ConnectionProperty(getAllCurrentConnections(), currentTransmitterConnections, connectionTypes, renderCenter());
		
		return ((IExtendedBlockState)state).withProperty(OBJProperty.INSTANCE, new OBJState(getVisibleGroups(), true)).withProperty(ConnectionProperty.INSTANCE, connectionProp);
	}

	@Override
	public boolean canRenderInLayer(BlockRenderLayer layer) 
	{
		return layer == BlockRenderLayer.CUTOUT || (transparencyRender() && layer == BlockRenderLayer.TRANSLUCENT);
	}

	public void notifyTileChange()
	{
		LabStuffUtils.notifyLoadedNeighborsOfTileChange(getWorld(), new Coord4D(getPos(), getWorld()));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == Capabilities.WRENCHABLE_CAPABILITY || capability == Capabilities.TILE_NETWORK_CAPABILITY || 
				capability == Capabilities.BLOCKABLE_CONNECTION_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == Capabilities.WRENCHABLE_CAPABILITY || capability == Capabilities.TILE_NETWORK_CAPABILITY 
				|| capability == Capabilities.BLOCKABLE_CONNECTION_CAPABILITY)
		{
			return (T)this;
		}
		
		return super.getCapability(capability, facing);
	}

	public static enum ConnectionType implements IStringSerializable
	{
		NORMAL,
		PUSH,
		PULL,
		NONE;

		public ConnectionType next()
		{
			if(ordinal() == values().length-1)
			{
				return NORMAL;
			}

			return values()[ordinal()+1];
		}

		@Override
		public String getName()
		{
			return name().toLowerCase();
		}
	}

	@Override
	public boolean shouldBreakingUseExtendedState() {
		return true;
	}
}