package keegan.labstuff.util;

import java.util.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.PacketHandling.PacketSimple;
import keegan.labstuff.PacketHandling.PacketSimple.EnumSimplePacket;
import keegan.labstuff.common.Coord4D;
import keegan.labstuff.common.capabilities.*;
import keegan.labstuff.container.*;
import keegan.labstuff.entities.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LabStuffUtils {

	private static boolean deobfuscated;

	static {
		try {
			deobfuscated = Launch.classLoader.getClassBytes("net.minecraft.world.World") != null;
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	 public static void openCargoRocketInventory(EntityPlayerMP player, EntityCargoRocket rocket)
	    {
	        player.getNextWindowId();
	        player.closeContainer();
	        int windowId = player.currentWindowId;
	        LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_CUSTOM_GUI, LabStuffUtils.getDimensionID(player.worldObj), new Object[] { windowId, 1, rocket.getEntityId() }), player);
	        player.openContainer = new ContainerRocketInventory(player.inventory, rocket, rocket.rocketType, player);
	        player.openContainer.windowId = windowId;
	        player.openContainer.addListener(player);
	    }
	
	public static int min(int[] nums) {
		int smallest = nums[0];
		for (int i = 0; i < nums.length; i++)
			if (smallest > nums[i])
				smallest = nums[i];
		return smallest;
	}

	public static void notifyLoadedNeighborsOfTileChange(World world, Coord4D coord) {
		for (EnumFacing dir : EnumFacing.VALUES) {
			Coord4D offset = coord.offset(dir);

			if (offset.exists(world)) {
				Block block1 = offset.getBlock(world);
				block1.onNeighborChange(world, offset.getPos(), coord.getPos());

				if (offset.getBlockState(world).isNormalCube()) {
					offset = offset.offset(dir);

					if (offset.exists(world)) {
						block1 = offset.getBlock(world);

						if (block1.getWeakChanges(world, offset.getPos())) {
							block1.onNeighborChange(world, offset.getPos(), coord.getPos());
						}
					}
				}
			}
		}
	}

	public static void openLanderInv(EntityPlayerMP player, EntityLanderBase landerInv) {
		player.getNextWindowId();
		player.closeContainer();
		int windowId = player.currentWindowId;
		LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI,
				getDimensionID(player.worldObj), new Object[] { windowId, 1, landerInv.getEntityId() }), player);
		player.openContainer = new ContainerLander(player.inventory, landerInv, player);
		player.openContainer.windowId = windowId;
		player.openContainer.addListener(player);
	}

	public static ScaledResolution getScaledRes(Minecraft minecraft, int width, int height) {
		return new ScaledResolution(minecraft);
		// return VersionUtil.getScaledRes(minecraft, width, height);
	}

	public static String translate(String key) {
		String result = I18n.translateToLocal(key);
		int comment = result.indexOf('#');
		String ret = (comment > 0) ? result.substring(0, comment).trim() : result;
		for (int i = 0; i < key.length(); ++i) {
			Character c = key.charAt(i);
			if (Character.isUpperCase(c)) {
				System.err.println(ret);
			}
		}
		return ret;
	}

	public static boolean isDeobfuscated() {
		return deobfuscated;
	}

	public static int[] convertToBinary(String string) {
		ArrayList<Integer> binary = new ArrayList<Integer>();
		byte[] bytes = string.getBytes();
		for (byte b : bytes) {
			int val = b;
			for (int i = 0; i < 8; i++) {
				binary.add(((val & 128) == 0 ? 0 : 1));
				val <<= 1;
			}
		}

		int[] array = new int[binary.size()];

		for (int i = 0; i < binary.size(); i++)
			array[i] = binary.get(i);

		return array;
	}

	public static String convertToString(int[] binary) {
		return new String(encodeToByteArray(binary));
	}

	private static byte[] encodeToByteArray(int[] bits) {
		BitSet bitSet = new BitSet(bits.length);
		for (int index = 0; index < bits.length; index++) {
			bitSet.set(index, bits[index] > 0);
		}

		return bitSet.toByteArray();
	}

	public static EnumFacing getDirectionOfConnection(TileEntity connection, TileEntity current) {
		if (current.getPos().getX() != connection.getPos().getX()) {
			if (current.getPos().getX() > connection.getPos().getX()) {
				return EnumFacing.EAST;
			} else {
				return EnumFacing.WEST;
			}
		} else if (current.getPos().getY() != connection.getPos().getY()) {
			if (current.getPos().getY() > connection.getPos().getY()) {
				return EnumFacing.UP;
			} else {
				return EnumFacing.DOWN;
			}
		} else if (current.getPos().getZ() != connection.getPos().getZ()) {
			if (current.getPos().getZ() > connection.getPos().getZ()) {
				return EnumFacing.NORTH;
			} else {
				return EnumFacing.SOUTH;
			}
		}
		return null;
	}

	public static ResourceLocation getResource(ResourceType type, String name) {
		return new ResourceLocation("labstuff", type.getPrefix() + name);
	}

	public static enum ResourceType {
		GUI("gui"), GUI_ELEMENT("gui/elements"), SOUND("sound"), RENDER("render"), TEXTURE_BLOCKS(
				"textures/blocks"), TEXTURE_ITEMS("textures/items"), MODEL("models"), INFUSE("infuse");

		private String prefix;

		private ResourceType(String s) {
			prefix = s;
		}

		public String getPrefix() {
			return prefix + "/";
		}
	}

	public static boolean hasUsableWrench(EntityPlayer player, BlockPos pos) {
		ItemStack tool = player.inventory.getCurrentItem();

		if (tool == null) {
			return false;
		}

		if (tool.getItem().equals(LabStuffMain.itemWrench)) {
			return true;
		}

		return false;
	}

	public static boolean isPartAt(World world, BlockPos pos, int meta) {
		if (world.getTileEntity(pos) != null) {
			if (CapabilityUtils.getCapability(world.getTileEntity(pos), Capabilities.GRID_TRANSMITTER_CAPABILITY, null)
					.getTransmissionType().ordinal() == meta)
				return true;
		}
		return false;
	}

	public static IBlockState getStateFromItemStack(ItemStack stack) {
		if (stack == null || stack.getItem() == null)
			return null;
		Block block = getBlockFromItem(stack.getItem());
		if (block != null)
			return block.getStateFromMeta(stack.getItemDamage());
		return null;
	}

	public static Block getBlockFromItem(Item item) {
		if (item == Items.CAULDRON)
			return Blocks.CAULDRON;
		return Block.getBlockFromItem(item);
	}

	public static int getDimensionID(World world) {
		return world.provider.getDimension();
	}

	public static int getDimensionID(WorldProvider provider) {
		return provider.getDimension();
	}

	public static int getDimensionID(TileEntity tileEntity) {
		return tileEntity.getWorld().provider.getDimension();
	}

	public static void sendToAllDimensions(EnumSimplePacket packetType, Object[] data) {
		for (WorldServer world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
			int id = getDimensionID(world);
			LabStuffMain.packetPipeline.sendToDimension(new PacketSimple(packetType, id, data), id);
		}
	}

	public static String translateWithFormat(String key, Object... values) {
		String result = I18n.translateToLocalFormatted(key, values);
		int comment = result.indexOf('#');
		String ret = (comment > 0) ? result.substring(0, comment).trim() : result;
		for (int i = 0; i < key.length(); ++i) {
			Character c = key.charAt(i);
			if (Character.isUpperCase(c)) {
				System.err.println(ret);
			}
		}
		return ret;
	}

	public static void openBuggyInv(EntityPlayerMP player, IInventory buggyInv, int type) {
		player.getNextWindowId();
		player.closeContainer();
		int id = player.currentWindowId;
		LabStuffMain.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_OPEN_PARACHEST_GUI,
				player.worldObj.provider.getDimension(), new Object[] { id, 0, 0 }), player);
		player.openContainer = new ContainerBuggy(player.inventory, buggyInv, type, player);
		player.openContainer.windowId = id;
		player.openContainer.addListener(player);
	}
}
