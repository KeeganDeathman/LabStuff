package keegan.labstuff.render.transmitter;

import java.util.*;

import javax.vecmath.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import keegan.labstuff.multipart.*;
import keegan.labstuff.multipart.PartSidedPipe.ConnectionType;
import keegan.labstuff.render.OBJBakedModelBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.*;
import net.minecraftforge.common.model.*;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TransmitterModel extends OBJBakedModelBase
{
	private static Set<TransmitterModel> modelInstances = new HashSet<TransmitterModel>();
	
	private Map<Integer, List<BakedQuad>> modelCache = new HashMap<Integer, List<BakedQuad>>();
	private TransmitterModel itemCache;
	
	private IBlockState tempState;
	private ItemStack tempStack;
	
	private static TextureAtlasSprite[] transporter_center = new TextureAtlasSprite[2];
	private static TextureAtlasSprite[] transporter_center_color = new TextureAtlasSprite[2];
	private static TextureAtlasSprite[] transporter_side = new TextureAtlasSprite[2];
	private static TextureAtlasSprite[] transporter_side_color = new TextureAtlasSprite[2];
	
	private TransmitterOverride override = new TransmitterOverride();
	
	public TransmitterModel(IBakedModel base, OBJModel model, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textures, HashMap<TransformType, Matrix4f> transform)
	{
		super(base, model, state, format, textures, transform);
		
		modelInstances.add(this);
	}
	
	private class TransmitterOverride extends ItemOverrideList 
    {
		public TransmitterOverride() 
		{
			super(Lists.newArrayList());
		}

	    @Override
	    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) 
	    {
			if(itemCache == null)
			{
				List<String> visible = new ArrayList<String>();
				
				for(EnumFacing side : EnumFacing.values())
				{
					visible.add(side.getName() + (side.getAxis() == Axis.Y ? "NORMAL" : "NONE"));
				}
				
				itemCache = new TransmitterModel(baseModel, getModel(), new OBJState(visible, true), vertexFormat, textureMap, transformationMap);
				itemCache.tempStack = stack;
			}
			
			return itemCache;
	    }
    }
	
	@Override
	public ItemOverrideList getOverrides()
	{
		return override;
	}
	
	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
	{
    	if(side != null) 
    	{
    		return ImmutableList.of();
    	}
    	
    	if(state != null && tempState == null)
    	{
			IExtendedBlockState extended = (IExtendedBlockState)state;
			BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
			int color = -1;
			
			
			OBJState obj = extended.getValue(OBJProperty.INSTANCE);
			
			if(layer != BlockRenderLayer.TRANSLUCENT)
			{
				color = -1;
			}
			
			int hash = Objects.hash(layer.ordinal(), color, ConnectionProperty.INSTANCE.valueToString(extended.getValue(ConnectionProperty.INSTANCE)));
			
			if(obj.getVisibilityMap().containsKey(Group.ALL) || obj.getVisibilityMap().containsKey(Group.ALL_EXCEPT))
	        {
	            updateStateVisibilityMap(obj);
	        }
			
			if(!modelCache.containsKey(hash))
			{
				TransmitterModel model = new TransmitterModel(baseModel, getModel(), obj, vertexFormat, textureMap, transformationMap);
				model.tempState = state;
				modelCache.put(hash, model.getQuads(state, side, rand));
			}
			
			return modelCache.get(hash);
    	}
    	
    	return super.getQuads(state, side, rand);
	}
	
	@Override
	public float[] getOverrideColor(Face f, String groupName)
	{
		if(tempState != null)
		{
			
			if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT)
			{
				return new float[] {1,0,0, 1};
			}
		}
		
		return null;
	}
	
	@Override
	public TextureAtlasSprite getOverrideTexture(Face f, String groupName)
	{
		if(tempState != null)
		{
			EnumFacing side = EnumFacing.getFacingFromVector(f.getNormal().x, f.getNormal().y, f.getNormal().z);
			ConnectionProperty connection = ((IExtendedBlockState)tempState).getValue(ConnectionProperty.INSTANCE);
			boolean sideIconOverride = getIconStatus(side, connection) > 0;
			
			if(MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.TRANSLUCENT)
			{
				return (!sideIconOverride && f.getMaterialName().contains("Center")) ? transporter_center[0] : transporter_side[0];
			}
			else {
				if(groupName.endsWith("NONE") && sideIconOverride)
				{
					for(String s : getModel().getMatLib().getMaterialNames())
					{
						if(s.contains("Texture.Name"))
						{
							continue;
						}
						
						if(!s.contains("Center") && !s.contains("Centre") && !s.contains("Opaque"))
						{
							return textureMap.get(s);
						}
					}
				}
				else {
				}
			}
		}
		
		return null;
	}
	
	@Override
	public boolean shouldRotate(Face f, String groupName)
	{
		if(tempState != null)
		{
			EnumFacing side = EnumFacing.getFacingFromVector(f.getNormal().x, f.getNormal().y, f.getNormal().z);
			ConnectionProperty connection = ((IExtendedBlockState)tempState).getValue(ConnectionProperty.INSTANCE);
			
			if(groupName.endsWith("NONE") && getIconStatus(side, connection) == 2)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public byte getIconStatus(EnumFacing side, ConnectionProperty connection)
	{
		ConnectionType type = PartSidedPipe.getConnectionType(side, connection.connectionByte, connection.transmitterConnections, connection.connectionTypes);

		if(type == ConnectionType.NONE)
		{
			if(connection.renderCenter)
			{
				return (byte)0;
			}
			else if(connection.connectionByte == 3 && side != EnumFacing.DOWN && side != EnumFacing.UP)
			{
				return (byte)1;
			}
			else if(connection.connectionByte == 12 && (side == EnumFacing.DOWN || side == EnumFacing.UP))
			{
				return (byte)1;
			}
			else if(connection.connectionByte == 12 && (side == EnumFacing.EAST || side == EnumFacing.WEST))
			{
				return (byte)2;
			}
			else if(connection.connectionByte == 48 && side != EnumFacing.EAST && side != EnumFacing.WEST)
			{
				return (byte)2;
			}
		}
		
		return (byte)0;
	}
	
	public static Map<TransformType, TRSRTransformation> transforms = ImmutableMap.<TransformType, TRSRTransformation>builder()
            .put(TransformType.GUI,                         get(0, 0, 0, 30, 225, 0, 0.625f))
            .put(TransformType.THIRD_PERSON_RIGHT_HAND,     get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(TransformType.THIRD_PERSON_LEFT_HAND,      get(0, 2.5f, 0, 75, 45, 0, 0.375f))
            .put(TransformType.FIRST_PERSON_RIGHT_HAND,     get(0, 0, 0, 0, 45, 0, 0.4f))
            .put(TransformType.FIRST_PERSON_LEFT_HAND,      get(0, 0, 0, 0, 225, 0, 0.4f))
            .put(TransformType.GROUND,                      get(0, 2, 0, 0, 0, 0, 0.25f))
            .put(TransformType.HEAD,                        get(0, 0, 0, 0, 0, 0, 1))
            .put(TransformType.FIXED,                       get(0, 0, 0, 0, 0, 0, 1))
            .put(TransformType.NONE,                        get(0, 0, 0, 0, 0, 0, 0))
            .build();
	
	 private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s) 
	    {
	        return new TRSRTransformation(
	            new Vector3f(tx / 16, ty / 16, tz / 16),
	            TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
	            new Vector3f(s, s, s),
	            null);
	    }
	
    @Override
    public Pair<? extends IPerspectiveAwareModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) 
    {
        return Pair.of(this, transforms.get(cameraTransformType).getMatrix());
    }
	
	public static void registerIcons(TextureMap map)
	{}
	
	public static void clearCache()
	{
		for(TransmitterModel model : modelInstances)
		{
			model.modelCache.clear();
			model.itemCache = null;
		}
	}
}