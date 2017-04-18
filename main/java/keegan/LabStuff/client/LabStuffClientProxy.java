package keegan.labstuff.client;

import java.util.*;

import com.google.common.collect.*;

import keegan.labstuff.LabStuffMain;
import keegan.labstuff.client.fx.EffectHandler;
import keegan.labstuff.common.LabStuffCommonProxy;
import keegan.labstuff.container.InventoryExtended;
import keegan.labstuff.entities.*;
import keegan.labstuff.items.IMetaItem;
import keegan.labstuff.multipart.*;
import keegan.labstuff.render.*;
import keegan.labstuff.render.transmitter.*;
import keegan.labstuff.tileentity.*;
import keegan.labstuff.util.*;
import keegan.labstuff.world.ColorHandler;
import keegan.labstuff.wrappers.*;
import mcmultipart.client.multipart.MultipartRegistryClient;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.*;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class LabStuffClientProxy extends LabStuffCommonProxy
{
	
	public static Set<BlockVec3> valueableBlocks = Sets.newHashSet();
    public static HashSet<BlockMetaList> detectableBlocks = Sets.newHashSet();
    public static List<BlockVec3> leakTrace;
    public static Map<String, PlayerGearData> playerItemData = Maps.newHashMap();
    public static double playerPosX;
    public static double playerPosY;
    public static double playerPosZ;
    public static float playerRotationYaw;
    public static float playerRotationPitch;
    public static boolean lastSpacebarDown;

    public static HashMap<Integer, Integer> clientSpaceStationID = Maps.newHashMap();
    public static MusicTicker.MusicType MUSIC_TYPE_MARS;
    public static EnumRarity galacticraftItem = EnumHelper.addRarity("GCRarity", TextFormatting.BLUE, "Space");
    public static Map<String, String> capeMap = new HashMap<>();
    public static InventoryExtended dummyInventory = new InventoryExtended();
    public static Map<Fluid, ResourceLocation> submergedTextures = Maps.newHashMap();
    private static Map<String, ResourceLocation> capesMap = Maps.newHashMap();
    public static IPlayerClient playerClientHandler = new PlayerClient();
    public static Minecraft mc = FMLClientHandler.instance().getClient();
    public static List<String> gearDataRequests = Lists.newArrayList();
    public static DynamicTextureProper overworldTextureClient;
    public static DynamicTextureProper overworldTextureWide;
    public static DynamicTextureProper overworldTextureLarge;
    public static boolean overworldTextureRequestSent;
    public static boolean overworldTexturesValid;
    public static float PLAYER_Y_OFFSET = 1.6200000047683716F;
    public static final ResourceLocation saturnRingTexture = new ResourceLocation("labstuff:textures/celestialbodies/saturn_rings.png");
    public static final ResourceLocation uranusRingTexture = new ResourceLocation("labstuff:textures/celestialbodies/uranus_rings.png");
    private static List<Item> itemsToRegisterJson = Lists.newArrayList();

    
    @Override
    public void spawnParticle(String particleID, Vector3 position, Vector3 motion, Object[] otherInfo)
    {
        EffectHandler.spawnParticle(particleID, position, motion, otherInfo);
    }
	
	@Override
	public void registerRenders()
	{
		//TileEntityrenderers
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWindTurbine.class, new TileEntityRenderWindTurbine());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntitySolenoidAxel.class, new RenderSolenoid());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityToroid.class, new RenderToroid());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAcceleratorTube.class, new RenderAcceleratorTube());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAcceleratorDetectorCore.class, new RenderAcceleratorDetector());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRibbonCable.class, new RenderRibbonCable());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTurbine.class, new RenderTurbineRotor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMatterCollector.class, new RenderMatterCollector());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRocket.class, new RenderRocket());
		
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartLiquidPipe.class, new RenderLiquidPipe());
		MultipartRegistryClient.bindMultipartSpecialRenderer(PartPlasmaPipe.class, new RenderPlasmaPipe());

	}
	
	@Override
	public void registerFluidModels() {
		LabStuffMain.labstuffFluidBlocks.forEach(this::registerFluidModel);
	}

	private void registerFluidModel(IFluidBlock fluidBlock) {
		final Item item = Item.getItemFromBlock((Block) fluidBlock);
		assert item != null;

		ModelBakery.registerItemVariants(item);

		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation("labstuff:fluid", fluidBlock.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}
	

	public World getWorldForID(int id)
	{
		  World world = mc.theWorld;

	        if (world != null && LabStuffUtils.getDimensionID(world) == id)
	        {
	            return world;
	        }

	        return null;
	}

	
	public static void registerItemRender(String domain, Item item)
	{
		if(item instanceof IMetaItem)
		{
			IMetaItem metaItem = (IMetaItem)item;
			List<ModelResourceLocation> variants = new ArrayList<ModelResourceLocation>();
			
			for(int i = 0; i < metaItem.getVariants(); i++)
			{
				if(metaItem.getTexture(i) == null)
				{
					continue;
				}
				
				ModelResourceLocation loc = new ModelResourceLocation(domain + ":" + metaItem.getTexture(i).toLowerCase(), "inventory");
				ModelLoader.setCustomModelResourceLocation(item, i, loc);
				variants.add(loc);
				ModelBakery.registerItemVariants(item, new ResourceLocation(domain + ":" + metaItem.getTexture(i)));
			}
			
			return;
		}
		
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@Override
	public void preInit()
	{
		OBJLoader.INSTANCE.addDomain("labstuff");	
		
		ModelLoaderRegistry.registerLoader(LabStuffOBJLoader.INSTANCE);
		
		MinecraftForge.EVENT_BUS.register(LabStuffOBJLoader.INSTANCE);
		
		ClientTickHandler tickHandlerClient = new ClientTickHandler();
        MinecraftForge.EVENT_BUS.register(tickHandlerClient);

		
		registerItemRender("labstuff", LabStuffMain.itemPartTransmitter);
		
		LabStuffRenderer.init();

	}
	
	@Override
	public void registerItemModel(Item item)
	{
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	@Override
	public void initMod()
	{
		ColorHandler.init();
	}
	
	@Override
	public EntityPlayer getPlayer(MessageContext context)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isServer())
		{
			return context.getServerHandler().playerEntity;
		}
		else {
			return Minecraft.getMinecraft().thePlayer;
		}
	}
	
	@Override
	public void handlePacket(Runnable runnable, EntityPlayer player)
	{
		if(player == null || player.worldObj.isRemote)
		{
			Minecraft.getMinecraft().addScheduledTask(runnable);
		}
		else if(player != null && !player.worldObj.isRemote)
		{
			((WorldServer)player.worldObj).addScheduledTask(runnable); //singleplayer
		}
	}
	
	interface MeshDefinitionFix extends ItemMeshDefinition {
		ModelResourceLocation getLocation(ItemStack stack);

		// Helper method to easily create lambda instances of this class
		static ItemMeshDefinition create(MeshDefinitionFix lambda) {
			return lambda;
		}

		default ModelResourceLocation getModelLocation(ItemStack stack) {
			return getLocation(stack);
		}
	}

}
