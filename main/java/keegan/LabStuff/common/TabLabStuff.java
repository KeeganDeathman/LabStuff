package keegan.labstuff.common;


import keegan.labstuff.LabStuffMain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;

public class TabLabStuff extends CreativeTabs {

	
	private String name;
	
	public TabLabStuff(String label, String name) {
		super(label);
		this.name = name;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	public String getTranslatedTabLabel()
	{
		
		return name;
		
	}

	@Override
	public Item getTabIconItem() {
		if(this.equals(LabStuffMain.tabLabStuff))
			return LabStuffMain.itemTestTube;
		if(this.equals(LabStuffMain.tabLabStuffSpace))
			return Item.getItemFromBlock(LabStuffMain.sputnik);
		if(this.equals(LabStuffMain.tabLabStuffComputers))
			return Item.getItemFromBlock(LabStuffMain.radioKit);
		return null;
	}
}
