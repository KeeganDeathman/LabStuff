package keegan.labstuff.jei.circuitmaker;

import keegan.labstuff.LabStuffMain;
import net.minecraft.item.Item;

public class CircuitCreationPTD 
{
	private Item drilled;
	private Item plate;
	private String designName;

	public CircuitCreationPTD(String design, Item drilled)
	{
		this.plate = LabStuffMain.itemCircuitBoardPlate;
		this.drilled = drilled;
		this.designName = design;
	}

	public Item getDrilled() {
		return drilled;
	}

	public void setDrilled(Item drilled) {
		this.drilled = drilled;
	}

	public Item getPlate() {
		return plate;
	}

	public String getDesignName() {
		return designName;
	}

	public void setDesignName(String designName) {
		this.designName = designName;
	}
	
}
