package keegan.labstuff.render;

import java.io.IOException;
import java.util.*;

import javax.vecmath.Matrix4f;

import com.google.common.base.Function;
import com.google.common.collect.*;

import keegan.labstuff.render.LabStuffOBJModel.OBJModelType;
import keegan.labstuff.render.transmitter.TransmitterModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.*;
import net.minecraftforge.client.model.obj.OBJModel.OBJBakedModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LabStuffOBJLoader implements ICustomModelLoader
{
	public static final LabStuffOBJLoader INSTANCE = new LabStuffOBJLoader();
	
	private final Map<ResourceLocation, LabStuffOBJModel> modelCache = new HashMap<ResourceLocation, LabStuffOBJModel>();
	
	public static final ImmutableMap<String, String> flipData = ImmutableMap.of("flip-v", String.valueOf(true));
	
	public static final String[] OBJ_RENDERS = new String[] {"rocket"};
	
	@SubscribeEvent
    public void onModelBake(ModelBakeEvent event) throws IOException 
    {
		
		for(String s : OBJ_RENDERS)
		{
			ModelResourceLocation model = new ModelResourceLocation("labstuff:" + s, "inventory");
	        Object obj = event.getModelRegistry().getObject(model);
	        
	        if(obj instanceof IBakedModel)
	        {
	        	event.getModelRegistry().putObject(model, createBakedObjItemModel((IBakedModel)obj, "labstuff:models/block/" + s + ".obj.ls", new OBJModel.OBJState(Lists.newArrayList(OBJModel.Group.ALL), true), DefaultVertexFormats.ITEM));
	        }
		}
    }
	
	public OBJBakedModel createBakedObjItemModel(IBakedModel existingModel, String name, IModelState state, VertexFormat format)
	{
		try {
			Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
				@Override
				public TextureAtlasSprite apply(ResourceLocation location)
				{
					return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
				}
			};
			
			ResourceLocation modelLocation = new ResourceLocation(name);
			OBJModel objModel = (OBJModel)OBJLoader.INSTANCE.loadModel(modelLocation);
			objModel = (OBJModel)objModel.process(flipData);
			ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
			builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);
			TextureAtlasSprite missing = textureGetter.apply(new ResourceLocation("missingno"));
			
			for(String s : objModel.getMatLib().getMaterialNames())
			{
				if(objModel.getMatLib().getMaterial(s).getTexture().getTextureLocation().getResourcePath().startsWith("#"))
				{
					FMLLog.severe("OBJLoader: Unresolved texture '%s' for obj model '%s'", objModel.getMatLib().getMaterial(s).getTexture().getTextureLocation().getResourcePath(), modelLocation);
					builder.put(s, missing);
				}
				else {
					builder.put(s, textureGetter.apply(objModel.getMatLib().getMaterial(s).getTexture().getTextureLocation()));
				}
			}
			
			builder.put("missingno", missing);
			
			return new TransmitterModel(existingModel, objModel, state, format, builder.build(), new HashMap<TransformType, Matrix4f>());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	public boolean accepts(ResourceLocation modelLocation)
	{
		return modelLocation.getResourcePath().endsWith(".obj.ls");
	}
	
	@Override
	public IModel loadModel(ResourceLocation loc) throws Exception
	{
		ResourceLocation file = new ResourceLocation(loc.getResourceDomain(), loc.getResourcePath());
		
		if(!modelCache.containsKey(file))
		{
			IModel model = OBJLoader.INSTANCE.loadModel(file);
			
			if(model instanceof OBJModel)
			{
				if(file.getResourcePath().contains("transmitter"))
				{
					LabStuffOBJModel mekModel = new LabStuffOBJModel(OBJModelType.TRANSMITTER, ((OBJModel)model).getMatLib(), file);
					modelCache.put(file, mekModel);
				}
				if(file.getResourcePath().contains("rocket"))
				{
					LabStuffOBJModel lsModel = new LabStuffOBJModel(OBJModelType.ROCKET, ((OBJModel)model).getMatLib(), file);
					modelCache.put(file, lsModel);
				}
			}
		}
		
		LabStuffOBJModel mekModel = modelCache.get(file);
		
		if(mekModel == null)
		{
			return ModelLoaderRegistry.getMissingModel();
		}
		
		return mekModel;
	}
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager)
    {
		modelCache.clear();
    }
}