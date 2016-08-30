package keegan.labstuff.render;

import org.lwjgl.opengl.GL11;

import keegan.labstuff.models.ModelTurbineRotor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderTurbineRotor implements IItemRenderer {

	protected ModelTurbineRotor grabberModel;
	protected ResourceLocation Tex;
	
	
	public ItemRenderTurbineRotor()
	{
		grabberModel = new ModelTurbineRotor();
		Tex = new ResourceLocation("labstuff:textures/models/turbinerotor.png");
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch(type){
		case EQUIPPED: return true;
		case EQUIPPED_FIRST_PERSON: return true;
		case INVENTORY: return true;
		case ENTITY: return true;
		case FIRST_PERSON_MAP: return false;
		default: return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	 @Override
	    public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	    {
		 	switch(type)
	        {
	        	case EQUIPPED:
	            	renderEquipped(false, type, data);
	            	break;
	            case EQUIPPED_FIRST_PERSON:
	                renderEquipped(false, type, data);
	                break;
	            case INVENTORY:
	            	renderEquipped(true, type, data);
	            	break;
	            case ENTITY:
	            	renderEquipped(true, type, data);
	            	break;
	            default:
	                break;
	        }
	    }
	    
	
	private void renderEquipped(boolean inventory, ItemRenderType type,  Object... data)
    {
		
		//System.out.println(type.toString() + "," + inventory);
    	GL11.glPushMatrix();
        
        if(!inventory)
        {
        	GL11.glRotatef(-55f, 0f, 0f, 1f);
        	GL11.glRotatef(90f, 0f, 1f, 0f);
        	GL11.glRotatef(90f, 1f, 0f, 0f);
        	GL11.glRotatef(5f, 0f, 1f, 0f);
    	}
        else if(inventory)
        {
        	GL11.glRotatef(180f, 0f, 1f, 0f);
        	GL11.glTranslatef(1f, 1.5f, 0f);
        }
        
        
        float scale = 8F;
        
        GL11.glTranslatef(0.079f, -1f, -0.9f);
        GL11.glScalef(scale, scale, scale);
        if(inventory)
        {
        	if(type == type.INVENTORY)
        	{
        		//GL11.glTranslatef(7f, 0f, 0f);
        		GL11.glScalef(-2, -2, -2);
        		GL11.glRotatef(180, 1f, 0f, 0f);
        		GL11.glRotatef(-45f, 0f, 1f, 0f);
        		GL11.glRotatef(45f, 1f, 0f, 0f);

        	}
    		Minecraft.getMinecraft().renderEngine.bindTexture(Tex);
        	grabberModel.render(.03125f, false, false);
        }
        else if(!inventory)
        {
    		Minecraft.getMinecraft().renderEngine.bindTexture(Tex);
        	grabberModel.render(.03125f, false, false);
        }
        
        GL11.glPopMatrix();
    }

}
