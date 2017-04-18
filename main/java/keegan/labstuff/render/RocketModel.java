package keegan.labstuff.render;

import java.util.*;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.*;

import keegan.labstuff.common.EnumColor;
import keegan.labstuff.render.transmitter.TransmitterModel;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.obj.OBJModel.Face;
import net.minecraftforge.common.model.*;

public class RocketModel extends OBJBakedModelBase
{
	private static Map<Block, List<BakedQuad>> rocketCache = new HashMap<Block, List<BakedQuad>>();
	private static Map<Integer, RocketModel> rocketItemCache = new HashMap<Integer, RocketModel>();
	
	private IBlockState tempState;
	private ItemStack tempStack;
	
	private RocketOverride override = new RocketOverride();
	
	public RocketModel(IBakedModel base, OBJModel model, IModelState state, VertexFormat format, ImmutableMap<String, TextureAtlasSprite> textures, HashMap<TransformType, Matrix4f> transform)
	{
		super(base, model, state, format, textures, transform);
	}

	public static void forceRebake()
	{
		rocketCache.clear();
		rocketItemCache.clear();
	}
	
	public EnumColor getColor()
	{
		return EnumColor.WHITE;
	}
	
    private class RocketOverride extends ItemOverrideList 
    {
		public RocketOverride() 
		{
			super(Lists.newArrayList());
		}

	    @Override
	    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) 
	    {
			if(rocketItemCache.containsKey(stack.getItemDamage()))
			{
				return rocketItemCache.get(stack.getItemDamage());
			}

			ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
			builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);
			TextureAtlasSprite missing = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(new ResourceLocation("missingno").toString());

			for(String s : getModel().getMatLib().getMaterialNames())
			{
				TextureAtlasSprite sprite = null;
				
				if(sprite == null)
				{
					sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getModel().getMatLib().getMaterial(s).getTexture().getTextureLocation().toString());
				}
				
				if(sprite == null)
				{
					sprite = missing;
				}
				
				builder.put(s, sprite);
			}
			
			builder.put("missingno", missing);
			RocketModel bakedModel = new RocketModel(baseModel, getModel(), getState(), vertexFormat, builder.build(), transformationMap);
			bakedModel.tempStack = stack;
			rocketItemCache.put(stack.getItemDamage(), bakedModel);
			
			return bakedModel;
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
	    	Block hash = state.getBlock();
			
			if(!rocketCache.containsKey(hash))
			{
				RocketModel model = new RocketModel(baseModel, getModel(), getState(), vertexFormat, textureMap, transformationMap);
				model.tempState = state;
				rocketCache.put(hash, model.getQuads(state, side, rand));
			}
			
			return rocketCache.get(hash);
    	}
    	
    	return super.getQuads(state, side, rand);
	}
	
	@Override
	public float[] getOverrideColor(Face f, String groupName)
	{
		if(groupName.equals("light"))
		{
			EnumColor c = getColor();
			return new float[] {c.getColor(0), c.getColor(1), c.getColor(2), 1};
		}
		
		return null;
	}

	private Pair<IPerspectiveAwareModel, Matrix4f> thirdPersonTransform;
    
	
    @Override
    public Pair<? extends IPerspectiveAwareModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType transformType) 
    {
    	if(transformType == TransformType.GUI)
    	{
    		GlStateManager.rotate(180, 1, 0, 0);
    		ForgeHooksClient.multiplyCurrentGlMatrix(TransmitterModel.transforms.get(transformType).getMatrix());
    		GlStateManager.translate(0.65F, 0.45F, 0.0F);
    		GlStateManager.rotate(90, 1, 0, 0);
    		GlStateManager.scale(1.6F, 1.6F, 1.6F);
    		
    		return Pair.of(this, null);
    	}
    	else if(transformType == TransformType.FIRST_PERSON_RIGHT_HAND || transformType == TransformType.FIRST_PERSON_LEFT_HAND)
    	{
    		GlStateManager.translate(0.0F, 0.2F, 0.0F);
    	}
    	else if(transformType == TransformType.THIRD_PERSON_RIGHT_HAND || transformType == TransformType.THIRD_PERSON_LEFT_HAND) 
        {
    		ForgeHooksClient.multiplyCurrentGlMatrix(TransmitterModel.transforms.get(transformType).getMatrix());
        	GlStateManager.translate(0.0F, 0.3F, 0.2F);
        	
        	return Pair.of(this, null);
        }
        
        return Pair.of(this, TransmitterModel.transforms.get(transformType).getMatrix());
    }
}