package keegan.labstuff.render;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

import keegan.labstuff.render.transmitter.TransmitterModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;

public class LabStuffOBJModel extends OBJModel
{
	public OBJModelType modelType;
	public ResourceLocation location;
	
	public LabStuffOBJModel(OBJModelType type, MaterialLibrary matLib, ResourceLocation modelLocation)
	{
		super(matLib, modelLocation);
		
		modelType = type;
		location = modelLocation;
	}
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		IBakedModel preBaked = super.bake(state, format, bakedTextureGetter);
		
		 if(modelType == OBJModelType.TRANSMITTER)
		{
			return new TransmitterModel(preBaked, this, state, format, TransmitterModel.getTexturesForOBJModel(preBaked), null);
		}
		 if(modelType == OBJModelType.ROCKET)
		 {
			 return new RocketModel(preBaked, this, state, format, RocketModel.getTexturesForOBJModel(preBaked), null);
		 }
		
		return null;
	}
	
	@Override
    public IModel process(ImmutableMap<String, String> customData)
    {
    	LabStuffOBJModel ret = new LabStuffOBJModel(modelType, getMatLib(), location);
        return ret;
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures)
    {
    	LabStuffOBJModel ret = new LabStuffOBJModel(modelType, getMatLib().makeLibWithReplacements(textures), location);
        return ret;
    }
	
	public static enum OBJModelType
	{
		ROCKET,
		TRANSMITTER
	}
}