package keegan.labstuff.jei.circuitmaker;

import net.minecraft.item.Item;

public class CircuitCreationETC 
{
	private Item etched;
	private Item circuit;
	private String designName;

	public CircuitCreationETC(String design, Item etched, Item circuit)
	{
		this.etched = etched;
		this.circuit = circuit;
		this.designName = design;
	}

	public Item getEtched() {
		return etched;
	}

	public void getEtched(Item etched) {
		this.etched = etched;
	}

	public Item getCircuit() {
		return circuit;
	}

	public void setCircuit(Item circuit) {
		this.circuit = circuit;
	}

	public String getDesignName() {
		return designName;
	}

	public void setDesignName(String designName) {
		this.designName = designName;
	}
	
}
