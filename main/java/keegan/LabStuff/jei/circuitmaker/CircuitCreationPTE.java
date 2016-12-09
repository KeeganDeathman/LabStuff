package keegan.labstuff.jei.circuitmaker;

import keegan.labstuff.LabStuffMain;
import net.minecraft.item.Item;

public class CircuitCreationPTE 
{
	private Item etched;
	private Item plate;
	private String designName;

	public CircuitCreationPTE(String design, Item etched)
	{
		this.plate = LabStuffMain.itemCircuitBoardPlate;
		this.etched = etched;
		this.designName = design;
	}

	public Item getEtched() {
		return etched;
	}

	public void setEtched(Item drilled) {
		this.etched = drilled;
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
