package keegan.labstuff.render;

import keegan.labstuff.models.ModelSolarPanel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemRenderSolarPanel implements IItemRenderer {

	protected ModelSolarPanel grabberModel;
	protected ResourceLocation Tex;
	
	
	public ItemRenderSolarPanel()
	{
		grabberModel = new ModelSolarPanel();
		Tex = new ResourceLocation("labstuff:textures/models/solar.png");
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
        
        
        float scale = 4F;
        
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
        	grabberModel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.03125F);
        }
        else if(!inventory)
        {
    		Minecraft.getMinecraft().renderEngine.bindTexture(Tex);
        	grabberModel.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.03125F);
        }
        
        GL11.glPopMatrix();
    }

}
