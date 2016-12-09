package keegan.labstuff.common;

import keegan.labstuff.LabStuffMain;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class TabLabStuff extends CreativeTabs {

	public TabLabStuff(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}

	public Item getTabIconItem()
	{
		
		return Item.getItemFromBlock(LabStuffMain.blockComputer);
	}
	
	public String getTranslatedTabLabel()
	{
		
		return "LabStuff";
		
	}
}
